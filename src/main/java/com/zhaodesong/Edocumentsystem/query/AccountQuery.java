package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-4-22 9:46
 */
@Data
public class AccountQuery implements Serializable {

    private static final long serialVersionUID = -299385616037064830L;

    private Integer id;

    private String mail;

    private String password;

    private String nickName;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;

    private LocalDateTime updateTimeStart;

    private LocalDateTime updateTimeEnd;
}
