#Excel各参数说明
主要讲解在excel中一些参数的设置规则;

##Url
* Url:为http/https请求的url内容;如果有参数值则为?前面的值(包含？);
* 如果想要构造不同的用例使用不同的Url，可以在用例中加入一列（secondurl）;以后请求时为Url+secondurl进行拼接请求;
* Url后面的参数值可以为关键字“host”,当为host时会使用DemoConf.java中的Host值进行全局替换;

##secondurl
* 当secondurl不为null且不为空时，请求的url为用例中的Url+secondurl;
* 当secondurl为rand关键词时生成5位随机数，请求的url为用例中的Url+5位随机数;

##HttpMethod
* 支持Post与Get请求（不区分大小写）

##Cookie
* 分为全局cookie与参数cookie(参数cookie需要在excel中加cookie列);
* 全局cookie如果不为空则每个请求都带全局cookie;
* 参数cookie，默认没有此参数列，如果带且不为空，则替换全局cookie（注：如果添加cookie列时，应注意重新设置IftConf类中的paramStartCol=4）; 

##ArgCount
* 参与签名的参数个数(必须为int,不能为null或为空);
* 当为0时不进行签名;
* 计算签名从第4列开始进行计数（包含第四列）; 
* 签名列的后一位应为待赋值列（sign），后面再跟post参数列;
* 现有的签名算法（commonSign类）：key=value&key=value&......key=valuecret_key对整个字符串进行MD5加密;说明：key为参数，value为参数值，secret_key为签名所需要的密钥;
* 支持的签名算法有：a.中文按照不同编码进行签名（utf-8,GBK）;b.value为空不参加签名，value为空也参加签名;

##CaseID
* CaseID不能重复;
*　CaseID和Excel表的sheet命名要满足java命名规范；

##Run
* 执行用例的开关，当为Y时执行此条用例，当不为Y时不执行（关键字必须为大写的Y或大写N）;

##Sign
* 先判断ArgCount是否进行签名;
* 当Sign中的参数值为空时（value.length() < 1）请求时Sign为空(不会将计算后的签名进行赋值);
* 当参数为null时（不区分大小写），不带sign参数;
* 当参数不为空且不为null时，会直接用计算后的签名对Sign进行重新赋值;

##Expres
* 预期结果格式为：key=value;
* 支持多个键值对的比对：key1=value1&key2=value2&key3=value3;
* 支持一个key可能对应多个值：key11=value11#value12&key2=value2;
* 支持一个key一定会对应多个值 ：key=[value1,value2,value3],预期结果也可写成key=value1为包含的关系;
* 对于http返回的结果可以进行xml解析与json解析;
* json解析默认为单层解析，也可以支持多层解析（修改IftConf.properties中的JsonNum值为，0为多层解析1为单层解析）;
* 当期望没有明确的值而是期望返回为某种数据类型时，如返回为一串数字时可以：key=int;

##TestPoint
* 测试点，用于该条用例的说明;
