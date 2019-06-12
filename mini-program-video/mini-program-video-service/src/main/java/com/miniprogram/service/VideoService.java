package com.miniprogram.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mini.program.util.PageResult;
import com.miniprogram.pojo.MiniBgm;
import com.miniprogram.pojo.MiniComments;
import com.miniprogram.pojo.MiniUsers;
import com.miniprogram.pojo.MiniVideos;

public interface VideoService {
	
	/**
	 * 保存视频
	 * @param video
	 * @return
	 */
	public String saveVideo(MiniVideos video);
	
	/**
	 * 更新视频
	 * @param video
	 * @return
	 */
	public void updateVideo(String videoId,String coverPath);
	
	/**
	 * 分页查询视频
	 */
	public PageResult getAllVideos(Integer isSave,MiniVideos video,Integer page,Integer pageSize);
	
	/**
	 * 获得热搜词
	 */
	public List<String> getHotWords();
	
	/**
	 * 用户喜欢视频
	 * @param userId
	 * @param videoId
	 * @param createVideoId
	 */
	public void userLikeVideo(String userId,String videoId,String createVideoId);
	
	/**
	 * 用户不喜欢视频
	 * @param userId
	 * @param videoId
	 * @param createVideoId
	 */
	public void userUnLikeVideo(String userId,String videoId,String createVideoId);
	
	/**
	 * 查询用户喜欢的视频
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PageResult queryMyLikeVideos(String userId, Integer page,Integer pageSize);
	
	/**
	 * 查询关注的视频
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	
	public PageResult queryMyFollowVideos(String userId, Integer page,Integer pageSize);

	/**
	 * 保存留言信息
	 */
	
	public void saveComments(MiniComments comments,String fatherCommentId,String toUserId);
	
	
	/**
	 * 查询留言，并进行分页显示
	 */
	public PageResult queryVideoComments(String videoId, Integer page,Integer pageSize);
	
	/**
	 * 删除视频
	 * @param videoId
	 * @return
	 */
	
	public int delVideo(String videoId);
}
