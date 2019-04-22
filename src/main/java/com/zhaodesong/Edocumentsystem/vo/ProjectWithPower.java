package com.zhaodesong.Edocumentsystem.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoDesong
 * @date 2019-4-22 9:46
 */
@Data
public class ProjectWithPower implements Serializable {

    private static final long serialVersionUID = 3143556936056297618L;

    private Integer id;

    private String name;

    private String power;
}
