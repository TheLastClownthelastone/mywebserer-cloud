package com.pt.client;
import com.pt.config.SilkSideSystemConfig;
import com.pt.protocol.Message;
import com.pt.protocol.handler.PtProtocolHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;

import java.util.concurrent.CountDownLatch;

/**
 * 管道初始化
 * 泛型加入管道类型
 */
public class SilkHandlerInitialization extends ChannelInitializer<Channel>
{

    private CountDownLatch latch;

    private SilkMessageHandler handler;

    public SilkHandlerInitialization(CountDownLatch latch)
    {
        this.latch = latch;
    }
    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        // 通过管道获取通道
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, SilkSideSystemConfig.getLengthIndex(),4,0,0));
        // 增加自定义自定义协议协议处理器
        pipeline.addLast(new PtProtocolHandler());
        // silk 自定义业务处理器
        this.handler = new SilkMessageHandler(this.latch);
        pipeline.addLast(this.handler);
    }

    public void resetLathc(CountDownLatch latch){
        handler.resetLatch(latch);
    }

    public Message getResult(){
        return handler.getResult();
    }
}
