/**
 * 
 */
package tc.bank.utils;

import tc.platform.P_Jdbc;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 参数工具类，主要提供对平台参数表：tp_cip_sysparameters的操作方法
 * @author AlphaLi
 *
 */
public class ParameterUtil {

	
	
	/**
	 * 根据项目名称、交易名称、参数名称、转义key获取对应的转义后的中文描述
	 * @param moduleName
	 * @param transCode
	 * @param paramKey
	 * @param sourceKey
	 * @return
	 */
	public static String TransferKeyName(String moduleName, String transCode, String paramKey, String sourceKey) {
		// TODO Auto-generated method stub
		//参数key查询SQL拼接
		String queryKeyVlaueSQL = "select paramvalue from tp_cip_sysparameters where modulecode='" + moduleName + "' and transcode='" + transCode + "' and paramkeyname='" + paramKey + "'";
		TCResult queryResult = null;
		queryResult = P_Jdbc.dmlSelect(null, queryKeyVlaueSQL, -1);
		if (queryResult == null) {
			//查询参数key异常
			AppLogger.info("查询参数key对应的值异常");
			return "";
		}
		if (queryResult.getStatus() == 2) {
			AppLogger.info("无满足条件的记录");
			return "";
		}
		//获取对应的参数Value
		JavaList queryList = (JavaList) queryResult.getOutputParams().get(1);
		String paramValue = queryList.getListItem(0).get(0).toString();
		//格式化参数Value，转义value对应的格式为：sourceKey#转义中文,sourceKey2#转义中文2
		String[] loopStr = paramValue.split(",");
		for(int i=0;i<loopStr.length;i++){
			String[] tmpStr = loopStr[i].toString().split("#");
			if(tmpStr[0].equals(sourceKey)){
				return tmpStr[1];
			}
		}
		return "";
	}
}
