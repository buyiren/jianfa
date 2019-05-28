package tc.AUMS_V.Warning_Manage.Warning_Manage.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * 预警策略查询
 * 
 * @date 2018-07-12 16:46:13
 */
@ComponentGroup(level = "应用", groupName = "WarningPolicyQuery", projectName = "AUMS_V", appName = "Warning_Manage")
public class A_WarningPolicyQuery {

	//static String poolName = "qdzh_oracle";
	static boolean commitFlg = true;

	/**
	 * @throws Exception
	 * @category 预警策略初始查询
	 * @param result
	 *            入参|预警策略结果集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param tableData
	 *            出参|预警策略表格|{@link com.alibaba.fastjson.JSONObject}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "result", comment = "预警策略结果集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "tableData", comment = "预警策略表格", type = com.alibaba.fastjson.JSONObject.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "预警策略初始查询", style = "处理型", type = "同步组件", date = "2018-08-24 09:21:53")
	public static TCResult A_warningQuery(JavaList result) throws Exception {
		List tableData = new ArrayList();
		
		String sql_01 = "select PARAMVALUE from tp_cip_sysparameters where MODULECODE = 'PCVA' and TRANSCODE='queryWarningPolicy' and PARAMKEYNAME like '%DeviceAlert%'";
		List<?> paramValueList = null;
		paramValueList = (List<?>) P_Jdbc.dmlSelect(null, sql_01, -1)
				.getOutputParams().get(1);
		P_Logger.info("********* PARAMVALUE :    " + paramValueList);
		String sql_ma = "select DISTINCT POLICYID from AUMS_WARN_POLICYDEVMAPPING";
		if(P_Jdbc.dmlSelect(null, sql_ma, -1).getOutputParams() != null){
			List listma = (List) P_Jdbc.dmlSelect(null, sql_ma, -1).getOutputParams().get(1);
			Set<String> set = new HashSet<String>();
			P_Logger.info("///////////////////listma--------------------: "
					+ listma);
			for (int j = 0; j < listma.size(); j++) {
				List listma1 = (List) listma.get(j);
				P_Logger.info("**************LISTMA1:   " + listma1);
				String polid = listma1.get(0).toString();
				P_Logger.info("**************polid:   " + polid);
				set.add(polid);
			}
			P_Logger.info("***********-----////*********policyId -- set: " + set);
			
			P_Logger.info("***************** result： " + result);
			if (!result.isEmpty()) {
				for (int i = 0; i < result.size(); i++) {
					JSONObject jo = new JSONObject();
					JavaDict jd = (JavaDict) result.get(i);
					P_Logger.info("**************jd: " + jd);
					String str1 = jd.get("policyId").toString();
					if (set.contains(str1)) {
						jo.put("isEnable", "1");
					} else {
						jo.put("isEnable", "0");
					}
					jo.put("policyId", str1);
					String umSql = "select UMID_A,UMID_B from AUMS_WARN_POLICYPEOPLEMAPPING where POLICYID = '"
							+ str1 + "'";
					if(P_Jdbc.dmlSelect(null, umSql, -1).getOutputParams() != null){
						List umList = (List) P_Jdbc.dmlSelect(null, umSql, -1).getOutputParams().get(1);
						List umList1 = (List) umList.get(0);
						StringBuffer su = new StringBuffer();
						P_Logger.info("******************* umList1: " + umList1);
						P_Logger.info("******************* umList1.size(): "
								+ umList1.size());
						P_Logger.info("******************* umList1.get(1): "
								+ umList1.get(1));
						if (umList1.get(1) == null) {
							su.append(umList1.get(0).toString());
						} else if (umList1.get(1) != null) {
							su.append(umList1.get(0).toString()).append(",")
									.append(umList1.get(1).toString());
						}
						String umidz = su.toString();
						jo.put("umId", umidz);
					}
					
					if ((jd.get("type").toString()).equals("DeviceAlert")) {
						jo.put("type", "设备");
					} else if ((jd.get("type").toString()).equals("BusinessAlert")) {
						jo.put("type", "业务");
					} else {
						jo.put("type", "未知");
					}
					String s = jd.get("policyProperty").toString();
					CharSequence cs = ",";
					boolean b = s.contains(cs);
					if (!paramValueList.isEmpty()) {
						JSONArray ja1 = new JSONArray();
						for (int j = 0; j < paramValueList.size(); j++) {
							List list1 = (List) paramValueList.get(j);
							String str = (String) list1.get(0);
							String[] keyValue = str.split("\\|");
							if (b == false) {
								if (s.equals(keyValue[0])) {
									JSONArray ja = new JSONArray();
									Map<String, String> map = new HashMap<String, String>();
									map.put("policyPropertyId", keyValue[0]);
									map.put("policyPropertyValue", keyValue[1]);
									ja.add(map);
									jo.put("policyProperty", ja);
									break;
								}
							} else if (b == true) {
								String ss[] = s.split(",");
								Map<String, String> map1 = new HashMap<String, String>();
								for (int k = 0; k < ss.length; k++) {
									String s2 = ss[k];
									if (s2.equals(keyValue[0])) {
										map1.put("policyPropertyId", keyValue[0]);
										map1.put("policyPropertyValue", keyValue[1]);
										P_Logger.info("*******111*********map1: "
												+ map1);
										ja1.add(map1);
										P_Logger.info("*******111*********ja1: "
												+ ja1);
										jo.put("policyProperty", ja1);
									}
								}
							}
						}
					}
					jo.put("policyFunction", jd.get("policyFunction").toString());
					jo.put("policyValue", jd.get("policyValue").toString());
					jo.put("notifyMethod", jd.get("notifyMethod").toString());
					jo.put("execute_StartTime", jd.get("execute_StartTime")
							.toString());
					jo.put("execute_EndTime", jd.get("execute_EndTime").toString());
					jo.put("count_StartTime", jd.get("count_StartTime").toString());
					jo.put("count_EndTime", jd.get("count_EndTime").toString());
					jo.put("working_Interval", jd.get("working_Interval")
							.toString());
					jo.put("create_User", jd.get("create_User").toString());
					jo.put("create_Time", jd.get("create_Time").toString());
					jo.put("update_User", jd.get("update_User").toString());
					jo.put("update_Time", jd.get("update_Time").toString());
					jo.put("remark1", jd.get("remark1").toString());
					jo.put("remark2", jd.get("remark2").toString());
					jo.put("remark3", jd.get("remark3").toString());
					P_Logger.info("***jo :   " + jo);
					tableData.add(jo);
				}
			}
		}
		P_Logger.info("***********预警策略结果集：  " + tableData);
		return TCResult.newSuccessResult(tableData);
	}

	/**
	 * @category 加载策略类型下拉框
	 * @param comBox
	 *            出参|策略类型下拉框返回数据|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "comBox", comment = "策略类型下拉框返回数据", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "加载策略类型下拉框", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-13 03:25:55")
	public static TCResult A_LoadPolicyTypecodeComBox() {

		JSONArray comBox = new JSONArray();
		String sql = "select PARAMVALUE from tp_cip_sysparameters where MODULECODE = 'PCVA' and TRANSCODE='queryWarningPolicy' and PARAMKEYNAME='POLICYTYPE'";
		try {
			List<?> statusList = (List<?>) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			String statusAll = statusList.get(0).toString();
			P_Logger.info("------------------------statusAll: " + statusAll);
			statusAll = statusAll.substring(2, statusAll.length() - 2);
			P_Logger.info("------------------------statusAll截取后   : "
					+ statusAll);
			String[] status = statusAll.split("#");
			P_Logger.info("------------------------status  :"
					+ Arrays.toString(status));
			for (int i = 0; i < status.length; i++) {
				JSONObject objLoop = new JSONObject();
				String[] keyValue = status[i].split("\\|");
				P_Logger.info("------------------------keyValue  :"
						+ Arrays.toString(keyValue));
				objLoop.put(keyValue[1], keyValue[0]);
				comBox.add(objLoop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		P_Logger.info("策略类型下拉框返回数据为" + comBox);

		return TCResult.newSuccessResult(comBox);
	}

	/**
	 * @category 根据策略类型加载策略属性下拉框
	 * @param policyType
	 *            入参|策略类型|{@link java.lang.String}
	 * @param comBox
	 *            出参|对应策略属性结果集|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "policyType", comment = "策略类型", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "comBox", comment = "对应策略属性结果集", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "根据策略类型加载策略属性下拉框", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-13 04:44:34")
	public static TCResult A_LoadPolicyPropertyComBox(String policyType) {

		JSONArray comBox = new JSONArray();

		String sql = "select PARAMVALUE from tp_cip_sysparameters where MODULECODE = 'PCVA' and TRANSCODE='queryWarningPolicy' and PARAMKEYNAME like '%"
				+ policyType + "%'";
		List<?> paramValueList;

		paramValueList = (List<?>) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
		P_Logger.info("------------------------paramValueList: "
				+ paramValueList);
		if (!paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				JSONObject objLoop = new JSONObject();
				List list = (List) paramValueList.get(i);
				String str = (String) list.get(0);
				P_Logger.info("*********************str :   " + str);
				String[] keyValue = str.split("\\|");
				// objLoop.put(keyValue[0], keyValue[1]);
				objLoop.put("key", keyValue[0]);
				objLoop.put("value", keyValue[1]);
				comBox.add(objLoop);
			}
		}

		P_Logger.info("返回的数据为" + comBox);

		return TCResult.newSuccessResult(comBox);
	}

	/**
	 * @category 表达式下拉框
	 * @param comBox
	 *            出参|表达式|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "comBox", comment = "表达式", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "表达式下拉框", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-13 05:41:30")
	public static TCResult A_LoadPolicyFunctionComBox() {
		JSONArray comBox = new JSONArray();
		String sql = "select PARAMVALUE from tp_cip_sysparameters where MODULECODE = 'PCVA' and TRANSCODE='queryWarningPolicy' and PARAMKEYNAME='policyFunction'";
		try {
			List<?> statusList = (List<?>) P_Jdbc.dmlSelect(null, sql, -1)
					.getOutputParams().get(1);
			String statusAll = statusList.get(0).toString();
			P_Logger.info("------------------------statusAll: " + statusAll);
			statusAll = statusAll.substring(2, statusAll.length() - 2);
			P_Logger.info("------------------------statusAll截取后   : "
					+ statusAll);
			String[] status = statusAll.split("#");
			P_Logger.info("------------------------status  :"
					+ Arrays.toString(status));
			for (int i = 0; i < status.length; i++) {
				JSONObject objLoop = new JSONObject();
				String[] keyValue = status[i].split("\\|");
				P_Logger.info("------------------------keyValue  :"
						+ Arrays.toString(keyValue));
				objLoop.put(keyValue[1], keyValue[0]);
				comBox.add(objLoop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		P_Logger.info("表达式下拉框返回数据为" + comBox);

		return TCResult.newSuccessResult(comBox);
	}

	/**
	 * @category 删除预警策略
	 * @param policyID
	 *            入参|策略ID|{@link java.lang.String}
	 * @param obj
	 *            出参|删除成功与否|{@link com.alibaba.fastjson.JSONObject}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "policyID", comment = "策略ID", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "obj", comment = "删除成功与否", type = com.alibaba.fastjson.JSONObject.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "删除预警策略", style = "处理型", type = "同步组件", date = "2018-07-14 06:31:01")
	public static TCResult A_DelWarningPolicy(String policyID) throws Exception {
		JSONObject obj = new JSONObject();
		String delPolicyDevsql = "delete from AUMS_WARN_POLICYDEVMAPPING where policyid = '"
				+ policyID + "'";
		String delWarningPolicysql = "delete from AUMS_WARN_POLICYINFO where policyid = '"
				+ policyID + "'";
		String delWarnPeoplesql = "delete from AUMS_WARN_POLICYPEOPLEMAPPING where POLICYID = '"+policyID+"'";
		String createTime = null;

		String sql = "select * from AUMS_WARN_POLICYINFO where POLICYID = "
				+ policyID;
		List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
		P_Logger.info("*****************AUMS_WARNINGPOLICY :   " + list);
		List list1 = (List) list.get(0);
		createTime = (String) list1.get(12);
		P_Logger.info("创建时间为" + createTime);
		if (createTime == null) {
			throw new Exception("创建时间查询失败");
		}

		boolean flag = JudgementDate(createTime);
		P_Logger.info("时间是否在24小时内" + flag);
		if (flag) {
			try {
				P_Jdbc.executeSQL(null, delWarningPolicysql, commitFlg);
				P_Jdbc.executeSQL(null, delPolicyDevsql, commitFlg);
				P_Jdbc.executeSQL(null, delWarnPeoplesql, commitFlg);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("删除策略失败，数据库异常！");
			}
			obj.put("resultFlag", true);
			obj.put("alertMsg", "删除预警策略成功！");
			return TCResult.newSuccessResult(obj);
		} else {
			obj.put("resultFlag", false);
			obj.put("alertMsg", "24小时内新建的策略不允许删除！");
			return TCResult.newSuccessResult(obj);
		}
	}

	public static boolean JudgementDate(String createTime) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date nowTime = new Date();
			Date createDate = sdf.parse(createTime);
			long time = nowTime.getTime() - createDate.getTime();
			if (time < 0) {
				return false;
			}
			double result = time * 1.0 / (1000 * 60 * 60);
			if (result > 24) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new Exception("时间格式化异常");
		}
	}

	/**
	 * @throws Exception
	 * @category 策略设备关联
	 * @param policyId
	 *            入参|策略ID|{@link java.lang.String}
	 * @param devId
	 *            入参|设备ID|{@link java.lang.String}
	 * @param userId
	 *            入参|创建人|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "policyId", comment = "策略ID", type = java.lang.String.class),
			@Param(name = "devId", comment = "设备ID", type = java.lang.String.class),
			@Param(name = "userId", comment = "创建人", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "策略设备关联", style = "处理型", type = "同步组件", date = "2018-07-19 02:11:31")
	public static TCResult A_PolicyDevMapping(String policyId, String devId,
			String userId) throws Exception {
		JSONObject obj = new JSONObject();

		String propertySql = "select POLICYPROPERTY from AUMS_WARN_POLICYINFO where POLICYID = '"
				+ policyId + "'";
		if(P_Jdbc.dmlSelect(null, propertySql, -1).getOutputParams() != null){
			List pp = (List) P_Jdbc.dmlSelect(null, propertySql, -1).getOutputParams().get(1);
			List pp1 = (List) pp.get(0);
			String policyProperty = (String) pp1.get(0);
	
			List<String> devidUsedList = new ArrayList<String>();
			String[] devIds = devId.split(",");
			for (int i = 0; i < devIds.length; i++) {
				String chargeDevSql = "select count(policyid) count from AUMS_WARN_POLICYDEVMAPPING where devid='"
						+ devIds[i] + "' and POLICYID = '" + policyId + "'";
				if(P_Jdbc.dmlSelect(null, chargeDevSql, -1).getOutputParams() != null){
					String count = null;
					List list = (List) P_Jdbc.dmlSelect(null, chargeDevSql, -1).getOutputParams().get(1);
					List list1 = (List) list.get(0);
					count = list1.get(0).toString();
					if (Integer.parseInt(count) > 0) {
						devidUsedList.add(devIds[i]);
						continue;
					}
		
					String devBrnoSql = "select BRANCHNO from (select di.DEVID,di.DEVBRANCHID,b.BRANCHNO from AUMS_DEV_INFO di left join AUMS_BRANCHINFO b "
							+ "on di.DEVBRANCHID = b.BRANCHID) t where DEVID = '"
							+ devIds[i] + "'";
					if(P_Jdbc.dmlSelect(null, devBrnoSql, -1).getOutputParams() != null){
						List db = (List) P_Jdbc.dmlSelect(null, devBrnoSql, -1).getOutputParams().get(1);
						List db1 = (List) db.get(0);
						String devBrno = (String) db1.get(0);
			
						String devTypeSql = "select DEVTYPE from (select DEVID,DEVASSORTMENTID,DEVMODELID,t1.DEVTYPENUM,DEVTYPE from "
								+ "(select DEVID,DEVASSORTMENTID,t.DEVMODELID,DEVTYPENUM from (select di.DEVID,di.DEVASSORTMENTID,da.DEVMODELID from AUMS_DEV_INFO di "
								+ "left join AUMS_DEV_ASSORTMENT da on di.DEVASSORTMENTID = da.ASSORTMENTID) t left join AUMS_DEV_MODEL dm on t.DEVMODELID = dm.DEVMODELID) t1 "
								+ "left join AUMS_DEV_TYPE dt on t1.DEVTYPENUM = dt.DEVTYPENUM) t2 where DEVID = '"
								+ devIds[i] + "'";
						if(P_Jdbc.dmlSelect(null, devTypeSql, -1).getOutputParams() != null){
							List dt = (List) P_Jdbc.dmlSelect(null, devTypeSql, -1).getOutputParams().get(1);
							List dt1 = (List) dt.get(0);
							String devType = (String) dt1.get(0);
				
							String policyDevSql = "insert into AUMS_WARN_POLICYDEVMAPPING(POLICYID,DEVID,POLICYPROPERTY,DEVBRNO,DEVTYPE,CREATETIME,CREATEUSER) values "
									+ "('"
									+ policyId
									+ "','"
									+ devIds[i]
									+ "','"
									+ policyProperty
									+ "','"
									+ devBrno
									+ "','"
									+ devType
									+ "',(select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual),'"
									+ userId + "')";
							try {
								P_Jdbc.executeSQL(null, policyDevSql, commitFlg);
							} catch (Exception e) {
								e.printStackTrace();
								throw new Exception("策略设备表维护失败，数据库操作异常！");
							}
						}
					}
				}
			}
			
			if (devidUsedList.size() == devIds.length) {
				obj.put("resultFlag", false);
				obj.put("alertMsg", "添加预警策略失败，这些机具的相应模块已有策略！");
			} else if (devidUsedList.size() == 0) {
				obj.put("resultFlag", true);
				obj.put("alertMsg", "添加预警策略成功！");
			} else {
				obj.put("resultFlag", true);
				obj.put("alertMsg", "添加预警策略成功！ID为" + devidUsedList + "的机具相应模块已有策略");
			}
		}
		
		return TCResult.newSuccessResult(obj);
	}

	/**
	 * @category 删除策略设备关系
	 * @param policyId
	 *            入参|策略ID|{@link java.lang.String}
	 * @param devid
	 *            入参|设备IDs|{@link java.lang.String}
	 * @return 1 成功<br/>
	 * @throws Exception
	 */
	@InParams(param = {
			@Param(name = "policyId", comment = "策略ID", type = java.lang.String.class),
			@Param(name = "devid", comment = "设备IDs", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "删除策略设备关系", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-17 05:39:51")
	public static TCResult A_DelPolicyDevMapping(String policyId, String devid)
			throws Exception {
		// JSONObject obj = new JSONObject();
		String[] deviceIds = devid.split(",");
		// List<String> devidUsedList = new ArrayList<String>();
		for (int i = 0; i < deviceIds.length; i++) {
			/*
			 * String chargeDevSql =
			 * "select count(policyid) count from AUMS_WARN_POLICYDEVMAPPING where devid='"
			 * + deviceIds[i] + "' and POLICYID = '" + policyId + "'"; String
			 * count = null; List list = (List) P_Jdbc.dmlSelect(poolName,
			 * chargeDevSql, 0).getOutputParams().get(1); List list1 = (List)
			 * list.get(0); count = list1.get(0).toString(); int a = (Integer)
			 * P_Jdbc.dmlSelect(poolName, chargeDevSql,
			 * 0).getOutputParams().get(1); count = Integer.toString(a);;
			 * P_Logger.info("*************** count:  " + count); if
			 * (Integer.parseInt(count) > 0) { devidUsedList.add(deviceIds[i]);
			 * continue; }
			 */
			String delPolicyDevSql = "delete from AUMS_WARN_POLICYDEVMAPPING where DEVID = '"
					+ deviceIds[i] + "' and POLICYID = '" + policyId + "'";
			P_Logger.info("*********************  delPolicyDevSql:   "
					+ delPolicyDevSql);
			try {
				P_Jdbc.executeSQL(null, delPolicyDevSql, commitFlg);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("策略设备表维护失败，数据库操作异常！");
			}
		}
		/*
		 * if (devidUsedList.size() == 0) { obj.put("resultFlag", true);
		 * obj.put("alertMsg", "删除设备成功！"); } else { obj.put("resultFlag", true);
		 * obj.put("alertMsg", "删除设备成功！ID为" + devidUsedList + "的机具相应模块没有策略"); }
		 */
		return TCResult.newSuccessResult();
	}

	/**
	 * @throws Exception
	 * @category 解绑设备机构查询
	 * @param policyId
	 *            入参|预警策略Id|{@link java.lang.String}
	 * @param rn
	 *            入参|起始行|int
	 * @param rownum
	 *            入参|终止行|int
	 * @param tableData
	 *            出参|查询信息|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	/*@InParams(param = {
			@Param(name = "policyId", comment = "预警策略Id", type = java.lang.String.class),
			@Param(name = "rn", comment = "起始行", type = int.class),
			@Param(name = "rownum", comment = "终止行", type = int.class) })
	@OutParams(param = { @Param(name = "tableData", comment = "查询信息", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "解绑设备机构查询", style = "处理型", type = "同步组件", date = "2018-08-01 03:15:44")
	public static TCResult A_WarningDevBranQuery(String policyId, int rn,
			int rownum) throws Exception {
		JSONObject tableData = new JSONObject();
		JSONArray tableData1 = new JSONArray();
		String sql1 = "select DEVID from AUMS_WARN_POLICYDEVMAPPING where POLICYID = '"
				+ policyId + "'";
		if(P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams() != null){
				List listD = (List) P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams().get(1);
				P_Logger.info("*********** listD :   " + listD);
				if(listD.size() != 0){
					for (int i = 0; i < listD.size(); i++) {
						String devId = null;
						List list3 = (List) listD.get(i);
						devId = list3.get(0).toString();
						P_Logger.info("********************devId:  " + devId);
						String sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (select DEVID,DEVNUM,DEVTYPE,DEVTYPEDESCRIBE,BRANCHNO,BRANCHNAME from (select DEVID,DEVNUM,DEVBRANCHID,DEVTYPE,DEVTYPEDESCRIBE from (select DEVID,DEVNUM,DEVBRANCHID,DEVASSORTMENTID,ASSORTMENTID,dm.DEVMODELID,dm.DEVTYPENUM from "
								+ "(select di.DEVID,di.DEVNUM,di.DEVBRANCHID,di.DEVASSORTMENTID,da.ASSORTMENTID,da.DEVMODELID from AUMS_DEV_INFO di left join AUMS_DEV_ASSORTMENT da on di.DEVASSORTMENTID = da.ASSORTMENTID) t left join AUMS_DEV_MODEL dm "
								+ "on t.DEVMODELID = dm.DEVMODELID) t2 left join AUMS_DEV_TYPE dt on t2.DEVTYPENUM = dt.DEVTYPENUM) t3 left join AUMS_BRANCHINFO b on t3.DEVBRANCHID = b.BRANCHID where DEVID in "
								+ "(select regexp_substr('"
								+ devId
								+ "','[^,]+', 1, level) from dual connect by regexp_substr('"
								+ devId
								+ "', '[^,]+', 1, level) is not null)"
								+ ") A WHERE ROWNUM <= nvl('"
								+ rownum
								+ "','10') ) WHERE RN >= nvl('" + rn + "','1')";
						try {
							List list = (List) P_Jdbc.dmlSelect(poolName, sql, 0)
									.getOutputParams().get(1);
							P_Logger.info("***************list:     " + list);
							for (int j = 0; j < list.size(); j++) {
								JSONObject jo = new JSONObject();
								List list1 = (List) list.get(j);
								P_Logger.info("******************list1:   " + list1);
								jo.put("devId", (String) list1.get(0));
								jo.put("devNum", (String) list1.get(1));
								jo.put("devType", (String) list1.get(2));
								jo.put("devDescribe", (String) list1.get(3));
								jo.put("branchNo", (String) list1.get(4));
								jo.put("branchName", (String) list1.get(5));
								tableData1.add(jo);
							}
						} catch (Exception e) {
							throw new Exception("查询设备信息错误，无此设备信息！");
						}
						tableData.put("result", tableData1);
					}
				}
		}
				
		// count 放外面
		String sqlC = "select count(*) from AUMS_WARN_POLICYDEVMAPPING where POLICYID = '"
				+ policyId + "'";
		List listC = (List) P_Jdbc.dmlSelect(poolName, sqlC, 0)
				.getOutputParams().get(1);
		List listCN = (List) listC.get(0);
		String count = listCN.get(0).toString();

		tableData.put("total", count);
		return TCResult.newSuccessResult(tableData);
	}*/

	/**
	 * @category 新增预警策略
	 * @param type
	 *            入参|策略类型|{@link java.lang.String}
	 * @param policyProperty
	 *            入参|策略属性|{@link java.lang.String}
	 * @param execute_Starttime
	 *            入参|异常排查开始时间|{@link java.lang.String}
	 * @param execute_Endtime
	 *            入参|异常排查结束时间|{@link java.lang.String}
	 * @param working_Interval
	 *            入参|时间间隔|{@link java.lang.String}
	 * @param notifyMethod
	 *            入参|通知方式|{@link java.lang.String}
	 * @param umId
	 *            入参|联系人|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param userId
	 *            入参|创建人|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "type", comment = "策略类型", type = java.lang.String.class),
			@Param(name = "policyProperty", comment = "策略属性", type = java.lang.String.class),
			@Param(name = "execute_Starttime", comment = "异常排查开始时间", type = java.lang.String.class),
			@Param(name = "execute_Endtime", comment = "异常排查结束时间", type = java.lang.String.class),
			@Param(name = "working_Interval", comment = "时间间隔", type = java.lang.String.class),
			@Param(name = "notifyMethod", comment = "通知方式", type = java.lang.String.class),
			@Param(name = "umId", comment = "联系人", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "userId", comment = "创建人", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "新增预警策略", style = "处理型", type = "同步组件", date = "2018-08-20 04:46:29")
	public static TCResult A_addWarningPolicy(String type,
			String policyProperty, String execute_Starttime,
			String execute_Endtime, String working_Interval,
			String notifyMethod, JavaList umId, String userId) {
		String sql = "insert into AUMS_WARN_POLICYINFO (POLICYID, TYPE, POLICYPROPERTY, NOTIFYMETHOD, EXECUTE_STARTTIME, EXECUTE_ENDTIME, WORKING_INTERVAL, CREATE_USER, CREATE_TIME) values "
				+ "((select nvl(max(to_number(POLICYID)),0)+1 from AUMS_WARN_POLICYINFO),'"
				+ type
				+ "','"
				+ policyProperty
				+ "','"
				+ notifyMethod
				+ "','"
				+ execute_Starttime
				+ "','"
				+ execute_Endtime
				+ "','"
				+ working_Interval
				+ "','"
				+ userId
				+ "',(select to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') from dual))";
		P_Jdbc.executeSQL(null, sql, commitFlg);
		String umId_A = "", umId_B = "";
		if (umId.size() == 1) {
			umId_A = (String) umId.get(0);
		} else if (umId.size() == 2) {
			umId_A = (String) umId.get(0);
			umId_B = (String) umId.get(1);
		}
		String umSql = "insert into AUMS_WARN_POLICYPEOPLEMAPPING (POLICYID, UMID_A, UMID_B) VALUES ((select nvl(max(to_number(POLICYID)),0) from AUMS_WARN_POLICYINFO),'"
				+ umId_A + "','"+umId_B+"')";
		P_Jdbc.executeSQL(null, umSql, commitFlg);
		
		return TCResult.newSuccessResult();
	}
	
	
	/**
	 * @category 修改预警策略
	 * @param type
	 *            入参|策略类型|{@link java.lang.String}
	 * @param policyProperty
	 *            入参|策略属性|{@link java.lang.String}
	 * @param execute_Starttime
	 *            入参|异常排查开始时间|{@link java.lang.String}
	 * @param execute_Endtime
	 *            入参|异常排查结束时间|{@link java.lang.String}
	 * @param working_Interval
	 *            入参|时间间隔|{@link java.lang.String}
	 * @param notifyMethod
	 *            入参|通知方式|{@link java.lang.String}
	 * @param umId
	 *            入参|联系人|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param userId
	 *            入参|创建人|{@link java.lang.String}
	 * @param policyId
	 *            入参|策略ID|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "type", comment = "策略类型", type = java.lang.String.class),
			@Param(name = "policyProperty", comment = "策略属性", type = java.lang.String.class),
			@Param(name = "execute_Starttime", comment = "异常排查开始时间", type = java.lang.String.class),
			@Param(name = "execute_Endtime", comment = "异常排查结束时间", type = java.lang.String.class),
			@Param(name = "working_Interval", comment = "时间间隔", type = java.lang.String.class),
			@Param(name = "notifyMethod", comment = "通知方式", type = java.lang.String.class),
			@Param(name = "umId", comment = "联系人", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "userId", comment = "创建人", type = java.lang.String.class),
			@Param(name = "policyId", comment = "策略ID", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "修改预警策略", style = "处理型", type = "同步组件", date = "2018-08-20 05:19:20")
	public static TCResult A_updateWarningPolicy(String type,
			String policyProperty, String execute_Starttime,
			String execute_Endtime, String working_Interval,
			String notifyMethod, JavaList umId, String userId, String policyId) {
		String sql = "update AUMS_WARN_POLICYINFO set UPDATE_TIME = (select to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') from dual),UPDATE_USER = '"+userId+"' ,TYPE = '"+type+"', POLICYPROPERTY = '"+policyProperty+"', "
				+ "NOTIFYMETHOD = '"+notifyMethod+"',EXECUTE_STARTTIME = '"+execute_Starttime+"', EXECUTE_ENDTIME = '"+execute_Endtime+"', WORKING_INTERVAL = '"+working_Interval+"' where POLICYID = '"+policyId+"'";
		P_Jdbc.executeSQL(null, sql, commitFlg);
		String umId_A = "", umId_B = "";
		P_Logger.info("******umId.size(): " + umId.size());
		if (umId.size() == 1) {
			umId_A = (String) umId.get(0);
		} else if (umId.size() == 2) {
			umId_A = (String) umId.get(0);
			umId_B = (String) umId.get(1);
		}
		String umSql = "update AUMS_WARN_POLICYPEOPLEMAPPING set UMID_A = '"+umId_A+"', UMID_B = '"+umId_B+"' where POLICYID = '"+policyId+"'";
		P_Jdbc.executeSQL(null, umSql, commitFlg);
		return TCResult.newSuccessResult();
	}
	

}
