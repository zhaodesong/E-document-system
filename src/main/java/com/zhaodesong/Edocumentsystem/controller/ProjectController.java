package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProjectController extends BaseController {
    private static String FOLDER = "D://temp//";

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
    @ResponseBody
    public Object createProject() {
        Map<String, Object> result = new HashMap<>();
        Integer accountId = (Integer) session.getAttribute("accountId");

        String name = request.getParameter("projectName");

        if (StringUtils.isEmpty(name)) {
            result.put("result", false);
            result.put("msg", "输入不能为空");
            return result;
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
        projectAccount.setPermission("11");

        // 磁盘中创建对应文件夹
        FileUtils.createDir(FOLDER + project.getId());

        if (insertFlag == 1 && projectAccountService.insert(projectAccount) == 1) {
            result.put("result", true);
            result.put("msg", "创建成功");
            result.put("project", projectService.getProjectById(project.getId()));
            return result;
        }
        result.put("result", false);
        result.put("msg", "创建失败");
        return result;
    }

    /*
    TODO 未进行权限验证
     */
    @RequestMapping(value = "/deleteProject")
    public String deleteProject() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }

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
    public String project() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        // 查询该项目下的所有文件
        List<Document> documentList = documentService.getAllDocInfoByProjectId(projectId);

        request.setAttribute("documents", documentList);
        session.setAttribute("projectId", projectId);
        return "project";
    }

    @RequestMapping("/toLoginSuccess")
    public String toLoginSuccess() {
        if (!sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        Account account = accountService.getById(accountId);
        request.setAttribute("nickName", account.getNickName());
        // 查询该用户加入的项目
        List<ProjectAccount> projectAccountList = projectAccountService.getProjectAccountByAccountId(accountId);
        if (projectAccountList != null || projectAccountList.size() == 0) {
            List<Project> projectList = new ArrayList<>();
            for (int i = 0; i < projectAccountList.size(); i++) {
                projectList.add(projectService.getProjectById(projectAccountList.get(i).getProjectId()));
            }
            request.setAttribute("project", projectList);
        }
        return "login_success";
    }

//    @RequestMapping("/toLoginSuccess")
//    @ResponseBody
//    public Object projectRename() {
//        if (!sessionCheck()) {
//            request.setAttribute("msg", "登录失效，请重新登录");
//            return "index";
//        }
//        Map<String, Object> result = new HashMap<>();
//        Integer projectId = (Integer) session.getAttribute("projectId");
//        String newName = request.getParameter("newName");
//
//        projectService.renameProject(projectId, newName);
//        result.put("msg", "修改成功");
//        return result;
//    }
}
