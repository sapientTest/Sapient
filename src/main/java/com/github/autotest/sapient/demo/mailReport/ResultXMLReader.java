package com.github.autotest.sapient.demo.mailReport;

//import ift.server.utilities.log.Mylogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;




/**解析testng-result.xml中有用的结果信息
 * @author chenqing
 * 
 */
public class ResultXMLReader extends DefaultHandler2 {
	private List<TestMethod> methods = null;
	private TestMethod method = null;
	private String preTag = null;
	private Map<String, Object> resultMap = new HashMap<String, Object>();
	private String messages;
	private StringBuffer cData; 
	private int flag;
	
	public Map<String, Object> getTestResults(String xmlFile)throws Exception{	
//		XMLReader reader = null;
//      reader=parser.getXMLReader();       
//      reader.setContentHandler(handler);
		InputSource is = null;
		InputStreamReader streamReader = null;
		ResultXMLReader handler = new ResultXMLReader();
		SAXParserFactory factory = SAXParserFactory.newInstance();  
        SAXParser parser = factory.newSAXParser(); 
		try {
			System.out.println(System.getProperty("user.dir") + xmlFile);
			InputStream xmlStream = new FileInputStream(new File(System.getProperty("user.dir") + xmlFile));
			streamReader = new InputStreamReader(xmlStream, "UTF-8");
			is = new InputSource(streamReader);
	        parser.parse(is, handler); 
		} catch(Exception e){
			InputStream xmlStream = new FileInputStream(new File(System.getProperty("user.dir") + xmlFile));
			streamReader = new InputStreamReader(xmlStream, "GBK");
			is = new InputSource(streamReader);
	        parser.parse(is, handler); 
		}
		
        return handler.getTestResults(); 
	}
	
	public Map<String, Object> getTestResults(){
		resultMap.put("test-methods", methods);
		return resultMap;
	}

	@Override
	public void startDocument() throws SAXException{
		methods = new ArrayList<TestMethod>();
	}
	
	@Override
	public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException{
	
		if("testng-results".equals(qName)){
			resultMap.put("skipped",attributes.getValue("skipped").toString());
			resultMap.put("failed", attributes.getValue("failed").toString());
			resultMap.put("total", attributes.getValue("total").toString());
			resultMap.put("passed", attributes.getValue("passed").toString());
		}else if("test-method".equals(qName)){
			method = new TestMethod();
			method.setStatus(attributes.getValue("status"));
			method.setSignature(attributes.getValue("signature"));
			method.setName(attributes.getValue("name"));
			messages="";
		}else if("suite".equals(qName)){
			resultMap.put("name", attributes.getValue("name").toString());
			resultMap.put("duration-ms", attributes.getValue("duration-ms").toString());
			resultMap.put("started-at", attributes.getValue("started-at").toString());
			
			resultMap.put("finished-at", attributes.getValue("finished-at").toString());
		}else if("line".equals(qName)){
			startCDATA();
			flag++;
		}else if("message".equals(qName)){
			startCDATA();
			flag++;
		}
		preTag = qName;
	}
	
	@Override
	public void endElement(String uri,String localName,String qName) throws SAXException {
		if("test-method".equals(qName)){
			method.setReason(messages);
			methods.add(method);
			method = null;
//			messages = "";
		}
		
		preTag = null;
		
		if("line".equals(qName)){
			endCDATA();
		}
		
		if("message".equals(qName)){
			endCDATA();
		}
	}
	
	@Override
	public void startCDATA()throws SAXException {
		if(cData==null){
			cData=new StringBuffer();
		}else{
			cData.delete(0, cData.length());
		}
		super.startCDATA();
	}
	@Override
	public void endCDATA()throws SAXException {
		messages=messages+cData.toString().trim();
		super.endCDATA();
	}
	
	@Override
	public void characters(char[] ch, int start,int length) throws SAXException {
		
		if (preTag!=null){
			if("message".equals(preTag)){
				String data=new String(ch,start,length);
				cData.append(data);
			}
			if("line".equals(preTag)){
				String data=new String(ch,start,length);
				cData.append(data);
			
		}
		}
		super.characters(ch, start, length);
	}
	
//	public static void main(String arge[]) throws Exception{
//		ResultXMLReader reader = new ResultXMLReader();
//		
//		Map<String, Object> results = reader.getTestResults("\\test-output\\testng-results.xml");
//		
//		for (Map.Entry<String, Object> m :results.entrySet()){
//			if(m.getKey()=="test-methods"){
//				continue;
//			}
//		
//			System.out.println(m.getKey()+"\t"+m.getValue());
//		}
//		
//		List<TestMethod> methods = (List<TestMethod>) results.get("test-methods");
//		
//		for(TestMethod method : methods){
//			System.out.println(method.getName()+" "+method.getStatus()+"\n"+method.getReason());
//		
//		}
//		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); 
//		String time = "2013-08-26T06:22:07Z +0800";
////		Date date = format1.parse(time); 
////		System.out.print(date);
//		
//		System.out.println(time.substring(0,time.length()-2)+":"+time.substring(time.length()-2));
//	}
	
}