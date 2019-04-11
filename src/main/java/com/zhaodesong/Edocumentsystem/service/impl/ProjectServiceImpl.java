package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.ProjectDao;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.ProjectQuery;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:11
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public List<Project> getProjectNotNull(ProjectQuery project) {
        return projectDao.findNotNull(project);
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
}
