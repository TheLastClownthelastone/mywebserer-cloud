package com.pt.protocol;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/5 12:43
 * 自定义序列化算法实现接口
 */
public interface Serialize {

    /**
     * 将byte数组转换成java对象
     * @param bytes
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T bytesCastBean(byte[] bytes,Class<T> tClass);

    /**
     * 将对象转成java对象
     * @param o
     * @return
     */
    byte[] beanCastBytes(Object o);

    /**
     * 定义枚举用来实现对应的序列化算法
     */
    @Slf4j
    enum Realization implements Serialize {
        /**
         * java实现序列化网络传输通过对象流实现
         */
        Java{
            @Override
            public <T> T bytesCastBean(byte[] bytes, Class<T> tClass) {
                ObjectInputStream objectInput = null;
                ByteArrayInputStream byteArrayInputStream = null;
                try{
                    byteArrayInputStream = new ByteArrayInputStream(bytes);
                    objectInput = new ObjectInputStream(byteArrayInputStream);
                    return (T)objectInput.readObject();
                }catch (Exception e){
                    log.error("[反序列化失败]",e);
                }finally {
                    IOUtils.closeQuietly(objectInput);
                    IOUtils.closeQuietly(byteArrayInputStream);
                }
                return null;
            }

            @Override
            public byte[] beanCastBytes(Object o) {
                ObjectOutputStream objectOutputStream = null;
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(o);
                    return byteArrayOutputStream.toByteArray();
                }catch (Exception e){
                    log.error("[序列化失败]",e);
                }finally {
                    IOUtils.closeQuietly(objectOutputStream);
                    IOUtils.closeQuietly(byteArrayOutputStream);
                }
                return new byte[0];
            }
        },
        /**
         * json实现默认序列化算法
         */
        Json{
            @Override
            public <T> T bytesCastBean(byte[] bytes, Class<T> tClass) {
                String json = new String(bytes, Charset.forName("UTF-8"));
                Gson gson = new Gson();
                return gson.fromJson(json, tClass);
            }

            @Override
            public byte[] beanCastBytes(Object o) {
                Gson gson = new Gson();
                String s = gson.toJson(o);
                return s.getBytes(Charset.forName("UTF-8"));
            }
        },
        /**
         * 通过google公司推出的protobuf实现序列化算法
         */
        Protobuf{
            @Override
            public <T> T bytesCastBean(byte[] bytes, Class<T> tClass) {
                return null;
            }

            @Override
            public byte[] beanCastBytes(Object o) {
                return new byte[0];
            }
        }
    }
}
