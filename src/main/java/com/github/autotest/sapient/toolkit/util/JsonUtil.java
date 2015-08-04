package com.github.autotest.sapient.toolkit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.github.autotest.sapient.ift.core.CasesUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 说明：解析Json字符串，解析后为map存储的键值对
 * @author lianghui (lianghui@360.cn)
 *
 */
public class JsonUtil {
	/**
	 * 日志记录
	 */
	protected static LogUtil log = LogUtil.getLogger(CasesUtils.class);
	
	private Map<String, Object> oneResult = new TreeMap<String, Object>();

	/**
	 * 单层解析json字符串
	 * @param str
	 * @return Map<String, Object> 异常返回null
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getResult(String str) {
		JsonUtil jsonUtil = new JsonUtil();
		try {
			jsonUtil.oneResult = JSONObject.fromObject(str);
		} catch (JSONException e) {
			log.error("单层解析json异常"+e);
			jsonUtil.oneResult = null;
		}
		return jsonUtil.oneResult;
	}

	/**
	 * 多层解析 json字符串
	 * @param str
	 * @return	Map<String, Object> 异常返回null
	 */
	public static Map<String, Object> getAllResult(String str) {
		JsonUtil jsonUtil = new JsonUtil();
		try {
			JSONObject tempJSON = JSONObject.fromObject(str);
			jsonToMap(tempJSON, jsonUtil.oneResult);
		} catch (Exception e) {
			log.error("多层解析json异常"+e);
			jsonUtil.oneResult = null;
		}
		return jsonUtil.oneResult;
	}

	/**
	 * 将json解析成map
	 * @param tempJSON
	 * @param resultMap
	 */
	@SuppressWarnings("unchecked")
	private static void jsonToMap(JSONObject tempJSON,Map<String,Object> resultMap) {
		for (Iterator<String> it = tempJSON.keys(); it.hasNext();) {
			String key = it.next();
			String realKey = key;
			Object valueObj = tempJSON.get(key);
	        if(judgeJson(valueObj,resultMap)){//判断对象是否为json对象，如果不是则直接处理
				if (resultMap.containsKey(realKey)) { // 判断是否存在相同的key，如果存在相同的key，map将用<String,ArrayList>泛型存储
					ArrayList valueList = new ArrayList();
					if (resultMap.get(realKey) instanceof ArrayList) { // 判断value是否已经为ArrayList，等二次发现有相同的key
						valueList = (ArrayList) resultMap.get(realKey);
						valueList.add(valueObj.toString().trim());
						resultMap.put(realKey, valueList);
					} else { // 第一次处理相同的key，new一个ArrayList
						valueList.add(resultMap.get(realKey));
						valueList.add(valueObj.toString().trim());
						resultMap.put(realKey, valueList);
					}

				} else {
					resultMap.put(realKey, valueObj.toString().trim());
				}

			}
		}
	}
	
	/**
	 * 判断对象是JsonObject还是JsonArray
	 * @param obj
	 * @param resultMap
	 * @return
	 */
	private static boolean judgeJson(Object obj,Map<String,Object> resultMap){
		if (obj instanceof JSONObject) {
			JSONObject jo = (JSONObject) obj;
			jsonToMap(jo,resultMap);
		} else if (obj instanceof JSONArray) {
			JSONArray ja = (JSONArray) obj;
			for (int i = 0; i < ja.size(); i++) {
				judgeJson(ja.get(i),resultMap); //判断数组中的对象进行判断
			}
		}else{
			return true;
		}
		return false;
	}

}
