package tc.View.Agent;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import tc.View.Agent.utils.B_RuntimeUtil;
import tc.View.Agent.utils.MathUtil;
import tc.bank.constant.IErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;

/**
 * 系统参数类组件
 * 
 * @date 2015-12-08 10:53:43
 */
@ComponentGroup(level = "应用", groupName = "系统命令调用组件", projectName = "AAAA", appName = "agent")
public class B_WinSysParam {

	/***
	 * 获取win系统信息
	 * 
	 * @return
	 */
	@InParams(param = {})
	@OutParams(param = { @Param(name = "javadict", comment = "数据字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取操作系统信息", style = "判断型", type = "同步组件", comment = "获取操作系统信息", author = "Anonymous", date = "2015-12-08 10:59:59")
	public static TCResult B_GetWindSysParam() {

		JavaDict javadict = new JavaDict();
		try {

			javadict = B_RuntimeUtil.B_getSystemInfo();

		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(javadict);
	}

	/***
	 * 获取CPU信息
	 * 
	 * @return
	 */
	@InParams(param = {})
	@OutParams(param = { @Param(name = "javadict", comment = "数据字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取CPU信息", style = "判断型", type = "同步组件", comment = "获取CPU信息", author = "Anonymous", date = "2015-12-08 10:59:59")
	public static TCResult B_GetWindCPUParam() {

		JavaDict javadict = new JavaDict();
		try {

			javadict = B_RuntimeUtil.B_getCPUInfo();

		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(javadict);
	}

	/***
	 * 获取指定盘信息
	 * 
	 * @return
	 */
	@InParams(param = { @Param(name = "diskname", comment = "盘符", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "javadict", comment = "数据字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取磁盘信息", style = "判断型", type = "同步组件", comment = "获取磁盘信息", author = "Anonymous", date = "2015-12-08 10:59:59")
	public static TCResult B_GetWindDiskParam(String diskname) {

		JavaDict javadict = new JavaDict();
		try {

			javadict = B_RuntimeUtil.B_getDiskInfo(diskname);

			if (javadict != null && javadict.size() > 0) {

				//物理硬盘实际大小
				javadict.put(
						"Disk_Size",
						MathUtil.divideBigDecimal(
								javadict.getStringItem("Size"),
								String.valueOf(1024 * 1024 * 1024L)));
				//物理空闲硬盘大小
				javadict.put(
						"Disk_FreeSpace",
						MathUtil.divideBigDecimal(
								javadict.getStringItem("FreeSpace"),
								String.valueOf(1024 * 1024 * 1024L)));
			}

		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(javadict);
	}

}
