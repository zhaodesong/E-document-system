package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.po.Project;
import com.zhaodesong.Edocumentsystem.query.AccountQuery;
import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ProjectController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/newProject")
    public String newProject() {
        Long accountId=(Long)request.getAttribute("id");
        request.setAttribute("accountId",accountId);
        return "create_project";
    }

    @RequestMapping(value = "/createProject")
    public String createProject() {
        Long accountId = Long.parseLong(request.getParameter("accountId"));
        String name = request.getParameter("name");

        if (StringUtils.isEmpty(name)) {
            request.setAttribute("msg", "输入不能为空，请重新输入");
            return "login_success";
        }

        Project project = new Project();
        project.setCreateAccount(accountId);
        project.setName(name);

        if (projectService.insert(project) == 1) {
            request.setAttribute("msg", "创建成功");
            return "login_success";
        }
        request.setAttribute("msg", "创建失败,请稍后重试");
        return "login_success";
    }

    public String deleteProject() {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        return null;
    }
}
