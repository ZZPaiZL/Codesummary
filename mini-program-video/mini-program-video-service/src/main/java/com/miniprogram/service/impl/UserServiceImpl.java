package com.miniprogram.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mini.program.util.MD5Utils;
import com.mini.program.util.MiniJSONResult;
import com.miniprogram.mapper.MiniUsersFansMapper;
import com.miniprogram.mapper.MiniUsersLikeVideosMapper;
import com.miniprogram.mapper.MiniUsersMapper;
import com.miniprogram.mapper.MiniUsersReportMapper;
import com.miniprogram.pojo.MiniUsers;
import com.miniprogram.pojo.MiniUsersFans;
import com.miniprogram.pojo.MiniUsersLikeVideos;
import com.miniprogram.pojo.MiniUsersReport;
import com.miniprogram.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {
		
	@Autowired
	private MiniUsersMapper userMapper;
	
	@Autowired
	private MiniUsersFansMapper userFansMapper;
	
	@Autowired
	private MiniUsersReportMapper usersReportMapper;
	
	@Autowired
	private MiniUsersLikeVideosMapper usersLikeVideosMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public boolean isExistUserName(String userName) {
		// TODO Auto-generated method stub
		MiniUsers user=new MiniUsers();
		user.setUsername(userName);
		MiniUsers result=userMapper.selectOne(user);
		return result == null ? false:true;
	}

	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveUser(MiniUsers user) {
		UUID id=UUID.randomUUID();
		
		user.setId(id.toString());
		userMapper.insert(user);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public MiniUsers isExistUser(MiniUsers user) {
		// TODO Auto-generated method stub
		try {
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Example example=new Example(MiniUsers.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("username",user.getUsername());
		criteria.andEqualTo("password",user.getPassword());
		
		MiniUsers result=userMapper.selectOneByExample(example);
		
		return result ;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateUserInfo(MiniUsers user) {
		Example example=new Example(MiniUsers.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("id",user.getId());
		userMapper.updateByExampleSelective(user, example);
		
		
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public MiniUsers queryUserInfo(String userId) {
		
		Example example=new Example(MiniUsers.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("id",userId);
		MiniUsers user=userMapper.selectOneByExample(example);
		return user;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public boolean isLikeVideo(String userId, String videoId) {
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(videoId)) {
			return false;
		}
		
		
		Example example=new Example(MiniUsersLikeVideos.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("videoId",videoId);
		List<MiniUsersLikeVideos> list=usersLikeVideosMapper.selectByExample(example);
		if(list!=null && list.size()>0) {
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveUserFans(String userId, String fansId) {
		MiniUsersFans userFans=new MiniUsersFans();
		userFans.setId(UUID.randomUUID().toString());
		userFans.setUserId(userId);
		userFans.setFanId(fansId);
		userFansMapper.insert(userFans);
		
		
		userMapper.addFansCount(userId);
		userMapper.addfollowCount(fansId);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void delUserFans(String userId, String fansId) {
		
		Example example=new Example(MiniUsersFans.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("fanId",fansId);
		
		userFansMapper.deleteByExample(example);
		
		userMapper.reduceFansCount(userId);
		userMapper.reducefollowCount(fansId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public boolean isFollow(String userId, String fansId) {
		Example example=new Example(MiniUsersFans.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("fanId",fansId);
		
		List<MiniUsersFans> userFans =userFansMapper.selectByExample(example);
		if(userFans !=null && userFans .size()>0 && !userFans.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void reportUser(MiniUsersReport userReport) {
		
		userReport.setId(UUID.randomUUID().toString());
		userReport.setCreateDate(new Date());
		usersReportMapper.insert(userReport);
		
		
	}

}
