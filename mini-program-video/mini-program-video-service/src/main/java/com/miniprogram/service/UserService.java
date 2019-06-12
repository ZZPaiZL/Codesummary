package com.miniprogram.service;

import com.miniprogram.pojo.MiniUsers;
import com.miniprogram.pojo.MiniUsersReport;

public interface UserService {
	/**
	 * @Description:判断用户名是否存在 
	 */
	public boolean isExistUserName(String userName);
	
	/**
	 * @Description:保存用户
	 */
	public void saveUser(MiniUsers user);
	
	/**
	 * @Description:判断用户信息是否正确 
	 */
	public MiniUsers isExistUser(MiniUsers user);
	
	/**
	 * @Description:修改用户信息
	 */
	public void updateUserInfo(MiniUsers user);
	/**
	 * @Description:查询用户信息
	 */
	public MiniUsers queryUserInfo(String userId);
	/**
	 * 查询当前用户是否喜欢此视频
	 * @param userId
	 * @param videoId
	 * @return
	 */
	public boolean isLikeVideo(String userId,String videoId);
	/**
	 * 保存关注
	 * @param userId
	 * @param fansId
	 */
	public void saveUserFans(String userId,String fansId);
	
	/**
	 * 取消关注
	 * @param userId
	 * @param fansId
	 */
	public void delUserFans(String userId,String fansId);
	
	/*
	 * 查询是否是粉丝数
	 */
	public boolean isFollow(String userId,String fansId);
	/**
	 * 举报用户
	 * @param userReport
	 */
	public void reportUser(MiniUsersReport userReport);
}
