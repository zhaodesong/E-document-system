package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:05
 */
public interface DocumentService {
    int deleteByProjectId(Integer projectId);

    List<Document> findNotNull(DocumentQuery documentQuery);

    int insert(Document document);

    String getNameByDocId(Long docId);

    Long getMaxDocId();

    Document getLatestVersionInfo(Long docId);

    int deleteByDocId(Long docId);

    List<Document> getDeleteInfoByDocId(Long docId);

    List<Document> getDeleteInfoByParentId(Long parentId);

    List<Document> getAllDocInfoByDocId(Long docId);

    List<Document> getAllDocInfoByProjectId(Integer projectId);

    int renameByDocId(Long docId, String name);

    List<Document> getAllDocInfoByParentId(Long parentId, Byte level);

    int changePermission(Long docId, Integer power);
}
