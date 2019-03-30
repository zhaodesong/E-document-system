package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.dao.AccountDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhaoDesong
 * @date 2019-3-12 17:20
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public Account getById(Long id) {
        return accountDao.getById(id);
    }

    @Override
    public boolean loginCheck(AccountQuery account) {
        List<Account> accountList = accountDao.findNotNull(account);
        return !accountList.isEmpty();
    }

    @Override
    public boolean mailRepeatCheck(AccountQuery account) {
        List<Account> accountList = accountDao.findNotNull(account);
        return !accountList.isEmpty();
    }

    @Override
    public int insert(Account account) {
        return accountDao.insert(account);
    }

    @Override
    public int deleteById(Long id) {
        return accountDao.deleteById(id);
    }

    @Override
    public int updateById(Account account) {
        log.debug("调用updateById函数，参数account={}", account);
        return accountDao.updateById(account);
    }
}
