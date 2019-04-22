package com.zhaodesong.Edocumentsystem.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoDesong
 * @date 2019-4-12 20:58
 */
@Data
public class AccountForManage implements Serializable {

    private static final long serialVersionUID = 4911570964452103677L;

    private Integer id;

    private String mail;

    private String nickName;

    private String power;
}
