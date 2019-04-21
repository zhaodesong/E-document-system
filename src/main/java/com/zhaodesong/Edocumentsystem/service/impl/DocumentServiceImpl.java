package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.dao.DocumentDao;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import com.zhaodesong.Edocumentsystem.vo.DocumentWithPower;
import org.apache.ibatis.ognl.InappropriateExpressionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
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
    public int recycleDeleteDirectlyByDocId(Long docId) {
        return documentDao.recycleDeleteDirectlyByDocId(docId);
    }

    @Override
    public int recycleDeleteIndirectlyByDocId(Long docId) {
        return documentDao.recycleDeleteIndirectlyByDocId(docId);
    }

    @Override
    public int recoveryDeleteByDocId(Long docId) {
        return documentDao.recoveryDeleteByDocId(docId);
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

        return result;
    }

    @Override
    public List<Document> getAllDocInfoByDocId(Long docId, Integer delFlag) {
        DocumentQuery query = new DocumentQuery();
        query.setDocId(docId);
        query.setDelFlag(delFlag);
        return documentDao.findNotNull(query);
    }

    @Override
    public List<DocumentWithPower> getAllDocInfoByProjectId(Integer projectId, Integer delFlag) {
        return documentDao.getAllDocInfoByProjectId(projectId, (byte) 0, delFlag);
    }

    @Override
    public int renameByDocId(Long docId, String name) {
        return documentDao.renameByDocId(docId, name);
    }

    @Override
    public List<DocumentWithPower> getAllDocInfoByParentId(Long parentId, Byte level) {
        return documentDao.getAllDocInfoByParentId(parentId, (byte) (level + 1));
    }

    @Override
    public List<Document> getAllDelectDocByProjectId(Integer projectId) {
        return documentDao.getAllDelectDocByProjectId(projectId);
    }

    @Override
    public int changePermission(Long docId, Integer power) {
        return documentDao.changePermission(docId, power);
    }

    private List<Document> getDeleteFile(Long parentId) {
        DocumentQuery query = new DocumentQuery();
        query.setParentId(parentId);
        query.setDelFlag(0);
        return documentDao.findNotNull(query);
    }
}
