package com.zhaodesong.Edocumentsystem.base;

import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

/**
 * DAO公共基类
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 * @param <Model> The Model Class 这里是泛型不是Model类
 * @param <PK> The Primary Key Class
 *
 */
public interface MyBatisBaseDao<Model, PK extends Serializable> {
    Model getById(PK var1) throws DataAccessException;

    Model getById(PK var1, boolean var2) throws DataAccessException;

    int deleteById(PK var1) throws DataAccessException;

    int save(Model var1) throws DataAccessException;

    int saveBatch(List<Model> var1) throws DataAccessException;

    int update(Model var1) throws DataAccessException;

//    List<Model> find(PageRequest var1);
//
//    List<Model> find(PageRequest var1, int var2);
//
//    Page<Model> findPage(PageRequest var1);

    Model getByEntity(Model var1);

    void deleteByEntity(Model var1);
}