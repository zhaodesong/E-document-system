package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Project;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface ProjectDao {
    Project getById(Long id);

    List<Project> findNotNull(Map<String,Object> map);

    int insertNotNull(Map<String,Object> map);

    int deleteById(Long id);

    int updateById(Long id);
}