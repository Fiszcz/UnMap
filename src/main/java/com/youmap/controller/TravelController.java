package com.youmap.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TravelController {
    @RequestMapping("/travelCreator")
    public String travelCreator() {
        return "EdycjaMap.html";
    }   
    
    @GetMapping("/travelViewer")
    public String travelViewer(@RequestParam String tr, HttpServletRequest request) {
    	if(request.isUserInRole("ROLE_ADMIN"))
    		return "ViewTravel1.html";
        return "ViewTravel.html";
    }   
}