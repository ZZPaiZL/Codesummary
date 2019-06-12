package com.vlog.service;

import java.util.Map;

import com.vlog.util.VlogJSONResult;

public interface IndexService {

	/**
	 * 显示首页信息
	 * @return
	 */
	public Map showInfo(Integer page, Integer pageSize);
	/**
	 * 查询更多日记
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Map showDiary(String id,String classify);
	
	/**
	 * 获取分类之下作品信息
	 * @param classify
	 * @return
	 */
	public Map showClassify(String category);
	
	public Map showDetail(String id);
	
}
