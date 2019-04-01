package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.query.ProjectAccountQuery;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface ProjectAccountDao {
    @Select("SELECT * FROM project_account WHERE id = #{id}")
    Project getById(Long id);

    @Select("<script>"
            + "SELECT * FROM project_account"
            + "<where>"
            + "<if test='id != null'>AND name = #{name}</if>"
            + "<if test='project_id !=null'>AND project_id = #{projectId}</if>"
            + "<if test='account_id !=null'>AND account_id = #{accountId}</if>"
            + "<if test='permission !=null'>AND permission = #{permission}</if>"
            + "<if test='createTimeStart !=null'>AND create_time &gt; #{createTimeStart}</if>"
            + "<if test='createTimeEnd !=null'>AND create_time &lt; #{createTimeEnd}</if>"
            + "<if test='updateTimeStart !=null'>AND create_time &gt; #{updateTimeStart}</if>"
            + "<if test='updateTimeEnd !=null'>AND create_time &lt; #{updateTimeEnd}</if>"
            + "</where>"
            + "</script>")
    List<Project> findNotNull(ProjectAccountQuery projectAccountQuery);

    @Insert("INSERT INTO project_account(id,project_id,account_id,permission,create_time,update_time) " +
            "VALUES (#{id}, #{projectId}, #{accountId},#{permission}, #{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(ProjectAccount projectAccount);

    @Delete("DELETE FROM project_account WHERE project_id = #{projectId}")
    int deleteByProjectId(Integer id);

    @Update("<script>"
            + "UPDATE project_account "
            + "<set>"
            + "<if test='id != null'>id = #{id}</if>"
            + "<if test='projectId !=null'>project_id = #{projectId}</if>"
            + "<if test='accountId !=null'>account_id = #{accountId}</if>"
            + "<if test='permission !=null'>permission = #{permission}</if>"
            + "</set>"
            + "WHERE id = #{id}"
            + "</script>")
    int updateById(ProjectAccount projectAccount);
}