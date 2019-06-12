package com.miniprogram.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/hello")
@Api(value = "测试swagger2配置成功的接口", tags = { "测试swagger2配置成功的controller" })
public class HelloController {
	
	@RequestMapping("/hello")
	public String hello() {
		
		return "测试swagger2是否配置成功";
		
	}
}


