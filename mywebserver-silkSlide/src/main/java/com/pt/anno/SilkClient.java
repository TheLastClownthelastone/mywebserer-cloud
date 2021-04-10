package com.pt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 14:04
 * 作用于需要执行远程调用额接口上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SilkClient {
    /**
     * 应用名称
     * @return
     */
    String name();
}
