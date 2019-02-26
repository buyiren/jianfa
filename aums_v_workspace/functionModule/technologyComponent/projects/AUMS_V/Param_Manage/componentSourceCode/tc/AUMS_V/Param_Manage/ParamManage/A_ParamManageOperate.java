package tc.AUMS_V.Param_Manage.ParamManage;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tc.bank.constant.BusException;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 参数管理统一操作类
 * 
 * @date 2018-07-12 16:35:15
 */
@ComponentGroup(level = "应用", groupName = "参数管理操作", projectName = "AUMS_V", appName = "Param_Manage")

public class A_ParamManageOperate {
	
	/**
	 * @category 校验参数Key是否可用
	 * @param paramKey
	 *            入参|参数名称| {@link String}
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "paramKey", comment = "参数名称", type = java.lang.String.class)})
	
	@Returns(returns = { @Return(id = "0", desp = "不可用"),
			@Return(id = "1", desp = "可用") })
	
	@Component(label = "校验参数Key是否可用", style = "判断型", type = "同步组件", comment = "根据参数名称校验该参数名称是否可用", author = "AlphaLi", date = "2018-7-12 15:13:02")
	
	public static TCResult A_CheckParamKey(String paramKey)
			throws BusException {

		if (paramKey == null || paramKey == "") {
			return new TCResult(0, ErrorCode.REMOTE, "参数名称为空");
		}
		String querySQL = "select paramkey from aums_param_info where paramkey='" + paramKey + "'";
		TCResult paramKeyQueryResult = null;
		// 查询参数key是否可用
		paramKeyQueryResult = P_Jdbc.dmlSelect(null, querySQL, -1);
		if (paramKeyQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询参数名称信息异常");
		}
		if (String.valueOf(paramKeyQueryResult.getStatus()).equals("2")) {
			return new TCResult(1, "000000", "该参数名称可用");
		}else{
			return new TCResult(0, ErrorCode.REMOTE, "该参数名称已存在");
		}
	}
	
	/**
	 * @category 参数绑定
	 * @param paramReleaseType
	 * 			   入参|参数发布类型|{@link String}
	 * @param paramID
	 *            入参|参数ID| {@link String}
	 * @param paramType
	 *            入参|参数所属设备设立形式| {@link String}
     * @param paramApp
	 *            入参|参数所属客户端程序| {@link String}
	 * @param paramAppInfoList
	 *            入参|参数所关联客户端程序列表| {@link JavaList}
	 * @param paramDevInfoList
	 *            入参|参数所关联设备列表| {@link JavaList}
	 * @throws BusException
	 */
	
	@InParams(param = {
			@Param(name = "paramReleaseType", comment = "参数发布类型", type = java.lang.String.class),
			@Param(name = "paramID", comment = "参数ID", type = java.lang.String.class),
			@Param(name = "paramType", comment = "参数所属设备形式", type = java.lang.String.class),
			@Param(name = "paramApp", comment = "参数所属客户端程序", type = java.lang.String.class),
			@Param(name = "paramAppInfoList", comment = "参数所关联客户端程序列表", type = JavaList.class),
			@Param(name = "paramDevInfoList", comment = "参数所关联设备列表", type = JavaList.class)
			})
	
	@Returns(returns = { @Return(id = "0", desp = "参数绑定设备失败"),
			@Return(id = "1", desp = "参数绑定设备成功") })
	
	@Component(label = "参数绑定", style = "判断型", type = "同步组件", comment = "参数绑定", author = "AlphaLi", date = "2018-7-12 15:13:02")
	
	public static TCResult A_BindParamInfo(String paramReleaseType,String paramID,String paramType,String paramApp,JavaList paramAppInfoList,JavaList paramDevInfoList) throws BusException{
		
		if(paramReleaseType == null || paramReleaseType==""){
			return new TCResult(0, ErrorCode.REMOTE, "参数发布类型为空");
		}
		//获取入库时间
		Calendar calendar = Calendar.getInstance();
		DateFormat df3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowdatetime = df3.format(calendar.getTime());
		
		if(paramReleaseType.equals("0")){
			//按设备设立形式发布
			//获取相应的设备列表
			String queryDevSQL = "select devid from AUMS_V_SEARCHDEVBYMULT_VIEW where formnum='" + paramType + "'";
			//插入AUMS_PARAM_RELEASEINFO表
			String insertReleaseInfoSQL = "insert into AUMS_PARAM_RELEASEINFO values('" + paramID + "','" + paramReleaseType + "','" + paramType + "','','" + nowdatetime + "','','')";
			P_Jdbc.executeSQL(null, insertReleaseInfoSQL, true);
			//插入AUMS_PARAM_TO_DEV表
			TCResult devTcResult = P_Jdbc.dmlSelect(null, queryDevSQL, -1);
			if(devTcResult==null){
				return new TCResult(0, ErrorCode.REMOTE, "数据库查询异常");
			}
			if(String.valueOf(devTcResult.getStatus()).equals("2")){
				//do nothing
			}else {
				JavaList devList = (JavaList)devTcResult.getOutputParams().get(1);
				for(int i=0;i<devList.size();i++){
					String devId = devList.getListItem(i).get(0).toString();
					String insertParamDevSQL = "insert into AUMS_PARAM_TO_DEV values('" + devId + "','" +paramID +"','" + nowdatetime + "','','')";
					P_Jdbc.executeSQL(null, insertParamDevSQL, true);
				}
			}
			AppLogger.info("按设备设立形式发布完成");
			return new TCResult(1, "000000", "参数绑定成功");
		}else if(paramReleaseType.equals("1")){
			//按客户端程序类型发布
			//获取相应的设备列表
			String paramAppStr = "";
			for(int i=0;i<paramAppInfoList.size();i++){
				paramAppStr = paramAppInfoList.getStringItem(i);
				//插入AUMS_PARAM_RELEASEINFO表
				String insertReleaseInfoSQL = "insert into AUMS_PARAM_RELEASEINFO values('" + paramID + "','" + paramReleaseType + "','" + paramType + "','" + paramAppStr + "','" + nowdatetime + "','','')";
				P_Jdbc.executeSQL(null, insertReleaseInfoSQL, true);
				//插入AUMS_PARAM_TO_DEV表
				String queryDevSQL = "select devid from AUMS_V_SEARCHDEVBYMULT_VIEW where formnum='" + paramType + "' and cprogramid='" + paramAppStr + "'";
				TCResult devTCResult = P_Jdbc.dmlSelect(null, queryDevSQL, -1);
				if(devTCResult==null){
					return new TCResult(0, ErrorCode.REMOTE, "数据库查询异常");
				}
				if(String.valueOf(devTCResult.getStatus()).equals("2")){
					//do nothing
				}else{
					JavaList devList = (JavaList)devTCResult.getOutputParams().get(1);
					for(int j=0;j<devList.size();j++){
						String devId = devList.getListItem(j).get(0).toString();
						String insertParamDevSQL = "insert into AUMS_PARAM_TO_DEV values('" + devId + "','" +paramID +"','" + nowdatetime + "','','')";
						P_Jdbc.executeSQL(null, insertParamDevSQL, true);
					}
				}
			}
			AppLogger.info("按设备客户端程序发布完成");
			return new TCResult(1, "000000", "参数绑定成功");
		}else if(paramReleaseType.equals("2")){
			if(paramDevInfoList == null){
				return new TCResult(0, ErrorCode.REMOTE, "请选择您所需要绑定的设备");
			}
			//按设备发布
			//插入AUMS_PARAM_RELEASEINFO表
			String insertReleaseInfoSQL = "insert into AUMS_PARAM_RELEASEINFO values('" + paramID + "','" + paramReleaseType + "','" + paramType + "','" + paramApp + "','" + nowdatetime + "','','')";
			P_Jdbc.executeSQL(null, insertReleaseInfoSQL, true);
			//插入AUMS_PARAM_TO_DEV表
			for(int i=0;i<paramDevInfoList.size();i++){
				String devId = paramDevInfoList.getStringItem(i).toString();
				String insertParamDevSQL = "insert into AUMS_PARAM_TO_DEV values('" + devId + "','" +paramID +"','" + nowdatetime + "','','')";
				P_Jdbc.executeSQL(null, insertParamDevSQL, true);
			}
			AppLogger.info("按设备发布完成");
			return new TCResult(1, "000000", "参数绑定成功");
		}else{
			return new TCResult(0, ErrorCode.REMOTE, "暂不支持的发布类型");
		}
	}
}
