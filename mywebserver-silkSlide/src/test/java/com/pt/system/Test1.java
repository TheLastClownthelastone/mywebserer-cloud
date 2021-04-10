package com.pt.system;

import com.pt.config.SilkSideSystemConfig;
import org.junit.Test;


/**
 * @author pt
 * @version 1.0
 * @date 2021/4/4 14:19
 * 测试jdk中System类
 */
public class Test1 {


    @Test
    public void test1(){
        SilkSideSystemConfig.setPort(8080);
        int port = SilkSideSystemConfig.getPort();
        System.out.println(port);

    }


}
