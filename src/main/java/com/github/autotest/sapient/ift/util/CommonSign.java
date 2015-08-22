package com.github.autotest.sapient.ift.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.github.autotest.sapient.toolkit.util.CommUtils;
import com.github.autotest.sapient.toolkit.util.LogUtil;

/**
 * 各种签名算法
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a> (bwgang@163.com)<br/>
 */
public class CommonSign {
	
	private static LogUtil log = LogUtil.getLogger(CommonSign.class);// 日志记录

	/**
	 * 说明：1.签名算法1 key=value&key=value&.....key=valuesecret_key 
	 * 2.最后连接私钥时不带&符号
	 * 3.计算MD5时，中文按照UTF-8编码计算
	 * @param signpara 参与签名计算的键值对
	 * @param secret_key 签名计算所需的密钥
	 * @return String
	 */
	public static String signMethodOne(TreeMap<String, String> signpara,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			expBaseSign = expBaseSign + entry.getKey() + "="
					+ entry.getValue().toString() + "&";
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1)
					+ secret_key;
		}
		return CommUtils.getMD5(expSign,"UTF-8");
	}

	/**
	 * 说明：1.签名算法2 key=value&key=value&.....key=valuesecret_key 
	 * 2.最后连接私钥时不带&符号
	 * 3.计算MD5时，中文按照GBK编码计算
	 * @param signpara 参与签名计算的键值对
	 * @param secret_key 签名计算所需的密钥
	 * @return String
	 */
	public static String signMethodTwo(TreeMap<String, String> signpara,String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			expBaseSign = expBaseSign + entry.getKey() + "="
					+ entry.getValue().toString() + "&";
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1)
					+ secret_key;
		}
		log.info("签名计算串：" + expSign);
		return CommUtils.getMD5Gbk(expSign);
	}

	/**
	 * 说明：签名算法3  1.key=value&key=value&.....key=valuesecret_key
	 * 2.value为空时 不参与签名计算
	 * 3.最后连接私钥时不带&符号 
	 * 4.计算MD5时，中文按照UTF-8编码计算
	 * 
	 * @param signpara 参与签名计算的键值对
	 * @param secret_key 签名计算所需的密钥
	 * @return String
	 */
	public static String signMethodThird(TreeMap<String, String> signpara,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			if (entry.getValue().toString().length() > 0) {
				expBaseSign = expBaseSign + entry.getKey() + "=" + entry.getValue().toString() + "&";
			}
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign.substring(0, expBaseSign.length() - 1) + secret_key;
		}
		return CommUtils.getMD5(expSign,"UTF-8");
	}
	
	/**
	 * 说明：签名算法4  1.key=value&key=value&.....key=valuesecret_key
	 * 2.value为空时 不参与签名计算
	 * 3.最后连接私钥时带&符号 
	 * 4.计算MD5时，中文按照UTF-8编码计算
	 * 
	 * @param signpara 参与签名计算的键值对
	 * @param secret_key 签名计算所需的密钥
	 * @return String
	 */
	public static String signMethodFour(TreeMap<String, String> signpara,
			String secret_key) {
		String expBaseSign = "";
		String expSign = "";

		Iterator<?> ite = signpara.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			if (entry.getValue().toString().length() > 0) {
				expBaseSign = expBaseSign + entry.getKey() + "=" +entry.getValue().toString() + "&";
			}
		}
		if (expBaseSign.length() < 1) {
			expSign = expBaseSign + secret_key;
		} else {
			expSign = expBaseSign + secret_key;
		}
		return CommUtils.getMD5(expSign,"UTF-8");
	}
}
