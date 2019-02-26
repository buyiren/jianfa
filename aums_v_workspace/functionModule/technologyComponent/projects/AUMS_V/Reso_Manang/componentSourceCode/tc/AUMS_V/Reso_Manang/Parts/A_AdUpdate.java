package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * @date 2018-08-17 10:33:52
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_AdUpdate {

	/**
	 * @category 广告发布
	 * @param devids
	 *            入参|设备编号|{@link java.lang.String}
	 * @param adtempId
	 *            入参|广告模板id|{@link java.lang.String}
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
			@Param(name = "userId", comment = "用户ID", type = java.lang.String.class),
			@Param(name = "workdate", comment = "操作日期", type = java.lang.String.class),
			@Param(name = "worktime", comment = "操作时间", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "广告更新", style = "判断型", type = "同步组件", comment = "广告更新", date = "2018-08-17 02:59:45")
	public static TCResult A_Update(String devids, String adtempId,
			String userId, String workdate, String worktime) {
		try {
			String update ="Update  AUMS_AD_TEMP_TO_DEV  SET (TEMPIDNAME,IDLETIMELIMIT,IDLEDEFAULT,TRANSTIMELIMIT,TRANSDEFAULT,CREATEUSER, CREATEDATE,CREATETIME) "
					+ " =(select TEMPNAME,IDLETIMELIMIT,IDLEDEFAULT,TRANSTIMELIMIT,TRANSDEFAULT,'"+userId+"','"+workdate+"','"+worktime+"' from AUMS_AD_TEMP where adtempId='"+adtempId+"' )"
					+ "where  devid in("+devids+")";
			
			P_Jdbc.ddlDelete(null, update);
			P_Logger.info("执行的sql #########       " + update);
		} catch (Exception e) {
			P_Jdbc.rollBack(null);
		}
		return TCResult.newSuccessResult();
	}

}
