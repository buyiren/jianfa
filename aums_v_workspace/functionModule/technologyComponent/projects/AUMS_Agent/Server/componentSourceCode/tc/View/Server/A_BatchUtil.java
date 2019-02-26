package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.ArrayList;
import java.util.concurrent.Future;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.agree.afa.jcomponent.TradeInvoker;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;



/**
 * 批量执行工具类
 * 
 * @date 2017-09-12 09:52:23
 */
@ComponentGroup(level = "应用", groupName = "批量执行工具类", projectName = "View", appName = "Server")
public class A_BatchUtil {

	private static final TCResult ret = null;

//	@InParams(param = {
//			@Param(name = "__REQ__", comment = "交易调用请求数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "__RSP__", comment = "交易调用回应数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "hasResponse", comment = "是否有回执(true/false)", type = java.lang.Boolean.class),
//			@Param(name = "timeoutInSecs", comment = "超时时间(秒)", type = java.lang.Integer.class),
//			@Param(name = "srvIdentifier", comment = "接出渠道", type = java.lang.String.class),
//			@Param(name = "MC", comment = "模板代码", type = java.lang.String.class),
//			@Param(name = "TC", comment = "交易代码", type = java.lang.String.class),
//			@Param(name = "DEVINFO", comment = "机具列表", type = java.lang.Object.class) })
//	@OutParams(param = { @Param(name = "future", comment = "调度结果", type = java.util.concurrent.Future.class) })
//	@Returns(returns = { @Return(id = "0", desp = "调用失败"),
//			@Return(id = "2", desp = "异常"), @Return(id = "1", desp = "调用成功") })
//	@Component(label = "异步批量交易调度", style = "选择型", type = "异步组件", comment = "异步批量交易调度", author = "李阔", date = "2017-09-12 09:52:23")
//	public static TCResult A_Dispatch_CallTrade(JavaDict __REQ__,
//			JavaDict __RSP__, Boolean hasResponse, int timeoutInSecs,
//			String srvIdentifier, String MC, String TC, Object DEVINFO) {
//
//		//将设备信息转换为JSON对象
//		JSONObject jsonObject = JSONObject.fromObject(DEVINFO);
//		//JSON对象转换为数组
//		JSONArray jsonArray = jsonObject.getJSONArray("DEVINFO");
//		//定义数据长度
//		int arrayLength = jsonArray.size();
//		AppLogger.info("传入机具对应的数组size为："+arrayLength);
//		//循环调用方法
//		TCResult ret = null;
//		for(int i=0;i<arrayLength;i++){
//			JSONObject jsonTemp = (JSONObject)jsonArray.getJSONObject(i);
//			__REQ__.put("clent_ip", "");
//			//异步调用交易
//			try {
//				if (!(MC instanceof String))
//					return TCResult.newFailureResult("FBAC01", "模板代码类型不合法,不为字符串");
//				if (!(TC instanceof String))
//					return TCResult.newFailureResult("FBAC01", "交易代码类型不合法,不为字符串");
//
//				ret = TradeInvoker.asyncInvokePubTrade(MC, TC, __REQ__,
//						timeoutInSecs, hasResponse, srvIdentifier);
//			} catch (Exception e) {
//				return new TCResult(2, "FBAE99", "调用时异常," + e.getMessage());
//			}
//		}
//		return TCResult.newSuccessResult((Future<?>) ret.getOutputParams().get(0));
//	}

	
	@InParams(param = {
			@Param(name = "arrayKey", comment = "json数组key", type = java.lang.String.class),
			@Param(name = "arrayInfo", comment = "json数组字符串", type = java.lang.Object.class) })
	@OutParams(param = {
			@Param(name = "arraySize", comment = "数组长度", type = java.lang.Integer.class),
			@Param(name = "arrayList", comment = "ArrayList数组", type = java.util.List.class),
			})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "计算json数组串长度", style = "判断型", type = "同步", comment = "传入json串,计算数组串长度", author = "李阔", date = "2017-09-12 09:52:23")
	public static TCResult A_CallTrade(String arrayKey,Object arrayInfo) {

		//将设备信息转换为JSON对象
		JSONObject jsonObject = JSONObject.fromObject(arrayInfo);
		//JSON对象转换为数组
		JSONArray jsonArray = jsonObject.getJSONArray(arrayKey);
		//获取数组长度
		int arrayLength = jsonArray.size();
		//将数据存储在java-List内
		ArrayList arrayList = new ArrayList();
		for(int i=0;i<arrayLength;i++){
			arrayList.add(jsonArray.get(i));
		}
		AppLogger.info("传入机具对应的数组size为："+arrayLength);
		//返回
		return TCResult.newSuccessResult(arrayLength,arrayList);
	}
}
