package tc.AUMS_V.MonitorService.A_MonitorUtil;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.SimpleDateFormat;
import java.util.Date;

import tc.bank.constant.BusException;
import tc.bank.utils.ParameterUtil;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 监控基础工具类
 * 
 * @date 2018-07-26 18:4:0
 */
@ComponentGroup(level = "应用", groupName = "监控基础工具类", projectName = "AUMS_V", appName = "MonitorService")
public class A_MonitorUtil {

	/**
	 * @category 大屏监控获取交易信息
	 * @throws BusException
	 */
	@InParams(param = { @Param(name = "brnoList", comment = "待查询的机构号", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "当日交易基础信息(返dict)", style = "选择型", type = "同步组件", comment = "查询当日交易基础信息", author = "AlphaLi", date = "2018-7-12 15:13:02")
	public static TCResult A_MonitorScreenTradeInfo(String brnoList)
			throws BusException {

		String queryTotalTradeSQL = "select transcode as tradeCode,count(transcode) as tradeCount,transname as tradeName from aums_sys_paymentbook where brno in ("
				+ brnoList
				+ ") group by transcode,transname order by tradecount desc";

		TCResult paramKeyQueryResult = null;
		paramKeyQueryResult = P_Jdbc.dmlSelect(null, queryTotalTradeSQL, -1);
		if (paramKeyQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询交易汇总信息异常");
		}
		if (paramKeyQueryResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}

		JavaList loopTmpList = (JavaList) paramKeyQueryResult.getOutputParams()
				.get(1);
		JavaList result = new JavaList();
		for (int i = 0; i < loopTmpList.size(); i++) {
			String tradeCode = loopTmpList.getListItem(i).get(0).toString();
			String quertSuccessCountSQL = "select count(*) as successcount from aums_sys_paymentbook where tradestatus='0' and transcode='"
					+ tradeCode + "'";
			TCResult tmpQueryResult = null;
			tmpQueryResult = P_Jdbc.dmlSelect(null, quertSuccessCountSQL, -1);
			JavaList successCountList = (JavaList) tmpQueryResult
					.getOutputParams().get(1);
			String successCount = successCountList.getListItem(0).get(0)
					.toString();
			String tradeName = loopTmpList.getListItem(i).get(2).toString();
			String tradeAllCount = loopTmpList.getListItem(i).get(1).toString();
			JavaDict dict = new JavaDict();
			dict.put("tradeCode", tradeCode);
			dict.put("tradeCount", tradeAllCount);
			dict.put("tradeName", tradeName);
			dict.put("successCount", successCount);
			result.add(dict);
		}
		if (result.size() == 0) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		return TCResult.newSuccessResult(result);
	}

	/**
	 * @category 大屏监控获取设备信息（设备运行情况）
	 * @throws BusException
	 */
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "设备运行情况基础信息(返dict)", style = "选择型", type = "同步组件", comment = "查询设备运行情况基础信息", author = "AlphaLi", date = "2018-7-12 15:13:02")
	public static TCResult A_MonitorScreenDevStatusInfo() throws BusException {

		String queryDevStatusSQL = "select runningstatus,count(*) as devStatusNum from aums_dev_status_info group by runningstatus";

		TCResult paramKeyQueryResult = null;
		paramKeyQueryResult = P_Jdbc.dmlSelect(null, queryDevStatusSQL, -1);
		if (paramKeyQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询设备状态汇总信息异常");
		}
		if (paramKeyQueryResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}

		JavaList loopTmpList = (JavaList) paramKeyQueryResult.getOutputParams()
				.get(1);
		JavaList result = new JavaList();
		for (int i = 0; i < loopTmpList.size(); i++) {
			String runningStatus = loopTmpList.getListItem(i).get(0).toString();
			String devStatusNum = loopTmpList.getListItem(i).get(1).toString();
			String runningStatusDesc = "";
			AppLogger.info("大屏监控，设备状态为初始值为：【"+runningStatus+"】");
			// 转义
			switch (Integer.valueOf(runningStatus).intValue()) {
			case 0:
				runningStatusDesc = "正常";
				break;
			case 1:
				runningStatusDesc = "停止服务";
				break;
			case 2:
				runningStatusDesc = "部分服务";
				break;
			case 3:
				runningStatusDesc = "未知";
				break;
			case 4:
				runningStatusDesc = "P通讯故障";
				break;
			case 5:
				runningStatusDesc = "维护";
				break;
			case 6:
				runningStatusDesc = "关机";
				break;
			case 7:
				runningStatusDesc = "停用";
				break;
			default:
				runningStatusDesc = "异常";
				break;
			}
			AppLogger.info("大屏监控，设备状态为转义后值为：【"+runningStatusDesc+"】");
			JavaDict dict = new JavaDict();
			dict.put("runningStatusDesc", runningStatusDesc);
			dict.put("devStatusNum", devStatusNum);
			result.add(dict);
		}
		if (result.size() == 0) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		return TCResult.newSuccessResult(result);
	}
	
	/**
	 * @category 大屏监控获取设备厂商信息（设备厂商排名）
	 * @throws BusException
	 */
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "设备厂商排名(返dict)", style = "选择型", type = "同步组件", comment = "查询设备厂商排名", author = "AlphaLi", date = "2018-7-12 15:13:02")
	public static TCResult A_MonitorScreenDevBrandInfo() throws BusException {

		String queryDevBrandInfoSQL = "select devbrandid,brandname from aums_dev_brandinfo";

		TCResult paramKeyQueryResult = null;
		paramKeyQueryResult = P_Jdbc.dmlSelect(null, queryDevBrandInfoSQL, -1);
		if (paramKeyQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询设备厂商信息异常");
		}
		if (paramKeyQueryResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}

		JavaList loopTmpList = (JavaList) paramKeyQueryResult.getOutputParams().get(1);
		JavaList result = new JavaList();
		for (int i = 0; i < loopTmpList.size(); i++) {
			//厂商信息
			String devBrandId = loopTmpList.getListItem(i).get(0).toString();
			String devBrandName = loopTmpList.getListItem(i).get(1).toString();
			
			//根据厂商信息，获取设备分类，然后获取设备总数
			String queryAllDev = "select count(*) totcalNum from aums_dev_info A,aums_v_devassortment_view B where A.DEVASSORTMENTID=B.ASSORTMENTID and b.devbrandid='" + devBrandId + "'";
			JavaList totalNumList = (JavaList)P_Jdbc.dmlSelect(null, queryAllDev, -1).getOutputParams().get(1);
			String totalNum = totalNumList.getListItem(0).get(0).toString();
			
			//根据厂商信息，获取设备分类，同时根据设备汇报表，获取问题设备数
			String queryFailDev = "select count(*) errorStatusNum from aums_dev_info A,aums_v_devassortment_view B,aums_dev_status_info C where A.DEVASSORTMENTID=B.ASSORTMENTID and a.devid=c.devid and b.devbrandid='" + devBrandId + "' and c.runningstatus!='0'";
			JavaList failNumList = (JavaList)P_Jdbc.dmlSelect(null, queryFailDev, -1).getOutputParams().get(1);
			String failNum = failNumList.getListItem(0).get(0).toString();
			
			JavaDict dict = new JavaDict();
			dict.put("brandName", devBrandName);
			dict.put("totalNum", totalNum);
			dict.put("failNum", failNum);
			result.add(dict);
		}
		if (result.size() == 0) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		return TCResult.newSuccessResult(result);
	}

	/**
	 * @category 大屏监控获取网点设备信息（设备汇总）--目前大屏只支持总行展示（查询条件根据总行网点号获取分行或支行网点号，从而获取对应的设备总数）
	 * @throws BusException
	 */
	@InParams(param = { @Param(name = "queryBrno", comment = "待查询的机构ID-总行机构", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "分行、支行设备汇总数查询(返dict)", style = "选择型", type = "同步组件", comment = "地图展现设备运行状态", author = "AlphaLi", date = "2018-7-12 15:13:02")
	public static TCResult A_MonitorScreenBrnoDevInfo(String queryBrno) throws BusException {

		//获取数据库类型
		String dbTypeSQL = "select paramvalue from tp_cip_sysparameters where modulecode='AUMS' and paramkeyname='DBTYPE'";
		JavaList dbTypeList = (JavaList)P_Jdbc.dmlSelect(null, dbTypeSQL, -1).getOutputParams().get(1);
		String dbType = dbTypeList.getListItem(0).get(0).toString();
		
		//获取坐标汇总信息
		String queryCoordinateSQL = "select branchid,LONGITUDE,LATITUDE,AREANAME from AUMS_MONITOR_COORDINATEINFO";
		TCResult queryCoordinateResult = null;
		queryCoordinateResult = P_Jdbc.dmlSelect(null, queryCoordinateSQL, -1);
		if (queryCoordinateResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询经纬度信息异常");
		}
		if (queryCoordinateResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		JavaList tmpCoordinateList = (JavaList) queryCoordinateResult.getOutputParams().get(1);
		
		//根据父节点查询所有子节点机构号
		String queryBrnoInfoSQL = "select branchid from aums_branchinfo where fatherbranchid=(select branchid from aums_branchinfo where fatherbranchid='" + queryBrno + "')";
		TCResult queryResult = null;
		queryResult = P_Jdbc.dmlSelect(null, queryBrnoInfoSQL, -1);
		if (queryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询下级机构号信息异常");
		}
		if (queryResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		JavaList tmpList = (JavaList) queryResult.getOutputParams().get(1);
		JavaList result = new JavaList();
		for (int i = 0; i < tmpList.size(); i++) {
			//根据数据库类型获取机构子节点
			String tmpBrnoInfo = tmpList.getListItem(i).get(0).toString();
			AppLogger.info("大屏监控，需要展示的网点信息为：【"+tmpBrnoInfo+"】");
			String queryBrnoListSQL = "";
			if(dbType.equals("ORA")){
				//确定经纬度
				String longitude = "";//经度
				String latitude = "";//纬度
				String areaname = "";//地区名称
				for(int j=0;j<tmpCoordinateList.size();j++){
					if(tmpCoordinateList.getListItem(j).get(0).toString().equals(tmpBrnoInfo)){
						longitude=tmpCoordinateList.getListItem(j).get(1).toString();
						latitude=tmpCoordinateList.getListItem(j).get(2).toString();
						areaname=tmpCoordinateList.getListItem(j).get(3).toString();
						break;
					}
				}
				//获取机构号对应的子机构号
				queryBrnoListSQL = "select branchid from AUMS_BRANCHINFO start with branchid = '"+tmpBrnoInfo+"' connect by (prior branchid) = fatherbranchid";
				TCResult tmpQueryResult = null;
				tmpQueryResult = P_Jdbc.dmlSelect(null, queryBrnoListSQL, -1);
				JavaDict dict = new JavaDict();
				
				if (tmpQueryResult == null) {
					JavaList areaInfo = new JavaList();
					areaInfo.add(longitude);
					areaInfo.add(latitude);
					areaInfo.add("0");
					areaInfo.add("0");
					dict.put("name", areaname);
					dict.put("value", areaInfo);
					result.add(dict);
				}
				if (tmpQueryResult.getStatus() == 2) {
					JavaList areaInfo = new JavaList();
					areaInfo.add(longitude);
					areaInfo.add(latitude);
					areaInfo.add("0");
					areaInfo.add("0");
					dict.put("name", areaname);
					dict.put("value", areaInfo);
					result.add(dict);
				}
				//将branchno改为str
				JavaList tmpBrnoList = (JavaList) tmpQueryResult.getOutputParams().get(1);
				String allBrnoInfo = "";
				for(int k=0;k<tmpBrnoList.size();k++){
					allBrnoInfo =  allBrnoInfo + ",'" + tmpBrnoList.getListItem(k).get(0).toString() +"'";
				}
				allBrnoInfo = allBrnoInfo.substring(1);//去掉第一个,
				AppLogger.info("大屏监控，需要查询的子网点信息为：【"+allBrnoInfo+"】");
				//获取总设备数
				String totalDevSQL = "select count(*) from aums_dev_info where devbranchid in (" + allBrnoInfo + ")";
				TCResult tmpTotalResult = null;
				tmpTotalResult = P_Jdbc.dmlSelect(null, totalDevSQL, -1);
				JavaList totalDev = (JavaList) tmpTotalResult.getOutputParams().get(1);
				String totalDevNum = totalDev.getListItem(0).get(0).toString();
				//获取问题设备数
				String failDevSQL = "select count(*) from aums_dev_info A,aums_dev_status_info B where a.devid=b.devid and a.devbranchid in (" + allBrnoInfo + ") and b.runningstatus!='0'";
				TCResult tmpFailResult = null;
				tmpFailResult = P_Jdbc.dmlSelect(null, failDevSQL, -1);
				JavaList totalFailDev = (JavaList) tmpFailResult.getOutputParams().get(1);
				String totalFailDevNum = totalFailDev.getListItem(0).get(0).toString();
				//追加信息
				JavaList areaInfo = new JavaList();
				areaInfo.add(longitude);
				areaInfo.add(latitude);
				areaInfo.add(totalDevNum);
				areaInfo.add(totalFailDevNum);
				dict.put("name", areaname);
				dict.put("value", areaInfo);
				result.add(dict);
				
			}else if(dbType.equals("DB2")){
				queryBrnoListSQL="with temptab(branchid,branchname,branchno,fatherbranchid) as (select a.branchid,a.branchname,a.branchno,a.fatherbranchid from aums_branchinfo a where a.branchid = '" +tmpBrnoInfo+"' union all select sub.branchid,sub.branchname,sub.branchno,sub.fatherbranchid from aums_branchinfo sub, temptab super where sub.fatherbranchid = super.branchid ) select branchid,fatherbranchid,branchname,branchno from temptab";
				//确定经纬度
				String longitude = "";//经度
				String latitude = "";//纬度
				String areaname = "";//地区名称
				for(int j=0;j<tmpCoordinateList.size();j++){
					if(tmpCoordinateList.getListItem(j).get(0).toString().equals(tmpBrnoInfo)){
						longitude=tmpCoordinateList.getListItem(j).get(1).toString();
						latitude=tmpCoordinateList.getListItem(j).get(2).toString();
						areaname=tmpCoordinateList.getListItem(j).get(3).toString();
						break;
					}
				}
				//获取机构号对应的子机构号
				queryBrnoListSQL = "select branchid from AUMS_BRANCHINFO start with branchid = '"+tmpBrnoInfo+"' connect by (prior branchid) = fatherbranchid";
				TCResult tmpQueryResult = null;
				tmpQueryResult = P_Jdbc.dmlSelect(null, queryBrnoListSQL, -1);
				JavaDict dict = new JavaDict();
				
				if (tmpQueryResult == null) {
					JavaList areaInfo = new JavaList();
					areaInfo.add(longitude);
					areaInfo.add(latitude);
					areaInfo.add("0");
					areaInfo.add("0");
					dict.put("name", areaname);
					dict.put("value", areaInfo);
					result.add(dict);
				}
				if (tmpQueryResult.getStatus() == 2) {
					JavaList areaInfo = new JavaList();
					areaInfo.add(longitude);
					areaInfo.add(latitude);
					areaInfo.add("0");
					areaInfo.add("0");
					dict.put("name", areaname);
					dict.put("value", areaInfo);
					result.add(dict);
				}
				//将branchno改为str
				JavaList tmpBrnoList = (JavaList) tmpQueryResult.getOutputParams().get(1);
				String allBrnoInfo = "";
				for(int k=0;k<tmpBrnoList.size();k++){
					allBrnoInfo =  allBrnoInfo + "," + tmpBrnoList.getListItem(k).get(0).toString();
				}
				allBrnoInfo = allBrnoInfo.substring(1);//去掉第一个,
				//获取总设备数
				String totalDevSQL = "select count(*) from aums_dev_info where devbranchid in (" + allBrnoInfo + ")";
				TCResult tmpTotalResult = null;
				tmpTotalResult = P_Jdbc.dmlSelect(null, totalDevSQL, -1);
				JavaList totalDev = (JavaList) tmpTotalResult.getOutputParams().get(1);
				String totalDevNum = totalDev.getListItem(0).get(0).toString();
				//获取问题设备数
				String failDevSQL = "select count(*) from aums_dev_info A,aums_dev_status_info B where a.devid=b.devid and a.devbranchid in (" + allBrnoInfo + ") and b.runningstatus!='0'";
				TCResult tmpFailResult = null;
				tmpFailResult = P_Jdbc.dmlSelect(null, failDevSQL, -1);
				JavaList totalFailDev = (JavaList) tmpFailResult.getOutputParams().get(1);
				String totalFailDevNum = totalFailDev.getListItem(0).get(0).toString();
				//追加信息
				JavaList areaInfo = new JavaList();
				areaInfo.add(longitude);
				areaInfo.add(latitude);
				areaInfo.add(totalDevNum);
				areaInfo.add(totalFailDevNum);
				dict.put("name", areaname);
				dict.put("value", areaInfo);
				result.add(dict);
				
			}else if(dbType.equals("Mysql")){
				return new TCResult(2, ErrorCode.REMOTE, "暂不支持Mysql数据库类型,请联系技术人员");
			}else{
				return new TCResult(2, ErrorCode.REMOTE, "暂不支持"+dbType+"数据库类型,请联系技术人员");
			}
		}
		if (result.size() == 0) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		return TCResult.newSuccessResult(result);
	}
	
	
	/**
	 * @category 大屏监控获取交易类型信息（当日交易类型汇总）
	 * @throws BusException
	 */
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "当日交易类型汇总查询(返dict)", style = "选择型", type = "同步组件", comment = "查询当日交易类型汇总", author = "AlphaLi", date = "2018-7-12 15:13:02")
	public static TCResult A_MonitorScreenTradeTypeInfo() throws BusException {

		//获取工作日期
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String workdate = sdf.format(now);
		//交易类型查询SQL赋值
		String queryTradeTypeInfoSQL = "select distinct(TRADETYPE) from aums_sys_paymentbook where workdate='" + workdate +"'";
		
		TCResult queryResult = null;
		queryResult = P_Jdbc.dmlSelect(null, queryTradeTypeInfoSQL, -1);
		if (queryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询当日交易类型信息异常");
		}
		if (queryResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}

		JavaList loopList = (JavaList) queryResult.getOutputParams().get(1);
		JavaList result = new JavaList();
		for (int i = 0; i < loopList.size(); i++) {
			//交易类型
			String tradeType = loopList.getListItem(i).get(0).toString();
			
			//根据交易类型获取交易笔数（当天）
			String queryAllDev = "select count(*) totcalNum from aums_sys_paymentbook where workdate='" + workdate + "' and TRADETYPE='" + tradeType + "'";
			JavaList totalNumList = (JavaList)P_Jdbc.dmlSelect(null, queryAllDev, -1).getOutputParams().get(1);
			String totalNum = totalNumList.getListItem(0).get(0).toString();
			
			//转义交易类型
			String moduleName = "*";
			String transCode = "*";
			String paramKey = "TRADETYPE";
			String tradeTypeDesc = ParameterUtil.TransferKeyName(moduleName, transCode, paramKey, tradeType);
			
			JavaDict dict = new JavaDict();
			dict.put("tradeTypeDesc", tradeTypeDesc);
			dict.put("totalNum", totalNum);
			result.add(dict);
		}
		if (result.size() == 0) {
			return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
		}
		return TCResult.newSuccessResult(result);
	}
}
