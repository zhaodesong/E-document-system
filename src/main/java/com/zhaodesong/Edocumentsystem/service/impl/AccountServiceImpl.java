package com.zhaodesong.Edocumentsystem.service.impl;

import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.dao.AccountDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Account getById(Integer id) {
        return accountDao.getById(id);
    }

    @Override
    public Account login(AccountQuery account) {
        List<Account> accountList = accountDao.findNotNull(account);
        if (accountList == null || accountList.size() == 0) {
            return null;
        }
        return accountList.get(0);
    }

    @Override
    public int logout(String mail) {
        return 0;
    }

    @Override
    public int insert(Account account) {
        LocalDateTime now = LocalDateTime.now();
        account.setCreateTime(now);
        account.setUpdateTime(now);
        return accountDao.insert(account);
    }

    @Override
    public int deleteById(Integer id) {
        return accountDao.deleteById(id);
    }

    @Override
    public int updateById(Account account) {
        //log.debug("调用updateById函数，参数account={}", account);
        account.setUpdateTime(LocalDateTime.now());
        return accountDao.updateById(account);
    }

    @Override
    public Account getByMail(String mail) {
        return accountDao.getByMail(mail);
    }
}
