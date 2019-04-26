package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import com.zhaodesong.Edocumentsystem.vo.DocumentWithPower;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface DocumentDao {
    Document getById(Long id);

    @Select("<script>"
            + "SELECT * FROM document"
            + "<where>"
            + "<if test='docId != null'>AND doc_id = #{docId} </if>"
            + "<if test='projectId != null'>AND project_id = #{projectId} </if>"
            + "<if test='name !=null'>AND name = #{name} </if>"
            + "<if test='version !=null'>AND version = #{version} </if>"
            + "<if test='accountIdCreate !=null'>AND account_id_create = #{accountIdCreate} </if>"
            + "<if test='accountIdModify !=null'>AND account_id_modify = #{accountIdModify} </if>"
            + "<if test='type !=null'>AND type = #{type} </if>"
            + "<if test='parentId !=null'>AND parent_id = #{parentId} </if>"
            + "<if test='createTimeStart !=null'>AND create_time &gt; #{createTimeStart} </if>"
            + "<if test='createTimeEnd !=null'>AND create_time &lt; #{createTimeEnd} </if>"
            + "<if test='updateTimeStart !=null'>AND create_time &gt; #{updateTimeStart} </if>"
            + "<if test='updateTimeEnd !=null'>AND create_time &lt; #{updateTimeEnd} </if>"
            + "</where>"
            + "</script>")
    List<Document> findNotNull(DocumentQuery documentQuery);

    @Insert("INSERT INTO document(id,doc_id,project_id,name,version,account_id_create,account_id_modify,type,parent_id,power,del_flag,create_time,update_time) " +
            "VALUES (#{id}, #{docId}, #{projectId}, #{name}, #{version}, #{accountIdCreate}, #{accountIdModify}, #{type}, #{parentId}, #{power}, #{delFlag}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Document document);

    @Delete("DELETE FROM document WHERE project_id = #{projectId}")
    int deleteByProjectId(Integer projectId);

    @Delete("DELETE FROM document WHERE doc_id = #{docId}")
    int deleteByDocId(Long docId);

    @Update("UPDATE document SET del_flag = 1 WHERE doc_id = #{docId}")
    int recycleDeleteDirectlyByDocId(Long docId);

    @Update("UPDATE document SET del_flag = 2 WHERE doc_id = #{docId}")
    int recycleDeleteIndirectlyByDocId(Long docId);

    @Update("UPDATE document SET del_flag = 0 WHERE doc_id = #{docId}")
    int recoveryDeleteByDocId(Long docId);

    @Update("UPDATE document SET power=#{power} WHERE doc_id=#{docId}")
    int changePermission(@Param("docId") Long docId, @Param("power") Integer power);

    @Select("SELECT MAX(doc_id) FROM document")
    Long getMaxDocId();

    @Select("SELECT * FROM document AS d WHERE d.project_id=#{projectId} AND parent_id=0 AND del_flag = #{delFlag} AND d.version = " +
            "(SELECT max(version) FROM document WHERE d.doc_id = doc_id AND parent_id=0 AND project_id=#{projectId} AND del_flag = #{delFlag})")
    List<DocumentWithPower> getAllDocInfoByProjectId(@Param("projectId") Integer projectId, @Param("delFlag") Integer delFlag);

    @Select("SELECT * FROM document AS d WHERE d.parent_id=#{parentId} AND del_flag = 0 AND d.version = " +
            "(SELECT max(version) FROM document WHERE d.doc_id = doc_id AND parent_id=#{parentId} AND del_flag = 0)")
    List<DocumentWithPower> getAllDocInfoByParentId(@Param("parentId") Long parentId);

    @Select("SELECT * FROM document AS d WHERE d.project_id=#{projectId} AND del_flag = 1 AND d.version = " +
            "(SELECT max(version) FROM document WHERE project_id=#{projectId} AND d.doc_id = doc_id AND del_flag = 1)")
    List<Document> getAllDelectDocByProjectId(Integer projectId);

    @Update("UPDATE document SET name=#{name} WHERE doc_id=#{docId}")
    int renameByDocId(@Param("docId") Long docId, @Param("name") String name);

    @Update("UPDATE document SET parent_id=#{parentId} WHERE doc_id=#{docId}")
    int moveByDocId(@Param("docId") Long docId, @Param("parentId") Long parentId);

    @Select("SELECT * FROM document AS d WHERE d.parent_id=#{parentId} AND del_flag = 0 AND d.version = " +
            "(SELECT max(version) FROM document WHERE d.doc_id = doc_id AND parent_id=#{parentId} AND del_flag = 0")
    List<Document> getDocInfoByParentId(@Param("parentId") Long parentId);
}