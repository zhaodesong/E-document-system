package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.ProjectQuery;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:05
 */
public interface ProjectService {
    List<Project> getProjectNotNull(ProjectQuery query);

    int insert(Project project);

    int deleteById(int id);

    Project getProjectById(Integer id);

    int renameProject(Integer projectId, String newName);
}
