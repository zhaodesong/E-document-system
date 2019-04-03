package com.zhaodesong.Edocumentsystem.exception;

/**
 * 文件/文件夹处理中的异常类
 */
public class FileException extends RuntimeException {

    private String msg;  //异常对应的描述信息

    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
