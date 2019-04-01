package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.ProjectAccountDao;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:09
 */
@Service
public class ProjectAccountServiceImpl implements ProjectAccountService {
    @Autowired
    private ProjectAccountDao projectAccountDao;

    @Override
    public int insert(ProjectAccount projectAccount) {
        return projectAccountDao.insert(projectAccount);
    }

    @Override
    public int deleteByProjectId(Integer projectId) {
        return projectAccountDao.deleteByProjectId(projectId);
    }
}
