package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String mail;

    private String password;

    private String nickName;

    private Boolean verifyFlag;

    private Date createTime;

    private Date updateTime;

}