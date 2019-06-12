package com.miniprogram.mapper;

import com.mini.program.util.MyMapper;
import com.miniprogram.pojo.MiniUsersLikeVideos;

public interface MiniUsersLikeVideosMapper extends MyMapper<MiniUsersLikeVideos> {
	public void deleteLikeVideo(String userId, String videoId);
}