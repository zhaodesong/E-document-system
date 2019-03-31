package com.zhaodesong.Edocumentsystem.service;

import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
public interface AccountService {
    Account getById(Long id);

    Account login(AccountQuery account);

    boolean mailRepeatCheck(AccountQuery account);

    int insert(Account account);

    int deleteById(Long id);

    int updateById(Account account);
}
