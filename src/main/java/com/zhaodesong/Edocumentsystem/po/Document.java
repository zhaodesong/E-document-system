package com.zhaodesong.Edocumentsystem.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Data
public class Document implements Serializable {

    private static final long serialVersionUID = 7408059535919834271L;

    private Long id;

    private Long docId;

    private Integer projectId;

    private String name;

    private Integer version;

    private Integer accountIdCreate;

    private Integer accountIdModify;

    private Boolean type;

    private Long parentId;

    private Byte level;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}