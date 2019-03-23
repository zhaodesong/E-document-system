package com.zhaodesong.Edocumentsystem.base;

public enum ResultCodeEnum {
    SIGNUP_SUCCESS(101,"登陆成功"),
    ;

    private int code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
