package com.pt.reflex;

import cn.hutool.core.lang.ClassScaner;
import com.pt.reflex.a.MyInterface;
import com.pt.reflex.b.MyInterfaceImpl;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 16:06
 */
public class Test1 {

    @Test
    public void test1(){
        Class<MyInterface> myInterfaceClass = MyInterface.class;
        Set<Class<?>> classes = ClassScaner.scanPackage();
        classes.stream().filter(c -> myInterfaceClass.isAssignableFrom(c) && !c.equals(myInterfaceClass)).forEach(c->{
            System.out.println(c);
        });
    }
    @Test
    public void test2(){
        Class<MyInterfaceImpl> myInterfaceClass = MyInterfaceImpl.class;
    }
}
