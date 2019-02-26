package tc.AUMS_V.Reso_Manang.Parts;

import tc.platform.P_Jdbc;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * menuRelease
 * 
 * @date 2018-07-11 11:10:17
 */
@ComponentGroup(level = "应用", groupName = "menuRelease", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_menuRelease {
	
	//static String poolName = "qdzh_oracle";
	static boolean commitFlg = true;

	/**
	 * @category 菜单发布
	 * @param menuModelId
	 *            入参|菜单模板ID|{@link java.lang.String}
	 * @param publicType
	 *            入参|发布方式|{@link java.lang.String}
	 * @param devIds
	 *            入参|设备IDs|{@link java.lang.String}
	 * @param bankArea
	 *            入参|发布地区|{@link java.lang.String}
	 * @param brno
	 *            入参|发布机构|{@link java.lang.String}
	 * @param userId
	 *            入参|用户|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "menuModelId", comment = "菜单模板ID", type = java.lang.String.class),
			@Param(name = "publicType", comment = "发布方式", type = java.lang.String.class),
			@Param(name = "devIds", comment = "设备IDs", type = java.lang.String.class),
			@Param(name = "bankArea", comment = "发布地区", type = java.lang.String.class),
			@Param(name = "brno", comment = "发布机构", type = java.lang.String.class),
			@Param(name = "userId", comment = "用户", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "菜单发布", style = "处理型", type = "同步组件", date = "2018-07-11 02:29:55")
	public static TCResult A_menuToRelease(String menuModelId,
			String publicType, String devIds, String bankArea, String brno,
			String userId) {
		String devs = null;
		if ("1".endsWith(publicType.trim())) {
			devs = devIds;
		}
		if ("2".endsWith(publicType.trim())) {
		}
		if ("3".endsWith(publicType.trim())) {
		}
		
		
		//发布之前先删除该摸坂绑定的设备
		String sqlD = "";
		
		
		//发布菜单到设备
		String sql = "insert into AUMS_MENU_DEVMODELMAPPING (DEVTYPE, MODELID, CREATE_USER, CREATE_TIME, BRNO, DEVID, DEVIP, MODELTYPE) "
				+ "select DEVTYPE,"
				+ menuModelId
				+ ","
				+ userId
				+ ",(select to_char(sysdate,'yyyymmddhh24miss') from dual),BRANCHNO,DEVID,DEVIP,'0' from "
				+ "(select DEVID,DEVASSORTMENTID,DEVBRANCHID,BRANCHNO,DEVIP,t1.DEVMODELID,DEVTYPENUM from (select DEVID,DEVASSORTMENTID,DEVBRANCHID,BRANCHNO,DEVIP,DEVMODELID from "
				+ "(select DEVID,DEVASSORTMENTID,DEVBRANCHID,BRANCHNO,DEVIP from AUMS_DEV_INFO di left join AUMS_BRANCHINFO b on di.DEVBRANCHID = b.BRANCHID) t left join "
				+ "AUMS_DEV_ASSORTMENT da on t.DEVASSORTMENTID = da.ASSORTMENTID) t1 left join AUMS_DEV_MODEL dm on t1.DEVMODELID = dm.DEVMODELID) t2 left join AUMS_DEV_TYPE dt "
				+ "on t2.DEVTYPENUM = dt.DEVTYPENUM where DEVID in (select regexp_substr("+devs+",'[^,]+', 1, level) from dual connect by regexp_substr("+devs+", '[^,]+', 1, level) is not null)";
		
		P_Jdbc.executeSQL(null, sql, commitFlg);
		return TCResult.newSuccessResult();
	}

}
