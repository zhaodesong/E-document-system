package com.zhaodesong.Edocumentsystem.base;

public enum ResultCodeEnum {
    SIGNUP_SUCCESS(1001,"登陆成功"),
    SIGNUP_FAILE(1002,"账号或密码错误");

    private int code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
