package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
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
            + "<if test='docId != null'>AND doc_id = #{docId}</if>"
            + "<if test='projectId != null'>AND project_id = #{projectId}</if>"
            + "<if test='name !=null'>AND name = #{name}</if>"
            + "<if test='version !=null'>AND version = #{version}</if>"
            + "<if test='accountIdCreate !=null'>AND account_id_create = #{accountIdCreate}</if>"
            + "<if test='accountIdModify !=null'>AND account_id_modify = #{accountIdModify}</if>"
            + "<if test='type !=null'>AND type = #{type}</if>"
            + "<if test='parentId !=null'>AND parent_id = #{parentId}</if>"
            + "<if test='level !=null'> AND level = #{level}</if>"
            + "<if test='createTimeStart !=null'>AND create_time &gt; #{createTimeStart}</if>"
            + "<if test='createTimeEnd !=null'>AND create_time &lt; #{createTimeEnd}</if>"
            + "<if test='updateTimeStart !=null'>AND create_time &gt; #{updateTimeStart}</if>"
            + "<if test='updateTimeEnd !=null'>AND create_time &lt; #{updateTimeEnd}</if>"
            + "</where>"
            + "</script>")
    List<Document> findNotNull(DocumentQuery documentQuery);

    @Insert("INSERT INTO document(id,doc_id,project_id,name,version,account_id_create,account_id_modify,type,parent_id,level,power,del_flag,create_time,update_time) " +
            "VALUES (#{id}, #{docId}, #{projectId}, #{name}, #{version}, #{accountIdCreate}, #{accountIdModify}, #{type}, #{parentId}, #{level}, #{power},#{del_flag}#{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Document document);

    @Delete("DELETE FROM document WHERE project_id = #{projectId}")
    int deleteByProjectId(Integer projectId);

    @Delete("DELETE FROM document WHERE doc_id = #{projectId}")
    int deleteByDocId(Long docId);

    @Update("UPDATE document SET power=#{power} WHERE doc_id=#{docId}")
    int changePermission(@Param("docId")Long docId, @Param("name")Integer power);

    @Select("SELECT MAX(doc_id) FROM document")
    Long getMaxDocId();

    @Select("SELECT * FROM document AS d WHERE d.project_id=#{projectId} AND d.level=#{level} AND d.version = " +
            "(SELECT max(version) FROM document WHERE d.doc_id = doc_id AND project_id=#{projectId} AND level=#{level})")
    List<Document> getAllDocInfoByProjectId(@Param("projectId")Integer projectId,@Param("level")Byte level);

    @Select("SELECT * FROM document AS d WHERE d.parent_id=#{parentId} AND d.level=#{level} AND d.version = " +
            "(SELECT max(version) FROM document WHERE d.doc_id = doc_id AND parent_id=#{parentId} AND level=#{level})")
    List<Document> getAllDocInfoByParentId(@Param("parentId")Long parentId,@Param("level")Byte level);

    @Update("UPDATE document SET power=#{name} WHERE doc_id=#{docId}")
    int renameByDocId(@Param("docId")Long docId, @Param("name")String name);
}