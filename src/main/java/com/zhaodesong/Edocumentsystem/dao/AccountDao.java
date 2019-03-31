package com.zhaodesong.Edocumentsystem.dao;

import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Repository
public interface AccountDao {
    @Select("SELECT * FROM account WHERE id = #{id}")
    Account getById(Long id);

    @Select("<script>"
            + "SELECT * FROM account"
            + "<where>"
            + "<if test='mail != null'>AND mail = #{mail}</if>"
            + "<if test='password !=null'>AND password = #{password}</if>"
            + "<if test='nickName !=null'>AND nick_name = #{nickName}</if>"
            + "<if test='verifyFlag !=null'>AND verify_flag = #{verifyFlag}</if>"
            + "<if test='createTimeStart !=null'>AND create_time &gt; #{createTimeStart}</if>"
            + "<if test='createTimeEnd !=null'>AND create_time &lt; #{createTimeEnd}</if>"
            + "<if test='updateTimeStart !=null'>AND create_time &gt; #{updateTimeStart}</if>"
            + "<if test='updateTimeEnd !=null'>AND create_time &lt; #{updateTimeEnd}</if>"
            + "</where>"
            + "</script>")
    List<Account> findNotNull(AccountQuery account);

    @Insert("INSERT INTO account(id,mail,password,nick_name,verify_flag,create_time,update_time) " +
            "VALUES (#{id}, #{mail}, #{password}, #{nickName}, #{verifyFlag}, #{createTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Account account);

    @Delete("DELETE FROM account WHERE id = #{id}")
    int deleteById(Long id);

    @Update("<script>"
            + "UPDATE account "
            + "<set>"
            + "<if test='mail != null'>mail = #{mail}</if>"
            + "<if test='password !=null'>password = #{password}</if>"
            + "<if test='nickName !=null'>nick_name = #{nickName}</if>"
            + "<if test='verifyFlag !=null'>verify_flag = #{verifyFlag}</if>"
            + "</set>"
            + "WHERE id = #{id}"
            + "</script>")
    int updateById(Account account);
}