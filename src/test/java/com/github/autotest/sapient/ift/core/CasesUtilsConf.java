package com.github.autotest.sapient.ift.core;

public class CasesUtilsConf {
	public static String SecretKey="参与签名的密钥";
	public static String Host = "http://127.0.0.1"; //配置全局Host,使用时excel中URL值应为“host”关键字
	
	public static String heard1 = "value1";  //可以在此处添加多个全局heard,key必须包含于HeardPara数组中
	
	public static final String[]  HeardPara ={""};
	public static final String[] GetPara = {"Gpara01","Gpara02","Gpara03"};
	public static final String[] PostPara = {"Ppara01","Ppara02","Ppara03"};

}
