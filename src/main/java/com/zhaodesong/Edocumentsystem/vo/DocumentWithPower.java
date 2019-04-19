package com.zhaodesong.Edocumentsystem.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DocumentWithPower implements Serializable {
    private Long id;

    private Long docId;

    private Integer projectId;

    private String name;

    private Integer version;

    private Integer accountIdCreate;

    private Integer accountIdModify;

    private Boolean type;

    private Long parentId;

    private Integer level;

    private Integer power;

    private Integer delFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String isEdit;
}
