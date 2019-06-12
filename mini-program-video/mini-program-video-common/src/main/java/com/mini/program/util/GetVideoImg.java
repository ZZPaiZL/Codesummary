package com.mini.program.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 *  ffmpeg工具类，用于音频和视频转换
 * @author Administrator
 *
 */
public class GetVideoImg {
	
	private String ffmpeg;
	
	public GetVideoImg(String ffmpeg) {
		super();
		this.ffmpeg = ffmpeg;
	}
	
	public void convertor(String inputPath,String outputPath) throws IOException  {
		//ffmpeg -ss 00:00:01 -y -i 11.mp4 -vframes 1 new.jpg 得到视频第一秒第一帧的图片
		List<String> command=new ArrayList<String>();
		command.add(ffmpeg);
		
		command.add("-ss");
		command.add("00:00:01");
		
		command.add("-y");
		command.add("-i");
		
		command.add(inputPath);
		command.add("-vframes");
		
		command.add("1");
		command.add(outputPath);
		
		ProcessBuilder pb=new ProcessBuilder(command);
		
			Process process=pb.start();//执行命令行
			
			//处理错误流中内存中的占用getErrorStream
			InputStream errorStram=process.getErrorStream();
			InputStreamReader inReader=new InputStreamReader(errorStram);
			BufferedReader br=new BufferedReader(inReader);
			
			String line="";
			while((line=br.readLine())!=null) {
				
			}
			
			if(br != null) {
				br.close();
			}
			if(inReader != null) {
				inReader.close();
			}if(errorStram != null) {
				errorStram.close();
			}
			
			
		} 
	
	
	
	//E:\ffmpeg\ffmpeg_win64\bin>ffmpeg -i 111.mp4 -i sjdfldj.mp3 -c:v copy -c:a aac -
	//strict experimental -map 0:v:0 -map 1:a:0 out.mp4
	
	public static void main(String[] args) {
		
		GetVideoImg ffmpegTest=new GetVideoImg("E:\\ffmpeg\\ffmpeg_win64\\bin\\ffmpeg.exe");
			try {
				ffmpegTest.convertor("E:\\ffmpeg\\ffmpeg_win64\\bin\\111.mp4", 
					
					"E:\\ffmpeg\\ffmpeg_win64\\bin\\111.jpg");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
	}

}
