package com.zhaodesong.Edocumentsystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ProjectAccountController {
    @Autowired
    private HttpServletRequest request;

}
