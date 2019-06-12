package com.miniprogram.mapper;

import com.mini.program.util.MyMapper;
import com.miniprogram.pojo.MiniUsers;

public interface MiniUsersMapper extends MyMapper<MiniUsers> {
	
	/**
	 * 用户受喜欢的数量累加
	 * @param userId
	 */
	public void addReceiveLikeCount(String createVideoId);
	/**
	 * 用户受喜欢的数量累减
	 * @param userId
	 */
	public void reduceReceiveLikeCount(String createVideoId);
	/**
	 * 增加粉丝数量
	 * @param userId
	 */
	public void addFansCount(String userId);
	/**
	 * 减少粉丝数量
	 * @param userId
	 */
	public void reduceFansCount(String userId);
	/**
	 * 增加关注数量
	 * @param userId
	 */
	public void addfollowCount(String userId);
	/**
	 * 减少关注数量
	 * @param userId
	 */
	public void reducefollowCount(String userId);
	
	
}