package com.zhaodesong.Edocumentsystem.mapper;

import com.zhaodesong.Edocumentsystem.po.Account;
import org.springframework.stereotype.Repository;

/**
 * AccountDAO继承基类
 */
@Repository
public interface AccountDao extends MyBatisBaseDao<Account, Long> {
}