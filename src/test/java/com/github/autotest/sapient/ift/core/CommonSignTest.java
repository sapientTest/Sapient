package com.github.autotest.sapient.ift.core;

import java.util.TreeMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.autotest.sapient.ift.util.CommonSign;

public class CommonSignTest {
	private CommonSign comSign ;
	private TreeMap signpara;
	private String secret_key;
	
	@BeforeTest
	public void beforeTest(){
		comSign = new CommonSign();
		signpara = new TreeMap();
		signpara.put("key01", "value01");
		signpara.put("key02", "value02");
		signpara.put("key03", "");
		signpara.put("键", "值");		
		secret_key = "12345";
		
	}
	
	@Test
	public void signMethodOne(){
		String sign = comSign.signMethodOne(signpara, secret_key);
		org.testng.Assert.assertTrue(sign.equals("83598bf84e5a313349884d056d3e9e8e"), "预期结果为：83598bf84e5a313349884d056d3e9e8e 实际结果为："+sign);
	}
	
	@Test
	public void signMethodTwo(){
		String sign = comSign.signMethodTwo(signpara, secret_key);
		org.testng.Assert.assertTrue(sign.equals("cb13696956bd665800e89ba40b195069"), "预期结果为：cb13696956bd665800e89ba40b195069 实际结果为："+sign);
		
	}
	
	@Test
	public void signMethodThird(){
		String sign = comSign.signMethodThird(signpara, secret_key);
		org.testng.Assert.assertTrue(sign.equals("36da22f664a7f5e37aa19ff3fbcaebb1"), "预期结果为：36da22f664a7f5e37aa19ff3fbcaebb1 实际结果为："+sign);
		
	}
	
	@Test
	public void signMethodFour(){
		String sign = comSign.signMethodFour(signpara, secret_key);
		org.testng.Assert.assertTrue(sign.equals("cbbbb31a763924f3063f217c399384d7"), "预期结果为：cbbbb31a763924f3063f217c399384d7 实际结果为："+sign);
		
		
	}

}
