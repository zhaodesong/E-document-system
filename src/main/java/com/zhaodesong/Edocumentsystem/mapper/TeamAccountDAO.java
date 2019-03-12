package com.zhaodesong.Edocumentsystem.mapper;

import com.zhaodesong.Edocumentsystem.po.TeamAccount;
import org.springframework.stereotype.Repository;

/**
 * TeamAccountDAO继承基类
 */
@Repository
public interface TeamAccountDAO extends MyBatisBaseDao<TeamAccount, Long> {
}