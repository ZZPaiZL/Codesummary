package com.miniprogram.controller.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.json.JSONUtils;
import com.mini.program.util.MiniJSONResult;
import com.miniprogram.util.RedisOperator;

public class MiniInterceptor implements HandlerInterceptor {

	
	/**
	 * 在调用controller之前的拦截
	 */
	
	@Autowired
    public RedisOperator redis;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 返回false,表示被拦截
		 * 返回true，表示不被拦截，跳过
		 */
		
		String userId=request.getHeader("userId");
		String userToken=request.getHeader("userToken");
		if(StringUtils.isNotBlank(userToken) && StringUtils.isNotBlank(userToken)) {
			String uniqueToken=redis.get("USER_REDIS_SESSION" + ":" + userId);
			if(uniqueToken.isEmpty() && StringUtils.isBlank(uniqueToken)) {
				System.out.println("请登录");
				returnErrorMsg(response,new MiniJSONResult().errorTokenMsg("请登录"));
				//return false;
				return true;
			}else {
				if(!uniqueToken.equals(userToken)) {
					System.out.println("异常登录");
					returnErrorMsg(response,new MiniJSONResult().errorTokenMsg("异常登录"));
					//return false;
					return true;
				}
			}
			
		}else {
			System.out.println("请登录");
			returnErrorMsg(response,new MiniJSONResult().errorTokenMsg("请登录"));
			//return false;
			return true;
		}
		
		
		
		
		return true;
	}

	/**
	 * 在调用controller之后，渲染视图之前的拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * 在调用controller之后，渲染视图之后的拦截
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
	
	
	public void returnErrorMsg(HttpServletResponse response,MiniJSONResult result) throws IOException {
		OutputStream out=null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json");
			out=response.getOutputStream();
			out.write(JSONUtils.toJSONString(result).getBytes("utf-8"));
			out.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(out !=null) {
				out.close();
			}
		}
		
	}
	

}
