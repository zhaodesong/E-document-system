package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.ProjectAccountDao;
import com.zhaodesong.Edocumentsystem.dao.ProjectDao;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.query.ProjectAccountQuery;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import com.zhaodesong.Edocumentsystem.vo.ProjectWithPower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:11
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ProjectAccountDao projectAccountDao;

    @Override
    public List<Project> getProjectByAccountId(Integer accountId) {
        ProjectAccountQuery query = new ProjectAccountQuery();
        query.setAccountId(accountId);
        List<ProjectAccount> projectAccountList = projectAccountDao.findNotNull(query);
        List<Project> result = new ArrayList<>();
        for (int i = 0; i < projectAccountList.size(); i++) {
            result.add(projectDao.getById(projectAccountList.get(i).getProjectId()));
        }
        return result;
    }

    @Override
    public int insert(Project project) {
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        return projectDao.insert(project);
    }

    @Override
    public int deleteById(int id) {
        return projectDao.deleteById(id);
    }

    @Override
    public Project getProjectById(Integer id) {
        return projectDao.getById(id);
    }

    @Override
    public int renameProject(Integer projectId, String newName) {
        Project project = new Project();
        project.setId(projectId);
        project.setName(newName);
        return projectDao.updateById(project);
    }

    @Override
    public List<ProjectWithPower> getProjectPowerByAccountId(Integer accountId) {
        return projectDao.getProjectPowerByAccountId(accountId);
    }

    @Override
    public int changeCreateAccount(Integer projectId, Integer newCreateAccount) {
        Project project = new Project();
        project.setId(projectId);
        project.setCreateAccount(newCreateAccount);
        return projectDao.updateById(project);
    }
}
