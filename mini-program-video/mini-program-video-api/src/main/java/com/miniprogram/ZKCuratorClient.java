package com.miniprogram;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.support.json.JSONUtils;
import com.mini.program.enums.BgmOperStatus;
import com.mini.program.util.JsonUtils;
import com.miniprogram.config.ResourceConfig;
import com.miniprogram.pojo.MiniBgm;
import com.miniprogram.service.BgmService;


import groovy.util.logging.Commons;

@Commons
public class ZKCuratorClient {

	//zk客户端
	private CuratorFramework client=null;
	final static Logger log=LoggerFactory.getLogger(ZKCuratorClient.class);

	//public final static String ZOOKEEPER_SERVER="192.168.50.243:2181";
	
	@Autowired
	private BgmService bgmService;
	@Autowired
	private ResourceConfig resourceConfig;
	
	public void init() {
		if(client != null) {
			return;
		}
		//重连策略
		RetryPolicy reconnet= new ExponentialBackoffRetry(1000,5);
		//创建客户端
		client=CuratorFrameworkFactory.builder().connectString(resourceConfig.getZookeeperServer())
				.sessionTimeoutMs(10000).retryPolicy(reconnet).namespace("admin")
				.build();
		//启动客户端
		client.start();
		try {
//			String str=new String (client.getData().forPath("/bgm/1004"));
//			log.info("启动成功 {}"+str);
			addChildPath("/bgm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void addChildPath(String nodePath) throws Exception {
		final PathChildrenCache cache= new PathChildrenCache(client, nodePath, true);
		cache.start();
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
					log.info("监听到事件");
					
					//1.从数据库中查询bgm，获取路径path
					
					String path=event.getData().getPath();
					String operator=new String(event.getData().getData());
					
					Map<String,String> map=JsonUtils.jsonToPojo(operator, Map.class);
					String operType=map.get("operType");
					String bgmPath=map.get("path");
					
					
					
//					String arr[]=path.split("/");
//					
//					String bgmId=arr[arr.length-1];
//					MiniBgm bgm = bgmService.queryBgmById(bgmId);
//					if(bgm == null) {
//						return;
//					}
					
					//2.定义保存到本地的bgm路径
					//String bgmPath=bgm.getPath();
					
					//springboot中bgm保存路径
					String filePath=resourceConfig.getFileSpace()+bgmPath;
					
					//3.定义下载路径，
					
					String arrPath[]=bgmPath.split("\\\\");
					
					String finalPath="";
					//处理url斜杠以及编码
					for (int i = 0; i < arrPath.length; i++) {
						if(StringUtils.isNotBlank(arrPath[i])) {
								finalPath+="/";
								finalPath+= URLEncoder.encode(arrPath[i], "utf-8");
						}
						
					}
					//后台管理的服务器地址
					String bgmUrl=resourceConfig.getBgmServer()+finalPath;
					
					if(operType.equals(BgmOperStatus.ADD.type)) {
						//下载bgm到springboot服务器
						URL url=new URL(bgmUrl);
						File file=new File(filePath);
						FileUtils.copyURLToFile(url, file);
						client.delete().forPath(path);
					}else if(operType.equals(BgmOperStatus.DELETE.type)) {
						//从本地删除文件
						File file=new File(filePath);
						FileUtils.forceDelete(file);
						client.delete().forPath(path);
					}
					
					
					
					
				}
				
			}
		});
		
		
	}
}
