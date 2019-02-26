package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 广告文件删除
 * 
 * @date 2018-07-10 15:13:12
 */
@ComponentGroup(level = "应用", groupName = "AdDelete", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_AdDelete {
	static boolean commitFlg = true;

	/**
	 * @category 广告文件删除
	 * @param adId
	 *            入参|广告ID|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "adId", comment = "广告ID", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "广告文件删除", style = "处理型", type = "同步组件", date = "2018-08-04 02:50:10")
	public static TCResult A_advertisementDelete(String adId) {
		String sql = "select IDLETIMELIMIT,IDLEDEFAULT,TRANSTIMELIMIT,TRANSDEFAULT from AUMS_AD_TEMP";
		if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null && !(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().isEmpty()) ){
			if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1) != null &&
					P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1) != ""){
				List list = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
				P_Logger.info("****************模板id组 list：   " + list);
				Set<String> set = new HashSet<String>();
				for (int k = 0; k < list.size(); k++) {
					List list1 = (List) list.get(k);
					P_Logger.info("******* list1:   " + list1);
					for (int i = 0; i < list1.size(); i++) {
						String s = (String) list1.get(i);
						if (s.length() > 1) {
							String[] s1 = s.split(",");
							for (int j = 0; j < s1.length; j++) {
								set.add(s1[j]);
							}
						} else {
							set.add(s);
						}
					}
				}
				P_Logger.info("***set :   " + set);
				
				String sqlUD = "delete from AUMS_AD_UPLOADINFO where ADID like '"
						+ adId + "'";
				String sqlMD = "delete from AUMS_AD_MANAGE where ADMANAGEID like '"
						+ adId + "'";
				CharSequence cs = ",";
				boolean b = adId.contains(cs);
				if (b == false) {
					if (set.contains(adId) == true) {
						if (bl(adId) == false) {
							P_Jdbc.executeSQL(null, sqlUD, commitFlg);
							P_Jdbc.executeSQL(null, sqlMD, commitFlg);
							P_Logger.info("广告：" + adId + "过期，可删除！");
						} else if (bl(adId) == true) {
							P_Logger.info("广告：" + adId + "没有过期，不可删除！");
						}
					} else {
						P_Jdbc.executeSQL(null, sqlUD, commitFlg);
						P_Jdbc.executeSQL(null, sqlMD, commitFlg);
						P_Logger.info("广告：" + adId + "没添加模板，可删除！");
					}
				} else if (b == true) {
					String sz[] = adId.split(",");
					for (int i = 0; i < sz.length; i++) {
						String szId = sz[i];
						String sqlUD1 = "delete from AUMS_AD_UPLOADINFO where ADID like '"
								+ szId + "'";
						String sqlMD1 = "delete from AUMS_AD_MANAGE where ADMANAGEID like '"
								+ szId + "'";
						if (set.contains(szId) == true) {
							if (bl(szId) == false) {
								P_Jdbc.executeSQL(null, sqlUD1, commitFlg);
								P_Jdbc.executeSQL(null, sqlMD1, commitFlg);
								P_Logger.info("广告：" + szId + "过期，可删除！");
							}else if(bl(szId) == true){
								P_Logger.info("广告：" + szId + "没有过期，不可删除！");
							}
						} else {
							P_Jdbc.executeSQL(null, sqlUD1, commitFlg);
							P_Jdbc.executeSQL(null, sqlMD1, commitFlg);
							P_Logger.info("广告：" + szId + "没添加模板，可删除！");
						}
					}
				}
			}
				
		}
		
		return TCResult.newSuccessResult();
	}
	
	
	public static boolean bl(String id){
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
