package com.miniprogram.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mini.program.util.MD5Utils;
import com.miniprogram.mapper.MiniBgmMapper;
import com.miniprogram.pojo.MiniBgm;
import com.miniprogram.service.BgmService;
import com.miniprogram.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class BgmServiceImpl implements BgmService {
		
	@Autowired
	private MiniBgmMapper bgmMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public List<MiniBgm> queryBgmList() {
		
		return bgmMapper.selectAll();
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public MiniBgm queryBgmById(String bgmId) {
		// TODO Auto-generated method stub
		return bgmMapper.selectByPrimaryKey(bgmId);
	}
	
	


	
	//@Transactional(propagation=Propagation.REQUIRED)
	

}
