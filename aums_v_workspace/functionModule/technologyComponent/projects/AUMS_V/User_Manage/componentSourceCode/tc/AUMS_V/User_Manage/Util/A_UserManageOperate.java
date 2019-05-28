package tc.AUMS_V.User_Manage.Util;

import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

/**
 * 用户管理统一操作类
 * 
 * @author AlphaLi
 * @date 2018-12-21 15:51:13
 */
@ComponentGroup(level = "应用", groupName = "用户管理操作")

public class A_UserManageOperate {
	
	
	/**
	 * @category 初始化IDE权限列表
	 * @param __SRC__
	 *            入参|全局容器| {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param transCode
	 *            入参|交易名称| {@link java.lang.String}
	 */
	@InParams(param = {
			@Param(name = "__SRC__", comment = "全局容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "transCode", comment = "交易名称", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "IDE权限初始化成功"),
			@Return(id = "1", desp = "IDE权限初始化失败") })
	@Component(label = "初始化IDE权限列表", comment = "初始化IDE权限列表", date = "2018-12-21 03:09:07")
	public static TCResult A_PublicInitIdeAuth(JavaDict __SRC__,String transCode) {
		
		String deletePermission = "delete from AUMS_PERMISSION where aumschannel='004'";
		String deleteViewData = "delete from AUMS_V_VIEWDATA where VIEWTYPE='3'";
		try {
			//删除旧的列表数据
			P_Jdbc.executeSQL(null, deletePermission.toString(), true);
			P_Jdbc.executeSQL(null, deleteViewData.toString(), true);
		} catch (Exception e) {
			AppLogger.error(e);
			P_Jdbc.rollBack(null);
			return new TCResult(0, ErrorCode.REMOTE, "删除数据失败");
		}
		
		try {
			JavaDict jd = __SRC__.getDictItem("_MsgDict_").getDictItem("REQ_BODY");
			String FatherId = "/";
			String HasChild = "null";
			String ServiceCode = "null";
			String AumsChannel = "004";
			for (Object obj:jd.keySet()) {
				String PermissionId = obj.toString();
				String PermissionName = jd.getStringItem(PermissionId).toString();
				String ViewKey = PermissionId;
				StringBuffer PermissionInsert = new StringBuffer(
						"insert into AUMS_PERMISSION(PERMISSIONID,FATHERID,HASCHILD,SERVICECODE,PERMISSIONNAME,AUMSCHANNEL,VIEWKEY) VALUES(");
				PermissionInsert.append("'" + PermissionId + "'" + ",");
				PermissionInsert.append("'" + FatherId + "'" + ",");
				PermissionInsert.append(HasChild+ ",");
				PermissionInsert.append(ServiceCode + ",");
				PermissionInsert.append("'" + PermissionName + "',");
				PermissionInsert.append("'" + AumsChannel + "',");
				PermissionInsert.append("'"  + ViewKey + "')");
				P_Jdbc.executeSQL(null, PermissionInsert.toString(), true);
				
				StringBuffer ViewKeyInsert = new StringBuffer(
						"insert into AUMS_V_VIEWDATA(VIEWKEY,VIEWTYPE,VIEWREMARK) VALUES(");
				ViewKeyInsert.append("'" + ViewKey + "'" + ",");
				ViewKeyInsert.append("'3'" + ",");
				ViewKeyInsert.append("'" + PermissionName + "')");
				P_Jdbc.executeSQL(null, ViewKeyInsert.toString(), true);
				
			}
			AppLogger.info("初始化IDE权限列表成功");
			return TCResult.newSuccessResult();
		}  catch (Exception e) {
				AppLogger.error(e);
				P_Jdbc.rollBack(null);
				return new TCResult(0, ErrorCode.REMOTE, "初始化IDE权限列表失败");
			}
	}
}
