package com.github.autotest.sapient.demo.mailReport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

import com.github.autotest.sapient.ift.IftConf;
import com.github.autotest.sapient.ift.core.CasesUtils;
import com.github.autotest.sapient.ift.testcase.IftTestCase;

public class CurrentUtils {
	/**
	 * 时区转换
	 * @param time 需要转换的时间 eg:2014-05-12T12:40:26Z
	 * @param  zoo 时区时间差，eg:+0
	 * @return
	 */
	public static String ZooConvert(String time,String zoo){	
		String src = time.replace("Z", "");//去掉Z字符串
		String dest = src.replace("T"," ");//将T替换为空格 
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"+"zoo"));
		Date date = null;
		try {
			date = inputFormat.parse(dest);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		        
//		SimpleDateFormat outputFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.CHINA);
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println("Asia/Beijing:" + outputFormat.format(date));		
		return outputFormat.format(date);		
	}
	
	/**
	 * 文件拷贝
	 * 
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void copyFile(String srcFile, String destPath)
			throws Exception {
//		InputStream in = new FileInputStream(fileName1);
		BufferedInputStream in = new BufferedInputStream(
				new FileInputStream(srcFile));
//		OutputStream out = new FileOutputStream(fileName2);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(destPath));
		byte[] buf = new byte[1024];
		File file1 = new File(srcFile);
		File file2 = new File(destPath);
		// 判断file1是否存在
		if (!file1.exists()) {
			file1.createNewFile();
		}
		// 判断file2是否存在
		if (!file2.exists()) {
			file2.createNewFile();
		}
		// 拷贝文件
		int i;
		while ((i=in.read(buf)) != -1) {
			out.write(buf, 0, i); 
		}
		in.close();
		out.close();
//		System.out.println("done!");
	}
	
	/**
	 * 考贝文件夹，及文件夹下的文件和文件夹
	 * @param srcPath
	 * @param destPath
	 */
	public static void copydirs(String srcPath,String destPath){
		File srcdir = new File(srcPath);
		File destdir = new File(destPath);
		if(!destdir.exists()){
			destdir.mkdir();
		}
		//获取源文件夹当前下的文件或目录
		File[] fileList = srcdir.listFiles();
		for(int i=0;i<fileList.length;i++){
			if(fileList[i].isFile()){
				//复制文件
				try {
					CurrentUtils.copyFile(fileList[i].toString(), 
							destPath+File.separator+fileList[i].getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(fileList[i].isDirectory()){
				//复制目录 
				String sorceDir = srcPath+File.separator+fileList[i].getName();
				String targetDir = destPath+File.separator+fileList[i].getName();
				//递归复制
				copydirs(sorceDir,targetDir);
			}
		}
		
	}
	
	/**
	 * 
	 * @param job jenkins中job名
	 * @return
	 */
	public static String getLastNumber(String job){
		String gethttpurl = "http://"+MailReportConf.jenkinshost+"/jenkins/job/"+job+"/lastBuild/buildNumber";//获取job的最后一个build名
		IftTestCase testCase = new IftTestCase();
		testCase.setUrl(gethttpurl);
		testCase.setHttpMethod("Get");
		CasesUtils cUtils = new CasesUtils();
		String buildNumber = cUtils.execResquest(testCase).getResBodyInfo();
		return buildNumber;
	}

}
