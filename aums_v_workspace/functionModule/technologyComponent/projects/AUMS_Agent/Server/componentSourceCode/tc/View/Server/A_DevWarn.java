package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import org.apache.commons.lang.StringUtils;

import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 預警共用組件
 * 
 * @author 李阔
 * @date 2017-08-28 14:25:14
 */
@ComponentGroup(level = "应用", groupName = "预警", projectName = "View", appName = "Agent")
public class A_DevWarn {

	/**
	 * @category 设备预警状态处理
	 * 
	 */
	@InParams(param = {
			@Param(name = "DeviceId", comment = "设备ID", type = java.lang.String.class),
			@Param(name = "ModelStr", comment = "模块列表", type = java.lang.String.class),
			@Param(name = "policyid", comment = "策略ID", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "ExceptionFlag", comment = "异常标识（0:无异常 1：有异常）", type = java.lang.String.class),
			@Param(name = "ExceptionStr", comment = "异常信息", type = java.lang.String.class),
			@Param(name = "brno", comment = "网点号", type = java.lang.String.class), })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "设备预警状态处理", style = "判断型", type = "同步组件", comment = "设备预警状态处理", author = "李阔", date = "2017-08-28 15:38:06")
	public static TCResult A_DeviceStat(String DeviceId, String ModelStr,
			String policyid) {

		// 异常标识 0:无异常 1：有异常
		String ExceptionFlag = "0";
		// 异常信息
		StringBuffer ExceptionStr = new StringBuffer();

		// 网点号
		String brno = "";

		try {
			AppLogger.info("请求设备ID:" + DeviceId + " 模块列表：" + ModelStr
					+ " 策略ID:" + policyid);

			String deviceSql = "select ta.DEVPZBRNO,tb.BRNONAME from t_pcva_devinfo ta ,t_pcva_brnoinfo tb where ta.devpzbrno=tb.brno and ta.devid='"
					+ DeviceId + "'";

			AppLogger.info("查询设备信息" + deviceSql);

			TCResult devTc = P_Jdbc.dmlSelectDict("", deviceSql, 1);

			if (1 == devTc.getStatus()) {
				JavaList list = (JavaList) devTc.getOutputParams().get(1);
				JavaDict dict = (JavaDict) list.get(0);
				brno = dict.getStringItem("DEVPZBRNO");
				ExceptionStr.append(dict.getStringItem("BRNONAME") + " 设备号:"
						+ DeviceId + " ");
			}

			// 需要判断机器是否开机在线，若不线暂时不处理(机器汇总状态 0正常1维护2设备异常3通讯故障)
			String devStat = (String) CacheOperationImpl.get("redis", "cache",
					"_DEV_" + DeviceId + "_ALLSTAT_");

			AppLogger.info("查询设备devStat_ALLSTAT_信息" + devStat);

			// 汇总状态正常，再预警具体的设备模块
			if (StringUtils.isNotBlank(devStat) && "0".equals(devStat)) {

				String[] modelList = ModelStr.split(",");

				for (int i = 0; i < modelList.length; i++) {
					String model = modelList[i];

					// 模塊狀態
					String modelStat = (String) CacheOperationImpl.get("redis",
							"cache", "_DEV_" + DeviceId + "_STAT_" + model
									+ "_");
					// 模块名称
					String modelname = "";
					// 外设状态正常
					if ("0".equals(modelStat)) {
						continue;
					}
					// 查询模块相关信息
					String querymodel = "SELECT MODELID,MODELNAME FROM T_PCVA_DEVMODELINFO WHERE  MODELID='"
							+ model + "'";
					AppLogger.info("查询模块信息" + querymodel);

					TCResult modeltc = P_Jdbc.dmlSelectDict("", querymodel, 1);

					if (1 == modeltc.getStatus()) {
						JavaList modellist = (JavaList) modeltc
								.getOutputParams().get(1);
						JavaDict modeldict = (JavaDict) modellist.get(0);
						modelname = modeldict.getStringItem("MODELNAME");
					}
					// Redis里面获取不到状态
					if (StringUtils.isBlank(modelStat)) {
						ExceptionStr.append(modelname + "模块状态未知 ");
					} else {
						ExceptionStr.append(modelname + "模块状态发生异常  ");
					}
					// 有异常发生
					ExceptionFlag = "1";
				}
			}
			return TCResult.newSuccessResult(ExceptionFlag,
					ExceptionStr.toString(), brno);
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB",
					"取设备外设模块状态发生异常：" + e.getMessage());
		}
	}

	/**
	 * @category 业务预警，预警卡箱剩余张数
	 * 
	 */
	@InParams(param = {
			@Param(name = "DeviceId", comment = "设备ID", type = java.lang.String.class),
			@Param(name = "PolicyMap", comment = "策略", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = {
			@Param(name = "ExceptionFlag", comment = "异常标识（0:无异常 1：有异常）", type = java.lang.String.class),
			@Param(name = "ExceptionStr", comment = "异常信息", type = java.lang.String.class),
			@Param(name = "brno", comment = "网点号", type = java.lang.String.class), })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "预警卡箱剩余张数", style = "判断型", type = "同步组件", comment = "预警卡箱剩余张数", author = "李阔", date = "2017-08-28 15:38:06")
	public static TCResult A_CheckCardStat(String DeviceId, JavaDict PolicyMap) {

		// 异常标识 0:无异常 1：有异常
		String ExceptionFlag = "0";
		// 异常信息
		StringBuffer ExceptionStr = new StringBuffer();

		// 网点号
		String brno = "";
		try {
			AppLogger.info("请求设备ID:" + DeviceId + " 策略MAP:"
					+ PolicyMap.toString());

			String deviceSql = "select ta.DEVPZBRNO,tb.BRNONAME from t_pcva_devinfo ta ,t_pcva_brnoinfo tb where ta.devpzbrno=tb.brno and ta.devid='"
					+ DeviceId + "'";

			AppLogger.info("查询设备信息" + deviceSql);

			TCResult devTc = P_Jdbc.dmlSelectDict("", deviceSql, 1);

			if (1 == devTc.getStatus()) {
				JavaList list = (JavaList) devTc.getOutputParams().get(1);
				JavaDict dict = (JavaDict) list.get(0);
				brno = dict.getStringItem("DEVPZBRNO");
				ExceptionStr.append(dict.getStringItem("BRNONAME") + " 设备号:"
						+ DeviceId + " ");
			}

			// 需要判断机器是否开机在线，若不线暂时不处理(机器汇总状态 0正常1维护2设备异常3通讯故障)
			String devStat = (String) CacheOperationImpl.get("redis", "cache",
					"_DEV_" + DeviceId + "_ALLSTAT_");

			// 汇总状态正常，再预警
			if (StringUtils.isNotBlank(devStat) && "0".equals(devStat)) {

				String cardBox = "select dvcnum,cardboxnum,certikind,count(1) as cnt from t_pcva_cardboxvouinfo where dvcnum='"
						+ DeviceId
						+ "' and vchrste='0' group by dvcnum,cardboxnum,certikind  having(count(1)) "
						+ PolicyMap.getStringItem("POLICYFUNCTION")
						+ " to_number("
						+ PolicyMap.getStringItem("POLICYVALUE") + ")";

				AppLogger.info("查询卡箱信息" + cardBox);
				TCResult cardTc = P_Jdbc.dmlSelectDict("", cardBox, 1);

				if (1 == cardTc.getStatus()) {

					JavaList javaList = (JavaList) cardTc.getOutputParams()
							.get(1);

					for (int i = 0; i < javaList.size(); i++) {

						JavaDict dict = (JavaDict) javaList.get(i);

						ExceptionStr.append("卡箱号："
								+ dict.getStringItem("CARDBOXNUM") + " 凭证种类:"
								+ dict.getStringItem("CERTIKIND") + " 剩余张数："
								+ dict.getStringItem("CNT") + "");
						ExceptionFlag = "1";
					}
				}
			}
			return TCResult.newSuccessResult(ExceptionFlag,
					ExceptionStr.toString(), brno);
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB",
					"取预警卡箱剩余张数发生异常：" + e.getMessage());
		}
	}

}