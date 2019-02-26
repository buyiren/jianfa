package tc.AUMS_V.Reso_Manang.Parts;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * @date 2018-08-17 10:33:52
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_AdRelease {

	/**
	 * @category 广告发布
	 * @param devids
	 *            入参|设备编号|{@link java.lang.String}
	 * @param adtempId
	 *            入参|广告模板id|{@link java.lang.String}
	 * @param publicType
	 *            入参|发布类型|{@link java.lang.String}
	 * @param userId
	 *            入参|用户ID|{@link java.lang.String}
	 * @param workdate
	 *            入参|操作日期|{@link java.lang.String}
	 * @param worktime
	 *            入参|操作时间|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "devids", comment = "设备编号", type = java.lang.String.class),
			@Param(name = "adtempId", comment = "广告模板id", type = java.lang.String.class),
			@Param(name = "publicType", comment = "发布类型", type = java.lang.String.class),
			@Param(name = "userId", comment = "用户ID", type = java.lang.String.class),
			@Param(name = "workdate", comment = "操作日期", type = java.lang.String.class),
			@Param(name = "worktime", comment = "操作时间", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "广告发布", style = "判断型", type = "同步组件", comment = "广告发布", date = "2018-08-22 07:44:31")
	public static TCResult A_release(String devids, String adtempId,String publicType, String userId, String workdate, String worktime) {
		try {
			String delete = "delete from AUMS_AD_TEMP_TO_DEV  where devid in("
					+ devids + ")";
			P_Logger.info("执行的sql         " + delete);
			P_Jdbc.executeSQL(null, delete,false);
			P_Jdbc.commit(null);
			
			String insert = "INSERT INTO AUMS_AD_TEMP_TO_DEV "
					+ "(DEVID,TEMPID,TEMPIDNAME,PUBLICTYPE,IDLETIMELIMIT,IDLEDEFAULT,TRANSTIMELIMIT,TRANSDEFAULT,CREATEUSER,CREATEDATE,CREATETIME)"
					+ "SELECT  devid,'"+adtempId+"',(select TEMPNAME from AUMS_AD_TEMP where ADTEMPID='"+adtempId+"'),'"+publicType+"',"
					+ "(select IDLETIMELIMIT from AUMS_AD_TEMP where ADTEMPID='"+adtempId+"'),"
					+ "(select IDLEDEFAULT from AUMS_AD_TEMP where ADTEMPID='"+adtempId+"'),"
					+ "(select TRANSTIMELIMIT from AUMS_AD_TEMP where ADTEMPID='"+adtempId+"'),"
					+ "(select TRANSDEFAULT from AUMS_AD_TEMP where ADTEMPID='"+adtempId+"'),'"+userId+"','"+workdate+"','"+worktime+"' from AUMS_DEV_INFO where devid in ("+devids+")";
			P_Logger.info("執行的sql是XXXXXXXXXXXXXXXXXXXXXX"+insert);
			P_Jdbc.executeSQL(null, insert, false);
			P_Jdbc.commit(null);
	
			String[] split = devids.split(",");
			P_Logger.info("11111"+split);
            for (String devid : split) {
					String sqlstr2= "update AUMS_AD_TEMP_TO_DEV set "
							+ " DEVBRNO = (select BRANCHNO from AUMS_BRANCHINFO where BRANCHID = (select DEVBRANCHID from AUMS_DEV_INFO where DEVID="+devid+" )),"
							+ " DEVBRNONAME=(select BRANCHNAME from AUMS_BRANCHINFO where BRANCHID = (select DEVBRANCHID from AUMS_DEV_INFO where DEVID="+devid+" ))"
							+ " where  DEVID="+devid+"";
					P_Logger.info("执行的sql XXXXXXXXXXXXX        " + sqlstr2);
					P_Jdbc.executeSQL(null, sqlstr2, true);
					
				
			}
		} catch (Exception e) {
			P_Jdbc.rollBack(null);
		}
		return TCResult.newSuccessResult();
	}

}
