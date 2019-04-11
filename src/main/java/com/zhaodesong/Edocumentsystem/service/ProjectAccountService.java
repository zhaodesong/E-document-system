package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.ProjectAccount;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:05
 */
public interface ProjectAccountService {
    int insert(ProjectAccount projectAccount);

    int deleteByProjectId(Integer projectId);

    List<ProjectAccount> getProjectAccountByAccountId(Integer accountId);

    ProjectAccount getByProjectIdAndAccountId(Integer projectId, Integer accountId);

    int deleteByProjectIdAndAccountId(Integer projectId, Integer accountId);

    int updatePermission(ProjectAccount projectAccount);
}
