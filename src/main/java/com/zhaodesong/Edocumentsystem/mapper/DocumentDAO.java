package com.zhaodesong.Edocumentsystem.mapper;

import com.zhaodesong.Edocumentsystem.po.Document;
import org.springframework.stereotype.Repository;

/**
 * DocumentDAO继承基类
 */
@Repository
public interface DocumentDAO extends MyBatisBaseDao<Document, Long> {
}