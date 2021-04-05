package com.pt.protocol.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/5 12:38
 */
@Data
public class User implements Serializable {
   public String id;
   public String name;
}
