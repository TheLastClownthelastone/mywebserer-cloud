package com.pt.context;

import cn.hutool.core.lang.ClassScaner;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.pt.anno.Instantiation;
import com.pt.anno.SilkClient;
import com.pt.model.CommResult;
import com.pt.protocol.Message;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 13:54
 */
public class SilkWorker {

    /**
     * 远程调用的map
     */
    private static Map<String,Object> rpcMap = new HashMap();

    private static Map<String,Object> instantiationMap = new HashMap<>();

    private SilkWorker(){

    }


    /**
     * 出事化方法
     */
    private void init(){
        Set<Class<?>> collect = ClassScaner.scanPackage();
        // 扫描客户端的包
        scanSilkClient(collect);
        // 扫描远程调用实现接口
        scanInstantiation(collect);

    }

    private void scanSilkClient(Set<Class<?>> collect) {
        //将含有@SilkClient的接口全部扫描
        collect.stream().filter(c -> c.isAnnotationPresent(SilkClient.class) && c.isInterface()).collect(Collectors.toSet());
        // 通过cglib动态代理技术给接口进行实例化
        for (Class<?> aClass : collect) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(aClass);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    // 将对应的方法进行写出
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",aClass.getSimpleName());
                    map.put("params",objects);
                    map.put("methodName",method.getName());
                    Message message = Message.buildMessage(map);
                    Message message1 = com.pt.client.SilkClient.getInstance().sendMessage(message);
                    CommResult messageBody = message1.getMessageBody();
                    Object data = messageBody.getData();
                    return data;
                }
            });
        }
    }


    private void  scanInstantiation(Set<Class<?>> collect){
        Set<Class<?>> instantiationClasses = collect.stream().filter(c -> c.isAnnotationPresent(Instantiation.class) && !c.isInterface()).collect(Collectors.toSet());
        for (Class<?> instantiationClass : instantiationClasses) {
            Instantiation annotation = instantiationClass.getAnnotation(Instantiation.class);
            String value = annotation.value();

            try {
                Object o = instantiationClass.newInstance();
                if (StringUtils.isNotEmpty(value)) {
                    instantiationMap.put(value,o);
                }else {
                    String simpleName = Arrays.stream(instantiationClass.getInterfaces()).filter(c -> c.isAssignableFrom(instantiationClass)).findFirst().get().getSimpleName();
                    instantiationMap.put(simpleName,o);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public static Map<String,Object> getInstantiationMap(){
        return instantiationMap;
    }

}
