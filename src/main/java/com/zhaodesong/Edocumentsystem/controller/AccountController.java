package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.query.ProjectQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Slf4j
public class AccountController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ProjectService projectService;


    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/toRegister")
    public String toRegister() {
        return "register";
    }

    @RequestMapping(value = "/login")
    public String login() {
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");

        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)) {
            request.setAttribute("msg", "输入不能为空，请重新输入");
            return "index";
        }

        AccountQuery accountQuery = new AccountQuery();
        accountQuery.setMail(mail);
        accountQuery.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        Account account = accountService.login(accountQuery);
        if (account != null) {
            request.setAttribute("accountId", account.getId());
            request.setAttribute("mail", mail);
            request.setAttribute("nickName", account.getNickName());

            ProjectQuery projectQuery = new ProjectQuery();
            projectQuery.setCreateAccount(account.getId());
            // 查询该用户的项目信息
            List<Project> projectList = projectService.getProjectNotNull(projectQuery);
            if (projectList != null || projectList.size() == 0) {
                request.setAttribute("project", projectList);
            }
            return "login_success";
        } else {
            request.setAttribute("msg", "邮箱或密码错误");
            return "index";
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register() {
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");
        String nickName = request.getParameter("nickName");

        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickName)) {
            request.setAttribute("msg", "输入不能为空，请重新输入");
            return "register";
        }
        // 验证该邮箱是否已被注册
        AccountQuery query = new AccountQuery();
        query.setMail(mail);
        if (accountService.mailRepeatCheck(query)) {
            request.setAttribute("msg", "该邮箱已注册");
            return "register";
        }

        Account account = new Account();
        account.setMail(mail);
        account.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        account.setNickName(nickName);
        account.setVerifyFlag(false);

        if (accountService.insert(account) == 1) {
            request.setAttribute("msg", "注册成功,请登录");
            return "index";
        }
        request.setAttribute("msg", "注册失败,请稍后重试");
        return "register";
    }
}
