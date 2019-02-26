package tc.AUMS_C.PCVA.product.custom;


import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tc.bank.constant.BusException;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.constant.IErrorCode;
import tc.bank.utils.RetResultUtil;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 任务处理类方法
 * 
 * @date 2017-09-04 10:21:18
 */
@ComponentGroup(level = "应用", groupName = "远程客服任务处理")
public class A_DealTaskMethod {

	/**
	 * @category 获取全流程节点
	 * @param flowdata
	 *            入参|流程信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param flownum
	 *            入参|流程条数|{@link int}
	 * @param flowpathnum
	 *            出参|全流程序号|{@link java.lang.String}
	 * @param flowpathname
	 *            出参|全流程名称|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "flowdata", comment = "流程信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "flownum", comment = "流程条数", type = int.class) })
	@OutParams(param = {
			@Param(name = "flowpathnum", comment = "全流程序号", type = java.lang.String.class),
			@Param(name = "flowpathname", comment = "全流程名称", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取全流程节点", style = "判断型", type = "同步组件", date = "2017-09-07 08:55:06")
	public static TCResult B_GetFlowInfo(JavaList flowdata, int flownum) {
		String flowpathnum = "", flowpathname = "", modenodestatus = "";
		Boolean nodeflag = false;
		double max = 1;
		double min = 0.01;
		double init = 0;
		for (int i = 0; i < flownum; i++) {
			String nodeid = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODEID");
			String nodename = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODENAME");
			double nodenum = Double.parseDouble(((JavaDict) (flowdata.get(i)))
					.getStringItem("NODENUM"));
			String nodetype = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODETYPE");
			String nodestatus = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODESTATUS");
			String nodeio = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODEIO");
			String nodekey = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODEKEY");
			String nodevalue = ((JavaDict) (flowdata.get(i)))
					.getStringItem("NODEVALUE");

			// 查询当前节点模板状态
			String sqlcmd = "select NODESTATUS from T_ARB_FLOWMODE where nodeid = ? ";
			JavaList values = new JavaList();
			values.add(nodeid);
			TCResult rt = P_Jdbc.preparedSelect(null, sqlcmd, values, 0);
			int status = rt.getStatus();
			if (status == 2) {
				modenodestatus = "0";
				AppLogger.debug("无该结点状态控制字段，默认开启");
			} else if (status == 1) {
				JavaList result = (JavaList) rt.getOutputParams().get(1);
				if (result.size() == 0) {
					modenodestatus = "0";
					AppLogger.debug("无该结点状态控制字段，默认开启");
				} else {
					JavaList resl = result.getItem(0);
					AppLogger.debug("resl" + resl);
					modenodestatus = resl.getStringItem(0);
				}

			} else {
				if (rt.getErrorCode() != null || rt.getErrorMsg() != null) {
					return TCResult.newFailureResult(rt.getErrorCode(),
							"查询节点模板表异常:" + rt.getErrorMsg());
				}
			}

			if (nodeio != null && "1".equals(nodetype) && "0".equals(nodeio)) {
				nodeflag = true;
				init = init + max + max;
				if ("".equals(flowpathnum)) {
					flowpathnum = "" + subZeroAndDot(nodenum);
					flowpathname = "" + nodename;
				} else {
					flowpathnum = flowpathnum + "-" + subZeroAndDot(nodenum);
					flowpathname = flowpathname + "-" + nodename;
				}
				continue;
			} else if (nodeio != null && "1".equals(nodetype)
					&& "1".equals(nodeio)) {
				nodeflag = false;
				init = nodenum;
				continue;
			} else {
				if (nodeflag == false) {
					init = init + max;
					if (init == nodenum) {
						if (!"1".equals(nodestatus)
								&& !"1".equals(modenodestatus)) {
							if ("".equals(flowpathnum)) {
								flowpathnum = "" + subZeroAndDot(nodenum);
								flowpathname = "" + nodename;
							} else {
								flowpathnum = flowpathnum + "-"
										+ subZeroAndDot(nodenum);
								flowpathname = flowpathname + "-" + nodename;
							}
						} else {
							continue;
						}

					} else {
						return RetResultUtil
								.getTCResToExternal(new BusException(
										ErrorCodeModule.IME005));
					}
				} else {
					init = init + min;

					if (init == nodenum) {
						if (!"1".equals(nodestatus)
								&& !"1".equals(modenodestatus)) {
							if ("".equals(flowpathnum)) {
								flowpathnum = "" + subZeroAndDot(nodenum);
								flowpathname = "" + nodename;
							} else {
								flowpathnum = flowpathnum + "-"
										+ subZeroAndDot(nodenum);
								flowpathname = flowpathname + "-" + nodename;
							}
						} else {
							continue;
						}

					} else {
						continue;
					}
				}
			}
		}
		return TCResult.newSuccessResult(flowpathnum, flowpathname);
	}

	/**
	 * @category 处理时间计算
	 * @param StartTime
	 *            入参|开始时间|{@link java.lang.String}
	 * @param EndTime
	 *            入参|结束时间|{@link java.lang.String}
	 * @param Dealtime
	 *            出参|处理时间|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "StartTime", comment = "开始时间", type = java.lang.String.class),
			@Param(name = "EndTime", comment = "结束时间", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "Dealtime", comment = "处理时间", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "处理时间计算", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-09-04 03:59:20")
	public static TCResult A_DealTimeMethod(String StartTime, String EndTime) {
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Date d1 = df.parse(StartTime.substring(0, 16));
			Date d2 = df.parse(EndTime.substring(0, 16));
			long Dealtime = (d2.getTime() - d1.getTime());
			String Dealtimes = "" + Dealtime;
			return TCResult.newSuccessResult(Dealtimes);
		} catch (ParseException e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}

	}

	/**
	 * @category 找到下一个节点
	 * @param NodeNum
	 *            入参|当前节点序号|{@link java.lang.String}
	 * @param __REQ__
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param flowdata
	 *            入参|流程信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param flownum
	 *            入参|流程条数|{@link int}
	 * @param NextNode
	 *            出参|下一个节点信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param FlowPathNum
	 *            出参|全流程序号|{@link java.lang.String}
	 * @param FlowPathName
	 *            出参|全流程名称|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 无记录<br/>
	 */
	@InParams(param = {
			@Param(name = "NodeNum", comment = "当前节点序号", type = java.lang.String.class),
			@Param(name = "__REQ__", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "flowdata", comment = "流程信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "flownum", comment = "流程条数", type = int.class) })
	@OutParams(param = {
			@Param(name = "NextNode", comment = "下一个节点信息", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "FlowPathNum", comment = "全流程序号", type = java.lang.String.class),
			@Param(name = "FlowPathName", comment = "全流程名称", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无记录") })
	@Component(label = "找到下一个节点", style = "判断型", type = "同步组件", date = "2017-09-07 03:26:52")
	public static TCResult B_GetNextNode(String NodeNum, JavaDict __REQ__,
			JavaList flowdata, int flownum) {
		try {
			JavaDict NextNode = new JavaDict();
			String flowpathnum = "", flowpathname = "", modenodestatus = "";
			Boolean nodeflag = false, nodenumflag = false, prenodeflag = false, setflag = false;
			int max = 1;
			double min = 0.01;
			double init = 0.0, preNodenum = 0.0;
			if (Double.parseDouble(NodeNum) % 1 == 0) {
				preNodenum = Double.parseDouble(NodeNum);
			} else {
				// 当前节点为已进入虚节点分支标志
				prenodeflag = true;
				preNodenum = (int) Double.parseDouble(NodeNum);

			}
			AppLogger.info("=====preNodenum====" + preNodenum);
			// 当前节点是否最后一个节点
			if (Double.parseDouble(NodeNum) == Double
					.parseDouble(((JavaDict) (flowdata.get(flownum - 1)))
							.getStringItem("NODENUM"))) {
				return new TCResult(2, ErrorCode.AGR, "节点已走完");
			}
			for (int i = 0; i < flownum; i++) {
				String keystr = "";
				JavaDict nodevalueDict = new JavaDict();
				String nodeid = ((JavaDict) (flowdata.get(i)))
						.getStringItem("NODEID");
				String nodename = ((JavaDict) (flowdata.get(i)))
						.getStringItem("NODENAME");
				double nodenum = Double.parseDouble(((JavaDict) (flowdata
						.get(i))).getStringItem("NODENUM"));
				String nodetype = ((JavaDict) (flowdata.get(i)))
						.getStringItem("NODETYPE");
				String nodestatus = ((JavaDict) (flowdata.get(i)))
						.getStringItem("NODESTATUS", "");
				String nodeio = ((JavaDict) (flowdata.get(i))).getStringItem(
						"NODEIO", "");
				String nodekey = ((JavaDict) (flowdata.get(i))).getStringItem(
						"NODEKEY", "");
				String nodevalue = ((JavaDict) (flowdata.get(i)))
						.getStringItem("NODEVALUE", "");

				// 查询当前节点模板状态
				String sqlcmd = "select NODESTATUS from T_ARB_FLOWMODE where nodeid = ? ";
				JavaList values = new JavaList();
				values.add(nodeid);
				TCResult rt = P_Jdbc.preparedSelect(null, sqlcmd, values, 0);
				int status = rt.getStatus();
				if (status == 2) {
					modenodestatus = "0";
					AppLogger.debug("无该结点状态控制字段，默认开启");
				} else if (status == 1) {
					JavaList result = (JavaList) rt.getOutputParams().get(1);
					if (result.size() == 0) {
						modenodestatus = "0";
						AppLogger.debug("无该结点状态控制字段，默认开启");
					} else {
						JavaList resl = result.getItem(0);
						AppLogger.debug("resl" + resl);
						modenodestatus = resl.getStringItem(0);
					}

				} else {
					if (rt.getErrorCode() != null || rt.getErrorMsg() != null) {
						return TCResult.newFailureResult(rt.getErrorCode(),
								"查询节点模板表异常:" + rt.getErrorMsg());
					}
				}

				// 虚节点判断字段合并
				List nodekeyList = Arrays.asList(nodekey.split(","));
				if (nodevalue != null && !"".equals(nodevalue)) {
					JSONObject jsonObj = (JSONObject) JSON
							.parseObject(nodevalue);
					nodevalueDict = getJavaDict(jsonObj);
				}
				// 虚节点，进入
				if (nodeio != null && "1".equals(nodetype)
						&& "0".equals(nodeio)) {
					// 设置虚节点进入标志
					nodeflag = true;
					JavaDict jsonDict = new JavaDict();
					for (int j = 0; j < nodekeyList.size(); j++) {
						if (__REQ__.get("FLOW_BUSIDATA") != null
								&& !"".equals(__REQ__.get("FLOW_BUSIDATA"))) {
							JSONObject jsonObj = (JSONObject) JSON
									.parseObject(__REQ__.get("FLOW_BUSIDATA")
											.toString());
							jsonDict = getJavaDict(jsonObj);
						}
						if ("".equals(__REQ__.getStringItem(
								(nodekeyList.get(j)), ""))) {
							// 已走过虚节点去业务数据里拿走的分支字段
							if ("".equals(jsonDict.getStringItem(
									(nodekeyList.get(j)), ""))) {
								// 虚节点判断字段为空表示还未走到设置未走到标志
								nodenumflag = false;
								break;
							} else {
								// 虚节点判断字段组合并设置已走到标志
								keystr = jsonDict.get(nodekeyList.get(j)) + "&";
								nodenumflag = true;
							}
						} else {
							// 虚节点判断字段组合并设置已走到标志
							keystr = __REQ__.get(nodekeyList.get(j)) + "&";
							nodenumflag = true;
						}
					}
					if (nodenumflag == true) {
						// 已走到虚节点将下一节点序号赋值给init
						keystr = keystr.substring(0, keystr.length() - 1);
						init = Double.parseDouble(nodevalueDict
								.getStringItem(keystr));
					} else {
						AppLogger
								.info("===================0000000000000==============="
										+ prenodeflag
										+ "====="
										+ preNodenum
										+ "=====" + nodevalueDict.values());
						if (prenodeflag == true
								&& nodevalueDict
										.containsValue((int) (preNodenum)) == true) {
							// 已走过虚节点

							init = preNodenum;
							AppLogger
									.info("===================111111111111111==============="
											+ init);
						} else {
							// 还未走到虚节点

							init = init + max + max;
							AppLogger
									.info("===================222222222222222==============="
											+ init);
						}
					}
					if ("1".equals(nodestatus)) {
						AppLogger.info("虚节点状态设置为停用状态不会生效");
					}
					if (setflag == true) {
						NextNode.put("nodeid", nodeid);
						NextNode.put("nodename", nodename);
						NextNode.put("nodenum", subZeroAndDot(nodenum));
						setflag = false;
					}
					if (Double.parseDouble(NodeNum) == nodenum) {
						setflag = true;
					} else {
						setflag = false;
					}
					if ("".equals(flowpathnum)) {
						flowpathnum = "" + subZeroAndDot(nodenum);
						flowpathname = "" + nodename;
					} else {
						flowpathnum = flowpathnum + "-"
								+ subZeroAndDot(nodenum);
						flowpathname = flowpathname + "-" + nodename;
					}
					continue;

				} else if (nodeio != null && "1".equals(nodetype)
						&& "1".equals(nodeio)) {
					nodeflag = false;
					init = nodenum;
					if ("1".equals(nodestatus)) {
						AppLogger.info("虚节点状态设置为停用状态不会生效");
					}
					continue;
				} else {
					if (nodeflag == false) {
						init = init + max;
						if (init == nodenum) {
							if (!"1".equals(nodestatus)
									&& !"1".equals(modenodestatus)) {
								if (setflag == true) {
									NextNode.put("nodeid", nodeid);
									NextNode.put("nodename", nodename);
									NextNode.put("nodenum",
											subZeroAndDot(nodenum));
								}
								if (Double.parseDouble(NodeNum) == nodenum) {
									setflag = true;
								} else {
									setflag = false;
								}
								if ("".equals(flowpathnum)) {
									flowpathnum = "" + subZeroAndDot(nodenum);
									flowpathname = "" + nodename;
								} else {
									flowpathnum = flowpathnum + "-"
											+ subZeroAndDot(nodenum);
									flowpathname = flowpathname + "-"
											+ nodename;
								}
							} else {
								continue;
							}

						} else {
							return RetResultUtil
									.getTCResToExternal(new BusException(
											ErrorCodeModule.IME005));
						}

					} else {
						init = init + min;
						if (init == nodenum) {
							if (!"1".equals(nodestatus)
									&& !"1".equals(modenodestatus)) {
								if (setflag == true) {
									NextNode.put("nodeid", nodeid);
									NextNode.put("nodename", nodename);
									NextNode.put("nodenum",
											subZeroAndDot(nodenum));
								}
								if (Double.parseDouble(NodeNum) == nodenum) {
									setflag = true;
								} else {
									setflag = false;
								}
								if ("".equals(flowpathnum)) {
									flowpathnum = "" + subZeroAndDot(nodenum);
									flowpathname = "" + nodename;
								} else {
									flowpathnum = flowpathnum + "-"
											+ subZeroAndDot(nodenum);
									flowpathname = flowpathname + "-"
											+ nodename;
								}
							} else {
								continue;
							}

						} else {
							init = init - min;
							continue;
						}
					}
				}
			}

			// 判断当前节点是否最后一个节点
			if (Double.parseDouble(NodeNum) == Double.parseDouble(flowpathnum
					.substring(flowpathnum.lastIndexOf("-") + 1,
							flowpathnum.length()))) {
				return new TCResult(2, ErrorCode.AGR, "节点已走完");
			}
			return TCResult.newSuccessResult(NextNode, flowpathnum,
					flowpathname);
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
	}

	private static JavaDict getJavaDict(JSONObject jsonObj) {
		JavaDict dict = new JavaDict();

		for (@SuppressWarnings("rawtypes")
		Map.Entry entry : jsonObj.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				dict.setItem(key, null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				dict.setItem(key, getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				dict.setItem(key, getJavaDict((JSONObject) value));
			} else {
				dict.setItem(key, value);
			}
		}
		return dict;
	}

	private static JavaList getJavaList(JSONArray seq) {
		JavaList list = new JavaList();
		for (int i = 0; i < seq.size(); i++) {
			Object value = seq.get(i);
			if (value == null) {
				list.add(null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				list.add(getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				list.add(getJavaDict((JSONObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

	private static String subZeroAndDot(double num) {
		if (num % 1.0 == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}

	/**
	 * @category 结点数据拼成json字符串
	 * @param reqdata
	 *            入参|请求容器数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param nodeid
	 *            入参|结点ID|{@link java.lang.String}
	 * @param busistep
	 *            入参|业务步骤|{@link java.lang.String}
	 * @param rspstrdata
	 *            出参|返回的json串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "reqdata", comment = "请求容器数据", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "nodeid", comment = "结点ID", type = java.lang.String.class),
			@Param(name = "busistep", comment = "业务步骤", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rspstrdata", comment = "返回的json串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "结点数据拼成json字符串", style = "判断型", type = "同步组件", comment = "根据上送的结点数据拼成json字符串", date = "2017-12-08 06:51:55")
	public static TCResult B_BusJsonData(JavaDict reqdata, String nodeid,
			String busistep) {
		try {
			JavaDict tmpdict = new JavaDict();
			if (reqdata == null) {
				return TCResult.newFailureResult(ErrorCode.AGR, "请求容器数据不能为空");
			}
			if (nodeid == null) {
				return TCResult.newFailureResult(ErrorCode.AGR, "结点ID不能为空");
			}
			if (busistep == null) {
				return TCResult.newFailureResult(ErrorCode.AGR, "业务步骤不能为空");
			}
			String sqlcmd = "select fieldcode,fieldinit,fieldio,tradefieldcode from t_arb_flowfieldinfo where nodeid = ? and busistep = ? and fieldflag='1' order by fieldnum ";
			JavaList values = new JavaList();
			values.add(nodeid);
			values.add(busistep);
			TCResult rt = P_Jdbc.preparedSelect(null, sqlcmd, values, 0);
			int status = rt.getStatus();
			if (status == 2) {
				AppLogger.debug("无该结点相关配置字段，直接返回空json串");
			} else if (status == 1) {
				JavaList result = (JavaList) rt.getOutputParams().get(1);
				if (result.size() == 0) {
					AppLogger.debug("无该结点相关配置字段，直接返回空json串");
				} else {
					for (int i = 0; i < result.size(); i++) {
						JavaList resl = result.getItem(i);
						AppLogger.debug("resl" + resl);
						String tmpfieldcode = "";
						tmpfieldcode = resl.getStringItem(0);
						Object tmpvalue;
						tmpvalue = reqdata.get(tmpfieldcode);
						if (tmpvalue == null || "".equals(tmpvalue)) {
							AppLogger.debug("节点步骤无此字段值"+tmpfieldcode);
						}else{
							tmpdict.put(tmpfieldcode, tmpvalue);
						}
						
					}
				}
			} else {
				if (rt.getErrorCode() != null || rt.getErrorMsg() != null) {
					return TCResult.newFailureResult(rt.getErrorCode(),
							"查询节点配置表异常:" + rt.getErrorMsg());
				}
			}
			if (!"".equals(reqdata.getStringItem("FILEINDXNO", ""))
					&& reqdata.getStringItem("FILEINDXNO", "") != null) {
				tmpdict.put("FILEINDXNO",
						reqdata.getStringItem("FILEINDXNO", ""));
			}
			tmpdict.put("AGENTID", reqdata.getStringItem("AGENTID", ""));
			tmpdict.put("AGENTLEVEL", reqdata.getStringItem("AGENTLEVEL", ""));
			tmpdict.put("AUTH_AGENTID",
					reqdata.getStringItem("AUTH_AGENTID", ""));
			tmpdict.put("AUTH_AGENTLEVEL",
					reqdata.getStringItem("AUTH_AGENTLEVEL", ""));
			tmpdict.put("tellerno", reqdata.getStringItem("tellerno", ""));
			tmpdict.put("branch", reqdata.getStringItem("branch", ""));
			tmpdict.put("devicenum", reqdata.getStringItem("devicenum", ""));
			tmpdict.put("devicetype", reqdata.getStringItem("devicetype", ""));
			tmpdict.put("CHANNELCODE", reqdata.getStringItem("CHANNELCODE", ""));
			tmpdict.put("CHECKTELLERNO",
					reqdata.getStringItem("CHECKTELLERNO", ""));
			tmpdict.put("CHECKTELLERNAME",
					reqdata.getStringItem("CHECKTELLERNAME", ""));
			if (reqdata.get("OTHERDATA") == null
					|| "".equals(reqdata.get("OTHERDATA"))) {
				AppLogger.info("该节点步骤无附加数据域");
			} else {
				tmpdict.put("OTHERDATA", reqdata.get("OTHERDATA"));
			}
			String jsonStr = JSON.toJSONString(tmpdict,
					SerializerFeature.WriteMapNullValue);
			return TCResult.newSuccessResult(jsonStr);
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
	}

	public static void main(String[] args) throws ParseException {

		// JavaDict tmpdict = new JavaDict();
		// TCResult rt = B_BusJsonData(tmpdict, "22", "1");
		// System.out.println(rt.getOutputParams());
		// DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// Date d1 = df.parse("20170912084003000737".substring(0,16));
		// Date d2 = df.parse("20170912084358000544".substring(0,16));
		// long diff = (d1.getTime() - d2.getTime()) ;
		// String NodeNum="5.01";
		// Boolean prenodeflag=false;
		// double preNodenum = 0.0;
		// if (Double.parseDouble(NodeNum)%1==0) {
		// preNodenum = Double.parseDouble(NodeNum);
		// System.out.println("11111111111");
		// } else {
		// prenodeflag = true;
		// preNodenum = (int) Double.parseDouble(NodeNum);
		// System.out.println("22222222222");
		// }
		// JavaDict jd=new JavaDict();
		// jd.put("1", 4.0);
		// jd.put("2", 5.0);
		// JavaList jl=new JavaList();
		// jl.add(4.0);
		// jl.add(5.0);
		// JavaDict jd = new JavaDict();
		// JavaDict sjd = new JavaDict();
		//
		// sjd.put("name", "zs");
		// sjd.put("age", "20");
		// jd.put("OTHERDATA", sjd);
		// Set jl = ((JavaDict) jd.get("OTHERDATA")).keySet();
		// System.out.println(jl);
		// System.out.println(jl.iterator().next());
		//
		// JavaDict jd2 = new JavaDict();
		JavaDict sjd2 = new JavaDict();

		sjd2.put("id", "1234");
		sjd2.put("age", "22");
		sjd2.put("1", "10");
		sjd2.put("2", 9.0);
		// jd2.put("OTHERDATA", sjd2);
		// System.out.println(jd2);
		// ((JavaDict) jd.get("OTHERDATA")).put("OTHERDATA", jd2);

		// System.out.println(sjd2);
		// System.out.println(sjd2.size());
		// Set jl=sjd2.keySet();
		// System.out.println(jl);
		// Object key = jl.iterator().next();
		// System.out.println("========key=========" + key);
		// Object key2 = jl.iterator().next();
		// System.out.println("========key=========" + key2);
		// for(Object obj:jl){
		// System.out.println(obj);
		// }
		System.out.println(sjd2.values());
		double preNodenum = 10.0301;
		String x = String.valueOf(preNodenum);

		String y = x.substring(x.lastIndexOf(".") + 1, x.length());
		System.out.println(y.length() - 2);
		BigDecimal bd = new BigDecimal(preNodenum);
		bd = bd.setScale(y.length() - 2, RoundingMode.HALF_DOWN);
		System.out.println(bd);
		System.out.println(sjd2.containsValue(bd.toString()));

	}

	/**
	 * @category 组织输出循环
	 * @param __REQ__
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param fullflag
	 *            入参|全量标志|boolean
	 * @param rsplist
	 *            出参|输出结果集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "cond_data_context", comment = "条件数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "busioper", comment = "业务操作关键字", type = java.lang.String.class),
			@Param(name = "ext_context", comment = "扩展参数字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "map_context", comment = "数据操作映射数据容器(业务扩展信息）", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = { @Param(name = "rsplist", comment = "输出结果集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "组织输出循环", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-09-05 07:41:49")
	public static TCResult B_OutputField(JavaDict cond_data_context,
			String busioper, JavaDict ext_context, JavaDict map_context) {
		try {

			TCResult result1 = A_DBUnityRptOper.A_DBUnityRptOpr(
					cond_data_context, busioper, ext_context, map_context);
			if (result1 == null) {
				AppLogger.debug("无该节点相关配置字段，直接返回空结果集");
			}
			int status = result1.getStatus();
			if (result1.getErrorCode() != null || result1.getErrorMsg() != null) {
				return TCResult.newFailureResult(result1.getErrorCode(),
						result1.getErrorMsg());
			}
			if (status == 2) {
				return TCResult.newFailureResult(ErrorCode.AGR, "无满足条件记录");
			} else if (status == 0) {
				return TCResult.newFailureResult(ErrorCode.AGR, "调用查询组件失败");
			}
			List outputParams = result1.getOutputParams();
			JavaList _Result_Data_ = new JavaList();
			int _RecordNum_ = 0;
			if (outputParams != null) {
				if (outputParams.size() != 5) {
					return TCResult.newFailureResult(ErrorCode.AGR,
							"调用查询组件返回的参数不对");
				}
				_Result_Data_ = (JavaList) outputParams.get(0);
				_RecordNum_ = (Integer) outputParams.get(1);
				for (int i = 0; i < _RecordNum_; i++) {
					String keycode = (String) ((JavaDict) _Result_Data_.get(i))
							.get("FIELDCODE");
					if (cond_data_context.get(keycode) == null) {
						((JavaDict) _Result_Data_.get(i)).put("FIELDVALUE", "");
					} else {
						((JavaDict) _Result_Data_.get(i)).put("FIELDVALUE",
								cond_data_context.get(keycode));
					}

				}
				return TCResult.newSuccessResult(_Result_Data_);

			} else {
				return TCResult.newFailureResult(ErrorCode.AGR, "调用查询组件失败");
			}

			// if(fullflag==true){
			// sqlcmd="select c.nodenum NODENUM,a.busistep FIELDBUSISTEP,a.fieldcode FIELDCODE,a.fieldname FIELDNAME,b.fieldtype FIELDTYPE,b.fieldlength FIELDLENGTH,a.fieldnum FIELDNUM,"
			// +
			// "a.fieldinit FIELDINIT,a.modifyflag MODIFYFLAG,a.mustflag MUSTFLAG,a.fieldio FIELDIO from t_arb_flowfieldinfo a,tp_cip_datadict b,t_arb_flownodeinfo c "
			// +
			// "where a.fieldcode=b.fieldcode and a.nodeid=c.nodeid and c.flowid=? order by NODENUM,FIELDBUSISTEP,FIELDNUM";
			// values.add(__REQ__.get("FLOWID"));
			// }else{
			// sqlcmd="select c.nodenum NODENUM,a.busistep FIELDBUSISTEP,a.fieldcode FIELDCODE,a.fieldname FIELDNAME,b.fieldtype FIELDTYPE,b.fieldlength FIELDLENGTH,a.fieldnum FIELDNUM,"
			// +
			// "a.fieldinit FIELDINIT,a.modifyflag MODIFYFLAG,a.mustflag MUSTFLAG,a.fieldio FIELDIO from t_arb_flowfieldinfo a,tp_cip_datadict b,t_arb_flownodeinfo c "
			// +
			// "where a.fieldcode=b.fieldcode and a.nodeid=c.nodeid and a.fieldio='2' and c.flowid=? and a.nodeid=? order by NODENUM,FIELDBUSISTEP,FIELDNUM";
			// values.add(__REQ__.get("FLOWID"));
			// values.add(__REQ__.get("NODEID"));
			// }

		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}

	}

	/**
	 * @category 组织推送消息数据
	 * @param PushMsgList
	 *            入参|上送消息数据集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param rsplist
	 *            出参|返回消息数据集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "PushMsgList", comment = "上送消息数据集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "rsplist", comment = "返回消息数据集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "组织推送消息数据", style = "判断型", type = "同步组件", date = "2017-10-17 04:20:02")
	public static TCResult B_PushMessageField(JavaList PushMsgList) {
		try {
			int index = PushMsgList.size();
			for (int i = 0; index > i; i++) {
				String fieldcode = ((JavaDict) PushMsgList.get(i)).get(
						"FIELDCODE").toString();
				String sqlcmd = "select fieldcode,fieldname,fieldtype,fieldlength from tp_cip_datadict where fieldcode = ?";
				JavaList values = new JavaList();
				values.add(fieldcode);
				TCResult rt = P_Jdbc.preparedSelect(null, sqlcmd, values, 0);
				int status = rt.getStatus();
				if (status == 2) {
					AppLogger.debug("数据字典无该配置字段");
					PushMsgList.remove(i);
					i = i - 1;
					index = index - 1;
				} else if (status == 1) {
					JavaList result = (JavaList) rt.getOutputParams().get(1);
					if (result.size() == 0) {
						AppLogger.debug("数据字典无该配置字段");
						PushMsgList.remove(i);
						i = i - 1;
						index = index - 1;
					} else {
						JavaList resl = result.getItem(0);
						AppLogger.debug("resl" + resl);
						String tmpfieldname = resl.getStringItem(1);
						String tmpfieldtype = resl.getStringItem(2);
						String tmpfieldlength = resl.getStringItem(3);
						((JavaDict) PushMsgList.get(i)).put("FIELDNAME",
								tmpfieldname);
						((JavaDict) PushMsgList.get(i)).put("FIELDTYPE",
								tmpfieldtype);
						((JavaDict) PushMsgList.get(i)).put("FIELDLENGTH",
								tmpfieldlength);
					}
				} else {
					if (rt.getErrorCode() != null || rt.getErrorMsg() != null) {
						return TCResult.newFailureResult(rt.getErrorCode(),
								"查询数据字典表异常:" + rt.getErrorMsg());
					}
				}
			}
			return TCResult.newSuccessResult(PushMsgList);
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
	}

	/**
	 * @category 合并Json
	 * @param Jsonfir
	 *            入参|原Json串|{@link java.lang.String}
	 * @param Jsonsen
	 *            入参|合并Json串|{@link java.lang.String}
	 * @param Jsonnew
	 *            出参|新Json串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "Jsonfir", comment = "原Json串", type = java.lang.String.class),
			@Param(name = "Jsonsen", comment = "合并Json串", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "Jsonnew", comment = "新Json串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "合并Json", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-09-07 04:08:05")
	public static TCResult B_GerNewJson(String Jsonfir, String Jsonsen) {
		JSONObject jsonObjfir = (JSONObject) JSON.parseObject(Jsonfir);
		JavaDict jsonDictfir = getJavaDict(jsonObjfir);

		JSONObject jsonObjsen = (JSONObject) JSON.parseObject(Jsonsen);
		JavaDict jsonDictsen = getJavaDict(jsonObjsen);
		// 数据域追加
		if (jsonDictfir.containsKey("OTHERDATA")
				&& jsonDictsen.containsKey("OTHERDATA")) {
			// for (int i = 0; i < ((JavaDict) jsonDictsen.get("OTHERDATA"))
			// .keySet().size(); i++) {
			// Set jl = ((JavaDict) jsonDictsen.get("OTHERDATA")).keySet();
			// Object key = jl.iterator().next();
			// AppLogger.info("========key=========" + key);
			// ((JavaDict) jsonDictfir.get("OTHERDATA")).put(key,
			// ((JavaDict) jsonDictsen.get("OTHERDATA")).get(key));
			// }
			// 修改上述for循环方法
			Set set = ((JavaDict) jsonDictsen.get("OTHERDATA")).keySet();
			for (Object key : set) {
				((JavaDict) jsonDictfir.get("OTHERDATA")).put(key,
						((JavaDict) jsonDictsen.get("OTHERDATA")).get(key));
			}
			jsonDictsen.put("OTHERDATA", jsonDictfir.get("OTHERDATA"));
		}
		jsonDictfir.putAll(jsonDictsen);

		String Jsonnew = JSON.toJSONString(jsonDictfir,
				SerializerFeature.WriteMapNullValue);

		return TCResult.newSuccessResult(Jsonnew);
	}

	/**
	 * @category 转换循环字段到容器中
	 * @param facedatain
	 *            入参|循环字段集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param reqdict
	 *            入参|目的容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "facedatain", comment = "循环字段集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "reqdict", comment = "目的容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "转换循环字段到容器中", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-09-11 04:09:06")
	public static TCResult B_ArbTransDict(JavaList facedatain, JavaDict reqdict) {
		try {
			for (int i = 0; i < facedatain.size(); i++) {
				reqdict.put(((JavaDict) facedatain.get(i)).get("FIELDCODE"),
						((JavaDict) facedatain.get(i)).get("FIELDVALUE"));
			}

			return TCResult.newSuccessResult();
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}

	}

	/**
	 * @category 公共字段容器转换
	 * @param transstrlist
	 *            入参|转换字段列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param predict
	 *            入参|转换前容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param lastdict
	 *            入参|转换后容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "transstrlist", comment = "转换字段列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "predict", comment = "转换前容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "lastdict", comment = "转换后容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "公共字段容器转换", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-10-23 10:11:06")
	public static TCResult B_DealPubTrans(JavaList transstrlist,
			JavaDict predict, JavaDict lastdict) {
		for (int i = 0; i < transstrlist.size(); i++) {
			if (predict.getStringItem(transstrlist.get(i)) == null
					|| "".equals(predict.getStringItem(transstrlist.get(i), ""))) {
				AppLogger.info("数据集无" + transstrlist.get(i) + "字段值");
			} else {
				lastdict.put(transstrlist.get(i),
						predict.getStringItem(transstrlist.get(i)));
			}
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 容器转为列模式
	 * @param liststr
	 *            入参|列模式key值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param listdict
	 *            入参|列容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param req
	 *            出参|返回容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "liststr", comment = "列模式key值", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "listdict", comment = "列容器", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "req", comment = "返回容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "容器转为列模式", style = "判断型", type = "同步组件", date = "2017-10-24 05:15:15")
	public static TCResult B_DictTransList(JavaList liststr, JavaList listdict) {
		JavaDict req = new JavaDict();
		for (int i = 0; i < liststr.size(); i++) {
			JavaList list = new JavaList();
			for (int j = 0; j < listdict.size(); j++) {
				list.add(((JavaDict) listdict.get(j)).get(liststr.get(i)));
			}
			req.put(liststr.get(i), list);
		}
		return TCResult.newSuccessResult(req);
	}

	/**
	 * @category 获取当前节点业务数据
	 * @param nodeid
	 *            入参|当前节点ID|{@link java.lang.String}
	 * @param busidict
	 *            入参|业务数据集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param newbusijson
	 *            出参|当前节点业务数据json串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "nodeid", comment = "当前节点ID", type = java.lang.String.class),
			@Param(name = "busidict", comment = "业务数据集", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = { @Param(name = "newbusijson", comment = "当前节点业务数据json串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取当前节点业务数据", style = "判断型", type = "同步组件", date = "2017-11-28 07:22:54")
	public static TCResult B_CheckCustBusiData(String nodeid, JavaDict busidict) {
		JavaDict newbusidict = new JavaDict();
		String sqlcmd = "select distinct fieldcode from T_ARB_FLOWFIELDINFO where nodeid = ?";
		JavaList values = new JavaList();
		values.add(nodeid);
		TCResult rt = P_Jdbc.preparedSelect(null, sqlcmd, values, 0);
		int status = rt.getStatus();
		if (status == 2) {
			AppLogger.debug("节点字段表无该节点配置字段");
		} else if (status == 1) {
			JavaList result = (JavaList) rt.getOutputParams().get(1);
			if (result.size() == 0) {
				AppLogger.debug("节点字段表无该节点配置字段");
			} else {
				for (int i = 0; i < result.size(); i++) {
					JavaList resl = result.getItem(i);
					AppLogger.debug("resl" + resl);
					String fieldcode = resl.getStringItem(0);
					if (busidict.get(fieldcode) == null
							|| "".equals(busidict.get(fieldcode))) {
						AppLogger.debug("节点字段" + fieldcode + "没有值");
					} else {
						newbusidict.put(fieldcode, busidict.get(fieldcode));
					}

				}
			}
		} else {
			if (rt.getErrorCode() != null || rt.getErrorMsg() != null) {
				return TCResult.newFailureResult(rt.getErrorCode(),
						"查询节点字段表异常:" + rt.getErrorMsg());
			}
		}

		// 将newbusidict转换为json串
		String jsonStr = "";
		if (newbusidict == null) {
			jsonStr = "{}";
		} else {
			jsonStr = JSON.toJSONString(newbusidict,
					SerializerFeature.WriteMapNullValue);
		}
		return TCResult.newSuccessResult(jsonStr);
	}

	/**
	 * @category 子交易返回值过滤
	 * @param tradereq
	 *            入参|子交易返回容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param nodeid
	 *            入参|节点|{@link java.lang.String}
	 * @param busistep
	 *            入参|步骤|{@link java.lang.String}
	 * @param rsreq
	 *            出参|过滤后的容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "tradereq", comment = "子交易返回容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "nodeid", comment = "节点", type = java.lang.String.class),
			@Param(name = "busistep", comment = "步骤", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rsreq", comment = "过滤后的容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "子交易返回值过滤", style = "判断型", type = "同步组件", date = "2017-12-08 06:26:32")
	public static TCResult B_GetTradeOutPutField(JavaDict tradereq,
			String nodeid, String busistep) {
		JavaDict rsreq=new JavaDict();
		String sqlcmd = "select fieldcode,fieldinit,fieldio,tradefieldcode from t_arb_flowfieldinfo where nodeid = ? and busistep = ? and fieldflag='1' and fieldio='2' order by fieldnum ";
		JavaList values = new JavaList();
		values.add(nodeid);
		values.add(busistep);
		TCResult rt = P_Jdbc.preparedSelect(null, sqlcmd, values, 0);
		int status = rt.getStatus();
		if (status == 2) {
			AppLogger.debug("无该结点相关配置字段，直接返回空json串");
		} else if (status == 1) {
			JavaList result = (JavaList) rt.getOutputParams().get(1);
			if (result.size() == 0) {
				AppLogger.debug("无该结点相关配置字段，直接返回空json串");
			} else {
				for (int i = 0; i < result.size(); i++) {
					JavaList resl = result.getItem(i);
					AppLogger.debug("resl" + resl);
					String tmpfieldcode = "", tradefieldcode = "";
					String tmpfieldinit = "";
					Object tmpvalue="";
					tmpfieldcode = resl.getStringItem(0);
					tmpfieldinit = resl.getStringItem(1);
					tradefieldcode = resl.getStringItem(3);
					
					
					if (tradefieldcode == null || "".equals(tradefieldcode)) {
						tmpvalue = tradereq.get(tmpfieldcode);
					} else {
						tmpvalue = tradereq.get(tradefieldcode);
					}
					if(tmpvalue == null || "".equals(tmpvalue)) {
						if (!"".equals(tmpfieldinit) && tmpvalue != null) {
							tmpvalue = tmpfieldinit;
						}
					}
					if(tmpvalue==null || "".equals(tmpvalue)){
						AppLogger.debug("无字段为空值");
					}else{
						rsreq.put(tmpfieldcode, tmpvalue);
					}
					
				}
			}
		}else {
			if (rt.getErrorCode() != null || rt.getErrorMsg() != null) {
				return TCResult.newFailureResult(rt.getErrorCode(),
						"查询节点配置表异常:" + rt.getErrorMsg());
			}
		}
		return TCResult.newSuccessResult(rsreq);
	}

}
