package com.zhaodesong.Edocumentsystem.base;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {
    protected static String FOLDER = "D://temp//";

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;

    protected boolean sessionCheck() {
        Integer accountId = (Integer) session.getAttribute("accountId");
        return accountId != null;
    }
}
