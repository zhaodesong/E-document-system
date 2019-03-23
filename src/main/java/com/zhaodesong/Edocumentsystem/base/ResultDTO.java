package com.zhaodesong.Edocumentsystem.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ResultDTO implements Serializable {
    private int code;
    private String message;
    private Map<String,Object> data;
}
