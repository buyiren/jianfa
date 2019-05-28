package tc.bank.pcva;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
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
 * 业务检查类
 * 
 * @date 2015-12-04 11:55:33
 */
@ComponentGroup(level = "银行", groupName = "业务检查类")
public class B_BusiCheck {

	/**
	 * @param req
	 *            入参|入参字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "req", comment = "入参字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "重复请求校验", style = "判断型", type = "同步组件", date = "2015-12-09 10:56:29")
	public static TCResult B_checkTradeRepeat(JavaDict req) {
		if (req == null) {
			return TCResult.newFailureResult(
					IErrorCode.getCode("FieldMustBeEntered"), "入参字典不能为空");
		}
		/**
		 * 调用jdbc组件 预编译查询
		 */
		String channelcode = req.getItem("channelcode");
		String pservicecode = req.getItem("p_servicecode");
		String poolName = null;
		String sqlcmd = "select chkcollist from tp_cip_busirepeatrule where channelcode in('*',?) and p_servicecode = ? order by channelcode desc";
		JavaList valueList = new JavaList();
		valueList.add(channelcode);
		valueList.add(pservicecode);
		int rownum = 1;
		String valueStr = null;
		try {
			TCResult tcRset = P_Jdbc.preparedSelect(poolName, sqlcmd,
					valueList, rownum);
			List<?> outputParams = tcRset.getOutputParams();
			String[] chkcol = null;
			if (outputParams != null && outputParams.size() > 0) {
				JavaList chkcollist = (JavaList) outputParams.get(1);
				if (chkcollist != null && chkcollist.size() > 0) {
					JavaList chkcollist1 = (JavaList) chkcollist.get(0);
					String chkcollist2 = (String) chkcollist1.get(0);
					chkcol = chkcollist2.split(",");
					if (chkcol != null && chkcol.length > 0) {
						for (int j = 0; j < chkcol.length; j++) {
							AppLogger.info("查看" + chkcol[j] + "是否为空");
							AppLogger.info(chkcol[j] + "=="
									+ req.getItem(chkcol[j]));
							if (j == 0) {
								valueStr = req.getItem(chkcol[0]);
							} else {
								valueStr = valueStr + "|"
										+ req.getItem(chkcol[j]);
							}
						}
					}
				}
			} else {
				return TCResult.newFailureResult(
						IErrorCode.getCode("RepeatRuleNotExist"),
						"未找到服务登记的判重规则");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		AppLogger.info("拼好的判重检查条件字符串:" + valueStr);

		/**
		 * 调用jdbc组件 预编译插入
		 */
		String tableName = "tp_cip_busirepeatchk".toUpperCase();
		JavaList colList = new JavaList();
		JavaList colchannelcode = new JavaList();
		colchannelcode.add("CHANNELCODE");
		colchannelcode.add(channelcode);
		JavaList colrepeatchkkey = new JavaList();
		colrepeatchkkey.add("REPEATCHKKEY");
		colrepeatchkkey.add(valueStr);
		JavaList colpworkdate = new JavaList();
		colpworkdate.add("P_WORKDATE");
		colpworkdate.add(req.getItem("p_workdate"));
		colList.add(colchannelcode);
		colList.add(colrepeatchkkey);
		colList.add(colpworkdate);
		boolean commitFlg = true;
		try {
			TCResult preparedInsert = P_Jdbc.preparedInsert(poolName,
					tableName, colList, commitFlg);
			int status = preparedInsert.getStatus();
			if (status == 1) {
				return TCResult.newSuccessResult(status);
			} else {
				return TCResult.newFailureResult(
						IErrorCode.getCode("RepeatSerRequest"), "重复的服务请求");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
	}

	/**
	 * @category 必输参数检查
	 * @param reqdict
	 *            入参|检查容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param reqlist
	 *            入参|检查集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "reqdict", comment = "检查容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "reqlist", comment = "检查集如[[字段,长度],[\"workdate\",\"8\"]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "必输参数检查", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-08-10 10:42:46")
	public static TCResult B_CheckValue(JavaDict reqdict, JavaList reqlist) {
		
		if (reqdict == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "请求容器不能为空");
		}
		if (reqlist == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "入参检查容器中变量的列表不能为空");
		}
		for (int i = 0; i < reqlist.size(); i++) {
			Object param = reqlist.getItem(i);
			if (!(param instanceof JavaList)) {
				return TCResult.newFailureResult(ErrorCode.AGR,
						"入参检查容器中变量的列表的子元素必须为列表类型");
			}
			JavaList param0 = (JavaList) param;
			if (param0.size() < 1) {
				return TCResult.newFailureResult(ErrorCode.AGR,
						"入参检查容器中变量的列表的子元素列表的长度不能小于1");
			}
			if (!reqdict.hasKey(param0.getItem(0))) {
				return TCResult.newFailureResult(
						ErrorCode.AGR,
						"请求容器中参数["
								+ String.valueOf(param0.getItem(0)) + "]不存在");
			}
			if (param0.size() > 1) {
				if (param0.getItem(1) != null) {
					if (String.valueOf(
							reqdict.getItem(param0.getItem(0)))
							.length() > Integer.parseInt(String
							.valueOf(param0.getItem(1)))) {
						return TCResult.newFailureResult(ErrorCode.AGR, "入参中的值["
								+ String.valueOf(param0.getItem(0)
										+ "]长度错误"));
					}
				}
			}
		}
		return TCResult.newSuccessResult();
	}
}
