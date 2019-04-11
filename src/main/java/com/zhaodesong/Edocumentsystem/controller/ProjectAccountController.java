package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class ProjectAccountController extends BaseController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProjectAccountService projectAccountService;

//    @RequestMapping("")
//    public String inviteMember() {
//        if (!sessionCheck()) {
//            request.setAttribute("msg", "登录失效，请重新登录");
//            return "index";
//        }
//        Integer accountId = (Integer) session.getAttribute("accountId");
//        Integer projectId = (Integer) session.getAttribute("projectId");
//        String inviteMail = request.getParameter("inviteMail");
//        if (!hasPermissionEditAccount(projectId,accountId)){
//            request.setAttribute("msg", "您没有操作权限");
//            return;
//        }
//        Account account = accountService.getByMail(inviteMail);
//        if (account == null) {
//            request.setAttribute("msg", "该用户不存在");
//            return;
//        }
//
//        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, account.getId());
//        if (projectAccount != null) {
//            request.setAttribute("msg", "该用户已在项目中");
//            return;
//        }
//        ProjectAccount insertAccount = new ProjectAccount();
//        insertAccount.setAccountId(accountId);
//        insertAccount.setProjectId(projectId);
//        // TODO 根据用户ID设置不同的权限
//        projectAccount.setPermission("00");
//        projectAccountService.insert(projectAccount);
//        request.setAttribute("msg", "添加新成员成功");
//        return;
//    }
//
//    @RequestMapping("")
//    public String deleteMember() {
//        if (!sessionCheck()) {
//            request.setAttribute("msg", "登录失效，请重新登录");
//            return "index";
//        }
//        Integer accountId = (Integer) session.getAttribute("accountId");
//        Integer projectId = (Integer) session.getAttribute("projectId");
//        Integer deleteId = Integer.parseInt(request.getParameter("deleteId"));
//        if (hasPermissionEditAccount(projectId,accountId)){
//            projectAccountService.deleteByProjectIdAndAccountId(projectId, deleteId);
//            request.setAttribute("msg", "移除成功");
//            return;
//        }
//        request.setAttribute("msg", "您没有操作权限");
//        return;
//    }


    public Object changePermissionAccount() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Map<String, Object> result = new HashMap<>();
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer changeId = Integer.parseInt(request.getParameter("changeId"));
        String p = request.getParameter("permission");
        if (!"00".equals(p) && !"01".equals(p) && !"11".equals(p)){
            result.put("msg", "参数错误");
            return result;
        }

        if (hasPermissionEditAccount(projectId,accountId)){
            ProjectAccount pa = new ProjectAccount();
            pa.setProjectId(projectId);
            pa.setAccountId(changeId);
            pa.setPermission(p);
            projectAccountService.updatePermission(pa);
        }
        result.put("msg", "修改成功");
        return result;
    }

    public Object changePermissionDoc() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Map<String, Object> result = new HashMap<>();
        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer changeId = Integer.parseInt(request.getParameter("changeId"));
        String p = request.getParameter("permission");
        if (!"00".equals(p) && !"01".equals(p) && !"11".equals(p)){
            result.put("msg", "参数错误");
            return result;
        }

        if (hasPermissionEditDoc(projectId,accountId)){
            ProjectAccount pa = new ProjectAccount();
            pa.setProjectId(projectId);
            pa.setAccountId(changeId);
            pa.setPermission(p);
            projectAccountService.updatePermission(pa);
        }
        result.put("msg", "修改成功");
        return result;
    }

    private boolean hasPermissionEditAccount(Integer projectId, Integer accountId) {
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
