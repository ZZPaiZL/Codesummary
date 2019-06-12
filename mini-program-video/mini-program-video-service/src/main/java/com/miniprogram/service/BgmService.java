package com.miniprogram.service;

import java.util.List;

import com.miniprogram.pojo.MiniBgm;
import com.miniprogram.pojo.MiniUsers;

public interface BgmService {
	
	/**
	 * @Description:查询背景音乐
	 */
	public List<MiniBgm> queryBgmList();
	/**
	 * @Description:通过bgmId查询背景音乐
	 */
	public MiniBgm queryBgmById(String bgmId);
	
}
