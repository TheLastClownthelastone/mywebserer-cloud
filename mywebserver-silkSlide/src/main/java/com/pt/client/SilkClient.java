package com.pt.client;

import com.pt.protocol.Message;
import com.pt.util.YmlUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 17:19
 */
public class SilkClient {
    // redis 服务器主机
    private String host;
    // redis 服务器的端口
    private int port;
    // 增加线程阻塞获取值
    private CountDownLatch lathc;
    // 线程组
    private EventLoopGroup group;
    // 管道初始化
    private SilkHandlerInitialization silkHandlerInitialization;
    // 程序启动器
    private Bootstrap bootstrap;
    // 异步通道
    private ChannelFuture cf;

    private static class SingletonHolder {
        static final SilkClient instance = new SilkClient();
    }

    public static SilkClient getInstance(){
        return SingletonHolder.instance;
    }

    private SilkClient()
    {
        this.host = YmlUtil.getString("rpc.host");
        this.port = YmlUtil.getInt("rpc.port");
        lathc = new CountDownLatch(0);
        silkHandlerInitialization = new SilkHandlerInitialization(lathc);
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(silkHandlerInitialization);

    }


    /**
     * 创建redis连接
     */
    public void conn(){
        try
        {
            this.cf = bootstrap.connect(host,port).sync();
            System.out.println("【服务器连接成功】》》》》》》》》》");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
        }
    }

    /**
     * 获取cf对象
     * @return
     */
    public ChannelFuture getChannelFuture(){
        if (this.cf == null)
        {
            this.conn();
        }
        if (!this.cf.channel().isActive())
        {
            this.conn();
        }
        return this.cf;
    }

    /**
     * redis客户端发送数据
     * @param message
     * @return
     * @throws InterruptedException
     */
    public Message sendMessage(Message message) throws InterruptedException
    {
        ChannelFuture channelFuture = getInstance().getChannelFuture();
        channelFuture.channel().writeAndFlush(message);

        lathc = new CountDownLatch(1);
        silkHandlerInitialization.resetLathc(lathc);
        lathc.await();
        return silkHandlerInitialization.getResult();
    }
}
