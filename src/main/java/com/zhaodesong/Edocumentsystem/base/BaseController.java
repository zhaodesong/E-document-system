package com.zhaodesong.Edocumentsystem.base;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ZhaoDesong
 * @date 2019-4-22 9:54
 */
public class BaseController {
    /**
     * 项目运行时存放文件的文件夹路径
     */
    protected static String FOLDER = "D://temp//";

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;

    protected boolean sessionCheck() {
        Integer accountId = (Integer) session.getAttribute("accountId");
        return accountId == null;
    }
}
