package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class Project implements Serializable {

    private static final long serialVersionUID = -3117496415378218683L;

    private Integer id;

    private String name;

    private Integer createAccount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}