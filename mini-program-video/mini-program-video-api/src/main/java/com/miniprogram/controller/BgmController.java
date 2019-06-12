package com.miniprogram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini.program.util.MiniJSONResult;
import com.miniprogram.service.BgmService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "背景音乐相关业务接口", tags = { "背景音乐相关业务接口controller" })
@RequestMapping("/bgm")
public class BgmController {
	
	@Autowired
	private BgmService BgmService;
	
	@ApiOperation(value = "背景音乐", notes = "背景音乐的接口")
	@PostMapping("/bgmlist")
	public MiniJSONResult bgmlist() {
		
		return MiniJSONResult.ok(BgmService.queryBgmList());
		
	}
}

