package com.zhaodesong.Edocumentsystem.controller;

import com.zhaodesong.Edocumentsystem.service.AccountService;
import com.zhaodesong.Edocumentsystem.po.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute Account account) {
        //return accountService.loginCheck(account)?"成功":"失败";
        return "test";
    }

    /*
    public boolean Register(String mail, String pwd, String pwdRepeat) {
        if(pwd.equals(pwdRepeat)) {
            return false;
        }
        
    }
    */

}
