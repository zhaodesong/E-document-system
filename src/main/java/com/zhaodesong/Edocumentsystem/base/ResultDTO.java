package com.zhaodesong.Edocumentsystem.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ResultDTO implements Serializable {
    private static final long serialVersionUID = 7490246494539157599L;

    private int code;
    private String message;
    private Map<String,Object> data;
}
