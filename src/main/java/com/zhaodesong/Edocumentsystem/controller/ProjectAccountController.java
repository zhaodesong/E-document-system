package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import com.zhaodesong.Edocumentsystem.vo.AccountForManage;
import com.zhaodesong.Edocumentsystem.vo.ProjectWithPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        log.debug("ProjectAccountController inviteMember开始, 参数为accountId = {}, projectId = {}, inviteMail = {}", accountId, projectId, inviteMail);

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
        insertAccount.setPermission("10");
        int sqlResult = projectAccountService.insert(insertAccount);
        if (sqlResult == 1) {
            AccountForManage acc = new AccountForManage();
            acc.setId(account.getId());
            acc.setMail(inviteMail);
            acc.setNickName(account.getNickName());
            acc.setPower("10");
            result.put("member", acc);
            result.put("msg", "添加新成员成功");
            result.put("result", 1);
        } else {
            result.put("msg", "添加新成员失败");
            result.put("result", 0);
        }

        log.debug("ProjectAccountController inviteMember结束");
        return result;
    }

    @RequestMapping("/deleteMember")
    public String deleteMember() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer deleteId = Integer.parseInt(request.getParameter("deleteId"));
        log.debug("ProjectAccountController deleteMember开始, 参数为accountId = {}, projectId = {}, deleteId = {}", accountId, projectId, deleteId);
        if (hasPermissionEditAccount(projectId, accountId)) {
            int sqlResult = projectAccountService.deleteByProjectIdAndAccountId(projectId, deleteId);
            if (sqlResult >= 1) {
                request.setAttribute("msg", "移除成功");
            } else {
                request.setAttribute("msg", "移除失败");
            }
        } else {
            request.setAttribute("msg", "您没有权限操作，移除失败");
        }
        log.debug("ProjectAccountController deleteMember结束");
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
        log.debug("ProjectAccountController changePermissionAccount开始, 参数为accountId = {}, projectId = {}, changeId = {}, p = {}", accountId, projectId, changeId, p);
        if (!"00".equals(p) && !"10".equals(p) && !"11".equals(p)) {
            result.put("msg", "参数错误");
            result.put("result", 0);
            return result;
        }

        if (hasPermissionEditAccount(projectId, accountId)) {
            ProjectAccount pa = new ProjectAccount();
            pa.setProjectId(projectId);
            pa.setAccountId(changeId);
            pa.setPermission(p);
            int sqlResult = projectAccountService.updatePermission(pa);
            if (sqlResult >= 1) {
                request.setAttribute("msg", "修改成功");
                result.put("result", 1);
            } else {
                request.setAttribute("msg", "修改失败");
                result.put("result", 0);
            }
        } else {
            request.setAttribute("msg", "您没有操作权限，修改失败");
            result.put("result", 0);
        }
        log.debug("ProjectAccountController changePermissionAccount结束");
        return result;
    }

    @RequestMapping("toAccountManage")
    public String toAccountManage() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = Integer.parseInt(request.getParameter("pid"));

        log.debug("ProjectAccountController toAccountManage开始, 参数为accountId = {}, projectId = {}", accountId, projectId);
        session.setAttribute("projectId", projectId);

        List<AccountForManage> accountList = accountService.getAccountForManage(projectId);
        if (accountList == null || accountList.size() == 0) {
            request.setAttribute("msg", "系统错误，请稍后重试");
            return "account_manage";
        }
        // 项目创建者以及当前登陆者的信息不能修改，因此不显示
        Integer createId = projectService.getProjectById(projectId).getCreateAccount();
        // 去掉登录者的信息
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).getId().equals(accountId)) {
                accountList.remove(i);
                break;
            }
        }
        // 去掉创建者的信息
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).getId().equals(createId)) {
                accountList.remove(i);
                break;
            }
        }
        request.setAttribute("accountList", accountList);
        log.debug("ProjectAccountController toAccountManage结束");
        return "account_manage";
    }

    @RequestMapping(value = "/quitProject")
    public String quitProject() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        int projectId = Integer.parseInt(request.getParameter("pid"));
        Integer accountId = (Integer) session.getAttribute("accountId");
        log.debug("ProjectAccountController quitProject开始, 参数为accountId = {}, projectId = {}", accountId, projectId);

        int sqlResult = projectAccountService.deleteByProjectIdAndAccountId(projectId, accountId);
        if (sqlResult == 0) {
            request.setAttribute("msg", "系统错误，退出失败");
            return "login_success";
        }

        List<ProjectWithPower> projectList = projectService.getProjectPowerByAccountId(accountId);
        request.setAttribute("project", projectList);
        log.debug("ProjectAccountController quitProject结束");
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
}
