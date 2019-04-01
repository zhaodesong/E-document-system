package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
public interface AccountService {
    Account getById(Integer id);

    @CachePut(cacheNames = "accounts", key = "#account.mail")
    Account login(AccountQuery account);

    @Cacheable(cacheNames = "accounts", key = "#mail")
    Account loginCheck(String mail);

    @CacheEvict(value = "accounts", key = "#mail")
    int logout(String mail);

    boolean mailRepeatCheck(AccountQuery account);

    int insert(Account account);

    int deleteById(Integer id);

    int updateById(Account account);
}
