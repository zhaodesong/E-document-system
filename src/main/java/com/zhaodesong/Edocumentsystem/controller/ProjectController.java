package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.base.BaseController;
import com.zhaodesong.Edocumentsystem.po.Account;
import com.zhaodesong.Edocumentsystem.po.Document;
import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.po.ProjectAccount;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.DocumentService;
import com.zhaodesong.Edocumentsystem.service.ProjectAccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import com.zhaodesong.Edocumentsystem.util.FileUtils;
import com.zhaodesong.Edocumentsystem.vo.DocumentWithPower;
import com.zhaodesong.Edocumentsystem.vo.ProjectWithPower;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
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
        projectAccount.setPermission("111");

        // 磁盘中创建对应文件夹
        FileUtils.createDir(FOLDER + project.getId());

        if (insertFlag == 1 && projectAccountService.insert(projectAccount) == 1) {
            result.put("result", 1);
            result.put("msg", "创建成功");
            ProjectWithPower projectWithPower = new ProjectWithPower();
            projectWithPower.setId(project.getId());
            projectWithPower.setName(name);
            projectWithPower.setPower("111");
            result.put("project", projectWithPower);
            return result;
        }
        result.put("result", false);
        result.put("msg", "创建失败");
        return result;
    }

    @RequestMapping(value = "/deleteProject")
    @ResponseBody
    public Object deleteProject() {
        Map<String, Object> result = new HashMap<>();
        int projectId = Integer.parseInt(request.getParameter("pid"));
        Integer accountId = (Integer) session.getAttribute("accountId");
        projectService.deleteById(projectId);
        projectAccountService.deleteByProjectId(projectId);
        documentService.deleteByProjectId(projectId);

        FileUtils.deleteDir(new File(FOLDER + projectId));

//        // 查询该用户加入的项目
//        List<ProjectWithPower> projectList = projectService.getProjectPowerByAccountId(accountId);
//        request.setAttribute("project", projectList);

        result.put("msg", "删除成功");
        result.put("result", 1);
        return result;
    }

    @RequestMapping("/toProject")
    public String toProject() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        Integer accountId = (Integer) session.getAttribute("accountId");
        // 查询该项目下的所有文件
        List<DocumentWithPower> documentList = documentService.getAllDocInfoByProjectId(projectId, 0);
        for (int i = 0; i < documentList.size(); i++) {
            DocumentWithPower doc = documentList.get(i);
            if (hasPermission(projectId, accountId, doc.getDocId())) {
                doc.setIsEdit("1");
            } else {
                doc.setIsEdit("0");
            }
        }

        request.setAttribute("documents", documentList);
        session.setAttribute("projectId", projectId);
        return "project";
    }

    @RequestMapping("/toSingleFolder")
    public String toSingleFolder() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer projectId = (Integer) session.getAttribute("projectId");
        Integer accountId = (Integer) session.getAttribute("accountId");
        Long docId = Long.parseLong(request.getParameter("docId"));
        // 查询该项目下的所有文件
        List<DocumentWithPower> documentList = documentService.getAllDocInfoByParentId(docId);
        for (int i = 0; i < documentList.size(); i++) {
            DocumentWithPower doc = documentList.get(i);
            if (hasPermission(projectId, accountId, doc.getDocId())) {
                doc.setIsEdit("1");
            } else {
                doc.setIsEdit("0");
            }
        }

        request.setAttribute("documents", documentList);
        request.setAttribute("parentId", docId);
        request.setAttribute("title", "流云文档");
        return "single_folder";
    }

    @RequestMapping("/toLoginSuccess")
    public String toLoginSuccess() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        Account account = accountService.getById(accountId);
        request.setAttribute("nickName", account.getNickName());
        // 查询该用户加入的项目
        List<ProjectWithPower> projectList = projectService.getProjectPowerByAccountId(accountId);
        request.setAttribute("project", projectList);
        return "login_success";
    }

    @RequestMapping("/toRecycleBin")
    public String project() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        // 查询该项目下的所有文件
        List<Document> documentList = documentService.getAllDelectDocByProjectId(projectId);

        request.setAttribute("documents", documentList);
        session.setAttribute("projectId", projectId);
        return "recycle_bin";
    }


    @RequestMapping("/renameProject")
    @ResponseBody
    public Object projectRename() {
        Map<String, Object> result = new HashMap<>();
        Integer projectId = (Integer) session.getAttribute("projectId");
        String newName = request.getParameter("newName");

        projectService.renameProject(projectId, newName);
        result.put("msg", "修改成功");
        result.put("result", 1);
        return result;
    }

    @RequestMapping("toProjectManage")
    public String toProjectManage() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
//        Integer accountId = (Integer) session.getAttribute("accountId");
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        session.setAttribute("projectId", projectId);
        request.setAttribute("projectName", projectService.getProjectById(projectId).getName());

        return "project_manage";
    }

    @RequestMapping("transferProject")
    @ResponseBody
    public Object transferProject() {
        Map<String, Object> result = new HashMap<>();
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String transferMail = request.getParameter("transferMail");
        Integer accountId = (Integer) session.getAttribute("accountId");


        Account account = accountService.getByMail(transferMail);
        if (account == null) {
            result.put("msg", "该用户不存在或未加入该项目");
            result.put("result", 0);
            return result;
        }
        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, account.getId());
        if (projectAccount == null) {
            result.put("msg", "该用户不存在或未加入该项目");
            result.put("result", 0);
            return result;
        }

        projectService.changeCreateAccount(projectId, account.getId());

        // 修改projectAccount
        projectAccountService.transferProject(projectId, accountId, account.getId());
        result.put("msg", "转让成功");
        result.put("result", 1);
        return result;
    }

    private boolean hasPermission(Integer projectId, Integer accountId, Long docId) {
        // 判断是否为高权限者
        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, accountId);
        String p = projectAccount.getPermission();
        if ("11".equals(p) || "111".equals(p)) {
            return true;
        }

        // 判断是否为文件拥有者
        Document document = documentService.getAllDocInfoByDocId(docId, 0).get(0);
        if (!document.getAccountIdCreate().equals(accountId)) {
            return true;
        }

        // 判断文档本身权限和账户权限
        if (document.getPower() == 1 && "10".equals(p)) {
            return true;
        }
        return false;
    }
}
