package com.pt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 16:45
 * 作用远程调用实现类上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Instantiation {
    String value() default "";
}
