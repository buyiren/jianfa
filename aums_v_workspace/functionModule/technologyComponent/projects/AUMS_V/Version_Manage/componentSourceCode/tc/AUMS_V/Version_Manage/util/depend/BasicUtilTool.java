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
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BasicUtilTool {

	
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
			//暂时不放到uuid下面，直接以versionCodew为子目录
//			fw = new FileWriter(upackPath + versionId + "/" + "version.json");
			//20180914 韩斌修改 原因：与张婵娟沟通后去人version文件并入dotnet中
			//fw = new FileWriter(upackPath + versionCode + "/dotnet/" + "version.json");
			fw = new FileWriter(upackPath + versionCode + "/" + "version.json");
			fw.write(json);
		} catch (IOException e) {
			AppLogger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
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
	 * 删除路径下的内容
	 * @param sPath
	 * @return 
	 * @return
	 */
	public static boolean deleteDirectory(String sPath) {
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		if (flag == true) {
		}
		return flag;
	}
	
	/**
	 * @category 发布版本
	 * @param versionCode
	 *            入参|关联版本号| {@link String}
	 * @param iseffect
	 *            入参|是否立即生效| {@link String}
	 * @param startTime
	 *            入参|开始时间| {@link String}
	 * @param batchperiod
	 *            入参|升级时间间隔| {@link String}
	 * @param batchnum
	 *            入参|升级机器数量| {@link String}
	 * @param devidList
	 *            入参|机具ID| {@link java.lang.String[]}
	 * @param devaddmode
	 *            入参|添加方式| {@link String}
	 * @param branchNo
	 * 			    入参|操作机构号|{@link String}
	 * @param returnflag
	 * 			    入参|回退标志|{@link boolean}
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "关联版本号", dictId = "229036DA83604821996CF618B840E0AD", type = String.class),
			@Param(name = "iseffect", comment = "是否立即生效", dictId = "1462E30BC7A54573A6F7775DA9E00D9F", type = String.class),
			@Param(name = "startTime", comment = "开始时间", dictId = "87034DCDEF76403B94FA9A73A0246C98", type = String.class),
			@Param(name = "batchperiod", comment = "升级时间间隔", dictId = "C8EC6C5F56AD45EF8710E13B0DBDA939", type = String.class),
			@Param(name = "batchnum", comment = "升级机器数量", dictId = "99BF5D853DD6410E9358CED2F405216E", type = String.class),
			@Param(name = "devidList", comment = "机具ID", type = java.lang.String[].class),
			@Param(name = "devaddmode", comment = "添加方式", dictId = "7D93D908FE9945AFB2827848000F12CF", type = String.class),
			@Param(name = "branchNo", comment = "操作机构号", dictId = "7F93D908FE9945AFB2827848000F12CF", type = String.class)})
	
	@Component(label = "发布版本", comment = "发布版本", date = "2018-5-09 10:47:19")
	public static TCResult deployVersion(String versionCode, String iseffect,
			String startTime, String batchperiod, String batchnum,
			String[] devidList, String devaddmode,String branchNo,boolean returnflag) throws Exception {
		
		JSONObject resultObj = new JSONObject();
		//Jedis jedis = RedisConnectionPool.getJedits();
		Jedis jedis = null;
		List<String> addDeviceIdList = new ArrayList<String>();
		List<String> updateDeviceIdList = new ArrayList<String>();
		List<String> wrongDeviceIdList = new ArrayList<String>();
		Map<String, String> wrongDeviceIdMap = new TreeMap<String, String>();
		List<String> illegalityDeviceIdList = new ArrayList<String>();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(now);
		String date14 = sdf1.format(now);
		VersionFilesList versionInfo = new VersionFilesList();
		A_OptionsVo optionsInfo = new A_OptionsVo();
		List<FilesDetails> fileDetailList = new ArrayList<FilesDetails>();
		//获取序列号
		String dbTypeSQL = "select paramvalue from tp_cip_sysparameters where modulecode='AUMS' and paramkeyname='DBTYPE'";
		JavaList dbTypeList = (JavaList)P_Jdbc.dmlSelect(null, dbTypeSQL, -1).getOutputParams().get(1);
		String dbType = dbTypeList.getListItem(0).get(0).toString();
		String getPolicyId = "";
		String policyId = "";
		if(dbType.equals("ORA")){
			getPolicyId = "select AUMS_VER_UPDATEPOLICY_SEQ.NEXTVAL FROM DUAL";
			JavaList policylist = (JavaList) P_Jdbc.dmlSelect(null, getPolicyId, -1).getOutputParams().get(1);
			BigDecimal policyNum = (BigDecimal) policylist.getListItem(0).get(0);
			policyId = String.valueOf(policyNum.intValue());
		}else if(dbType.equals("DB2")){
			getPolicyId = "select nextval for AUMS_VER_UPDATEPOLICY_SEQ FROM SYSIBM.DUAL";
			JavaList policylist = (JavaList) P_Jdbc.dmlSelect(null, getPolicyId, -1).getOutputParams().get(1);
			BigDecimal policyNum = (BigDecimal) policylist.getListItem(0).get(0);
			policyId = String.valueOf(policyNum.intValue());
		}else if(dbType.equals("Mysql")){
			getPolicyId = "select current_val into val from AUMS_VER_UPDATEPOLICY_SEQ";
			JavaList policylist = (JavaList) P_Jdbc.dmlSelect(null, getPolicyId, -1).getOutputParams().get(1);
			BigDecimal policyNum = (BigDecimal) policylist.getListItem(0).get(0);
			policyId = String.valueOf(policyNum.intValue());
		}else{
			throw new BusException(ErrorCodeModule.IME999,"暂不支持此类型数据库:【"+dbType+"】");
		}
		//获取序列号完成
		//判断所选机具是否为空
		if (devidList == null || devidList.length==0) {
			throw new BusException(ErrorCodeModule.IME999,"请选择您需要发布版本的机具");
		}
		AppLogger.info("=======1");
		//add lk 20180327 用于版本回退
		//获取操作网点唯一标识
		String brno = branchNo;
		//获取applydate
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String applydate = df.format(new Date());
		//设定版本操作类型
		String policytype = "0";
		//add lk 20180327 增加完成
		//获取版本编号对应的版本ID
		String versionIDSQL = "select versionid,description from aums_ver_branchverinfo where versioncode='" + versionCode + "'";
		JavaList versionIDList =  (JavaList) P_Jdbc.dmlSelect(null, versionIDSQL, -1).getOutputParams().get(1);
		String versionID = (String) versionIDList.getListItem(0).get(0);
		String description = (String) versionIDList.getListItem(0).get(1);
		//格式化需要发布的机具列表
		for (String device : devidList) {
			AppLogger.info("待处理的机具为：【"+device+"】");
			StringBuffer checkDeviceSql = new StringBuffer(
					"select VERSIONID,VERSIONCODE from AUMS_VER_TO_DEV TPVTD WHERE TPVTD.DEVID=");
			checkDeviceSql.append("'" + device + "'");
			checkDeviceSql.append("order by to_number(updatepolicyid) desc");
			AppLogger.info("=======2");
			String oldVersionID = "";
			String oldVersionCode = "";
			TCResult checkDeviceResult = P_Jdbc.dmlSelect(null, checkDeviceSql.toString(), -1);
			if(checkDeviceResult==null){
				AppLogger.info("=======3");
				AppLogger.info("公共发布版本,查询AUMS_VER_TO_DEV异常");
				continue;
			}
			if (checkDeviceResult.getStatus()==2) {
				AppLogger.info("=======4");
				addDeviceIdList.add(device);
				AppLogger.info("查询设备版本关系表无记录");
				for (int i = 0; i < addDeviceIdList.size(); i++) {
					AppLogger.info("addDeviceIdList的第"+i+1+"个值是："+addDeviceIdList.get(i));
				} 
			} else {
				AppLogger.info("=======5");
				JavaList versionList = (JavaList) checkDeviceResult.getOutputParams().get(1);
				oldVersionID = (String) versionList.getListItem(0).get(0);
				oldVersionCode = (String) versionList.getListItem(0).get(1);
				
				if (oldVersionCode.equals(versionCode)) {
					AppLogger.info("=======6");
					String checkDeviceStatusSql = "select devid,status from AUMS_VER_LOG where adid = '"
							+ oldVersionID + "' and devid='" + device + "'";
					TCResult checkDeviceStatusResult = P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
					if(checkDeviceStatusResult==null){
						AppLogger.info("=======7");
						AppLogger.info("公共发布版本,查询AUMS_VER_LOG异常");
						continue;
					}
					
					if (checkDeviceStatusResult.getStatus()==2) {
						AppLogger.info("=======8");
						wrongDeviceIdMap.put(device, device);
					} else {
						AppLogger.info("=======9");
						JavaList versionStatusFailIdList = (JavaList) checkDeviceStatusResult.getOutputParams().get(1);
						for (int i = 0; i < versionStatusFailIdList.size(); i++) {
							ArrayList loopInfo = versionStatusFailIdList.getListItem(i);
							AppLogger.info("=======10");
							if (loopInfo.get(1).toString().equals("4")) {
								AppLogger.info("=======11");
								updateDeviceIdList.add(loopInfo.get(0).toString());
							} else {
								AppLogger.info("=======12");
								wrongDeviceIdMap.put(loopInfo.get(0).toString(),
										loopInfo.get(0).toString());
							}
						}
					}
				} else {
					AppLogger.info("=======13");
					String queryOldVerisonCode = "SELECT VERSIONCODE FROM aums_ver_branchverinfo where VERSIONID = '" + oldVersionID + "'";
					TCResult oldVersionResult = P_Jdbc.dmlSelect(null, queryOldVerisonCode, -1);
					if(oldVersionResult == null){
						AppLogger.info("=======14");
						AppLogger.info("公共发布版本,查询aums_ver_branchverinfo异常");
						continue;
					}
					if(oldVersionResult.getStatus()==2){
						AppLogger.info("=======15");
						wrongDeviceIdMap.put(device, device);
						continue;
					}
					JavaList oldVersionList = (JavaList)oldVersionResult.getOutputParams().get(1);
					String oldVerisonCode = String.valueOf(oldVersionList.getListItem(0).get(0));
					AppLogger.info("=======16");
					int lenBranchId = branchNo.length()+8;
					String versionIdVer = versionCode.substring(2, 2+lenBranchId)
							+ versionCode.substring(3+lenBranchId,7+lenBranchId);
					String oldVersionIdVer = oldVerisonCode.substring(2, 2+lenBranchId)
							+ oldVerisonCode.substring(3+lenBranchId,7+lenBranchId);
					if (returnflag || Long.parseLong(versionIdVer) > Long.parseLong(oldVersionIdVer)) {
						AppLogger.info("=======17");
						String checkDeviceStatusSql = "select devid,status from aums_ver_log where adid = '"
								+ oldVersionID + "' and devid='" + device + "'";
						TCResult checkDeviceStatusResult = P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
						if(checkDeviceStatusResult==null){
							AppLogger.info("=======18");
							AppLogger.info("公共发布版本,查询AUMS_VER_LOG异常");
							continue;
						}
						
						if (checkDeviceStatusResult.getStatus()==2) {
							AppLogger.info("=======19");
							wrongDeviceIdMap.put(device, device);
						} else {
							JavaList versionStatusFailIdList = (JavaList) checkDeviceStatusResult.getOutputParams().get(1);
							for (int i = 0; i < versionStatusFailIdList.size(); i++) {
								AppLogger.info("=======20");
								ArrayList loopInfo =  versionStatusFailIdList.getListItem(i);
								if (loopInfo.get(1).toString().equals("3")
										|| loopInfo.get(1).toString().equals("4")) {
									AppLogger.info("=======21");
									updateDeviceIdList.add(loopInfo.get(0)
											.toString());
								} else {
									AppLogger.info("=======22");
									wrongDeviceIdMap.put(
											loopInfo.get(0).toString(),
											loopInfo.get(0).toString());
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
				+ "】,updateDeviceIdList：【" + updateDeviceIdList + "】"
				+ "】,addDeviceIdList：【" + addDeviceIdList + "】");
		boolean isInsertPolicyFlag = false;
		if (wrongDeviceIdMap.isEmpty()) {
			List<String> addIllegalityList = checkDeviceRegister(addDeviceIdList);
			AppLogger.info("【addIllegalityList】"+addIllegalityList);
			AppLogger.info("检查设备是否注册完成-331");
			if (!(addIllegalityList.isEmpty())) {
				AppLogger.info("addIllegalityList非空");
				illegalityDeviceIdList.addAll(addIllegalityList);
			} else {
				AppLogger.info("addIllegalityList为空");
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
				AppLogger.info("startTime==="+startTime);
				AppLogger.info("插入策略表1");
				P_Jdbc.executeSQL(null, addPolicy.toString(), true);
				AppLogger.info("插入策略表1成功");
				isInsertPolicyFlag = true;
				for (String deviceId : addDeviceIdList) {
					StringBuffer deviceInfoSb = new StringBuffer(
							"select branchno,devtype,devnum from AUMS_V_DEVRUNNINGINFOVIEW where DEVID=");
					deviceInfoSb.append("'" + deviceId + "'");
					
					JavaList deviceList = (JavaList) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1).getOutputParams().get(1);
					
					String devNum = (String) deviceList.getListItem(0).get(2);
					String devBranchNo = (String) deviceList.getListItem(0).get(0);
					String tmpDevType = (String) deviceList.getListItem(0).get(1);
					
					String devbrno="";
					if(devBranchNo == null || devBranchNo.equals("")){
						devbrno="9999";
					}else{
						devbrno=devBranchNo;
					}
					String devType="";
					if(tmpDevType == null || tmpDevType.equals("")){
						devType="VTM";
					}else{
						devType=tmpDevType;
					}
					StringBuffer saveDeviceAndVersion = new StringBuffer(
							"insert into AUMS_VER_TO_DEV (DEVID,VERSIONID,DEVBRNO,DEVTYPE,UPDATEPOLICYID,devaddmode,createtime,devnum,versioncode) VALUES(");
					saveDeviceAndVersion.append("'" + deviceId + "',");
					saveDeviceAndVersion.append("'" + versionID + "',");
					saveDeviceAndVersion.append("'" + devbrno + "',");
					saveDeviceAndVersion.append("'" + devType + "',");
					saveDeviceAndVersion.append("'" + policyId + "',");
					saveDeviceAndVersion.append("'" + devaddmode + "',");
					saveDeviceAndVersion.append("'" + date + "',");
					saveDeviceAndVersion.append("'" + devNum + "',");
					saveDeviceAndVersion.append("'" + versionCode + "')");
					P_Jdbc.executeSQL(null, saveDeviceAndVersion.toString(), true);
					
					StringBuffer saveDeviceVersionLog = new StringBuffer(
							"insert into AUMS_VER_LOG (DEVID,ADID,DESCRIPTION,UPDATEPOLICYID,STATUS,CREATETIME,UPDATETIME,REMARK1,REMARK2,APPLYDATE) VALUES(");
					saveDeviceVersionLog.append("'" + deviceId + "',");
					saveDeviceVersionLog.append("'" + versionID + "',");
					saveDeviceVersionLog.append("'" + description + "',");
					saveDeviceVersionLog.append("'" + policyId + "',");
					saveDeviceVersionLog.append("'3',");
					saveDeviceVersionLog.append("'" + date14 + "',");
					saveDeviceVersionLog.append("'" + date14 + "',");
					saveDeviceVersionLog.append("'',");
					saveDeviceVersionLog.append("'A',");
					saveDeviceVersionLog.append("'" + date + "')");
					P_Jdbc.executeSQL(null, saveDeviceVersionLog.toString(), true);
					
					
					StringBuffer selectVersionIdByDevId = new StringBuffer(
							"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
					selectVersionIdByDevId
							.append("'"
									+ deviceId
									+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id union SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_branchverinfo TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
					selectVersionIdByDevId
					.append("'"
							+ deviceId
							+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id");
			
					TCResult verserionIDRes = P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
					
					AppLogger.info("执行的关联查询为：【"+selectVersionIdByDevId.toString()+"】");
					if(verserionIDRes == null){
						return new TCResult(0, ErrorCode.REMOTE, "查询版本与设备明细查询时出现异常！");
					}
					JavaList list = (JavaList) verserionIDRes.getOutputParams().get(1);
					if (!(jedis == null)) {
						AppLogger.info("获取redis成功，开始存入数据");
						if (!(list.isEmpty())) {
							@SuppressWarnings("rawtypes")
							ArrayList versionArr = list.getListItem(0);
							versionInfo.setId((versionArr.get(0) == null ? "Id空"
									: versionArr.get(0)).toString());
							versionInfo
									.setName((versionArr.get(1) == null ? "name空"
											: versionArr.get(1)).toString());
							versionInfo
									.setDescription((versionArr.get(2) == null ? "描述空"
											: versionArr.get(2)).toString());
							optionsInfo
									.setMatchPatterns((versionArr.get(3) == null ? "匹配模式空"
											: versionArr.get(3)).toString());
							optionsInfo
									.setExcludedFiles((versionArr.get(4) == null ? "排除文件空"
											: versionArr.get(4)).toString());
							optionsInfo
									.setExcludedDirs((versionArr.get(5) == null ? "排除文件夹空"
											: versionArr.get(5)).toString());
							optionsInfo
									.setOnceUpdateDirs((versionArr.get(6) == null ? "空"
											: versionArr.get(6)).toString());
							optionsInfo
									.setOnceUpdateFiles((versionArr.get(7) == null ? "空"
											: versionArr.get(7)).toString());
							versionInfo.setOptions(optionsInfo);
							for (int i = 0; i < list.size(); i++) {
								FilesDetails fileInfo = new FilesDetails();
								JavaList fileArr = (JavaList) list.getListItem(i);
								fileInfo.setId((fileArr.get(8) == null ? "空"
										: fileArr.get(8)).toString());
								fileInfo.setMd5((fileArr.get(9) == null ? "空"
										: fileArr.get(9)).toString());
								fileInfo.setPath((fileArr.get(10) == null ? "空"
										: fileArr.get(10)).toString());
								fileInfo.setVersionid((fileArr.get(1) == null ? "空"
										: fileArr.get(11)).toString());
								fileInfo.setSize((fileArr.get(12) == null ? "空"
										: fileArr.get(12)).toString());
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
		AppLogger.info("检查设备是否注册完成-448");
		
		AppLogger.info("updateIllegalityList:【" + updateIllegalityList
				+ "】,illegalityDeviceIdList：【" + illegalityDeviceIdList + "】");
		
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
				AppLogger.info("startTime==="+startTime);
				AppLogger.info("插入策略表2");
				P_Jdbc.executeSQL(null, addPolicy.toString(), true);
				AppLogger.info("插入策略表2成功");
			}
			for (String deviceId : updateDeviceIdList) {
				//源码
				StringBuffer deviceInfoSb = new StringBuffer(
						"select branchno,devtype,devnum from AUMS_V_DEVRUNNINGINFOVIEW where DEVID=");
				deviceInfoSb.append("'" + deviceId + "'");
				
				JavaList deviceList = (JavaList) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1).getOutputParams().get(1);
				
				String devNum = (String) deviceList.getListItem(0).get(2);
				String devBranchNo = (String) deviceList.getListItem(0).get(0);
				String tmpDevType = (String) deviceList.getListItem(0).get(1);
				
				String devbrno="";
				if(devBranchNo == null || devBranchNo.equals("")){
					devbrno="9999";
				}else{
					devbrno=devBranchNo;
				}
				String devType="";
				if(tmpDevType == null || tmpDevType.equals("")){
					devType="VTM";
				}else{
					devType=tmpDevType;
				}
				
				StringBuffer saveDeviceAndVersion = new StringBuffer(
						"insert into AUMS_VER_TO_DEV (DEVID,VERSIONID,DEVBRNO,DEVTYPE,UPDATEPOLICYID,devaddmode,createtime,devnum,versioncode) VALUES(");
				saveDeviceAndVersion.append("'" + deviceId + "',");
				saveDeviceAndVersion.append("'" + versionID + "',");
				saveDeviceAndVersion.append("'" + devbrno + "',");
				saveDeviceAndVersion.append("'" + devType + "',");
				saveDeviceAndVersion.append("'" + policyId + "',");
				saveDeviceAndVersion.append("'" + devaddmode + "',");
				saveDeviceAndVersion.append("'" + date + "',");
				saveDeviceAndVersion.append("'" + devNum + "',");
				saveDeviceAndVersion.append("'" + versionCode + "')");
				P_Jdbc.executeSQL(null, saveDeviceAndVersion.toString(), true);
				
				StringBuffer saveDeviceVersionLog = new StringBuffer(
						"insert into AUMS_VER_LOG (DEVID,ADID,DESCRIPTION,UPDATEPOLICYID,STATUS,CREATETIME,UPDATETIME,REMARK1,REMARK2,APPLYDATE) VALUES(");
				saveDeviceVersionLog.append("'" + deviceId + "',");
				saveDeviceVersionLog.append("'" + versionID + "',");
				saveDeviceVersionLog.append("'" + description + "',");
				saveDeviceVersionLog.append("'" + policyId + "',");
				saveDeviceVersionLog.append("'3',");
				saveDeviceVersionLog.append("'" + date14 + "',");
				saveDeviceVersionLog.append("'" + date14 + "',");
				saveDeviceVersionLog.append("'',");
				saveDeviceVersionLog.append("'B',");
				saveDeviceVersionLog.append("'" + date + "')");
				P_Jdbc.executeSQL(null, saveDeviceVersionLog.toString(), true);
				
				
				StringBuffer selectVersionIdByDevId = new StringBuffer(
						"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
				selectVersionIdByDevId
						.append("'"
								+ deviceId
								+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id union SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_branchverinfo TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
				selectVersionIdByDevId
				.append("'"
						+ deviceId
						+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id");
		
				TCResult verserionIDRes = P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
				
				AppLogger.info("执行的关联查询为：【"+selectVersionIdByDevId.toString()+"】");
				if(verserionIDRes == null){
					return new TCResult(0, ErrorCode.REMOTE, "查询版本与设备明细查询时出现异常！");
				}
				JavaList list = (JavaList) verserionIDRes.getOutputParams().get(1);
				if (!(jedis == null)) {
					AppLogger.info("获取redis成功，开始存入数据");
					if (!(list.isEmpty())) {
						@SuppressWarnings("rawtypes")
						ArrayList versionArr = list.getListItem(0);
						versionInfo.setId((versionArr.get(0) == null ? "Id空"
								: versionArr.get(0)).toString());
						versionInfo
								.setName((versionArr.get(1) == null ? "name空"
										: versionArr.get(1)).toString());
						versionInfo
								.setDescription((versionArr.get(2) == null ? "描述空"
										: versionArr.get(2)).toString());
						optionsInfo
								.setMatchPatterns((versionArr.get(3) == null ? "匹配模式空"
										: versionArr.get(3)).toString());
						optionsInfo
								.setExcludedFiles((versionArr.get(4) == null ? "排除文件空"
										: versionArr.get(4)).toString());
						optionsInfo
								.setExcludedDirs((versionArr.get(5) == null ? "排除文件夹空"
										: versionArr.get(5)).toString());
						optionsInfo
								.setOnceUpdateDirs((versionArr.get(6) == null ? "空"
										: versionArr.get(6)).toString());
						optionsInfo
								.setOnceUpdateFiles((versionArr.get(7) == null ? "空"
										: versionArr.get(7)).toString());
						versionInfo.setOptions(optionsInfo);
						for (int i = 0; i < list.size(); i++) {
							FilesDetails fileInfo = new FilesDetails();
							JavaList fileArr = (JavaList) list.getListItem(i);
							fileInfo.setId((fileArr.get(8) == null ? "空"
									: fileArr.get(8)).toString());
							fileInfo.setMd5((fileArr.get(9) == null ? "空"
									: fileArr.get(9)).toString());
							fileInfo.setPath((fileArr.get(10) == null ? "空"
									: fileArr.get(10)).toString());
							fileInfo.setVersionid((fileArr.get(1) == null ? "空"
									: fileArr.get(11)).toString());
							fileInfo.setSize((fileArr.get(12) == null ? "空"
									: fileArr.get(12)).toString());
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
			return new TCResult(0, ErrorCode.REMOTE, illegalityDeviceIdList + "BasicUtilTool==机具已有最新版本");
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
	public static List<String> checkDeviceRegister(List<String> checkList)
			throws Exception {
		List<String> result = new ArrayList<String>();
		for (String item : checkList) {
			AppLogger.info("item的值是："+item);
			StringBuffer deviceInfoSb = new StringBuffer(
					"select devid from aums_dev_info where devuniqueid is not null and DEVID=");
			deviceInfoSb.append("'" + item + "'");
			
			TCResult deviceTcResult = P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1);
			if (deviceTcResult == null || deviceTcResult.getStatus() ==2) {
				AppLogger.info("==result.add(item)==完成");
				result.add(item);
			}
		}
		return result;
	}
}
