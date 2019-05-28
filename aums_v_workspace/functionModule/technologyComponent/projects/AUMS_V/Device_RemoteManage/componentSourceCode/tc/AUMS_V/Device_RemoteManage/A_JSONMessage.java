package tc.AUMS_V.Device_RemoteManage;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.List;
import java.util.Map;

import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @date 2017-04-08 10:57:32
 */
@ComponentGroup(level = "应用", groupName = "json解析组件", projectName = "AAAA", appName = "server")
public class A_JSONMessage {

	/**
	 * @param obj
	 *            入参|javaBean|{@link java.lang.Object}
	 * @param jsonStr
	 *            出参|json字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "obj", comment = "javaBean", type = java.lang.Object.class) })
	@OutParams(param = { @Param(name = "jsonStr", comment = "json字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "javaBean转换json串", style = "判断型", type = "同步组件", comment = "将javaBean转换成json字符串", date = "2015-12-22 10:04:52")
	public static TCResult beanToStr(Object obj) {
		if (obj == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数为null");
		}
		String jsonStr = JSON.toJSONString(obj,
				SerializerFeature.WriteMapNullValue);
		return TCResult.newSuccessResult(jsonStr);
	}

	/**
	 * @category javaDict转换json串
	 * @param dict
	 *            入参|字典|{@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param jsonStr
	 *            出参|json字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "dict", comment = "字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = { @Param(name = "jsonStr", comment = "json字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "javaDict转换json串", style = "判断型", type = "同步组件", comment = "平台字典数据结构的对象转换成json字符串", date = "2017-04-10 09:50:37")
	public static TCResult dictToStr(JavaDict dict) {
		byte[] a = null;
		if (dict == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数为null");
		}
		String jsonStr = JSON.toJSONString(dict,
				SerializerFeature.WriteMapNullValue);
		return TCResult.newSuccessResult(jsonStr);
	}

	/**
	 * @param obj
	 *            入参|javaBean|{@link java.lang.Object}
	 * @param formattedJsonStr
	 *            出参|格式优雅的json字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "obj", comment = "javaBean", type = java.lang.Object.class) })
	@OutParams(param = { @Param(name = "formattedJsonStr", comment = "格式优雅的json字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "javaBean转换格式化json串", style = "判断型", type = "同步组件", comment = "将javaBean转换成格式优雅的json字符串", date = "2015-12-22 10:05:47")
	public static TCResult beanToFormattedStr(Object obj) {
		if (obj == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数为null");
		}
		String formattedJsonStr = JSON.toJSONString(obj,
				SerializerFeature.PrettyFormat,
				SerializerFeature.WriteMapNullValue);
		return TCResult.newSuccessResult(formattedJsonStr);
	}

	/**
	 * @param dict
	 *            入参|字典|{@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param formattedJsonStr
	 *            出参|格式优美的json字符串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "dict", comment = "字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = { @Param(name = "formattedJsonStr", comment = "格式优美的json字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "javaDict转换格式化json串", style = "判断型", type = "同步组件", comment = "将平台字典数据结构的对象转换格式优美的json字符串", date = "2015-12-22 10:06:10")
	public static TCResult dictToFormattedStr(JavaDict dict) {
		if (dict == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数为null");
		}
		String formattedJsonStr = JSON.toJSONString(dict,
				SerializerFeature.PrettyFormat,
				SerializerFeature.WriteMapNullValue);
		return TCResult.newSuccessResult(formattedJsonStr);
	}

	private static JavaList getJavaList(JSONArray seq) {
		JavaList list = new JavaList();
		for (int i = 0; i < seq.size(); i++) {
			Object value = seq.get(i);
			if (value == null) {
				list.add(null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				list.add(getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				list.add(getJavaDict((JSONObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

	private static JavaDict getJavaDict(JSONObject jsonObj) {
		JavaDict dict = new JavaDict();

		for (@SuppressWarnings("rawtypes")
		Map.Entry entry : jsonObj.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				dict.setItem(key, null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				dict.setItem(key, getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				dict.setItem(key, getJavaDict((JSONObject) value));
			} else {
				dict.setItem(key, value);
			}
		}
		return dict;
	}

	/**
	 * @param className
	 *            入参|完整的类的名称，要包含包名|{@link java.lang.String}
	 * @param jsonStr
	 *            入参|json字符串|{@link java.lang.String}
	 * @param obj
	 *            出参|javaBean|{@link Object}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "className", comment = "完整的类的名称，要包含包名", type = java.lang.String.class),
			@Param(name = "jsonStr", comment = "json字符串", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "obj", comment = "javaBean", type = Object.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "json串转换javaBean", style = "判断型", type = "同步组件", comment = "json字符串转换为javaBean", date = "2015-12-22 10:13:44")
	public static TCResult strToBean(String className, String jsonStr) {
		if (className == null || className.equals("")) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"className参数为null或为空字符串");
		}
		if (jsonStr == null || jsonStr.equals("")) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"jsonStr参数为null或为空字符串");
		}
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			return TCResult.newFailureResult(ErrorCode.PARAM, e);
		}
		Object bean = JSON.parseObject(jsonStr, cls);
		return TCResult.newSuccessResult(bean);
	}

	/**
	 * @param dict
	 *            入参|字典|{@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param jsonStr
	 *            入参|json串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "dict", comment = "字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "jsonStr", comment = "json串的byte数组", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "json串转换javaDict", style = "判断型", type = "同步组件", comment = "json字符串转换平台字典类型对象", date = "2015-12-22 10:41:15")
	public static TCResult strToDict(JavaDict dict, String jsonStr) {
		if (jsonStr == null) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"jsonStr参数为null或为空串");
		}
		if (dict == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "dict参数为null");
		}
		JSONObject jsonObj = (JSONObject) JSON.parseObject(new String(jsonStr));
		JavaDict jsonDict = getJavaDict(jsonObj);
		dict.putAll(jsonDict);
		return TCResult.newSuccessResult();
	}

	/**
	 * @category json拆包
	 * @param REQdict
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param resultdict_name
	 *            入参|存放结果的容器名|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "REQdict", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "resultdict_name", comment = "存放结果的容器名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "json拆包", style = "判断型", type = "同步组件", comment = "解析json格式的__REQ__['__RCVPCK__']", author = "Anonymous", date = "2017-04-10 09:59:07")
	public static TCResult A_json_unpack(JavaDict REQdict,
			String resultdict_name) {
		byte[] b = (byte[]) REQdict.get("__RCVPCK__");
		JSONObject jsonObj = (JSONObject) JSON.parseObject(new String(b));
		JavaDict jsonDict = getJavaDict(jsonObj);
		REQdict.put(resultdict_name, jsonDict);
		return TCResult.newSuccessResult();
	}

	/**
	 * @category json拼包
	 * @param RSPdict
	 *            入参|响应容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param respdict_name
	 *            入参|存放响应报文的容器名|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "RSPdict", comment = "响应容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "respdict_name", comment = "存放响应报文的容器名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "json拼包", style = "判断型", type = "同步组件", comment = "给定一个dict对象，组件将dict对象转为json字节数组，存放在__RSP__['__SNDPCK__']", author = "Anonymous", date = "2017-04-10 11:24:03")
	public static TCResult A_json_pack(JavaDict RSPdict, String respdict_name) {
		if (RSPdict == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数为null");
		}
		String jsonStr = JSON.toJSONString((JavaDict)RSPdict.get(respdict_name),
				SerializerFeature.WriteMapNullValue);
		RSPdict.put("__SNDPCK__", jsonStr.getBytes());
		return TCResult.newSuccessResult();
	}

	// /**
	// * @category JSON拆包
	// * @param jsonStr
	// * 入参|json字符串|{@link java.lang.String}
	// * @param dict
	// * 出参|存放结果的dict|
	// * {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	// * @return 0 失败<br/>
	// * 1 成功<br/>
	// */
	// @InParams(param = { @Param(name = "jsonStr", comment = "json字符串", type =
	// java.lang.String.class) })
	// @OutParams(param = { @Param(name = "dict", comment = "存放结果的dict", type =
	// cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	// @Returns(returns = { @Return(id = "0", desp = "失败"),
	// @Return(id = "1", desp = "成功") })
	// @Component(label = "JSON拆包", style = "判断型", type = "同步组件", author =
	// "Anonymous", date = "2017-04-08 10:58:54")
	// public static TCResult A_JSON_unpack(String jsonStr) {
	// JavaDict container = new JavaDict();
	// container=JSONAnalyser.jsonStr2JavaDict(jsonStr, container);
	// return TCResult.newSuccessResult(container);
	// }
	//
	// /**
	// * @category JSON拼包
	// * @param dict
	// * 入参|要打包的容器|
	// * {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	// * @param jsonStr
	// * 出参|json字符串|{@link java.lang.String}
	// * @return 0 失败<br/>
	// * 1 成功<br/>
	// */
	// @InParams(param = { @Param(name = "dict", comment = "要打包的容器", type =
	// cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	// @OutParams(param = { @Param(name = "jsonStr", comment = "json字符串", type =
	// java.lang.String.class) })
	// @Returns(returns = { @Return(id = "0", desp = "失败"),
	// @Return(id = "1", desp = "成功") })
	// @Component(label = "json拼包", style = "判断型", type = "同步组件", author =
	// "Anonymous", date = "2017-04-08 11:00:15")
	// public static TCResult A_JSON_pack(JavaDict dict) {
	// String jsonStr = JSONAnalyser.javaDict2JSONStr(dict);
	// return TCResult.newSuccessResult(jsonStr);
	// }
	
	/**
	 * @param dict
	 *            入参|字典|{@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param list
	 *            入参|list列表|{@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param serviceIdentifier
	 *            入参|服务标识|{@link java.lang.String}
	 * @param cacheName
	 *            入参|缓存容器名称|{@link java.lang.String}
	 * @param pattern
	 *            入参|key的映射表达式|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "dict", comment = "字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "list", comment = "list列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "serviceIdentifier", comment = "服务标识", type = java.lang.String.class),
			@Param(name = "cacheName", comment = "缓存容器名称", type = java.lang.String.class),
			@Param(name = "pattern", comment = "key的映射表达式", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "拆C端返回的状态报文", style = "判断型", type = "同步组件", comment = "拆C端返回的状态报文，并把结果放入字典", date = "2017-09-28 21:35:15")
	public static TCResult unpackStatusJson(JavaDict dict,JavaList list,String serviceIdentifier, String cacheName,String pattern) {
		AppLogger.info("拆包开始");
		/*
		 * 输入参数举例：
		 * dict={}
		 * list=[{"moduleName":"CardReader","status":{"antiFraudModule":0,"mixedMode":0,"powerSaveRecoverTime":0,"devicePosition":0,"guidLights":[],"extra":{"errorCode":"2202017000","errErrDescrible":""},"positions":[{"transportStatus":0,"transport":0,"positionStatus":0,"shutter":0,"position":32},{"transportStatus":0,"transport":0,"positionStatus":1,"shutter":4,"position":8192}],"dropBox":false,"banknoteReader":0,"stackerItems":4,"intermediateStacker":0,"acceptor":0,"safeDoor":1,"device":0},"error":null},{"moduleName":"FingeprintReader","status":{"antiFraudModule":0,"powerSaveRecoveryTime":0,"cdmDevicePosition":0,"guidLights":[],"extra":{"name":"GWI_CRM_XFS300","spVersion":"Ver1.0.2.1","driverVersion":"XFS3.0.0","firmwareVersion":"URBoot:TS-P177-S120100,00020000;FPGA:URFPGA-,000006","lastErrCode":"9F00000000","lastDesc":"IncorrecttimingforissuingFirmwareVersionRead","dispenseToSlot":""},"positions":[{"transportStatus":0,"transport":0,"positionStatus":1,"shutter":4,"position":2048}],"intermediateStacker":5,"dispenser":2,"safeDoor":1,"device":0},"error":null}]
		 * serviceIdentifier="redis"
		 * cacheName="cache"
		 * pattern="_DEV_4237_SMAP_*"
		 * 交易先到redis里面查询pattern对应的key，并根据这些key查询对应的value值，这些value值用.号分割，分割后数组的第一个值为模块名称，第二、三...分别为list参数中的节点路径。该方法就是把这些value作为新的key，获取这些路径对应的值作为新的value
		 * 如查询pattern对应的key和value分别为_DEV_4237_SMAP_2_：CardReader.status.device、_DEV_4237_SMAP_3_：FingeprintReader.status.device、_DEV_4237_SMAP_5_：IDCardReader.status.device，该方法实现dict={"CardReader.status.device":0,"FingeprintReader.status.device":0}
		*/
		List keyList = CacheOperationImpl.keys(serviceIdentifier, cacheName, pattern);
		AppLogger.info("redis获取到的机具模块key为：【" + keyList + "】");
		JavaDict mapDict = new JavaDict();
		if (keyList.size()==0){
			AppLogger.info("该设备无对应的redis key，请确认该设备是否成功注册！");
			return TCResult.newSuccessResult();
		}
		for (int i = 0; i < keyList.size(); i++) {
			String map_key = (String) keyList.get(i);
			AppLogger.info("map_key:"+map_key);
			//到redis里面查询pattern对应的key
			String map_value=(String) CacheOperationImpl.get(serviceIdentifier,cacheName, map_key);
			AppLogger.info("map_value:"+map_value);
			if (map_value.indexOf(".")!=-1){
				AppLogger.info("map_valueSplit:"+map_value.split("\\.").toString());
				mapDict.setItem(map_value.split("\\.")[0], map_value);
			}
		}
		JavaDict statusDict_1 = new JavaDict();
		AppLogger.info("获取映射列表成功");
		//根据这些key查询对应的value值，然后对每个key进行循环处理
		for (int i=0;i<list.size();i++){
			JavaDict dictlist=list.getDictItem(i);
			String key=dictlist.getStringItem("ModuleName");
			AppLogger.info("modelname:"+key);
			
			if ("".equals(key)) {
				return TCResult.newFailureResult(ErrorCode.AGR, "list的每一个子项都是一个字典,且每个字典都必须包含modelname");
			}
			//确定报文中的moduleName对应的值，是否在redis里面也具有，如果有，则把路径中含有的值赋值到key里面
			if (mapDict.hasKey(key)){
				statusDict_1=packMap(mapDict,dictlist,mapDict.getStringItem(key));
				dict.putAll(statusDict_1);
			}else{
				continue;
			}
		}
		AppLogger.info("拆包结束");
		return TCResult.newSuccessResult();
	}
	
	public static JavaDict packMap(JavaDict resultDict,JavaDict sourceDict,String mapstr){
		AppLogger.info("调用packMap方法开始");
		AppLogger.info("param3:"+mapstr);
		AppLogger.info("param1:"+String.valueOf(resultDict));
		AppLogger.info("param2:"+String.valueOf(sourceDict));
		int mapstrlength=0;
		if (mapstr.indexOf(".")!=-1){
			mapstrlength=mapstr.split("\\.").length;
		}
		//不含.号是不合法的
		AppLogger.info("mapstrlength:"+String.valueOf(mapstrlength));
		JavaDict result=(JavaDict) sourceDict.clone();
		for (int k=1;k<mapstrlength;k++){
			Object value =result.getItem(mapstr.split("\\.")[k]);
			//AppLogger.info("value:"+String.valueOf(value));
			//AppLogger.info("k:"+String.valueOf(k));
			if (value == null) {
				result.setItem(mapstr, null);
				return result;
			}
			if (value.getClass() ==JavaDict.class){
				result=(JavaDict) ((JavaDict) value).clone();
			}else if (value.getClass() ==String.class){
				if (k==mapstrlength-1){
				    result.setItem(mapstr, value);   
				}else{
				    result.setItem(mapstr, null);
				}
			}else{
				result.setItem(mapstr, value);
			}
		}
		resultDict.put(mapstr, result.getItem(mapstr));
		AppLogger.info("调用packMap方法结束");
		return resultDict;
	}
	
	public static int getLength(String s){
		int splitSize=s.split(".").length;
		return splitSize;
	}

}
