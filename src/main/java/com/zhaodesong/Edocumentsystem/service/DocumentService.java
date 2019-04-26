package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import com.zhaodesong.Edocumentsystem.vo.DocumentWithPower;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:05
 */
public interface DocumentService {
    int deleteByProjectId(Integer projectId);

    List<Document> findNotNull(DocumentQuery documentQuery);

    int insert(Document document);

    String getNameByDocIdAndVersion(Long docId, Integer version);

    Long getMaxDocId();

    Document getLatestVersionInfo(Long docId);

    int deleteByDocId(Long docId);

    int recycleDeleteDirectlyByDocId(Long docId);

    int recycleDeleteIndirectlyByDocId(Long docId);

    int recoveryDeleteByDocId(Long docId);

    List<Document> getDeleteInfoByDocId(Long docId);

    List<Document> getDeleteInfoByParentId(Long parentId);

    List<Document> getAllDocInfoByDocId(Long docId, Integer delFlag);

    List<DocumentWithPower> getAllDocInfoByProjectId(Integer projectId, Integer delFlag);

    int renameByDocId(Long docId, String name);

    List<DocumentWithPower> getAllDocInfoByParentId(Long parentId);

    List<Document> getAllDelectDocByProjectId(Integer projectId);

    int changePermission(Long docId, Integer power);

    int moveByDocId(Long docId, Long parentId);

    List<Document> getDocInfoByParentId(Long parentId);
}
