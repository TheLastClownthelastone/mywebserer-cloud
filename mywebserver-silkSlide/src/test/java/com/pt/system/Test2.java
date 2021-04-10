package com.pt.system;

import cn.hutool.core.lang.ClassScaner;
import com.pt.anno.SilkClient;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 14:47
 */
public class Test2 {

    @Test
    public void test1(){
        Set<Class<?>> classes = ClassScaner.scanPackage();
        Set<Class<?>> collect = classes.stream().filter(c -> c.isAnnotationPresent(SilkClient.class) && c.isInterface()).collect(Collectors.toSet());
        for (Class<?> aClass : collect) {
            System.out.println(aClass.getName());
        }
        System.out.println(collect);
    }
}
