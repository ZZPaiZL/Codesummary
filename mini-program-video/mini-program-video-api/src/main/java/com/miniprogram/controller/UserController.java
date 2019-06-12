package com.miniprogram.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mini.program.util.MiniJSONResult;
import com.miniprogram.pojo.MiniUsers;
import com.miniprogram.pojo.MiniUsersReport;
import com.miniprogram.pojo.vo.MiniUsersVo;
import com.miniprogram.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户注册与登录
 */

@RestController
@Api(value = "用户相关业务的接口", tags = { "用户相关业务的接口controller" })
@RequestMapping("/user")
public class UserController extends BasicController {

	@Autowired
	private UserService userService;

	
	
	@PostMapping("/uploadFace")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query")
	@ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
	public MiniJSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files) {
		
		if(StringUtils.isBlank(userId)) {
			return MiniJSONResult.errorMsg("用户id为空");
		}
		
		
//		//文件保存的命名地址
//		String filePath="F:\\EclipseWorkSpace\\mini-program-videos";
		//数据库中的相对路径
		String uploadPathDB="/"+userId+"/face";
		
		//文件流，Output用来写，Input用来读
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		
		//将文件保存在服务器
		try {
			//判断文件是否为空
			if(files.length > 0 && files != null) {
				
				String fileName=files[0].getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)) {
					//文件的最终保存路径
					String finalPath=FILEPATH+uploadPathDB+"/"+fileName;
					//设置数据库的保存位置
					uploadPathDB+=("/"+fileName);
					File fileOut =new File(finalPath);
					//判断文件是否存在
					if(fileOut.getParentFile()!=null || !fileOut.getParentFile().isDirectory()) {
						//不存在，进行创建
						fileOut.getParentFile().mkdirs();
					}
					fileOutputStream=new FileOutputStream(fileOut);
					inputStream=files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);//复制文件
					
				}

			}else {
				return MiniJSONResult.errorMsg("上传失败");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MiniJSONResult.errorMsg("上传失败");
		} finally {
			if(fileOutputStream != null) {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		//将头像路径写入数据库
		MiniUsers user=new MiniUsers();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);
		
		return MiniJSONResult.ok(uploadPathDB);

	}
	
	
	@ApiImplicitParams({
	@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query"),
	@ApiImplicitParam(name="粉丝Id",value="粉丝ID",required=false,dataType="String",paramType="query")
	})
	@ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
	@PostMapping("/query")
	public MiniJSONResult query(String userId,String fanId) {
		
		if(StringUtils.isBlank(userId)) {
			return MiniJSONResult.errorMsg("用户id为空");
		}
		
		MiniUsers userInfo=userService.queryUserInfo(userId);
		MiniUsersVo userVo = new MiniUsersVo();
		BeanUtils.copyProperties(userInfo, userVo);
		
		userVo.setFollow(userService.isFollow(userId, fanId));

		return MiniJSONResult.ok(userVo);

	}
	
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="videoId",value="视频ID",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="createVideoId",value="视频发布者ID",required=true,dataType="String",paramType="query")
	
	})
	@ApiOperation(value = "查询用户与视频关系", notes = "查询用户与视频的关系接口")
	@PostMapping("/queryCreateVideo")
	public MiniJSONResult queryCreateVideo(String userId,String videoId,String createVideoId) {
		
		if(StringUtils.isBlank(createVideoId)) {
			return MiniJSONResult.errorMsg("");
		}
		//查询视频发布者信息
		MiniUsers userInfo=userService.queryUserInfo(createVideoId);
		MiniUsersVo createVideoVo = new MiniUsersVo();
		BeanUtils.copyProperties(userInfo, createVideoVo);
		//查询当前用户和视频的点赞关系
		boolean islike=userService.isLikeVideo(userId, videoId);
		createVideoVo.setIslikeVideo(islike);
		
		
		return MiniJSONResult.ok(createVideoVo);

	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="fanId",value="粉丝ID",required=true,dataType="String",paramType="query"),
		
	})
	@ApiOperation(value = "关注用户", notes = "关注用户的接口")
	@PostMapping("/becomefans")
	public MiniJSONResult becomefans(String userId,String fanId) {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return MiniJSONResult.errorMsg("");
		}
		
		userService.saveUserFans(userId, fanId);;
		
		return MiniJSONResult.ok("关注成功");

	}
	
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="fanId",value="粉丝ID",required=true,dataType="String",paramType="query"),
		
	})
	@ApiOperation(value = "取消关注用户", notes = "取消关注用户的接口")
	@PostMapping("/canclefans")
	public MiniJSONResult canclefans(String userId,String fanId) {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return MiniJSONResult.errorMsg("");
		}
		
		userService.delUserFans(userId, fanId);;
		
		return MiniJSONResult.ok("取消关注成功");

	}
	
	@ApiImplicitParam(name="userReport",value="举报信息",required=true,dataType="String",paramType="form")
	@ApiOperation(value = "举报用户", notes = "举报用户的接口")
	@PostMapping("/reportUser")
	public MiniJSONResult reportUser(@RequestBody MiniUsersReport userReport) {
		
		userService.reportUser(userReport);
		return MiniJSONResult.ok("举报成功");

	}
	
	
	
}
