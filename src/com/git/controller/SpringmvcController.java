package com.git.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.git.annotation.Controller;
import com.git.annotation.Quatifier;
import com.git.annotation.RequestMapping;
import com.git.service.MyService;
import com.git.service.SpringmvcService;

@Controller("git")
public class SpringmvcController {
	@Quatifier("MyServiceImpl")
    MyService myService;
	
    @Quatifier("SpringmvcServiceImpl")
    SpringmvcService smService;
    
    @RequestMapping("insert")
    public String insert(HttpServletRequest request, HttpServletResponse response, String param) {
        myService.insert(null);
        smService.insert(null);
        return null;
    }

    @RequestMapping("delete")
    public String delete(HttpServletRequest request, HttpServletResponse response, String param) {
        myService.delete(null);
        smService.delete(null);
        return null;
    }

    @RequestMapping("update")
    public String update(HttpServletRequest request, HttpServletResponse response, String param) {
        myService.update(null);
        smService.update(null);
        return null;
    }

    @RequestMapping("select")
    public String select(HttpServletRequest request, HttpServletResponse response, String param) {
        myService.select(null);
        smService.select(null);
        return null;
    }
}
