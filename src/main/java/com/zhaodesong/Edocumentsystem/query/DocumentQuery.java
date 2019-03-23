package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DocumentQuery implements Serializable {
    private Long id;

    private Integer teamId;

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
