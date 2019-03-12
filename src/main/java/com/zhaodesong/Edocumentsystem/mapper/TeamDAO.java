package com.zhaodesong.Edocumentsystem.mapper;

import com.zhaodesong.Edocumentsystem.po.Team;
import org.springframework.stereotype.Repository;

/**
 * TeamDAO继承基类
 */
@Repository
public interface TeamDAO extends MyBatisBaseDao<Team, Integer> {
}