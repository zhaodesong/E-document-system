package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer createAccount;

    private Date createTime;

    private Date updateTime;


}