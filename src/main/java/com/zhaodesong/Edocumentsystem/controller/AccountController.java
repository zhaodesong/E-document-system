package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import com.zhaodesong.Edocumentsystem.vo.ProjectWithPower;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class AccountController extends BaseController {
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

    @RequestMapping(value = "/toAccount")
    public String toAccount() {
        return "account";
    }

    @RequestMapping(value = "/login")
    public String login() {
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");
        log.debug("AccountController login开始, 参数为mail = {}, password = {}", mail, password);

        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)) {
            request.setAttribute("msg", "输入不能为空，请重新输入");
            return "index";
        }

        Account account = accountService.login(mail, password);
        if (account == null) {
            request.setAttribute("msg", "邮箱或密码错误");
            return "index";
        }

        session.setAttribute("nickName", account.getNickName());
        session.setAttribute("mail", mail);
        session.setAttribute("accountId", account.getId());

        // 查询该用户加入的项目
        List<ProjectWithPower> projectList = projectService.getProjectPowerByAccountId(account.getId());
        request.setAttribute("project", projectList);

        log.debug("AccountController login结束");
        return "login_success";
    }

    @RequestMapping(value = "/register")
    public String register() {
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");
        String nickName = request.getParameter("nickName");
        log.debug("AccountController register开始, 参数为mail = {}, password = {}, nickName = {}", mail, password, nickName);

        if (StringUtils.isEmpty(mail) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickName)) {
            request.setAttribute("msg", "输入不能为空，请重新输入");
            return "register";
        }

        Account account = new Account();
        account.setMail(mail);
        account.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()).toLowerCase());
        account.setNickName(nickName);
        account.setVerifyFlag(false);

        if (accountService.insert(account) == 1) {
            request.setAttribute("msg", "注册成功,请登录");
            return "index";
        }
        request.setAttribute("msg", "注册失败,请稍后重试");
        log.debug("AccountController register结束");
        return "register";
    }

    @RequestMapping("/logout")
    public String logout() {
        session.invalidate();
        return "index";
    }

    @RequestMapping("/changeNickName")
    @ResponseBody
    public Object changeNickName() {
        Map<String, Object> result = new HashMap<>();
        if (sessionCheck()) {
            result.put("msg", "登录失效，请重新登录");
            result.put("result", -1);
            return result;
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        String newName = request.getParameter("nickName");
        log.debug("AccountController register开始, 参数为accountId = {}, newName = {}", accountId, newName);

        Account account = new Account();
        account.setId(accountId);
        account.setNickName(newName);
        int sqlResult = accountService.updateById(account);
        if (sqlResult == 1) {
            session.setAttribute("nickName", newName);
            result.put("msg", "修改成功");
            result.put("result", 1);
        } else {
            result.put("msg", "修改失败");
            result.put("result", 0);
        }
        log.debug("AccountController changeNickName结束");
        return result;
    }


    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePassword() {
        Map<String, Object> result = new HashMap<>();
        if (sessionCheck()) {
            result.put("msg", "登录失效，请重新登录");
            result.put("result", -1);
            return result;
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        log.debug("AccountController changePassword开始, 参数为accountId = {}, oldPwd = {}, newPwd = {}", accountId, oldPwd, newPwd);

        Account acc = accountService.getById(accountId);
        if (!acc.getPassword().equals(DigestUtils.md5DigestAsHex(oldPwd.getBytes()).toLowerCase())) {
            result.put("msg", "原密码不正确");
            result.put("result", 2);
            return result;
        }
        Account account = new Account();
        account.setId(accountId);
        account.setPassword(DigestUtils.md5DigestAsHex(newPwd.getBytes()));
        int sqlResult = accountService.updateById(account);
        if (sqlResult == 1) {
            result.put("msg", "修改成功");
            result.put("result", 1);
        } else {
            result.put("msg", "修改失败");
            result.put("result", 0);
        }
        log.debug("AccountController changePwd结束");
        return result;
    }

}
