#关键字说明
关键字用与excel中动态生成用例数据;

* rand:随机生成长度为10个字符串（自定义长度修改IftConf.properties中的RandNum）;
* timestamp:获取Unix格式时间戳;
* date:生成日期格式为年月日时分秒的日期;eg:20120626092109 (可自定义date格式,修改IftConf.properties中的dateFormat;)
* null:当为null时不带该参数;