package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.query.ProjectQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import com.zhaodesong.Edocumentsystem.util.FileUtils;
import com.zhaodesong.Edocumentsystem.vo.AccountForManage;
import com.zhaodesong.Edocumentsystem.vo.ProjectWithPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProjectAccountController extends BaseController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProjectAccountService projectAccountService;
    @Autowired
    private ProjectService projectService;

    @RequestMapping("/inviteMember")
    @ResponseBody
    public Object inviteMember() {
        Map<String, Object> result = new HashMap<>();

        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = (Integer) session.getAttribute("projectId");
        String inviteMail = request.getParameter("inviteMail");
        if (!hasPermissionEditAccount(projectId, accountId)) {
            result.put("msg", "您没有操作权限");
            result.put("result", 0);
            return result;
        }
        Account account = accountService.getByMail(inviteMail);
        if (account == null) {
            result.put("msg", "该用户不存在");
            result.put("result", 0);
            return result;
        }

        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, account.getId());
        if (projectAccount != null) {
            result.put("msg", "该用户已在项目中");
            result.put("result", 0);
            return result;
        }
        ProjectAccount insertAccount = new ProjectAccount();
        insertAccount.setAccountId(account.getId());
        insertAccount.setProjectId(projectId);
        insertAccount.setPermission("01");
        projectAccountService.insert(insertAccount);

        AccountForManage acc = new AccountForManage();
        acc.setId(account.getId());
        acc.setMail(inviteMail);
        acc.setNickName(account.getNickName());
        acc.setPower("00");
        result.put("member", acc);
        result.put("msg", "添加新成员成功");
        result.put("result", 1);
        return result;
    }

    @RequestMapping("/deleteMember")
    public String deleteMember() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer deleteId = Integer.parseInt(request.getParameter("deleteId"));
        System.out.println("ProjectAccountController:85     projectId=" + projectId + ",accountId=" + accountId + ",deleteId=" + deleteId);
        if (hasPermissionEditAccount(projectId, accountId)) {
            projectAccountService.deleteByProjectIdAndAccountId(projectId, deleteId);
            request.setAttribute("msg", "移除成功");
            return "redirect:/toAccountManage?pid=" + projectId;
        }
        request.setAttribute("msg", "您没有操作权限");
        return "redirect:/toAccountManage?pid=" + projectId;
    }


    @RequestMapping("/changePermission")
    @ResponseBody
    public Object changePermissionAccount() {
        Map<String, Object> result = new HashMap<>();
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer changeId = Integer.parseInt(request.getParameter("changeId"));
        String p = request.getParameter("permission");
        if (!"00".equals(p) && !"01".equals(p) && !"11".equals(p)) {
            result.put("msg", "参数错误");
            return result;
        }

        if (hasPermissionEditAccount(projectId, accountId)) {
            ProjectAccount pa = new ProjectAccount();
            pa.setProjectId(projectId);
            pa.setAccountId(changeId);
            pa.setPermission(p);
            projectAccountService.updatePermission(pa);
        }
        result.put("msg", "修改成功");
        return result;
    }

    @RequestMapping("toAccountManage")
    public String toAccountManage() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        session.setAttribute("projectId", projectId);

        if (!hasPermissionEditAccount(projectId, accountId)) {
            request.setAttribute("msg", "您无权访问该页面");
            return "redirect:/toLoginSuccess";
        }

        List<AccountForManage> accountList = accountService.getAccountForManage(projectId);
        request.setAttribute("accountList", accountList);
        return "account_manage";
    }

    @RequestMapping(value = "/quitProject")
    public String deleteProject() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }

        int projectId = Integer.parseInt(request.getParameter("pid"));
        Integer accountId = (Integer) session.getAttribute("accountId");

        projectAccountService.deleteByProjectIdAndAccountId(projectId, accountId);


        // 查询该用户加入的项目
        List<ProjectWithPower> projectList = projectService.getProjectPowerByAccountId(accountId);
        request.setAttribute("project", projectList);
        return "login_success";
    }


    private boolean hasPermissionEditAccount(Integer projectId, Integer accountId) {
        Project project = projectService.getProjectById(projectId);
        if (accountId.equals(project.getCreateAccount())) {
            return true;
        }

        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, accountId);
        String p = projectAccount.getPermission();
        return "11".equals(p);
    }

    private boolean hasPermissionEditDoc(Integer projectId, Integer accountId) {
        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, accountId);
        String p = projectAccount.getPermission();
        return "01".equals(p) || "11".equals(p);
    }
}
