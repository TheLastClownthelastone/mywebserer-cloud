package com.pt.protocol;

import com.pt.config.SilkSideSystemConfig;
import com.pt.model.CommResult;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/5 12:11
 * 自定义协议父类
 */
@Data
public class Message<T extends Serializable>{

    /**
     * 魔数
     * 作为协议的开头用来区分协议的不同
     */
    private byte[] magicNo;

    /**
     * 协议的版本
     */
    private int version;

    /**
     * 序列化算法
     */
    private int serialization;

    /**
     * 协议的类型
     */
    private int messageType;

    /**
     * 消息正文的长度
     */
    private int messageLength;

    /**
     * 消息正文
     *  消息正文的对象必须是实现了序列化接口用于网络传输
     */
    private CommResult<T> messageBody;


    public static Message buildMessage(Object object){
        Message message = new Message<>();
        message.setMagicNo(SilkSideSystemConfig.getMagicNo());
        message.setVersion(Version.PT_SILK_1.getCode());
        message.setSerialization(0);
        message.setMessageType(1);
        CommResult success = CommResult.success(object);
        int length = Serialize.Realization.Java.beanCastBytes(success).length;
        message.setMessageLength(length);
        message.setMessageBody(success);
        return message;
    }


}
