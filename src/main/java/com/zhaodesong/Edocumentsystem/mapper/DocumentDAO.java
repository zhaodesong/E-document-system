package com.zhaodesong.Edocumentsystem.mapper;

import com.zhaodesong.Edocumentsystem.po.Document;
import org.springframework.stereotype.Repository;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface DocumentDAO extends MyBatisBaseDao<Document, Long> {
}