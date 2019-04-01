package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.ProjectAccount;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:05
 */
public interface ProjectAccountService {
    int insert(ProjectAccount projectAccount);

    int deleteByProjectId(Integer projectId);
}
