package com.miniprogram.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mini.program.util.MD5Utils;
import com.mini.program.util.PageResult;
import com.mini.program.util.TimeFormat;
import com.miniprogram.mapper.MiniBgmMapper;
import com.miniprogram.mapper.MiniCommentsMapper;
import com.miniprogram.mapper.MiniCommentsMapperCustom;
import com.miniprogram.mapper.MiniSearchRecordsMapper;
import com.miniprogram.mapper.MiniUsersLikeVideosMapper;
import com.miniprogram.mapper.MiniUsersMapper;
import com.miniprogram.mapper.MiniVideosMapperCustom;
import com.miniprogram.mapper.MiniVideosMapper;
import com.miniprogram.pojo.MiniBgm;
import com.miniprogram.pojo.MiniComments;
import com.miniprogram.pojo.MiniSearchRecords;
import com.miniprogram.pojo.MiniUsersLikeVideos;
import com.miniprogram.pojo.MiniVideos;
import com.miniprogram.pojo.vo.MiniCommentsVo;
import com.miniprogram.pojo.vo.MiniVideosVo;
import com.miniprogram.service.BgmService;
import com.miniprogram.service.UserService;
import com.miniprogram.service.VideoService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoServiceImpl implements VideoService {
		
	@Autowired
	private MiniVideosMapper videoMapper;
	
	@Autowired
	private MiniCommentsMapperCustom commentsMapperCustom;
	
	
	@Autowired
	private MiniCommentsMapper commentsMapper;
	
	
	@Autowired
	private MiniVideosMapperCustom videoCustomMapper;
	
	@Autowired
	private MiniSearchRecordsMapper SearchRecordsMapper;
	
	@Autowired
	private MiniUsersLikeVideosMapper usersLikeVideosMapper;
	
	@Autowired
	private  MiniUsersMapper usersMapper;
	
//	@Transactional(propagation=Propagation.SUPPORTS)使用当前同一事务，不创建事务
//	
//	@Transactional(propagation=Propagation.SUPPORTS)
//	//@Transactional(propagation=Propagation.REQUIRED)查看有没有事务，有，使用，没有则创建事务
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public String saveVideo(MiniVideos video) {
		
		String videoId=UUID.randomUUID().toString();
		video.setId(videoId);
		videoMapper.insertSelective(video);
		return videoId;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {
		MiniVideos video=new MiniVideos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videoMapper.updateByPrimaryKeySelective(video);
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public PageResult getAllVideos(Integer isSave,MiniVideos video,Integer page, Integer pageSize) {
		
		String desc=video.getVideoDesc();
		String userId=video.getUserId();
		//保存热搜词
		if(isSave != null && isSave == 1) {
			MiniSearchRecords searchRecords =new MiniSearchRecords();
			searchRecords.setId(UUID.randomUUID().toString());
			searchRecords.setContent(desc);
			SearchRecordsMapper.insert(searchRecords);
		}
		
		
		
		PageHelper.startPage(page, pageSize);
		List<MiniVideosVo> list = videoCustomMapper.queryAllVideos(desc,userId);
		PageInfo<MiniVideosVo> pageInfo=new PageInfo<>(list);
		PageResult result=new PageResult();
		result.setPage(page);
		result.setCounts(pageInfo.getTotal());
		result.setTotal(pageInfo.getPages());
		result.setRows(list);
		return result;
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {
		return SearchRecordsMapper.getHotWords();
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String createVideoId) {
		//1.增加用户和视屏喜欢关联表
		MiniUsersLikeVideos usersLikeVideos=new MiniUsersLikeVideos();
		String id=UUID.randomUUID().toString();
		usersLikeVideos.setId(id);
		usersLikeVideos.setUserId(userId);
		usersLikeVideos.setVideoId(videoId);
		usersLikeVideosMapper.insert(usersLikeVideos);
		
		//2.视屏喜欢数量累加
		videoCustomMapper.addVideoLikeCount(videoId);
		
		//3.用户喜欢数量累加
		usersMapper.addReceiveLikeCount(createVideoId);
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String createVideoId) {
		//1.删除用户和视屏喜欢关联表
				Example examp=new Example(MiniUsersLikeVideos.class);
				Criteria criteria=examp.createCriteria();
				criteria.andEqualTo("userId",userId);	
				criteria.andEqualTo("videoId",videoId);
				usersLikeVideosMapper.deleteByExample(examp);
				//System.out.println(userId);
				//usersLikeVideosMapper.deleteLikeVideo(userId, videoId);
				//2.视屏喜欢数量累减
				videoCustomMapper.reduceVideoLikeCount(videoId);
				//3.用户喜欢数量累减
				usersMapper.reduceReceiveLikeCount(createVideoId);
		
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public PageResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		
		List<MiniVideosVo> list = videoCustomMapper.queryMyLikeVideos(userId);
		
		PageInfo<MiniVideosVo> pageInfo=new PageInfo<>(list);
		
		PageResult result=new PageResult();
		
		result.setPage(page);
		result.setCounts(pageInfo.getTotal());
		result.setTotal(pageInfo.getPages());
		result.setRows(list);
		return result;
		
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public PageResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);
		
		List<MiniVideosVo> list = videoCustomMapper.queryMyFollowVideos(userId);
		
		PageInfo<MiniVideosVo> pageInfo=new PageInfo<>(list);
		
		PageResult result=new PageResult();
		
		result.setPage(page);
		result.setCounts(pageInfo.getTotal());
		result.setTotal(pageInfo.getPages());
		result.setRows(list);
		return result;
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveComments(MiniComments comments,String fatherCommentId,String toUserId) {
		
		comments.setId(UUID.randomUUID().toString());
		comments.setCreateTime(new Date());
		
		if(StringUtils.isNotBlank(fatherCommentId)&& StringUtils.isNotBlank(toUserId)) {
			comments.setFatherCommentId(fatherCommentId);
			comments.setToUserId(toUserId);
		}
		commentsMapper.insert(comments);
		
	}
	@Override
	public PageResult queryVideoComments(String videoId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		
		List<MiniCommentsVo> list=commentsMapperCustom.queryComments(videoId);
		for(MiniCommentsVo c:list) {
			String timeFormat=TimeFormat.format(c.getCreateTime());
			c.setTimeFormat(timeFormat);
		}
		PageInfo<MiniCommentsVo> pageInfo=new PageInfo<>(list);
		
		PageResult result=new PageResult();
		
		result.setPage(page);
		result.setCounts(pageInfo.getTotal());
		result.setTotal(pageInfo.getPages());
		result.setRows(list);
		return result;
	}
	@Override
	public int delVideo(String videoId) {
		int i=videoMapper.deleteByPrimaryKey(videoId);
		
		return i;
	}
	
	


	
	
	

}
