package com.github.autotest.sapient.demo.mailReport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import com.github.autotest.sapient.toolkit.util.CommUtils;

public class HtmlFile {

	/**
	 * @param args
	 */
	public String MailHtml() {
		// TODO Auto-generated method stub
		try {
			String filePath = System.getProperty("user.dir")
					+ "/sapient/ift/template/MailTemplate.html";
			System.out.println(filePath);
			//报告连接 
			String jobName = MailReportConf.jobName; //注意新工程时要修改
			String report_url = "http://"+MailReportConf.jenkinshost+"/jenkins/job/"+jobName+"/"+
			                    CurrentUtils.getLastNumber(jobName)+"/HTML_Report/";
			String templateContent = "";
			FileInputStream fileinputstream = new FileInputStream(filePath);
			int length = fileinputstream.available();
			byte bytes[] = new byte[length];
			fileinputstream.read(bytes);
			fileinputstream.close();
			templateContent = new String(bytes);
			// 邮件模板
			StringBuffer theMessage = new StringBuffer();
			ResultXMLReader reader = new ResultXMLReader();
			String messages = new String();
			// Map<String, Object> results =
			// reader.getTestResults("\\test-output\\testng-results.xml");
			Map<String, Object> results = reader
					.getTestResults("/sapient/dispatch/testng-out/testng-results.xml");
			// case总体情况
			templateContent = templateContent.replaceAll("#title#", MailReportConf.reportTitle);
			templateContent = templateContent.replaceAll("#case_name#", "用例名称");
			templateContent = templateContent
					.replaceAll("#case_total#", "用例总数");
			templateContent = templateContent.replaceAll("#passed_name#", "成功");
			templateContent = templateContent.replaceAll("#failed_name#", "失败");
			templateContent = templateContent.replaceAll("#skipped_name#",
					"未执行");
			templateContent = templateContent.replaceAll("#started_name#",
					"开始时间");
			templateContent = templateContent.replaceAll("#finished_name#",
					"结束时间");
			templateContent = templateContent.replaceAll("#duration_name#",
					"耗时");
			templateContent = templateContent.replaceAll("#case_desc#",
					"请点击查看详情");
			templateContent = templateContent.replaceAll("#report_url#",report_url); 
//			templateContent = templateContent.replaceAll("#case_num#", "编号");
//			templateContent = templateContent.replaceAll("#case_name2#", "标题");
//			templateContent = templateContent.replaceAll("#case_result#", "结果");
//			templateContent = templateContent.replaceAll("#result_desc#", "详情");
			templateContent = templateContent.replaceAll("#casename#", results
					.get("name").toString());
			templateContent = templateContent.replaceAll("#total#", results
					.get("total").toString());
			templateContent = templateContent.replaceAll("#passed#", results
					.get("passed").toString());
			templateContent = templateContent.replaceAll("#failed#", results
					.get("failed").toString());
			templateContent = templateContent.replaceAll("#skipped#", results
					.get("skipped").toString());
			templateContent = templateContent.replaceAll("#started_at#",
					CurrentUtils.ZooConvert(results.get("started-at").toString(),"+0"));//结束时间，并进行时区转换
			templateContent = templateContent.replaceAll("#finished_at#",
					CurrentUtils.ZooConvert(results.get("finished-at").toString(),"+0"));//结束时间，并进行时区转换
			templateContent = templateContent.replaceAll("#duration_ms#",
					results.get("duration-ms").toString());
			// case详情
//			List<TestMethod> methods = (List<TestMethod>) results
//					.get("test-methods");
//			int i = 0;
//			for (TestMethod method : methods) {
//				if (method.getName().equals("afterTest")) {
//				} else if (method.getName().equals("beforeTest")) {
//
//				} else {
//					i++;
//					if (method.getStatus().equals("FAIL")) {
//						theMessage
//								.append("<tr style=\"background:#FF6A6A\"><td><font size=\"2px\">"
//										+ i + "</font></th>");
//						theMessage.append("<td><font size=\"2px\">"
//								+ method.getName() + "</font></td>");
//						theMessage.append("<td><font size=\"2px\">"
//								+ method.getStatus() + "</font></td>");
//						theMessage
//								.append("<td "
//										+ "style='text-align:left;'><font size=\"2px\">"
//										+ method.getReason() + "</font>"
//										+ "</td>");
//
//					}
//					if (method.getStatus().equals("PASS")) {
//						theMessage.append("<tr><td><font size=\"2px\">" + i
//								+ "</font></th>");
//						theMessage.append("<td><font size=\"2px\">"
//								+ method.getName() + "</font></td>");
//						theMessage.append("<td><font size=\"2px\">"
//								+ method.getStatus() + "</font></td>");
//						theMessage
//								.append("<td style='text-align:left;'><font size=\"2px\">"
//										+ "</font></td>");
//					}
//					if (method.getStatus().equals("SKIP")) {
//						theMessage
//								.append("<tr style=\"background:#FFD700\"><td><font size=\"2px\">"
//										+ i + "</font></th>");
//						theMessage.append("<td><font size=\"2px\">"
//								+ method.getName() + "</font></td>");
//						theMessage.append("<td><font size=\"2px\">"
//								+ method.getStatus() + "</font></td>");
//						theMessage
//								.append("<td style='text-align:left;'><font size=\"2px\">"
//										+ "</font></td>");
//					}
//				}
//				theMessage.append("</tr>");
//			}
			templateContent = templateContent.replaceAll("#message#",
					theMessage.toString());
			String filename = CommUtils.getTimestamp() + ".html";
			// String filename = GetTimeNow.getTimeNow()+".html";
			filename = System.getProperty("user.dir") + "\\testNgXslt\\out\\"
					+ filename;// 生成html文件保存路径
			String filename2 = "mailReport/emailable-report.html";
			FileOutputStream fileoutputstream = new FileOutputStream(filename2);// 建立文件输出流
			OutputStreamWriter osw  =   new  OutputStreamWriter(fileoutputstream,"UTF-8");//指定生成的html文件格式,如果是jenks执行需要改成GBK
			byte tag_bytes[] = templateContent.getBytes();
			osw.write(templateContent);
			osw.close();

			System.out.println("邮件报告保存在：" + filename2);
			return filename2;
		} catch (Exception e) {
			System.out.print(e.toString());
			return null;
		}

	}
}
