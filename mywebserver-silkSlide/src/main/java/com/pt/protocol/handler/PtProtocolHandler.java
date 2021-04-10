package com.pt.protocol.handler;

import com.pt.config.SilkSideSystemConfig;
import com.pt.model.CommResult;
import com.pt.protocol.Message;
import com.pt.protocol.Serialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 12:23
 * 自定义协议编码解码处理器
 */
public class PtProtocolHandler extends ByteToMessageCodec<Message> {
    /**
     * 编码方法
     * @param channelHandlerContext
     * @param message
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        // 设置魔数
        byteBuf.writeBytes(message.getMagicNo());
        // 协议版本
        byteBuf.writeInt(message.getVersion());
        // 序列化算法
        byteBuf.writeInt(message.getSerialization());
        // 协议类型
        byteBuf.writeInt(message.getMessageType());
        // 消息正文长度
        byteBuf.writeInt(message.getMessageLength());
        // 消息正文
        CommResult messageBody = message.getMessageBody();
        byte[] bytes = Serialize.Realization.values()[message.getSerialization()].beanCastBytes(messageBody);
        byteBuf.writeBytes(bytes);
    }

    /**
     * 解码方法
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 读取魔数
        Message message = new Message();
        byte[] magicNo = new byte[SilkSideSystemConfig.getMagicNo().length];
        byteBuf.readBytes(magicNo);
        message.setMagicNo(magicNo);
        // 读取协议版本
        message.setVersion(byteBuf.readInt());
        // 读取序列化算法
        int setSerialization = byteBuf.readInt();
        message.setSerialization(setSerialization);
        // 协议类型
        message.setMessageType(byteBuf.readInt());
        // 消息正文长度
        int contentLength = byteBuf.readInt();
        message.setMessageLength(contentLength);
        // 读取消息正文
        byte[] bytes = new byte[contentLength];
        byteBuf.readBytes(bytes);
        CommResult commResult = Serialize.Realization.values()[setSerialization].bytesCastBean(bytes, CommResult.class);
        message.setMessageBody(commResult);
        list.add(message);
    }
}
