package com.github.autotest.sapient.demo.project;

import com.github.autotest.sapient.ift.core.CasesUtils;
import com.github.autotest.sapient.ift.core.IFtResultInfo;
import com.github.autotest.sapient.ift.testcase.IftTestCase;
import com.github.autotest.sapient.ift.util.CommonSign;
import com.github.autotest.sapient.toolkit.httpclient.ResponseInfo;
import com.github.autotest.sapient.toolkit.util.CommUtils;

/**
 * 说明：接口执行类，必须继承自类CasesUtils
 * 
 * @author 
 *
 */
public class DemoCasesUtils {

	/**
	 * 接口业务逻辑处理方法，此方式的传入参数是 IftTestCase  返回结果类型是IFtResultInfo 方法中如何处理业务逻辑 
	 * 调用httpclient发起请求，处理返回结果等可自定义
	 * @param testcase
	 * @return IFtResultInfo
	 */
	public IFtResultInfo DemoMethod1(IftTestCase testcase)  {
		CasesUtils casesUtils = new CasesUtils();
		//IftConf.JsonNum = 0;
		//暂停100毫秒
		//CommUtils.sleep(100);
		//更新用例的签名计算、url参数、form参数、header参数--必须
		testcase=casesUtils.updateAllToListForCase(testcase, DemoConf.GetPara, DemoConf.PostPara, DemoConf.HeardPara,new DemoConf());
		//更新用例参数值，针对rand、timetamp、date等特殊标识处理--必须
		testcase=casesUtils.updateAllParaForCase(testcase);
		//更新用例签名值--可选  如果无需计算签名  则不需要
		testcase=casesUtils.updateSignValueForCase(testcase, CommonSign.signMethodThird(casesUtils.getSignMap(testcase), DemoConf.SecretKey));
		//发起请求
		ResponseInfo resInfo = casesUtils.execResquest(testcase);
		//关闭httpclient连接
		casesUtils.closeConn();
		//预期值与实际值比对 并返回IFtResultInfo类型
		return casesUtils.getIFtResultInfo(resInfo, testcase);
	}
	
	public IFtResultInfo DemoMethod2(IftTestCase testcase)  {
		IFtResultInfo iftResInfo=new IFtResultInfo();
		ResponseInfo resInfo = new ResponseInfo(); 
		//执行用例  可以使用CasesUtils中已封装的ExecPostResquest、ExecGetResquest方法 或者直接调用httpclient
		//。。。。
		//返回的类型为IFtResultInfo即可
		
		resInfo.setHttpUrl("this a demo2");
		resInfo.setResBodyInfo("demo");
		
		iftResInfo.setResponseInfo(resInfo);//接口返回信息resInfo
		iftResInfo.setActRes("demo");//实际结果
		iftResInfo.setExpRes("demo");//期望结果
		iftResInfo.setCompareRes(true);//比对结果
		return iftResInfo;
	}

}

 
	
	

