package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Returns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tc.AUMS_V.Reso_Manang.Util.CreateFileUtil;
import tc.AUMS_V.Reso_Manang.Util.GroupFieldVo;
import tc.AUMS_V.Reso_Manang.Util.MenuGroupInfoVo;
import tc.AUMS_V.Reso_Manang.Util.MenuModelInfoVo;
import tc.AUMS_V.Reso_Manang.Util.MenuTradeInfoVo;
import tc.AUMS_V.Reso_Manang.Util.MenuTraderelVo;
import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 菜单JSON
 * 
 * @date 2018-06-26 16:32:2
 */
@ComponentGroup(level = "应用", groupName = "menuJSON", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_menuJson {

	//static String poolName = "qdzh_oracle";
	static boolean commitFlg = true;

	@InParams(param = { @galaxy.ide.tech.cpt.Param(name = "modelId", comment = "菜单模板ID", type = String.class) })
	@Returns(returns = { @galaxy.ide.tech.cpt.Return(id = "0", desp = "失败"),
			@galaxy.ide.tech.cpt.Return(id = "1", desp = "成功") })
	@Component(label = "写json文件", style = "判断型", type = "同步组件", comment = "菜单发布json", date = "2018-07-04 07:41:46")
	public static TCResult A_makeJsonFile(String modelId) throws Exception {
		MenuModelInfoVo menuModelInfoVo = new MenuModelInfoVo();
		P_Logger.info("-------------------json:  " + "JSON!!!");
		
		//
		String sql = "select * from AUMS_MENU_MODELINFO where MENU_MODEL_ID = '" + modelId + "'";
		P_Logger.info("****--P_Jdbc.dmlSelect(null, sqlD, -1): " + P_Jdbc.dmlSelect(null, sql, -1));
		P_Logger.info("****--P_Jdbc.dmlSelect(null, sqlD, -1).getOutputParams(): " + P_Jdbc.dmlSelect(null, sql, -1).getOutputParams());
		
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			P_Logger.info("********list:  " + list);
			String groupfields = null;
			for (int i = 0; i < list.size(); i++) {
				List list1 = (List) list.get(i);
				groupfields = (String) list1.get(13);
			}
			P_Logger.info("**************** groupfields  : " + groupfields);
	
			String sqlD = "select DEVID from AUMS_MENU_DEVMODELMAPPING where MODELID = '"+ modelId + "'";
			P_Logger.info("****SQLd: " + sqlD);
			P_Logger.info("****--P_Jdbc.dmlSelect(null, sqlD, -1): " + P_Jdbc.dmlSelect(null, sqlD, -1));
			P_Logger.info("****--P_Jdbc.dmlSelect(null, sqlD, -1).getOutputParams(): " + P_Jdbc.dmlSelect(null, sqlD, -1).getOutputParams());
			if(P_Jdbc.dmlSelect(null, sqlD, -1).getOutputParams() != null){
				List listD = (List) P_Jdbc.dmlSelect(null, sqlD, -1).getOutputParams().get(1);
				P_Logger.info("*****************listD: " + listD);
				P_Logger.info("***********listD.size: " + listD.size());
				
				String[] devids = new String[listD.size()];
				for (int i = 0; i < listD.size(); i++) {
					List list1 = (List) listD.get(i);
					for (int j = 0; j < list1.size(); j++) {
						devids[j] = (String) list1.get(j);
					}
				}
				if (devids == null) {
					P_Logger.info("该菜单模版未绑定设备。");
				}
				
				Map<String, MenuTradeInfoVo> menuMap = queryAllMenuMenuTradeInfo();
				Map<String, MenuGroupInfoVo> menuGroupMap = queryAllMenuGroupInfo();
				Map<String, MenuTraderelVo> menuTraderelMap = queryAllTraderelInfo();
				Map<String, GroupFieldVo> menuGroupFieldMap = queryAllGroupFieldInfo();
		
				P_Logger.info("栏位:" + groupfields);
				String[] groupFeilds = groupfields.split(",");
				JSONArray totalArr = new JSONArray();
				for (int i = 0; i < groupFeilds.length; i++) {
					JSONArray fieldArr = new JSONArray();
					String groupFeild = groupFeilds[i].toString();
					GroupFieldVo groupFieldVo = new GroupFieldVo();
					groupFieldVo = (GroupFieldVo) menuGroupFieldMap.get(groupFeild);
					String gids = groupFieldVo.getGROUP_IDS();
					String[] gidArr = gids.split(",");
					for (int j = 0; j < gidArr.length; j++) {
						MenuGroupInfoVo menuGroupInfoVo = (MenuGroupInfoVo) menuGroupMap
								.get(gidArr[j].toString());
						JSONObject groupObje = new JSONObject();
						String[] groupLayout = new String[2];
						groupLayout[0] = (String)menuGroupInfoVo.getLAYOUTROW();
						groupLayout[0] = (String)menuGroupInfoVo.getLAYOUTCOL();
						//groupObje.put("layoutRow", menuGroupInfoVo.getLAYOUTROW());
						//groupObje.put("layoutCol", menuGroupInfoVo.getLAYOUTCOL());
						groupObje.put("layout", groupLayout);
						groupObje.put("categoryName", menuGroupInfoVo.getCATEGORYNAME());
						groupObje.put("themeColor", menuGroupInfoVo.getTHEMECOLOR());
						JSONArray itemsArr = new JSONArray();
						String gid = gidArr[j];
						MenuTraderelVo menuTraderelVo = (MenuTraderelVo) menuTraderelMap
								.get(gid);
						String menus = menuTraderelVo.getMENUARR();
						String[] menuArr = menus.split(",");
						for (int k = 0; k < menuArr.length; k++) {
							JSONObject parentObj = new JSONObject();
							MenuTradeInfoVo menuTradeInfoVo = (MenuTradeInfoVo) menuMap
									.get(menuArr[k].toString());
							parentObj.put("tradeName",
									(String) menuTradeInfoVo.getTRADENAME());
							parentObj.put("icon", (String) menuTradeInfoVo.getICON());
							parentObj.put("bg", (String) menuTradeInfoVo.getBG());
							parentObj.put("size",
									(String) menuTradeInfoVo.getMENUSIZE());
							parentObj.put("isEnabled",
									(String) menuTradeInfoVo.getISENABLED());
							parentObj.put("activeClass",
									(String) menuTradeInfoVo.getACTIVECLASS());
							parentObj.put("navigationMode",
									(String) menuTradeInfoVo.getNAVIGATIONMODE());
							parentObj.put("tadPath",
									(String) menuTradeInfoVo.getTADPATH());
							parentObj.put("code",
									(String) menuTradeInfoVo.getTRADECODE());
							String ishaschild = (String) menuTradeInfoVo
									.getISHASCHILD();
							if ("0".equals(ishaschild)) {
								String children = menuTradeInfoVo.getCHILDRENARR();
								P_Logger.info("children:" + children);
								String[] childrenArr = children.split(",");
		
								JSONArray childrenJSONArrBig = new JSONArray();
								JSONArray childrenJSONArr = new JSONArray();
								if ((childrenArr != null) && (childrenArr.length > 0)) {
									for (int m = 0; m < childrenArr.length; m++) {
										JSONObject childrenObj = new JSONObject();
										String childGid = childrenArr[m].toString();
										MenuGroupInfoVo menuGroupInfoChildVo = (MenuGroupInfoVo) menuGroupMap
												.get(childGid);
										String layout1row = (String) menuGroupInfoVo
												.getLAYOUTROW();
										String layout1col = (String) menuGroupInfoVo
												.getLAYOUTCOL();
										childrenObj.put("layoutRow",
												(String) menuGroupInfoChildVo
														.getLAYOUTROW());
										childrenObj.put("layoutCol",
												(String) menuGroupInfoChildVo
														.getLAYOUTCOL());
										childrenObj.put("categoryName",
												(String) menuGroupInfoChildVo
														.getCATEGORYNAME());
										childrenObj.put("themeColor",
												(String) menuGroupInfoChildVo
														.getTHEMECOLOR());
										MenuTraderelVo menuTraderelVo2 = (MenuTraderelVo) menuTraderelMap
												.get(childGid);
										String[] menusArr = ((String) menuTraderelVo2
												.getMENUARR()).split(",");
										JSONArray itemsArr2 = new JSONArray();
										for (int n = 0; n < menusArr.length; n++) {
											String menuChild = menusArr[n].toString();
											MenuTradeInfoVo menuChildTradeVo = (MenuTradeInfoVo) menuMap
													.get(menuChild);
											JSONObject childrenMenuObj = new JSONObject();
											childrenMenuObj.put("tradeName",
													(String) menuChildTradeVo
															.getTRADENAME());
											childrenMenuObj
													.put("icon",
															(String) menuChildTradeVo
																	.getICON());
											childrenMenuObj.put("bg",
													(String) menuChildTradeVo.getBG());
											childrenMenuObj.put("size",
													(String) menuChildTradeVo
															.getMENUSIZE());
											childrenMenuObj.put("isEnabled",
													(String) menuChildTradeVo
															.getISENABLED());
											childrenMenuObj.put("navigationMode",
													(String) menuChildTradeVo
															.getNAVIGATIONMODE());
											childrenMenuObj.put("tadPath",
													(String) menuChildTradeVo
															.getTADPATH());
											childrenMenuObj.put("code",
													(String) menuChildTradeVo
															.getTRADECODE());
											itemsArr2.add(childrenMenuObj);
										}
										childrenObj.put("items", itemsArr2);
										childrenJSONArr.add(childrenObj);
									}
									childrenJSONArrBig.add(childrenJSONArr);
									parentObj.put("children", childrenJSONArrBig);
								}
							}
							itemsArr.add(parentObj);
							groupObje.put("items", itemsArr);
						}
						fieldArr.add(groupObje);
					}
					totalArr.add(fieldArr);
				}
				P_Logger.info("生成的json字符串" + totalArr);
				String jsonString = totalArr.toString();
		
				// String menuUploadPath = "http://192.9.200.225:8023/Menu/json/";
				String menuUploadPath = "/home/afa4sj/AFA4J_2.7.1_1207/share/Menu/json/";
				/*
				 * Map<String, String> pathMap = getUploadPath("00");
				 * 
				 * String menuUploadPath = (String)pathMap.get("jsonUpload"); String
				 * menuAccessPath = (String)pathMap.get("jsonAccess");
				 * P_Logger.info("menuUploadPath:" + menuUploadPath);
				 * P_Logger.info("menuAccessPath:" + menuAccessPath);
				 */
				String devidStr = "";
				String path = "";
				// String path2 = "";
				for (int i = 0; i < devids.length; i++) {
					String devid = devids[i];
					path = menuUploadPath + "MENU_" + devid + "/";// /home/weblogic/resource/Menu/json/MENU_" + devid + "/"
		
					// path2 = "D:\\tr\\MENU_" + devid + "\\";
					boolean flag = true;
					flag = CreateFileUtil.makeJsonFile(jsonString, path,
							"tile-menu-data");
					if (!flag) {
						devidStr = devidStr + devid + ",";
					}
				}
				
				menuModelInfoVo.setFilepath(menuUploadPath);
				menuModelInfoVo.setIsupload("0");
			}
				
		}
			
		return TCResult.newSuccessResult(new Object[0]);
	}

	public static Map<String, String> getUploadPath(String flag) {
		Map<String, String> pathMap = new HashMap();
		Map<String, String> nginxmap = getNginxPath();
		String nginxPath = (String) nginxmap.get("nginxPath");// /home/weblogic/resource
		String accessPath = (String) nginxmap.get("accessPath");// http://32.114.73.114:8023
		if (flag.equals("00")) {
			String selectimagePath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='PCVA' AND tpi.TRANSCODE='advertise' AND tpi.PARAMKEYNAME='menuimage'";
			String selectjsonPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='PCVA' AND tpi.TRANSCODE='advertise' AND tpi.PARAMKEYNAME='menujson'";
			List<?> imagePathList = null;
			List<?> jsonPathList = null;
			String imagePath = "";
			String jsonPath = "";
			imagePathList = (List) P_Jdbc.dmlSelect(null, selectimagePath, -1).getOutputParams().get(1);
			jsonPathList = (List) P_Jdbc.dmlSelect(null, selectjsonPath, -1).getOutputParams().get(1);
			P_Logger.info("***********************imagePathList:  "
					+ imagePathList);
			P_Logger.info("***********************jsonPathList:  "
					+ jsonPathList);
			if (imagePathList != null) {
				// imagePath = imagePathList.get(0).toString();
				List list = (List) imagePathList.get(0);
				imagePath = list.get(0).toString();

				pathMap.put("imageUpload", nginxPath + imagePath);
				pathMap.put("imageAccess", accessPath + imagePath);
			}
			if (jsonPathList != null) {
				// jsonPath = jsonPathList.get(0).toString();
				List list = (List) jsonPathList.get(0);
				jsonPath = list.get(0).toString();

				pathMap.put("jsonUpload", nginxPath + jsonPath);// /home/weblogic/resource
																// + /Menu/json/
				pathMap.put("jsonAccess", accessPath + jsonPath);// http://32.114.73.114:8023
																	// +
																	// /Menu/json/
			}
		}
		return pathMap;
	}

	public static Map<String, String> getNginxPath() {
		Map<String, String> nginxmap = new HashMap();
		String selectNginxPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='PCVA' AND tpi.TRANSCODE='v_nginx' AND tpi.PARAMKEYNAME='rootDir'";
		String selectAccessPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='PCVA' AND tpi.TRANSCODE='v_nginx' AND tpi.PARAMKEYNAME='accesspath'";
		List<?> nginxPathList = null;
		List<?> accessPathList = null;
		String nginxPath = "";
		String accessPath = "";
		nginxPathList = (List) P_Jdbc.dmlSelect(null, selectNginxPath, -1).getOutputParams().get(1);
		accessPathList = (List) P_Jdbc.dmlSelect(null, selectAccessPath, -1).getOutputParams().get(1);
		if (nginxPathList != null) {
			// nginxPath = nginxPathList.get(0).toString();
			List list = (List) nginxPathList.get(0);
			nginxPath = list.get(0).toString();

			nginxmap.put("nginxPath", nginxPath);// /home/weblogic/resource
		}
		if (accessPathList != null) {
			// accessPath = accessPathList.get(0).toString();
			List list = (List) accessPathList.get(0);
			accessPath = list.get(0).toString();

			nginxmap.put("accessPath", accessPath);// http://32.114.73.114:8023
		}
		return nginxmap;
	}

	private static Map<String, GroupFieldVo> queryAllGroupFieldInfo()
			throws Exception {
		Map<String, GroupFieldVo> map = new HashMap();
		List<GroupFieldVo> list = new ArrayList();
		String sql = "select * from AUMS_MENU_GROUPFIELD";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			list = (List<GroupFieldVo>) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				GroupFieldVo vo = new GroupFieldVo();
				List list1 = (List) list.get(i);
				vo.setGFD((String) list1.get(0));
				vo.setGROUPSERINO((String) list1.get(1));
				vo.setGROUP_IDS((String) list1.get(2));
				vo.setREMARK1((String) list1.get(3));
				vo.setREMARK2((String) list1.get(4));
				vo.setREMARK3((String) list1.get(5));
				String gfd = vo.getGFD();
				map.put(gfd, vo);
			}
		}
		return map;
	}

	public static Map<String, MenuTraderelVo> queryAllTraderelInfo()
			throws Exception {
		Map<String, MenuTraderelVo> map = new HashMap();
		String sql = "select * from AUMS_MENU_TRADEREL";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
			
			for (int i = 0; i < list.size(); i++) {
				MenuTraderelVo vo = new MenuTraderelVo();
				List list1 = (List) list.get(i);
				vo.setMENUTRADERELID((String) list1.get(0));
				vo.setGROUP_ID((String) list1.get(1));
				vo.setMENUARR((String) list1.get(2));
				vo.setREMARK1((String) list1.get(3));
				vo.setREMARK2((String) list1.get(4));
				vo.setREMARK3((String) list1.get(5));
				String gid = vo.getGROUP_ID();
				map.put(gid, vo);
			}
		}
		return map;
	}

	private static Map<String, MenuGroupInfoVo> queryAllMenuGroupInfo()
			throws Exception {
		Map<String, MenuGroupInfoVo> map = new HashMap();
		String sql = "select * from AUMS_MENU_GROUPINFO";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				MenuGroupInfoVo vo = new MenuGroupInfoVo();
				List list1 = (List) list.get(i);
				vo.setGROUP_ID((String) list1.get(0));
				vo.setCATEGORYNAME((String) list1.get(0));
				vo.setTHEMECOLOR((String) list1.get(1));
				vo.setLAYOUTROW((String) list1.get(2));
				vo.setLAYOUTCOL((String) list1.get(3));
				vo.setREMARK1((String) list1.get(4));
				vo.setREMARK2((String) list1.get(5));
				vo.setREMARK3((String) list1.get(6));
				map.put(vo.getGROUP_ID(), vo);
			}
		}
		return map;
	}

	private static Map<String, MenuTradeInfoVo> queryAllMenuMenuTradeInfo()
			throws Exception {
		Map<String, MenuTradeInfoVo> map = new HashMap();
		String sql = "select * from AUMS_MENU_TRADEINFO";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if ((list == null) || (list.size() == 0)) {
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				MenuTradeInfoVo vo = new MenuTradeInfoVo();
				List list1 = (List) list.get(i);
				vo.setMENU_ID((String) list1.get(0));
				vo.setTRADENAME((String) list1.get(1));
				vo.setICON((String) list1.get(2));
				vo.setBG((String) list1.get(3));
				vo.setMENUSIZE((String) list1.get(4));
				vo.setISENABLED((String) list1.get(5));
				vo.setACTIVECLASS((String) list1.get(6));
				vo.setNAVIGATIONMODE((String) list1.get(7));
				vo.setTADPATH((String) list1.get(8));
				vo.setTRADECODE((String) list1.get(9));
				vo.setISHASCHILD((String) list1.get(10));
				vo.setCHILDRENARR((String) list1.get(11));
				vo.setCATEGORYNAME((String) list1.get(12));
				vo.setTHEMECOLOR((String) list1.get(13));
				vo.setLAYOUT((String) list1.get(14));
				vo.setREMARK1((String) list1.get(15));
				vo.setREMARK2((String) list1.get(16));
				vo.setREMARK3((String) list1.get(17));
	
				map.put(vo.getMENU_ID(), vo);
			}
		}
		return map;
	}

	private static String[] queryDevidsByModelId(String modelId) {
		String sql = "select DEVID from AUMS_MENU_DEVMODELMAPPING where MODELID like nvl('"
				+ modelId + "','%%')";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
			List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			String[] devids = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				List list1 = (List) list.get(i);
				for (int j = 0; j < list1.size(); j++) {
					devids[j] = (String) list1.get(j);
				}
			}
		return devids;
		}else{
			return null;
		}
	}
}
