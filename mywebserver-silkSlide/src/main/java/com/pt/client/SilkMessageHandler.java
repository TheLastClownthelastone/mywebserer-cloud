package com.pt.client;
import com.pt.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.FullBulkStringRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * netty 自定义客户端集成 ChannelDuplexHandler 该处理
 * netty 自定义服务器集成 SimpleChannelInboundHandler
 */
public class SilkMessageHandler extends SimpleChannelInboundHandler<Message>
{

    private CountDownLatch latch;

    private Message result;

    public SilkMessageHandler(CountDownLatch latch)
    {
        this.latch = latch;
    }

    /**
     * 读取服务器发送过来消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        //latch.countDown();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        this.result = msg;
        latch.countDown();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("执行异常了 :"+cause.getMessage());
    }

    public void resetLatch(CountDownLatch latch){
        this.latch = latch;
    }

    public Message getResult(){
        return this.result;
    }

}
