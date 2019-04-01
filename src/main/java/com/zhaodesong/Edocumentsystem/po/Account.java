package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class Account implements Serializable {

    private static final long serialVersionUID = 4553617236020412455L;

    private Integer id;

    private String mail;

    private String password;

    private String nickName;

    private Boolean verifyFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}