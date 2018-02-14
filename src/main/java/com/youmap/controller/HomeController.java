package com.youmap.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(HttpServletRequest request) {
    	if(request.isUserInRole("ROLE_USER"))
    		return "redirect:main";
        return "LoginPage.html";
    }   
    
    @RequestMapping("/editProfile")
    public String secret() {
        return "EditProfile.html";
    }
    
    @RequestMapping("/main")
    public String main() {
        return "MainPage.html";
    }
}