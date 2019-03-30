package com.zhaodesong.Edocumentsystem.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.zhaodesong.Edocumentsystem.base.ResultCodeEnum;
import com.zhaodesong.Edocumentsystem.base.ResultDTO;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDTO login(String data) {
        ResultDTO result = new ResultDTO();
        if (StringUtils.isEmpty(data)) {
            result.setCode(ResultCodeEnum.PARM_NULL.getCode());
            result.setMessage(ResultCodeEnum.PARM_NULL.getMessage());
        }

        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(data);
        String mail = (String) map.get("mail");
        String password = (String) map.get("password");
        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)) {
            result.setCode(ResultCodeEnum.PARM_UNCOMPLETE.getCode());
            result.setMessage(ResultCodeEnum.PARM_UNCOMPLETE.getMessage());
        }

        AccountQuery query = new AccountQuery();
        query.setMail(mail);
        query.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        if (accountService.loginCheck(query)) {
            result.setCode(ResultCodeEnum.LOGIN_SUCCESS.getCode());
            result.setMessage(ResultCodeEnum.LOGIN_SUCCESS.getMessage());
            return result;
        }
        result.setCode(ResultCodeEnum.LOGIN_FAIL.getCode());
        result.setMessage(ResultCodeEnum.LOGIN_FAIL.getMessage());
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDTO register(String data) {
        ResultDTO result = new ResultDTO();
        if (StringUtils.isEmpty(data)) {
            result.setCode(ResultCodeEnum.PARM_NULL.getCode());
            result.setMessage(ResultCodeEnum.PARM_NULL.getMessage());
        }

        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(data);
        String mail = (String) map.get("mail");
        String password = (String) map.get("password");
        String nickName = (String) map.get("nickName");
        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickName)) {
            result.setCode(ResultCodeEnum.PARM_UNCOMPLETE.getCode());
            result.setMessage(ResultCodeEnum.PARM_UNCOMPLETE.getMessage());
        }
        // 验证该邮箱是否已被注册
        AccountQuery query = new AccountQuery();
        query.setMail(mail);
        if (accountService.mailRepeatCheck(query)) {
            result.setCode(ResultCodeEnum.REGISTER_FAIL_MAIL.getCode());
            result.setMessage(ResultCodeEnum.REGISTER_FAIL_MAIL.getMessage());
            return result;
        }

        Account account = new Account();
        account.setMail(mail);
        account.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        account.setNickName(nickName);
        account.setVerifyFlag(false);

        if (accountService.insert(account) == 1) {
            result.setCode(ResultCodeEnum.REGISTER_SUCCESS.getCode());
            result.setMessage(ResultCodeEnum.REGISTER_SUCCESS.getMessage());
            return result;
        }
        result.setCode(ResultCodeEnum.REGISTER_FAIL.getCode());
        result.setMessage(ResultCodeEnum.REGISTER_FAIL.getMessage());
        return result;
    }
}
