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
public class FFmpegTest {
	
	private String ffmpeg;
	
	public FFmpegTest(String ffmpeg) {
		super();
		this.ffmpeg = ffmpeg;
	}
	
	public void convertor(String inputPath,String outputPath) {
		//ffmpeg -i input.mp4 output.avi 转换命令行
		List<String> command=new ArrayList<String>();
		command.add(ffmpeg);
		command.add("-i");
		command.add(inputPath);
		command.add(outputPath);
		
		ProcessBuilder pb=new ProcessBuilder(command);
		try {
			Process process=pb.start();//执行命令行
			
			//处理错误流中内存中的占用
			InputStream errorStram=process.getInputStream();
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
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FFmpegTest ffmpegTest=new FFmpegTest("E:\\ffmpeg\\ffmpeg_win64\\bin\\ffmpeg.exe");
		ffmpegTest.convertor("E:\\ffmpeg\\ffmpeg_win64\\bin\\111.mp4", 
				"E:\\ffmpeg\\ffmpeg_win64\\bin\\333.mp4");
		
	}

}
