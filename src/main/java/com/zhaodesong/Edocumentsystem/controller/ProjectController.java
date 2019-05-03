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
        log.debug("ProjectController createProject开始, 参数为accountId = {}, name = {}", accountId, name);

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
        } else {
            result.put("result", 0);
            result.put("msg", "创建失败");
        }
        log.debug("ProjectController createProject结束");
        return result;
    }

    @RequestMapping(value = "/deleteProject")
    @ResponseBody
    public Object deleteProject() {
        Map<String, Object> result = new HashMap<>();
        int projectId = Integer.parseInt(request.getParameter("pid"));
        log.debug("ProjectController deleteProject开始, 参数为projectId = {}", projectId);

        int sqlResult1 = projectService.deleteById(projectId);
        int sqlResult2 = projectAccountService.deleteByProjectId(projectId);
        int sqlResult3 = documentService.deleteByProjectId(projectId);

        FileUtils.deleteDir(new File(FOLDER + projectId));

        if (sqlResult1 >= 1 && sqlResult2 >= 1 && sqlResult3 >= 1) {
            result.put("msg", "删除成功");
            result.put("result", 1);
        } else {
            result.put("msg", "删除失败");
            result.put("result", 0);
        }
        log.debug("ProjectController deleteProject结束");
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
        log.debug("ProjectController toProject开始, 参数为projectId = {}, accountId = {}", projectId, accountId);

        List<DocumentWithPower> documentList = documentService.getAllDocInfoByProjectId(projectId, 0);
        for (DocumentWithPower doc : documentList) {
            if (hasPermission(projectId, accountId, doc.getDocId())) {
                doc.setIsEdit("1");
            } else {
                doc.setIsEdit("0");
            }
        }

        Project project = projectService.getProjectById(projectId);
        request.setAttribute("documents", documentList);
        session.setAttribute("projectName", project.getName());
        session.setAttribute("projectId", projectId);
        log.debug("ProjectController toProject结束");
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
        log.debug("ProjectController toSingleFolder开始, 参数为projectId = {}, accountId = {}, docId = {}", projectId, accountId, docId);

        List<DocumentWithPower> documentList = documentService.getAllDocInfoByParentId(docId);
        for (DocumentWithPower doc : documentList) {
            if (hasPermission(projectId, accountId, doc.getDocId())) {
                doc.setIsEdit("1");
            } else {
                doc.setIsEdit("0");
            }
        }

        request.setAttribute("documents", documentList);
        request.setAttribute("parentId", docId);
        log.debug("ProjectController toSingleFolder结束");
        return "single_folder";
    }

    @RequestMapping("/toLoginSuccess")
    public String toLoginSuccess() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer accountId = (Integer) session.getAttribute("accountId");
        log.debug("ProjectController toLoginSuccess开始, 参数为 accountId = {} ", accountId);
        Account account = accountService.getById(accountId);

        List<ProjectWithPower> projectList = projectService.getProjectPowerByAccountId(accountId);
        request.setAttribute("nickName", account.getNickName());
        request.setAttribute("project", projectList);
        log.debug("ProjectController toLoginSuccess结束");
        return "login_success";
    }

    @RequestMapping("/toRecycleBin")
    public String toRecycleBin() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        log.debug("ProjectController toRecycleBin开始, 参数为 projectId = {} ", projectId);

        List<Document> documentList = documentService.getAllDelectDocByProjectId(projectId);

        request.setAttribute("documents", documentList);
        session.setAttribute("projectId", projectId);
        log.debug("ProjectController toRecycleBin结束");
        return "recycle_bin";
    }

    @RequestMapping("/renameProject")
    @ResponseBody
    public Object projectRename() {
        Map<String, Object> result = new HashMap<>();
        Integer projectId = (Integer) session.getAttribute("projectId");
        String newName = request.getParameter("newName");
        log.debug("ProjectController projectRename开始, 参数为 projectId = {}, newName = {} ", projectId, newName);
        int sqlResult = projectService.renameProject(projectId, newName);
        if (sqlResult >= 1) {
            result.put("msg", "修改成功");
            result.put("result", 1);
        } else {
            result.put("msg", "修改失败");
            result.put("result", 0);
        }

        log.debug("ProjectController projectRename结束");
        return result;
    }

    @RequestMapping("toProjectManage")
    public String toProjectManage() {
        if (sessionCheck()) {
            request.setAttribute("msg", "登录失效，请重新登录");
            return "index";
        }
        Integer projectId = Integer.parseInt(request.getParameter("pid"));
        log.debug("ProjectController toProjectManage开始, 参数为 projectId = {}", projectId);

        session.setAttribute("projectId", projectId);
        request.setAttribute("projectName", projectService.getProjectById(projectId).getName());

        log.debug("ProjectController toProjectManage结束");
        return "project_manage";
    }

    @RequestMapping("transferProject")
    @ResponseBody
    public Object transferProject() {
        Map<String, Object> result = new HashMap<>();
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String transferMail = request.getParameter("transferMail");
        Integer accountId = (Integer) session.getAttribute("accountId");
        log.debug("ProjectController transferProject开始, 参数为 projectId = {}, transferMail = {}, accountId = {}", projectId, transferMail, accountId);

        // 查询该用户是否存在
        Account account = accountService.getByMail(transferMail);
        if (account == null) {
            result.put("msg", "该用户不存在或未加入该项目");
            result.put("result", 0);
            return result;
        }
        // 查询该用户是否加入该项目
        ProjectAccount projectAccount = projectAccountService.getByProjectIdAndAccountId(projectId, account.getId());
        if (projectAccount == null) {
            result.put("msg", "该用户不存在或未加入该项目");
            result.put("result", 0);
            return result;
        }

        int sqlResult1 = projectService.changeCreateAccount(projectId, account.getId());

        // 修改projectAccount中的记录
        int sqlResult2 = projectAccountService.transferProject(projectId, accountId, account.getId());
        if (sqlResult1 >= 1 && sqlResult2 >= 1) {
            result.put("msg", "转让成功");
            result.put("result", 1);
        } else {
            result.put("msg", "转让失败");
            result.put("result", 0);
        }

        log.debug("ProjectController transferProject结束");
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
        return document.getPower() == 1 && "10".equals(p);
    }
}
