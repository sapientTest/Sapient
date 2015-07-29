package com.github.autotest.sapient.demo;

import com.github.autotest.sapient.demo.project.DemoCasesUtils;
import com.github.autotest.sapient.dispatch.report.TestReport;
import com.github.autotest.sapient.ift.IftConf;
import com.github.autotest.sapient.ift.IftExec;
import com.github.autotest.sapient.toolkit.mysql.MysqlUtil;


/**
 * @author lianghui (lianghui@360.cn)
 *
 */
public class Entry {


	public static void main(String[] args) {
		//依赖的jar文件路径信息  必须设置 以maven方式运行一次后会记录本地库jar文件位置
		if (!IftConf.updateJarFile(args)) return;
		
		//数据库初始化，如果需要则打开注解
//		MysqlUtil msq= new MysqlUtil() ;
//		msq.connMysql(ip, port, dataName, userName, passWord);
//		msq.excSql("select * from table");
//		msq.closedConn();
		
		//添加用例
		IftExec iftExec = new IftExec();
		/**
		 * 	casePath 用例路径 必填
			sheetName Excel的sheet表名 可选
			caseName 用例名称 必填
			cls 执行用例的类 必填
			method 类中的方法 必填
		 */
		String excelFilePath = IftConf.RootPath+"demo.xlsx";
		
		iftExec.addCase(excelFilePath,"Sheet1","用例名称1",DemoCasesUtils.class,"DemoMethod1");
		iftExec.addCase(excelFilePath,"Sheet2","用例名称2",DemoCasesUtils.class,"DemoMethod1");
		
		//执行
		TestReport report=iftExec.run();
		
		//输出执行结果
		System.out.print("任务执行结果："+report.getResMsg()+"\n");
		if (report.getResNo()>-1) {
			System.out.print("任务名称："+report.getTaskName()+"\n");
			System.out.print("任务执行时间："+report.getSumTime()+"毫秒\n");
			System.out.print("Html报告："+report.getHtmlReport()+"/index.html\n");
			System.out.print("Excel报告："+iftExec.getExcelReportPath());
		}
		
	}

}
