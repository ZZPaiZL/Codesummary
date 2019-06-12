package com.miniprogram.mapper;

import java.util.List;

import com.mini.program.util.MyMapper;
import com.miniprogram.pojo.vo.MiniCommentsVo;

public interface MiniCommentsMapperCustom extends MyMapper<MiniCommentsVo> {
	public List<MiniCommentsVo> queryComments(String videoId);
}