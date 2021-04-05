package com.pt.config;

import cn.hutool.core.convert.Convert;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/4 14:15
 * 系统常量设置类
 */
public class SilkSideSystemConfig {

    private static final String PORT = "silkSlide.port";


    /**
     * 返回绑定的系统端口
     * @return
     */
    public static int getPort(){
        String property = System.getProperty("silkSlide.port");
        return Convert.toInt(property);
    }

    /**
     * @param port 端口
     * 设置端口存放在java应用全局变量中
     */
    public static void setPort(int port){
        System.setProperty(PORT,String.valueOf(port));
    }

}
