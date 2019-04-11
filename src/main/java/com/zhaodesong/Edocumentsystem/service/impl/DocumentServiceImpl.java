package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.DocumentDao;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import org.apache.ibatis.ognl.InappropriateExpressionException;
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

    @Override
    public Document getLatestVersionInfo(Long docId) {
        DocumentQuery query = new DocumentQuery();
        query.setDocId(docId);
        List<Document> documentList = documentDao.findNotNull(query);
        if (documentList.get(0).getVersion() > documentList.get(documentList.size() - 1).getVersion()) {
            return documentList.get(0);
        } else {
            return documentList.get(documentList.size() - 1);
        }
    }

    @Override
    public int deleteByDocId(Long docId) {
        return documentDao.deleteByDocId(docId);
    }

    @Override
    public List<Document> getDeleteInfoByDocId(Long docId) {
        DocumentQuery query = new DocumentQuery();
        query.setDocId(docId);
        return documentDao.findNotNull(query);
    }

    @Override
    public List<Document> getDeleteInfoByParentId(Long parentId) {
        List<Document> result = new ArrayList<>(getDeleteFile(parentId));
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getType()) {
                result.addAll(getDeleteFile(result.get(i).getDocId()));
            }
        }

        DocumentQuery query2 = new DocumentQuery();
        query2.setDocId(parentId);
        List<Document> document = documentDao.findNotNull(query2);
        result.add(document.get(0));
        return result;
    }

    @Override
    public List<Document> getAllDocInfoByDocId(Long docId) {
        DocumentQuery query = new DocumentQuery();
        query.setDocId(docId);
        return documentDao.findNotNull(query);
    }

    @Override
    public List<Document> getAllDocInfoByProjectId(Integer projectId) {
        return documentDao.getAllDocInfoByProjectId(projectId,(byte)0);
    }

    @Override
    public int renameByDocId(Long docId, String name) {
        return documentDao.renameByDocId(docId, name);
    }

    @Override
    public List<Document> getAllDocInfoByParentId(Long parentId,Byte level) {
        return documentDao.getAllDocInfoByParentId(parentId,(byte)(level+1));
    }

    @Override
    public int changePermission(Long docId, Integer power) {
        return documentDao.changePermission(docId,power);
    }

    private List<Document> getDeleteFile(Long parentId) {
        DocumentQuery query1 = new DocumentQuery();
        query1.setParentId(parentId);
        List<Document> documentList = documentDao.findNotNull(query1);
        return documentList;
    }
}
