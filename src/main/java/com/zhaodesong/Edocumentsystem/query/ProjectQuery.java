package com.zhaodesong.Edocumentsystem.query;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoDesong
 * @date 2019-4-22 9:46
 */
@Data
public class ProjectQuery implements Serializable {

    private static final long serialVersionUID = -6716451389750142047L;

    private Integer id;

    private String name;

    private Integer createAccount;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;

    private LocalDateTime updateTimeStart;

    private LocalDateTime updateTimeEnd;
}
