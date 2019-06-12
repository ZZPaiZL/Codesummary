package com.miniprogram.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mini.program.enums.VideoStautsEnum;
import com.mini.program.util.GetVideoImg;
import com.mini.program.util.MiniJSONResult;
import com.mini.program.util.MusicFuseVideo;
import com.mini.program.util.PageResult;
import com.miniprogram.pojo.MiniBgm;
import com.miniprogram.pojo.MiniComments;
import com.miniprogram.pojo.MiniUsers;
import com.miniprogram.pojo.MiniVideos;
import com.miniprogram.service.BgmService;
import com.miniprogram.service.VideoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "视频相关业务的接口", tags = { "视频相关业务的接口controller" })
@RequestMapping("/video")
public class VideoController extends BasicController{
	
	@Autowired
	private BgmService bgmService;
	
	@Autowired
	private VideoService videoService;
	
	//headers="content-type=multipart/form-data"这个api是为了让文件可选,不是为了填空
	@PostMapping( value="/upload",headers="content-type=multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="bgmId",value="背景音乐ID",required=false,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoSeconds",value="视频播放秒数",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoWidth",value="视频宽度",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoHeight",value="视频高度",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="desc",value="视频描述",required=false,dataType="String",paramType="form")
	})
	@ApiOperation(value = "用户上传视频", notes = "用户上传视频的接口")
	public MiniJSONResult uploadVideo(String userId,
			String bgmId,double videoSeconds,int videoWidth,int videoHeight,String desc,
			@ApiParam(value="视频文件" ,required=true) MultipartFile file) {
		
		if(StringUtils.isBlank(userId)) {
			return MiniJSONResult.errorMsg("用户id为空");
		}
		
		
//		//文件保存的命名地址
//		String filePath="F:\\EclipseWorkSpace\\mini-program-videos";
		
		
		//文件的最终保存路径
		String finalPath="";
		//数据库中的相对路径
		String uploadPathDB="/"+userId+"/video";
		//封面截图在数据库中的位置
		String coverPathDB="/"+userId+"/video";
		
		//文件流，Output用来写，Input用来读
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		//判断文件是否为空
		try {
				if(file!=null) {
					
				
				String fileName=file.getOriginalFilename();
				//生成图片前缀
				String coverNamePrefix=UUID.randomUUID().toString();
				if(StringUtils.isNotBlank(fileName)) {
					
					finalPath=FILEPATH+uploadPathDB+"/"+fileName;
					//设置数据库的保存位置
					uploadPathDB+=("/"+fileName);
					
					coverPathDB+=("/"+coverNamePrefix+".jpg");
					File fileOut =new File(finalPath);
					//判断文件是否存在
					if(fileOut.getParentFile()!=null || !fileOut.getParentFile().isDirectory()) {
						//不存在，进行创建
						fileOut.getParentFile().mkdirs();
					}
					fileOutputStream=new FileOutputStream(fileOut);
					inputStream=file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);//复制文件
					
				}

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
			//判断bgm是否为空，不为空，查询bgm的信息，并与视频合并，生成新的视频
			if(StringUtils.isNotBlank(bgmId)) {
				MiniBgm bgm=bgmService.queryBgmById(bgmId);
				String bgmPath=FILEPATH+bgm.getPath();//bgm所在路径
				
				MusicFuseVideo tool=new MusicFuseVideo(FFMPEG);
				String inputPath=finalPath;
				
				String videoName=UUID.randomUUID().toString()+".mp4";
				uploadPathDB="/"+userId+"/video"+"/"+videoName;
				finalPath =FILEPATH+uploadPathDB;
				
				try {
					tool.convertor(inputPath, bgmPath, videoSeconds, finalPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//生成新视频时，删除原来的视频，节约内存
				
			}
			System.out.println("uploadPathDB:"+uploadPathDB);
			System.out.println("finalPath:"+finalPath);
			System.out.println("coverPathDB:"+coverPathDB);
		
			//调用截图工具
			GetVideoImg toolCover=new GetVideoImg(FFMPEG);
			try {
				toolCover.convertor(finalPath, FILEPATH+coverPathDB);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//保存视屏到数据库
			MiniVideos video=new MiniVideos();
			video.setAudioId(bgmId);
			video.setUserId(userId);
			video.setVideoDesc(desc);
			video.setVideoHeight(videoHeight);
			video.setVideoWidth(videoWidth);
			video.setVideoSeconds((float)videoSeconds);
			video.setVideoPath(uploadPathDB);
			video.setStatus(VideoStautsEnum.SUCCESS.values);
			video.setCreateTime(new Date());
			video.setCoverPath(coverPathDB);
			
			
			String videoId=videoService.saveVideo(video);
			
			
		return MiniJSONResult.ok(videoId);

	}
	
	
	//此方法失效，不可用
	@PostMapping( value="/uploadCover",headers="content-type=multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoId",value="视频ID",required=true,dataType="String",paramType="form")
	})
	@ApiOperation(value = "上传视频封面", notes = "上传视频封面的接口")
	public MiniJSONResult uploadCover(String videoId,
			String userId,
			@ApiParam(value="视频封面" ,required=true) MultipartFile file) {
		
		if(StringUtils.isBlank(videoId)|| StringUtils.isBlank(userId)) {
			return MiniJSONResult.errorMsg("用户或视频id为空");
		}
		
		
//		//文件保存的命名地址
//		String filePath="F:\\EclipseWorkSpace\\mini-program-videos";
		
		
		//文件的最终保存路径
		String finalPath="";
		//数据库中的相对路径
		String uploadPathDB="/"+userId+"/video";
		
		//文件流，Output用来写，Input用来读
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		//判断文件是否为空
		try {
				
				String fileName=file.getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)) {
					
					finalPath=FILEPATH+uploadPathDB+"/"+fileName;
					//设置数据库的保存位置
					uploadPathDB+=("/"+fileName);
					File fileOut =new File(finalPath);
					//判断文件是否存在
					if(fileOut.getParentFile()!=null || !fileOut.getParentFile().isDirectory()) {
						//不存在，进行创建
						fileOut.getParentFile().mkdirs();
					}
					fileOutputStream=new FileOutputStream(fileOut);
					inputStream=file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);//复制文件
					
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
			//存放数据库中
			videoService.updateVideo(videoId, uploadPathDB);
			
		return MiniJSONResult.ok();

	}
	
	
	/**
	 * 
	 * isSave 当这个值为1时，进行热搜词保存，为0，不保存
	 */
	@PostMapping( value="/showVideos")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page",value="当前页数",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="video",value="视频信息",required=false,dataType="String",paramType="form"),
		@ApiImplicitParam(name="isSave",value="保存热搜词条件",required=false,dataType="String",paramType="query"),
	})
	@ApiOperation(value = "分页查询视频", notes = "分页查询视频的接口")
	public MiniJSONResult showVideos(@RequestBody MiniVideos video,Integer isSave, Integer page) {
		if(page == null) {
			page=1;
		}
		
		PageResult result=videoService.getAllVideos(isSave,video,page, PAGE_SIZE);
		
		return MiniJSONResult.ok(result);
	
	
	}
	
	@PostMapping( value="/getHots")
	@ApiOperation(value = "获得热搜词", notes = "获得热搜词的接口")
	public MiniJSONResult getHots() {

		return MiniJSONResult.ok(videoService.getHotWords());

	}
	@PostMapping( value="/userLike")
	@ApiOperation(value = "用户喜欢视频", notes = "用户喜欢视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoId",value="视频ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="createVideoId",value="视频发布者ID",required=true,dataType="String",paramType="form")
	
	})
	public MiniJSONResult userLike(String userId,String videoId,String createVideoId) {

		videoService.userLikeVideo(userId, videoId, createVideoId);
		
		return MiniJSONResult.ok();

	}
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoId",value="视频ID",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="createVideoId",value="视频发布者ID",required=true,dataType="String",paramType="form")
	
	})
	@PostMapping( value="/userUnLike")
	@ApiOperation(value = "用户不喜欢视频", notes = "用户不喜欢视频的接口")
	public MiniJSONResult userUnLike(String userId,String videoId,String createVideoId) {
		videoService.userUnLikeVideo(userId, videoId, createVideoId);
		return MiniJSONResult.ok();

	}
	
	@PostMapping( value="/showMyLike")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page",value="当前页数",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="userId",value="用户id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="pageSize",value="一页中分页数",required=true,dataType="String",paramType="query")
	})
	@ApiOperation(value = "查询我喜欢的视频", notes = "查询我喜欢的视频的接口")
	public MiniJSONResult showMyLike(String userId, Integer page,Integer pageSize) {
				
		if(StringUtils.isBlank(userId)) {
			return MiniJSONResult.errorMsg("用户id不能为空");
		}
		if(page == null) {
			page=1;
		}
		if(pageSize == null) {
			page=6;
		}
		PageResult result=videoService.queryMyLikeVideos(userId, page, pageSize);
		
		return MiniJSONResult.ok(result);
	}

	@PostMapping( value="/showMyFollow")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page",value="当前页数",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="userId",value="用户id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="pageSize",value="一页中分页数",required=true,dataType="String",paramType="query")
	})
	@ApiOperation(value = "查询关注用户的视频", notes = "查询关注用户的视频的接口")
	public MiniJSONResult showMyFollow(String userId, Integer page,Integer pageSize) {
				
		if(StringUtils.isBlank(userId)) {
			return MiniJSONResult.errorMsg("用户id不能为空");
		}
		if(page == null) {
			page=1;
		}
		if(pageSize == null) {
			page=6;
		}
		//PageResult result=videoService.queryMyLikeVideos(userId, page, pageSize);
		PageResult result=videoService.queryMyFollowVideos(userId, page, pageSize);
		return MiniJSONResult.ok(result);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="comments",value="评论信息",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="fatherCommentId",value="一条评论的id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="toUserId",value="被回复人id",required=true,dataType="String",paramType="query")
	})
	@PostMapping( value="/saveLeaveMsg")
	@ApiOperation(value = "用户留言", notes = "用户留言的接口")
	public MiniJSONResult saveLeaveMsg(@RequestBody MiniComments comments,String fatherCommentId,String toUserId) {
//		videoService.userUnLikeVideo(userId, videoId, createVideoId);
		
		videoService.saveComments(comments,fatherCommentId,toUserId);
		return MiniJSONResult.ok();

	}
	
	
	
	@PostMapping( value="/queryVideoComments")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page",value="当前页数",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="videoId",value="视频id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="pageSize",value="一页中分页数",required=true,dataType="String",paramType="query")
	})
	@ApiOperation(value = "查询视频留言", notes = "查询视频留言的接口")
	public MiniJSONResult queryVideoComments(String videoId, Integer page,Integer pageSize) {
				
		if(StringUtils.isBlank(videoId)) {
			return MiniJSONResult.errorMsg("");
		}
		if(page == null) {
			page=1;
		}
		if(pageSize == null) {
			page=10;
		}
//		PageResult result=videoService.queryMyLikeVideos(userId, page, pageSize);
		
		PageResult result=videoService.queryVideoComments(videoId, page, pageSize);
		
		return MiniJSONResult.ok(result);
	}

	
	@PostMapping( value="/delVideo")
	@ApiImplicitParam(name="page",value="当前页数",required=true,dataType="String",paramType="query")
	@ApiOperation(value = "删除视频", notes = "删除视频的接口")
	public MiniJSONResult delVideo(String videoId) {
				
		if(StringUtils.isBlank(videoId)) {
			return MiniJSONResult.errorMsg("");
		}
		//写接口
		int result=videoService.delVideo(videoId);
		if(result == 1) {
			return MiniJSONResult.ok();
		}
		
		return MiniJSONResult.errorMsg("删除失败");
	}
	
}


