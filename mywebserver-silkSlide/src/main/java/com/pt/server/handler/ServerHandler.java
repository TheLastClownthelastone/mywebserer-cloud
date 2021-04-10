package com.pt.server.handler;

import com.pt.context.SilkWorker;
import com.pt.model.CommResult;
import com.pt.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 13:47
 * 服务器端业务处理
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    Map<String,Object> map = SilkWorker.getInstantiationMap();

    /**
     * 服务受到消息的时候进行处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        //  执行方法返回结果
        CommResult messageBody = msg.getMessageBody();
        Map<String, Object> data = (Map<String, Object>) messageBody.getData();
        String name = (String) data.get("name");
        Object[] params = (Object[]) data.get("params");
        String methodName = (String) data.get("methodName");

        Object o = map.get(name);
        Method excute = Arrays.stream(o.getClass().getDeclaredMethods()).filter(method -> methodName.equals(method.getName()) && params.length == method.getParameterCount()).findFirst().get();
        Object invoke = excute.invoke(o, params);
        Message message = Message.buildMessage(invoke);
        ctx.channel().writeAndFlush(message);
    }
}
