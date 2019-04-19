package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.ProjectQuery;
import com.zhaodesong.Edocumentsystem.vo.ProjectWithPower;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface ProjectDao {
    @Select("SELECT * FROM project WHERE id = #{id}")
    Project getById(Integer id);

    @Select("<script>"
            + "SELECT * FROM project"
            + "<where>"
            + "<if test='name != null'>AND name = #{name} </if>"
            + "<if test='createAccount !=null'>AND create_account = #{createAccount} </if>"
            + "<if test='createTimeStart !=null'>AND create_time &gt; #{createTimeStart} </if>"
            + "<if test='createTimeEnd !=null'>AND create_time &lt; #{createTimeEnd} </if>"
            + "<if test='updateTimeStart !=null'>AND create_time &gt; #{updateTimeStart} </if>"
            + "<if test='updateTimeEnd !=null'>AND create_time &lt; #{updateTimeEnd} </if>"
            + "</where>"
            + "</script>")
    List<Project> findNotNull(ProjectQuery project);

    @Insert("INSERT INTO project(id,name,create_account,create_time,update_time) " +
            "VALUES (#{id}, #{name}, #{createAccount}, #{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Project project);

    @Delete("DELETE FROM project WHERE id = #{id}")
    int deleteById(Integer id);

    @Update("<script>"
            + "UPDATE project "
            + "<set>"
            + "<if test='name != null'>name = #{name}</if> "
            + "<if test='createAccount !=null'>create_account = #{createAccount}</if> "
            + "</set>"
            + "WHERE id = #{id}"
            + "</script>")
    int updateById(Project project);

    @Select("SELECT project.id as id,name,permission as power FROM project, project_account WHERE project.id = project_id AND account_id = #{accountId}")
    List<ProjectWithPower> getProjectPowerByAccountId(Integer accountId);
}