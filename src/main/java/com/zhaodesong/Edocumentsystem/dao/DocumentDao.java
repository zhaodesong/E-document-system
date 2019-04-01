package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Document;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface DocumentDao {
    Document getById(Long id);

    List<Document> findNotNull(Map<String, Object> map);

    int insertNotNull(Map<String, Object> map);

    @Delete("DELETE FROM project_account WHERE project_id = #{projectId}")
    int deleteByProjectId(Integer projectId);

    int updateById(Long id);
}