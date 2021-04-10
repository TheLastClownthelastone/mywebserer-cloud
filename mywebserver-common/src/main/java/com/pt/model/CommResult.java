package com.pt.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 12:52
 * 通用消息体
 */
@Data
public class CommResult<T> implements Serializable{

    private String code;

    private String message;

    private T data;

    private CommResult(){}

    private  CommResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功信息
     * @param object
     * @return
     */
    public static CommResult success(Object object){
        return new CommResult("200", "ok", object);
    }


    public static CommResult fail(String message){
        return new CommResult("500",message);
    }

}
