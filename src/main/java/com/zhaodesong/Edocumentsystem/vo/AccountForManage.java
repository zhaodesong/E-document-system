package com.zhaodesong.Edocumentsystem.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoDesong
 * @date 2019-4-12 20:58
 */
@Data
public class AccountForManage implements Serializable {

    private Integer id;

    private String mail;

    private String nickName;

    private String power;
}
