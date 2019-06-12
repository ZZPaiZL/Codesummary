package com.miniprogram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniprogram.util.RedisOperator;

@RestController
public class BasicController {
	
		@Autowired
	    public RedisOperator redis;
		
		public static final String USER_REDIS_SESSION="user-redis-session";
		
		//文件保存的命名地址
		public static final	String FILEPATH="F:\\EclipseWorkSpace\\mini-program-videos";
		//ffmpeg工具的路径
		public static final String FFMPEG="E:\\ffmpeg\\ffmpeg_win64\\bin\\ffmpeg.exe";

		//每页分页的记录数
		public static final Integer PAGE_SIZE=6;
		
	
		
}


