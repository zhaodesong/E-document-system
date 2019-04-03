package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.DocumentDao;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 19:06
 */
@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentDao documentDao;

    @Override
    public int deleteByProjectId(Integer projectId) {
        return documentDao.deleteByProjectId(projectId);
    }

    @Override
    public List<Document> findNotNull(DocumentQuery documentQuery) {
        return documentDao.findNotNull(documentQuery);
    }

    @Override
    public int insert(Document document) {
        return documentDao.insert(document);
    }

    @Override
    public String getNameByDocId(Long docId) {
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocId(docId);
        List<Document> documentList = documentDao.findNotNull(documentQuery);
        if (documentList == null || documentList.size() == 0) {
            return null;
        }
        return documentList.get(0).getName();
    }

    @Override
    public Long getMaxDocId() {
        Long docId = documentDao.getMaxDocId();
        return docId == null ? 1 : docId;
    }
}
