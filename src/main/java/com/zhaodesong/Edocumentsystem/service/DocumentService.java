package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;

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
}
