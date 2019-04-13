package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.vo.AccountForManage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
public interface AccountService {
    Account getById(Integer id);

    Account login(AccountQuery account);

    int logout(String mail);

    int insert(Account account);

    int deleteById(Integer id);

    int updateById(Account account);

    Account getByMail(String mail);

    List<AccountForManage> getAccountForManage(Integer projectId);
}
