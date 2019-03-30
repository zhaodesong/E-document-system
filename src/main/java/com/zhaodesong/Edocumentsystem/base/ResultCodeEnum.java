package com.zhaodesong.Edocumentsystem.base;

public enum ResultCodeEnum {
    // 通用code
    PARM_NULL(1, "参数为空"),
    PARM_UNCOMPLETE(2,"参数不完整"),
    // 登录code
    LOGIN_SUCCESS(1000, "登陆成功"),
    LOGIN_FAIL(1001, "账号或密码错误"),
    // 注册code
    REGISTER_SUCCESS(2000, "注册成功"),
    REGISTER_FAIL_MAIL(2001, "该邮箱已注册"),
    REGISTER_FAIL(2021, "注册失败，请稍后重试"),

    ;



    private int code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
