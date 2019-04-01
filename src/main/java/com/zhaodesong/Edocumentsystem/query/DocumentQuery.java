package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DocumentQuery implements Serializable {
    private static final long serialVersionUID = -1731054131432638704L;
    private Long id;

    private Integer projectId;

    private String name;

    private Integer version;

    private Integer accountId;

    private Boolean type;

    private Long parentId;

    private Byte level;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;

    private LocalDateTime updateTimeStart;

    private LocalDateTime updateTimeEnd;
}
