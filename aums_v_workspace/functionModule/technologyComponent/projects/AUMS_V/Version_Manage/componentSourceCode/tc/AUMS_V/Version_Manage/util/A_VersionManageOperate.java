package tc.AUMS_V.Version_Manage.util;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import org.apache.poi.ss.usermodel.Workbook;

import redis.clients.jedis.Jedis;
import tc.AUMS_V.Version_Manage.util.depend.A_OptionsVo;
import tc.AUMS_V.Version_Manage.util.depend.A_VersionUpdatePolicy;
import tc.AUMS_V.Version_Manage.util.depend.BasicUtilTool;
import tc.AUMS_V.Version_Manage.util.depend.ExcelExcelParseTool;
import tc.AUMS_V.Version_Manage.util.depend.FilesDetails;
import tc.AUMS_V.Version_Manage.util.depend.ReadFile;
import tc.AUMS_V.Version_Manage.util.depend.RedisConnectionPool;
import tc.AUMS_V.Version_Manage.util.depend.SerializeUtil;
import tc.AUMS_V.Version_Manage.util.depend.VersionFilesList;
import tc.AUMS_V.Version_Manage.util.depend.VersiondetailinfoVo;
import tc.AUMS_V.Version_Manage.util.depend.unpackZip;
import tc.bank.constant.BusException;
import tc.AUMS_V.Version_Manage.util.A_VersionApiController;
import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import tc.platform.P_Time;
import tc.platform.P_HttpClient;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 版本管理统一操作类
 * 
 * @author AlphaLi
 * @date 2018-5-04 15:51:13
 */
@ComponentGroup(level = "应用", groupName = "版本管理操作")
public class A_VersionManageOperate {

	/**
	 * @category 校验版本号是否存在
	 * @param versionCode
	 *            入参|版本编号| {@link String}
	 * @param fileName
	 *            入参|文件名称| {@link String}
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "版本编号", type = java.lang.String.class),
			@Param(name = "fileName", comment = "文件名称", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "不可用"),
			@Return(id = "1", desp = "可用") })
	@Component(label = "校验版本号是否存在", style = "判断型", type = "同步组件", comment = "根据版本文件名校验版本号是否存在", author = "AlphaLi", date = "2018-05-07 07:13:02")
	
	public static TCResult A_CheckVersionCode(String versionCode, String fileName)
			throws BusException {

		Pattern mainFileNameChk = Pattern.compile("^V\\_[0-9]{8}\\_[0-9]{4}");
		Pattern depFileNameChk  = Pattern.compile("^V\\_[0-9]{8}\\_[0-9]{4,12}\\_[0-9]{4}");
		
		Matcher matchPar = mainFileNameChk.matcher(versionCode);
		Matcher matchDep = depFileNameChk.matcher(versionCode);
		if (!matchPar.matches() && !matchDep.matches()) {
			return new TCResult(0, ErrorCode.REMOTE, "版本号不合法!");
		}
		
		//文件已经存在检查
		String sql = "select 1 from AUMS_VER_INFO_EXT where ZIPFILENAME='"+ fileName + "'";
		TCResult versionCodeQueryResult = P_Jdbc.dmlSelect(null, sql, -1);
		if (versionCodeQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询文件信息异常");
		}
		if (String.valueOf(versionCodeQueryResult.getStatus()).equals("1")) {
			return new TCResult(0, ErrorCode.REMOTE, "版本文件【"+ fileName +"】已存在");
		}
		return TCResult.newSuccessResult();
	}

	
	/**
	 * @category 校验分行版本号是否存在
	 * @param versionCode
	 *            入参|版本编号| {@link String}
	 * @param fileName
	 *            入参|文件名称| {@link String}
	 * @param branchId
	 *            入参|机构ID| {@link String}
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "版本编号", type = java.lang.String.class),
			@Param(name = "fileName", comment = "文件名称", type = java.lang.String.class),
			@Param(name = "branchId", comment = "机构ID", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "不可用"),
			@Return(id = "1", desp = "可用") })
	@Component(label = "校验分行版本号是否存在", style = "判断型", type = "同步组件", comment = "根据版本文件名校验版本号是否存在", author = "AlphaLi", date = "2018-05-07 07:13:02")
	
	public static TCResult A_CheckVersionCodeBranch(String versionCode, String fileName, String branchId)
			throws BusException {

		//查询分行的机构号
		String branchNo = null;
		String branchNoSql = "SELECT BRANCHNO FROM AUMS_BRANCHINFO WHERE BRANCHID ='" + branchId + "'";
		TCResult branchNoQuery = P_Jdbc.dmlSelect(null, branchNoSql, -1);
		if (branchNoQuery != null) {
			if (branchNoQuery.getStatus() != 2) {
				//获取机构号
				JavaList branchInfoList = (JavaList) branchNoQuery.getOutputParams().get(1);
				branchNo = branchInfoList.getListItem(0).get(0).toString();
				if (branchNo == null || branchNo =="") {
					return new TCResult(0, ErrorCode.REMOTE, "机构号非法");
				}
				AppLogger.info("获取到的机构号为：" + branchNo);
			}
		} else {
			return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常");
		}
		
		int lenBranchNo = branchNo.length();
		//分行的版本号为 "D_机构号_8位日期_4位版本序号"
		Pattern pattern = Pattern.compile("^D\\_[0-9]{"+lenBranchNo+"}\\_[0-9]{8}\\_[0-9]{4}");
		if (!(versionCode == null)) {
			Matcher match = pattern.matcher(versionCode);
			if (!match.matches()) {
				return new TCResult(0, ErrorCode.REMOTE, "分行版本号规则:D_分行机构号_8位日期_4位版本序号");
			}
		}
		
		//文件名所属机构检查
		if (!fileName.substring(2, 2+lenBranchNo).equals(branchNo)) {
			return new TCResult(0, ErrorCode.REMOTE, "文件名中的机构号【"+ fileName.substring(2, 2+lenBranchNo) +"】与操作机构【"+ branchNo +"】不一致！！！");
		}

		String sql = "select versionCode from AUMS_VER_INFO where versionCode='"+ versionCode + "'";
		TCResult versionCodeQueryResult = null;
		// 查询版本号是否可用
		versionCodeQueryResult = P_Jdbc.dmlSelect(null, sql, -1);
		if (versionCodeQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询版本信息异常");
		}
		if (String.valueOf(versionCodeQueryResult.getStatus()).equals("2")) {
			// 查询版本号是否低于正在使用的版本号
			String compareSql = "SELECT V1.VERSIONCODE FROM AUMS_VER_INFO V1,AUMS_VER_INFO_EXT V2 WHERE VERSIONTYPE = '2' and V1.VERSIONID=V2.VERSIONID and V2.OPERBRANCHNO='"+ branchNo +"' order by V1.VERSIONCODE desc";
			TCResult versionCodeCompareQuery = P_Jdbc.dmlSelect(null, compareSql, -1);
			if (versionCodeCompareQuery != null) {
				//判断查询是否为空
				if(String.valueOf(versionCodeCompareQuery.getStatus()).equals("2")){
					return new TCResult(1, "000000", "该版本号可用");
				}else{
					// 获取设备最新的版本信息
					JavaList versionCodeInfoList = (JavaList)versionCodeCompareQuery.getOutputParams().get(1);
					String versionCodeInfo = versionCodeInfoList.getListItem(0).get(0).toString();
					//D_10001_20181201_0001
					String oldVersionCodeInfo = versionCodeInfo.substring(3+lenBranchNo,11+lenBranchNo)+versionCodeInfo.substring(12+lenBranchNo,16+lenBranchNo);
					AppLogger.info("数据库查询截取的数据库版本号为：" + oldVersionCodeInfo);
					String newVersionCodeInfo = versionCode.substring(3+lenBranchNo,11+lenBranchNo)+versionCode.substring(12+lenBranchNo,16+lenBranchNo);
					AppLogger.info("本次上传的版本号为：" + newVersionCodeInfo);
					if (Long.parseLong(oldVersionCodeInfo) > Long.parseLong(newVersionCodeInfo)) {
						return new TCResult(0, ErrorCode.REMOTE, "文件版本号太低，请上传新的版本文件!");
					}
				}
			} else {
				return new TCResult(0, ErrorCode.REMOTE, "查询版本信息异常");
			}
		}else{
			return new TCResult(0, ErrorCode.REMOTE, "该版本号已存在");
		}
		return TCResult.newSuccessResult();
	}
	
	
	/**
	 * @category 版本文件上传
	 * @param versionCode
	 *            入参|版本号| {@link String}
	 * @param strategy_Id
	 *            入参|策略ID| {@link String}
	 * @param description
	 *            入参|描述信息| {@link String}
	 * @param uploadFileName
	 *            入参|文件名| {@link String}
	 * @param urlAddress
	 *            入参|文件存贮路径| {@link String}
	 * @param branchId
	 *            入参|机构ID| {@link String}
	 * @param addFlag
	 *            入参|增量版本标志| {@link String}
	 * @return 0 上传失败<br/>
	 *         1上传 成功<br/>
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "版本号", type = java.lang.String.class),
			@Param(name = "strategy_Id", comment = "策略ID", type = java.lang.String.class),
			@Param(name = "description", comment = "描述信息", type = java.lang.String.class),
			@Param(name = "uploadFileName", comment = "文件名", type = java.lang.String.class),
			@Param(name = "urlAddress", comment = "文件存贮路径", type = java.lang.String.class),
			@Param(name = "branchId", comment = "机构ID", type = java.lang.String.class),
			@Param(name = "addFlag", comment = "增量版本标志", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "上传成功"),
			@Return(id = "1", desp = "上传失败") })
	@Component(label = "版本文件上传", style = "判断型", type = "同步组件", comment = "版本文件上传", author = "AlphaLi", date = "2018-05-07 07:13:02")
	
	public static TCResult uploadVersionFile(String versionCode, String strategy_Id,
			String description, String uploadFileName, String urlAddress, String branchId, String addFlag) {
		try {
			if (uploadFileName == null) {
				return new TCResult(0, ErrorCode.REMOTE, "请上传版本文件！");
			}
			if (description == null || description.isEmpty() || description == "") {
				description = "备注未填";
			}
			
			String VersionAttr = "";
			if ("N".equals(addFlag)) { //全量版本
				VersionAttr = "1";
			}else if ("Y".equals(addFlag)) { //增量版本
				VersionAttr = "2";
			} else {
				return new TCResult(0, ErrorCode.REMOTE, "增量版本标志为空，只能是Y或N");
			}
			
			//文件分隔符
			String file_separator = System.getProperty("file.separator");
			
			//根据机构ID获取操作机构号
			String getBranchType = "select FATHERBRANCHID,BRANCHNO from AUMS_BRANCHINFO WHERE BRANCHID='"+ branchId +"'";
			String BranchNo = "";
			TCResult BranchQueryResult = P_Jdbc.dmlSelect(null, getBranchType.toString(), -1);
			if (BranchQueryResult==null || BranchQueryResult.getStatus()==2) {
				return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常！");
			} else {
				JavaList BranchRst = (JavaList)BranchQueryResult.getOutputParams().get(1);
				BranchNo = (String) BranchRst.getListItem(0).get(1);
			}
			String OperBranchNo = BranchNo;
			
			//获取版本类型 1-总行 2-分行
			String VersionType;
			if (A_VersionApiController.isMajorFile(uploadFileName)) {
				VersionType = "1";
			} else {
				VersionType = "2";
			}

			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(now);
			
			//总行版本文件
			boolean mainBranchFlag = false;
			if (VersionType=="1") {
				mainBranchFlag = true;
			}
			
			//截取文件的使用机构（此处逻辑过于依赖文件名称规则，后续如果修改，需要重新编写）
			String UseBranchNo = BranchNo;
			if (!A_VersionApiController.isMajorFile(uploadFileName)) {//分行版本
				UseBranchNo=uploadFileName.substring(findStrIndex(uploadFileName, 1, "_")+1, findStrIndex(uploadFileName, 2, "_"));
			} else {
				if (!A_VersionApiController.isNationalFile(uploadFileName)) {//总行开发的分行版本
					UseBranchNo=uploadFileName.substring(findStrIndex(uploadFileName, 2, "_")+1, findStrIndex(uploadFileName, 3, "_"));
				}
			}
			
			// 总行上传文件名称                  V_20181201_0001.zip          "V_"+ 日期  +"_"+ 编号 +".zip"
			// 总行上传的分行文件名称   V_20181201_10001_0001.zip    "V_"+ 日期  +"_"+ 分行机构号 +"_"+ 编号 +".zip"
			// 分行上传的文件名                  D_10001_20181201_0001.zip    "D_"+ 分行机构号  +"_"+ 日期 +"_"+ 编号 +".zip"
			
			//版本号检查规则
			Pattern versionCodeChk = Pattern.compile("^V\\_[0-9]{8}\\_[0-9]{4}");
			
			//总行文件名和分行文件名检查规则
			String fileNameStr = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
			// 分行特色版本文件名规则
			if (mainBranchFlag) {
				//主版本文件名规则
				Pattern mainFileNameChk = Pattern.compile("^V\\_[0-9]{8}\\_[0-9]{4}");
				// 总行上传的分行版本文件名规则
				Pattern depFileNameChk  = Pattern.compile("^V\\_[0-9]{8}\\_[0-9]{4,12}\\_[0-9]{4}");
				//版本号检查
				Matcher versionchk = versionCodeChk.matcher(versionCode);
				if (!versionchk.matches()) {
					return new TCResult(0, ErrorCode.REMOTE, "版本号输入不合法!");
				}
				//获取文件名及检查
				
				Matcher matchPar = mainFileNameChk.matcher(fileNameStr);
				Matcher matchDep = depFileNameChk.matcher(fileNameStr);
				if (!matchPar.matches() && !matchDep.matches()) {
					return new TCResult(0, ErrorCode.REMOTE, "版本文件名规则不合法!");
				}
			} else {
				Pattern spcFileNameChk  = Pattern.compile("^D\\_[0-9]{4,12}\\_[0-9]{8}\\_[0-9]{4}");
				//版本号检查
				Matcher versionchk = spcFileNameChk.matcher(versionCode);
				if (!versionchk.matches()) {
					return new TCResult(0, ErrorCode.REMOTE, "分行版本文件名规则不合法!");
				}
			}
			
			// 查询主版本是否登记
			String fileExistsChk = "select VERSIONID,DESCRIPTION,STRATEGY_ID from aums_ver_info where versionCode='" + versionCode + "'";
			TCResult FileExistsQueryResult = P_Jdbc.dmlSelect(null, fileExistsChk, -1);
			
			if (FileExistsQueryResult == null) {
				return new TCResult(0, ErrorCode.REMOTE, "查询版本信息异常");
			}else {
				if(FileExistsQueryResult.getStatus() == 2){
					//登记主版本信息、扩展版本信息、文件信息，并解压文件
					//获取uuid
					String uuid = UUID.randomUUID().toString();
					StringBuffer versionInsert = new StringBuffer(
							"insert into AUMS_VER_INFO values('" + uuid + "',");
					versionInsert.append("'" + versionCode + "'" + ",");
					versionInsert.append("'" + description + "'" + ",");
					versionInsert.append("'" + VersionType + "'" + ",");
					versionInsert.append("'" + date + "',");
					versionInsert.append("'" + strategy_Id + "','','')");
					
					String zipfileid = UUID.randomUUID().toString();
					StringBuffer versionExtInsert = new StringBuffer(
							"insert into AUMS_VER_INFO_EXT values('" + zipfileid + "',");
					versionExtInsert.append("'" + uploadFileName + "'" + ",");
					versionExtInsert.append("'" + strategy_Id + "'" + ",");
					versionExtInsert.append("'" + urlAddress + "'" + ",");
					versionExtInsert.append("'" + description + "'" + ",");
					versionExtInsert.append("'" + uuid + "',");
					versionExtInsert.append("'" + OperBranchNo + "',");
					versionExtInsert.append("'" + UseBranchNo + "',");
					versionExtInsert.append("'" + VersionAttr + "',");
					versionExtInsert.append("'" + date + "','','')");
					try {
						//插入数据
						P_Jdbc.executeSQL(null, versionInsert.toString(), false);
						P_Jdbc.executeSQL(null, versionExtInsert.toString(), false);
						
						String selectUpackPath = "";
						if (mainBranchFlag) {
							selectUpackPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='Version_Manage' AND tpi.TRANSCODE='version' AND tpi.PARAMKEYNAME='versionUnpackPath'";
						} else {
							selectUpackPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='Version_Manage' AND tpi.TRANSCODE='version' AND tpi.PARAMKEYNAME='branchVersionUnpackPath'";
						}
						JavaList unpackPathInfoList = (JavaList) P_Jdbc.dmlSelect(null, selectUpackPath, -1).getOutputParams().get(1);
						//数据库参数配置为基础的解压路径
						String upackPath = unpackPathInfoList.getListItem(0).get(0).toString();
						//实际解压路径为基础解压路径+版本号，因为总行需要解决一个版本含多个压缩文件的情况
						upackPath = upackPath + versionCode + file_separator;
						AppLogger.info("解压文件路径为：【" + upackPath + "】");
						//判断文件夹是否存在并创建解压路径
						BasicUtilTool.isHaveDirs(upackPath);
					    AppLogger.info("创建zipFile成功!!!");
						unpackZip.unZipFiles( new File(urlAddress), upackPath);
						AppLogger.info("解压文件成功!!!");
						//BasicUtilTool.createVersionJsonFile(versionCode, strategy_Id, description,uuid, upackPath);
						BasicUtilTool.createVersionJsonFile(fileNameStr, strategy_Id, description,uuid, upackPath);
						AppLogger.info("创建JSON成功!!!");
						//List<VersiondetailinfoVo> fileInfo = ReadFile.getFileInfo(upackPath + versionCode, uuid);
						List<VersiondetailinfoVo> fileInfo = ReadFile.getFileInfo(upackPath + fileNameStr, versionCode);
						AppLogger.info("读取总数目数" + fileInfo.size());
						long savestarTime = System.currentTimeMillis();
						
						for (VersiondetailinfoVo versionFileInfo : fileInfo) {
							//AppLogger.info("path"+versionFileInfo.getPATH());
							String uuidDetailInfo = UUID.randomUUID().toString();
							String path = versionFileInfo.getPATH();
							StringBuffer versionDetailSb = new StringBuffer(
									"insert into aums_ver_detailinfo_main  VALUES('" + uuidDetailInfo + "',");
							versionDetailSb.append("'" + versionFileInfo.getFILENAME()+ "'" + ",");
							versionDetailSb.append("'" + path + "'"+ ",");
							versionDetailSb.append("'" + versionFileInfo.getTYPE() + "'"+ ",");
							versionDetailSb.append("'" + versionFileInfo.getMD5() + "'"+ ",");
							versionDetailSb.append("'" + zipfileid + "'" + ",");
							versionDetailSb.append("'" + date + "',");
							versionDetailSb.append("'" + versionFileInfo.getFILESIZE() + "',");
							versionDetailSb.append("'" + uploadFileName + "',");
							versionDetailSb.append("'" + path.substring(findStrIndex(path,4,file_separator)+1) + "')");
							P_Jdbc.executeSQL(null, versionDetailSb.toString(), false);
						}
						P_Jdbc.commit(null);
						long saveendTime = System.currentTimeMillis();
						AppLogger.info("存库时间:【" + String.valueOf(saveendTime - savestarTime) + "】ms");
					} catch (Exception e) {
						AppLogger.error(e);
						P_Jdbc.rollBack(null);
						return new TCResult(0, ErrorCode.REMOTE, "版本文件上传异常");
					}
				}else if (FileExistsQueryResult.getStatus() == 1) {
					//主版本已经存在，查询文件版本
					if (!mainBranchFlag) {//如果是分行提交的，查询到记录代表此版本已经上传
						return new TCResult(0, ErrorCode.REMOTE, "版本【"+ versionCode +"】对应的文件【"+ uploadFileName +"】已上传成功，请不要重复上传");
					} 
					JavaList VersionInfoList = (JavaList) FileExistsQueryResult.getOutputParams().get(1);
					String VersionId = VersionInfoList.getListItem(0).get(0).toString();
					String selectVersionExt = "select 1 from AUMS_VER_INFO_EXT ext where ext.VERSIONID ='"+ VersionId +"' AND ext.ZIPFILENAME='"+ uploadFileName +"' AND ext.STRATEGY_ID='"+ strategy_Id +"'";
					TCResult selectVersionExtRst = P_Jdbc.dmlSelect(null, selectVersionExt, -1);
					if (selectVersionExtRst == null) {
						return new TCResult(0, ErrorCode.REMOTE, "查询版本对应的文件信息异常");
					}else {
						if(selectVersionExtRst.getStatus() == 2){
							//上传文件并解压处理
							String zipfileid = UUID.randomUUID().toString();
							StringBuffer versionExtInsert = new StringBuffer(
									"insert into AUMS_VER_INFO_EXT values('" + zipfileid + "',");
							versionExtInsert.append("'" + uploadFileName + "'" + ",");
							versionExtInsert.append("'" + strategy_Id + "'" + ",");
							versionExtInsert.append("'" + urlAddress + "'" + ",");
							versionExtInsert.append("'" + description + "'" + ",");
							versionExtInsert.append("'" + VersionId + "',");
							versionExtInsert.append("'" + OperBranchNo + "',");
							versionExtInsert.append("'" + UseBranchNo + "',");
							versionExtInsert.append("'" + VersionAttr + "',");
							versionExtInsert.append("'" + date + "','','')");
							try {
								//插入数据
								P_Jdbc.executeSQL(null, versionExtInsert.toString(), false);
								
								String selectUpackPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='Version_Manage' AND tpi.TRANSCODE='version' AND tpi.PARAMKEYNAME='versionUnpackPath'";
								JavaList unpackPathInfoList = (JavaList) P_Jdbc.dmlSelect(null, selectUpackPath, -1).getOutputParams().get(1);
								//数据库参数配置为基础的解压路径
								String upackPath = unpackPathInfoList.getListItem(0).get(0).toString();
								//实际解压路径为基础解压路径+版本号，因为总行需要解决一个版本含多个压缩文件的情况
								upackPath = upackPath + versionCode + file_separator;
								AppLogger.info("解压文件路径为：【" + upackPath + "】");
								//判断文件夹是否存在并创建解压路径
								BasicUtilTool.isHaveDirs(upackPath);
							    AppLogger.info("创建zipFile成功!!!");
								unpackZip.unZipFiles( new File(urlAddress), upackPath);
								AppLogger.info("解压文件成功!!!");
								//BasicUtilTool.createVersionJsonFile(versionCode, strategy_Id, description,uuid, upackPath);
								BasicUtilTool.createVersionJsonFile(fileNameStr, strategy_Id, description,VersionId, upackPath);
								AppLogger.info("创建JSON成功!!!");
								//List<VersiondetailinfoVo> fileInfo = ReadFile.getFileInfo(upackPath + versionCode, uuid);
								List<VersiondetailinfoVo> fileInfo = ReadFile.getFileInfo(upackPath + fileNameStr, versionCode);
								AppLogger.info("读取总数目数" + fileInfo.size());
								long savestarTime = System.currentTimeMillis();
								
								for (VersiondetailinfoVo versionFileInfo : fileInfo) {
									//AppLogger.info("path"+versionFileInfo.getPATH());
									String uuidDetailInfo = UUID.randomUUID().toString();
									String path = versionFileInfo.getPATH();
									StringBuffer versionDetailSb = new StringBuffer(
											"insert into aums_ver_detailinfo_main  VALUES('" + uuidDetailInfo + "',");
									versionDetailSb.append("'" + versionFileInfo.getFILENAME()+ "'" + ",");
									versionDetailSb.append("'" + path + "'"+ ",");
									versionDetailSb.append("'" + versionFileInfo.getTYPE() + "'"+ ",");
									versionDetailSb.append("'" + versionFileInfo.getMD5() + "'"+ ",");
									versionDetailSb.append("'" + zipfileid + "'" + ",");
									versionDetailSb.append("'" + date + "',");
									versionDetailSb.append("'" + versionFileInfo.getFILESIZE() + "',");
									versionDetailSb.append("'" + uploadFileName + "',");
									versionDetailSb.append("'" + path.substring(findStrIndex(path,4,file_separator)+1) + "')");
									P_Jdbc.executeSQL(null, versionDetailSb.toString(), false);
								}
								P_Jdbc.commit(null);
								long saveendTime = System.currentTimeMillis();
								AppLogger.info("存库时间:【" + String.valueOf(saveendTime - savestarTime) + "】ms");
							} catch (Exception e) {
								AppLogger.error(e);
								P_Jdbc.rollBack(null);
								return new TCResult(0, ErrorCode.REMOTE, "版本文件上传异常");
							}
						}else if (selectVersionExtRst.getStatus() == 1) {
							return new TCResult(0, ErrorCode.REMOTE, "版本【"+ versionCode +"】对应的文件【"+ uploadFileName +"】已上传成功，请不要重复上传");
						}else {
							return new TCResult(0, ErrorCode.REMOTE, "查询版本对应的文件信息失败");
						}
					}
				}else {
					return new TCResult(0, ErrorCode.REMOTE, "查询版本信息返回失败");
				}
			}
		} catch (Exception e) {
			AppLogger.error(e);
			return new TCResult(0, ErrorCode.REMOTE, "上传版本文件异常");
		}
		return TCResult.newSuccessResult();
	}
	
	 
	
	/**
	 * @category 文件信息广播到分行
	 * @param uploadFileName
	 *            入参|文件名| {@link String}
	 * @param urlAddress
	 *            入参|文件存贮路径| {@link String}
	 * @param branchId
	 *            入参|机构ID| {@link String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "uploadFileName", comment = "文件名", type = java.lang.String.class),
			@Param(name = "urlAddress", comment = "文件存贮路径", type = java.lang.String.class),
			@Param(name = "branchId", comment = "机构ID", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "成功"),
			@Return(id = "1", desp = "失败") })
	@Component(label = "文件信息广播到分行", style = "判断型", type = "同步组件", comment = "文件信息广播到分行", author = "AlphaLi", date = "2018-05-07 07:13:02")
	
	public static TCResult notifytobranch(String uploadFileName, String urlAddress, String branchId) {
		//查询基础参数获取超时时间
		String selectTimeOutParma = "select PARAMVALUE from tp_cip_sysparameters where MODULECODE ='Version_Manage' AND TRANSCODE='version' AND PARAMKEYNAME='ServerFailWaitTime'";
		JavaList TimeOutParmaList = (JavaList) P_Jdbc.dmlSelect(null, selectTimeOutParma, -1).getOutputParams().get(1);
		String TimeOutParma = TimeOutParmaList.getListItem(0).get(0).toString();
		AppLogger.info("修改服务器状态为失败的间隔时间：【" + TimeOutParma + "】秒");
		
		//将所有服务器超时时间以外未收到心跳的状态更新为不可用
		TCResult TR = P_Time.getFormatTime("yyyy-MM-dd HH:mm:ss");
		String nowdatetime = (String)TR.getOutputParams().get(0);
		AppLogger.info("nowdatetime=="+nowdatetime);
		String UpdateServerStatus = "update AUMS_VER_DEPSERVERINFO set SERVERSTATUS='0' where ceil(((To_date('"+nowdatetime+"' , 'yyyy-mm-dd hh24-mi-ss') - To_date(UPDATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60 * 60 )>"+TimeOutParma;
		try {
			//批量更新
			P_Jdbc.executeSQL(null, UpdateServerStatus.toString(), true);
		} catch (Exception e) {
			AppLogger.info(e);
			return new TCResult(0, ErrorCode.REMOTE, "批量更新服务器状态异常");
		}
		
		//将所有分行服务器列表新增到文件推送状态表
		String BranchServerInsert = "insert into AUMS_VER_FILETOBRANCH select '"+ uploadFileName +"',IP,BRANCHNO,'0', '"+ nowdatetime +"',null,null from AUMS_VER_DEPSERVERINFO";
		try {
			//插入数据
			P_Jdbc.executeSQL(null, BranchServerInsert.toString(), true);
		} catch (Exception e) {
			AppLogger.info(e);
			return new TCResult(0, ErrorCode.REMOTE, "插入分行服务器数据异常");
		}
				
		//查询基础参数
		String getInitPar = "select IP,PORT,TIMEOUT,USERNAME,PASSWD,BRANCHPATH,BRANCHZIPPATH,REMARK1,REMARK2 from AUMS_VER_NOTIFYINFO";
		String IP;
		String PORT;
		String TIMEOUT;
		String USERNAME;
		String PASSWD;
		String BRANCHPATH;
		String BRANCHZIPPATH;
		TCResult InitParResult = P_Jdbc.dmlSelect(null, getInitPar.toString(), -1);
		if (InitParResult==null || InitParResult.getStatus()==2) {
			return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常！");
		} else {
			JavaList InitParRst = (JavaList)InitParResult.getOutputParams().get(1);
			IP = InitParRst.getListItem(0).get(0).toString();
			PORT = InitParRst.getListItem(0).get(1).toString();
			TIMEOUT = InitParRst.getListItem(0).get(2).toString();
			USERNAME = InitParRst.getListItem(0).get(3).toString();
			PASSWD = InitParRst.getListItem(0).get(4).toString();
			BRANCHPATH = InitParRst.getListItem(0).get(5).toString();
			BRANCHZIPPATH = InitParRst.getListItem(0).get(6).toString();
			if (uploadFileName.startsWith("V_")) {//是否总行文件,判断规则后续完善
				BRANCHPATH = BRANCHPATH+File.separator+"major";
				BRANCHZIPPATH = BRANCHZIPPATH+File.separator+"major";
			} else {
				BRANCHPATH = BRANCHPATH+File.separator+"department";
				BRANCHZIPPATH = BRANCHZIPPATH+File.separator+"department";
			}
		}
		
		//拼接http报文
		JSONObject object = new JSONObject();
		object.put("serverIp", IP);
		object.put("serverPort", PORT);
		object.put("timeOut", TIMEOUT);
		object.put("userName", USERNAME);
		object.put("passWd", PASSWD);
		object.put("localFilePath", BRANCHPATH);
		object.put("localZipFilePath", BRANCHZIPPATH);
		object.put("serverFilePath", urlAddress);
		object.put("fileName", uploadFileName);
		
		String url;
		int conntimeout =-1;
		int rsptimeout = 1;
		//获取需要推送的服务器地址，全国版本所有服务器均推送，分行版本只推送到对应分行的服务器
		String getBranchGroup;
		if (A_VersionApiController.isNationalFile(uploadFileName)) {
			getBranchGroup = "select IP,BRANCHNO,SERVERSTATUS,UPDATETIME from AUMS_VER_DEPSERVERINFO";
		}else {
			String brno;
			if (A_VersionApiController.isMajorFile(uploadFileName)) {//总行开发的分行定制版本
				brno=uploadFileName.substring(findStrIndex(uploadFileName, 2, "_")+1, findStrIndex(uploadFileName, 3, "_"));
			} else {//分行自主开发的特色版本
				brno=uploadFileName.substring(findStrIndex(uploadFileName, 1, "_")+1, findStrIndex(uploadFileName, 2, "_"));
			}
			getBranchGroup = "select IP,BRANCHNO,SERVERSTATUS,UPDATETIME from AUMS_VER_DEPSERVERINFO where BRANCHNO = '"+ brno +"'";
		}
		
		TCResult BranchGroupResult = P_Jdbc.dmlSelect(null, getBranchGroup.toString(), -1);
		if (BranchGroupResult==null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常！");
		}else if(BranchGroupResult.getStatus()==2){
			AppLogger.info("无目标机构配置，文件不推送");
			return TCResult.newSuccessResult();
		} 
		else {
			JavaList BranchGroupList = (JavaList)BranchGroupResult.getOutputParams().get(1);
			//循环处理每一个分行
			for (int i = 0; i < BranchGroupList.size(); i++) {
				String depip = BranchGroupList.getListItem(i).get(0).toString();
				object.put("mainIp", depip);
				url = "http://"+depip+":16669/FileServer/RcvMainNotice";
				//发送通知
				try {
					AppLogger.info("发送的HTTP请求报文是===="+object.toJSONString());
					P_HttpClient.doPost(url, null, object.toJSONString(), conntimeout, rsptimeout, "UTF-8");
				} catch (Exception e) {
					AppLogger.info(e);
				}
			}
		}
		return TCResult.newSuccessResult();
			
			
			
			/*
			//此部分先屏蔽，采用总行通知分行每一台服务器的模式
			String mainIp = "";
			String branchtemp = "";
			JavaList jl = new JavaList();
			boolean succflag = false;
			for (int i = 0; i < BranchGroupList.size(); i++) {
				String depip = BranchGroupList.getListItem(i).get(0).toString();
				String branchno = BranchGroupList.getListItem(i).get(1).toString();
				String serversts = BranchGroupList.getListItem(i).get(2).toString();
				if (branchtemp.equals("")) { //处理的第一条数据
					if (serversts.equals("1")) { //服务器状态正常
						object.put("mainIp", depip);
						mainIp = depip;
						succflag = true;
					}else {
						jl.add(depip);
					}
					branchtemp = branchno;
				} else {
					if (branchtemp.equals(branchno)) {//同一分行的其他机器
						if (succflag) {//判断当前分行是否已经有一台成功的机器,如果有，则将当前的都加入到其他列表里
							jl.add(depip);
						} else {//当前分行还没有状态正常的服务器
							if (serversts.equals("1")) { //服务器状态是否正常
								object.put("mainIp", depip);
								mainIp = depip;
								succflag = true;
							}else {
								jl.add(depip);
							}
						}
					} else {//其他分行循环开始
						object.put("otherList", jl);
						//先将上一批次的进行发送通知
						url = "http://"+mainIp+":16669/FileServer/RcvMainNotice";
						//发送通知
						try {
							P_HttpClient.doPost(url, null, object.toJSONString(), conntimeout, rsptimeout, "UTF-8");
						} catch (Exception e) {
							AppLogger.info(e);
						}

						//处理新的批次
						//清空List缓存
						succflag = false;
						jl.clear();
						if (serversts.equals("1")) { //服务器状态正常
							object.put("mainIp", depip);
							mainIp = depip;
							succflag = true;
						}else {
							jl.add(depip);
						}
					}
					branchtemp = branchno;
				}
				
			}
			//for循环结束后，如果有最后一个批次，则发送通知
			if (succflag) {
				object.put("otherList", jl);
				url = "http://"+mainIp+":16669/FileServer/RcvMainNotice";
				//发送通知
				try {
					P_HttpClient.doPost(url, null, object.toJSONString(), conntimeout, rsptimeout, "UTF-8");
				} catch (Exception e) {
					AppLogger.info(e);
				}
			}*/
	}
	
	
	/**
	 * @category 广播失败重试
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 * @throws BusException
	 */
	@InParams(param = { })
	@Returns(returns = { @Return(id = "0", desp = "成功"),
			@Return(id = "1", desp = "失败") })
	@Component(label = "广播失败重试", style = "判断型", type = "同步组件", comment = "广播失败重试", author = "AlphaLi", date = "2018-05-07 07:13:02")
	
	public static TCResult notifyFailReSend() {
		//查询基础参数获取超时时间
		String selectTimeOutParma = "select PARAMVALUE from tp_cip_sysparameters where MODULECODE ='Version_Manage' AND TRANSCODE='version' AND PARAMKEYNAME='FailReSendWaitTime'";
		TCResult TCRst = P_Jdbc.dmlSelect(null, selectTimeOutParma, -1);
		if (TCRst == null || TCRst.getStatus() == 2) {
			return new TCResult(0, ErrorCode.REMOTE, "查询基础参数失败");
		}
		JavaList TimeOutParmaList = (JavaList) TCRst.getOutputParams().get(1);
		String TimeOutParma = TimeOutParmaList.getListItem(0).get(0).toString();
		AppLogger.info("文件失败重试间隔为：【" + TimeOutParma + "】秒");
		
		//将所有服务器1小时内未收到心跳的状态更新为不可用
		TCResult TR = P_Time.getFormatTime("yyyy-MM-dd HH:mm:ss");
		String nowdatetime = (String)TR.getOutputParams().get(0);
		AppLogger.info("当前时间戳为：【"+nowdatetime+ "】秒");
		//获取重试间隔以外，并且状态为失败的记录
		String SelFailStatus = "select FILENAME,IP,BRANCHNO from AUMS_VER_FILETOBRANCH where FILESTATUS='0' and ceil(((To_date('"+nowdatetime+"' , 'yyyy-mm-dd hh24-mi-ss') - To_date(UPDATETIME , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60 * 60)>"+TimeOutParma;
		TCResult TCRst1 = P_Jdbc.dmlSelect(null, SelFailStatus, -1);
		if (TCRst1 == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询需要通知的明细信息失败");
		}else if (TCRst1.getStatus() == 2) {
			AppLogger.info("无失败数据需要处理");
			return TCResult.newSuccessResult();
		}
		JavaList SelFailStatusList = (JavaList) TCRst1.getOutputParams().get(1);
		
		//查询通知基础信息
		String selectNotifyBasicInfo= "select IP,PORT,USERNAME,PASSWD,TIMEOUT,BRANCHZIPPATH,BRANCHPATH from AUMS_VER_NOTIFYINFO";
		TCResult TCRst2 = P_Jdbc.dmlSelect(null, selectNotifyBasicInfo, -1);
		if (TCRst2 == null || TCRst2.getStatus() == 2) {
			return new TCResult(0, ErrorCode.REMOTE, "查询通知公共参数失败");
		}
		JavaList selectNotifyBasicInfoList = (JavaList) TCRst2.getOutputParams().get(1);

		String IP_INFO = selectNotifyBasicInfoList.getListItem(0).get(0).toString();
		String PORT_INFO = selectNotifyBasicInfoList.getListItem(0).get(1).toString();
		String USERNAME_INFO = selectNotifyBasicInfoList.getListItem(0).get(2).toString();
		String PASSWD_INFO = selectNotifyBasicInfoList.getListItem(0).get(3).toString();
		String TIMEOUT_INFO = selectNotifyBasicInfoList.getListItem(0).get(4).toString();
		String BRANCHZIPPATH_INFO = selectNotifyBasicInfoList.getListItem(0).get(5).toString();
		String BRANCHPATH_INFO = selectNotifyBasicInfoList.getListItem(0).get(6).toString();
		
		//init
		JSONObject object = new JSONObject();
		object.put("serverIp", IP_INFO);
		object.put("serverPort", PORT_INFO);
		object.put("userName", USERNAME_INFO);
		object.put("passWd", PASSWD_INFO);
		object.put("timeOut", TIMEOUT_INFO);
		
		String fileName;
		String serverIp;
		String url;
		//循环处理每一笔数据
		for (int i = 0; i < SelFailStatusList.size(); i++) {
			AppLogger.info("======开始处理第"+ i +"笔"+"=====");
			fileName = TimeOutParmaList.getListItem(i).get(0).toString();
			serverIp = TimeOutParmaList.getListItem(i).get(1).toString();
			//查询文件在总行的路径
			String selectFilePath= "select ZIPFILEPATH from AUMS_VER_INFO_EXT where ZIPFILENAME ='"+ fileName +"'";
			TCResult TCRst3 = P_Jdbc.dmlSelect(null, selectFilePath, -1);
			if (TCRst3 == null || TCRst3.getStatus() == 2) {
				return new TCResult(0, ErrorCode.REMOTE, "查询文件扩展信息失败");
			}
			JavaList selectFilePathList = (JavaList) TCRst3.getOutputParams().get(1);
			String FilePath = selectFilePathList.getListItem(0).get(0).toString();
			AppLogger.info("总行文件路径为：【" + FilePath + "】");
			
			//判断文件是否分行
			if (A_VersionApiController.isMajorFile(fileName)) {
				BRANCHZIPPATH_INFO = BRANCHZIPPATH_INFO + File.separator + "major" + File.separator;
				BRANCHPATH_INFO = BRANCHZIPPATH_INFO + File.separator + "major" + File.separator;
			} else {
				BRANCHZIPPATH_INFO = BRANCHZIPPATH_INFO + File.separator + "department" + File.separator;
				BRANCHPATH_INFO = BRANCHZIPPATH_INFO + File.separator + "department" + File.separator;
			}
			
			//报文明细处理
			object.put("fileName", fileName);
			object.put("mainIp", serverIp);
			object.put("localZipFilePath", BRANCHZIPPATH_INFO);
			object.put("localFilePath", BRANCHPATH_INFO);
			object.put("serverFilePath", FilePath);
			url = "http://"+serverIp+":16669/FileServer/RcvMainNotice";
			//发送通知
			try {
				AppLogger.info("发送的HTTP请求报文是====【"+object.toJSONString()+"】");
				P_HttpClient.doPost(url, null, object.toJSONString(), -1, 1, "UTF-8");
			} catch (Exception e) {
				AppLogger.info(e);
			}
		}

		return TCResult.newSuccessResult();
	
	}

	/**
	 * @category 按规则发布版本
	 * @param versioncode
	 *            入参|版本号| {@link String}
	 * @param BranchNoList
	 *            入参|机构号列表| {@link JavaList}
	 * @param devidList
	 *            入参|机具ID列表| {@link JavaList}
	 * @param mainVersionCode
	 *            入参|主版本号| {@link JavaList}
	 * @param branchNo
	 * 			    入参|操作机构号|{@link String}
	 * @param remark
	 * 			    入参|发版备注|{@link String}
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "关联版本号",  type = String.class),
			@Param(name = "BranchNoList", comment = "机构号列表", type = JavaList.class),
			@Param(name = "devidList", comment = "机具ID列表", type = JavaList.class),
			@Param(name = "mainVersionCode", comment = "主版本号",  type = String.class),
			@Param(name = "branchNo", comment = "操作机构号", type = String.class),
			@Param(name = "remark", comment = "发版备注", type = String.class)})
	@Returns(returns = { @Return(id = "0", desp = "按规则发布版本成功"),
			@Return(id = "1", desp = "按规则发布版本失败") })
	@Component(label = "按规则发布版本", comment = "按规则发布版本", date = "2018-12-27 00:00:00")
	public static TCResult A_ReleaseVersionByRule(String versionCode,JavaList BranchNoList, 
			JavaList devidList ,String mainVersionCode ,String branchNo ,String remark) throws Exception {
		
		try {
			//获取发版序号，用来标识版本发布顺序
			AppLogger.info("============Start===============");
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
				return new TCResult(0, ErrorCode.REMOTE, "暂不支持此类型数据库:【"+dbType+"】");
			}
			//初始化时间戳
			Date nowStartTime = new Date();
			SimpleDateFormat sdfStartTime = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat isoStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String Time14 = sdfStartTime.format(nowStartTime);
			String Time19 = isoStartTime.format(nowStartTime);
			
			//mainVersionCode处理
			if (mainVersionCode==null) {
				mainVersionCode = "";
			}
			
			//根据机构号获取版本类型1-总行，2-分行
			String getBranchType = "select FATHERBRANCHID from AUMS_BRANCHINFO WHERE BRANCHNO='"+ branchNo +"'";
			String FatherBranch = "";
			TCResult BranchQueryResult = P_Jdbc.dmlSelect(null, getBranchType.toString(), -1);
			if (BranchQueryResult==null || BranchQueryResult.getStatus()==2) {
				return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常！");
			} else {
				JavaList BranchRst = (JavaList)BranchQueryResult.getOutputParams().get(1);
				FatherBranch = (String) BranchRst.getListItem(0).get(0);
			}
			String VersionType = FatherBranch.equals("adminBranch") ? "1" : "2";
			
			//根据版本号获取版本ID
			String getBranchByVC = "select VERSIONID,VERSIONCODE,DESCRIPTION,STRATEGY_ID from AUMS_VER_INFO WHERE VERSIONCODE='"+ versionCode +"'";
			String VersionId = "";
			String VersionDesc = "";
			String VersionStrId = "";
			TCResult VersionIdVerResult = P_Jdbc.dmlSelect(null, getBranchByVC.toString(), -1);
			if (VersionIdVerResult==null || VersionIdVerResult.getStatus()==2) {
				return new TCResult(0, ErrorCode.REMOTE, "未找到版本号对应的版本信息！");
			} else {
				JavaList versionList = (JavaList)VersionIdVerResult.getOutputParams().get(1);
				VersionId = (String) versionList.getListItem(0).get(0);
				VersionDesc = (String) versionList.getListItem(0).get(2);
				VersionStrId = (String) versionList.getListItem(0).get(3);
			}
			if (VersionId.equals("")) {
				return new TCResult(0, ErrorCode.REMOTE, "版本ID信息不正确");
			}
			
			
			//是否试点版本
			boolean isTest = false;
			String testflag;
			String uuid;

			//登记版本发布规则表
			uuid = UUID.randomUUID().toString();
			//判断所选机构是否为空,不为空则登记信息
			if (BranchNoList != null && !BranchNoList.isEmpty() && BranchNoList.size()>0) {
				isTest = true;
				for (int i=0;i<BranchNoList.size();i++) {
					String brno_i = BranchNoList.getStringItem(i);
					StringBuffer BranchInsert = new StringBuffer(
							"insert into AUMS_VER_RELEASERULEBYBRNO VALUES('"+ uuid +"',");
					BranchInsert.append("'" + brno_i + "'" + ",");
					BranchInsert.append("'" + Time14 + "'" + ",null)");
					P_Jdbc.executeSQL(null, BranchInsert.toString(), false);
				}
			}
			//判断所选终端是否为空,不为空则登记信息
			if (devidList != null && !devidList.isEmpty() && devidList.size()>0) {
				isTest = true;
				for (int i=0;i<devidList.size();i++) {
					String device_i = devidList.getStringItem(i);
					String getDevNanme = "select DEVUNIQUEID from AUMS_DEV_INFO WHERE DEVID='"+ device_i +"'";
					TCResult GetDevNanmeResult = P_Jdbc.dmlSelect(null, getDevNanme.toString(), -1);
					if (GetDevNanmeResult==null || GetDevNanmeResult.getStatus()==2) {
						return new TCResult(0, ErrorCode.REMOTE, "未找到设备【"+ device_i +"】");
					} else {
						JavaList DevNameList = (JavaList)GetDevNanmeResult.getOutputParams().get(1);
						String DevNm = DevNameList.getListItem(0).get(0).toString();
						StringBuffer DeviceInsert = new StringBuffer(
								"insert into AUMS_VER_RELEASERULEBYDEV VALUES('"+ uuid +"',");
						DeviceInsert.append("'" + DevNm + "'" + ",");
						DeviceInsert.append("'" + Time14 + "'" + ",null)");
						P_Jdbc.executeSQL(null, DeviceInsert.toString(), false);
					}
				}
			}
			if (isTest) {
				testflag = "2";
			} else {
				testflag = "1";
			}
			
			StringBuffer RuleInsert = new StringBuffer(
					"insert into AUMS_VER_RELEASERULE VALUES('" + uuid + "',");
			RuleInsert.append("'" + VersionId + "'" + ",");
			RuleInsert.append("'" + versionCode + "'" + ",");
			RuleInsert.append("'" + VersionType + "'" + ",");
			RuleInsert.append("'" + testflag + "'" + ",");
			RuleInsert.append("'" + mainVersionCode + "'" + ",");
			RuleInsert.append("'" + VersionDesc + "'" + ",");
			RuleInsert.append("'" + VersionStrId + "'" + ",");
			RuleInsert.append("'1'" + ",");
			RuleInsert.append("'" + policyId + "'" + ",");
			RuleInsert.append("'" + branchNo + "',");
			RuleInsert.append("'" + Time19 + "',");
			RuleInsert.append("'',");
			RuleInsert.append("'"+ remark +"')");
			P_Jdbc.executeSQL(null, RuleInsert.toString(), false);
			AppLogger.info("版本发布成功");
			
			/* 合并增量版本到主版本
			 * 
			 */

			//获取版本保留期数(暂不使用)
			/*
			String AdditionNumberSQL = "select paramvalue from tp_cip_sysparameters where modulecode='Version_Manage' and transcode='version' and paramkeyname='AdditionNumber'";
			TCResult AdditionNumberResult = P_Jdbc.dmlSelect(null, AdditionNumberSQL, -1);
			if (AdditionNumberResult==null || AdditionNumberResult.getStatus()==2) {
				return new TCResult(0, ErrorCode.REMOTE, "查询系统基本参数异常");
			}
			JavaList AdditionNumberList = (JavaList)AdditionNumberResult.getOutputParams().get(1);
			int AdditionNumber = Integer.parseInt(AdditionNumberList.getListItem(0).get(0).toString());
			*/
			if (isTest) { //试点版本不合并
				AppLogger.info("试点版本，不处理");
				//提交数据库
				P_Jdbc.commit(null);
				AppLogger.info("数据库提交成功");
				return TCResult.newSuccessResult();
			}
			
			//非试点版本，发布时将与此版本号相同并且状态为‘1-已发布’的试点版本状态均置为‘3-已失效’
			String VerSetSameVersionCodeSQL = "update AUMS_VER_RELEASERULE set VERSIONSTATUS='3',REMARK2='setstatus by release' where VERSIONCODE='" + versionCode + "' and TESTFLAG='2' and VERSIONSTATUS='1'";
			P_Jdbc.executeSQL(null, VerSetSameVersionCodeSQL, false);
			
			//获取发布的增量版本文件明细
			String VersionFileInfoCountSQL = "select MAIN.FILEID, MAIN.FILENAME, MAIN.FILEPATH, MAIN.FILETYPE, MAIN.MD5, MAIN.ZIPFILEID, MAIN.CREATE_TIME, MAIN.FILESIZE, MAIN.ZIPFILENAME, MAIN.CLIENTPATH, EXT.OPERBRANCHNO, EXT.USEBRANCHNO from AUMS_VER_DETAILINFO_MAIN MAIN "+
			                                 "join AUMS_VER_INFO_EXT EXT on EXT.ZIPFILEID = MAIN.ZIPFILEID and EXT.VERSIONID='"+ VersionId +"' and EXT.VERSIONATTR='2'";
			TCResult VersionFileInfoResult = P_Jdbc.dmlSelect(null, VersionFileInfoCountSQL, -1);
			if (VersionFileInfoResult==null) {
				return new TCResult(0, ErrorCode.REMOTE, "查询版本扩展信息异常");
			}if (VersionFileInfoResult.getStatus()==2) { //无增量时结束
				AppLogger.info("无增量版本，不处理");
				P_Jdbc.commit(null);
				AppLogger.info("数据库提交成功");
				return TCResult.newSuccessResult();
			}
			JavaList VersionFileInfoResultList = (JavaList)VersionFileInfoResult.getOutputParams().get(1);

			for (int i = 0; i < VersionFileInfoResultList.size(); i++)
			{
				//查询出多个文件的，循环合并每一笔文件对应的版本文件
				String nfileid = VersionFileInfoResultList.getListItem(i).get(0).toString();
				String nfilename = VersionFileInfoResultList.getListItem(i).get(1).toString();
				String nfilepath = VersionFileInfoResultList.getListItem(i).get(2).toString();
				String nfiletype = VersionFileInfoResultList.getListItem(i).get(3).toString();
				String nfilemd5 = VersionFileInfoResultList.getListItem(i).get(4).toString();
				String nzipfileid = VersionFileInfoResultList.getListItem(i).get(5).toString();
				String ncreate_time = VersionFileInfoResultList.getListItem(i).get(6).toString();
				String nfilesize = VersionFileInfoResultList.getListItem(i).get(7).toString();
				String nzipfilename = VersionFileInfoResultList.getListItem(i).get(8).toString();
				String nclientpath = VersionFileInfoResultList.getListItem(i).get(9).toString();
				String operbranchno = VersionFileInfoResultList.getListItem(i).get(10).toString();
				String usebranchno = VersionFileInfoResultList.getListItem(i).get(11).toString();
				
				//获取此文件对应的主版本文件，如果存在，则备份主版本文件，然后更新数据，如果不存在，直接新增一条主文件  VERSIONATTR=1 表示主版本，VERSIONSTATUS=1 表示已发版未回退
				//AppLogger.info("nclientpath==="+nclientpath);
				String VersionAddInfoSQL = "select * from (select MAIN.FILEID, MAIN.FILENAME, MAIN.FILEPATH, MAIN.FILETYPE, MAIN.MD5, MAIN.ZIPFILEID, MAIN.CREATE_TIME, MAIN.FILESIZE, MAIN.ZIPFILENAME, MAIN.CLIENTPATH from AUMS_VER_DETAILINFO_MAIN MAIN"+
                                           " join AUMS_VER_INFO_EXT EXT on MAIN.ZIPFILEID=EXT.ZIPFILEID and EXT.OPERBRANCHNO='"+ operbranchno +"' and EXT.USEBRANCHNO='"+ usebranchno +"' and EXT.VERSIONATTR='1' "+
                                           " join AUMS_VER_RELEASERULE RULE on EXT.VERSIONID = RULE.VERSIONID and RULE.VERSIONSTATUS = '1' and RULE.TESTFLAG = '1' "+
                                           " where MAIN.CLIENTPATH='"+ nclientpath +"' order by RULE.POLICYID desc) where rownum=1";
				AppLogger.info("VersionAddInfoSQL==="+VersionAddInfoSQL);
				TCResult VersionAddInfoResult = P_Jdbc.dmlSelect(null, VersionAddInfoSQL, -1);
				if (VersionAddInfoResult==null) {
					return new TCResult(0, ErrorCode.REMOTE, "查询版本扩展信息异常");
				}
				int resultstatus = VersionAddInfoResult.getStatus();
				AppLogger.info("resultstatus=========================="+resultstatus);
				if (resultstatus == 1 || resultstatus == 2) {
					String ofileid = "";
					String ofilename = "";
					String ofilepath = "";
					String ofiletype = "";
					String ofilemd5 = "";
					String ozipfileid = "";
					String ocreate_time = "";
					String ofilesize = "";
					String ozipfilename = "";
					String oclientpath = "";
					//登记更新记录表，用于回退及查询合并记录使用
					StringBuffer versionMergeSB = new StringBuffer(
							"insert into AUMS_VER_VERSIONMERGELOG  VALUES('" + uuid + "',");
					if (resultstatus == 2) { //查询无记录，表示此条为新增内容，需要新增到主版本里
						AppLogger.info("新增内容，合并到主版本");
						//因为新增内容未获取到主版本信息，此处需要重新获取主版本(获取规则是，已经发布生效的，并且作用域与当前增量版本相同的全量版本)
						String VersionAddInfoAttrSQL = "select * from (select EXT.ZIPFILEID,EXT.ZIPFILENAME from AUMS_VER_RELEASERULE RULE "+
                                "join AUMS_VER_INFO_EXT EXT on EXT.VERSIONID = RULE.VERSIONID and EXT.VERSIONATTR='1' and EXT.OPERBRANCHNO='"+ operbranchno +"' and EXT.USEBRANCHNO='"+ usebranchno +"' "+
                                "where RULE.VERSIONSTATUS = '1' and RULE.TESTFLAG = '1' order by RULE.POLICYID desc) where rownum=1";
						AppLogger.info("VersionAddInfoSQL==="+VersionAddInfoAttrSQL);
						TCResult VersionAddInfoAttrResult = P_Jdbc.dmlSelect(null, VersionAddInfoAttrSQL, -1);
						if (VersionAddInfoAttrResult==null || VersionAddInfoAttrResult.getStatus()==2) {
							return new TCResult(0, ErrorCode.REMOTE, "查询主版本信息异常");
						}
						JavaList VersionAddInfoAttrResultList = (JavaList)VersionAddInfoAttrResult.getOutputParams().get(1);
						ofileid = nfileid;
						ofilename = nfilename;
						ofilepath = nfilepath;
						ofiletype = nfiletype;
						ofilemd5 = nfilemd5;
						ozipfileid = VersionAddInfoAttrResultList.getListItem(0).get(0).toString();;
						ocreate_time = ncreate_time;
						ofilesize = nfilesize;
						ozipfilename = VersionAddInfoAttrResultList.getListItem(0).get(1).toString();;
						oclientpath = nclientpath;
						versionMergeSB.append("'A'" + ",");
					}if (resultstatus == 1) { //查询有记录，表示此条为修改内容，需要合并到主版本里
						AppLogger.info("修改内容，合并到主版本");
						JavaList VersionAddInfoResultList = (JavaList)VersionAddInfoResult.getOutputParams().get(1);
						ofileid = VersionAddInfoResultList.getListItem(0).get(0).toString();
						ofilename = VersionAddInfoResultList.getListItem(0).get(1).toString();
						ofilepath = VersionAddInfoResultList.getListItem(0).get(2).toString();
						ofiletype = VersionAddInfoResultList.getListItem(0).get(3).toString();
						ofilemd5 = VersionAddInfoResultList.getListItem(0).get(4).toString();
						ozipfileid = VersionAddInfoResultList.getListItem(0).get(5).toString();
						ocreate_time = VersionAddInfoResultList.getListItem(0).get(6).toString();
						ofilesize = VersionAddInfoResultList.getListItem(0).get(7).toString();
						ozipfilename = VersionAddInfoResultList.getListItem(0).get(8).toString();
						oclientpath = VersionAddInfoResultList.getListItem(0).get(9).toString();
						versionMergeSB.append("'U'" + ",");
					}
					versionMergeSB.append("'" + nfileid+ "'" + ",");
					versionMergeSB.append("'" + nfilepath + "'"+ ",");
					versionMergeSB.append("'" + nfilemd5 + "'"+ ",");
					versionMergeSB.append("'" + nzipfileid + "'"+ ",");
					versionMergeSB.append("'" + nzipfilename + "'" + ",");
					versionMergeSB.append("'" + nfilesize + "',");
					versionMergeSB.append("'" + nclientpath + "',");
					versionMergeSB.append("'" + ncreate_time + "',");
					versionMergeSB.append("'" + ofileid+ "'" + ",");
					versionMergeSB.append("'" + ofilepath + "'"+ ",");
					versionMergeSB.append("'" + ofilemd5 + "'"+ ",");
					versionMergeSB.append("'" + ozipfileid + "'"+ ",");
					versionMergeSB.append("'" + ozipfilename + "'" + ",");
					versionMergeSB.append("'" + ofilesize + "',");
					versionMergeSB.append("'" + oclientpath + "',");
					versionMergeSB.append("'" + ocreate_time + "','','')");
					//更新版本文件明细表
					StringBuffer versionUpdateSB = new StringBuffer("");
					if (resultstatus == 2) { //查询无记录，表示此条为新增内容，需要新增到主版本里
						AppLogger.info("更新主版本表新增内容");
						versionUpdateSB.append("update AUMS_VER_DETAILINFO_MAIN set ");
						//zipfile信息修改即可
						versionUpdateSB.append("ZIPFILEID = '" + ozipfileid + "'"+ ",");
						versionUpdateSB.append("ZIPFILENAME = '" + ozipfilename + "' where ");
						versionUpdateSB.append("FILEID = '" + nfileid + "'");
					}if (resultstatus == 1) { //查询有记录，表示此条为修改内容，需要合并到主版本里
						AppLogger.info("更新主版本表");
						versionUpdateSB.append("update AUMS_VER_DETAILINFO_MAIN set ");
						versionUpdateSB.append("FILEPATH = '" + nfilepath + "'"+ ",");
						versionUpdateSB.append("MD5 = '" + nfilemd5 + "'"+ ",");
						//zipfile包暂时先不改，否则会应查询结果
						//versionUpdateSB.append("ZIPFILEID = '" + nzipfileid + "'"+ ",");
						//versionUpdateSB.append("ZIPFILENAME = '" + nzipfilename + "'" + ",");
						versionUpdateSB.append("FILESIZE = '" + nfilesize + "',");
						versionUpdateSB.append("CREATE_TIME = '" + ncreate_time + "' where ");
						versionUpdateSB.append("FILEID = '" + ofileid + "'");
					}
					P_Jdbc.executeSQL(null, versionMergeSB.toString(), false);
					P_Jdbc.executeSQL(null, versionUpdateSB.toString(), false);
				} 
			
			}
			P_Jdbc.commit(null);
			AppLogger.info("数据库提交成功");
			return TCResult.newSuccessResult();
		} catch (Exception e) {
			AppLogger.info(e);
			P_Jdbc.rollBack(null);
			return new TCResult(0, ErrorCode.REMOTE, "版本发布异常");
		}
	}
	
	/**
	 * @category 版本回退
	 * @param ruleId
	 * 			     入参|发布规则编号|{@link java.lang.String}
	 * @param branchNO
	 * 			     入参|操作机构号|{@link java.lang.String}
	 */
	@InParams(param = {
			@Param(name = "ruleId", comment = "发布规则编号", type = java.lang.String.class),
			@Param(name = "branchNO", comment = "操作机构号", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "按版本号回退版本成功"),
			@Return(id = "1", desp = "按版本号回退版本失败") })
	@Component(label = "版本回退", comment = "版本回退", date = "2018-03-16 02:10:26")
	public static TCResult A_VersionRePublic(String ruleId, String branchNO) {
			//回退检查，对于存在此版本之后发布的正式版本未回退的，则不允许回退此版本。即正式版本的回退遵循后发布的先回退。
			//查询此版本规则信息
			String VersionRuleSQL = "select RULE.VERSIONID,RULE.VERSIONTYPE,RULE.POLICYID,RULE.TESTFLAG,EXT.USEBRANCHNO from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where RULEID='"+ ruleId +
					                "' and RULE.VERSIONID = EXT.VERSIONID and RULE.VERSIONSTATUS='1'";
			TCResult VersionRuleResult = P_Jdbc.dmlSelect(null, VersionRuleSQL, -1);
			if (VersionRuleResult==null || VersionRuleResult.getStatus()==2) {
				return new TCResult(0, ErrorCode.REMOTE, "查询版本发布信息异常");
			}
			JavaList VersionRuleList = (JavaList)VersionRuleResult.getOutputParams().get(1);
			String testflag = VersionRuleList.getListItem(0).get(3).toString();
			if ("1".equals(testflag)) { //非试点版本
				String versiontype = VersionRuleList.getListItem(0).get(1).toString();
				String policyid = VersionRuleList.getListItem(0).get(2).toString();
				String usebrno = "";
				for (int i = 0; i < VersionRuleList.size(); i++) { //处理一个版本对应多文件的场景
					usebrno = usebrno + "'" +VersionRuleList.getListItem(i).get(4).toString() + "',";
					
				}
				usebrno = usebrno.substring(0, usebrno.length()-1);
				
				//查询是否有此版本之后发布的作用域相同并且未回退的同类版本，如果存在，则不允许回退
				String VersionNewRuleSQL = "select RULE.VERSIONCODE,RULE.VERSIONDESC from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where "+
		                "RULE.VERSIONTYPE = '" + versiontype + "' and RULE.VERSIONSTATUS='1' and RULE.TESTFLAG='1' and RULE.VERSIONID = EXT.VERSIONID and EXT.USEBRANCHNO in (" + usebrno + ") and RULE.POLICYID>'" + policyid + "' order by RULE.CREATETIME DESC";
				TCResult VersionNewRuleResult = P_Jdbc.dmlSelect(null, VersionNewRuleSQL, -1);
				if (VersionNewRuleResult==null) {
					return new TCResult(0, ErrorCode.REMOTE, "查询版本发布信息异常");
					}
				if (VersionNewRuleResult.getStatus()==1) {//有比当前版本更新的版本未回退处理
					JavaList VersionNewRuleList = (JavaList)VersionNewRuleResult.getOutputParams().get(1);
					String newversioncode = VersionNewRuleList.getListItem(0).get(0).toString();
					String newversiondesc = VersionNewRuleList.getListItem(0).get(1).toString();
					return new TCResult(0, ErrorCode.REMOTE, "存在未回退的版本【" + newversioncode + ",  "+ newversiondesc +"】，此版本不能回退");
					}
				
				//无版本依赖，则做回退处理
				String VersionRuleLogSQL = "select OPERTYPE,NFILEID,NFILEPATH,NMD5,NZIPFILEID,NZIPFILENAME,NFILESIZE,NCLIENTPATH,NUPDATETIME,OFILEID,OFILEPATH,OMD5,OZIPFILEID,OZIPFILENAME,OFILESIZE,OCLIENTPATH,OUPDATETIME,REMARK1,REMARK2 from AUMS_VER_VERSIONMERGELOG where "+
		                "RULEID = '"+ ruleId +"'";
				TCResult VersionRuleLogResult = P_Jdbc.dmlSelect(null, VersionRuleLogSQL, -1);
				if (VersionRuleLogResult==null ) {
					return new TCResult(0, ErrorCode.REMOTE, "查询版本发布日志记录异常");
				}else if(VersionRuleLogResult.getStatus()==1) { //查询到合并记录时，做合并回退处理
					JavaList VersionRuleLogList = (JavaList)VersionRuleLogResult.getOutputParams().get(1);
					for (int i = 0; i < VersionRuleLogList.size(); i++) {
						String opertype = VersionRuleLogList.getListItem(i).get(0).toString();
						String nfileid = VersionRuleLogList.getListItem(i).get(1).toString();
						//String nfilepath = VersionRuleLogList.getListItem(i).get(2).toString();
						//String nfilemd5 = VersionRuleLogList.getListItem(i).get(3).toString();
						String nzipfileid = VersionRuleLogList.getListItem(i).get(4).toString();
						String nzipfilename = VersionRuleLogList.getListItem(i).get(5).toString();
						//String nfilesize = VersionRuleLogList.getListItem(i).get(6).toString();
						//String nclientpath = VersionRuleLogList.getListItem(i).get(7).toString();
						//String nupdatetime = VersionRuleLogList.getListItem(i).get(8).toString();
						String ofileid = VersionRuleLogList.getListItem(i).get(9).toString();
						String ofilepath = VersionRuleLogList.getListItem(i).get(10).toString();
						String ofilemd5 = VersionRuleLogList.getListItem(i).get(11).toString();
						//String ozipfileid = VersionRuleLogList.getListItem(i).get(12).toString();
						//String ozipfilename = VersionRuleLogList.getListItem(i).get(13).toString();
						String ofilesize = VersionRuleLogList.getListItem(i).get(14).toString();
						//String oclientpath = VersionRuleLogList.getListItem(i).get(15).toString();
						String oupdatetime = VersionRuleLogList.getListItem(i).get(16).toString();
						StringBuffer VerOperTypeSQL = new StringBuffer("");
						if ("A".equals(opertype)) { //新增文件,直接将版本文件对应的要说吧信息更新回明细表里
							VerOperTypeSQL.append("update AUMS_VER_DETAILINFO_MAIN set ");
							VerOperTypeSQL.append("ZIPFILEID = '" + nzipfileid + "'"+ ",");
							VerOperTypeSQL.append("ZIPFILENAME = '" + nzipfilename + "' where ");
							VerOperTypeSQL.append("FILEID = '" + nfileid + "'");
						} else if("U".equals(opertype)) { //修改文件
							VerOperTypeSQL.append("update AUMS_VER_DETAILINFO_MAIN set ");
							VerOperTypeSQL.append("FILEPATH = '" + ofilepath + "'"+ ",");
							VerOperTypeSQL.append("MD5 = '" + ofilemd5 + "'"+ ",");
							//VerOperTypeSQL.append("ZIPFILEID = '" + ozipfileid + "'"+ ",");
							//VerOperTypeSQL.append("ZIPFILENAME = '" + ozipfilename + "'" + ",");
							VerOperTypeSQL.append("FILESIZE = '" + ofilesize + "',");
							VerOperTypeSQL.append("CREATE_TIME = '" + oupdatetime + "' where ");
							VerOperTypeSQL.append("FILEID = '" + ofileid + "'");
						} else if("D".equals(opertype)) { //删除文件
							AppLogger.info("预留删除分支，暂不处理");
						}
						if (!"".equals(VerOperTypeSQL)) {
							try {
								P_Jdbc.executeSQL(null, VerOperTypeSQL.toString(), false);
							} catch (Exception e) {
								AppLogger.info(e);
								P_Jdbc.rollBack(null);
							}		
						}			
					}
				}

			}
		
		// 回退版本，直接将当前回退的版本置为已回退即可
		String VerReturnSQL = "update AUMS_VER_RELEASERULE set VERSIONSTATUS='2' where RULEID='"
				+ ruleId + "' and VERSIONSTATUS='1'";
		// 将此版本号VersionCode对应的所有试点版本一并回退掉（此逻辑保留，如果不需要，则删除此处处理即可，VERSIONSTATUS='3'代表原试点版本发布时规则被置为已失效，此处将已失效的全部回退）
		String VerReturnSameVersionCodeSQL = "update AUMS_VER_RELEASERULE set VERSIONSTATUS='2',REMARK2='return by rule' where VERSIONCODE=(select VERSIONCODE from AUMS_VER_RELEASERULE where RULEID='" + ruleId + "') and TESTFLAG='2' and VERSIONSTATUS='3'";
		try {
			P_Jdbc.executeSQL(null, VerReturnSQL, false);
			P_Jdbc.executeSQL(null, VerReturnSameVersionCodeSQL, false);
			P_Jdbc.commit(null);
			AppLogger.info("版本回退成功");
		} catch (Exception e) {
			AppLogger.info(e);
			P_Jdbc.rollBack(null);
			return new TCResult(0, ErrorCode.REMOTE, "版本回退更新原策略异常！");

		}
		return TCResult.newSuccessResult();
	}
	
	/**
	 * @category 删除版本NEW
	 * @param zipFileId
	 *            入参|压缩文件ID| {@link java.lang.String}
	 * @param branchId
	 *            入参|机构ID| {@link java.lang.String}
	 */
	@InParams(param = { @Param(name = "zipFileId", comment = "压缩文件ID", type = java.lang.String.class),
			            @Param(name = "branchId", comment = "机构ID", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "删除成功"),
			@Return(id = "1", desp = "删除失败") })
	@Component(label = "删除版本NEW", comment = "删除版本NEW", author = "AlphaLi", date = "2018-05-07 14:05:43")
	public static TCResult A_DelVersion(String zipFileId, String branchId) {
		
		if (zipFileId == null || zipFileId.isEmpty()) {
			return new TCResult(0, ErrorCode.REMOTE, "文件标识为空！");
		}
		//文件分隔符
		String file_separator = System.getProperty("file.separator");
		boolean successflag = true;
		//根据机构ID获机构为总行还是分行
		String getBranchType = "select FATHERBRANCHID from AUMS_BRANCHINFO WHERE BRANCHID='"+ branchId +"'";
		String FatherBranch = "";
		TCResult BranchQueryResult = P_Jdbc.dmlSelect(null, getBranchType.toString(), -1);
		if (BranchQueryResult==null || BranchQueryResult.getStatus()==2) {
			return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常！");
		} else {
			JavaList BranchRst = (JavaList)BranchQueryResult.getOutputParams().get(1);
			FatherBranch = (String) BranchRst.getListItem(0).get(0);
		}
		String VersionType = FatherBranch.equals("adminBranch") ? "1" : "2";
		
		//总行标志
		boolean mainBranchFlag = false;
		if (VersionType=="1") {
			mainBranchFlag = true;
		}
		
		String versionUnpackPath = "";
		if (mainBranchFlag) {
			versionUnpackPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE='Version_Manage' AND tpi.TRANSCODE='version' AND tpi.PARAMKEYNAME='versionUnpackPath'";
		} else {
			versionUnpackPath = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE='Version_Manage' AND tpi.TRANSCODE='version' AND tpi.PARAMKEYNAME='branchVersionUnpackPath'";
		}
		JavaList versionUnpackPathList = null;
		String unpackPath="";
		try {
			versionUnpackPathList = (JavaList)P_Jdbc.dmlSelect(null,versionUnpackPath,-1).getOutputParams().get(1);
			unpackPath = (String) versionUnpackPathList.getListItem(0).get(0).toString();
		} catch (Exception e) {
			AppLogger.info("查询版本明细文件所在位置时异常");
			AppLogger.info(e);
		}
		
		String queryVerExtInfoSQL ="select a1.versionid,a1.zipfilepath,a1.zipfilename,a2.versioncode from aums_ver_info_ext a1,aums_ver_info a2 where a1.zipfileid = '" + zipFileId + "' and a1.versionid=a2.versionid";
		TCResult queryVerExtInfoRst = P_Jdbc.dmlSelect(null, queryVerExtInfoSQL, -1);
		if (queryVerExtInfoRst == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询版本文件信息异常");
		} else {
			if (queryVerExtInfoRst.getStatus()!=1) {
				return new TCResult(0, ErrorCode.REMOTE, "版本文件信息不存在！！！");
			} else {
				JavaList queryVerExtInfoList = (JavaList)queryVerExtInfoRst.getOutputParams().get(1);
				String versionid = queryVerExtInfoList.getListItem(0).get(0).toString();
				String zipfilepath = queryVerExtInfoList.getListItem(0).get(1).toString();
				String zipfilename = queryVerExtInfoList.getListItem(0).get(2).toString();
				AppLogger.info("zipfilename===="+zipfilename);
				String zipfiledir=zipfilename.substring(0, zipfilename.lastIndexOf("."));
				String versioncode = queryVerExtInfoList.getListItem(0).get(3).toString();
				
				String VerExtCountSQL ="select count(0) from aums_ver_info_ext where versionid = '" + versionid + "'";
				TCResult VerExtCountRst = P_Jdbc.dmlSelect(null, VerExtCountSQL, -1);
				if (VerExtCountRst == null) {
					return new TCResult(0, ErrorCode.REMOTE, "查询版本文件总数异常");
				} else {
					if (VerExtCountRst.getStatus()!=1) {
						return new TCResult(0, ErrorCode.REMOTE, "查询版本文件总数异常！！！");
					} else {
						JavaList VerExtCountList = (JavaList)VerExtCountRst.getOutputParams().get(1);
						int filecount = Integer.parseInt(VerExtCountList.getListItem(0).get(0).toString());
						String delFilePaht = "";
						if (filecount==1) {
							//删除逻辑
							String deleteVersionDetailSQL = "delete from AUMS_VER_DETAILINFO_MAIN where ZIPFILEID='"+ zipFileId +"'";
							String deleteVersionSQL = "delete from aums_ver_info where VERSIONID in (select VERSIONID from AUMS_VER_INFO_EXT where ZIPFILEID='"+ zipFileId +"')";
							String deleteVersionExtSQL = "delete from AUMS_VER_INFO_EXT where ZIPFILEID ='" + zipFileId + "'";
							try {
								P_Jdbc.executeSQL(null, deleteVersionDetailSQL, false);
								P_Jdbc.executeSQL(null, deleteVersionSQL, false);
								P_Jdbc.executeSQL(null, deleteVersionExtSQL, false);
								delFilePaht = unpackPath + versioncode;
								AppLogger.info("待删除文件路径为："+delFilePaht);
								successflag = BasicUtilTool.deleteDirectory(delFilePaht);
								if (successflag == false) {
									P_Jdbc.rollBack(null);
									return new TCResult(0, ErrorCode.REMOTE, "删除版本文件失败！！！");
								}else {
									AppLogger.info("删除版本文件压缩包开始，删除的包为：【"+zipfilepath+"】");
									try{
										File file = new File(zipfilepath);
										successflag = file.delete();
										if (successflag == false) {
											P_Jdbc.rollBack(null);
											return new TCResult(0, ErrorCode.REMOTE, "删除版本文件压缩包失败！！！");
										}else {
											P_Jdbc.commit(null);
											AppLogger.info("删除版本文件压缩包成功");
										}
									}catch (Exception e) {
										P_Jdbc.rollBack(null);
										AppLogger.info("删除版本文件压缩包异常"+e);
									}
								}
							} catch (Exception e) {
								AppLogger.info(e);
								P_Jdbc.rollBack(null);
								return new TCResult(0, ErrorCode.REMOTE, "删除版本信息和版本详情失败！");
							}
						} else if (filecount>1) {
							//删除逻辑
							String deleteVersionDetailSQL = "delete from AUMS_VER_DETAILINFO_MAIN where ZIPFILEID='"+ zipFileId +"'";
							String deleteVersionExtSQL = "delete from AUMS_VER_INFO_EXT where ZIPFILEID ='" + zipFileId + "'";
							try {
								P_Jdbc.executeSQL(null, deleteVersionDetailSQL, false);
								P_Jdbc.executeSQL(null, deleteVersionExtSQL, false);
								delFilePaht = unpackPath + versioncode + file_separator + zipfiledir;
								AppLogger.info("待删除文件路径为："+delFilePaht);
								successflag = BasicUtilTool.deleteDirectory(delFilePaht);
								if (successflag == false) {
									P_Jdbc.rollBack(null);
									return new TCResult(0, ErrorCode.REMOTE, "删除版本文件失败！！！");
								}else {
									AppLogger.info("删除版本文件压缩包开始，删除的包为：【"+zipfilepath+"】");
									try{
										File file = new File(zipfilepath);
										successflag = file.delete();
										if (successflag == false) {
											P_Jdbc.rollBack(null);
											return new TCResult(0, ErrorCode.REMOTE, "删除版本文件压缩包失败！！！");
										}else {
											P_Jdbc.commit(null);
											AppLogger.info("删除版本文件压缩包成功");
										}
									}catch (Exception e) {
										P_Jdbc.rollBack(null);
										AppLogger.info("删除版本文件压缩包异常"+e);
									}
								}
							} catch (Exception e) {
								AppLogger.info(e);
								P_Jdbc.rollBack(null);
								return new TCResult(0, ErrorCode.REMOTE, "删除版本信息和版本详情失败！");
							}
						}else{
							return new TCResult(0, ErrorCode.REMOTE, "查询版本文件总数为0");
						}
					}
				}
			}
		}
		
		P_Jdbc.commit(null);
		return TCResult.newSuccessResult();
	}
	
	
	/**
	 * @category 导入版本更新规则基础模板表
	 * @param filename
	 *            入参|Excel文件|
	 *            {@link org.springframework.web.multipart.MultipartFile}
	 */
	@InParams(param = { @Param(name = "fileName", comment = "文件名", type = java.lang.String.class),
			@Param(name = "fileUrl", comment = "文件路径", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "导入版本策略成功"),
			@Return(id = "1", desp = "导入版本策略失败") })
	@Component(label = "导入版本更新规则基础模板表", comment = "导入版本更新规则基础模板表", author = "AlphaLi", date = "2018-5-08 05:25:30")
	public static TCResult A_loadVersionOptionsTemplate(String fileName,String fileUrl) {
		if (fileName == null || "".equals(fileName)) {
			return new TCResult(0, ErrorCode.REMOTE, "请选择您要导入的规则模板表！");
		} else if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			return new TCResult(0, ErrorCode.REMOTE, "请选择正确格式的规则模板表！");
		}
		ExcelExcelParseTool excelParseTool = new ExcelExcelParseTool();
		Workbook workbook = null;
		try {
			InputStream in = new FileInputStream(fileUrl+fileName);
			workbook = excelParseTool.initWorkBook(in,fileUrl+fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new TCResult(0, ErrorCode.REMOTE, "导入的版本更新规则基础模板文件不存在！");
		} catch (IOException e) {
			e.printStackTrace();
			return new TCResult(0, ErrorCode.REMOTE, "导入的版本更新规则基础模板异常！");
		}
		List<List<String>> list = null;
		try{
			list = excelParseTool.getExcelData(workbook);
		}catch(Exception e){
			e.printStackTrace();
			AppLogger.error("获取模板信息异常，【"+e.getMessage()+"】");
		}
		
		String excluded_files = null;
		String excluded_dirs = null;
		String once_update_files = null;
		String once_update_dirs = null;
		String match_patterns = null;
		String strategy_template_name = null;
		//获取入库时间
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String createtime = sdf.format(now);
		//设置备注描述
		String remark = "版本更新规则基础模板导入";
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				List<String> cellList = list.get(i);
				for (int j = 0; j < cellList.size(); j++) {
					if (j == 0) {
						excluded_files = cellList.get(j);
						j++;
					}
					if (j == 1) {
						excluded_dirs = cellList.get(j);
						j++;
					}
					if (j == 2) {
						once_update_files = cellList.get(j);
						j++;
					}
					if (j == 3) {
						once_update_dirs = cellList.get(j);
						j++;
					}
					if (j == 4) {
						match_patterns = cellList.get(j);
						j++;
					}
					if (j == 5) {
						strategy_template_name = cellList.get(j);
						j++;
					}
					//获取UUID
					String optionsTemplateId = UUID.randomUUID().toString();
					String sql = "INSERT INTO aums_ver_options_template(options_template_id,EXCLUDED_FILES,EXCLUDED_DIRS,ONCE_UPDATE_FILES,ONCE_UPDATE_DIRS,create_time,strategy_template_name,MATCH_PATTERNS,REMARK1) values('"
							+ optionsTemplateId
							+ "','"
							+ excluded_files
							+ "','"
							+ excluded_dirs
							+ "','"
							+ once_update_files
							+ "','"
							+ once_update_dirs
							+ "','"
							+ createtime
							+ "','"
							+ strategy_template_name
							+ "','"
							+ match_patterns
							+ "','"
							+ remark + "')";
					AppLogger.info("版本更新策略模板导入SQL：【" + sql + "】");
					P_Jdbc.executeSQL(null, sql, true);
				}
			}
		}
		return new TCResult(1, "000000", "版本更新模板导入成功!!!");
	}
	
	/**
	 * @category 分行按地区发布版本
	 * @param bankArea
	 *            入参|地区代号| {@link java.lang.String}
	 * @param versionId
	 *            入参|版本号| {@link java.lang.String}
	 * @param devaddmode
	 *            入参|添加方式| {@link java.lang.String}
	 */
	@InParams(param = {
			@Param(name = "bankArea", comment = "地区代号", type = java.lang.String.class),
			@Param(name = "versionId", comment = "版本号", type = java.lang.String.class),
			@Param(name = "devaddmode", comment = "添加方式", type = java.lang.String.class),
			@Param(name = "branchNo", comment = "操作机构号", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "按地区发布版本成功"),
			@Return(id = "1", desp = "按地区发布版本失败") })
	@Component(label = "按地区发布版本", comment = "按地区发布版本", date = "2018-03-14 03:09:07")
	public static TCResult A_PublicVersionByArea(String bankArea, String versionId,String devaddmode,String branchNo) {
		
		String sql = "select A.devid from aums_dev_info A,aums_branch_group_relation B where a.devbranchid=b.branchid and b.brangroupid='" + bankArea + "'";
		//这里前端送的是versionCode
		String sql_dev = "select DEVID from aums_ver_to_dev where VERSIONCODE = '" + versionId +"'";
		
		try {
			//查询已经是versionId版本的设备id
			TCResult ver2devResult = P_Jdbc.dmlSelect(null, sql_dev, -1);
			JavaList updevidlist = null;
			String[] updevids = null;
			if(ver2devResult != null && ver2devResult.getStatus() != 2){
				updevidlist = (JavaList) ver2devResult.getOutputParams().get(1);
				updevids = new String[updevidlist.size()];
				//该机构下已经是versionId的设备id
				if (updevidlist != null && !updevidlist.isEmpty()) {
					for (int i = 0; i < updevidlist.size(); i++) {
						updevids[i] = updevidlist.getListItem(i).get(0).toString();
					}
				}
			}
			AppLogger.info("按地区发布,获取版本设备绑定关系完成");
			TCResult areaDevInfo = P_Jdbc.dmlSelect(null, sql, -1);
			if(areaDevInfo == null){
				return new TCResult(0, ErrorCode.REMOTE, "获取地区设备时异常");
			}
			if(areaDevInfo.getStatus() == 2){
				return new TCResult(0, ErrorCode.REMOTE, "该地区没有设备");
			}
			AppLogger.info("按地区发布,获取地区所包含的设备信息完成");
			JavaList devidlist = (JavaList) areaDevInfo.getOutputParams().get(1);
			if (devidlist != null && !devidlist.isEmpty()) {
				
				List<String> devidsList =new ArrayList<String>();
				for (int i = 0; i < devidlist.size(); i++) {
					String devid = devidlist.getListItem(i).get(0).toString();
					if(updevidlist != null && !updevidlist.isEmpty()){
						//比较全部设备id与未升级的设备id，将版本还未是最新versionId的devid加入要升级的devids数组中。
						boolean flag = true;
						for(int j =0;j<updevids.length;j++){
							if(devid.equals(updevids[j])){
								flag = false;
								break;
							}
						}
						if(flag){
							devidsList.add(devid);
						}
					}else{
						devidsList.add(devid);
					}
				}
				AppLogger.info("按地区发布,循环处理完成");
				String[] devids = new String[devidsList.size()];
				AppLogger.info("按地区发布,需要发布的机具列表：【"+devids+"】");
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String startTime = sdf.format(now);
				
				TCResult resultObj = (TCResult)BasicUtilTool.deployVersion(versionId, "1", startTime,"10", "100", devidsList.toArray(devids), devaddmode,branchNo,false);
				AppLogger.info("按地区发布,发布返回的数据为：【"+resultObj+"】");
				if(resultObj.getStatus()==0){
					AppLogger.info("版本号：【"+versionId + "】发布失败=="+resultObj.getErrorCode()+"  "+resultObj.getErrorMsg());
					return new TCResult(0, ErrorCode.REMOTE, "按地区发布失败,部分机具已有最新版本或其版本状态未知");
				}
			}else{
				return new TCResult(0, ErrorCode.REMOTE, "该地区没有设备！");
			}
		} catch (Exception e) {
			AppLogger.info(e);
			return new TCResult(0, ErrorCode.REMOTE, "按地区发布版本异常！");
		}
		return new TCResult(1, "000000", "按地区发布版本成功！！！");
	}
	
	
	/**
	 * @category 分行按机构发布
	 * @param brnoList
	 *            入参|机构号| {@link JavaList}
	 * @param versionCode
	 *            入参|版本号| {@link java.lang.String}
	 * @param devaddmode
	 *            入参|添加方式| {@link java.lang.String}
	 */
	@InParams(param = {
			@Param(name = "brnoList", comment = "机构号", type = JavaList.class),
			@Param(name = "versionCode", comment = "版本号", type = java.lang.String.class),
			@Param(name = "devaddmode", comment = "添加方式", type = java.lang.String.class) ,
			@Param(name = "brnoNo", comment = "操作机构号", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "按机构发布版本成功"),
			@Return(id = "1", desp = "按机构发布版本失败") })
	@Component(label = "按机构发布版本", date = "2018-03-14 02:58:02")
	public static TCResult A_BranchVersionByBrno(JavaList brnoList, String versionCode,String devaddmode,String brnoNo) {
		String sql_dev = "select DEVID from aums_ver_to_dev where VERSIONCODE = '" + versionCode +"'";
		TCResult resultObj = null;
		try {
			//查询已经是versionId版本的设备id
			TCResult tcVerDevResult = P_Jdbc.dmlSelect(null, sql_dev.toString(), -1);
			JavaList updevidList = null;
			String[] updevids = null;
			if (tcVerDevResult==null || tcVerDevResult.getStatus()==2) {
				AppLogger.info("当前版本设备未升级");
			} else {
				updevidList = (JavaList)tcVerDevResult.getOutputParams().get(1);
				updevids = new String[updevidList.size()];
				//该机构下已经是versionId的设备id
				if (updevidList != null && !updevidList.isEmpty()) {
					for (int i = 0; i < updevidList.size(); i++) {
						updevids[i] = updevidList.getListItem(0).get(i).toString();
					}
				}
			}
			//A_VersionUpdatePolicy avup = new A_VersionUpdatePolicy();
			//JSONObject jsonobj = new JSONObject();
			for(int i=0;i<brnoList.size();i++){
				String brno = brnoList.getStringItem(i);
				String dev_by_brno = "select DEVID,DEVNUM,DEVIP,BRANCHNO from AUMS_V_DEVLISTINFO_VIEW where DEVBRANCHID = '" + brno +"'";
				JavaList devList = (JavaList)P_Jdbc.dmlSelect(null, dev_by_brno, -1).getOutputParams().get(1);
				List<String> devidsList =new ArrayList<String>();
				for(int j=0;j<devList.size();j++){
					String devid = devList.getListItem(j).get(0).toString();
					if(updevidList != null && !updevidList.isEmpty()){
						//比较全部设备id与未升级的设备id，将版本还未是最新versionId的devid加入要升级的devids数组中。
						boolean flag = true;
						for(int k =0;k<updevids.length;k++){
							if(devid.equals(updevids[k])){
								flag = false;
								break;
							}
						}
						if(flag){
							devidsList.add(devid);
						}
					}else{
						//如果没有updevids中没有已经是最新版本的设备，则更新全部的设备
						devidsList.add(devid);
					}
				}
				
				String[] devids = new String[devidsList.size()];
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String startTime = sdf.format(now);
				AppLogger.info("devidsList=="+devidsList.toArray(devids));
				resultObj = BasicUtilTool.deployVersion(versionCode, "1", startTime,"10", "100", devidsList.toArray(devids), devaddmode,brnoNo,false);
				AppLogger.info("按机构发布,发布返回的数据为：【"+resultObj+"】");
				if(resultObj.getStatus()==0){
					AppLogger.info("版本号：【"+versionCode + "】发布失败==="+resultObj.getErrorCode()+"  "+resultObj.getErrorMsg());
					return new TCResult(0, ErrorCode.REMOTE, "按机构发布失败,"+resultObj.getErrorMsg());
				}
			}
		} catch (Exception e) {
			AppLogger.info(e);
		}
		AppLogger.info("按机构发布返回客户端的信息为：【"+resultObj+"】");
		return new TCResult(1, "000000", "按机构发布版本成功！！！");
	}
	
	
	
	/**
	 * @category 分行按设备发布版本
	 * @param versioncode
	 *            入参|关联版本号| {@link String}
	 * @param devidList
	 *            入参|机具ID| {@link JavaList}
	 * @param devaddmode
	 *            入参|添加方式| {@link String}
	 * @param branchNo
	 * 			    入参|操作机构号|{@link String}
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "关联版本号",  type = String.class),
			@Param(name = "devidList", comment = "机具ID", type = JavaList.class),
			@Param(name = "devaddmode", comment = "添加方式",  type = String.class),
			@Param(name = "branchNo", comment = "操作机构号", type = String.class)})
	@Returns(returns = { @Return(id = "0", desp = "按设备发布版本成功"),
			@Return(id = "1", desp = "按设备发布版本失败") })
	@Component(label = "按设备发布版本", comment = "按设备发布版本", date = "2017-10-09 10:47:19")
	public static TCResult A_BranchPublicVersionByDev(String versionCode,JavaList devidList, String devaddmode,String branchNo) throws Exception {
		
		String iseffect = "1";
		String batchperiod = "10";
		String batchnum = "100";
		
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
			return new TCResult(0, ErrorCode.REMOTE, "暂不支持此类型数据库:【"+dbType+"】");
		}
		//获取序列号完成
		//判断所选机具是否为空
		if (devidList == null || devidList.size()==0) {
			return new TCResult(0, ErrorCode.REMOTE, "请选择您需要发布版本的机具");
		}
		
		Date nowStartTime = new Date();
		SimpleDateFormat sdfStartTime = new SimpleDateFormat("yyyyMMddHHmmss");
		String startTime = sdfStartTime.format(nowStartTime);
		
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
		String versionIDSQL = "select versionid,description from AUMS_VER_BRANCHVERINFO where versioncode='" + versionCode + "'";
		JavaList versionIDList =  (JavaList) P_Jdbc.dmlSelect(null, versionIDSQL, -1).getOutputParams().get(1);
		String versionID = (String) versionIDList.getListItem(0).get(0);
		String description = (String) versionIDList.getListItem(0).get(1);
		
		//格式化需要发布的机具列表
		AppLogger.info("=======1");
		for (int i=0;i<devidList.size();i++) {
			AppLogger.info("=======2");
			String device = devidList.getStringItem(i);
			StringBuffer checkDeviceSql = new StringBuffer(
					"select VERSIONID,VERSIONCODE from AUMS_VER_TO_DEV TPVTD WHERE TPVTD.DEVID=");
			checkDeviceSql.append("'" + device + "'");
			checkDeviceSql.append("order by to_number(updatepolicyid) desc");
			String oldVersionID = "";
			String oldVersionCode = "";
			TCResult tcVerResult = P_Jdbc.dmlSelect(null, checkDeviceSql.toString(), -1);
			AppLogger.info("=======3");
			if (tcVerResult==null || tcVerResult.getStatus()==2) {
				AppLogger.info("=======4");
				addDeviceIdList.add(device);
			} else {
				AppLogger.info("=======5");
				JavaList versionList = (JavaList)tcVerResult.getOutputParams().get(1);
				oldVersionID = (String) versionList.getListItem(0).get(0);
				oldVersionCode = (String) versionList.getListItem(0).get(1);
				AppLogger.info("=======6");
				if (oldVersionCode.equals(versionCode)) {
					AppLogger.info("=======7");
					String checkDeviceStatusSql = "select devid,status from AUMS_VER_LOG where adid = '"
							+ oldVersionID + "' and devid='" + device + "'";
					TCResult versionStatusTCResult = P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
					if (versionStatusTCResult== null || versionStatusTCResult.getStatus()==2) {
						AppLogger.info("=======8");
						wrongDeviceIdMap.put(device, device);
					} else {
						AppLogger.info("=======9");
						JavaList versionStatusFaileIdList = (JavaList) versionStatusTCResult.getOutputParams().get(1);
						for (int j = 0; j < versionStatusFaileIdList.size(); j++) {
							ArrayList loopInfo = versionStatusFaileIdList.getListItem(j);
							AppLogger.info("=======10");
							if (loopInfo.get(1).toString().equals("4")) {
								updateDeviceIdList.add(loopInfo.get(0).toString());
								AppLogger.info("=======11");
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
					JavaList oldVersionList = (JavaList)P_Jdbc.dmlSelect(null, queryOldVerisonCode, -1).getOutputParams().get(1);
					String oldVerisonCode = String.valueOf(oldVersionList.getListItem(0).get(0));
					
					//****_20181115_0002
					AppLogger.info("=======14");
					String [] array_temp1 = versionCode.split("_");
					String [] array_temp2 = oldVerisonCode.split("_");
					String versionIdVer = array_temp1[array_temp1.length-2]+array_temp1[array_temp1.length-1];
					String oldVersionIdVer = array_temp2[array_temp2.length-2]+array_temp2[array_temp2.length-1];
					
					if (Long.parseLong(versionIdVer) > Long
							.parseLong(oldVersionIdVer)) {
						AppLogger.info("=======15");
						String checkDeviceStatusSql = "select devid,status from aums_ver_log where adid = '"
								+ oldVersionID + "' and devid='" + device + "'";
						TCResult versionStatusTCResult = P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
						
						if (versionStatusTCResult==null || versionStatusTCResult.getStatus()==2) {
							AppLogger.info("=======16");
							wrongDeviceIdMap.put(device, device);
						} else {
							AppLogger.info("=======17");
							JavaList versionStatusFaileIdList = (JavaList)versionStatusTCResult.getOutputParams().get(1);
							for (int j = 0; j < versionStatusFaileIdList.size(); j++) {
								AppLogger.info("=======18");
								ArrayList loopInfo =  versionStatusFaileIdList.getListItem(j);
								if (loopInfo.get(1).toString().equals("3")
										|| loopInfo.get(1).toString().equals("4")) {
									updateDeviceIdList.add(loopInfo.get(0).toString());
									AppLogger.info("=======19");
								} else {
									AppLogger.info("=======20");
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
				AppLogger.info("startTime==="+startTime);
				AppLogger.info("插入策略表1");
				P_Jdbc.executeSQL(null, addPolicy.toString(), true);
				AppLogger.info("插入策略表1成功");
				isInsertPolicyFlag = true;
				for (String deviceId : addDeviceIdList) {
					AppLogger.info("操作的设备ID为：【"+deviceId+"】");
					StringBuffer deviceInfoSb = new StringBuffer(
							"select branchno,devtype,devnum from AUMS_V_DEVRUNNINGINFOVIEW where DEVID=");
					deviceInfoSb.append("'" + deviceId + "'");
					
					JavaList deviceList = (JavaList) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1).getOutputParams().get(1);
					
					String devNum = (String) deviceList.getListItem(0).get(2);
					String devBranchNo = (String) deviceList.getListItem(0).get(0);
					String tmpDevType = (String) deviceList.getListItem(0).get(1);
					
					String devbrno = (devBranchNo == "" ? "9999" : devBranchNo).toString();
					String devType = (tmpDevType == "" ? "VTM" : tmpDevType).toString();
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
							"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
					selectVersionIdByDevId
							.append("'"
									+ deviceId
									+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id union SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_branchverinfo TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
							selectVersionIdByDevId
							.append("'"
									+ deviceId
									+ "'order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id");
					TCResult tmpTCResult = null; 
					tmpTCResult = P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
					AppLogger.info("操作的SQL为：【"+selectVersionIdByDevId.toString()+"】");
					if(tmpTCResult==null || tmpTCResult.getStatus()==2){
						//暂时会出现查不到的情况，这个问题还要待查，暂时先continue掉
						AppLogger.info("操作的设备ID为：【"+deviceId+"】,查询内容为空");
						continue;
					}
					JavaList list = (JavaList) tmpTCResult.getOutputParams().get(1);
					
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
								ArrayList fileArr = list.getListItem(i);
								fileInfo.setId((fileArr.get(8) == null ? "空"
										: fileArr.get(8)).toString());
								fileInfo.setMd5((fileArr.get(9) == null ? "空"
										: fileArr.get(9)).toString());
								fileInfo.setPath((fileArr.get(10) == null ? "空"
										: fileArr.get(10)).toString());
								fileInfo.setVersionid((fileArr.get(11) == null ? "空"
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
						"select branchno,devtype,devnum from AUMS_V_DEVRUNNINGINFOVIEW  where DEVID=");
				deviceInfoSb.append("'" + deviceId + "'");
				
				JavaList deviceList = (JavaList) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1).getOutputParams().get(1);
				
				String devNum = (String) deviceList.getListItem(0).get(2);
				String devBranchNo = (String) deviceList.getListItem(0).get(0);
				String tmpDevType = (String) deviceList.getListItem(0).get(1);
				
				String devbrno = "";
				String devType = "";
				if(devBranchNo.equals("")){
					devbrno = "9999";
				}else{
					devbrno = devBranchNo;
				}
				if(tmpDevType.equals("")){
					devType = "VTM";
				}else{
					devType = tmpDevType;
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
				JavaList list = (JavaList) P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1).getOutputParams().get(1);
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
							ArrayList fileArr =  list.getListItem(i);
							fileInfo.setId((fileArr.get(8) == null ? "空"
									: fileArr.get(8)).toString());
							fileInfo.setMd5((fileArr.get(9) == null ? "空"
									: fileArr.get(9)).toString());
							fileInfo.setPath((fileArr.get(10) == null ? "空"
									: fileArr.get(10)).toString());
							fileInfo.setVersionid((fileArr.get(11) == null ? "空"
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
			return new TCResult(0, ErrorCode.REMOTE, illegalityDeviceIdList + "VersionManage==机具已有最新版本");
		}
		if (!(jedis == null)) {
			jedis.close();
		}
		return new TCResult(1, "000000", "发布完成");
	}
	
	
	/**
	 * @category 按设备发布版本
	 * @param versioncode
	 *            入参|关联版本号| {@link String}
	 * @param devidList
	 *            入参|机具ID| {@link JavaList}
	 * @param devaddmode
	 *            入参|添加方式| {@link String}
	 * @param branchNo
	 * 			    入参|操作机构号|{@link String}
	 */
	@InParams(param = {
			@Param(name = "versionCode", comment = "关联版本号",  type = String.class),
			@Param(name = "devidList", comment = "机具ID", type = JavaList.class),
			@Param(name = "devaddmode", comment = "添加方式",  type = String.class),
			@Param(name = "branchNo", comment = "操作机构号", type = String.class)})
	@Returns(returns = { @Return(id = "0", desp = "按设备发布版本成功"),
			@Return(id = "1", desp = "按设备发布版本失败") })
	@Component(label = "按设备发布版本", comment = "按设备发布版本", date = "2017-10-09 10:47:19")
	public static TCResult A_PublicVersionByDev(String versionCode,JavaList devidList, String devaddmode,String branchNo) throws Exception {
		String iseffect = "1";
		String batchperiod = "10";
		String batchnum = "100";
		
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
			return new TCResult(0, ErrorCode.REMOTE, "暂不支持此类型数据库:【"+dbType+"】");
		}
		//获取序列号完成
		//判断所选机具是否为空
		if (devidList == null || devidList.size()==0) {
			return new TCResult(0, ErrorCode.REMOTE, "请选择您需要发布版本的机具");
		}
		Date nowStartTime = new Date();
		SimpleDateFormat sdfStartTime = new SimpleDateFormat("yyyyMMddHHmmss");
		String startTime = sdfStartTime.format(nowStartTime);
		
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
		String versionIDSQL = "select versionid from aums_ver_info where versioncode='" + versionCode + "'";
		JavaList versionIDList =  (JavaList) P_Jdbc.dmlSelect(null, versionIDSQL, -1).getOutputParams().get(1);
		String versionID = (String) versionIDList.getListItem(0).get(0);
		//格式化需要发布的机具列表
		for (int i=0;i<devidList.size();i++) {
			String device = devidList.getStringItem(i);
			StringBuffer checkDeviceSql = new StringBuffer(
					"select VERSIONID,VERSIONCODE from AUMS_VER_TO_DEV TPVTD WHERE TPVTD.DEVID=");
			checkDeviceSql.append("'" + device + "'");
			checkDeviceSql.append("order by to_number(updatepolicyid) desc");
			String oldVersionID = "";
			String oldVersionCode = "";
			TCResult tcVerResult = P_Jdbc.dmlSelect(null, checkDeviceSql.toString(), -1);
			if (tcVerResult==null || tcVerResult.getStatus()==2) {
				addDeviceIdList.add(device);
			} else {
				JavaList versionList = (JavaList)tcVerResult.getOutputParams().get(1);
				oldVersionID = (String) versionList.getListItem(0).get(0);
				oldVersionCode = (String) versionList.getListItem(0).get(1);
				
				if (oldVersionCode.equals(versionCode)) {
					String checkDeviceStatusSql = "select devid,status from AUMS_VER_LOG where adid = '"
							+ oldVersionID + "' and devid='" + device + "'";
					TCResult versionStatusTCResult = P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
					if (versionStatusTCResult== null || versionStatusTCResult.getStatus()==2) {
						wrongDeviceIdMap.put(device, device);
					} else {
						JavaList versionStatusFaileIdList = (JavaList) versionStatusTCResult.getOutputParams().get(1);
						for (int j = 0; j < versionStatusFaileIdList.size(); j++) {
							ArrayList loopInfo = versionStatusFaileIdList.getListItem(j);
							if (loopInfo.get(1).toString().equals("4")) {
								updateDeviceIdList.add(loopInfo.get(0).toString());
							} else {
								wrongDeviceIdMap.put(loopInfo.get(0).toString(),
										loopInfo.get(0).toString());
							}
						}
					}
				} else {
					String queryOldVerisonCode = "SELECT VERSIONCODE FROM aums_ver_info where VERSIONID = '" + oldVersionID + "'";
					JavaList oldVersionList = (JavaList)P_Jdbc.dmlSelect(null, queryOldVerisonCode, -1).getOutputParams().get(1);
					String oldVerisonCode = String.valueOf(oldVersionList.getListItem(0).get(0));
					
					String [] array_temp1 = versionCode.split("_");
					String [] array_temp2 = oldVerisonCode.split("_");
					String versionIdVer = array_temp1[array_temp1.length-2]+array_temp1[array_temp1.length-1];
					String oldVersionIdVer = array_temp2[array_temp2.length-2]+array_temp2[array_temp2.length-1];
					if (Long.parseLong(versionIdVer) > Long
							.parseLong(oldVersionIdVer)) {
						String checkDeviceStatusSql = "select devid,status from aums_ver_log where adid = '"
								+ oldVersionID + "' and devid='" + device + "'";
						TCResult versionStatusTCResult = P_Jdbc.dmlSelect(null,checkDeviceStatusSql,-1);
						
						if (versionStatusTCResult==null || versionStatusTCResult.getStatus()==2) {
							wrongDeviceIdMap.put(device, device);
						} else {
							JavaList versionStatusFaileIdList = (JavaList)versionStatusTCResult.getOutputParams().get(1);
							for (int j = 0; j < versionStatusFaileIdList.size(); j++) {
								ArrayList loopInfo =  versionStatusFaileIdList.getListItem(j);
								if (loopInfo.get(1).toString().equals("3")
										|| loopInfo.get(1).toString().equals("4")) {
									updateDeviceIdList.add(loopInfo.get(0).toString());
								} else {
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
					AppLogger.info("操作的设备ID为：【"+deviceId+"】");
					StringBuffer deviceInfoSb = new StringBuffer(
							"select branchno,devtype,devnum from AUMS_V_DEVRUNNINGINFOVIEW where DEVID=");
					deviceInfoSb.append("'" + deviceId + "'");
					
					JavaList deviceList = (JavaList) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1).getOutputParams().get(1);
					
					String devNum = (String) deviceList.getListItem(0).get(2);
					String devBranchNo = (String) deviceList.getListItem(0).get(0);
					String tmpDevType = (String) deviceList.getListItem(0).get(1);
					
					String devbrno = (devBranchNo == "" ? "9999" : devBranchNo).toString();
					String devType = (tmpDevType == "" ? "VTM" : tmpDevType).toString();
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
					
					StringBuffer selectVersionIdByDevId = new StringBuffer(
							"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo_main TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
					selectVersionIdByDevId
							.append("'"
									+ deviceId
									+ "' order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id");
					TCResult tmpTCResult = null; 
					tmpTCResult = P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
					AppLogger.info("操作的SQL为：【"+selectVersionIdByDevId.toString()+"】");
					if(tmpTCResult==null || tmpTCResult.getStatus()==2){
						//暂时会出现查不到的情况，这个问题还要待查，暂时先continue掉
						AppLogger.info("操作的设备ID为：【"+deviceId+"】,查询内容为空");
						continue;
					}
					JavaList list = (JavaList) tmpTCResult.getOutputParams().get(1);
					
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
								ArrayList fileArr = list.getListItem(i);
								fileInfo.setId((fileArr.get(8) == null ? "空"
										: fileArr.get(8)).toString());
								fileInfo.setMd5((fileArr.get(9) == null ? "空"
										: fileArr.get(9)).toString());
								fileInfo.setPath((fileArr.get(10) == null ? "空"
										: fileArr.get(10)).toString());
								fileInfo.setVersionid((fileArr.get(11) == null ? "空"
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
						"select branchno,devtype,devnum from AUMS_V_DEVRUNNINGINFOVIEW  where DEVID=");
				deviceInfoSb.append("'" + deviceId + "'");
				
				JavaList deviceList = (JavaList) P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1).getOutputParams().get(1);
				
				String devNum = (String) deviceList.getListItem(0).get(2);
				String devBranchNo = (String) deviceList.getListItem(0).get(0);
				String tmpDevType = (String) deviceList.getListItem(0).get(1);
				
				String devbrno = "";
				String devType = "";
				if(devBranchNo.equals("")){
					devbrno = "9999";
				}else{
					devbrno = devBranchNo;
				}
				if(tmpDevType.equals("")){
					devType = "VTM";
				}else{
					devType = tmpDevType;
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
				StringBuffer selectVersionIdByDevId = new StringBuffer(
						"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS  fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo_main TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
				selectVersionIdByDevId
						.append("'"
								+ deviceId
								+ "' order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id");
				JavaList list = (JavaList) P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1).getOutputParams().get(1);
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
							ArrayList fileArr =  list.getListItem(i);
							fileInfo.setId((fileArr.get(8) == null ? "空"
									: fileArr.get(8)).toString());
							fileInfo.setMd5((fileArr.get(9) == null ? "空"
									: fileArr.get(9)).toString());
							fileInfo.setPath((fileArr.get(10) == null ? "空"
									: fileArr.get(10)).toString());
							fileInfo.setVersionid((fileArr.get(11) == null ? "空"
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
			return new TCResult(0, ErrorCode.REMOTE, illegalityDeviceIdList + "VersionManage==机具已有最新版本");
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
			StringBuffer deviceInfoSb = new StringBuffer(
					"select devid from aums_dev_info where devuniqueid is not null and DEVID=");
			deviceInfoSb.append("'" + item + "'");
			
			TCResult deviceTcResult = P_Jdbc.dmlSelect(null, deviceInfoSb.toString(), -1);
			if (deviceTcResult == null || deviceTcResult.getStatus() ==2) {
				result.add(item);
			}
		}
		return result;
	}
	
	
	/**
	 * @category 列出所有的做过升级的策略,按策略进行回退
	 */
	@InParams(param = { @Param(name = "loginBrno", comment = "登陆机构号", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Component(label = "列出所有已升级的策略", comment = "列出所有已升级的策略", style = "选择型", type = "同步组件",author = "alpha", date = "2018-03-21 02:26:38")
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	public static TCResult A_RollbackPolicyList(String loginBrno) {
		
		String branchNo = null;
		String branchNoSql = "SELECT BRANCHNO FROM AUMS_BRANCHINFO WHERE BRANCHID ='" + loginBrno + "'";
		TCResult branchNoQuery = P_Jdbc.dmlSelect(null, branchNoSql, -1);
		if (branchNoQuery != null) {
			if (branchNoQuery.getStatus() != 2) {
				//获取机构号
				JavaList branchInfoList = (JavaList) branchNoQuery.getOutputParams().get(1);
				branchNo = branchInfoList.getListItem(0).get(0).toString();
				if (branchNo == null || branchNo =="") {
					return new TCResult(0, ErrorCode.REMOTE, "机构号非法");
				}
				AppLogger.info("获取到的机构号为：" + branchNo);
			}
		} else {
			return new TCResult(0, ErrorCode.REMOTE, "查询机构信息异常");
		}
		
		//int lenBranchNo = branchNo.length()+8;
		
		// 获取登录用户
		String brno = branchNo;

		// 获取所有升级的策略
		String policySQL = "select UPDATEPOLICYID,brno,applydate from aums_ver_versionupdatepolicy where policytype='0' order by to_number(updatepolicyid) desc";
		TCResult policyQueryResult = null;
		policyQueryResult = P_Jdbc.dmlSelect(null, policySQL, -1);
		if (policyQueryResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询所有策略信息异常");
		}
		if (policyQueryResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "策略信息无满足条件的记录");
		}
		
		JavaList loopTmpList = (JavaList) policyQueryResult.getOutputParams().get(1);
		AppLogger.info("loopTmpList========"+loopTmpList);
		JavaList result = new JavaList();
		// 将用户所能看到的机具与拥有最新版本的机具进行对比，筛选出用户可以进行版本回退的机具
		for (int i=0;i<loopTmpList.size();i++) {
			String updatepolicyid = loopTmpList.getListItem(i).get(0).toString();
			String policybrno = loopTmpList.getListItem(i).get(1).toString();
			String applydate = loopTmpList.getListItem(i).get(2).toString();
			AppLogger.info("updatepolicyid="+updatepolicyid+",    policybrno="+policybrno+",    applydate="+applydate);
			// 根据策略ID，获取版本号及描述
			String getVersionInfoSql = "select a2.versioncode,a2.description,a3.status from aums_ver_to_dev a1,aums_ver_branchverinfo a2,aums_ver_log a3 where a1.versionid=a2.versionid and a3.adid=a2.versionid and a1.updatepolicyid='" + updatepolicyid + "'";
			
			TCResult versionInfoResult = P_Jdbc.dmlSelect(null, getVersionInfoSql, -1);
			if (versionInfoResult == null) {
				return new TCResult(0, ErrorCode.REMOTE, "根据策略ID查询版本信息异常");
			}
			if (versionInfoResult.getStatus() == 2) {
				AppLogger.info("版本回退，策略ID查询版本信息内容为空");
				continue;
			}
			JavaList versionInfoList = (JavaList)versionInfoResult.getOutputParams().get(1);
			// 判断策略操作网点号和登录用户是否相同或是其上级
			if (policybrno.equals(brno)) {
				// 版本策略ID、版本号、版本发布日期、版本描述、版本状态
				JavaDict dict = new JavaDict();
				dict.put("strategy_Id", updatepolicyid);
				dict.put("versionCode", versionInfoList.getListItem(0).get(0).toString());
				dict.put("applyDate", applydate);
				dict.put("adDescription", versionInfoList.getListItem(0).get(1).toString());
				dict.put("adStatus", versionInfoList.getListItem(0).get(2).toString());
				result.add(dict);
			} else if (querySJJGXY(brno, policybrno)) {
				// 回退机构号是升级操作的上级管理机构
				// 版本策略ID、版本号、版本发布日期、版本描述【第一层界面返回】
				JavaDict dict = new JavaDict();
				dict.put("strategy_Id", updatepolicyid);
				dict.put("versionCode", versionInfoList.getListItem(0).get(0).toString());
				dict.put("applyDate", applydate);
				dict.put("adDescription", versionInfoList.getListItem(0).get(1).toString());
				dict.put("adStatus", versionInfoList.getListItem(0).get(2).toString());
				result.add(dict);
			}
		}
		return TCResult.newSuccessResult(result);
	}
	
	/**
	 * 查询操作回退的网点是否为原升级操作网点的上级【XX银行客户化】 递归查询
	 * 
	 * @param currentbrno
	 *            操作回退的网点
	 * @param operatebrno
	 *            原升级操作的网点
	 * @return
	 */
	public static Boolean querySJJGXY(String currentbrno, String operatebrno) {
		// 查询上级机构
		String queryBrnoSql = "select fatherbranchid from aums_branchinfo where branchno='"
				+ operatebrno + "'";

		TCResult brnoResult = P_Jdbc.dmlSelect(null, queryBrnoSql, -1);
		if (brnoResult == null) {
			AppLogger.info("递归查询机构信息，查询机构信息异常");
			return false;
		}
		if (brnoResult.getStatus() == 2) {
			AppLogger.info("递归查询机构信息，查询机构信息内容为空");
			return false;
		}
		JavaList tmpList = (JavaList)brnoResult.getOutputParams().get(1);
		if(tmpList.getListItem(0).get(0)==null){
			//直接父节点为空，则认为为超管机构
			return true;
		}
		String fatherBrno = tmpList.getListItem(0).get(0).toString();
		if (fatherBrno.equals("")) {
			//直接父节点为空，则认为为超管机构
			return true;
		} else {
			if (fatherBrno.equals(currentbrno)) {
				return true;
			} else if (fatherBrno.equals("adminBranch")){
				//父节点为adminBranch标识该机构为总行
				return true;
			}else {
				querySJJGXY(currentbrno, fatherBrno);
			}
		}
		return false;
	}

	
	/**
	 * @category 根据策略ID查询返回需要回滚的机具列表
	 * @param policyId
	 *            入参|版本更新策略ID|{@link java.lang.String}
	 * @return
	 */
	@InParams(param = { @Param(name = "policyId", comment = "版本更新策略ID", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "需要回滚的机具列表", comment = "需要回滚的机具列表", style = "选择型", type = "同步组件",author = "alpha", date = "2018-03-15 02:26:38")
	public static TCResult A_RollbackDevList(String policyId) {

		// 查询出数据库所有策略ID的机具列表
		String devidListsql = "select A1.devid,A1.devbrno,A2.versioncode,A2.description from aums_ver_to_dev A1,aums_ver_info A2 where A1.versionid=A2.versionid and A1.updatepolicyid='" + policyId + "'";
		TCResult devListResult = P_Jdbc.dmlSelect(null, devidListsql, -1);
		if (devListResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "查询需要版本回退的机具列表异常");
		}
		if (devListResult.getStatus() == 2) {
			return new TCResult(2, ErrorCode.REMOTE, "查询需要版本回退的机具列表内容为空");
		}
		
		// 返回客户端文件列表
		JavaList tmpList = (JavaList) devListResult.getOutputParams().get(1);
		JavaList result = new JavaList();
		// 将用户所能看到的机具与拥有最新版本的机具进行对比，筛选出用户可以进行版本回退的机具
		for (int i = 0; i < tmpList.size(); i++) {
			String devid = tmpList.getListItem(i).get(0).toString();// 设备编号
			String devBrno = tmpList.getListItem(i).get(1).toString();// 设备网点号
			String versionCode = tmpList.getListItem(i).get(2).toString();// 版本号
			// 查询机具的上一版本号
			String queryUpVerSql = "select A1.updatepolicyid,A2.versioncode,A2.description from aums_ver_to_dev A1,aums_ver_info A2 where A1.versionid=A2.versionid and A1.devid='"
					+ devid
					+ "' and A1.updatepolicyid!='"
					+ policyId
					+ "' order by A1.updatepolicyid desc";
			TCResult queryVerResult = P_Jdbc.dmlSelect(null, queryUpVerSql, -1);
			if (queryVerResult == null) {
				return new TCResult(0, ErrorCode.REMOTE, "查询需要版本回退的机具列表异常");
			}
			if (queryVerResult.getStatus() == 2) {
				AppLogger.info("查询需要版本回退的机具列表内容为空");
				return new TCResult(2, ErrorCode.REMOTE, "查询需要版本回退的机具无上一版本");
			}
			// 目前取第一个版本号
			JavaList tmpVerList = (JavaList)queryVerResult.getOutputParams().get(1);
			String upVerCode = tmpVerList.getListItem(0).get(1).toString();
			
			JavaDict dict = new JavaDict();
			dict.put("devNum", devid);
			dict.put("branchNo", devBrno);
			dict.put("versionCode", versionCode);
			dict.put("upperVersionCode", upVerCode);
			result.add(dict);
		}
		return TCResult.newSuccessResult(result);
	}
	
	public static int findStrIndex(String Str, int Idx ,String Match){
		/*
		 * 返回字符串指定字符第N次出现的下标
		 * 参数1  Str   原始字符串
		 * 参数2  Idx   指定查找第N次出现的位置
		 * 参数3  Match 要查找的字符
		 */
        int number = 0;
        int i;
        Pattern pattern = Pattern.compile(Match);  
        Matcher findMatcher = pattern.matcher(Str);  
        
        while(findMatcher.find()) {  
            number++;  
           if(number == Idx){
              break;  
           }
        }
        if (number>0 && Idx<=number) {
        	i = findMatcher.start();
		}else {
			i = -1;
		}
        //AppLogger.info("字符"+ Match +"第"+Idx+"次出现的位置是"+i);
		return i;
	}
	
}
