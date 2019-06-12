package com.vlog.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vlog.mapper.VlogCategoryMapper;
import com.vlog.mapper.VlogDiaryMapper;
import com.vlog.mapper.VlogProductionMapper;
import com.vlog.mapper.VlogUserMapper;
import com.vlog.pojo.VlogCategory;
import com.vlog.pojo.VlogDiary;
import com.vlog.pojo.VlogProduction;
import com.vlog.pojo.VlogUser;
import com.vlog.service.IndexService;
import com.vlog.util.VlogJSONResult;
import com.vlog.util.PageResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class IndexServiceImpl implements IndexService {
	
	@Autowired
	private VlogUserMapper userMapper;
	@Autowired
	private VlogProductionMapper productionMapper;
	
	@Autowired
	private VlogCategoryMapper categoryMapper;
	
	@Autowired
	private VlogDiaryMapper diaryMapper;
	

	
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Map showInfo(Integer page, Integer pageSize) {
		VlogUser user=userMapper.selectAll().get(0);
		String introduce=user.getIntroduce();//得到个人简介
	
		//开启分页，一页三条
		PageHelper.startPage(page, pageSize);
		
		//获取最新旅拍作品
		Example examp=new Example(VlogCategory.class);
		//Criteria criteria=examp.createCriteria();
		examp.setOrderByClause("time desc");
		List<VlogCategory> productionlist=categoryMapper.selectByExample(examp);
//		PageInfo<VlogCategory> pageInfo1=new PageInfo<>(productionlist);
		
		VlogCategory	vlogCategory1=productionlist.get(0);
		VlogCategory	vlogCategory2=productionlist.get(1);
		VlogCategory	vlogCategory3=productionlist.get(2);
		
		
		
		//PageHelper.startPage(page, pageSize);
		//获取最新博客日记
		Example exampdiary=new Example(VlogDiary.class);
		//Criteria criteria=examp.createCriteria();
		exampdiary.setOrderByClause("time desc");
		List<VlogDiary> diary=diaryMapper.selectByExample(exampdiary);
		//PageInfo<VlogDiary> pageInfo2=new PageInfo<>(diary);
		
		
		
		
//		PageResult diaryResult=new PageResult();
//		diaryResult.setPage(page);
//		diaryResult.setCounts(pageInfo2.getTotal());
//		diaryResult.setTotal(pageInfo2.getPages());
//		diaryResult.setRows(diary);
		
		Map map=new HashMap();
		
		map.put("introduce", introduce);
		map.put("diaryResult", diary);
		map.put("vlogCategory1", vlogCategory1);
		map.put("vlogCategory2", vlogCategory2);
		map.put("vlogCategory3", vlogCategory3);
//		for(VlogProduction i:productionlist) {
//			System.out.println(i.getTime());
//		}
		
		
		
		
		return map;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Map showDiary(String id,String classify) {
		Map map=new HashMap();
		Example exampdiary=new Example(VlogDiary.class);
		//如果传入了id,就使用id查询
		if(id != null && id != "") {
			VlogDiary diary=diaryMapper.selectByPrimaryKey(id);
			map.put("diaryResult", diary);
			return map;
		}
		
		if(classify != null && classify != "") {
			Criteria criteria=exampdiary.createCriteria();
			criteria.andEqualTo("classify",classify);
			List<VlogDiary> diary=diaryMapper.selectByExample(exampdiary);
			map.put("diaryResult", diary);
			return map;
		}
		
		//获取最新博客日记
		//Criteria criteria=examp.createCriteria();
				exampdiary.setOrderByClause("time desc");
				List<VlogDiary> diary=diaryMapper.selectByExample(exampdiary);
				map.put("diaryResult", diary);
				return map;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Map showClassify(String category) {
		Map map=new HashMap();
		Example examp=new Example(VlogCategory.class);
		examp.setOrderByClause("time desc");
		Criteria criteria=examp.createCriteria();
		if(category != null && category != "") {
			criteria.andEqualTo("category",category);
		}
		List<VlogCategory> categorys=categoryMapper.selectByExample(examp);
		map.put("Result", categorys);
		return map;
	}

	@Override
	public Map showDetail(String id) {
		Map map=new HashMap();
		Example examp=new Example(VlogProduction.class);
		examp.setOrderByClause("time desc");
		Criteria criteria=examp.createCriteria();
		if(id != null && id != "") {
			criteria.andEqualTo("categoryId",id);
		}
		List<VlogProduction> list=productionMapper.selectByExample(examp);
		map.put("info", list);
		return map;
	}

}
