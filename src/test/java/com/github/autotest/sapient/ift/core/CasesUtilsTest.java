package com.github.autotest.sapient.ift.core;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.github.autotest.sapient.ift.IftConf;
import com.github.autotest.sapient.ift.testcase.IftTestCase;

public class CasesUtilsTest {
	
	IftTestCase testCase ;
	LinkedHashMap<String,String> caseMap;
	CasesUtils caseUtils;
	
	@BeforeTest
	public void beforeTest(){
		caseUtils = new CasesUtils();
		testCase = new IftTestCase();
		testCase.setCaseId("test001");
		caseMap = new LinkedHashMap();
		IftConf.DependPara.put("id", "0001"); //自定义依赖参数
		IftConf.DependPara.put("id_01", "002");

	}
	
	@Test(description="请求参数中有自定义依赖参数，进行替换")
	public void updateAllParaForCase01(){
		caseMap.put("Ppara1", "${id}");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Ppara1");
		org.testng.Assert.assertTrue(actResult.equals("0001"), "预期结果为：0001 实际结果为："+actResult);
	}
	
	@Test(description="请求参数中有自定义依赖参数，进行部分替换")
	public void updateAllParaForCase02(){
		caseMap.put("Ppara2", "value${id}value");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Ppara2");
		org.testng.Assert.assertTrue(actResult.equals("value0001value"), "预期结果为：value0001value 实际结果为："+actResult);	
	}
	
	@Test(description="请求参数中有自定义依赖参数，Expres包含${}时，不在替换范围内")
	public void updateAllParaForCase03(){
		caseMap.put("Expres", "value${id}value");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Expres");
		org.testng.Assert.assertTrue(actResult.equals("value${id}value"), "预期结果为：value${id}value 实际结果为："+actResult);	
	}
	
	@Test(description="针对参数值为关键字rand的处理")
	public void updateAllParaForCase04(){
		caseMap.put("Ppara3", "rand");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Ppara3");
		org.testng.Assert.assertTrue(!actResult.equals("rand"), "预期结果为：value${id}value 实际结果为："+actResult);	
	}
	
	@Test(description="针对参数值为关键字rand的处理(参数为Expres)")
	public void updateAllParaForCase05(){
		String testNgExp = "";
		caseMap.put("Expres", "rand");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Expres");
		org.testng.Assert.assertTrue(actResult.length()==10, "预期结果为：10位的随机数;实际结果为："+actResult.length());	
	}
	
	@Test(description="针对参数值为关键字timestamp的处理")
	public void updateAllParaForCase06(){
		caseMap.put("Ppara6", "timestamp");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Ppara6");
		org.testng.Assert.assertTrue(actResult.length()==10, "预期结果为：10位的Unix时间戳; 实际结果为："+actResult.length());	
	}
	
	@Test(description="针对参数值为关键字date的处理")
	public void updateAllParaForCase07(){
		caseMap.put("Ppara7", "date");
		testCase.setCaseMap(caseMap);
		IftTestCase resultCase = caseUtils.updateAllParaForCase(testCase);
		String actResult = resultCase.getCaseMap().get("Ppara7");
		org.testng.Assert.assertTrue(actResult.length()==14, "预期结果为：13位的20120626092109时间戳; 实际结果为："+actResult.length());	
	}
	
	@Test(description="更新用例中的url全局变量")
	public void updateUrlHost(){
		testCase.setUrl("host");
		HashMap paraValue = new HashMap();
		paraValue.put("Host", "http://127.0.0.1");
		IftTestCase resultCase = caseUtils.updateAllToListForCase(testCase,CasesUtilsConf.GetPara,CasesUtilsConf.PostPara,CasesUtilsConf.HeardPara,new CasesUtilsConf());
		String actResult = resultCase.getUrl();
		org.testng.Assert.assertTrue(actResult.equals("http://127.0.0.1"), "预期结果为：http://127.0.0.1; 实际结果为："+actResult.length());	
	}
	

}
