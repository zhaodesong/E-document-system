package com.zhaodesong.Edocumentsystem.Service.impl;

import com.zhaodesong.Edocumentsystem.Service.AccountService;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.mapper.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public Account getById(Long id) {
        return accountDao.getById(id);
    }
}
