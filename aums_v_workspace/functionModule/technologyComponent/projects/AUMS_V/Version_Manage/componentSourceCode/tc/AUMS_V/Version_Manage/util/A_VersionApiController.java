package tc.AUMS_V.Version_Manage.util;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import tc.AUMS_V.Version_Manage.util.depend.A_OptionsVo;
import tc.AUMS_V.Version_Manage.util.depend.FilesDetails;
import tc.AUMS_V.Version_Manage.util.depend.VersionFilesList;
import tc.platform.P_Jdbc;
import tc.platform.P_Json;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;


/**
 * 版本基础信息返回
 * 
 * @date 2017-09-29 9:8:26
 */

@ComponentGroup(level = "应用", groupName = "版本基础信息返回")
public class A_VersionApiController {

	/**
	 * @category 根据设备唯一标识返回版本信息
	 * @param machineUniqueId
	 *            入参|设备唯一标识|{@link java.lang.String}
	 * @return
	 */
	@InParams(param = { @Param(name = "machineUniqueId", comment = "设备唯一标识", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "设备关联版本信息获取", comment = "设备关联版本信息获取", style = "判断型", type = "同步组件", author = "alpha", date = "2018-03-15 02:26:38")
	public static TCResult getcurrentVersion(String machineUniqueId)
			throws Exception {
		
		/*String file_separator = System.getProperty("file.separator");
		
		String devIdSQL = "select devid from aums_Dev_info where devuniqueid='"
				+ machineUniqueId + "'";

		TCResult queryDevIdResult = P_Jdbc.dmlSelect(null, devIdSQL, -1);

		VersionFilesList versionInfo = new VersionFilesList();

		if (queryDevIdResult == null) {
			AppLogger.info("设备基础信息查询异常");
			versionInfo.setDescription("");
			List tmpList = new ArrayList();
			versionInfo.setFiles(tmpList);
			versionInfo.setId("");
			versionInfo.setName("");
			A_OptionsVo tmpVo = new A_OptionsVo();
			versionInfo.setOptions(tmpVo);
			JSONObject object = new JSONObject();
			object.put("result", versionInfo);
			object.put("success", false);
//			return TCResult.newSuccessResult(JSON.toJSONString(versionInfo));
			return TCResult.newSuccessResult(object.toJSONString());
		}

		if (queryDevIdResult.getStatus() == 2) {
			versionInfo.setDescription("");
			List tmpList = new ArrayList();
			versionInfo.setFiles(tmpList);
			versionInfo.setId("");
			versionInfo.setName("");
			A_OptionsVo tmpVo = new A_OptionsVo();
			versionInfo.setOptions(tmpVo);
			JSONObject object = new JSONObject();
			object.put("result", versionInfo);
			object.put("success", false);
			return TCResult.newSuccessResult(object.toJSONString());
		}
		JavaList devIdList = (JavaList) queryDevIdResult.getOutputParams().get(1);
		String devId = devIdList.getListItem(0).get(0).toString();

		A_OptionsVo optionsInfo = new A_OptionsVo();

		List<FilesDetails> fileDetailList = new ArrayList<FilesDetails>();
		byte[] versionInfoByte = null;
		// 保留原来的方法，如果从redis读取失败则从数据库取出相应的数据
		if (versionInfoByte == null) {
			AppLogger.info("从redis读取失败，开始从数据库读取数据");
			
			StringBuffer selectVersionIdByDevId = new StringBuffer(
					"SELECT TPVI.VERSIONID,TPVI.VERSIONCODE,TPVI.DESCRIPTION,TPOS.MATCH_PATTERNS,TPOS.EXCLUDED_FILES,TPOS.EXCLUDED_DIRS,TPOS.ONCE_UPDATE_DIRS,TPOS.ONCE_UPDATE_FILES,TPVID.FILEID,TPVID.MD5,TPVID.FILEPATH,TPVID.VERSIONID AS fileversionid,TPVID.FILESIZE  FROM aums_ver_info TPVI JOIN  aums_ver_detailinfo TPVID ON TPVI.VERSIONID=TPVID.VERSIONID JOIN (select tt.VERSIONID from (select t.VERSIONID from AUMS_VER_TO_DEV t WHERE T.DEVID=");
			selectVersionIdByDevId
					.append("'"
							+ devId
							+ "' order by to_number(t.updatepolicyid) desc) tt where rownum=1) DEVTOVER ON TPVI.VERSIONID=DEVTOVER.VERSIONID JOIN aums_ver_options_template TPOS ON TPOS.OPTIONS_TEMPLATE_ID=TPVI.strategy_id");
			
			TCResult selectVersionIdByDevIdResult = P_Jdbc.dmlSelect(null, selectVersionIdByDevId.toString(), -1);
			
			if(selectVersionIdByDevIdResult.getStatus()==2){
				versionInfo.setDescription("");
				List tmpList = new ArrayList();
				versionInfo.setFiles(tmpList);
				versionInfo.setId("");
				versionInfo.setName("");
				A_OptionsVo tmpVo = new A_OptionsVo();
				versionInfo.setOptions(tmpVo);
				JSONObject object = new JSONObject();
				object.put("result", versionInfo);
				object.put("success", false);
				return TCResult.newSuccessResult(object.toJSONString());
			}
			
			JavaList list = (JavaList)selectVersionIdByDevIdResult.getOutputParams().get(1);
			
			if (!(list.isEmpty())) {
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
					//20180906 李阔修改 将文件path格式由：unzip/v_20180903_0001/dotnet/Plugins/Awp.Ameba.Flow.Mpt.dll改为：dotnet/Plugins/Awp.Ameba.Flow.Mpt.dll
					if(fileArr.get(10) == null){
						fileInfo.setVPath("空");
						fileInfo.setPath("空");
					}else{
						String orgipath = fileArr.get(10).toString();
						String vPath = orgipath.substring(findStrIndex(orgipath,1,file_separator)+1,findStrIndex(orgipath,4,file_separator)+1);
						String rPath = orgipath.substring(findStrIndex(orgipath,4,file_separator)+1);
						fileInfo.setVPath(vPath);
						fileInfo.setPath(rPath);
					}
					fileInfo.setVersionid((fileArr.get(11) == null ? "空"
							: fileArr.get(11)).toString());
					fileInfo.setSize((fileArr.get(12) == null ? "空"
							: fileArr.get(12)).toString());
					fileDetailList.add(fileInfo);
				}
				versionInfo.setFiles(fileDetailList);
			}
		}
		AppLogger.info("return从versionInfo:" + versionInfo.toString());
		JSONObject object = new JSONObject();
		object.put("result", versionInfo);
		object.put("success", true);
		return TCResult.newSuccessResult(object.toJSONString());
		*/
		return TCResult.newSuccessResult();
	}
	
	
	/**
	 * @category NEW根据设备信息返回版本信息
	 * @param machineUniqueId
	 *            入参|设备唯一标识|{@link java.lang.String}
	 * @param machineOrganNo
	 *            入参|设备所属机构号|{@link java.lang.String}
	 * @param machineBranchNo
	 *            入参|设备所属分行机构号|{@link java.lang.String}
	 * @param result
	 *            出参|返回C端报文|{@link java.lang.String}
	 * @return
	 */
	@InParams(param = { @Param(name = "machineUniqueId", comment = "设备唯一标识", type = java.lang.String.class),
			            @Param(name = "machineOrganNo", comment = "设备所属机构号", type = java.lang.String.class),
			            @Param(name = "machineBranchNo", comment = "设备所属分行机构号", type = java.lang.String.class)})
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "NEW设备关联版本信息获取", comment = "NEW设备关联版本信息获取", style = "判断型", type = "同步组件", author = "alpha", date = "2018-03-15 02:26:38")
	public static TCResult getcurrentVersionNew(String machineUniqueId,
			String machineOrganNo,String machineBranchNo)
			throws Exception {

		if(machineUniqueId == null || machineUniqueId.equals("")){
			return new TCResult(0, ErrorCode.REMOTE, "设备唯一标识为空");
		}
		if(machineOrganNo == null || machineOrganNo.equals("")){
			return new TCResult(0, ErrorCode.REMOTE, "设备所属机构号为空");
		}
		if(machineBranchNo == null || machineBranchNo.equals("")){
			return new TCResult(0, ErrorCode.REMOTE, "设备所属分行机构号为空");
		}
		
		//公共信息初始化
		VersionFilesList versionInfo = new VersionFilesList();
		A_OptionsVo optionsInfo = new A_OptionsVo();
		List<FilesDetails> fileDetailList = new ArrayList<FilesDetails>();
		String file_separator = System.getProperty("file.separator");
		String branchSpecilRuleId = "";
		String branchRuleId = "";
		String mainRuleId = "";
		
		AppLogger.info("======1======1=======");
		//步骤1-根据设备所属分行查询分行特色版本
		String SpecialRuleSQL = "select RULE.RULEID,RULE.VERSIONID,RULE.VERSIONCODE,RULE.POLICYID from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where RULE.VERSIONID=EXT.VERSIONID and EXT.VERSIONATTR='1' and RULE.OPERBRANCHNO='"+ machineBranchNo +"' and RULE.VERSIONTYPE='2' and RULE.VERSIONSTATUS='1' order by RULE.POLICYID desc";
		TCResult SpecialRuleResult = P_Jdbc.dmlSelect(null, SpecialRuleSQL, -1);
		if (SpecialRuleResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
		}
		if (SpecialRuleResult.getStatus() == 1) {
			//分行特色版本版本处理
			AppLogger.info("======1======2=======");
			JavaList SpecialRuleResultList = (JavaList)SpecialRuleResult.getOutputParams().get(1);
			for (int i = 0; i < SpecialRuleResultList.size(); i++) {
				AppLogger.info("======1======3======="+i);
				String ruleid_tmp = SpecialRuleResultList.getListItem(i).get(0).toString();
				AppLogger.info("ruleid_tmp===="+ruleid_tmp);
				//查询此条ruleid是否有对应的试点信息
				String selectTestSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"'";
				TCResult selectTestSqlResult = P_Jdbc.dmlSelect(null, selectTestSql.toString(), -1);
				if (selectTestSqlResult == null) {
					return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
				}else if (selectTestSqlResult.getStatus()==2) {
					AppLogger.info("======1======4======="+i);
					//此版本无试点,为正式版本,将发布规则标志传到外部
					branchSpecilRuleId = ruleid_tmp;
					break;
				}else if (selectTestSqlResult.getStatus()==1) {
					AppLogger.info("======1======5======="+i);
					//此版本试点版本,需要判断此试点版本是否适用于此终端
					String selectTestByThisDevSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' and BRANCHID=(select branchid from AUMS_BRANCHINFO where branchno='"+ machineOrganNo +"') union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"' and DEVICEID='"+ machineUniqueId +"'";
					TCResult selectTestByThisDevResult = P_Jdbc.dmlSelect(null, selectTestByThisDevSql.toString(), -1);
					if (selectTestByThisDevResult == null) {
						return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
					}
					if (selectTestByThisDevResult.getStatus() == 1) {
						AppLogger.info("======1======6======="+i);
						//此版本为试点版本,并且该终端满足试点条件,将发布规则标志传到外部
						branchSpecilRuleId = ruleid_tmp;
						break;
					}
				}
			}
		}
		AppLogger.info("======2======1=======");
		//步骤2-根据设备所属分行查询总行开发的分行版本信息
		String BranchRuleSQL = "select RULE.RULEID,RULE.VERSIONID,RULE.VERSIONCODE,RULE.POLICYID from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where EXT.USEBRANCHNO='"+ machineBranchNo +"' and RULE.VERSIONID=EXT.VERSIONID and EXT.VERSIONATTR='1' and RULE.VERSIONTYPE='1' and RULE.VERSIONSTATUS='1' order by RULE.POLICYID desc";
		TCResult BranchRuleResult = P_Jdbc.dmlSelect(null, BranchRuleSQL, -1);
		if (BranchRuleResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
		}
		if (BranchRuleResult.getStatus() == 1) {
			//总行发版的分行版本版本处理
			AppLogger.info("======2======2=======");
			JavaList BranchRuleResultList = (JavaList)BranchRuleResult.getOutputParams().get(1);
			for (int i = 0; i < BranchRuleResultList.size(); i++) {
				AppLogger.info("======2======3======="+i);
				String ruleid_tmp = BranchRuleResultList.getListItem(i).get(0).toString();
				AppLogger.info("ruleid_tmp===="+ruleid_tmp);
				//查询此条ruleid是否有对应的试点信息
				String selectTestSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"'";
				TCResult selectTestSqlResult = P_Jdbc.dmlSelect(null, selectTestSql.toString(), -1);
				if (selectTestSqlResult == null) {
					return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
				}else if (selectTestSqlResult.getStatus()==2) {
					AppLogger.info("======2======4======="+i);
					//此版本无试点,为分行的正式版本,将发布规则标志传到外部
					branchRuleId = ruleid_tmp;
					break;
				}else if (selectTestSqlResult.getStatus()==1) {
					AppLogger.info("======2======5======="+i);
					//此版本为分行的试点版本,需要判断此试点版本是否适用于此终端
					String selectTestByThisDevSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' and BRANCHID=(select branchid from AUMS_BRANCHINFO where branchno='"+ machineOrganNo +"') union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"' and DEVICEID='"+ machineUniqueId +"'";
					TCResult selectTestByThisDevResult = P_Jdbc.dmlSelect(null, selectTestByThisDevSql.toString(), -1);
					if (selectTestByThisDevResult == null) {
						return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
					}
					if (selectTestByThisDevResult.getStatus() == 1) {
						AppLogger.info("======2======6======="+i);
						//此版本为试点版本,并且该终端满足试点条件,将发布规则标志传到外部
						branchRuleId = ruleid_tmp;
						break;
					}
				}
			}
		}
		AppLogger.info("======3======1=======");
		//步骤3-根据设备所属查询主版本
		String MainRuleSQL = "select RULE.RULEID,RULE.VERSIONID,RULE.VERSIONCODE,RULE.POLICYID from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where EXT.USEBRANCHNO=(select branchno from AUMS_BRANCHINFO where FATHERBRANCHID='adminBranch') and RULE.VERSIONID=EXT.VERSIONID and EXT.VERSIONATTR='1' and RULE.VERSIONTYPE='1' and RULE.VERSIONSTATUS='1' order by RULE.POLICYID desc";
		TCResult MainRuleResult = P_Jdbc.dmlSelect(null, MainRuleSQL, -1);
		if (MainRuleResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
		}
		if (MainRuleResult.getStatus() == 1) {
			//主版本处理
			AppLogger.info("======3======2=======");
			JavaList MainRuleResultList = (JavaList)MainRuleResult.getOutputParams().get(1);
			for (int i = 0; i < MainRuleResultList.size(); i++) {
				AppLogger.info("======3======3======="+i);
				String ruleid_tmp = MainRuleResultList.getListItem(i).get(0).toString();
				AppLogger.info("ruleid_tmp===="+ruleid_tmp);
				//查询此条ruleid是否有对应的试点信息
				String selectTestSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"'";
				TCResult selectTestSqlResult = P_Jdbc.dmlSelect(null, selectTestSql.toString(), -1);
				if (selectTestSqlResult == null) {
					return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
				}else if (selectTestSqlResult.getStatus()==2) {
					AppLogger.info("======3======4======="+i);
					//此版本无试点,为正式主版本,将发布规则标志传到外部
					mainRuleId = ruleid_tmp;
					break;
				}else if (selectTestSqlResult.getStatus()==1) {
					AppLogger.info("======3======5======="+i);
					//此版本试点版本,需要判断此试点版本是否适用于此终端
					String selectTestByThisDevSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' and BRANCHID=(select branchid from AUMS_BRANCHINFO where branchno='"+ machineOrganNo +"') union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"' and DEVICEID='"+ machineUniqueId +"'";
					TCResult selectTestByThisDevResult = P_Jdbc.dmlSelect(null, selectTestByThisDevSql.toString(), -1);
					if (selectTestByThisDevResult == null) {
						return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常！！！");
					}
					if (selectTestByThisDevResult.getStatus() == 1) {
						AppLogger.info("======3======6======="+i);
						//此版本为试点版本,并且该终端满足试点条件,将发布规则标志传到外部
						mainRuleId = ruleid_tmp;
						break;
					}
				}
			}
			//循环结束了,还没找到主版本,报错退出
			if (mainRuleId.isEmpty()) {
				return new TCResult(0, ErrorCode.REMOTE, "未找到对应的主版本信息，请核查");
			}
		}
		AppLogger.info("======4======1=======");
		ArrayList<String> ruleidlist = new ArrayList<String>();
		if (!mainRuleId.isEmpty()) { //处理主版本
			ruleidlist.add(mainRuleId);
		}else {
			return new TCResult(0, ErrorCode.REMOTE, "未找到对应的主版本信息，请核查");
		}
		if (!branchRuleId.isEmpty()) { //存在分行版本，处理分行版本
			ruleidlist.add(branchRuleId);
		}
		if (!branchSpecilRuleId.isEmpty()) { //存在分行特色版本，处理分行特色版本
			ruleidlist.add(branchSpecilRuleId);
		}
		
		StringBuffer SB = new StringBuffer("");
		for (int i = 0; i < ruleidlist.size(); i++) {
			SB.append("'"+ ruleidlist.get(i) +"',");
		}
		String ruidlistsql = SB.substring(0, SB.length()-1);
		AppLogger.info("======4======2======="+ruidlistsql);
		//整体版本处理-查询适用此终端的全量版本信息，按照文件名称排序
		
		StringBuffer selectSpecialVersion = new StringBuffer(
				"select tpvi.versionid, tpvi.versioncode, tpvi.description, tpos.match_patterns, tpos.excluded_files, tpos.excluded_dirs, tpos.once_update_dirs, tpos.once_update_files, tpvid.fileid, tpvid.md5, tpvid.filepath, tpvid.fileid, tpvid.filesize, ext.zipfilename from aums_ver_info tpvi ");
		selectSpecialVersion.append(" join aums_ver_info_ext ext on tpvi.versionid= ext.versionid and (ext.operbranchno=ext.usebranchno or ext.usebranchno='"+ machineBranchNo +"')");
		selectSpecialVersion.append(" join aums_ver_detailinfo_main tpvid on ext.zipfileid=tpvid.zipfileid and tpvid.clientpath!='version.json' ");
		selectSpecialVersion.append(" join aums_ver_releaserule rule on rule.versionid=tpvi.versionid ");
		selectSpecialVersion.append(" join aums_ver_options_template tpos on tpos.options_template_id=tpvi.strategy_id ");
		selectSpecialVersion.append(" where rule.ruleid in ("+ ruidlistsql +") order by filepath");
		AppLogger.info("selectSpecialVersion=======【"+selectSpecialVersion.toString()+"】");
		TCResult selectSpecialVersionResult = P_Jdbc.dmlSelect(null, selectSpecialVersion.toString(), -1);
		if (selectSpecialVersionResult == null || selectSpecialVersionResult.getStatus() == 2) {
			return new TCResult(0, ErrorCode.REMOTE, "版本文件明细信息查询异常");
		} else {
			JavaList list = (JavaList)selectSpecialVersionResult.getOutputParams().get(1);
			if (!(list.isEmpty())) {
				AppLogger.info("===mark===3===");
				ArrayList versionArr = list.getListItem(0);
				versionInfo.setId((versionArr.get(0) == null ? "Id空" : versionArr.get(0)).toString());
				versionInfo.setName((versionArr.get(1) == null ? "name空" : versionArr.get(1)).toString());
				versionInfo.setDescription((versionArr.get(2) == null ? "描述空" : versionArr.get(2)).toString());
				optionsInfo.setMatchPatterns((versionArr.get(3) == null ? "匹配模式空" : versionArr.get(3)).toString());
				optionsInfo.setExcludedFiles((versionArr.get(4) == null ? "排除文件空" : versionArr.get(4)).toString());
				optionsInfo.setExcludedDirs((versionArr.get(5) == null ? "排除文件夹空" : versionArr.get(5)).toString());
				optionsInfo.setOnceUpdateDirs((versionArr.get(6) == null ? "空" : versionArr.get(6)).toString());
				optionsInfo.setOnceUpdateFiles((versionArr.get(7) == null ? "空" : versionArr.get(7)).toString());
				versionInfo.setOptions(optionsInfo);
				String zipfilename;
				String vPath;
				for (int i = 0; i < list.size(); i++) {
					ArrayList fileArr = list.getListItem(i);
					FilesDetails fileInfo = new FilesDetails();
					fileInfo.setId((fileArr.get(8) == null ? "空" : fileArr.get(8)).toString());
					fileInfo.setMd5((fileArr.get(9) == null ? "空" : fileArr.get(9)).toString());
					if(fileArr.get(10) == null){
						fileInfo.setVPath("空");
						fileInfo.setPath("空");
					}else{
						//文件path格式由：unzip/share/V_20190101_0001/V_20181201_0001/dotnet/version.json改为：share/V_20190101_0001/V_20181201_0001/dotnet/version.json
						//文件path格式由：unzip/share/V_20190101_0001/V_20181201_0001/dotnet/version.json改为两个字段（unzip不要了），vpath=share/V_20190101_0001/V_20181201_0001/（3部分），path=dotnet/version.json
						String orgipath = fileArr.get(10).toString();
						//String vPath = orgipath.substring(findStrIndex(orgipath,1,file_separator)+1,findStrIndex(orgipath,4,file_separator)+1);
						zipfilename = fileArr.get(13).toString();
						if (isMajorFile(zipfilename)) {
							vPath = "major" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
							//vPath = "major" + File.separator + zipfilename.substring(0, zipfilename.lastIndexOf('.'));
						} else {
							vPath = "department" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
							//vPath = "department" + File.separator + zipfilename.substring(0, zipfilename.lastIndexOf('.'));
						}
						String rPath = orgipath.substring(findStrIndex(orgipath,4,file_separator)+1);
						fileInfo.setVPath(vPath);
						fileInfo.setPath(rPath);
					}
					fileInfo.setVersionid((fileArr.get(11) == null ? "空" : fileArr.get(11)).toString());
					fileInfo.setSize((fileArr.get(12) == null ? "空" : fileArr.get(12)).toString());
					fileDetailList.add(fileInfo);
				}
				
			}
		}
		

		//以上处理完主版本信息，此处开始处理未合并到主版本里的增量版本信息
		//查询满足此机构的试点版本列表
		AppLogger.info("======xxx=======");
		StringBuffer VersionTestInfo = new StringBuffer("select RULE.VERSIONID, MAIN.FILEID, MAIN.MD5, MAIN.FILEPATH, MAIN.ZIPFILENAME, MAIN.FILESIZE, MAIN.CLIENTPATH from AUMS_VER_RELEASERULE RULE ");
		VersionTestInfo.append("join AUMS_VER_INFO_EXT EXT on EXT.VERSIONID = RULE.VERSIONID and EXT.VERSIONATTR = '2' AND (EXT.OPERBRANCHNO=EXT.USEBRANCHNO OR EXT.USEBRANCHNO='"+ machineBranchNo +"') ");
		VersionTestInfo.append("join AUMS_VER_DETAILINFO_MAIN MAIN on EXT.ZIPFILEID = MAIN.ZIPFILEID and MAIN.CLIENTPATH!='version.json' ");
		VersionTestInfo.append("where RULE.TESTFLAG='2' and RULE.VERSIONSTATUS='1' and ");
		VersionTestInfo.append("(exists (select 1 from AUMS_VER_RELEASERULEBYBRNO BRNO where BRNO.RULEID =RULE.RULEID and BRNO.BRANCHID in (select BRANCHID from AUMS_BRANCHINFO where BRANCHNO='"+ machineOrganNo +"')) or exists (select 1 from AUMS_VER_RELEASERULEBYDEV DEV where DEV.RULEID =RULE.RULEID and DEV.DEVICEID='"+ machineUniqueId +"')) order by RULE.POLICYID,MAIN.CREATE_TIME");
		AppLogger.info("VersionTestInfo============="+VersionTestInfo);
		TCResult VersionTestInfoResult = P_Jdbc.dmlSelect(null, VersionTestInfo.toString(), -1);
		
		if (VersionTestInfoResult == null) {
			return new TCResult(0, ErrorCode.REMOTE, "试点明细明细信息查询异常");
		} else if (VersionTestInfoResult.getStatus() == 1) {
			//存在试点版本，处理试点
			JavaList TestList = (JavaList)VersionTestInfoResult.getOutputParams().get(1);
			String zipfilename;
			String vPath;
			for (int i = 0; i < TestList.size(); i++) {
				String clientpath = TestList.getListItem(i).get(6).toString();
				for (int j = 0; j < fileDetailList.size(); j++) {
					FilesDetails fileInfo1 = fileDetailList.get(j);
					if (clientpath.equals(fileInfo1.getPath())) { //如果当前文件与之前的文件相同，则处理
						fileDetailList.remove(j); //将此条明细移除
						break;
					}
				}
				//内层循环结束还没找到对应的文件，则表示此文件为新增的，则直接将此文件加入返回内容
				FilesDetails fileInfo = new FilesDetails();
				fileInfo.setId(TestList.getListItem(i).get(1).toString());
				fileInfo.setMd5(TestList.getListItem(i).get(2).toString());
				if(TestList.getListItem(i).get(3).toString() == ""){
					fileInfo.setVPath("空");
					fileInfo.setPath("空");
				}else{
					String orgipath = TestList.getListItem(i).get(3).toString();
					zipfilename = TestList.getListItem(i).get(4).toString();
					if (isMajorFile(zipfilename)) {
						vPath = "major" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
					} else {
						vPath = "department" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
					}
					String rPath = orgipath.substring(findStrIndex(orgipath,4,file_separator)+1);
					fileInfo.setVPath(vPath);
					fileInfo.setPath(rPath);
				}
				fileInfo.setVersionid(TestList.getListItem(i).get(0).toString());
				fileInfo.setSize(TestList.getListItem(i).get(5).toString());
				fileDetailList.add(fileInfo);
			}
		}
			
		versionInfo.setFiles(fileDetailList);
		
		AppLogger.info("versionInfo==="+versionInfo.toString());
		JSONObject object = new JSONObject();
		object.put("result", versionInfo);
		object.put("success", true);
		return TCResult.newSuccessResult(object.toJSONString());
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
	
	
	/**
	 * @category 检查设备文件合法性
	 * @param machineUniqueId
	 *            入参|设备唯一标识|{@link java.lang.String}
	 * @param machineOrganNo
	 *            入参|设备所属机构号|{@link java.lang.String}
	 * @param machineBranchNo
	 *            入参|设备所属分行机构号|{@link java.lang.String}
	 * @param filesList
	 *            入参|文件列表|{@link JavaList}
	 * @param result
	 *            出参|返回C端报文|{@link JavaList}
	 * @return
	 */
	@InParams(param = { @Param(name = "machineUniqueId", comment = "设备唯一标识", type = java.lang.String.class),
			            @Param(name = "machineOrganNo", comment = "设备所属机构号", type = java.lang.String.class),
			            @Param(name = "machineBranchNo", comment = "设备所属分行机构号", type = java.lang.String.class),
			            @Param(name = "filesList", comment = "文件列表", type = JavaList.class)})
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "检查设备文件合法性", comment = "检查设备文件合法性", style = "判断型", type = "同步组件", author = "alpha", date = "2018-03-15 02:26:38")
	public static TCResult chaekDevicePatchStatus(String machineUniqueId,
			String machineOrganNo,String machineBranchNo,JavaList filesList)
			throws Exception {

		//公共信息初始化
		List<FilesDetails> fileDetailList = new ArrayList<FilesDetails>();
		JSONObject object = new JSONObject();
		String file_separator = System.getProperty("file.separator");
		String branchSpecilRuleId = "";
		String branchRuleId = "";
		String mainRuleId = "";

		if(machineUniqueId == null || machineUniqueId.equals("") ||  machineOrganNo == null || machineOrganNo.equals("") ||
				machineBranchNo == null || machineBranchNo.equals("") || filesList == null || machineBranchNo.isEmpty()){
			object.put("success", false);
			object.put("message", "入参非法");
			return new TCResult(0, ErrorCode.REMOTE, "入参非法");
		}
		
		
		//获取此设备对应的版本文件

		//步骤1-根据设备所属分行查询分行特色版本
		String SpecialRuleSQL = "select RULE.RULEID,RULE.VERSIONID,RULE.VERSIONCODE,RULE.POLICYID from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where RULE.VERSIONID=EXT.VERSIONID and EXT.VERSIONATTR='1' and RULE.OPERBRANCHNO='"+ machineBranchNo +"' and RULE.VERSIONTYPE='2' and RULE.VERSIONSTATUS='1' order by RULE.POLICYID desc";
		TCResult SpecialRuleResult = P_Jdbc.dmlSelect(null, SpecialRuleSQL, -1);
		if (SpecialRuleResult == null) {
			object.put("success", false);
			object.put("message", "数据库操作异常1");
			return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常1");
		}
		if (SpecialRuleResult.getStatus() == 1) {
			//分行特色版本版本处理
			JavaList SpecialRuleResultList = (JavaList)SpecialRuleResult.getOutputParams().get(1);
			for (int i = 0; i < SpecialRuleResultList.size(); i++) {
				String ruleid_tmp = SpecialRuleResultList.getListItem(i).get(0).toString();
				AppLogger.info("ruleid_tmp===="+ruleid_tmp);
				//查询此条ruleid是否有对应的试点信息
				String selectTestSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"'";
				TCResult selectTestSqlResult = P_Jdbc.dmlSelect(null, selectTestSql.toString(), -1);
				if (selectTestSqlResult == null) {
					object.put("success", false);
					object.put("message", "数据库操作异常2");
					return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常2");
				}else if (selectTestSqlResult.getStatus()==2) {
					//此版本无试点,为正式版本,将发布规则标志传到外部
					branchSpecilRuleId = ruleid_tmp;
					break;
				}else if (selectTestSqlResult.getStatus()==1) {
					//此版本试点版本,需要判断此试点版本是否适用于此终端
					String selectTestByThisDevSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' and BRANCHID=(select branchid from AUMS_BRANCHINFO where branchno='"+ machineOrganNo +"') union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"' and DEVICEID='"+ machineUniqueId +"'";
					TCResult selectTestByThisDevResult = P_Jdbc.dmlSelect(null, selectTestByThisDevSql.toString(), -1);
					if (selectTestByThisDevResult == null) {
						object.put("success", false);
						object.put("message", "数据库操作异常3");
						return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常3");
					}
					if (selectTestByThisDevResult.getStatus() == 1) {
						//此版本为试点版本,并且该终端满足试点条件,将发布规则标志传到外部
						branchSpecilRuleId = ruleid_tmp;
						break;
					}
				}
			}
		}
		//步骤2-根据设备所属分行查询总行开发的分行版本信息
		String BranchRuleSQL = "select RULE.RULEID,RULE.VERSIONID,RULE.VERSIONCODE,RULE.POLICYID from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where EXT.USEBRANCHNO='"+ machineBranchNo +"' and RULE.VERSIONID=EXT.VERSIONID and EXT.VERSIONATTR='1' and RULE.VERSIONTYPE='1' and RULE.VERSIONSTATUS='1' order by RULE.POLICYID desc";
		TCResult BranchRuleResult = P_Jdbc.dmlSelect(null, BranchRuleSQL, -1);
		if (BranchRuleResult == null) {
			object.put("success", false);
			object.put("message", "数据库操作异常4");
			return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常4");
		}
		if (BranchRuleResult.getStatus() == 1) {
			//总行发版的分行版本版本处理
			JavaList BranchRuleResultList = (JavaList)BranchRuleResult.getOutputParams().get(1);
			for (int i = 0; i < BranchRuleResultList.size(); i++) {
				String ruleid_tmp = BranchRuleResultList.getListItem(i).get(0).toString();
				AppLogger.info("ruleid_tmp===="+ruleid_tmp);
				//查询此条ruleid是否有对应的试点信息
				String selectTestSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"'";
				TCResult selectTestSqlResult = P_Jdbc.dmlSelect(null, selectTestSql.toString(), -1);
				if (selectTestSqlResult == null) {
					object.put("success", false);
					object.put("message", "数据库操作异常5");
					return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常5");
				}else if (selectTestSqlResult.getStatus()==2) {
					//此版本无试点,为分行的正式版本,将发布规则标志传到外部
					branchRuleId = ruleid_tmp;
					break;
				}else if (selectTestSqlResult.getStatus()==1) {
					//此版本为分行的试点版本,需要判断此试点版本是否适用于此终端
					String selectTestByThisDevSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' and BRANCHID=(select branchid from AUMS_BRANCHINFO where branchno='"+ machineOrganNo +"') union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"' and DEVICEID='"+ machineUniqueId +"'";
					TCResult selectTestByThisDevResult = P_Jdbc.dmlSelect(null, selectTestByThisDevSql.toString(), -1);
					if (selectTestByThisDevResult == null) {
						object.put("success", false);
						object.put("message", "数据库操作异常6");
						return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常6");
					}
					if (selectTestByThisDevResult.getStatus() == 1) {
						//此版本为试点版本,并且该终端满足试点条件,将发布规则标志传到外部
						branchRuleId = ruleid_tmp;
						break;
					}
				}
			}
		}
		//步骤3-根据设备所属查询主版本
		String MainRuleSQL = "select RULE.RULEID,RULE.VERSIONID,RULE.VERSIONCODE,RULE.POLICYID from AUMS_VER_RELEASERULE RULE,AUMS_VER_INFO_EXT EXT where EXT.USEBRANCHNO=(select branchno from AUMS_BRANCHINFO where FATHERBRANCHID='adminBranch') and RULE.VERSIONID=EXT.VERSIONID and EXT.VERSIONATTR='1' and RULE.VERSIONTYPE='1' and RULE.VERSIONSTATUS='1' order by RULE.POLICYID desc";
		TCResult MainRuleResult = P_Jdbc.dmlSelect(null, MainRuleSQL, -1);
		if (MainRuleResult == null) {
			object.put("success", false);
			object.put("message", "数据库操作异常7");
			return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常7");
		}
		if (MainRuleResult.getStatus() == 1) {
			//主版本处理
			JavaList MainRuleResultList = (JavaList)MainRuleResult.getOutputParams().get(1);
			for (int i = 0; i < MainRuleResultList.size(); i++) {
				String ruleid_tmp = MainRuleResultList.getListItem(i).get(0).toString();
				AppLogger.info("ruleid_tmp===="+ruleid_tmp);
				//查询此条ruleid是否有对应的试点信息
				String selectTestSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"'";
				TCResult selectTestSqlResult = P_Jdbc.dmlSelect(null, selectTestSql.toString(), -1);
				if (selectTestSqlResult == null) {
					object.put("success", false);
					object.put("message", "数据库操作异常8");
					return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常8");
				}else if (selectTestSqlResult.getStatus()==2) {
					//此版本无试点,为正式主版本,将发布规则标志传到外部
					mainRuleId = ruleid_tmp;
					break;
				}else if (selectTestSqlResult.getStatus()==1) {
					//此版本试点版本,需要判断此试点版本是否适用于此终端
					String selectTestByThisDevSql = "select 1 from AUMS_VER_RELEASERULEBYBRNO where RULEID='"+ ruleid_tmp +"' and BRANCHID=(select branchid from AUMS_BRANCHINFO where branchno='"+ machineOrganNo +"') union select 2 from AUMS_VER_RELEASERULEBYDEV where RULEID='"+ ruleid_tmp +"' and DEVICEID='"+ machineUniqueId +"'";
					TCResult selectTestByThisDevResult = P_Jdbc.dmlSelect(null, selectTestByThisDevSql.toString(), -1);
					if (selectTestByThisDevResult == null) {
						object.put("success", false);
						object.put("message", "数据库操作异常9");
						return new TCResult(0, ErrorCode.REMOTE, "数据库操作异常9");
					}
					if (selectTestByThisDevResult.getStatus() == 1) {
						//此版本为试点版本,并且该终端满足试点条件,将发布规则标志传到外部
						mainRuleId = ruleid_tmp;
						break;
					}
				}
			}
			//循环结束了,还没找到主版本,报错退出
			if (mainRuleId.isEmpty()) {
				object.put("success", false);
				object.put("message", "未找到对应的主版本信息，请核查");
				return new TCResult(0, ErrorCode.REMOTE, "未找到对应的主版本信息，请核查");
			}
		}
		
		ArrayList<String> ruleidlist = new ArrayList<String>();
		if (!mainRuleId.isEmpty()) { //处理主版本
			ruleidlist.add(mainRuleId);
		}else {
			object.put("success", false);
			object.put("message", "未找到对应的主版本信息，请核查");
			return new TCResult(0, ErrorCode.REMOTE, "未找到对应的主版本信息，请核查");
		}
		if (!branchRuleId.isEmpty()) { //存在分行版本，处理分行版本
			ruleidlist.add(branchRuleId);
		}
		if (!branchSpecilRuleId.isEmpty()) { //存在分行特色版本，处理分行特色版本
			ruleidlist.add(branchSpecilRuleId);
		}
		StringBuffer SB = new StringBuffer("");
		for (int i = 0; i < ruleidlist.size(); i++) {
			SB.append("'"+ ruleidlist.get(i) +"',");
		}
		String ruidlistsql = SB.substring(0, SB.length()-1);
		
		//String filelistsql = SB2.substring(0, SB2.length()-1);
		//整体版本处理-查询适用此终端的全量版本信息
		StringBuffer selectSpecialVersion = new StringBuffer(
				"select tpvi.versionid, tpvi.versioncode, tpvid.fileid, tpvid.md5, tpvid.filepath, tpvid.filesize, ext.zipfilename from aums_ver_info tpvi ");
		selectSpecialVersion.append(" join aums_ver_info_ext ext on tpvi.versionid= ext.versionid and (ext.operbranchno=ext.usebranchno or ext.usebranchno='"+ machineBranchNo +"') ");
		selectSpecialVersion.append(" join aums_ver_detailinfo_main tpvid on ext.zipfileid=tpvid.zipfileid and tpvid.clientpath!='version.json' ");
		selectSpecialVersion.append(" join aums_ver_releaserule rule on rule.versionid=tpvi.versionid ");
		selectSpecialVersion.append(" join aums_ver_options_template tpos on tpos.options_template_id=tpvi.strategy_id ");
		selectSpecialVersion.append(" where rule.ruleid in ("+ ruidlistsql +") and (tpvid.filepath like '");
		for (int i = 0; i < filesList.size(); i++) {
			String jsonStr = filesList.get(i).toString();
			JavaDict jd = new JavaDict();
			P_Json.strToDict(jd, jsonStr);
			String newpath = jd.getStringItem("path");
			selectSpecialVersion.append("%"+ newpath +"' or tpvid.filepath like '");
		}
		//最后一次循环去掉末尾的字符
		selectSpecialVersion.delete(selectSpecialVersion.length()-26,selectSpecialVersion.length()-1);
		selectSpecialVersion.append(") order by filepath");
		
		try {
			TCResult selectSpecialVersionResult = P_Jdbc.dmlSelect(null, selectSpecialVersion.toString(), -1);
			if (selectSpecialVersionResult == null) {
				object.put("success", false);
				object.put("message", "版本文件明细信息查询异常");
			} else if(selectSpecialVersionResult.getStatus() == 2){//查询试点版本
				//查询满足此机构的试点版本列表
				AppLogger.info("======xxx=======");
				StringBuffer VersionTestInfo = new StringBuffer("select RULE.VERSIONID, MAIN.FILEID, MAIN.MD5, MAIN.FILEPATH, MAIN.ZIPFILENAME, MAIN.FILESIZE, MAIN.CLIENTPATH from AUMS_VER_RELEASERULE RULE ");
				VersionTestInfo.append("join AUMS_VER_INFO_EXT EXT on EXT.VERSIONID = RULE.VERSIONID and EXT.VERSIONATTR = '2' AND (EXT.OPERBRANCHNO=EXT.USEBRANCHNO OR EXT.USEBRANCHNO='"+ machineBranchNo +"') ");
				VersionTestInfo.append("join AUMS_VER_DETAILINFO_MAIN MAIN on EXT.ZIPFILEID = MAIN.ZIPFILEID and MAIN.CLIENTPATH!='version.json' ");
				VersionTestInfo.append("where RULE.TESTFLAG='2' and RULE.VERSIONSTATUS='1' and (MAIN.CLIENTPATH='");
				for (int i = 0; i < filesList.size(); i++) {
					String jsonStr = filesList.get(i).toString();
					JavaDict jd = new JavaDict();
					P_Json.strToDict(jd, jsonStr);
					String newpath = jd.getStringItem("path");
					VersionTestInfo.append(newpath +"' or MAIN.CLIENTPATH='");
				}
				//最后一次循环去掉末尾的字符
				VersionTestInfo.delete(VersionTestInfo.length()-22,VersionTestInfo.length()-1);				
				VersionTestInfo.append(") and (exists (select 1 from AUMS_VER_RELEASERULEBYBRNO BRNO where BRNO.RULEID =RULE.RULEID and BRNO.BRANCHID in (select BRANCHID from AUMS_BRANCHINFO where BRANCHNO='"+ machineOrganNo +"')) or exists (select 1 from AUMS_VER_RELEASERULEBYDEV DEV where DEV.RULEID =RULE.RULEID and DEV.DEVICEID='"+ machineUniqueId +"')) order by RULE.POLICYID,MAIN.CREATE_TIME");
				AppLogger.info("VersionTestInfo============="+VersionTestInfo);
				TCResult VersionTestInfoResult = P_Jdbc.dmlSelect(null, VersionTestInfo.toString(), -1);
				if (VersionTestInfoResult == null) {
					return new TCResult(0, ErrorCode.REMOTE, "试点明细明细信息查询异常");
				} else if (VersionTestInfoResult.getStatus() == 1) {
					//存在试点版本，处理试点
					JavaList TestList = (JavaList)VersionTestInfoResult.getOutputParams().get(1);
					String zipfilename;
					String vPath;
					for (int i = 0; i < TestList.size(); i++) {						
						String clientpath = TestList.getListItem(i).get(6).toString();
						for (int j = 0; j < fileDetailList.size(); j++) {
							FilesDetails fileInfo1 = fileDetailList.get(j);
							if (clientpath.equals(fileInfo1.getPath())) { //如果当前文件与之前的文件相同，则处理
								fileDetailList.remove(j); //将此条明细移除
								AppLogger.info("j=remove");
								break;
							}
						}
						AppLogger.info("okokokokokokok");
						//内层循环结束还没找到对应的文件，则表示此文件为新增的，则直接将此文件加入返回内容
						FilesDetails fileInfo = new FilesDetails();
						fileInfo.setId(TestList.getListItem(i).get(1).toString());
						fileInfo.setMd5(TestList.getListItem(i).get(2).toString());
						if(TestList.getListItem(i).get(3).toString() == ""){
							fileInfo.setVPath("空");
							fileInfo.setPath("空");
						}else{
							String orgipath = TestList.getListItem(i).get(3).toString();
							zipfilename = TestList.getListItem(i).get(4).toString();
							if (isMajorFile(zipfilename)) {
								vPath = "major" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
							} else {
								vPath = "department" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
							}
							String rPath = orgipath.substring(findStrIndex(orgipath,4,file_separator)+1);
							fileInfo.setVPath(vPath);
							fileInfo.setPath(rPath);
						}
						fileInfo.setVersionid(TestList.getListItem(i).get(0).toString());
						fileInfo.setSize(TestList.getListItem(i).get(5).toString());
						fileDetailList.add(fileInfo);
					}
					object.put("success", true);
					object.put("message", "");
					object.put("files", fileDetailList);
				}else{
					object.put("success", false);
					object.put("message", "处理文件明细信息异常");
				}
					
			} else {
				JavaList list = (JavaList)selectSpecialVersionResult.getOutputParams().get(1);
				if (!(list.isEmpty())) {
					String zipfilename;
					String vPath;
					for (int i = 0; i < list.size(); i++) {
						FilesDetails fileInfo = new FilesDetails();
						ArrayList fileArr = list.getListItem(i);
						fileInfo.setMd5(fileArr.get(3).toString());
						String orgipath = fileArr.get(4).toString();
						//String vPath = orgipath.substring(findStrIndex(orgipath,1,file_separator)+1,findStrIndex(orgipath,4,file_separator)+1);
						zipfilename = fileArr.get(6).toString();
						if (isMajorFile(zipfilename)) {
							vPath = "major" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
							//vPath = "major" + File.separator + zipfilename.substring(0, zipfilename.lastIndexOf('.'));
						} else {
							vPath = "department" + File.separator + orgipath.substring(findStrIndex(orgipath,3,file_separator)+1,findStrIndex(orgipath,4,file_separator));
							//vPath = "department" + File.separator + zipfilename.substring(0, zipfilename.lastIndexOf('.'));
						}
						
						String rPath = orgipath.substring(findStrIndex(orgipath,4,file_separator)+1);
						fileInfo.setPath(rPath);
						fileInfo.setVPath(vPath);
						fileDetailList.add(fileInfo);
					}
					object.put("success", true);
					object.put("message", "");
					object.put("files", fileDetailList);
				}else {
					object.put("success", false);
					object.put("message", "处理文件明细信息异常");
				}
			}
		} catch (Exception e) {
			AppLogger.info("e");
		}
		return TCResult.newSuccessResult(object.toJSONString());
	}
	
	//是否总行文件,根据文件名规则(后续此方法要根据实际情况调整)
	public static boolean isMajorFile(String FileName)
		  {
		return FileName.startsWith("V_") ? true : false;
		  }
	
	//是否全国版本,根据文件名规则(后续此方法要根据实际情况调整)
	public static boolean isNationalFile(String FileName)
	  {
		return A_VersionManageOperate.findStrIndex(FileName, 3, "_")==-1 ? true : false;
	  }
}
