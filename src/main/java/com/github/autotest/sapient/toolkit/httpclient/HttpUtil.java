package com.github.autotest.sapient.toolkit.httpclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.github.autotest.sapient.toolkit.util.LogUtil;
import com.github.autotest.sapient.toolkit.util.StringUtil;

/**
 * 封装HttpClient 发送Get、Post等请求
 * @author lianghui (lianghui@360.cn) 2013-01-10
 */
public class HttpUtil {
	protected LogUtil log = LogUtil.getLogger(this.getClass());
	private String charset;
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpResponse response;
	/**
	 * 默认构造函数
	 */
	public HttpUtil() {
		this.charset = "UTF-8"; 
		PoolingClientConnectionManager pccm = new PoolingClientConnectionManager();
        pccm.setMaxTotal(100); //设置整个连接池最大链接数
		httpClient = new DefaultHttpClient(pccm);
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getConnectionManager().closeIdleConnections(30,TimeUnit.SECONDS);
	}

	/**
	 * 构造函数 设置编码
	 * @param charset
	 */
	public HttpUtil(String charset) {
		this();
		this.charset = charset;
	}
	
	/**
	 * 构造函数，设置代理
	 * 
	 * @param ip
	 * @param port
	 */
	public HttpUtil(String ip, int port) {
		this();
		HttpHost proxy = new HttpHost(ip, port);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
	}

	//--对外get处理方法
	/**
     * 根据header url发起get请求
     * @param headers
     * @param url
     * @return ResponseInfo
     */
	public ResponseInfo get(Map<String, String> headers, String url) {
		ResponseInfo resInfo = new ResponseInfo();
		if(StringUtil.IsNullOrEmpty(url)){
			log.error("url为空");
			resInfo.setResBodyInfo("url为空");
			return resInfo;
		}
		httpGet = new HttpGet(url);
		httpGet = setHttpGetHeader(httpGet, headers);
		return get(httpGet);
	}
	
	/**
	 * 根据url发起get请求
	 * @param url
	 * @return ResponseInfo
	 */
	public ResponseInfo get(String url) {
		return get(null, url);
	}

	
	
	//--对外post处理方法
	 /**
     * 设置发送请求的header,url,params信息，发送post请求，得到返回的字符串结果集
     * @param headers
     * @param url
     * @param params
     * @return ResponseInfo
     */
    public ResponseInfo post(TreeMap<String,String> headers, String url, TreeMap<String,Object> params) {  
        httpPost = new HttpPost(url);  
        httpPost = setHttpPostHeaderAndParams(httpPost,headers,params);  
        return post(httpPost);  
    } 
   
    /**
     * 设置发送请求的headers,url,str信息，发送post请求，得到返回的字符串结果集
     * @param headers
     * @param url
     * @param str
     * @return ResponseInfo
     */
	public ResponseInfo post(TreeMap<String, String> headers, String url, String str) {
		ResponseInfo resInfo = new ResponseInfo();
		if(StringUtil.IsNullOrEmpty(url)){
			log.error("url为空");
			resInfo.setResBodyInfo("url为空");
			return resInfo;
		}
		httpPost = new HttpPost(url);
		httpPost=setHttpPostHeaderAndParams(httpPost, headers, null);
		return post(httpPost, str);
	}
   
	/**
	 * get方式下载文件，以字符串方式显示 
	 * @param headers
	 * @param url
	 * @return String
	 */
	public String getFile(Map<String, String> headers, String url,String enCoding) {
		if (StringUtil.IsNullOrEmpty(enCoding)) enCoding="UTF-8";
		httpGet = new HttpGet(url);
		httpGet = setHttpGetHeader(httpGet, headers);
		try {
			return new String(getFile(httpGet),enCoding);
		} catch (UnsupportedEncodingException e) {
			log.error("getFile执行异常207：");
			log.error(e);
			return "";
		}
	}

	/**
	 * get方式下载文件 返回字节
	 * @param headers
	 * @param url
	 * @param file
	 * @return byte[]
	 */
	public byte[] getFile(Map<String, String> headers,String url, File file) {
		httpGet = new HttpGet(url);
		httpGet = setHttpGetHeader(httpGet, headers);
		byte[] bs = getFile(httpGet);
		FileOutputStream output;
		try {
			output = new FileOutputStream(file);
			output.write(bs);
			output.close();
		} catch (FileNotFoundException e) {
			log.error("getFile执行异常223：");
			log.error(e);
		} catch (IOException e) {
			log.error("getFile执行异常226：");
			log.error(e);
		}
		return bs;
	}
	
	/**
	 * 关闭连接
	 */
	public void close() {
		httpClient.getConnectionManager().shutdown();
	}

	/**
	 * 设置编码
	 * @param charset
	 */
	public void setCharset(String charset) {
		if (null != charset && charset.length() > 0) {
			this.charset = charset;
		}
	}
	
	/**
	 * 默认的header信息<br>
	 * Accept-Charset:GBK,utf-8;q=0.7,*;q=0.3<br>
	 * Accept-Encoding:gzip,deflate,sdch<br>
	 * Accept-Language:zh-CN,zh;q=0.8<br>
	 * User-Agent:Q-Test-Automation-Framework-QTAF/1.0.0 Apache-HttpClient/4.2.5 (java 1.7)<br>
	 * Accept:*<br>
	 * Content-Type:application/x-www-form-urlencoded; charset=utf-8<br>
	 * @return Map<String, String>
	 */
	public Map<String, String> getCommonHeader() {
		Map<String, String> headers = new TreeMap<String, String>();
		headers.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//		headers.put("Accept-Encoding", "deflate,sdch");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("User-Agent", "AutoTest-sapient/1.0.0 Apache-HttpClient/4.2.5 (java 1.7)");
		headers.put("Accept", "*/*");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		return headers;
	}
	
	//私有方法
	//------Get
	/**
	 * 设置header信息 会覆盖默认的Header
	 * @param httpget
	 * @param headers
	 * @return HttpGet
	 */
	private HttpGet setHttpGetHeader(HttpGet httpGet,Map<String, String> headers) {
		Map<String, String>  headersTmp=new TreeMap<String, String> ();
    	headersTmp=getCommonHeader();
    	if (headers != null && headers.size()>0) {
    		 Iterator<Entry<String, String>> itetmp = headers.entrySet().iterator();
    	        while(itetmp.hasNext()){
    					Entry<?, ?> entrytmp = (Entry<?, ?>) itetmp.next();
    					headersTmp.put(entrytmp.getKey().toString(), entrytmp.getValue().toString());
    	        }
        }
		for (Entry<String, String> entry : headersTmp.entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
		}
		return httpGet;
	}

	/**
	 * 发起Get请求
	 * @param HttpGet
	 * @return ResponseInfo
	 */
	private ResponseInfo get(HttpGet httpget) {
		ResponseInfo resInfo = new ResponseInfo();
		try {
			response = httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			log.error("get执行异常291：");
			log.error(e);
		} catch (IOException e) {
			log.error("get执行异常294：");
			log.error(e);
		} 
		resInfo.setResBodyInfo(getResBody(response));
		resInfo.setResHeaderInfo(getResHeader(response));
		resInfo.setCookies(getCookies()); //将返回的cookie信息传给resInfo
        return resInfo;
	}

	
	//------Post
	
	/**
     * 封装httpPost请求
     * @param httppost
     * @param headers
     * @param params
     * @return HttpPost
     */
	private HttpPost setHttpPostHeaderAndParams(HttpPost httpPost,Map<String, String> headers, Map<String,Object> params) {
		Map<String, String> headersTmp = new TreeMap<String, String>();
		headersTmp = getCommonHeader();
		if (headers != null && headers.size() > 0) {
			Iterator<Entry<String, String>> itetmp = headers.entrySet().iterator();
			while (itetmp.hasNext()) {
				Entry<?, ?> entrytmp = (Entry<?, ?>) itetmp.next();
				headersTmp.put(entrytmp.getKey().toString(), entrytmp.getValue().toString());
			}
		}
		Iterator<Entry<String, String>> ite1 = headersTmp.entrySet().iterator();
		while (ite1.hasNext()) {
			Entry<?, ?> entry1 = (Entry<?, ?>) ite1.next();
			httpPost.addHeader(entry1.getKey().toString(), entry1.getValue().toString());
		}
		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		if (params != null && !params.isEmpty()) {
			Iterator<Entry<String, Object>> ite2 = params.entrySet().iterator();
			while (ite2.hasNext()) {
				Entry<?, ?> entry2 = (Entry<?, ?>) ite2.next();
				nvps.add(new BasicNameValuePair(entry2.getKey().toString(),entry2.getValue().toString()));
			}
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, this.charset));
		} catch (UnsupportedEncodingException e) {
			log.error("post设置异常338：");
			log.error(e);
		}
		return httpPost;
	}

	//------GetFile
	@SuppressWarnings("deprecation")
	private  byte[] getFile(HttpGet httpget) {
		HttpResponse response;
		byte[] in;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in=EntityUtils.toByteArray(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (ClientProtocolException e) {
			log.error("getFile执行异常357：");
			log.error(e);
			in = null;
		} catch (IOException e) {
			log.error("getFile执行异常361：");
			log.error(e);
			in = null;
		}
		return in;
	}
	
	/**
     * 发起Post请求
     * @param httppost
     * @return ResponseInfo
     */
	private ResponseInfo post(HttpPost httppost) {
		HttpResponse response;
		try {
			response = httpClient.execute(httppost);
		} catch (ClientProtocolException e) {
			log.error("post执行异常378：");
			log.error(e);
			response = null;
		} catch (IOException e) {
			log.error("post执行异常382：");
			log.error(e);
			response = null;
		}
		ResponseInfo resInfo = new ResponseInfo();
		resInfo.setResBodyInfo(getResBody(response));
		resInfo.setResHeaderInfo(getResHeader(response));
		resInfo.setCookies(getCookies());//将返回的cookie信息传给resInfo
		httppost.abort();
		return resInfo;
	}
	
	 /**
     * 封装发送请求的httpPost,str信息
     * @param httppost
     * @param str
     * @return ResponseInfo
     */
    private ResponseInfo post(HttpPost httppost, String str) {
		StringEntity reqEntity;
		try {
			reqEntity = new StringEntity(str);
			httppost.setEntity(reqEntity);
		} catch (UnsupportedEncodingException e) {
			log.error("post执行异常405：");
			log.error(e);
		}
		return post(httppost);
	}
    
    
    /**
	 * 从响应结果中获取Header信息，返回map表
	 * @param res
	 * @return Map<String, String>
	 */
	private Map<String, String> getResHeader(HttpResponse res) {
		Map<String,String> headerRes = new TreeMap<String,String>();
		if (null == res) return headerRes;
//		if (res.getStatusLine().getStatusCode() == 200) {//判断返回码，获取header信息
		Header[] headers = res.getAllHeaders();
		for(int i=0;i<headers.length;i++){
			headerRes.put(headers[i].getName(),headers[i].getValue());
		}
		return headerRes;
	}

	/**
	 * 从响应中获取body信息
	 * @param res
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	private String getResBody(HttpResponse res) {
		String strResult = "";
		if (res == null ) return strResult;
		if (res.getStatusLine().getStatusCode() == 200) {
			try {
				strResult = strResult +
						EntityUtils.toString(res.getEntity(), charset);
			} catch (ParseException e) {
				log.error("获取body信息异常450：");
				log.error(e);
				strResult = e.getMessage().toString();
			} catch (IOException e) {
				log.error("获取body信息异常454：");
				log.error(e);
				strResult = e.getMessage().toString();
			}
		} else if (res.getStatusLine().getStatusCode() == 302) {
			String url = res.getLastHeader("Location").getValue();
			res.setStatusCode(200);
			strResult = url;
			return strResult;
		} else {
			strResult = "Error Response:" + res.getStatusLine().toString();
			if (res.getEntity() != null) {
				try {
					res.getEntity().consumeContent();
				} catch (IOException e) {
					log.error("获取body信息异常469：");
					log.error(e);
				}
			}
		}
		return strResult;
	}
	
	/**
	 * 获取返回结果中的cookies信息
	 * @return
	 */
	private String getCookies(){
		StringBuilder sb = new StringBuilder();
        List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
        for(Cookie cookie: cookies)
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        return sb.toString();
	}
	

}
