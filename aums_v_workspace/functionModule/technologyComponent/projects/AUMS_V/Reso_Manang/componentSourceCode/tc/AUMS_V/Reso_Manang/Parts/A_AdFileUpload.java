package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 广告上传
 * 
 * @date 2018-06-22 16:32:30
 */
@ComponentGroup(level = "应用", groupName = "AdFileUpload", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_AdFileUpload {
	static boolean commitFlg = true;

	@InParams(param = {
			@galaxy.ide.tech.cpt.Param(name = "adFilePath", comment = "广告图片文件路径", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "startDate", comment = "开始日期", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "endDate", comment = "结束日期", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "startTime", comment = "开始时间", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "endTime", comment = "结束时间", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "isEnable", comment = "是否启用", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "userId", comment = "用户", type = String.class),
			@galaxy.ide.tech.cpt.Param(name = "fileDesc", comment = "文件描述", type = String.class) })
	@Returns(returns = { @galaxy.ide.tech.cpt.Return(id = "0", desp = "失败"),
			@galaxy.ide.tech.cpt.Return(id = "1", desp = "成功") })
	@Component(label = "广告图片上传", style = "判断型", type = "同步组件", date = "2018-07-04 02:50:03")
	public static TCResult A_fileUpload(String adFilePath, String startDate,
			String endDate, String startTime, String endTime, String isEnable,
			String userId, String fileDesc) {
		if ((startDate != null) && (!"".equals(startDate.trim()))) {
			startDate = startDate.replace("-", "");
		}
		if ((endDate != null) && (!"".equals(endDate.trim()))) {
			endDate = endDate.replace("-", "");
		}
		if ((startTime != null) && (!"".equals(startTime.trim()))) {
			startTime = startTime.replace(":", "");
		}
		if ((endTime != null) && (!"".equals(endTime.trim()))) {
			endTime = endTime.replace(":", "");
		}
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = df.format(date);
		String createUser = userId;
		P_Logger.info("****************userId: " + userId);
		P_Logger.info("****************isEnable: " + isEnable);
		String adId = null;
		String admanageId = null;
		List<?> adIdList = null;
		List<?> adManageIdList = null;
		String adIdSql = "select nvl((max(to_number(ADID))),0)+1 from AUMS_AD_UPLOADINFO";
		String adManangeIdSql = "select nvl((max(to_number(ADMANAGEID))),0)+1 from AUMS_AD_MANAGE";  
		adIdList = (List<?>) P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams().get(1);
		adManageIdList = (List<?>) P_Jdbc.dmlSelect(null, adManangeIdSql, -1).getOutputParams().get(1);
		P_Logger.info("************获取Id日志****************");
		if (adIdList != null) {
			List list1 = (List) adIdList.get(0);
			adId = list1.get(0).toString();
			P_Logger.info("adId:  " + adId);
		}
		if (adManageIdList != null) {
			List list1 = (List) adManageIdList.get(0);
			admanageId = list1.get(0).toString();
			P_Logger.info("admanageId:  " + admanageId);
		}
		String[] a = adFilePath.split("/");
		String fileName = a[(a.length - 1)];
		
		P_Logger.info("********路径：  " + a[(a.length - 2)]);//Ad
		StringBuffer su = new StringBuffer();
		su.append(a[(a.length - 2)]).append("/").append(fileName);
		String path = su.toString();
		
		String adType = "";
		if (fileName.endsWith(".jpeg")) {
			adType = "image/jpeg";
		} else if (fileName.endsWith(".png")) {
			adType = "image/png";
		} else if (fileName.endsWith(".bmp")) {
			adType = "image/bmp";
		} else if(fileName.endsWith(".jpg")){
			adType = "image/jpg";
		} else {
			adType = "image/else";
		}
		String insertUploadSql = "insert into AUMS_AD_UPLOADINFO (ADID, FILENAME, FILEPATH, FILEDESC, UPLOADUSER, UPLOADTIME) values ('"
				+ adId
				+ "','"
				+ fileName
				+ "','"
				+ path
				+ "','"
				+ fileDesc + "','" + createUser + "','" + createTime + "')";
		String insertManageSql = "insert into AUMS_AD_MANAGE (STARTDATE, ENDDATE, STARTTIME, ENDTIME, WORKSTATUS, ADMANAGEID, ADTYPE, ADPATH, DEVID, CREATETIME, ISENABLED) values ('"
				+
				startDate
				+ "','"
				+ endDate
				+ "','"
				+ startTime
				+ "','"
				+ endTime
				+ "',0,'"
				+ admanageId
				+ "','"
				+ adType
				+ "','"
				+ path
				+ "',' ','"
				+ createTime
				+ "','"
				+ isEnable
				+ "')";
		P_Jdbc.executeSQL(null, insertUploadSql, commitFlg);
		P_Jdbc.executeSQL(null, insertManageSql, commitFlg);
		JSONObject json = new JSONObject();
		json.put("alertFlag", "上传广告成功！");
		return TCResult.newSuccessResult(new Object[] { json });
	}

	/**
	 * @category 广告修改查询机具信息
	 * @param adManageId
	 *            入参|广告ID|{@link java.lang.String}
	 * @param tableData
	 *            出参|受影响的机具信息|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "adManageId", comment = "广告ID", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "tableData", comment = "受影响的机具信息", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "广告修改查询机具信息", style = "处理型", type = "同步组件", date = "2018-08-03 06:37:40")
	public static TCResult A_adUpdSel(String adManageId) {
		JSONArray tableData = new JSONArray();
		String adIdSql = "select ADTEMPID,IDLETIMELIMIT,IDLEDEFAULT,TRANSTIMELIMIT,TRANSDEFAULT from AUMS_AD_TEMP";
		P_Logger.info("*P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams(): " + P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams());
		if(P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams() != null){
			if(P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams().get(1) != null){
				List adIdlist = (List) P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams().get(1);
				CharSequence cs = ",";
				StringBuffer su = new StringBuffer();
				for (int i = 0; i < adIdlist.size(); i++) {
					List malist = (List) adIdlist.get(i);
					P_Logger.info("**********malist: " + malist);
					String adTempId = malist.get(0).toString();
					Set<String> set = new HashSet<String>();
					for (int j = 1; j < malist.size(); j++) {
						P_Logger.info("******id:   " + malist.get(j));
						if(malist.get(j) != null){
							String id = malist.get(j).toString();
							boolean b = id.contains(cs);
							if (b == true) {
								String str[] = id.split(",");
								for (String st : str) {
									set.add(st);
								}
							} else if (b == false) {
								set.add(id);
							}
						}
					}
					if (set.contains(adManageId)) {
						su.append(adTempId).append(",");
					}
				}
				String s2 = su.toString();
				P_Logger.info("***************s2: " + s2);
				if(s2.length() != 0){
					s2 = s2.substring(0, s2.length() - 1);
					P_Logger.info("*********s2:   " + s2);
					
					StringBuffer su1 = new StringBuffer();
					if(s2.length() != 0){
						String[] split = ((String)(s2)).split(",");
						for(String str:split){
							su1.append(str);
							su1.append("','");
						}
					}
					String temp = "'"+su1.substring(0,su1.length()-2);
					P_Logger.info("********temp: " + temp);
					
					String sql = "select DEVID,DEVNUM,BRANCHNO,BRANCHNAME from (select di.DEVID,di.DEVNUM,di.DEVBRANCHID from (select DISTINCT DEVID from AUMS_AD_TEMP_TO_DEV "
							+ "where TEMPID in ("+ temp + ")) t LEFT JOIN AUMS_DEV_INFO di "
							+ "on t.DEVID = di.DEVID) t1 LEFT JOIN AUMS_BRANCHINFO b on t1.DEVBRANCHID = b.BRANCHID";
					try{
						if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
							List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
							P_Logger.info("----------------------list: " + list);
							if(list.size() != 0){
								for (int i = 0; i < list.size(); i++) {
									List list1 = (List) list.get(i);
									JSONObject jo = new JSONObject();
									jo.put("devId", (String) list1.get(0));
									jo.put("devNum", (String) list1.get(1));
									jo.put("branchNum", (String) list1.get(2));
									jo.put("branchName", (String) list1.get(3));
									tableData.add(jo);
								}
							}
						}
					}catch(NullPointerException e){
						e.printStackTrace();
					}
				}
			}
		}
		return TCResult.newSuccessResult(tableData);
	}

	/**
	 * @category 广告删除查询
	 * @param adManageId
	 *            入参|广告IDs|{@link java.lang.String}
	 * @param tableData
	 *            出参|查询结果|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "adManageId", comment = "广告IDs", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "tableData", comment = "查询结果", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "广告删除查询", style = "处理型", type = "同步组件", author = "BJ", date = "2018-08-03 06:39:12")
	public static TCResult A_adDelSel(String adManageId) {
		JSONArray tableData = new JSONArray();
		String adIdSql = "select ADTEMPID,IDLETIMELIMIT,IDLEDEFAULT,TRANSTIMELIMIT,TRANSDEFAULT from AUMS_AD_TEMP";
		if(P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams() != null){
			if(P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams().get(1) != null){
				List adIdlist = (List) P_Jdbc.dmlSelect(null, adIdSql, -1).getOutputParams().get(1);
				CharSequence cs = ",";
				Set<String> set = new HashSet<String>();
				boolean bb = adManageId.contains(",");
				Set<String> setKS = new HashSet<String>();
				Set<String> setKSG = new HashSet<String>();
				Set<String> setBKS = new HashSet<String>();
				for (int i = 0; i < adIdlist.size(); i++) {
					List malist = (List) adIdlist.get(i);
					P_Logger.info("**********malist: " + malist);
					String tempId = (String) malist.get(0);
					for (int j = 1; j < malist.size(); j++) {
						if(malist.get(j) != null){
							String id = malist.get(j).toString();
							boolean b = id.contains(cs);
							if (b == true) {
								String str[] = id.split(",");
								for (String st : str) {
									set.add(st);
								}
							} else if (b == false) {
								set.add(id);
							}
						}
					}
				}
				P_Logger.info("***********set: " + set);
				if(bb == true){//传的广告ID为复选
					String str1[] = adManageId.split(",");
					for(int k=0;k<str1.length;k++){
						String s1 = str1[k];
						if(set.contains(s1)){
							if(overdue(s1) == false){//过期，可删
								setKSG.add(s1);
							}else if(overdue(s1) == true){
								setBKS.add(s1);
							}
						}else if(! set.contains(s1)){
							setKS.add(s1);
						}
					}
				}else if(bb == false){//传的广告ID为单选
					if(set.contains(adManageId)){
						if(overdue(adManageId) == false){//过期，可删
							setKSG.add(adManageId);
						}else if(overdue(adManageId) == true){
							setBKS.add(adManageId);
						}
					}else if(! set.contains(adManageId)){
						setKS.add(adManageId);
					}
				}
				
				P_Logger.info("*********setKS:   " + setKS);//可删除（没有添加模板）adId
				P_Logger.info("*********setKSG:   " + setKSG);//可删除（添加模板但过期）adId
				P_Logger.info("*********setBKS:   " + setBKS);//不可删除（没添加模板）adId (Set集合)
				
				if(setBKS.size() != 0){
					StringBuffer su = new StringBuffer();
					Iterator it = setBKS.iterator();
					while(it.hasNext()){
						su.append(it.next()).append(",");
					}
					String s2 = su.toString();
					s2 = s2.substring(0, s2.length() - 1);
					
					P_Logger.info("***********s2: " + s2);
					StringBuffer su1 = new StringBuffer();
					if(s2.length() != 0){
						String[] split = ((String)(s2)).split(",");
						for(String str:split){
							su1.append(str);
							su1.append("','");
						}
					}
					String temp = "'"+su1.substring(0,su1.length()-2);
					P_Logger.info("***********temp: " + temp);
					
					String sql  = "select * from AUMS_AD_UPLOADINFO where ADID in ("+temp+")";
					if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
						List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
						for (int i = 0; i < list.size(); i++) {
							List list1 = (List) list.get(i);
							JSONObject jo = new JSONObject();
							jo.put("adId", (String) list1.get(0));
							jo.put("fileName", (String) list1.get(1));
							jo.put("filePath", (String) list1.get(2));
							jo.put("fileDesc", (String) list1.get(3));
							jo.put("remark1", (String) list1.get(4));
							jo.put("remark2", (String) list1.get(5));
							jo.put("remark3", (String) list1.get(6));
							jo.put("uploadUser", (String) list1.get(7));
							jo.put("uploadTime", (String) list1.get(8));
							tableData.add(jo);
						}
					}
				}
			}
		}
		return TCResult.newSuccessResult(tableData);
	}

	//比较过期与否方法
	public static boolean overdue(String id){
		String dtSql = "select ENDDATE,ENDTIME from AUMS_AD_MANAGE where ADMANAGEID like '" + id + "'";
		List list3 = (List) P_Jdbc.dmlSelect(null, dtSql, -1).getOutputParams().get(1);
		String date = null;
		String time = null;
		//for (int i = 0; i < list3.size(); i++) {
			List list4 = (List) list3.get(0);
			date = (String) list4.get(0);
			time = (String) list4.get(1);
		//}
		StringBuffer sb1 = new StringBuffer();
		sb1.append(date).append(" ").append(time);
		String strC = sb1.toString();
		Date dateXT = new Date();
		Date cDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss");
		try {
			cDate = sdf.parse(strC);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean flag = false;
		if (dateXT.getTime() - cDate.getTime() > 0) {
			flag = false;         //过期，可直接删除
		} else {
			flag = true;          //没过期，得判断是否添加模板，添加了不能删除；没添加可删除
		}
		
		return flag;
	}
}
