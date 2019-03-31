package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ProjectQuery implements Serializable {

    private static final long serialVersionUID = -6716451389750142047L;

    private Integer id;

    private String name;

    private Long createAccount;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;

    private LocalDateTime updateTimeStart;

    private LocalDateTime updateTimeEnd;
}
