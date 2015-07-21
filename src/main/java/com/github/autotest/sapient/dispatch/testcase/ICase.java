package com.github.autotest.sapient.dispatch.testcase;

import java.util.List;

/**
 * 测试集(一个或多个测试集、测试套)
 * @author lianghui (lianghui@360.cn)
 *
 */
public interface ICase {
	
	/**
	 * 获取用例的xml文件路径信息列表
	 * @return List<String>
	 */
	public List<String> getCaseList();
}
