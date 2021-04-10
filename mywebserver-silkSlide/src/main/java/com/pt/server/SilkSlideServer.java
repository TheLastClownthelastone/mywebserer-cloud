package com.pt.server;

import com.pt.config.SilkSideSystemConfig;
import com.pt.protocol.handler.PtProtocolHandler;
import com.pt.server.handler.ServerHandler;
import com.pt.util.YmlUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Data;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/5 13:13
 * 服务器端
 */
@Data
public class SilkSlideServer {

    /**
     * 设置帧的长度
     */
    private int frameLength;

    private int port = YmlUtil.getInt("server.port");



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

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.channel(NioServerSocketChannel.class)
                    .group(boss,worker)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 防止tcp半包粘包情况制定的handler,将消息制定为网络传输中的帧作为单位
                            // 第一个参数表还是帧的最大长度，第二个是长度的偏移量（长度字节所在的位置）,表示长度的字节大小（int 4个字节，long 8个字节） 第三个最后是长度与内容要拨开几个字节
                            // 最后一个参数表示需要将协议长度到内容中多余字节移除的个数
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,SilkSideSystemConfig.getLengthIndex(),4,0,0));
                            // 增加自定义自定义协议协议处理器
                            pipeline.addLast(new PtProtocolHandler());
                            // 增加请求调用方法
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.bind().sync();
            // 开启异步关闭通道
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}
