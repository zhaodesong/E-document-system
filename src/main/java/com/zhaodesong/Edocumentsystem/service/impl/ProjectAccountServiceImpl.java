package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.ProjectAccountDao;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.query.ProjectAccountQuery;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        projectAccount.setCreateTime(LocalDateTime.now());
        projectAccount.setUpdateTime(LocalDateTime.now());
        return projectAccountDao.insert(projectAccount);
    }

    @Override
    public int deleteByProjectId(Integer projectId) {
        return projectAccountDao.deleteByProjectId(projectId);
    }

    @Override
    public List<ProjectAccount> getProjectAccountByAccountId(Integer accountId) {
        ProjectAccountQuery query = new ProjectAccountQuery();
        query.setAccountId(accountId);
        return projectAccountDao.findNotNull(query);
    }

    @Override
    public ProjectAccount getByProjectIdAndAccountId(Integer projectId, Integer accountId) {
        ProjectAccountQuery query = new ProjectAccountQuery();
        query.setProjectId(projectId);
        query.setAccountId(accountId);
        List<ProjectAccount> projectAccountList = projectAccountDao.findNotNull(query);
        if (projectAccountList == null || projectAccountList.size() == 0) {
            return null;
        }
        return projectAccountList.get(0);
    }

    @Override
    public int deleteByProjectIdAndAccountId(Integer projectId, Integer accountId) {
        return projectAccountDao.deleteByProjectIdAndAccountId(projectId, accountId);
    }

    @Override
    public int updatePermission(ProjectAccount projectAccount) {
        return projectAccountDao.updatePermission(projectAccount);
    }

    @Override
    public int transferProject(Integer projectId, Integer oldAccount, Integer newAccount) {
        ProjectAccount projectAccount = new ProjectAccount();
        projectAccount.setProjectId(projectId);
        projectAccount.setAccountId(oldAccount);
        projectAccount.setPermission("10");
        if (projectAccountDao.updatePermission(projectAccount) == 0) {
            return 0;
        }

        projectAccount.setAccountId(newAccount);
        projectAccount.setPermission("111");
        return projectAccountDao.updatePermission(projectAccount);
    }
}
