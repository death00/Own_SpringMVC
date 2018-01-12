package com.git.service.impl;

import java.util.Map;

import com.git.annotation.Autowired;
import com.git.annotation.Service;
import com.git.service.MyService;
import com.git.service.SpringmvcService;

@Service
public class MyServiceImpl implements MyService {
	
	@Autowired
	SpringmvcService springmvcService;
	
//	@Autowired
//	MyService myService;
	
	@Override
	public int insert(Map map) {
		System.out.println("MyServiceImpl:" + "insert");
		springmvcService.insert(map);
//		System.out.println("再来一次");
//		myService.delete(map);
		return 0;
	}

	@Override
	public int delete(Map map) {
		System.out.println("MyServiceImpl:" + "delete");
		return 0;
	}

	@Override
	public int update(Map map) {
		System.out.println("MyServiceImpl:" + "update");
		return 0;
	}

	@Override
	public int select(Map map) {
		System.out.println("MyServiceImpl:" + "select");
		return 0;
	}
}
