package com.git.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.git.annotation.Controller;
import com.git.annotation.RequestMapping;
import com.git.annotation.RequestParameter;

@Controller
@RequestMapping(url="/user")
public class UserController {
	
	@RequestMapping(url ="/login.html", method="POST")
	public String login(@RequestParameter(value="userName") String userName,@RequestParameter(value="request") HttpServletRequest request, @RequestParameter(value="password") String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", "aaaaa");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 
		request.setAttribute("userName", "tanlei");
		return "index.jsp";
	}
}
