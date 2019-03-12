package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class TeamAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer teamId;

    private Integer accountId;

    private Byte permission;
}