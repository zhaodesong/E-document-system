package com.zhaodesong.Edocumentsystem.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-4-22 9:45
 */
@Data
public class DocumentWithPower implements Serializable {

    private static final long serialVersionUID = 8031089473906923567L;

    private Long id;

    private Long docId;

    private Integer projectId;

    private String name;

    private Integer version;

    private Integer accountIdCreate;

    private Integer accountIdModify;

    private Boolean type;

    private Long parentId;

    private Integer power;

    private Integer delFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String isEdit;
}
