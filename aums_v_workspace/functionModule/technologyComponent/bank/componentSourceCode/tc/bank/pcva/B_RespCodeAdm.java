package tc.bank.pcva;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.List;

import tc.bank.constant.IErrorCode;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 返回码管理类
 * 
 * @date 2015-12-07 15:40:7
 */
@ComponentGroup(level = "银行", groupName = "返回码管理类")
public class B_RespCodeAdm {

	/**
	 * @param req
	 *            入参|入参字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param responseCode
	 *            入参|响应码|{@link java.lang.String}
	 * @param sourceSys
	 *            入参|源系统|{@link java.lang.String}
	 * @param targetSys
	 *            入参|目标系统|{@link java.lang.String}
	 * @param targetRspCode
	 *            出参|目标响应码|{@link java.lang.String}
	 * @param responseDesc
	 *            出参|响应描述|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "req", comment = "入参字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "responseCode", comment = "响应码", type = java.lang.String.class),
			@Param(name = "sourceSys", comment = "源系统", type = java.lang.String.class),
			@Param(name = "targetSys", comment = "目标系统", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "targetRspCode", comment = "目标响应码", type = java.lang.String.class),
			@Param(name = "responseDesc", comment = "响应描述", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "返回码映射", style = "判断型", type = "同步组件", comment = "按入参查询响应码信息表，将目标响应码和响应信息作为出参返回", date = "2015-12-09 10:18:52")
	public static TCResult B_RespCodemap(JavaDict req, String responseCode,
			String sourceSys, String targetSys) {
		if(req == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "入参字典不能为空");
		}
		if(responseCode == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "响应码不能为空");
		}
		if(sourceSys == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "源系统不能为空");
		}
		if(targetSys == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "目标系统不能为空");
		}
		/**
		 * 调用jdbc组件
		 * 预编译查询
		 */
		String poolName = null;
		String sqlcmd = "select targetrspcode,responsedesc from tp_cip_rspcodeinfo where responsecode = ? and sourcesys = ? and targetsys in ('*',?) ";
		JavaList values = new JavaList();
		values.add(responseCode);
		values.add(sourceSys);
		values.add(targetSys);
		int rownum = 1;
		String descDetail = null;   //返回的详细信息
		String targetrspcode = null;//目标响应码
		try {
			TCResult preparedSelect = P_Jdbc.preparedSelect(poolName, sqlcmd,
					values, rownum);
			List<?> outputParams = preparedSelect.getOutputParams();
			if (outputParams != null && outputParams.size() > 0) {
				JavaList retList = (JavaList) outputParams.get(1);
				if (retList != null && retList.size() > 0) {
					JavaList colList = (JavaList) retList.get(0);
					targetrspcode = (String) colList.get(0);
					String responsedesc = (String) colList.get(1);
					/**
					 * 
					 * 判断是否含有需要替换的具体信息
					 */
					if (responsedesc.indexOf("|") != -1) {
						//含有，需要替换%s
						String[] descSize = responsedesc.split("\\|");
						if (descSize != null && descSize.length > 1) {
							String[] splitTar = descSize[0].split("%s");
							String[] splitSour = descSize[1].split(",");
							for (int i = 0; i < splitTar.length; i++) {
								if (i == 0) {
									descDetail = splitTar[0];
								} else {
									descDetail = descDetail
											+ req.getItem(splitSour[i - 1])
											+ splitTar[i];
								}
							}
						}
					} else {
						//不含有，直接赋值返回
						descDetail = responsedesc;
					}
				}
			} else {
				return TCResult.newSuccessResult(IErrorCode.getCode("SystemException"),"系统异常");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(targetrspcode, descDetail);
	}
}
