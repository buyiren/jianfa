package tc.AUMS_V.Version_Manage.util.depend;


import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import redis.clients.jedis.Jedis;
import tc.bank.constant.BusException;
import tc.bank.constant.ErrorCodeModule;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @date 2018-01-11 20:45:15
 */


public class A_VersionService {

	/**
	 * 文件夹判断工具
	 * 
	 * @param Path
	 */
	public static void isHaveDirs(String Path) {
		File file = new File(Path);
		if (file.exists()) {
			if (file.isDirectory()) {
				AppLogger.info("文件夹存在");
			} else {
				AppLogger.info("同名文件夹存在");
			}
		} else {
			AppLogger.info("文件夹不存在 ，创建");
			file.mkdirs();
		}
	}

	/**
	 * @category 发布版本
	 * @param versionid
	 *            入参|关联版本号| {@link String}
	 * @param iseffect
	 *            入参|是否立即生效| {@link String}
	 * @param startTime
	 *            入参|开始时间| {@link String}
	 * @param batchperiod
	 *            入参|升级时间间隔| {@link String}
	 * @param batchnum
	 *            入参|升级机器数量| {@link String}
	 * @param devid
	 *            入参|机具ID| {@link java.lang.String}
	 * @param devaddmode
	 *            入参|添加方式| {@link String}
	 */
	@InParams(param = {
			@Param(name = "versionId", comment = "关联版本号", dictId = "229036DA83604821996CF618B840E0AD", type = String.class),
			@Param(name = "iseffect", comment = "是否立即生效", dictId = "1462E30BC7A54573A6F7775DA9E00D9F", type = String.class),
			@Param(name = "startTime", comment = "开始时间", dictId = "87034DCDEF76403B94FA9A73A0246C98", type = String.class),
			@Param(name = "batchperiod", comment = "升级时间间隔", dictId = "C8EC6C5F56AD45EF8710E13B0DBDA939", type = String.class),
			@Param(name = "batchnum", comment = "升级机器数量", dictId = "99BF5D853DD6410E9358CED2F405216E", type = String.class),
			@Param(name = "devid", comment = "机具ID", type = java.lang.String[].class),
			@Param(name = "devaddmode", comment = "添加方式", dictId = "7D93D908FE9945AFB2827848000F12CF", type = String.class),
			@Param(name = "brnoId", comment = "操作网点机构ID", dictId = "7D93D908FE9945AFB2827848000F12CF", type = String.class)})
	@Component(label = "发布版本", comment = "发布版本", date = "2017-10-09 10:47:19")
	public TCResult deployVersion(String versionId, String iseffect,
			String startTime, String batchperiod, String batchnum,
			String[] devid, String devaddmode,String brnoId) throws Exception {
		JSONObject resultObj = new JSONObject();
		Jedis jedis = RedisConnectionPool.getJedits();
		List<String> addDeviceIdList = new ArrayList<String>();
		List<String> updateDeviceIdList = new ArrayList<String>();
		List<String> wrongDeviceIdList = new ArrayList<String>();
		Map<String, String> wrongDeviceIdMap = new TreeMap<String, String>();
		List<String> illegalityDeviceIdList = new ArrayList<String>();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(now);
		VersionFilesList versionInfo = new VersionFilesList();
		A_OptionsVo optionsInfo = new A_OptionsVo();
		List<FilesDetails> fileDetailList = new ArrayList<FilesDetails>();
		String getPolicyId = "select AUMS_VER_VERSIONUPDATEPOLICY_SEQ.NEXTVAL FROM DUAL";
		List<?> policylist = (List<?>) P_Jdbc.dmlSelect(null, getPolicyId, -1);
		BigDecimal policyNum = (BigDecimal) policylist.get(0);
		String policyId = String.valueOf(policyNum.intValue());
		if (devid == null || devid.length == 0) {
			throw new BusException(ErrorCodeModule.IME999,"该机构没有设备");
		}
		
		//add lk 20180327 用于版本回退
		//获取操作网点唯一标识
		String brno = brnoId;
		//获取applydate
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String applydate = df.format(new Date());
		//设定版本操作类型
		String policytype = "0";
		//add lk 20180327 增加完成
		
		for (String device : devid) {
			StringBuffer checkDeviceSql = new StringBuffer(
					"select VERSIONID from AUMS_VER_TO_DEV TPVTD WHERE TPVTD.DEVID=");
			checkDeviceSql.append("'" + device + "'");
			checkDeviceSql.append("order by to_number(updatepolicyid) desc");
			String oldVersionId = null;
			List<?> versionList = (List<?>) P_Jdbc.dmlSelect(null, checkDeviceSql.toString(), -1);
			if (versionList.isEmpty()) {
				addDeviceIdList.add(device);
			} else {
				oldVersionId = (String) versionList.get(0);
				if (oldVersionId.equals(versionId)) {
					String checkDeviceStatusSql = "select devid,status from AUMS_VER_LOG where adid = '"
							+ oldVersionId + "' and devid='" + device + "'";
					List<?> versionStatusFaileIdList = (List<?>) P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
					if (versionStatusFaileIdList.isEmpty()) {
						wrongDeviceIdMap.put(device, device);
					} else {
						for (int i = 0; i < versionStatusFaileIdList.size(); i++) {
							Object[] loopInfo = (Object[]) versionStatusFaileIdList
									.get(i);
							if (loopInfo[1].toString().equals("4")) {
								updateDeviceIdList.add(loopInfo[0].toString());
							} else {
								wrongDeviceIdMap.put(loopInfo[0].toString(),
										loopInfo[0].toString());
							}
						}
					}
				} else {
					String queryCurrentVersionCode = "SELECT VERSIONCODE FROM aums_ver_info where VERSIONID = '"
							+ versionId + "'";
					String queryOldVerisonCode = "SELECT VERSIONCODE FROM aums_ver_info where VERSIONID = '"
							+ oldVersionId + "'";
					String CurrentVersionCode = String.valueOf((List<?>)P_Jdbc.dmlSelect(null, queryCurrentVersionCode, -1).getOutputParams().get(0));
					String OldVerisonCode = String.valueOf((List<?>)P_Jdbc.dmlSelect(null, queryOldVerisonCode, -1).getOutputParams().get(0));
					String versionIdVer = CurrentVersionCode.substring(2, 10)
							+ CurrentVersionCode.substring(11, 15);
					String oldVersionIdVer = OldVerisonCode.substring(2, 10)
							+ OldVerisonCode.substring(11, 15);
					if (Long.parseLong(versionIdVer) > Long
							.parseLong(oldVersionIdVer)) {
						String checkDeviceStatusSql = "select devid,status from t_Pcva_Verlog where adid = '"
								+ oldVersionId + "' and devid='" + device + "'";
						List<?> versionStatusFaileIdList = (List<?>) P_Jdbc.dmlSelect(null, checkDeviceStatusSql, -1);
						if (versionStatusFaileIdList.isEmpty()) {
							wrongDeviceIdMap.put(device, device);
						} else {
							for (int i = 0; i < versionStatusFaileIdList.size(); i++) {
								Object[] loopInfo = (Object[]) versionStatusFaileIdList
										.get(i);
								if (loopInfo[1].toString().equals("3")
										|| loopInfo[1].toString().equals("4")) {
									updateDeviceIdList.add(loopInfo[0]
											.toString());
								} else {
									wrongDeviceIdMap.put(
											loopInfo[0].toString(),
											loopInfo[0].toString());
								}
							}
						}
					} else {
						wrongDeviceIdMap.put(device, device);
					}
				}
			}
		}
		
		AppLogger.info("wrongDeviceIdList:【" + wrongDeviceIdMap
				+ "】,updateDeviceIdList：【" + updateDeviceIdList + "】");
		boolean isInsertPolicyFlag = false;
		if (wrongDeviceIdMap.isEmpty()) {
			List<String> addIllegalityList = checkDeviceRegister(addDeviceIdList);
			if (!(addIllegalityList.isEmpty())) {
				illegalityDeviceIdList.addAll(addIllegalityList);
			} else {
				StringBuffer addPolicy = new StringBuffer(
						"insert into AUMS_VER_VERSIONUPDATEPOLICY (UPDATEPOLICYID,ISEFFECT,BEGINTIME,BATCHPERIOD,BATCHNUM,policytype,applydate,brno) VALUES(");
				addPolicy.append("'" + policyId + "',");
				addPolicy.append("'" + iseffect + "',");
				addPolicy.append("to_date('" + startTime
						+ "','YYYY-MM-DD HH24:MI:SS'),");
				addPolicy.append("'" + batchperiod + "',");
				addPolicy.append("'" + batchnum + "',");
				//追加补充字段 lk 20180327
				addPolicy.append("'" + policytype + "',");
				addPolicy.append("'" + applydate + "',");
				addPolicy.append("'" + brno + "')");
				
				P_Jdbc.executeSQL(null, addPolicy.toString(), true);
				isInsertPolicyFlag = true;
				for (String deviceId : addDeviceIdList) {
					StringBuffer deviceInfoSb = new StringBuffer(
							"select select branchno,devtype from AUMS_V_DEVRUNNINGINFOVIEW DEVID=");
					deviceInfoSb.append("'" + deviceId + "'");
					
					List<?> deviceList = (List<?>) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1);
					Object[] arr = (Object[]) deviceList.get(0);
					String devbrno = (arr[0] == null ? "9999" : arr[0])
							.toString();
					String devType = (arr[1] == null ? "VTM" : arr[1])
							.toString();
					StringBuffer saveDeviceAndVersion = new StringBuffer(
							"insert into AUMS_VER_TO_DEV (DEVID,VERSIONID,DEVBRNO,DEVTYPE,UPDATEPOLICYID,devaddmode,createtime) VALUES(");
					saveDeviceAndVersion.append("'" + deviceId + "',");
					saveDeviceAndVersion.append("'" + versionId + "',");
					saveDeviceAndVersion.append("'" + devbrno + "',");
					saveDeviceAndVersion.append("'" + devType + "',");
					saveDeviceAndVersion.append("'" + policyId + "',");
					saveDeviceAndVersion.append("'" + devaddmode + "',");
					saveDeviceAndVersion.append("'" + date + "')");
					P_Jdbc.executeSQL(null, saveDeviceAndVersion.toString(), true);
					StringBuffer selectVersionIdByDevId = new StringBuffer(
							"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.PATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
					selectVersionIdByDevId
							.append("'"
									+ deviceId
									+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN T_PCVA_OPTIONS_TEMPLATE TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.OPTIONS_ID");
					List<?> list = (List<?>) P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
					if (!(jedis == null)) {
						AppLogger.info("获取redis成功，开始存入数据");
						if (!(list.isEmpty())) {
							Object[] versionArr = (Object[]) list.get(0);
							versionInfo.setId((versionArr[0] == null ? "Id空"
									: versionArr[0]).toString());
							versionInfo
									.setName((versionArr[1] == null ? "name空"
											: versionArr[1]).toString());
							versionInfo
									.setDescription((versionArr[2] == null ? "描述空"
											: versionArr[2]).toString());
							optionsInfo
									.setMatchPatterns((versionArr[3] == null ? "匹配模式空"
											: versionArr[3]).toString());
							optionsInfo
									.setExcludedFiles((versionArr[4] == null ? "排除文件空"
											: versionArr[4]).toString());
							optionsInfo
									.setExcludedDirs((versionArr[5] == null ? "排除文件夹空"
											: versionArr[5]).toString());
							optionsInfo
									.setOnceUpdateDirs((versionArr[6] == null ? "空"
											: versionArr[6]).toString());
							optionsInfo
									.setOnceUpdateFiles((versionArr[7] == null ? "空"
											: versionArr[7]).toString());
							versionInfo.setOptions(optionsInfo);
							for (int i = 0; i < list.size(); i++) {
								FilesDetails fileInfo = new FilesDetails();
								Object[] fileArr = (Object[]) list.get(i);
								fileInfo.setId((fileArr[8] == null ? "空"
										: fileArr[8]).toString());
								fileInfo.setMd5((fileArr[9] == null ? "空"
										: fileArr[9]).toString());
								fileInfo.setPath((fileArr[10] == null ? "空"
										: fileArr[10]).toString());
								fileInfo.setVersionid((fileArr[11] == null ? "空"
										: fileArr[11]).toString());
								fileInfo.setSize((fileArr[12] == null ? "空"
										: fileArr[12]).toString());
								fileDetailList.add(fileInfo);
							}
							versionInfo.setFiles(fileDetailList);
						}
						byte[] versionInfobyte = SerializeUtil
								.serialize((Object) versionInfo);
						jedis.set(deviceId.getBytes(), versionInfobyte);
					}
				}
			}
		} else {
			resultObj.put("resultFlag", false);
			Iterator<Map.Entry<String, String>> it = wrongDeviceIdMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				wrongDeviceIdList.add(entry.getKey());
			}
			return new TCResult(0, ErrorCode.REMOTE, wrongDeviceIdList+ "机具已有最新版本或其版本状态未知，请不要选择这些机具后重试！");
		}
		List<String> updateIllegalityList = checkDeviceRegister(updateDeviceIdList);
		if (!(updateIllegalityList.isEmpty())) {
			illegalityDeviceIdList.addAll(updateIllegalityList);
		} else if (illegalityDeviceIdList.isEmpty()) {
			if (isInsertPolicyFlag) {
				AppLogger.info("已插入一次策略，无需重新插入");
			} else {
				StringBuffer addPolicy = new StringBuffer(
						"insert into AUMS_VER_VERSIONUPDATEPOLICY (UPDATEPOLICYID,ISEFFECT,BEGINTIME,BATCHPERIOD,BATCHNUM,policytype,applydate,brno) VALUES(");
				addPolicy.append("'" + policyId + "',");
				addPolicy.append("'" + iseffect + "',");
				addPolicy.append("to_date('" + startTime
						+ "','YYYY-MM-DD HH24:MI:SS'),");
				addPolicy.append("'" + batchperiod + "',");
				addPolicy.append("'" + batchnum + "')");
				//追加补充字段 lk 20180327
				addPolicy.append("'" + policytype + "')");
				addPolicy.append("'" + applydate + "')");
				addPolicy.append("'" + brno + "')");
				
				P_Jdbc.executeSQL(null, addPolicy.toString(), true);
			}
			for (String deviceId : updateDeviceIdList) {
				//源码
				StringBuffer deviceInfoSb = new StringBuffer(
						"select select branchno,devtype from AUMS_V_DEVRUNNINGINFOVIEW DEVID=");
				deviceInfoSb.append("'" + deviceId + "'");
				List<?> deviceList = (List<?>) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1);
				Object[] arr = (Object[]) deviceList.get(0);
				String devbrno = (arr[0] == null ? "9999" : arr[0]).toString();
				String devType = (arr[1] == null ? "VTM" : arr[1]).toString();
				StringBuffer saveDeviceAndVersion = new StringBuffer(
						"insert into AUMS_VER_TO_DEV (DEVID,VERSIONID,DEVBRNO,DEVTYPE,UPDATEPOLICYID,devaddmode,createtime) VALUES(");
				saveDeviceAndVersion.append("'" + deviceId + "',");
				saveDeviceAndVersion.append("'" + versionId + "',");
				saveDeviceAndVersion.append("'" + devbrno + "',");
				saveDeviceAndVersion.append("'" + devType + "',");
				saveDeviceAndVersion.append("'" + policyId + "',");
				saveDeviceAndVersion.append("'" + devaddmode + "',");
				saveDeviceAndVersion.append("'" + date + "')");
				P_Jdbc.executeSQL(null, saveDeviceAndVersion.toString(), true);
				StringBuffer selectVersionIdByDevId = new StringBuffer(
						"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.PATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
				selectVersionIdByDevId
						.append("'"
								+ deviceId
								+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN T_PCVA_OPTIONS_TEMPLATE TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.OPTIONS_ID");
				List<?> list = (List<?>) P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
				if (!(jedis == null)) {
					AppLogger.info("获取redis成功，开始存入数据");
					if (!(list.isEmpty())) {
						Object[] versionArr = (Object[]) list.get(0);
						versionInfo.setId((versionArr[0] == null ? "Id空"
								: versionArr[0]).toString());
						versionInfo.setName((versionArr[1] == null ? "name空"
								: versionArr[1]).toString());
						versionInfo
								.setDescription((versionArr[2] == null ? "描述空"
										: versionArr[2]).toString());
						optionsInfo
								.setMatchPatterns((versionArr[3] == null ? "匹配模式空"
										: versionArr[3]).toString());
						optionsInfo
								.setExcludedFiles((versionArr[4] == null ? "排除文件空"
										: versionArr[4]).toString());
						optionsInfo
								.setExcludedDirs((versionArr[5] == null ? "排除文件夹空"
										: versionArr[5]).toString());
						optionsInfo
								.setOnceUpdateDirs((versionArr[6] == null ? "空"
										: versionArr[6]).toString());
						optionsInfo
								.setOnceUpdateFiles((versionArr[7] == null ? "空"
										: versionArr[7]).toString());
						versionInfo.setOptions(optionsInfo);
						for (int i = 0; i < list.size(); i++) {
							FilesDetails fileInfo = new FilesDetails();
							Object[] fileArr = (Object[]) list.get(i);
							fileInfo.setId((fileArr[8] == null ? "空"
									: fileArr[8]).toString());
							fileInfo.setMd5((fileArr[9] == null ? "空"
									: fileArr[9]).toString());
							fileInfo.setPath((fileArr[10] == null ? "空"
									: fileArr[10]).toString());
							fileInfo.setVersionid((fileArr[11] == null ? "空"
									: fileArr[11]).toString());
							fileInfo.setSize((fileArr[12] == null ? "空"
									: fileArr[12]).toString());
							fileDetailList.add(fileInfo);
						}
						versionInfo.setFiles(fileDetailList);
						byte[] versionInfobyte = SerializeUtil
								.serialize(versionInfo);
						jedis.set(deviceId.getBytes(), versionInfobyte);
					}
				}
			}
			if (illegalityDeviceIdList.isEmpty()) {
				return new TCResult(1, "000000", "发布完成");
			} else {
				return new TCResult(0, ErrorCode.REMOTE, illegalityDeviceIdList + "机具未注册");
			}
		} else {
			return new TCResult(0, ErrorCode.REMOTE, illegalityDeviceIdList + "机具已有最新版本");
		}
		if (!(jedis == null)) {
			jedis.close();
		}
		return new TCResult(1, "000000", "发布完成");
	}

	/**
	 * 检查机具LIST是否全部注册 工具类
	 * 
	 * @param checkList
	 * @return
	 * @throws Exception
	 */
	public List<String> checkDeviceRegister(List<String> checkList)
			throws Exception {
		List<String> result = new ArrayList<String>();
		for (String item : checkList) {
			StringBuffer deviceInfoSb = new StringBuffer(
					"select devid from aums_dev_info where devuniqueid is not null and t1.DEVID=");
			deviceInfoSb.append("'" + item + "'");
			List<?> deviceList = (List<?>) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1);
			if (deviceList.isEmpty()) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * @category 加载版本策略下拉框列表
	 */
	@Component(label = "加载版本策略下拉框列表", comment = "加载版本策略下拉框列表", author = "zhipeng", date = "2017-09-01 05:00:54")
	public Object A_VersionOptionslist() throws BusException {
		String sql = "select OPTIONS_TEMPLATE_ID,strategy_template_name from aums_ver_options_template";
		JSONArray data = new JSONArray();
		try {
			List<?> options_Template_List = (List<?>) P_Jdbc.dmlSelect(null, sql, -1);
			if (options_Template_List != null) {
				for (int i = 0; i < options_Template_List.size(); i++) {
					Object[] objArr = (Object[]) options_Template_List.get(i);
					JSONObject obj = new JSONObject();
					obj.put("desp", (objArr[0] == null ? "" : objArr[0]) + "：" + (objArr[1] == null ? "" : objArr[1]));
					obj.put("value", objArr[0] == null ? "" : objArr[0]);
					data.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusException(ErrorCodeModule.IME999,"加载机构下拉框列表时数据库异常");
		}
		AppLogger.info("加载版本策略下拉框列表:" + data);
		return data;
	}

	/**
	 * 创建version.json文件工具类
	 * 
	 * @param versionCode
	 * @param optionId
	 * @param description
	 * @param versionId
	 * @param upackPath
	 * @throws JsonProcessingException
	 */
	public static void createVersionJsonFile(String versionCode,
			String optionId, String description, String versionId,
			String upackPath) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		VersionJsonFile jsonObject = new VersionJsonFile();
		jsonObject.setVersionId(versionId);
		jsonObject.setVersionName(versionCode);
		jsonObject.setDescription(description);
		jsonObject.setOptionId(optionId);
		String json = mapper.writeValueAsString(jsonObject);
		FileWriter fw = null;
		try {
			fw = new FileWriter(upackPath + versionId + "/" + "version.json");
			fw.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
