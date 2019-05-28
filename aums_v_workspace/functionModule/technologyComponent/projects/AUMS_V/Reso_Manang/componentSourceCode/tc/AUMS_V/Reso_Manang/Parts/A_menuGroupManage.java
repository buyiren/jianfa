package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tc.AUMS_V.Reso_Manang.Util.MenuGroupInfoVo;
import tc.AUMS_V.Reso_Manang.Util.MenuTraderelVo;
import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 菜单管理
 * 
 * @date 2018-07-01 16:32:24
 */
@ComponentGroup(level = "应用", groupName = "menuGroupManage", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_menuGroupManage {
	//static String poolName = "qdzh_oracle";
	static boolean commitFlg = true;

	@OutParams(param = { @galaxy.ide.tech.cpt.Param(name = "resArr", comment = "菜单组信息", type = JSONArray.class) })
	@Returns(returns = { @galaxy.ide.tech.cpt.Return(id = "1", desp = "成功") })
	@Component(label = "查询菜单组信息", style = "处理型", type = "同步组件", date = "2018-07-05 05:39:52")
	public static TCResult A_queryMenuGroupInfo() {
		JSONArray resArr = new JSONArray();
		List<?> list = null;
		String sql = "select * from AUMS_MENU_GROUPINFO";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1) != null){
				list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams()
						.get(1);
				Map<String, MenuTraderelVo> map = new HashMap();
				try {
					List<?> list1 = null;
					String sql1 = "select * from AUMS_MENU_TRADEREL";
					if(P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams() != null){
						list1 = (List) P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams().get(1);
						if ((list1 == null) || (list1.size() == 0)) {
							return null;
						}
						for (int i = 0; i < list1.size(); i++) {
							List<String> listI1 = (List) list1.get(i);
							MenuTraderelVo vo = new MenuTraderelVo();
							vo.setMENUTRADERELID((String) listI1.get(0));
							vo.setGROUP_ID((String) listI1.get(1));
							vo.setMENUARR((String) listI1.get(2));
							vo.setREMARK1((String) listI1.get(3));
							vo.setREMARK2((String) listI1.get(4));
							vo.setREMARK3((String) listI1.get(5));
							String gid = vo.getGROUP_ID();
							map.put(gid, vo);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 0; i < list.size(); i++) {
					Map map1 = new HashMap();
					List<String> listI = (List) list.get(i);
					MenuGroupInfoVo vo = new MenuGroupInfoVo();
					vo.setGROUP_ID((String) listI.get(0));
					vo.setCATEGORYNAME((String) listI.get(1));
					vo.setTHEMECOLOR((String) listI.get(2));
					vo.setLAYOUTROW((String) listI.get(3));
					vo.setLAYOUTCOL((String) listI.get(4));
					vo.setREMARK1((String) listI.get(5));
					vo.setREMARK2((String) listI.get(6));
					vo.setREMARK3((String) listI.get(7));
					P_Logger.info("vo.getGROUP_ID():" + vo.getGROUP_ID());
					MenuTraderelVo vo2 = (MenuTraderelVo) map.get(vo.getGROUP_ID());
					map1.put("gid", vo.getGROUP_ID().toString());
					map1.put("categoryName", vo.getCATEGORYNAME().toString());
					map1.put("themeColor", vo.getTHEMECOLOR().toString());
					String s1 = vo.getLAYOUTROW().toString();
					String s2 = vo.getLAYOUTCOL().toString();
					int a = Integer.parseInt(s1);
					int b = Integer.parseInt(s2);
					int[] iI = { a, b };
					map1.put("layout", iI);
					List listS = new ArrayList();
					JSONObject jo = null;
					List listTrade = null;
					if (vo2.getMENUARR().toString() != null) {
						String[] menuArr = vo2.getMENUARR().split(",");
						listTrade = menuItemNameByIds(menuArr);
						List listT = (List) listTrade.get(1);
						for (int j = 0; j < listT.size(); j++) {
							jo = new JSONObject();
							List listT1 = (List) listT.get(j);
							jo.put("tradeName", listT1.get(0));
							jo.put("icon", listT1.get(1));
							jo.put("bg", listT1.get(2));
							jo.put("menuSize", listT1.get(3));
							jo.put("isEnabled", listT1.get(4));
							jo.put("activeClass", listT1.get(5));
							jo.put("navigationMode", listT1.get(6));
							jo.put("tadPath", listT1.get(7));
							jo.put("code", listT1.get(8));
							jo.put("menu_Id", listT1.get(9));
							listS.add(jo);
						}
					}
					map1.put("items", listS);
					resArr.add(map1);
				}
			}
		}	
				
		return TCResult.newSuccessResult(new Object[] { resArr });
	}

	public static List<?> menuItemNameByIds(String[] menuArr) {
		List<?> list = null;
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < menuArr.length; i++) {
			if (i == 0) {
				str.append("'" + menuArr[i] + "'");
			} else {
				str.append(",'" + menuArr[i] + "'");
			}
		}
		String sql = "select TRADENAME tradeName,ICON icon,BG bg,MENUSIZE menuSize,ISENABLED isEnabled,ACTIVECLASS activeClass,NAVIGATIONMODE navigationMode,TADPATH tadPath,TRADECODE code,MENU_ID menu_Id from AUMS_MENU_TRADEINFO  where MENU_ID in ("
				+
				str.toString() + ")";
		try {
			if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
				list = P_Jdbc.dmlSelect(null, sql, -1).getOutputParams();
			}	
		} catch (Exception e) {
			P_Logger.info("数据库查询异常！");
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, MenuTraderelVo> queryAllTraderelInfo()
			throws Exception {
		Map<String, MenuTraderelVo> map = new HashMap();
		List<MenuTraderelVo> list = new ArrayList<MenuTraderelVo>();
		String sql = "select * from AUMS_MENU_TRADEREL";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
			
			for (int i = 0; i < list.size(); i++) {
				MenuTraderelVo menuTraderelVo = (MenuTraderelVo) list.get(i);
				String gid = menuTraderelVo.getGROUP_ID();
				map.put(gid, menuTraderelVo);
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, MenuGroupInfoVo> queryAllMenuGroupInfo()
			throws Exception {
		Map<String, MenuGroupInfoVo> map = new HashMap();
		List<MenuGroupInfoVo> list = new ArrayList();
		String sql = "select * from AUMS_MENU_GROUPINFO";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				MenuGroupInfoVo vo = (MenuGroupInfoVo) list.get(i);
				map.put(vo.getGROUP_ID(), vo);
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<MenuGroupInfoVo> queryAllMenuGroupListInfo()
			throws Exception {
		List<MenuGroupInfoVo> list = null;
		String sql = "select * from AUMS_MENU_GROUPINFO";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			P_Logger.info("********menugroupinfo方法里的size:  " + list.size()
					+ "7777777777777777777777777");
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
		}
		return list;
	}

	/**
	 * @category 发布结果查询（DB2舍弃不用）
	 * @param rn
	 *            入参|起始行|{@link int}
	 * @param rownum
	 *            入参|终止行|{@link int}
	 * @param branId
	 *            入参|机构ID|{@link java.lang.String}
	 * @param tableData
	 *            出参|查询数据|{@link com.alibaba.fastjson.JSONObject}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "rn", comment = "起始行", type = int.class),
			@Param(name = "rownum", comment = "终止行", type = int.class),
			@Param(name = "branId", comment = "机构ID", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "tableData", comment = "查询数据", type = com.alibaba.fastjson.JSONObject.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "发布结果查询", style = "处理型", type = "同步组件", date = "2018-07-30 05:19:24")
	public static TCResult A_ReleaseResultQuery(int rn, int rownum,
			String branId) {
		JSONArray tableDataSJ = new JSONArray();
		String sql1 = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( select DEVNUM,DEVBRNONAME,DQDHNAME,TEMPIDNAME,CREATEUSER,CREATEDATE||CREATETIME from AUMS_AD_TEMP_TO_DEV ttd left join AUMS_DEV_INFO di on ttd.DEVID = di.DEVID "
				+ ") A WHERE ROWNUM <= nvl('"
				+ rownum
				+ "','10') ) WHERE RN >= nvl('" + rn + "','1')";
		String sql2 = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( select DEVNUM,DEVBRNONAME,DQDHNAME,TEMPIDNAME,CREATEUSER,CREATEDATE||CREATETIME from AUMS_AD_TEMP_TO_DEV ttd left join AUMS_DEV_INFO di on ttd.DEVID = di.DEVID "
				+ "where DEVBRNO in ( select BRANCHNO from AUMS_BRANCHINFO where BRANCHID in "
				+ "(select regexp_substr('"
				+ branId
				+ "','[^,]+', 1, level) from dual connect by regexp_substr('"
				+ branId
				+ "', '[^,]+', 1, level) is not null ))) A WHERE ROWNUM <= nvl('"
				+ rownum + "','10') ) " + "WHERE RN >= nvl('" + rn + "','1')";
		String sql1C = "SELECT count(*) FROM (SELECT A.*, ROWNUM RN FROM ( select DEVNUM,DEVBRNONAME,DQDHNAME,TEMPIDNAME,CREATEUSER,CREATETIME from AUMS_AD_TEMP_TO_DEV ttd left join AUMS_DEV_INFO di on ttd.DEVID = di.DEVID "
				+ ") A)";
		String sql2C = "SELECT count(*) FROM (SELECT A.*, ROWNUM RN FROM ( select DEVNUM,DEVBRNONAME,DQDHNAME,TEMPIDNAME,CREATEUSER,CREATETIME from AUMS_AD_TEMP_TO_DEV ttd left join AUMS_DEV_INFO di on ttd.DEVID = di.DEVID "
				+ "where DEVBRNO in ( select BRANCHNO from AUMS_BRANCHINFO where BRANCHID in "
				+ "(select regexp_substr('"
				+ branId
				+ "','[^,]+', 1, level) from dual connect by regexp_substr('"
				+ branId + "', '[^,]+', 1, level) is not null ))) A";
		List list = null;
		List listC = null;
		if (branId.trim().length() == 0) {
			list = (List) P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams()
					.get(1);
			listC = (List) P_Jdbc.dmlSelect(null, sql1C, -1).getOutputParams()
					.get(1);
		} else if (branId.trim().length() != 0) {
			list = (List) P_Jdbc.dmlSelect(null, sql2, -1).getOutputParams()
					.get(1);
			listC = (List) P_Jdbc.dmlSelect(null, sql2C, -1).getOutputParams()
					.get(1);
		}
		P_Logger.info("*********************** 查询结果 list:  " + list);
		for (int i = 0; i < list.size(); i++) {
			JSONObject jo = new JSONObject();
			List list1 = (List) list.get(i);
			jo.put("devNum", (String) list1.get(0));
			jo.put("devBrnoName", (String) list1.get(1));
			jo.put("dqdhName", (String) list1.get(2));
			jo.put("tempIdName", (String) list1.get(3));
			jo.put("creatUser", (String) list1.get(4));
			jo.put("creatTime", (String) list1.get(5));
			tableDataSJ.add(jo);
		}
		List listCN1 = (List) listC.get(0);
		String total = listCN1.get(0).toString();
		P_Logger.info("*********************总条数：" + total);
		int totali = Integer.parseInt(total);
		P_Logger.info("*********************查询数据：" + tableDataSJ);
		JSONObject tableData = new JSONObject();
		tableData.put("total", totali);
		tableData.put("result", tableDataSJ);
		return TCResult.newSuccessResult(tableData);
	}

	/**
	 * @category 菜单组删除
	 * @param group_Id
	 *            入参|菜单组ID|{@link java.lang.String}
	 * @param ja
	 *            出参|删除结果|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "group_Id", comment = "菜单组ID", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "ja", comment = "删除结果", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "菜单组删除", style = "处理型", type = "同步组件", date = "2018-08-23 04:14:34")
	public static TCResult A_MenuGroupDelete(String group_Id) {
		JSONArray ja = new JSONArray();
		
		//菜单组ID，value对应map
		String gnaSql = "select GROUP_ID,CATEGORYNAME from AUMS_MENU_GROUPINFO";
		if(P_Jdbc.dmlSelect(null, gnaSql, -1).getOutputParams() != null){
			List gnaList = (List) P_Jdbc.dmlSelect(null, gnaSql, -1).getOutputParams().get(1);
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < gnaList.size(); i++) {
				List gnaList1 = (List) gnaList.get(i);
				P_Logger.info("***********gnaList1: " + gnaList1);
				String id = gnaList1.get(0).toString();
				String value = gnaList1.get(1).toString();
				map.put(id, value);
			}
			P_Logger.info("************map:" + map);
					
			String groupFieldSql = "select distinct GROUP_IDS from AUMS_MENU_GROUPFIELD";
			if(P_Jdbc.dmlSelect(null, groupFieldSql, -1).getOutputParams() != null){
				List gidslist = (List) P_Jdbc.dmlSelect(null, groupFieldSql, -1).getOutputParams().get(1);
				Set<String> set = new HashSet<String>();
				CharSequence cs = ",";
				for (int i = 0; i < gidslist.size(); i++) {
					List gidlist = (List) gidslist.get(i);
					String gid = (String) gidlist.get(0);
					boolean b = gid.contains(cs);
					if (b == true) {
						String[] str = gid.split(",");
						for (String s : str) {
							set.add(s);
						}
					} else if (b == false) {
						set.add(gid);
					}
				}
				P_Logger.info("******************gidSet:  " + set);
				StringBuffer su = new StringBuffer();
				for (String ss : set) {
					su.append(ss).append(",");
				}
				String sss = su.toString();
				sss = sss.substring(0, sss.length() - 1);
				P_Logger.info("**********************str: " + sss);
				StringBuffer su2 = new StringBuffer();
				if (group_Id.length() != 0) {
					String[] split = ((String) (group_Id)).split(",");
					for (String str : split) {
						su2.append(str);
						su2.append("','");
					}
				}
				String temp2 = "'" + su2.substring(0, su2.length() - 2);
				P_Logger.info("********temp2(group_Id): " + temp2);
				StringBuffer su1 = new StringBuffer();
				if (sss.length() != 0) {
					String[] split = ((String) (sss)).split(",");
					for (String str : split) {
						su1.append(str);
						su1.append("','");
					}
				}
				String temp = "'" + su1.substring(0, su1.length() - 2);
				P_Logger.info("********temp: " + temp);
				String delInfoSql = "delete from AUMS_MENU_GROUPINFO where GROUP_ID in ("
						+ temp2 + ") and GROUP_ID not in (" + temp + ")";
				String delRelSql = "delete from AUMS_MENU_TRADEREL where GROUP_ID in ("
						+ temp2 + ") and GROUP_ID not in (" + temp + ")";
				P_Jdbc.executeSQL(null, delInfoSql, commitFlg);
				P_Jdbc.executeSQL(null, delRelSql, commitFlg);
				
				String[] gids = group_Id.split(",");
				for (String str : gids) {
					P_Logger.info(" ******gids:  " + str);
				}
				P_Logger.info(" ******gids[]:  " + gids);
		
				//删除结果信息
				for (int i = 0; i < gids.length; i++) {
					JSONObject jo = new JSONObject();
					if (set.contains(gids[i])) {
						jo.put("alertMsg", "【" + map.get(gids[i]) + "】在使用中，无法删除！");
					} else {
						jo.put("alertMsg", "【" + map.get(gids[i]) + "】已删除！");
					}
					ja.add(jo);
				}
			}
				
		}
			
		return TCResult.newSuccessResult(ja);
	}

	/**
	 * @category 查询菜单模板详细信息
	 * @param result
	 *            入参|模板信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param tableData
	 *            出参|查询结果集|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "result", comment = "模板信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "tableData", comment = "查询结果集", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "查询菜单模板详细信息", style = "处理型", type = "同步组件", date = "2018-08-24 07:29:22")
	public static TCResult A_queryModelInfo(JavaList result) {
		P_Logger.info("******************** size: " + result.size());
		if(result.size() != 0){
			JSONArray tableData = new JSONArray();
			P_Logger.info("******************** size: " + result.size());
			for (int i = 0; i < result.size(); i++) {
				JSONObject tableData1 = new JSONObject();
				P_Logger.info("****************result.get(i) ： " + result.get(i));
				P_Logger.info("****************result.get(i).getclass ： " + result.get(i).getClass());
				JavaDict jd = (JavaDict) result.get(i);
				P_Logger.info("****************jd : " + jd);
				P_Logger.info("****************jd.get(menu_Model_Id) : " + jd.get("menu_Model_Id"));
				tableData1.put("menu_Model_Id", jd.get("menu_Model_Id").toString());
				tableData1.put("modelName", jd.get("model_Name").toString());
				tableData1.put("isUpload", jd.get("isUpload").toString());
				tableData1.put("create_User", jd.get("create_User").toString());
				tableData1.put("create_Time", jd.get("create_Time").toString());
				String groupFields = jd.get("groupFields").toString();
				P_Logger.info("********************groupFields :  " + groupFields);
				String[] gf = groupFields.split(",");
				P_Logger.info("****************  gf.length:   " + gf.length);
				JSONArray resArr = new JSONArray();
				for (int j = 0; j < gf.length; j++) {
					JSONArray ja = new JSONArray();
					String gfd = gf[j];
					String sqlgfd = "select GROUP_IDS from AUMS_MENU_GROUPFIELD where GFD = '"
							+ gfd + "'";
					if(P_Jdbc.dmlSelect(null, sqlgfd, -1).getOutputParams() != null){
						List listgfd = (List) P_Jdbc.dmlSelect(null, sqlgfd, -1).getOutputParams().get(1);
						List listgfd1 = (List) listgfd.get(0);
						String gids = (String) listgfd1.get(0);
						String[] gidss = gids.split(",");
						P_Logger.info("**********************gidss.length  :  "
								+ gidss.length);
						for (int k = 0; k < gidss.length; k++) {
							String gid = gidss[k];
							List<?> list = null;
							String sql = "select * from AUMS_MENU_GROUPINFO where GROUP_ID = '"
									+ gid + "'";
							if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
								list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
								Map<String, MenuTraderelVo> map = new HashMap();
								try {
									List<?> list1 = null;
									String sql1 = "select * from AUMS_MENU_TRADEREL";
									if(P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams() != null){
										list1 = (List) P_Jdbc.dmlSelect(null, sql1, -1).getOutputParams().get(1);
										if ((list1 == null) || (list1.size() == 0)) {
											return null;
										}
										for (int i1 = 0; i1 < list1.size(); i1++) {
											List<String> listI1 = (List) list1.get(i1);
											MenuTraderelVo vo = new MenuTraderelVo();
											vo.setMENUTRADERELID((String) listI1.get(0));
											vo.setGROUP_ID((String) listI1.get(1));
											vo.setMENUARR((String) listI1.get(2));
											vo.setREMARK1((String) listI1.get(3));
											vo.setREMARK2((String) listI1.get(4));
											vo.setREMARK3((String) listI1.get(5));
											String gidt = vo.getGROUP_ID();
											map.put(gidt, vo);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								Map map1 = null;
								for (int i1 = 0; i1 < list.size(); i1++) {
									map1 = new HashMap();
									List<String> listI = (List) list.get(i1);
									MenuGroupInfoVo vo = new MenuGroupInfoVo();
									vo.setGROUP_ID((String) listI.get(0));
									vo.setCATEGORYNAME((String) listI.get(1));
									vo.setTHEMECOLOR((String) listI.get(2));
									vo.setLAYOUTROW((String) listI.get(3));
									vo.setLAYOUTCOL((String) listI.get(4));
									vo.setREMARK1((String) listI.get(5));
									vo.setREMARK2((String) listI.get(6));
									vo.setREMARK3((String) listI.get(7));
									P_Logger.info("******************vo.getGROUP_ID():"
											+ vo.getGROUP_ID());
									MenuTraderelVo vo2 = (MenuTraderelVo) map.get(vo
											.getGROUP_ID());
									map1.put("gid", vo.getGROUP_ID().toString());
									map1.put("categoryName", vo.getCATEGORYNAME()
											.toString());
									map1.put("themeColor", vo.getTHEMECOLOR().toString());
									String s1 = vo.getLAYOUTROW().toString();
									String s2 = vo.getLAYOUTCOL().toString();
									int a = Integer.parseInt(s1);
									int b = Integer.parseInt(s2);
									int[] iI = { a, b };
									map1.put("layout", iI);
									List listS = new ArrayList();
									JSONObject jo = null;
									List listTrade = null;
									if (vo2.getMENUARR().toString() != null) {
										String[] menuArr = vo2.getMENUARR().split(",");
										listTrade = menuItemNameByIds(menuArr);
										List listT = (List) listTrade.get(1);
										for (int j1 = 0; j1 < listT.size(); j1++) {
											jo = new JSONObject();
											List listT1 = (List) listT.get(j1);
											jo.put("tradeName", listT1.get(0));
											jo.put("icon", listT1.get(1));
											jo.put("bg", listT1.get(2));
											jo.put("menuSize", listT1.get(3));
											jo.put("isEnabled", listT1.get(4));
											jo.put("activeClass", listT1.get(5));
											jo.put("navigationMode", listT1.get(6));
											jo.put("tadPath", listT1.get(7));
											jo.put("code", listT1.get(8));
											jo.put("menu_Id", listT1.get(9));
											listS.add(jo);
										}
									}
									map1.put("items", listS);
								}
								ja.add(map1);
							}
						}
					}
					resArr.add(ja);
					tableData1.put("groupInfo", resArr);
					
				}
				tableData.add(tableData1);
			}
			return TCResult.newSuccessResult(new Object[] { tableData });
		}else{
			return null;
		}
	}

	/**
	 * @category 新增菜单模板
	 * @param group_Ids
	 *            入参|菜单组信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param modelName
	 *            入参|菜单模板名称|{@link java.lang.String}
	 * @param userId
	 *            入参|用户名|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "group_Ids", comment = "菜单组信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "modelName", comment = "菜单模板名称", type = java.lang.String.class),
			@Param(name = "userId", comment = "用户名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "新增菜单模板", style = "处理型", type = "同步组件", author = "BJ", date = "2018-08-07 03:23:22")
	public static TCResult A_insertModelInfo(JavaList group_Ids,
			String modelName, String userId) {
		String sqlLgfd = "select GFD from AUMS_MENU_GROUPFIELD where GROUPSERINO = (select max(to_number(GROUPSERINO)) from AUMS_MENU_GROUPFIELD)";
		StringBuffer su = new StringBuffer();
		for (int i = 0; i < group_Ids.size(); i++) {
			String strGROUP_IDS = (String) group_Ids.get(i);
			String sqlGroupField = "insert into AUMS_MENU_GROUPFIELD (GFD, GROUPSERINO, GROUP_IDS) values ((select 'GFD'||(nvl(max(to_number(substr(GFD,4,length(GFD)))),0)+1) from"
					+ " AUMS_MENU_GROUPFIELD),(select nvl(max(to_number(GROUPSERINO)),0)+1 from AUMS_MENU_GROUPFIELD),'"
					+ strGROUP_IDS + "')";
			P_Jdbc.executeSQL(null, sqlGroupField, commitFlg);
			List list = (List) P_Jdbc.dmlSelect(null, sqlLgfd, 0)
					.getOutputParams().get(1);
			List list1 = (List) list.get(0);
			String gfd = (String) list1.get(0);
			su.append(gfd).append(",");
		}
		String gfds = su.toString();
		gfds = gfds.substring(0, gfds.length() - 1);
		P_Logger.info("*********gfds: " + gfds);
		String sqlModel = "insert into AUMS_MENU_MODELINFO (MENU_MODEL_ID, MODEL_NAME, CREATE_USER, CREATE_TIME, GROUPFIELDS,ISUPLOAD) values "
				+ "((select nvl(max(to_number(MENU_MODEL_ID)),0)+1 from AUMS_MENU_MODELINFO),'"
				+ modelName
				+ "','"
				+ userId
				+ "',"
				+ "(select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual),'"
				+ gfds + "','0')";
		P_Jdbc.executeSQL(null, sqlModel, commitFlg);

		return TCResult.newSuccessResult();
	}

	/**
	 * @category 菜单发布
	 * @param modelId
	 *            入参|模板ID|{@link java.lang.String}
	 * @param userId
	 *            入参|创建用户|{@link java.lang.String}
	 * @param devIds
	 *            入参|设备|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "modelId", comment = "模板ID", type = java.lang.String.class),
			@Param(name = "userId", comment = "创建用户", type = java.lang.String.class),
			@Param(name = "devIds", comment = "设备", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "菜单发布", style = "处理型", type = "同步组件", author = "BHD", date = "2018-08-22 10:55:10")
	public static TCResult A_menuRealease(String modelId, String userId,
			String devIds) {
		P_Logger.info("发布菜单：");
		String sql = "insert into AUMS_MENU_DEVMODELMAPPING (DEVTYPE, MODELID, CREATE_USER, CREATE_TIME, BRNO, DEVID, DEVIP, MODELTYPE)"
				+ "select nvl(DEVTYPE,'defaultType'),'"
				+ modelId
				+ "','"
				+ userId
				+ "',(select to_char(sysdate,'yyyymmddhh24miss') from dual),BRANCHNO,DEVID,nvl(DEVIP,'192.168.0.1'),'0' from "
				+ "(select DEVID,DEVASSORTMENTID,DEVBRANCHID,BRANCHNO,DEVIP,t1.DEVMODELID,DEVTYPENUM from    (select DEVID,DEVASSORTMENTID,DEVBRANCHID,BRANCHNO,DEVIP,DEVMODELID from "
				+ "(select DEVID,DEVASSORTMENTID,DEVBRANCHID,BRANCHNO,DEVIP from AUMS_DEV_INFO di left join AUMS_BRANCHINFO b on di.DEVBRANCHID = b.BRANCHID) t left join AUMS_DEV_ASSORTMENT da "
				+ "on t.DEVASSORTMENTID = da.ASSORTMENTID) t1 left join AUMS_DEV_MODEL dm on t1.DEVMODELID = dm.DEVMODELID) t2 left join AUMS_DEV_TYPE dt on t2.DEVTYPENUM = dt.DEVTYPENUM "
				+ "where DEVID in  (" + devIds + ")";
		P_Jdbc.executeSQL(null, sql, commitFlg);

		return TCResult.newSuccessResult();
	}

	/**
	 * @category 菜单模板删除
	 * @param menu_Model_Id
	 *            入参|模板IDs|{@link java.lang.String}
	 * @param ja
	 *            出参|删除结果|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "menu_Model_Id", comment = "模板IDs", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "ja", comment = "删除结果", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "菜单模板删除", style = "处理型", type = "同步组件", author = "BHD", date = "2018-08-23 05:22:52")
	public static TCResult A_menuModelDel(String menu_Model_Id) {
		JSONArray ja = new JSONArray();
		Map<String, String> map = new HashMap<String, String>();
		String mSql = "select MENU_MODEL_ID,MODEL_NAME from AUMS_MENU_MODELINFO";
		
		if(P_Jdbc.dmlSelect(null, mSql, -1).getOutputParams() != null){
			List mList = (List) P_Jdbc.dmlSelect(null, mSql, -1).getOutputParams().get(1);
			P_Logger.info("***************mList" + mList);
			for (int i = 0; i < mList.size(); i++) {
				List list1 = (List) mList.get(i);
				String id = list1.get(0).toString();
				if(list1.get(1) != null){
					String value = list1.get(1).toString();
					map.put(id, value);
				}else if(list1.get(1) == null){
					String value = "";
					map.put(id, value);
				}
			}
			P_Logger.info("*******map: " + map);
	
			String fbSql = "select DISTINCT MODELID,MODEL_NAME from AUMS_MENU_DEVMODELMAPPING md left join AUMS_MENU_MODELINFO mm on md.MODELID = mm.MENU_MODEL_ID";
			if(P_Jdbc.dmlSelect(null, fbSql, -1).getOutputParams() != null){
				List list = (List) P_Jdbc.dmlSelect(null, fbSql, -1).getOutputParams().get(1);
				List mid = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					List list1 = (List) list.get(i);
					String id = list1.get(0).toString();
					String value = list1.get(1).toString();
					mid.add(id);
				}
				P_Logger.info("*******发布过的模板 ID  mid: " + mid);
		
				// 转发布过的modelID格式
				StringBuffer su1 = new StringBuffer();
				for (int i = 0; i < mid.size(); i++) {
					String str = mid.get(i).toString();
					su1.append(str).append("','");
				}
				String temp1 = "'" + su1.substring(0, su1.length() - 2);
				P_Logger.info("********发布过的mid转格式后的: " + temp1);
		
				// 转入参menu_Model_Id格式
				StringBuffer su = new StringBuffer();
				if (menu_Model_Id.length() != 0) {
					String[] split = ((String) (menu_Model_Id)).split(",");
					for (String str : split) {
						su.append(str);
						su.append("','");
					}
				}
				String temp = "'" + su.substring(0, su.length() - 2);
				P_Logger.info("********menu_Model_Id: " + temp);
		
		
				
				String[] mids = menu_Model_Id.split(",");
				P_Logger.info("***********mids: " + mids);
				
				// 删除语句
				String delSql = "delete from AUMS_MENU_MODELINFO where MENU_MODEL_ID in ("
								+ temp + ") and MENU_MODEL_ID not in(" + temp1 + ")";
				P_Jdbc.executeSQL(null, delSql, false);
			
			//删除结果弹窗信息
				for (int i = 0; i < mids.length; i++) {
					JSONObject jo = new JSONObject();
					if (mid.contains(mids[i])) {
						jo.put("alertMessage", "【" + map.get(mids[i]) + "】已经发布，不可删除！");
					} else {
						jo.put("alertMessage", "【" + map.get(mids[i]) + "】已删除！");
					}
					ja.add(jo);
				}
			}
		}
		
		
		return TCResult.newSuccessResult(ja);
	}

	/**
	 * @category 菜单项删除
	 * @param menu_Id
	 *            入参|菜单项编号s|{@link java.lang.String}
	 * @param ja
	 *            出参|删除结果信息|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "menu_Id", comment = "菜单项编号s", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "ja", comment = "删除结果信息", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "菜单项删除", style = "处理型", type = "同步组件", author = "BHD", date = "2018-07-24 10:30:02")
	public static TCResult A_menuItemDel(String menu_Id) {
		JSONArray ja = new JSONArray();
		
		//交易ID，value对应map
		String tSql = "select MENU_ID,TRADENAME FROM AUMS_MENU_TRADEINFO";
		
		List tList = (List) P_Jdbc.dmlSelect(null, tSql, -1).getOutputParams().get(1);
		P_Logger.info("*********tList :" + tList);
		Map<String, String> map = new HashMap<String,String>();
		for(int i=0; i<tList.size(); i++){
			List tList1 = (List) tList.get(i);
			P_Logger.info("*************** tList1:  " + tList1);
			String id = tList1.get(0).toString();
			String value = "";
			if(tList1.get(1) != null){
				value = tList1.get(1).toString();
			}
			map.put(id, value);
		}
		P_Logger.info("*********map: " + map);
		
		//查询添加过菜单组的菜单项set集合 
		String gSql = "select DISTINCT MENUARR from AUMS_MENU_TRADEREL";
		List gList = (List) P_Jdbc.dmlSelect(null, gSql, -1).getOutputParams().get(1);
		StringBuffer su = new StringBuffer();
		for(int i=0; i<gList.size(); i++){
			List gList2 = (List) gList.get(i);
			String strg = gList2.get(0).toString();
			su.append(strg).append(",");
		}
		P_Logger.info("**********所有mid su： " + su);
		String sug = su.toString();
		String[] strG = sug.split(",");
		Set<String> set = new HashSet<String>();
		for(int i=0;i<strG.length;i++){
			set.add(strG[i]);
		}
		P_Logger.info("************set: " + set);
		
		//转set格式（sql语句里）
		StringBuffer su2 = new StringBuffer();
		for(String str : set){
			su2.append(str).append(",");
		}
		String su2s = su2.toString();
		su2s = su2s.substring(0, su2s.length()-1);
		P_Logger.info("*********set转字符串1： su2s   ：" + su2s);
		StringBuffer su3 = new StringBuffer();
		if(su2s.length() != 0){      // MID14,MID15
			String[] split = ((String)(su2s)).split(",");
			for(String str:split){
				su3.append(str);
				su3.append("','");
			}
		}
		String temp3 = "'"+su3.substring(0,su3.length()-2);
		P_Logger.info("***********set转字符串2（sql里）  temp3: " + temp3);
		
		
		//转入参menu_Id格式（放sql语句里）
		StringBuffer su1 = new StringBuffer();
		String[] split = ((String)(menu_Id)).split(",");
		if(menu_Id.length() != 0){      // MID14,MID15
			for(String str:split){
				su1.append(str);
				su1.append("','");
			}
		}
		String temp1 = "'"+su1.substring(0,su1.length()-2);
		P_Logger.info("***********temp: " + temp1); //'MID14','MID15'
		
		String delSql = "delete from AUMS_MENU_TRADEINFO where MENU_ID in ("+temp1+") and MENU_ID not in ("+temp3+")";
		P_Jdbc.executeSQL(null, delSql, commitFlg);
		
		//删除结果信息
		for(int i=0; i<split.length; i++){
			JSONObject jo = new JSONObject();
			if(set.contains(split[i])){
				jo.put("alertMessage", "【" + map.get(split[i]) + "】 已添加菜单组，无法删除！");
			}else{
				jo.put("alertMessage", "【" + map.get(split[i]) + "】 已删除！");
			}
			ja.add(jo);
		}
		
		return TCResult.newSuccessResult(ja);
	}

}
