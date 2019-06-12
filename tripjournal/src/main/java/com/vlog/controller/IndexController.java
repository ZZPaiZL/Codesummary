package com.vlog.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vlog.pojo.VlogDiary;
import com.vlog.service.IndexService;
import com.vlog.util.VlogJSONResult;

@Controller
@RequestMapping("/vlog")
public class IndexController extends BaseController {
	
	@Autowired
	private IndexService indexService;
	
	@RequestMapping("/index")
	public String index(Integer page,Model m) {
		if(page == null) {
			page=1;
			
		}
		Map map=indexService.showInfo(page,PAGE_SIZE);
		m.addAttribute("map",map);
		return "thymeleaf/index";
	}
	
	//无用方法
//	@PostMapping("/showDiary")
//	@ResponseBody
//	public VlogJSONResult showDiary(HttpServletRequest req) {
//		String page=req.getParameter("page");
//		Map map=indexService.showDiary(Integer.parseInt(page), PAGE_SIZE);
//		return VlogJSONResult.ok(map);
//		
//		
//	}
	
	
	
	@RequestMapping("/production")
	public String produc() {
		
		return "thymeleaf/production";
	}
	
	@RequestMapping("/diary")
	public String diary(Model m,HttpServletRequest req) {
		
		String classify=req.getParameter("classify");
		String id="";
		Map map=indexService.showDiary(id,classify);
		m.addAttribute("map",map);
		return "thymeleaf/diary";
	}
	@RequestMapping("/diaryDetail")
	public String diaryDetail(Model m,HttpServletRequest req) {
		String classify=req.getParameter("classify");
		String id=req.getParameter("info");
		Map map=indexService.showDiary(id,classify);
		m.addAttribute("info",map);
		return "thymeleaf/diary_detail";
	}
	
	@RequestMapping("/productionClass")
	public String productionClass(Model m,HttpServletRequest req) {
		String category=req.getParameter("category");
		Map map=indexService.showClassify(category);
		m.addAttribute("info",map);
		return "thymeleaf/production_class";
	}
	
	@RequestMapping("/productionDetail")
	public String productionDetail(Model m,HttpServletRequest req) {
//		String category=req.getParameter("category");
//		Map map=indexService.showClassify(category);
//		m.addAttribute("info",map);
		return "thymeleaf/production_detail";
	}
	
	
	@RequestMapping("/showDetail")
	@ResponseBody
	public Map showDetail(Model m,HttpServletRequest req) {
		String id=req.getParameter("id");
		Map map=indexService.showDetail(id);
//		m.addAttribute("info",map);
		return map;
	}
	
	
}
