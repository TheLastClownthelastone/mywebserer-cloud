package com.pt.protocol;

import lombok.Getter;

/**
 * @author pt
 * @version 1.0
 * @date 2021/4/10 12:43
 */
@Getter
public enum Version {
    PT_SILK_1(1);
    private int code;

    Version(int code) {
        this.code = code;
    }
}
