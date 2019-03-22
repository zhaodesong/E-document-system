package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class ProjectAccount implements Serializable {

    private static final long serialVersionUID = -5943729703469817484L;

    private Long id;

    private Integer teamId;

    private Integer accountId;

    private Byte permission;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}