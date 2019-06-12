package com.miniprogram.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mini.program.util.MyMapper;
import com.miniprogram.pojo.vo.MiniVideosVo;

public interface MiniVideosMapperCustom extends MyMapper<MiniVideosVo> {
	
	public List<MiniVideosVo> queryAllVideos(@Param("videoDesc") String videoDesc,@Param("userId")String userId);
	/**
	 *对视频喜欢的数量进行累加
	 * @param videoId
	 */
	public void addVideoLikeCount(String videoId);
	
	/**
	 *对视频喜欢的数量进行累减
	 * @param videoId
	 */
	public void reduceVideoLikeCount(String videoId);
	
	/**
	 * 查询喜欢的视频
	 * @param userId
	 * @return
	 */
	public List<MiniVideosVo> queryMyLikeVideos(String userId);
	
	/**
	 * 查询关注的视频
	 * @param userId
	 * @return
	 */
	public List<MiniVideosVo> queryMyFollowVideos(String userId);
	
	//public int  delVideo(String videoId);

}