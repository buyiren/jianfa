package tc.View.Agent;

import tc.bank.constant.IErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

/**
 * 系统命令
 * 
 * @date 2018-02-03 12:7:9
 */
@ComponentGroup(level = "应用", groupName = "命令运行类", projectName = "View", appName = "Agent")
public class A_RuntimeUtil {
	
	/***
	 * 执行windows命令
	 * 
	 * @return
	 */
	@InParams(param = {@Param(name = "common", comment = "命令", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "执行windows命令", style = "判断型", type = "同步组件", comment = "执行windows命令", author = "", date = "")
	public static TCResult B_getRunCommon(String common) {
		try {
			 Runtime.getRuntime().exec(common);
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult();
	}

}
