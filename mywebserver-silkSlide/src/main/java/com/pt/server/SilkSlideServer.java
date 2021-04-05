package com.pt.server;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/5 13:13
 * 服务器端
 */
public class SilkSlideServer {

    public SilkSlideServer(){

    }

    /**
     * 服务器初始化操作
     */
    private void init(){
        // 指定服务器中额线程数量为一个
        EventLoopGroup boss = new NioEventLoopGroup(1);
        // 指定服务器中工作线程的数量
        EventLoopGroup worker = new NioEventLoopGroup();
    }

}
