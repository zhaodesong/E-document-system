package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AccountQuery implements Serializable {

    private static final long serialVersionUID = -299385616037064830L;

    private Long id;

    private String mail;

    private String password;

    private String nickName;

    private Boolean verifyFlag;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;

    private LocalDateTime updateTimeStart;

    private LocalDateTime updateTimeEnd;
}
