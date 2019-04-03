package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.query.DocumentQuery;
import com.zhaodesong.Edocumentsystem.query.ProjectQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import com.zhaodesong.Edocumentsystem.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class ProjectController {
    private static String FOLDER = "D://temp//";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AccountService accountService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectAccountService projectAccountService;

    @RequestMapping(value = "/newProject")
    public String newProject(HttpSession session) {
        return "create_project";
    }

    @RequestMapping(value = "/createProject")
    public String createProject(HttpSession session) {
        Integer accountId = (Integer) session.getAttribute("accountId");
        String mail = (String) session.getAttribute("mail");
        Account account = accountService.loginCheck(mail);
        if (account == null) {
            request.setAttribute("msg", "登录已失效，请重新登录");
            return "index";
        }
        String name = request.getParameter("projectName");

        if (StringUtils.isEmpty(name)) {
            request.setAttribute("msg", "输入不能为空，请重新输入");
            return "login_success";
        }

        // project表中创建条目
        Project project = new Project();
        project.setCreateAccount(accountId);
        project.setName(name);
        int insertFlag = projectService.insert(project);

        // projectAccount表中创建条目
        ProjectAccount projectAccount = new ProjectAccount();
        projectAccount.setAccountId(accountId);
        projectAccount.setProjectId(project.getId());
        // TODO 根据用户ID设置不同的权限
        projectAccount.setPermission((byte) 3);

        // 磁盘中创建对应文件夹
        FileUtils.createDir(FOLDER + project.getId());

        if (insertFlag == 1 && projectAccountService.insert(projectAccount) == 1) {
            request.setAttribute("msg", "创建成功");
            return "redirect:/toLoginSuccess";
        }
        request.setAttribute("msg", "创建失败,请稍后重试");
        return "redirect:/toLoginSuccess";
    }

    /*
    TODO 未进行权限验证
     */
    @RequestMapping(value = "/deleteProject")
    public String deleteProject(HttpSession session) {
        int projectId = Integer.parseInt(request.getParameter("pid"));
        Integer accountId = (Integer) session.getAttribute("accountId");
        projectService.deleteById(projectId);
        projectAccountService.deleteByProjectId(projectId);
        documentService.deleteByProjectId(projectId);
        request.setAttribute("msg", "删除成功");
        ProjectQuery projectQuery = new ProjectQuery();
        projectQuery.setCreateAccount(accountId);
        // 查询该用户的项目信息
        List<Project> projectList = projectService.getProjectNotNull(projectQuery);
        request.setAttribute("project", projectList);
        return "login_success";
    }

    @RequestMapping("/toProject")
    public String project(HttpSession session) {
        Integer accountId = (Integer) session.getAttribute("accountId");
        String mail = (String) session.getAttribute("mail");
        Integer projectId = Integer.parseInt(request.getParameter("pid"));

        DocumentQuery query = new DocumentQuery();
        query.setProjectId(projectId);
        // 查询该项目下的所有文件
        List<Document> documentList = documentService.findNotNull(query);

        request.setAttribute("documents", documentList);
        session.setAttribute("projectId", projectId);
        return "project";
    }

    @RequestMapping("/toLoginSuccess")
    public String toLoginSuccess(HttpSession session) {
        Integer accountId = (Integer) session.getAttribute("accountId");

        Account account = accountService.getById(accountId);

        request.setAttribute("nickName", account.getNickName());

        ProjectQuery projectQuery = new ProjectQuery();
        projectQuery.setCreateAccount(account.getId());
        // 查询该用户的项目信息
        List<Project> projectList = projectService.getProjectNotNull(projectQuery);
        if (projectList != null || projectList.size() == 0) {
            request.setAttribute("project", projectList);
        }
        return "login_success";
    }
}
