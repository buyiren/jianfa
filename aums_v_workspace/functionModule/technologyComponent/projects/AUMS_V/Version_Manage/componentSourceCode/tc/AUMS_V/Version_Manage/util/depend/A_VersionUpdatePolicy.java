package tc.AUMS_V.Version_Manage.util.depend;


import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 版本更新策略
 * 
 * @date 2018-01-11 20:45:30
 */

public class A_VersionUpdatePolicy {

	/**
	 * @category 初始化版本更新策略表格
	 */
	@SuppressWarnings("unchecked")
	@Component(label = "初始化版本更新策略表格", comment = "初始化版本更新策略表格", author = "zhipeng", date = "2017-10-09 09:49:29")
	public Object A_LoadVersionUpdatePolicy() {
		JSONArray tableData = new JSONArray();
		String versionUpdatePolicySql = "SELECT a.DEVID,a.VERSIONID,a.devbrno,a.CREATETIME,b.ISEFFECT,b.BEGINTIME,b.BATCHPERIOD,b.BATCHNUM from aums_ver_to_dev a,aums_ver_versionupdatepolicy b "
				+ "where a.UPDATEPOLICYID=b.UPDATEPOLICYID ORDER BY to_number(a.UPDATEPOLICYID) DESC";
		try {
			List<?> tardDetailList = (JavaList)P_Jdbc.dmlSelect(null, versionUpdatePolicySql.toString(), -1).getOutputParams().get(1);
			
			String selectVersionInfoSql = "select VERSIONID,VERSIONCODE,DESCRIPTION,CREATE_TIME,STRATEGY_ID,REMARK1,REMARK2 from aums_ver_info";
			List<VersioninfoVo> totalList = (List<VersioninfoVo>)  P_Jdbc.dmlSelect(null, selectVersionInfoSql, -1).getOutputParams().get(1);
			if (tardDetailList != null) {
				for (int i = 0; i < tardDetailList.size(); i++) {
					JSONArray rowData = new JSONArray();
					Object[] objArr = (Object[]) tardDetailList.get(i);
					rowData.add(objArr[0] == null ? "" : objArr[0]);
					String versioncode = GetVersion((objArr[1] == null ? ""
							: objArr[1]).toString(), totalList);
					rowData.add(versioncode);
					rowData.add(objArr[2] == null ? "" : objArr[2]);
					rowData.add(objArr[3] == null ? "" : objArr[3]);
					if (objArr[4].equals("0")) {
						rowData.add("否");
					} else {
						rowData.add("是");
					}
					if (objArr[5] == null) {
						rowData.add("");
					} else {
						 rowData.add(objArr[5].toString());
					}
					rowData.add(objArr[6] == null ? "" : objArr[6]);
					rowData.add(objArr[7] == null ? "" : objArr[7]);
					tableData.add(rowData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new TCResult(0, ErrorCode.REMOTE, "初始化版本更新策略时数据库异常！");
		}
		AppLogger.info("初始化版本更新策略表格返回数据:【" + tableData + "】");
		return tableData;
	}

	public static String GetVersion(String versionID,
			List<VersioninfoVo> totalList) {
		String versionCode = "";
		for (int i = 0; i < totalList.size(); i++) {
			if (versionID.equals(totalList.get(i))) {
				versionCode = totalList.get(i).getVERSIONCODE();
			}
		}
		return versionCode;
	}

	/**
	 * @category 初始化版本编号下拉框
	 */
	@Component(label = "初始化版本编号下拉框", comment = "初始化版本编号下拉框", author = "zhipeng", date = "2017-10-09 10:18:45")
	public Object A_LoadVersionID() {
		JSONArray data = new JSONArray();
		JSONObject obj1 = new JSONObject();
		obj1.put("desp", "--请选择--");
		obj1.put("value", "");
		data.add(obj1);
		try {
			String sql = "SELECT a.VERSIONID,a.VERSIONCODE FROM aums_ver_info a ";
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if (!list.isEmpty()) {
				for (int j = 0; j < list.size(); j++) {
					Object[] objArr = (Object[]) list.get(j);
					JSONObject obj = new JSONObject();
					obj.put("desp", objArr[1] == null ? "" : objArr[1]);
					obj.put("value", objArr[0] == null ? "" : objArr[0]);
					data.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new TCResult(0, ErrorCode.REMOTE, "初始化版本编号数据库异常！");
		}
		AppLogger.info("初始化版本编号下拉框返回数据：【" + data + "】");
		return data;
	}

//	/**
//	 * @category 初始化设备类型下拉框
//	 */
//	@Component(label = "初始化设备类型下拉框", comment = "初始化设备类型下拉框", author = "zhipeng", date = "2017-10-09 10:45:40")
//	public Object A_LoadDevType() {
//		String sql = "SELECT DISTINCT DEVTYPE FROM T_PCVA_DEVLXINFO";
//		JSONArray data = new JSONArray();
//		try {
//			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
//			if (list != null) {
//				JSONObject obj1 = new JSONObject();
//				obj1.put("desp", "--请选择--");
//				obj1.put("value", "");
//				data.add(obj1);
//				for (int i = 0; i < list.size(); i++) {
//					Object objArr = (Object) list.get(i);
//					JSONObject obj = new JSONObject();
//					obj.put("desp", objArr == null ? "" : objArr);
//					obj.put("value", objArr == null ? "" : objArr);
//					data.add(obj);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new TCResult(0, ErrorCode.REMOTE, "初始化设备类型下拉框数据库异常！");
//		}
//		AppAppLogger.info("初始化设备类型下拉框返回数据：【" + data + "】");
//		return data;
//	}

//	/**
//	 * @category 根据设备类型查找设备ID
//	 * @param devtype
//	 *            入参|设备类型| {@link String}
//	 */
//	@InParams(param = { @Param(name = "devtype", comment = "设备类型", dictId = "E7F3684137B2471B90F59D9B02E10BAB", type = String.class) })
//	@Component(label = "根据设备类型查找设备ID", comment = "根据设备类型查找设备ID", author = "zhipeng", date = "2017-10-09 03:09:57")
//	public Object A_SelectDevidByDevtype(String devtype) {
//		JSONArray brnoList = ComboxArraryUtil.brnoList();
//		Set<String> set = new HashSet<String>();
//		DefaultUser user = (DefaultUser) ASAPI.authenticator().getCurrentUser();
//		String user_brno = user.getUserVO().getUsr_brno();
//		set.add(user_brno);
//		List<String> brnoListAll = new ArrayList<String>();
//		List<BranchInfo> resultBranchInfo = new ArrayList<BranchInfo>();
//		List<BranchInfo> totalList = new ArrayList<BranchInfo>();
//		totalList = BankInfoUtil.queryBranchInfoAll();
//		BankInfoUtil.queryBranchInfoAllByBrnoList(set, brnoListAll,
//				resultBranchInfo, totalList);
//		brnoListAll.add(user_brno);
//		IDbSupport dbSupport = (IDbSupport) WebApplicationContextContainer
//				.getBean(IDbSupport.class);
//		JSONArray tableData = new JSONArray();
//		StringBuffer sql = new StringBuffer(
//				"SELECT DEVID,T3.DEVTYPE,DEVDESC,DEVPZBRNO FROM aums_ver_info T1,T_PCVA_DEVBRAND T2,T_PCVA_DEVLXINFO T3 WHERE T1.DEVPP = T2.DEVBRANDID AND T2.DEVTYPE = T3.DEVTYPEID");
//		if (StringUtil.isNotEmpty(devtype)&&devtype!=null) {
//			sql.append(" and T2.devtype = '" + devtype + "'");
//		}
//		try {
//			List<?> tardDetailList = dbSupport.executeSQLQuery(sql.toString(), null);
//			if (tardDetailList != null) {
//				for (int i = 0; i < tardDetailList.size(); i++) {
//					JSONArray rowData = new JSONArray();
//					Object[] objArr = (Object[]) tardDetailList.get(i);
//					if (objArr[3] == null) {
//						continue;
//					}
//					int l = 0;
//					for (int k = 0; k < brnoListAll.size(); k++) {
//						String objArr4 = brnoListAll.get(k);
//						if (objArr4.equals(objArr[3])) {
//							l = 1;
//							break;
//						}
//					}
//					if (l == 0) {
//						continue;
//					}
//					rowData.add(objArr[0] == null ? "" : objArr[0]);
//					rowData.add(objArr[1] == null ? "" : objArr[1]);
//					String brnoname = ComboxOrderUtil.comboxDesp(brnoList, objArr[3] == null ? "" : StringUtil.toString(objArr[3]));
//					rowData.add(brnoname);
//					rowData.add(objArr[2] == null ? "" : objArr[2]);
//					//rowData.add(objArr[3] == null ? "" : objArr[3]);
//					tableData.add(rowData);
//				}
//			}
//		} catch (DBSupportException e) {
//			e.printStackTrace();
//			throw AWebException.raise("初始化版本更新策略时数据库异常", e);
//		}
//		return tableData;
//	}

	/**
	 * @category 根据网点号查询设备ID
	 * @param brno
	 *            入参|机构| {@link String}
	 */
	@InParams(param = { @Param(name = "brno", comment = "机构", dictId = "F0931611C0FE47E9BCD9DC9A4989170E", type = String.class) })
	@Component(label = "根据网点号查询其下属设备", comment = "根据网点号查询其下属设备", author = "zhipeng", date = "2017-10-09 08:55:01")
	public Object A_SelectDevInfoListByBrno(String brno) {
		JSONArray tableData = new JSONArray();
//		JSONArray brnoList = ComboxArraryUtil.brnoList();
//		StringBuffer sql = new StringBuffer(
//				"SELECT DEVID,T3.DEVTYPE,DEVDESC,DQDH||DEVPZBRNO as DEVPZBRNO FROM aums_ver_info T1,T_PCVA_DEVBRAND T2,T_PCVA_DEVLXINFO T3 WHERE T1.DEVPP = T2.DEVBRANDID AND T2.DEVTYPE = T3.DEVTYPEID");
//		if (brno == null || brno == "") {
//			DefaultUser user = (DefaultUser) ASAPI.authenticator()
//					.getCurrentUser();
//			brno = user.getUserVO().getUsr_brno();
//		}
//		Set<String> set = new HashSet<String>();
//		set.add(brno);
//		List<String> brnoListAll = new ArrayList<String>();
//		List<BranchInfo> resultBranchInfo = new ArrayList<BranchInfo>();
//		List<BranchInfo> totalList = new ArrayList<BranchInfo>();
//		totalList = BankInfoUtil.queryBranchInfoAll();
//		BankInfoUtil.queryBranchInfoAllByBrnoList(set, brnoListAll,
//				resultBranchInfo, totalList);
//		brnoListAll.add(brno);
//		try {
//			List<?> tardDetailList = dbSupport.executeSQLQuery(sql.toString(), null);
//			if (tardDetailList != null) {
//				long startTime = Long.parseLong(DateUtils.getWindowsSysTimeSSS());
//				AppAppLogger.info("开始筛选结果集起始时间：" + startTime);
//				for (int i = 0; i < tardDetailList.size(); i++) {
//					JSONArray rowData = new JSONArray();
//					Object[] objArr = (Object[]) tardDetailList.get(i);
//					if (objArr[3] == null) {
//						continue;
//					}
//					int l = 0;
//					AppLogger.info("brnoListAll:"+brnoListAll);
//					for (int k = 0; k < brnoListAll.size(); k++) {
//						String objArr4 = brnoListAll.get(k);
//						if (objArr4.equals(objArr[3])) {
//							l = 1;
//							break;
//						}
//					}
//					if (l == 0) {
//						continue;
//					}
//					rowData.add(objArr[0] == null ? "" : objArr[0]);
//					rowData.add(objArr[1] == null ? "" : objArr[1]);
//					String brnoname = ComboxOrderUtil.comboxDesp(brnoList, objArr[3] == null ? "" : StringUtil.toString(objArr[3]));
//					rowData.add(brnoname);
//					rowData.add(objArr[2] == null ? "" : objArr[2]);
//					tableData.add(rowData);
//				}
//				long endTime = Long.parseLong(DateUtils.getWindowsSysTimeSSS());
//				AppLogger.info("开始筛选结果集结束时间：" + endTime);
//				AppLogger.info("共耗时：" + (endTime - startTime) + "毫秒");
//			}
//		} catch (DBSupportException e) {
//			e.printStackTrace();
//		}
		AppLogger.info("根据网点号查询其下属设备返回数据：【" + tableData + "】");
		return tableData;
	}

	/**
	 * @category 查询版本更新策略详细信息
	 * @param devid
	 *            入参|表格选中的数据| {@link java.lang.String}
	 */
	@InParams(param = { @Param(name = "devid", comment = "表格选中的数据", type = java.lang.String[].class) })
	@Component(label = "查询版本更新策略详细信息", comment = "查询版本更新策略详细信息", author = "zhipeng", date = "2017-10-12 02:45:24")
	public Object A_SelectDetalInfo(String[] devid) {
		String[] str = devid[0].split(",");
		JSONObject obj = new JSONObject();
		try {
			JSONArray data_0 = new JSONArray();
			JSONObject obj_1 = new JSONObject();
			String queryVerInfo = "select VERSIONCODE FROM aums_ver_info where VERSIONID = '" + str[1] + "'";
			List<?> list = (List<?>) P_Jdbc.dmlSelect(null, queryVerInfo, -1);
			String versioncode = list.get(0).toString();
			obj_1.put("desp", versioncode);
			obj_1.put("value", str[1]);
			data_0.add(obj_1);
			String queryVerInfoAll = "SELECT VERSIONID,VERSIONCODE FROM aums_ver_info";
			List<?> list1 = (List<?>) P_Jdbc.dmlSelect(null, queryVerInfoAll, -1);
			for (int i = 0; i < list1.size(); i++) {
				Object[] objArr = (Object[]) list1.get(i);
				JSONObject obj_0 = new JSONObject();
				if (!str[0].equals(objArr[0])) {
					obj_0.put("desp", objArr[1] == null ? "" : objArr[1]);
					obj_0.put("value", objArr[0] == null ? "" : objArr[0]);
					data_0.add(obj_0);
				}
			}
			JSONArray data_2 = new JSONArray();
			if (str[3].endsWith("0")) {
				JSONObject obj_3 = new JSONObject();
				obj_3.put("desp", "否");
				obj_3.put("value", "0");
				data_2.add(obj_3);
				JSONObject obj_4 = new JSONObject();
				obj_4.put("desp", "是");
				obj_4.put("value", "1");
				data_2.add(obj_4);
			} else {
				JSONObject obj_3 = new JSONObject();
				obj_3.put("desp", "是");
				obj_3.put("value", "1");
				data_2.add(obj_3);
				JSONObject obj_4 = new JSONObject();
				obj_4.put("desp", "否");
				obj_4.put("value", "0");
				data_2.add(obj_4);
			}
			String queryDevAdModeInfo = "SELECT devaddmode FROM aums_ver_to_dev where devid = '" + str[0] + "' and versionid = '" + str[1] + "'";
			List<?> devaddmodelist = (List<?>) P_Jdbc.dmlSelect(null, queryDevAdModeInfo, -1);
			String devaddmode = devaddmodelist.get(0).toString();
			obj.put("versionid", data_0);
			obj.put("iseffect", data_2);
			obj.put("devaddmode", devaddmode);
		} catch (Exception e) {
			
		}
		Date date = new Date(Long.parseLong(str[5]));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd Hh:mm:ss");
		String time = sdf.format(date);
		obj.put("startTime", time);
		obj.put("batchperiod", str[6]);
		obj.put("batchnum", str[7]);
		AppLogger.info("查询版本更新策略详细信息返回数据：【" + obj + "】");
		return obj;
	}
}
