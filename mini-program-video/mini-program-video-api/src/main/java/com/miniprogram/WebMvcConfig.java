package com.miniprogram;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.miniprogram.controller.interceptor.MiniInterceptor;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	/**
	 * 静态资源配置
	 * WebMvcConfigurerAdapter配置资源呢类，是Spring内部的一种配置方式
	 *采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//swagger2是静态资源，所以也得配置，目录在META-INF/resources
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/META-INF/resources/")
		.addResourceLocations("file:F:/EclipseWorkSpace/mini-program-videos/");
	}
	
	//注册拦截器为一个bean对象
	@Bean
	public MiniInterceptor miniInterceptor() {
		
		return new MiniInterceptor();
		
	}
	
	//加载zookeeper类
		@Bean(initMethod="init")
		public ZKCuratorClient zkCuratorClient() {
			
			return new ZKCuratorClient();
			
		}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
		.addPathPatterns("/video/upload","/video/uploadCover","/video/userUnLike","/video/userLike","/video/saveLeaveMsg")
		.addPathPatterns("/bgm/bgmlist")
		.excludePathPatterns("/user/queryCreateVideo")
		
		;
			
		super.addInterceptors(registry);
	}
	
		
	
	
}
