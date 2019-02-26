package tc.bank.product;

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
import tc.platform.P_String;
import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.jcomponent.CacheOperationImpl.CacheOperationException;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 系统参数类组件
 * 
 * @date 2015-12-08 10:53:43
 */
@ComponentGroup(level = "银行", groupName = "系统参数类组件")
public class B_SysParam {

	/**
	 * @param moduleCode
	 *            入参|模块代码|{@link java.lang.String}
	 * @param transCode
	 *            入参|交易代码|{@link java.lang.String}
	 * @param paramKeyName
	 *            入参|参数名称|{@link java.lang.String}
	 * @param paramValue
	 *            出参|参数值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "moduleCode", comment = "模块代码", type = java.lang.String.class),
			@Param(name = "transCode", comment = "交易代码", type = java.lang.String.class),
			@Param(name = "paramKeyName", comment = "参数名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "paramValue", comment = "参数值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取参数信息", style = "判断型", type = "同步组件", comment = "按入参查询系统参数表，将参数值作为出参返回", author = "Anonymous", date = "2015-12-08 10:59:59")
	public static TCResult B_GetSysParam(String moduleCode, String transCode,
			String paramKeyName) {
		if(moduleCode == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "模块代码不能为空");
		}
		if(transCode == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "交易代码不能为空");
		}
		if(paramKeyName == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "参数名称不能为空");
		}
		/**
		 * 调用JDBC组件 预编译查询
		 */
		String poolName = null;
		String sqlcmd = "select paramvalue from tp_cip_sysparameters where modulecode = ? and transcode = ? and paramkeyname = ?";
		JavaList values = new JavaList();
		values.add(moduleCode);
		values.add(transCode);
		values.add(paramKeyName);
		int rownum = 1;
		String paramValue = null; // 参数值
		try {
			TCResult preparedSelect = P_Jdbc.preparedSelect(poolName, sqlcmd,
					values, rownum);
			List<?> outputParams = preparedSelect.getOutputParams();
			if (outputParams != null && outputParams.get(1) != null) {
				JavaList retList = (JavaList) outputParams.get(1);
				if (retList != null && retList.size() > 0) {
					JavaList colList = (JavaList) retList.get(0);
					paramValue = (String) colList.get(0);
				}
			} else {
				return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), "参数不存在");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(paramValue);
	}

	/**
	 * @category 初始化系统参数
	 * @param req
	 *            入参|入参字典__REQ__|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param paramLevel
	 *            入参|参数级别列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param moduleCode
	 *            入参|模块代码|{@link java.lang.String}
	 * @param transCode
	 *            入参|交易代码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "req", comment = "入参字典__REQ__", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "paramLevel", comment = "参数级别列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "moduleCode", comment = "模块代码", type = java.lang.String.class),
			@Param(name = "transCode", comment = "交易代码", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "初始化系统参数", style = "判断型", type = "同步组件", comment = "将符合条件的参数赋值到入参字典中\r\n参数级别列表[级别1（代表该参数适用于所有的模块和所有交易）,级别2（代表该参数适用于具体的模块下的所有交易），级别3（代表该参数适用于具体的模块下的具体交易）]\r\n如果为1，模块和交易代码入参可以为空；如果为2，模块代码入参不为空，交易代码可以为空；如果为3，模块和交易代码入参都不为空", date = "2016-04-15 10:25:18")
	public static TCResult B_InitSysParam(JavaDict req, JavaList paramLevel,
			String moduleCode, String transCode) {
		if (req == null) {
			return TCResult.newFailureResult(
					IErrorCode.getCode("FieldMustBeEntered"), "入参字典不能为空");
		}
		if (paramLevel == null) {
			return TCResult.newFailureResult(
					IErrorCode.getCode("FieldMustBeEntered"), "参数级别列表不能为空");
		}
		if (moduleCode == null) {
			//参数级别为2
			boolean contains2 = paramLevel.contains("2");
			if(contains2){
				return TCResult.newFailureResult(
						IErrorCode.getCode("FieldMustBeEntered"), "模块代码不能为空");
			}
		}
		if (transCode == null) {
			//参数级别为3
			boolean contains3 = paramLevel.contains("3");
			if(contains3){
				return TCResult.newFailureResult(
						IErrorCode.getCode("FieldMustBeEntered"), "交易代码不能为空");
			}
		}
		JavaList sqlValues = new JavaList();
		JavaList values = new JavaList();
		try {
			/**
			 * 拼接参数级别条件和值
			 */
			if (paramLevel != null && paramLevel.size() > 0) {
				for (int i = 0; i < paramLevel.size(); i++) {
					String level = (String) paramLevel.get(i);
					//参数级别为1
					if ("1".equals(level)) {
						sqlValues.add(" (paramlevel='1') ");
					} 
					//参数级别为2,设置模块代码
					else if ("2".equals(level)) {
						sqlValues.add(" (paramlevel='2' and modulecode= ?) ");
						values.add(moduleCode);
					}
					//参数级别为3,设置模块和交易代码
					else if ("3".equals(level)) {
						sqlValues.add(" (paramlevel='3' and modulecode= ? and transcode= ?) ");
						values.add(moduleCode);
						values.add(transCode);
					}
				}
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		String valueSql = null;
		String sep = "or";
		try {
			/**
			 * 调用平台级技术组件
			 * 字符串拼接
			 */
			TCResult joinStr = P_String.joinStr(sqlValues, sep);
			List<?> outputParams2 = joinStr.getOutputParams();
			if (outputParams2 != null && outputParams2.size() > 0) {
				valueSql = (String) outputParams2.get(0);
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		String poolName = null;
		StringBuffer sqlcmd = new StringBuffer(
				"select paramkeyname,paramvalue from tp_cip_sysparameters where initflag = '1' ");
		sqlcmd.append("and (").append(valueSql).append(")");
		int rownum = 0;
		try {
			/**
			 * 调用预编译查询
			 */
			TCResult preparedSelect = P_Jdbc.preparedSelect(poolName,
					sqlcmd.toString(), values, rownum);
			List<?> outputParams = preparedSelect.getOutputParams();
			/**
			 * 遍历结果集
			 * 将参数名称和参数值分别放入到__REQ__的key和value中
			 */
			if (outputParams != null && outputParams.size() > 0) {
				JavaList retList = (JavaList) outputParams.get(1);
				if (retList != null && retList.size() > 0) {
					for (int i = 0; i < retList.size(); i++) {
						JavaList colList = new JavaList();
						colList = (JavaList) retList.get(i);
						if (colList != null && colList.size() > 0) {
							req.put((String) colList.get(0),
									(String) colList.get(1));
						}
					}
				}
			} else {
				return TCResult.newFailureResult(
						IErrorCode.getCode("SystemException"), "初始化参数失败");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(req);
	}
	
	/**
	 * @param moduleCode
	 *            入参|模块代码|{@link java.lang.String}
	 * @param transCode
	 *            入参|交易代码|{@link java.lang.String}
	 * @param paramKeyName
	 *            入参|参数名称|{@link java.lang.String}
	 * @param paramValue
	 *            出参|参数值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "moduleCode", comment = "模块代码", type = java.lang.String.class),
			@Param(name = "transCode", comment = "交易代码", type = java.lang.String.class),
			@Param(name = "paramKeyName", comment = "参数名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "paramValue", comment = "参数值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取参数信息从redis", style = "判断型", type = "同步组件", comment = "按入参查询系统参数表，将参数值作为出参返回", author = "Anonymous", date = "2015-12-08 10:59:59")
	public static TCResult B_GetSysParamByRedis(String moduleCode, String transCode,
			String paramKeyName) {
		if(moduleCode == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "模块代码不能为空");
		}
		if(transCode == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "交易代码不能为空");
		}
		if(paramKeyName == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "参数名称不能为空");
		}
		//先从redis里取，取不到再查询数据库
		String ret = (String) CacheOperationImpl.get("redis","cache", paramKeyName);
		if(ret!=null){
			AppLogger.debug("redis="+ret);
			return TCResult.newSuccessResult(ret);
		}
		/**
		 * 调用JDBC组件 预编译查询
		 */
		String poolName = null;
		String sqlcmd = "select paramvalue from tp_cip_sysparameters where modulecode = ? and transcode = ? and paramkeyname = ?";
		JavaList values = new JavaList();
		values.add(moduleCode);
		values.add(transCode);
		values.add(paramKeyName);
		int rownum = 1;
		String paramValue = null; // 参数值
		try {
			TCResult preparedSelect = P_Jdbc.preparedSelect(poolName, sqlcmd,
					values, rownum);
			List<?> outputParams = preparedSelect.getOutputParams();
			if (outputParams != null && outputParams.get(1) != null) {
				JavaList retList = (JavaList) outputParams.get(1);
				if (retList != null && retList.size() > 0) {
					JavaList colList = (JavaList) retList.get(0);
					paramValue = (String) colList.get(0);
				}
			} else {
				return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), "参数不存在");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
		}
		try {
			CacheOperationImpl.putAndReplicate("redis", "cache", paramKeyName, paramValue);
			//设置超时时间【3分钟超时】
			CacheOperationImpl.expire("redis", "cache", paramKeyName, 180);
		} catch (CacheOperationException e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
		}
		return TCResult.newSuccessResult(paramValue);
	}
}
