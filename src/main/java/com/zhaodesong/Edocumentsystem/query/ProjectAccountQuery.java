package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ProjectAccountQuery implements Serializable {

    private static final long serialVersionUID = 6790466685786922076L;

    private Long id;

    private Integer projectId;

    private Integer accountId;

    private String permission;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;

    private LocalDateTime updateTimeStart;

    private LocalDateTime updateTimeEnd;
}
