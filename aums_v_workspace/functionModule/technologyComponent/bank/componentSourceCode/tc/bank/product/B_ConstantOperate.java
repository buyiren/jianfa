package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import tc.bank.constant.IErrorCode;
import tc.bank.constant.SysConstant;
import tc.bank.utils.StringUtil;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 常量操作类
 * 
 * @date 2015-12-09 11:39:43
 */
@ComponentGroup(level = "银行", groupName = "常量操作类")
public class B_ConstantOperate {

	/**
	 * @param constant
	 *            入参|常量名称|{@link java.lang.String}
	 * @param ReturnCode
	 *            出参|常量值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "constant", comment = "常量名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "ReturnCode", comment = "常量值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取响应码", style = "判断型", type = "同步组件", comment = "按入参从错误码常量类里获取常量值", date = "2015-12-09 11:50:04")
	public static TCResult B_GetRespCode(String constant) {
		if (null != constant && !constant.equals("")) {
			// AppLogger.info("进来了今天:"+constant);
			String ret = IErrorCode.getCode(constant);
			if (null != ret) {
				return TCResult.newSuccessResult(ret);
			} else {
				return TCResult.newFailureResult(
						IErrorCode.getCode("SystemException"), "此常量名称在常量中不存在");
			}

		} else {
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), "常量名称不能为空");
		}

	}

	/**
	 * @param constant
	 *            入参|常量名称|{@link java.lang.String}
	 * @param ReturnCode
	 *            出参|常量值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "constant", comment = "常量名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "ReturnCode", comment = "常量值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取系统常量", style = "判断型", type = "同步组件", comment = " 按入参从系统参数常量类里获取常量值", date = "2015-12-09 11:50:04")
	public static TCResult B_GetSysConstant(String constant) {
		if (null != constant && !constant.equals("")) {
			String ret = SysConstant.getSysConstant(constant);
			if (null != ret) {
				return TCResult.newSuccessResult(ret);
			} else {
				return TCResult.newFailureResult(
						IErrorCode.getCode("SystemException"), "此常量名称在常量中不存在");
			}
		} else {
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), "常量名称不能为空");
		}

	}

	/**
	 * @param rspCodeStr
	 *            入参|响应码名称|{@link java.lang.String}
	 * @param rspCodeVal
	 *            入参|响应码值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "rspCodeStr", comment = "响应码名称", type = java.lang.String.class),
			@Param(name = "rspCodeVal", comment = "响应码值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "响应码比较", style = "判断型", type = "同步组件", comment = "入参常量名称和常量值  在常量类中找是否一致  一致返回1 不一致返回0", date = "2016-01-04 11:37:57")
	public static TCResult B_RespCodeCompare(String rspCodeStr,
			String rspCodeVal) {
		AppLogger.info("rspCodeStr: " + rspCodeStr);
		AppLogger.info("rspCodeVal: " + rspCodeVal);
		String rspcodestr = IErrorCode.getCode(rspCodeStr);
		AppLogger.info("rspcodestr: " + rspcodestr);
		if (StringUtil.isEmpty(rspCodeStr) || StringUtil.isEmpty(rspCodeVal)) {
			AppLogger.info("参数一致返回0");
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), "0");
		}
		if (rspcodestr.equals(rspCodeVal)) {
			AppLogger.info("参数一致返回1");
			return TCResult.newSuccessResult();
		} else {
			AppLogger.info("参数一致返回0");
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), "0");
		}
	}

	/**
	 * @param constantStr
	 *            入参|常量名称|{@link java.lang.String}
	 * @param constantVal
	 *            入参|常量值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "constantStr", comment = "常量名称", type = java.lang.String.class),
			@Param(name = "constantVal", comment = "常量值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "系统常量比较", style = "判断型", type = "同步组件", comment = "入参常量名称和常量值  在常量类中找是否一致  一致返回1 不一致返回0", date = "2016-01-04 11:33:30")
	public static TCResult B_SysConstantCompare(String constantStr,
			String constantVal) {
		AppLogger.info("constantStr: " + constantStr);
		AppLogger.info("constantVal: " + constantVal);
		String constantstr = SysConstant.getSysConstant(constantStr);
		AppLogger.info("constantstr: " + constantstr);
		if (StringUtil.isEmpty(constantStr) || StringUtil.isEmpty(constantVal)) {
			AppLogger.info("参数一致返回0");
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), "0");
		}
		if (constantstr.equals(constantVal)) {
			AppLogger.info("参数一致返回1");
			return TCResult.newSuccessResult();
		} else {
			AppLogger.info("参数一致返回0");
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), "0");
		}
	}

	/**
	 * @category 获取配置文件信息
	 * @param properfile
	 *            入参|配置文件名|{@link java.lang.String}
	 * @param systemId
	 *            入参|系统编号|{@link java.lang.String}
	 * @param keyArg
	 *            入参|需要读取的KEY数组|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param resultdict
	 *            出参|结果集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "properfile", comment = "配置文件名", type = java.lang.String.class),
			@Param(name = "systemId", comment = "系统编号", type = java.lang.String.class),
			@Param(name = "keyArg", comment = "需要读取的KEY数组", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "resultdict", comment = "结果集", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取配置文件信息", style = "判断型", type = "同步组件", date = "2017-08-11 09:55:20")
	public static TCResult B_GetProperties(String properfile, String systemId,
			JavaList keyArg) {
		JavaDict resultdict = new JavaDict();
		FileInputStream inputStream = null;
		try {
			
			
			String path=System.getProperty("user.home")  + File.separator+"workspace/cfg" + File.separator+ properfile;
			File file = new File(path);
			if (!file.exists()) {
				return TCResult.newFailureResult(
						IErrorCode.getCode("PropertiesNotExist"),"文件不存在："+path);
			}

			inputStream = new FileInputStream(file);

			Properties prop = new Properties();

			prop.load(inputStream);

			for (int i = 0; i < keyArg.size(); i++) {
				String key = (String)keyArg.get(i);
				String value = prop.getProperty(systemId + "." + key, "");
				resultdict.put(key, value);
			}
			
			
			
		} catch (IOException e) {
			AppLogger.info(e.getMessage());
			AppLogger.info(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("PropertiesNotExist"), e);
		}finally{
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					return TCResult.newFailureResult(
							IErrorCode.getCode("PropertiesCloseException"),e);
				}
			}
			
			
		}
		return TCResult.newSuccessResult(resultdict);
	}

}
