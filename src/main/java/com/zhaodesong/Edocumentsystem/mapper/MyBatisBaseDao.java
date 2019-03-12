package com.zhaodesong.Edocumentsystem.mapper;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * DAO公共基类
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 * @param <Model> The Model Class 这里是泛型不是Model类
 * @param <PK> The Primary Key Class
 *
 */
public interface MyBatisBaseDao<Model, PK extends Serializable> {
//    int deleteByPrimaryKey(PK id);
//
//    int insert(Model record);
//
//    int insertSelective(Model record);
//
//    Model selectByPrimaryKey(PK id);
//
//    int updateByPrimaryKeySelective(Model record);
//
//    int updateByPrimaryKey(Model record);
    Model getById(PK id);
}