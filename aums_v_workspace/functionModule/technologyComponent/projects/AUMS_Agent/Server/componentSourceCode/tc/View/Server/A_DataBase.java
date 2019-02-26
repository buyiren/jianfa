package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import tc.View.Server.utils.*;
import tc.bank.constant.IErrorCode;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.jcomponent.ParamMemorizer;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * @date 2017-05-31 15:0:33
 */
@ComponentGroup(level = "应用", groupName = "数据操作（多连接池）", projectName = "AAAA", appName = "server")
public class A_DataBase {
	@SuppressWarnings("unchecked")
//	@InParams(param = {
//			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
//			@Param(name = "data_context", comment = "数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "busioper", comment = "操作关键字", type = java.lang.String.class),
//			@Param(name = "ext_info", comment = "扩展列表,现用来支持排序,分页,条件数据获取", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
//			@Param(name = "commitFlag", comment = "事务提交标识", type = java.lang.Object.class),
//			@Param(name = "map_context", comment = "业务类型信息容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
//			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
//	@OutParams(param = {
//			@Param(name = "datarow", comment = "数据信息字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "rownum", comment = "最后一次查询或影响的行数", type = int.class) })
//	@Returns(returns = { @Return(id = "0", desp = "失败"),
//			@Return(id = "2", desp = "查询类无记录或更新删除类影响到的记录数为0"),
//			@Return(id = "1", desp = "成功") })
//	@Component(label = "SQL组执行组件", style = "选择型", type = "同步组件", comment = "一个固定业务的某个业务动作所有涉及的SQL组的执行处理,SQL组是按照交易功能划分， 入参3用例：[[[\"productcode\"],[\"ASC\"]],[int(__REQ__[\"pageflag\"]),int(__REQ__[\"currpage\"]),int(__REQ__[\"pagenum\"])],[[[\"channelcode\", \"=\", __REQ__[\"prechannelcode\"], \"and\"],[\"channelserno\", \"=\", __REQ__[\"prechannelserno\"], \"and\"],[\"channeldate\", \"=\", __REQ__[\"prechanneldate\"]]]]]", author = "姚园-3958", date = "2016-01-04 09:50:17")
//	public static TCResult P_DB_ExecGrpSQL(String poolName,
//			JavaDict data_context, String busioper, JavaList ext_info,
//			Object commitFlag, JavaDict map_context, String ver, String area)
//			throws ClassNotFoundException, SQLException {
//
//		AppLogger.info("提交标志" + commitFlag.toString());
//		if (ver == null && area == null || ver.equals("V1.0.020130818")
//				&& area.equals("PRD")) {
//			Connection DBConn = null;
//			JavaDict outdata = new JavaDict();
//			TCResult ret = null;
//			TCResult result = null;
//			TCResult _MF_Ret_ = null;
//			boolean Memory_Suc_Flag = false;
//			DBConn = DBConnProvider.getConnection(poolName);
//
//			if (DBConn == null) {
//				return TCResult.newFailureResult(ErrorCode.CONN,
//						"SQL执行组件数据库连接异常，获取数据库连接失败");
//			}
//			JavaDict new_map_context = new JavaDict();
//			if (map_context == null) {
//				new_map_context.setItem("templatecode",
//						data_context.getItem("templatecode"));
//				new_map_context.setItem("transcode",
//						data_context.getItem("transcode"));
//				new_map_context
//						.setItem(
//								"busitype",
//								((data_context.getItem("busitype") != null) ? (data_context
//										.getItem("busitype"))
//										: AfaValueDef.__BUSITYPE__));
//
//			} else {
//				new_map_context.setItem("templatecode",
//						map_context.getItem("templatecode"));
//				new_map_context.setItem("transcode",
//						map_context.getItem("transcode"));
//				new_map_context
//						.setItem(
//								"busitype",
//								((map_context.getItem("busitype") != null) ? (map_context
//										.getItem("busitype"))
//										: AfaValueDef.__BUSITYPE__));
//
//			}
//			// 参数内存化模式
//			new_map_context.setItem("busioper", busioper);
//			new_map_context
//					.setItem(
//							"__MFFLG__",
//							((data_context.getItem("__MFFLG__") != null) ? (data_context
//									.getItem("__MFFLG__"))
//									: AfaValueDef.__MFFLAG__));
//			if ((new_map_context.getItem("__MFFLG__").equals("1"))) {
//
//				// 基础操作序列获取
//				String memory_key = "SQLMAP."
//						+ new_map_context.getItem("templatecode")
//						+ new_map_context.getItem("transcode")
//						+ new_map_context.getItem("busitype")
//						+ new_map_context.getItem("busioper");
//				AppLogger.info("内存参数:" + memory_key);
//				_MF_Ret_ = ParamMemorizer.getParam("FDATAPROCMAPSQL",
//						memory_key);
//				// 获取失败
//				if (_MF_Ret_.getStatus() == 2) {
//					// 获取通用业务类型key
//					if ((Integer) new_map_context.getItem("busitype") != 0) {
//						memory_key = "SQLMAP."
//								+ new_map_context.getItem("templatecode")
//								+ new_map_context.getItem("transcode")
//								+ AfaValueDef.__BUSITYPE__
//								+ new_map_context.getItem("busioper");
//						_MF_Ret_ = ParamMemorizer.getParam("FDATAPROCMAPSQL",
//								memory_key);
//						AppLogger.info("内存参数:" + memory_key);
//
//						// 获取通用业务类型key成功
//						if (_MF_Ret_.getStatus() == 1) {
//							Memory_Suc_Flag = true;
//						}
//					}
//				} else if (_MF_Ret_.getStatus() == 1)
//					Memory_Suc_Flag = true;
//			}
//			if (Memory_Suc_Flag) {
//				JavaList _result_ = new JavaList();
//				JavaList mem_result = new JavaList();
//				mem_result.addAll((Arrays.asList(((String) _MF_Ret_
//						.getOutputParams().get(0)).split("\\|:::\\|"))));
//				for (int i = 0; i < ((List<?>) mem_result).size(); i++) {
//					_result_.add(Arrays.asList(((String) mem_result.get(i))
//							.split("\\|::\\|")));
//				}
//				JavaList sub_ret = new JavaList(_result_.size(), "busidatakey",
//						_result_);
//				result = new TCResult(1, null, null, sub_ret);
//			} else {
//				JavaList temp_arr1 = new JavaList("operseqno");
//				JavaList temp_arr2 = new JavaList("ASC");
//				JavaList temp_arr3 = new JavaList(temp_arr1, temp_arr2);
//				JavaList temp_arr4 = new JavaList(temp_arr3, null, null);
//				result = PyDB.PyDBExecOneSQL(DBConn, new_map_context,
//						"getprocmapsql", temp_arr4, false);
//			}
//			if (result.getStatus() == 2) {
//				if (new_map_context.getItem("busitype").equals("0"))
//					return new TCResult(2, ErrorCode.AGR, "交易["
//							+ ((String) new_map_context.getItem("transcode"))
//							+ "],操作["
//							+ ((String) new_map_context.getItem("busioper"))
//							+ "]对应SQL未配置,请先配置");
//			} else if (result.getStatus() == 0) {
//				return TCResult.newFailureResult(result.getErrorCode(),
//						"获取交易对应的SQL信息失败," + result.getErrorMsg());
//			} else {
//				List<Object> item = null;
//				Iterator<Object> it = ((JavaList) result.getOutputParams().get(
//						2)).iterator();
//				while (it.hasNext()) {
//					item = (List<Object>) it.next();
//					ret = PyDB.PyDBExecOneSQL(DBConn, data_context, item.get(0)
//							.toString(), ext_info, false);
//					if (ret.getStatus() == 0) {
//						return TCResult.newFailureResult(ErrorCode.AGR,
//								ret.getErrorCode() + ret.getErrorMsg());
//					} else if (ret.getStatus() == 2) {
//						if (commitFlag.toString().equals("true")) {
//							DBConn.rollback();
//						}
//						return new TCResult(2, ErrorCode.AGR,
//								ret.getErrorCode() + ret.getErrorMsg());
//
//					} else {
//						if (ret.getOutputParams().size() > 1) {
//							JavaList values = new JavaList();
//							if (data_context.getItem("_nowpagenum_") != null) {
//								// 如果是分页，将totalrownum总比数，totalpagenum总页数，nowpagenum当前页数，pagerownum每页行数压入到outdata中
//								outdata.setItem("nowpagenum",
//										data_context.getItem("_nowpagenum_"));
//								data_context.removeItem("_nowpagenum_");
//								outdata.setItem("totalpagenum",
//										data_context.getItem("_totalpagenum_"));
//								data_context.removeItem("totalpagenum");
//								outdata.setItem("pagerownum",
//										data_context.getItem("_pagerownum_"));
//								data_context.removeItem("_pagerownum_");
//								outdata.setItem("totalrownum",
//										data_context.getItem("_totalrownum_"));
//								data_context.removeItem("_totalrownum_");
//							}
//							if ((Integer) ret.getOutputParams().get(0) > 1
//									&& ret.getOutputParams().get(1) != null) // 直接将行转为列模式
//
//							{
//								for (int i = 0; i < ((List<?>) ret
//										.getOutputParams().get(1)).size(); i++) {
//									outdata.put(((List<?>) ret
//											.getOutputParams().get(1)).get(i),
//											new JavaList());
//								}
//								Object row_line = null;
//								for (int j = 0; j < ((List<?>) ret
//										.getOutputParams().get(2)).size(); j++) {
//									row_line = ((List<?>) ret.getOutputParams()
//											.get(2)).get(j);
//									JavaList one_line = new JavaList();
//									for (int i = 0; i < ((List<?>) ret
//											.getOutputParams().get(1)).size(); i++) {
//										String value;
//										if (((List<?>) row_line).get(i) == null) {
//											value = "";
//										} else {
//											value = (String) ((List<?>) row_line)
//													.get(i);
//										}
//										if (value instanceof String) {
//											value = value.trim();
//										}
//										one_line.add(value);
//										JavaList l = (JavaList) outdata
//												.get(((List<?>) ret
//														.getOutputParams().get(
//																1)).get(i));
//										l.add(value);
//									}
//									values.add(one_line);
//								}
//								outdata.setItem("keys", ((List<?>) ret
//										.getOutputParams()).get(1));
//								outdata.setItem("values", values);
//							} else if ((Integer) ((JavaList) (ret
//									.getOutputParams())).getItem(0) == 1
//									&& ret.getOutputParams().get(1) != null) {
//								for (int i = 0; i < ((List<?>) ret
//										.getOutputParams().get(1)).size(); i++) {
//									if (((List<?>) ((List<?>) ret
//											.getOutputParams().get(2)).get(0))
//											.get(i) == null) {
//										outdata.put(((List<?>) ret
//												.getOutputParams().get(1))
//												.get(i), "");
//									} else {
//										outdata.put(
//												((List<?>) ret
//														.getOutputParams().get(
//																1)).get(i),
//												((List<?>) ((List<?>) ret
//														.getOutputParams().get(
//																2)).get(0))
//														.get(i));
//										if (outdata.get(((List<?>) ret
//												.getOutputParams().get(1))
//												.get(i)) instanceof String) {
//											outdata.put(((List<?>) ret
//													.getOutputParams().get(1))
//													.get(i), ((String) outdata
//													.get(((List<?>) ret
//															.getOutputParams()
//															.get(1)).get(i)))
//													.trim());
//										}
//									}
//									values.add(outdata.get(((List<?>) ret
//											.getOutputParams().get(1)).get(i)));
//								}
//								outdata.put("keys", ret.getOutputParams()
//										.get(1));
//								outdata.put("values", values);
//							} else if (ret.getOutputParams().get(1) == null) // +
//							{
//
//							} else
//								return TCResult.newFailureResult(ErrorCode.AGR,
//										"执行异常,多key表查询的条数非法,非1行");
//						} else {
//							// DBConn.commit(); //+
//							// return
//							// TCResult.newSuccessResult("操作成功",ret.getOutputParams().get(0));
//						}
//					}
//				}
//			}
//			if (commitFlag.toString().equals("true")) {
//				AppLogger.info("提交事务");
//				DBConn.commit();
//			}
//			return TCResult.newSuccessResult(outdata, ret.getOutputParams()
//					.get(0));
//		} else
//			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
//
//	}

	/**
	 * @category SQL格式化查询
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param busidatakey
	 *            入参|sql关键字|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param _Result_
	 *            出参|格式化sql|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         2 无满足条件记录<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "busidatakey", comment = "sql关键字", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "_Result_", comment = "格式化sql", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "2", desp = "无满足条件记录"), @Return(id = "1", desp = "成功") })
	@Component(label = "SQL格式化查询", style = "选择型", type = "同步组件", comment = "根据平台数据操作sql信息表afa_asst_dataprocsql中的busidatakey，获取组成完整sql语句要素信息", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_SQLSel(String poolName, String busidatakey,
			String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			if (!(busidatakey instanceof String)) {
				return TCResult.newFailureResult("FBC001", "SQL格式化查询入参非法,非字符串");
			}

			PreparedStatement ps = DBConn
					.prepareStatement(
							"select sqlstr, proctablename from afa_asst_dataprocsql where busidatakey = ?",
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, busidatakey);
			ResultSet rs = ps.executeQuery();
			if (rs == null) {
				return TCResult.newFailureResult("FBD001",
						"SQL格式化查询数据库异常,获取操作游标时返回空");
			}

			ResultSetMetaData rsmd = rs.getMetaData();
			JavaList result_column = new JavaList();
			JavaList result = new JavaList();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					result_column.add(rs.getString(i));
				}
				result.add(result_column);
				result_column = new JavaList();
			}
			rs.last();
			if (rs.getRow() == 0) {
				// return TCResult.newFailureResult("FBD001","无满足条件记录");
				return new TCResult(2, "FBD001", "无满足条件记录");
			} else if (rs.getRow() > 0) {
				return TCResult.newSuccessResult(result);
			} else {
				return TCResult.newFailureResult("FBD001", "查询数据库失败,记录数异常");
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	/**
	 * @category SQL语句执行
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param sqlstr
	 *            入参|执行的SQL语句|{@link java.lang.String}
	 * @param commitFlg
	 *            入参|事务提交标识|{@link Object}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 执行失败<br/>
	 *         2 影响笔数为0<br/>
	 *         1 执行成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "sqlstr", comment = "执行的SQL语句", type = java.lang.String.class),
			@Param(name = "commitFlg", comment = "事务提交标识", type = Object.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "执行失败"),
			@Return(id = "2", desp = "影响笔数为0"),
			@Return(id = "1", desp = "执行成功") })
	@Component(label = "SQL语句执行", style = "选择型", type = "同步组件", comment = "执行传入的sql语句,执行影响的条数放到返回结果的list[3][0]中,返回值有3个", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_ExecSQL(String poolName, String sqlstr,
			Object commitFlg, String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			AppLogger.info("要执行的sql语句为:" + sqlstr);
			AppLogger.info("事务标识为:" + commitFlg);
			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			Statement st = null;
			ResultSet rs = null;
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			st = DBConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			int __rowcount__ = 0;
			if (!(sqlstr instanceof String)) {
				return TCResult.newFailureResult("FBC001",
						"数据库sql执行组件SQL语句参数类型非法,不是字符串类型");
			}
			if (!(commitFlg instanceof Boolean)) {
				return TCResult.newFailureResult("FBC001",
						"数据库sql执行组件提交标识类型错误,不为bool");
			}
			if (sqlstr.toLowerCase().startsWith("select")) {
				rs = st.executeQuery(sqlstr);
				rs.last();
				__rowcount__ = rs.getRow();
				rs.close();
			} else {
				__rowcount__ = st.executeUpdate(sqlstr);
			}
			if ((Boolean) commitFlg) {
				DBConn.commit();
			}
			st.close();
			DBConn.close();
			AppLogger.info("影响到的笔数为:" + __rowcount__);
			if (__rowcount__ == 0) {
				// return TCResult.newFailureResult("FBD001","更新无满足条件记录");
				return new TCResult(2, "FBD001", "更新无满足条件记录");
			} else {
				return TCResult.newSuccessResult(__rowcount__);
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	/**
	 * @category 对象创建
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|DDL创建语句SQL|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "DDL创建语句SQL", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对象创建", style = "判断型", type = "同步组件", comment = "执行数据库对象DDL创建语句", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DDLCrt(String poolName, String sqlcmd,
			String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			Statement st = null;
			st = DBConn.createStatement();
			if (!(sqlcmd instanceof String)) {
				return TCResult.newFailureResult("FBC001",
						"数据库DDL创建时sql语句入参非法,非字符串");
			}
			st.execute(sqlcmd);
			st.close();
			DBConn.close();
			return TCResult.newSuccessResult();
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	/**
	 * @category 对象删除
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|DDL删除语句SQL|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "DDL删除语句SQL", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对象删除", style = "判断型", type = "同步组件", comment = "执行数据库DDL删除语句", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DDLDel(String poolName, String sqlcmd,
			String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			Statement st = null;
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			st = DBConn.createStatement();
			if (!(sqlcmd instanceof String)) {
				return TCResult.newFailureResult("FBC001",
						"数据库DDL删除时sql语句入参非法,非字符串");
			}
			st.execute(sqlcmd);
			st.close();
			DBConn.close();
			return TCResult.newSuccessResult();
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");

	}

	/**
	 * @category 对象更改
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|DDL修改语句SQL|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "DDL修改语句SQL", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对象更改", style = "判断型", type = "同步组件", comment = "执行数据库DDL修改语句", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DDLMod(String poolName, String sqlcmd,
			String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			Statement st = null;
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			st = DBConn.createStatement();
			if (!(sqlcmd instanceof String)) {
				return TCResult.newFailureResult("FBC001",
						"数据库DDL修改时sql语句入参非法,非字符串");
			}
			st.execute(sqlcmd);
			st.close();
			// DBConn.close();
			return TCResult.newSuccessResult();
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

//	/**
//	 * @category 序列号操作
//	 * @param poolName
//	 *            入参|连接池名称|{@link java.lang.String}
//	 * @param seqname
//	 *            入参|序号名称|{@link java.lang.String}
//	 * @param dbtype
//	 *            入参|数据库类型Oracle、DB2或Informix|{@link java.lang.String}
//	 * @param seqlen
//	 *            入参|序号长度|{@link int}
//	 * @param ver
//	 *            入参|版本日期|{@link java.lang.String}
//	 * @param area
//	 *            入参|适用区域|{@link java.lang.String}
//	 * @param outname
//	 *            出参|序号的当前值|{@link java.lang.String}
//	 * @return 0 执行失败<br/>
//	 *         1 执行成功<br/>
//	 * @throws SQLException
//	 */
//	@InParams(param = {
//			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
//			@Param(name = "seqname", comment = "序号名称", type = java.lang.String.class),
//			@Param(name = "dbtype", comment = "数据库类型Oracle、DB2或Informix", type = java.lang.String.class),
//			@Param(name = "seqlen", comment = "序号长度", type = int.class),
//			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
//			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
//	@OutParams(param = { @Param(name = "outname", comment = "序号的当前值", type = java.lang.String.class) })
//	@Returns(returns = { @Return(id = "0", desp = "执行失败"),
//			@Return(id = "1", desp = "执行成功") })
//	@Component(label = "序列号操作", style = "判断型", type = "同步组件", comment = "获取数据库中指定sequence的当前值,返回值保持在返回的list[3]中,现仅支持Oracle/DB2/Informix三种数据库,对于Informix,dual表是手工创建的", author = "姚园-3958", date = "2016-01-04 09:50:17")
//	public static TCResult P_DB_GetSeqNo(String poolName, String seqname,
//			String dbtype, int seqlen, String ver, String area)
//			throws SQLException {
//		if (ver == null && area == null || ver.equals("V1.0.020130818")
//				&& area.equals("PRD")) {
//
//			Connection conn = null;
//			conn = DBConnProvider.getConnection(poolName);
//			if (conn == null) {
//				return TCResult.newFailureResult(ErrorCode.CONN,
//						"SQL执行组件数据库连接异常，获取数据库连接失败");
//			}
//			Statement stmt = null;
//			ResultSet __result__ = null;
//			try {
//				AppLogger.info("进入P_DB_GetSeqNo,输入参数信息:");
//				AppLogger.info("序号名称:" + seqname);
//				AppLogger.info("数据库类型:" + dbtype);
//				AppLogger.info("序号长度:" + String.valueOf(seqlen));
//				if (!(seqname instanceof String)) {
//					return TCResult.newFailureResult("FBAC01",
//							"序号名称参数类型非法,不是字符串");
//				}
//				if (!(dbtype instanceof String)) {
//					return TCResult.newFailureResult("FBAC01",
//							"数据库类型参数类型非法,不是字符串");
//				}
//				if (!(seqname instanceof String) || seqname == null
//						|| seqname.equals("")) {
//					return TCResult.newFailureResult("", "序号名称:" + seqname
//							+ "不合法,不能为空或者null");
//				}
//				if (seqlen < 0) {
//					return TCResult
//							.newFailureResult("FBAC01", "序号长度参数不合法，不是正数");
//				}
//				String sqlcmd = "";
//				String dbname = conn.getMetaData().getDatabaseProductName();
//				dbtype = AfaValueDef.DB_DBTypeMap.getItem(dbname);
//
//				if (dbtype.toUpperCase().equals("DB2")) {
//					sqlcmd = "SELECT  nextval for " + seqname.trim()
//							+ " from sysibm.sysdummy1";
//				} else if (dbtype.toUpperCase().equals("MYSQL")) {
//					sqlcmd = "SELECT " + "next value for " + "MYCATSEQ_"
//							+ seqname.trim().toUpperCase();
//				} else if (dbtype.toUpperCase().equals("ORA")) {
//					sqlcmd = "SELECT " + seqname.trim() + ".NEXTVAL FROM DUAL";
//				} else if (dbtype.toUpperCase().equals("INF")) {
//					sqlcmd = "SELECT " + seqname.trim() + ".NEXTVAL FROM DUAL";
//				} else if (dbtype.toUpperCase().equals("GBASE")) {
//					sqlcmd = "SELECT " + seqname.trim()
//							+ ".NEXTVAL FROM SYSMASTER:SYSDUAL";
//				} else {
//					return TCResult.newFailureResult("FBAC01", "数据库类型"
//							+ seqname.trim()
//							+ "非法,仅支持Oracle、DB2、Informix和Gbase");
//				}
//				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//						ResultSet.CONCUR_READ_ONLY);
//				__result__ = stmt.executeQuery(sqlcmd);
//				if (__result__.first() != __result__.last()) {
//					return TCResult.newFailureResult("FBAD00",
//							"获取序号值信息异常,结果集不为1条");
//				}
//				String __tmp__serno = __result__.getString(1);
//				String __ret__ = "";
//				int len = __tmp__serno.length();
//				if (seqlen <= len) {
//					__ret__ = __tmp__serno;
//				} else {
//					StringBuffer buff = new StringBuffer();
//					for (int i = 1; i <= seqlen - len; i++) {
//						buff.append("0");
//					}
//					buff.append(__tmp__serno);
//					__ret__ = buff.toString();
//				}
//				stmt.close(); // +
//				AppLogger.info("获取到的序号值为:" + __ret__);
//				return TCResult.newSuccessResult(__ret__);
//			} catch (Exception e) {
//				if (__result__ != null) {
//					__result__.close();
//				}
//				if (stmt != null) {
//					stmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//
//				return TCResult.newFailureResult("FBD999", "获取序号值失败," + e);
//			}
//		} else
//			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
//	}

	/**
	 * @category 数据删除
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param tablename
	 *            入参|表名称|{@link java.lang.String}
	 * @param condition
	 *            入参|条件语句list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|{@link Object}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         2 删除影响的笔数为0<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "tablename", comment = "表名称", type = java.lang.String.class),
			@Param(name = "condition", comment = "条件语句list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识", type = Object.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "2", desp = "删除影响的笔数为0"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据删除", style = "选择型", type = "同步组件", comment = "删除指定条件的记录,条件语句[[列名,操作符,值,后边的条件关系/None], ...],每个列表体现出的条件都是一个()单元", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DMLDel(String poolName, String tablename,
			JavaList condition, Object commitFlg, String ver, String area)
			throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			Statement st = null;
			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			if (tablename == null || tablename.equals("")) {
				return TCResult.newFailureResult("FBC001", "数据表不能为null或者空");
			}
			if (condition.equals("")) {
				return TCResult.newFailureResult("FBC001", "条件信息表不能为空");
			}
			if (commitFlg == null) {
				return TCResult.newFailureResult("FBC001", "提交标识类型表不能为null或者空");
			}
			String _Sql_ = "delete from " + tablename;
			String _con_ = "";
			String constr = "";
			if (condition == null || condition.size() == 0) {
				_con_ = "";
			} else {
				_con_ = " where ( ";
				int conlen = 0;
				for (int i = 0; i < condition.size(); i++) {
					conlen = ((JavaList) condition.get(i)).size();
					if (!(condition.get(i) instanceof JavaList)
							|| ((JavaList) condition.get(i)).size() < 3) {
						return TCResult.newFailureResult("FBC001",
								String.valueOf(i) + "条件类型错误");
					} else {
						if (((JavaList) condition.get(i)).get(2) == null
								|| ((JavaList) condition.get(i)).get(2) instanceof String) {
							if (((JavaList) condition.get(i)).get(1).equals(
									"==")) {
								constr = "("
										+ String.valueOf(((JavaList) condition
												.get(i)).get(0)
												+ "="
												+ String.valueOf(((JavaList) condition
														.get(i)).get(2)) + ")");
							} else {
								constr = "("
										+ String.valueOf(((JavaList) condition
												.get(i)).get(0))
										+ " "
										+ String.valueOf(((JavaList) condition
												.get(i)).get(1)) + " " + "'"
										+ ((JavaList) condition.get(i)).get(2)
										+ "')";
							}
						} else {
							constr = "("
									+ String.valueOf(((JavaList) condition
											.get(i)).get(0))
									+ " "
									+ String.valueOf(((JavaList) condition
											.get(i)).get(1))
									+ " "
									+ String.valueOf(((JavaList) condition
											.get(i)).get(2)) + ")";
						}
					}
					AppLogger.info("conlen:" + conlen);
					if (conlen == 4) {
						if (((JavaList) condition.get(i)).get(3) != null) {
							constr = constr + " "
									+ ((JavaList) condition.get(i)).get(3)
									+ " ";
						}
					}
					_con_ = _con_ + constr;
				}
				_con_ = _con_ + ")";
			}
			_Sql_ = _Sql_ + _con_;
			AppLogger.info(_Sql_);
			st = DBConn.createStatement();
			int __rowcount__ = st.executeUpdate(_Sql_);
			if ((Boolean) commitFlg) {
				DBConn.commit();
			}
			if (__rowcount__ == 0) {
				st.close(); // +
				return new TCResult(2, "FBD001", "删除时未找到记录");
			} else {
				st.close(); // +
				return TCResult.newSuccessResult();
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	/**
	 * @category 数据库回滚
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据库回滚", style = "判断型", type = "同步组件", comment = "回滚数据库事务,0-回滚失败,1-回滚成功", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_RollBack(String poolName, String ver,
			String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			DBConn.rollback();
			return TCResult.newSuccessResult();
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	/**
	 * @category 数据库提交
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据库提交", style = "判断型", type = "同步组件", comment = "提交数据库的事务,0-系统异常,1-提交成功", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_Commit(String poolName, String ver, String area)
			throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {
			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			DBConn.commit();
			return TCResult.newSuccessResult();
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	/**
	 * @category 数据插入
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param tablename
	 *            入参|库表名称|{@link java.lang.String}
	 * @param values
	 *            入参|新增列值key,value的list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|{@link Object}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         2 新增影响的笔数为0<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "tablename", comment = "库表名称", type = java.lang.String.class),
			@Param(name = "values", comment = "新增列值key,value的list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识", type = Object.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "2", desp = "新增影响的笔数为0"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据插入", style = "选择型", type = "同步组件", comment = "登记数据到数据库中,值列表参数格式为[\"列名\",值]列表", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DMLIns(String poolName, String tablename,
			JavaList values, Object commitFlg, String ver, String area)
			throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			AppLogger.info("P_DB_DMLIns函数输入信息:");
			AppLogger.info("库表名称:" + tablename);
			AppLogger.info("列值key,value列表值:" + values);
			AppLogger.info("提交标识:" + commitFlg);
			Connection conn = null;
			conn = DBConnProvider.getConnection(poolName);
			if (!(tablename instanceof String)) {
				return TCResult.newFailureResult("FBC001", "表名称参数类型错误,不为字符串");
			}
			if (!(values instanceof JavaList)) {
				return TCResult.newFailureResult("FBC001", "列信息类型错误,不为list");
			}
			JavaList cols = new JavaList();
			JavaList vals = new JavaList();
			JavaList qmark = new JavaList();
			int i = 1;
			for (Object tc : values) {
				if (!(tc instanceof JavaList)) {
					return TCResult.newFailureResult("FBC001",
							"列值列表中的每个单元类型非法,不是列表");
				}
				if (((JavaList) tc).size() != 2) {
					return TCResult.newFailureResult("FBC001",
							"列值列表中的每个单元列表值非法,里面要素个数不为2");
				}
				cols.add(((JavaList) tc).get(0));
				if (((JavaList) tc).get(1) == null) {
					vals.add("");
					qmark.add(":" + i);
				} else {
					if (((JavaList) tc).get(1) instanceof String) {
						qmark.add(":" + i);
						vals.add(((JavaList) tc).get(1));
					} else {
						vals.add(((JavaList) tc).get(1));
					}
				}
				i = i + 1;
			}
			StringBuffer bf = new StringBuffer();
			for (int j = 0; j < cols.size(); j++) {
				bf.append(cols.get(j));
				if (j != cols.size() - 1) {
					bf.append(",");
				}
			}
			String cols_string = bf.toString();
			StringBuffer buf = new StringBuffer();
			for (int j = 0; j < qmark.size(); j++) {
				buf.append(qmark.get(j));
				if (j != qmark.size() - 1) {
					buf.append(",");
				}
			}
			String qmark_string = buf.toString();
			String __sql__ = "insert into " + tablename + " ( " + cols_string
					+ " ) values(" + qmark_string + ")";
			AppLogger.info("insert 的语句为:" + __sql__);
			AppLogger.info("传入参数为:" + vals);
			if (conn == null) {
				return TCResult.newFailureResult("FBD001", "新增记录时数据库连接变量非法,为空");
			}
			// Statement st=conn.createStatement();
			int __rowcount__ = 0;
			if (__sql__.toLowerCase().startsWith("select")) {
				Statement st = conn.createStatement();
				__rowcount__ = st.executeQuery(__sql__).getRow();
				st.close();
			} else {
				PreparedStatement pst = conn.prepareStatement(__sql__);
				for (int k = 0; k < vals.size(); k++) {
					pst.setString(k + 1, vals.getStringItem(k));
				}
				__rowcount__ = pst.executeUpdate();
				pst.close();
			}
			if ((Boolean) commitFlg) {
				conn.commit();
			}
			// st.close();
			if (__rowcount__ == 0) {
				// return TCResult.newFailureResult("FBD001","新增记录时插入影响的笔数为0");
				return new TCResult(2, "FBD001", "新增记录时插入影响的笔数为0");
			} else {
				return TCResult.newSuccessResult();
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

//	/**
//	 * @category 数据新增更新删除类统一接口
//	 * @param data_context
//	 *            入参|数据容器|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param busioper
//	 *            入参|操作关键字|{@link java.lang.String}
//	 * @param ext_context
//	 *            入参|扩展参数字典|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param commitFlg
//	 *            入参|事务提交标识|{@link Object}
//	 * @param map_context
//	 *            入参|业务类型信息容器|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param ver
//	 *            入参|版本日期|{@link java.lang.String}
//	 * @param area
//	 *            入参|适用区域|{@link java.lang.String}
//	 * @param rownum
//	 *            出参|最后一次数据操作影响的条数|{@link int}
//	 * @return 0 失败<br/>
//	 *         2 影响的记录数为零<br/>
//	 *         1 成功<br/>
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 */
//	@InParams(param = {
//			@Param(name = "data_context", comment = "数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "busioper", comment = "操作关键字", type = java.lang.String.class),
//			@Param(name = "ext_context", comment = "扩展参数字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "commitFlg", comment = "事务提交标识", type = Object.class),
//			@Param(name = "map_context", comment = "业务类型信息容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
//			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
//	@OutParams(param = { @Param(name = "rownum", comment = "最后一次数据操作影响的条数", type = int.class) })
//	@Returns(returns = { @Return(id = "0", desp = "失败"),
//			@Return(id = "2", desp = "影响的记录数为零"),
//			@Return(id = "1", desp = "成功") })
//	@Component(label = "数据新增更新删除类统一接口", style = "选择型", type = "同步组件", comment = "非查询类业务数据操作的入口函数,所有对数据库的非查询类操纵均需通过本接口进行操作", author = "姚园-3958", date = "2016-01-04 09:50:17")
//	public static TCResult P_DBUnityAltOpr(JavaDict data_context,
//			String busioper, JavaDict ext_context, Object commitFlg,
//			JavaDict map_context, String ver, String area)
//			throws ClassNotFoundException, SQLException {
//		if (ver == null && area == null || ver.equals("V1.0.020130818")
//				&& area.equals("PRD")) {
//
//			if (!(data_context instanceof JavaDict)) {
//				return TCResult.newFailureResult("FBCC01", "入参数据容器非字典类型");
//			}
//			if (busioper == null) {
//				return TCResult.newFailureResult("FBCC01", "操作关键字不可为空");
//			}
//			if (!(busioper instanceof String)) {
//				return TCResult.newFailureResult("FBCC01", "操作关键字非字符串类型");
//			}
//			if (ext_context != null && !(ext_context instanceof JavaDict)) {
//				return TCResult.newFailureResult("FBCC01", "入参扩展参数字典类型非字典类型");
//			}
//			if (!(commitFlg instanceof Boolean)) {
//				return TCResult.newFailureResult("FBCC01", "入参事务标识非bool类型");
//			}
//			if (map_context != null && !(map_context instanceof JavaDict)) {
//				return TCResult.newFailureResult("FBCC01", "入参业务类型信息容器非dict类型");
//			}
//			JavaDict chk_context = new JavaDict();
//			if (map_context == null) {
//				chk_context = data_context;
//			} else {
//				chk_context = map_context;
//			}
//			JavaList chkvar = new JavaList();
//			TCResult result = null;
//			chkvar.add("templatecode");
//			chkvar.add("transcode");
//			for (Object var_item : chkvar) {
//				if (!chk_context.containsKey(var_item)) {
//					return TCResult.newFailureResult("FBCC01", "数据字典中缺少必输的键值["
//							+ var_item + "]");
//				} else {
//					if (!(chk_context.get(var_item) instanceof String)) {
//						return TCResult.newFailureResult("FBCC01", "键值["
//								+ var_item + "]类型非法,非字符串");
//					}
//				}
//			}
//			data_context.put("busioper", busioper);
//			Object dyncondlist;
//			if (ext_context != null) {
//				if (ext_context.get("dyncondlist") == null) {
//					dyncondlist = null;
//				} else {
//					dyncondlist = ext_context.get("dyncondlist");
//				}
//			} else {
//				dyncondlist = null;
//			}
//			/*
//			 * JavaList l=new JavaList(); l.add(null); l.add(null);
//			 * l.add(dyncondlist);
//			 */
//			result = PyDB.PyDBExecGrpSQL(data_context, new JavaList(null, null,
//					dyncondlist), commitFlg, map_context);
//			if (result.getStatus() == 0) {
//				return TCResult.newFailureResult(result.getErrorCode(),
//						result.getErrorMsg());
//			} else if (result.getStatus() == 2) {
//				return new TCResult(2, "FBCD01", "影响的记录数为零");
//			} else {
//				return TCResult.newSuccessResult(result.getOutputParams()
//						.get(1));
//			}
//		} else
//			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
//	}

	/**
	 * @category 数据更新
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param tablename
	 *            入参|表名称|{@link java.lang.String}
	 * @param colinfos
	 *            入参|列信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param condition
	 *            入参|条件信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|{@link Object}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param updrows
	 *            出参|影响到的笔数|{@link int}
	 * @return 0 失败,数据库异常<br/>
	 *         2 更新影响的笔数为0<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "tablename", comment = "表名称", type = java.lang.String.class),
			@Param(name = "colinfos", comment = "列信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "condition", comment = "条件信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识", type = Object.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "updrows", comment = "影响到的笔数", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "2", desp = "更新影响的笔数为0"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据更新", style = "选择型", type = "同步组件", comment = "更新指定条件指定库表的指定列值,列信息为[[\"列名\",值]]列表,条件语句:[[列名,操作符,值后边的条件关系/None], ...],更新影响的笔数放到list[3]中", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DMLUpd(String poolName, String tablename,
			JavaList colinfos, JavaList condition, Object commitFlg,
			String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {
			Connection DBConn = null;
			Statement st = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"SQL执行组件数据库连接异常，获取数据库连接失败");
			}
			st = DBConn.createStatement();
			if (!(tablename instanceof String)) {
				return TCResult.newFailureResult("FBC001", "表名称参数类型错误,不为字符串");
			}
			if (!(colinfos instanceof JavaList)) {
				return TCResult.newFailureResult("FBC001", "列信息类型错误,不为list");
			}
			if (!(condition instanceof JavaList)) {
				return TCResult.newFailureResult("FBC001", "条件信息类型错误,不为list");
			}
			String colstr = "";
			int colnum = colinfos.size() - 1;
			JavaList tc = new JavaList();
			for (int i = 0; i < colinfos.size(); i++) {
				tc = (JavaList) colinfos.get(i);
				if (tc.get(1) instanceof String) {
					colstr = colstr + " " + tc.get(0) + "='" + tc.get(1) + "'";
				} else {
					colstr = colstr + " " + tc.get(0) + "=" + tc.get(1);
				}
				if (i != colnum) {
					colstr = colstr + ",";
				}
			}
			String _Sql_ = "update " + tablename + " set " + colstr + " ";
			String _con_ = "";
			if (condition == null || condition.size() == 0) {
			} else {
				_con_ = "where ( ";
				JavaList cond = new JavaList();
				int conlen = 0;
				String constr = "";
				for (int ii = 0; ii < condition.size(); ii++) {
					cond = (JavaList) condition.get(ii);
					conlen = cond.size();
					if (!(cond instanceof JavaList) || cond.size() < 3) {
						return TCResult.newFailureResult("A017053", ii
								+ "条件类型错误");
					} else {
						if (cond.get(2) == null
								|| cond.get(2) instanceof String) {
							if (cond.get(1).equals("==")) {
								constr = "(" + cond.getStringItem(0) + "="
										+ cond.getStringItem(2) + ")";
							} else {
								constr = "(" + cond.getStringItem(0) + " "
										+ cond.getStringItem(1) + " " + "'"
										+ cond.getStringItem(2) + "')";
							}
						} else {
							constr = "(" + cond.getStringItem(0) + " "
									+ cond.getStringItem(1) + " "
									+ cond.getStringItem(2) + ")";
						}
					}
					if (conlen == 4 && cond.get(3) != null) {
						constr = constr + " " + cond.get(3) + " ";
					}
					_con_ = _con_ + constr;
				}
				_con_ = _con_ + ")";
			}
			_Sql_ = _Sql_ + _con_;
			AppLogger.info("_Sql_语句:" + _Sql_);
			int __rownum__ = st.executeUpdate(_Sql_);
			if ((Boolean) commitFlg) {
				DBConn.commit();
			}
			st.close();
			if (__rownum__ == 0) {
				// return TCResult.newFailureResult("FBD001","更新无满足条件记录");
				return new TCResult(2, "FBD001", "更新无满足条件记录");
			} else {
				return TCResult.newSuccessResult(__rownum__);
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");

	}

	/**
	 * @category 数据查询
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param selsqlstr
	 *            入参|查询sql语句|{@link java.lang.String}
	 * @param rownum
	 *            入参|需要获取的数据笔数|{@link int}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param num
	 *            出参|查询到的行数|{@link int}
	 * @param result
	 *            出参|所有的行数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败,数据库异常<br/>
	 *         2 无满足条件记录<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "selsqlstr", comment = "查询sql语句", type = java.lang.String.class),
			@Param(name = "rownum", comment = "需要获取的数据笔数", type = int.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "num", comment = "查询到的行数", type = int.class),
			@Param(name = "result", comment = "所有的行数据", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "2", desp = "无满足条件记录"), @Return(id = "1", desp = "成功") })
	@Component(label = "数据查询", style = "选择型", type = "同步组件", comment = "根据sql查询语句获取查询的数据,默认是取所有,可以指定需要获取的行数,list[3]中存放查询到的笔数和数据,格式为:[笔数,所有行的list],获取条数为-1默认为获取所有条数", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DMLSel(String poolName, String selsqlstr,
			int rownum, String ver, String area) throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {
			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);

			Statement st = null;
			ResultSet rs = null;
			if (!(selsqlstr instanceof String)) {
				return TCResult.newFailureResult("FBC001", "sql语句参数类型非法,不是字符串");
			}
			if (DBConn == null) {
				return TCResult.newFailureResult("FBC001", "数据库连接对象为空,无数据库连接");
			}
			AppLogger.info(selsqlstr);
			st = DBConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(selsqlstr);
			ResultSetMetaData rsmd = rs.getMetaData();
			JavaList result_column = new JavaList();
			JavaList result = new JavaList();
			int rownumber = 0;
			if (rownum == 0) {
				while (rs.next()) {
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						result_column.add(rs.getString(i));
					}
					result.add(result_column);
					result_column = new JavaList();
					rownumber++;
				}
				rs.last();
			} else {
				int j = 0;
				while (j++ < rownum && rs.next()) {
					// rs.next();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						result_column.add(rs.getString(i));
					}

					result.add(result_column);
					result_column = new JavaList();
					rownumber++;
				}
			}
			st.close(); // +
			if (rs == null || rownumber == 0) {
				return new TCResult(2, "FBD001", "无满足条件记录");
			} else if (rownumber > 0) {
				// return TCResult.newSuccessResult(rs.getRow(),result);
				return TCResult.newSuccessResult(rownumber, result);
			} else {
				return TCResult.newFailureResult("FBD001", "查询数据库失败,记录数异常");
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");

	}

	/**
	 * @category 数据查询
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param selsqlstr
	 *            入参|查询sql语句|{@link java.lang.String}
	 * @param rownum
	 *            入参|需要获取的数据笔数|{@link int}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param num
	 *            出参|查询到的行数|{@link int}
	 * @param result
	 *            出参|所有的行数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败,数据库异常<br/>
	 *         2 无满足条件记录<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "selsqlstr", comment = "查询sql语句", type = java.lang.String.class),
			@Param(name = "rownum", comment = "需要获取的数据笔数", type = int.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "num", comment = "查询到的行数", type = int.class),
			@Param(name = "result", comment = "所有的行数据", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "2", desp = "无满足条件记录"), @Return(id = "1", desp = "成功") })
	@Component(label = "数据查询", style = "选择型", type = "同步组件", comment = "根据sql查询语句获取查询的数据,默认是取所有,可以指定需要获取的行数,list[3]中存放查询到的笔数和数据,格式为:[笔数,所有行的list],获取条数为-1默认为获取所有条数", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_DMLSel(String poolName, String selsqlstr,
			int rownum, JavaList parlist, String ver, String area)
			throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {
			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);

			if (!(selsqlstr instanceof String)) {
				return TCResult.newFailureResult("FBC001", "sql语句参数类型非法,不是字符串");
			}
			if (DBConn == null) {
				return TCResult.newFailureResult("FBC001", "数据库连接对象为空,无数据库连接");
			}
			AppLogger.info("sql:" + selsqlstr);
			AppLogger.info("参数:" + parlist);
			PreparedStatement ps = DBConn.prepareStatement(selsqlstr,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int idx = 1; idx <= parlist.size(); idx++) {
				ps.setString(idx, (String) parlist.getItem(idx - 1));

			}

			ResultSet rs = ps.executeQuery();
			if (rs == null) {
				return TCResult.newFailureResult("FBD001",
						"SQL格式化查询数据库异常,获取操作游标时返回空");
			}

			ResultSetMetaData rsmd = rs.getMetaData();
			JavaList result_column = new JavaList();
			JavaList result = new JavaList();
			int rownumber = 0;
			if (rownum == 0) {
				while (rs.next()) {
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						result_column.add(rs.getString(i));
					}
					result.add(result_column);
					result_column = new JavaList();
					rownumber++;
				}
				rs.last();
			} else {
				int j = 0;
				while (j++ < rownum && rs.next()) {
					// rs.next();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						result_column.add(rs.getString(i));
					}

					result.add(result_column);
					result_column = new JavaList();
					rownumber++;
				}
			}
			ps.close(); // +
			if (rs == null || rownumber == 0) {

				return new TCResult(2, "FBD001", "无满足条件记录");
			} else if (rownumber > 0) {
				// return TCResult.newSuccessResult(rs.getRow(),result);

				return TCResult.newSuccessResult(rownumber, result);
			} else {
				return TCResult.newFailureResult("FBD001", "查询数据库失败,记录数异常");
			}
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");

	}

//	/**
//	 * @category 数据查询类统一接口
//	 * @param cond_data_context
//	 *            入参|条件数据容器|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param busioper
//	 *            入参|业务数据操作关键字|{@link java.lang.String}
//	 * @param ext_context
//	 *            入参|扩展参数字典|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param map_context
//	 *            入参|数据操作映射数据容器|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param ver
//	 *            入参|版本日期|{@link java.lang.String}
//	 * @param area
//	 *            入参|适用区域|{@link java.lang.String}
//	 * @param out_context
//	 *            出参|获取到的数据字典|
//	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
//	 * @param rownum
//	 *            出参|查询到的数据条数|{@link int}
//	 * @return 0 失败<br/>
//	 *         2 查询无记录<br/>
//	 *         1 成功<br/>
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 */
//	@InParams(param = {
//			@Param(name = "cond_data_context", comment = "条件数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "busioper", comment = "业务数据操作关键字", type = java.lang.String.class),
//			@Param(name = "ext_context", comment = "扩展参数字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "map_context", comment = "数据操作映射数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
//			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
//	@OutParams(param = {
//			@Param(name = "out_context", comment = "获取到的数据字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
//			@Param(name = "rownum", comment = "查询到的数据条数", type = int.class) })
//	@Returns(returns = { @Return(id = "0", desp = "失败"),
//			@Return(id = "2", desp = "查询无记录"), @Return(id = "1", desp = "成功") })
//	@Component(label = "数据查询类统一接口", style = "选择型", type = "同步组件", comment = "对数据库查询类操作接口", author = "姚园-3958", date = "2016-01-04 09:50:17")
//	public static TCResult P_DBUnityRptOpr(JavaDict cond_data_context,
//			String busioper, JavaDict ext_context, JavaDict map_context,
//			String ver, String area) throws ClassNotFoundException,
//			SQLException {
//		if (ver == null && area == null || ver.equals("V1.0.020130818")
//				&& area.equals("PRD")) {
//
//			if (!(cond_data_context instanceof JavaDict)) {
//				return TCResult.newFailureResult("FBCC01", "入参条件数据容器非字典类型");
//			}
//			if (!(busioper instanceof String)) {
//				return TCResult.newFailureResult("FBCC01", "入参条件业务关键字非字符串类型");
//			}
//			if (ext_context != null && !(ext_context instanceof JavaDict)) {
//				return TCResult.newFailureResult("FBCC01", "入参扩展参数字典类型非字典类型");
//			}
//			if (map_context != null && !(map_context instanceof JavaDict)) {
//				return TCResult.newFailureResult("FBCC01", "入参业务类型信息容器非dict类型");
//			}
//			JavaDict chk_context = new JavaDict();
//			if (map_context == null) {
//				chk_context = cond_data_context;
//			} else {
//				chk_context = map_context;
//			}
//			JavaList chkvar = new JavaList("templatecode", "transcode");
//			for (Object var_item : chkvar) {
//				if (!chk_context.containsKey(var_item)) {
//					return TCResult.newFailureResult("FBCC01", "数据容器中缺少必输的键值["
//							+ var_item + "]");
//				} else {
//					if (!(chk_context.get(var_item) instanceof String)) {
//						return TCResult.newFailureResult("FBCC01", "键值["
//								+ var_item + "]类型非法,非字符串");
//					}
//				}
//			}
//			cond_data_context.put("busioper", busioper);
//			JavaList orderlist = new JavaList();
//			JavaList pagelist = new JavaList();
//			JavaList dyncondlist = new JavaList();
//			if (ext_context != null) {
//				if (ext_context.getListItem("orderlist") != null) {
//					orderlist = ext_context.getListItem("orderlist");
//				} else {
//					orderlist = null;
//				}
//				if (ext_context.getListItem("pagelist") != null) {
//					pagelist = ext_context.getListItem("pagelist");
//				} else {
//					pagelist = null;
//				}
//				if (ext_context.getListItem("dyncondlist") != null) {
//					dyncondlist = ext_context.getListItem("dyncondlist");
//				} else {
//					dyncondlist = null;
//				}
//			} else {
//				// ext_context = new JavaDict();
//				orderlist = null;
//				pagelist = null;
//				dyncondlist = null;
//			}
//			JavaList l = new JavaList(orderlist, pagelist, dyncondlist);
//			TCResult result = PyDB.PyDBExecGrpSQL(cond_data_context, l, false,
//					map_context);
//			if (result.getStatus() == 0) {
//				return TCResult.newFailureResult(result.getErrorCode(),
//						result.getErrorMsg());
//			} else if (result.getStatus() == 2) {
//				return new TCResult(2, "FBCD01", "无满足条件记录");
//			} else {
//
//				// if(result.getOutputParams().size()>1)
//				// {
//				return TCResult.newSuccessResult(result.getOutputParams()
//						.get(0), result.getOutputParams().get(1));
//				// }
//				// else
//
//				// return TCResult.newSuccessResult("无满足条件记录",0);
//
//			}
//		} else
//			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
//
//	}

	/**
	 * @category 条件列表构建
	 * @param data_context
	 *            入参|数据容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param condkeylist
	 *            入参|条件字段列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param opertaglist
	 *            入参|操作符列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param condlist
	 *            出参|条件列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "data_context", comment = "数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "condkeylist", comment = "条件字段列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "opertaglist", comment = "操作符列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "condlist", comment = "条件列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "条件列表构建", style = "判断型", type = "同步组件", comment = "针对条件不确定性查询所做的where条件拼接，目前仅支持and连接，", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DBBuildCondlist(JavaDict data_context,
			JavaList condkeylist, JavaList opertaglist, String ver, String area) {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			if (!(data_context instanceof JavaDict)) {
				return TCResult.newFailureResult("FBCC01", "入参数据容器非dict类型");
			}
			if (!(condkeylist instanceof JavaList)) {
				return TCResult.newFailureResult("FBC001", "条件字段列表非list类型");
			}
			if (!(opertaglist instanceof JavaList)) {
				return TCResult.newFailureResult("FBC001", "操作符列表非list类型");
			}
			if (condkeylist.size() != opertaglist.size()) {
				return TCResult.newFailureResult("FBCC01",
						"操作符列表长度与条件字段列表长度不相等");
			}
			JavaList tmpvaluelist = new JavaList();
			JavaList tmpkeylist = new JavaList();
			JavaList tmptaglist = new JavaList();
			JavaList condlist = new JavaList();
			for (int i = 0; i < condkeylist.size(); i++) {
				if (data_context.containsKey(condkeylist.get(i))
						&& ((String) data_context.get(condkeylist.get(i)))
								.length() > 0) {
					tmpkeylist.add(condkeylist.get(i));
					tmptaglist.add(opertaglist.get(i));
					tmpvaluelist.add(data_context.get(condkeylist.get(i)));
				}
			}
			JavaList tmpwherelist = new JavaList();
			JavaList tmpwhereitemlist = null;
			for (int j = 0; j < tmpkeylist.size(); j++) {
				tmpwhereitemlist = new JavaList(tmpkeylist.get(j),
						tmptaglist.get(j));
				if (tmptaglist.get(j).equals("like")) {
					tmpwhereitemlist.add("%" + tmpvaluelist.get(j) + "%");
				} else {
					tmpwhereitemlist.add(tmpvaluelist.get(j));
				}
				if (j != tmpkeylist.size() - 1) {
					tmpwhereitemlist.add("and");
				}
				tmpwherelist.add(tmpwhereitemlist);
			}
			condlist = tmpwherelist;
			if (condlist.size() == 0) {
				condlist = new JavaList(new JavaList("1", "=", "1"));
			}
			// AppLogger.info(condlist.toString());
			return TCResult.newSuccessResult(condlist);
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");

	}

	/**
	 * @category 调用存储过程
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param procname
	 *            入参|存储过程名称|{@link java.lang.String}
	 * @param paras_list
	 *            入参|输入参数列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param paras_dict
	 *            入参|输入参数字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param result
	 *            出参|结果信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "procname", comment = "存储过程名称", type = java.lang.String.class),
			@Param(name = "paras_list", comment = "输入参数列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "paras_dict", comment = "输入参数字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "结果信息", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "调用存储过程", style = "判断型", type = "同步组件", comment = "调用存储过程", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_DB_CallProc(String poolName, String procname,
			JavaList paras_list, JavaDict paras_dict, String ver, String area)
			throws SQLException {
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {
			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult("FBD001",
						"获取数据库操作游标时数据库连接对象为空,无数据库连接");
			}
			CallableStatement cs = DBConn.prepareCall("{call procname}");
			cs.setObject("pa", paras_list);
			cs.execute();
			return TCResult.newSuccessResult(cs.getObject("pa"));
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");

	}

	/**
	 * @category 数据分片节点查询
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param rwtype
	 *            入参|分片算法|{@link java.lang.String}
	 * @param tablename
	 *            入参|列值|{@link java.lang.String}
	 * @param columnvalue
	 *            入参|算法参数|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param connpoolname
	 *            出参|连接池名称|{@link java.lang.String}
	 * @return 0 失败,数据库异常<br/>
	 *         1 成功<br/>
	 * @throws SQLException
	 */

	@InParams(param = {// 入参增加读写标识
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "rwtype", comment = "读写标识", type = java.lang.String.class),
			@Param(name = "tablename", comment = "表名", type = java.lang.String.class),
			@Param(name = "columnvalue", comment = "列值", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			// @Param(name = "tablename", comment ="表名", type =
			// java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "connpoolname", comment = "连接池名称", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败,数据库异常"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据分片", style = "判断性", type = "同步组件", comment = "根据分片算法和分片列值，返回sql执行应该指向的数据库节点", author = "刘棽-05796", date = "2017-01-16 09:50:17")
	public static TCResult P_DB_Partition(String poolName, String rwtype,
			String tablename, JavaList columnvalue, String ver, String area)
			throws SQLException {
		// String rwtype;//读写标识
		// String tablename = new String();//表名
		// JavaList columnvalue = new JavaList();//列值
		if (ver == null && area == null || ver.equals("V1.0.020130818")
				&& area.equals("PRD")) {

			// String partitioncolumn = new String();
			JavaList algorithmparameter = new JavaList();// 参数列表
			JavaList connpoolname = new JavaList();

			Connection DBConn = null;
			DBConn = DBConnProvider.getConnection(poolName);
			if (DBConn == null) {
				return TCResult.newFailureResult(ErrorCode.CONN,
						"数据库连接异常，获取数据库连接失败");
			}
			AppLogger.info("获取数据库连接成功");

			Statement stmt = DBConn.createStatement();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String CurrentDate = df.format(new java.util.Date());
			String sqlStr = "select partitioncolumn,algorithm,algorithmparameter from afa_ddbm_tablerule where tablename='"
					+ tablename
					+ "' and startdate<='"
					+ CurrentDate
					+ "' and enddate>='" + CurrentDate + "'";
			AppLogger.info("sql:" + sqlStr);

			ResultSet rs1 = stmt.executeQuery(sqlStr);
			if (rs1.next() == false) {
				return TCResult.newFailureResult("FBC001", "未配置分片规则");
			} else {
				// partitioncolumn = rs1.getString(1);
				String algorithm = rs1.getString(2);
				String algorithm_parameter = rs1.getString(3);
				String[] algorithmparameter_temp = algorithm_parameter
						.split(":");
				for (int i = 0; i < algorithmparameter_temp.length; i++) {
					algorithmparameter.add(algorithmparameter_temp[i]);
				}

				AppLogger.info("调用分片算法，参数：" + algorithm + "," + columnvalue
						+ "," + algorithmparameter + "," + tablename);
				JavaList partitionresult = calculate(algorithm, columnvalue,
						algorithmparameter, tablename);
				if (partitionresult.get(0) == "all") {
					String sql = "select distinct connpoolname from AFA_DDBM_PARTITIONMAP where tablename='"
							+ tablename + "' and opertype='" + rwtype + "'";
					AppLogger.info("sql:" + sql);
					ResultSet rs2 = stmt.executeQuery(sql);
					if (rs2.next() == false) {
						return TCResult.newFailureResult("FBC001", "未配置分片结果映射");
					} else {
						connpoolname.add(rs2.getString(1));
						while (rs2.next()) {
							connpoolname.add(rs2.getString(1));
						}
					}
				} else {
					AppLogger.info("分片结果：" + partitionresult);
					for (int i = 0; i < partitionresult.size(); i++) {
						String sql = "select connpoolname from (select connpoolname from afa_ddbm_partitionmap where tablename='"
								+ tablename
								+ "' and startdate<='"
								+ CurrentDate
								+ "' and enddate>='"
								+ CurrentDate
								+ "' and partitionresult='"
								+ partitionresult.get(i)
								+ "' and opertype='"
								+ rwtype
								+ "' order by dbms_random.random()) where rownum = 1";
						AppLogger.info("sql:" + sql);

						ResultSet rs2 = stmt.executeQuery(sql);
						if (rs2.next() == false) {
							return TCResult.newFailureResult("FBC001",
									"未配置分片结果映射");
						} else {
							connpoolname.add(rs2.getString(1));
						}
					}

				}

			}
			return TCResult.newSuccessResult(connpoolname);
		} else
			return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
	}

	public static JavaList calculate(String algorithm, JavaList columnvalue,
			JavaList algorithmparameter, String tablename) {
		// 入参algorithm 算法名，columnvalue 条件列值， algorithmparameter 参数列表， tablename
		// 表名
		JavaList PartitionResult = new JavaList();
		if (columnvalue == null) {
			// 返回所有连接池
			AppLogger.info("columnvalue为空，返回all");
			PartitionResult.add("all");
		} else if (columnvalue.getItem(1) == null) {// 若条件列值size为1，则只有值，没有运算条件，为insert
			if (algorithm.equals("PartitionByMod")) {
				Long algorithmparameter1 = Long
						.parseLong((String) algorithmparameter.getItem(0));
				String columnvalue1 = ((JavaList) columnvalue.getItem(2))
						.getItem(0);
				PartitionResult.add(String.valueOf(PartitionByMod(columnvalue1,
						algorithmparameter1)));
			} else if (algorithm.equals("PartitionByMurmurHash")) {
				int algorithmparameter1 = Integer.valueOf(
						(String) algorithmparameter.getItem(0)).intValue();
				String columnvalue1 = ((JavaList) columnvalue.getItem(2))
						.getItem(0);
				PartitionResult.add(String.valueOf(PartitionByMurmurHash(
						algorithmparameter1, 156, columnvalue1)));
			}
		} else {// 若条件列值size为2,则第一列为运算类型，第二列为值
			AppLogger.info("运算条件为：" + columnvalue.getItem(1));
			if (columnvalue.getItem(1).equals("=")) {
				AppLogger.info("运算条件为：=");
				if (algorithm.equals("PartitionByMod")) {
					AppLogger.info("分片算法为PartitionByMod");
					Long algorithmparameter1 = Long
							.parseLong((String) algorithmparameter.getItem(0));
					String columnvalue1 = ((JavaList) columnvalue.getItem(2))
							.getItem(0);
					PartitionResult.add(String.valueOf(PartitionByMod(
							columnvalue1, algorithmparameter1)));
				} else if (algorithm.equals("PartitionByMurmurHash")) {
					AppLogger.info("分片算法为PartitionByMurmurHash");
					int algorithmparameter1 = Integer.valueOf(
							(String) algorithmparameter.getItem(0)).intValue();
					String columnvalue1 = ((JavaList) columnvalue.getItem(2))
							.getItem(0);
					PartitionResult.add(PartitionByMurmurHash(
							algorithmparameter1, 156, columnvalue1));
				}
			} else if (columnvalue.getItem(1).equals("between")) {
				AppLogger.info("运算条件为：between");
				int max = Integer
						.valueOf(
								(String) ((JavaList) columnvalue.getItem(2))
										.getItem(1)).intValue();
				int min = Integer
						.valueOf(
								(String) ((JavaList) columnvalue.getItem(2))
										.getItem(0)).intValue();
				AppLogger.info("max:" + max + ",min:" + min);
				// int Dvalue = ((JavaList)
				// columnvalue.getItem(2)).getIntItem(1) - ((JavaList)
				// columnvalue.getItem(2)).getIntItem(0);
				int Dvalue = max - min;
				if (algorithm.equals("PartitionByMod")) {
					if (Dvalue >= Integer.valueOf(
							(String) algorithmparameter.getItem(0)).intValue()) {
						// 返回所有连接池
						AppLogger.info("Dvalue大于条件区间，返回all");
						PartitionResult.add("all");
					} else {
						for (int i = min; i <= max; i++) {
							// Long algorithmparameter1 =
							// Long.parseLong((String)
							// algorithmparameter.get(0));
							Long algorithmparameter1 = Long
									.parseLong((String) algorithmparameter
											.getItem(0));
							// String k = String.valueOf(i);
							// String k = ((JavaList)
							// columnvalue.getItem(2)).getItem(i);
							String k = String.valueOf(i);
							PartitionResult.add(String.valueOf(PartitionByMod(
									k, algorithmparameter1)));
						}
					}
				} else if (algorithm.equals("PartitionByMurmurHash")) {
					for (int i = min; i <= max; i++) {
						// int algorithmparameter1 =
						// Integer.valueOf(algorithmparameter.getIntItem(i));
						int algorithmparameter1 = Integer.valueOf(
								(String) algorithmparameter.getItem(0))
								.intValue();
						// String k = String.valueOf(i);
						// String k = ((JavaList)
						// columnvalue.getItem(2)).getItem(0);
						String k = String.valueOf(i);
						PartitionResult.add(PartitionByMurmurHash(
								algorithmparameter1, 156, k));
					}
				}
			} else if (columnvalue.getItem(1).equals("in")) {
				AppLogger.info("运算条件为：in");
				if (algorithm.equals("PartitionByMod")) {
					for (int i = 0; i < ((JavaList) columnvalue.get(2)).size(); i++) {
						// Long algorithmparameter1 = Long.parseLong((String)
						// algorithmparameter.get(0));
						Long algorithmparameter1 = Long
								.parseLong((String) algorithmparameter
										.getItem(0));
						// String k = String.valueOf(((JavaList)
						// columnvalue.getItem(2)).getIntItem(i));
						String k = ((JavaList) columnvalue.getItem(2))
								.getItem(i);
						PartitionResult.add(String.valueOf(PartitionByMod(k,
								algorithmparameter1)));
					}
				} else if (algorithm.equals("PartitionByMurmurHash")) {
					for (int i = 0; i < ((JavaList) columnvalue.get(2)).size(); i++) {
						AppLogger.info("i=" + i);
						int algorithmparameter1 = Integer.valueOf(
								(String) algorithmparameter.getItem(0))
								.intValue();
						AppLogger.info("algorithmparameter1="
								+ algorithmparameter1);
						String k = ((JavaList) columnvalue.getItem(2))
								.getItem(i);
						AppLogger.info("k=" + k);
						PartitionResult.add(PartitionByMurmurHash(
								algorithmparameter1, 156, k));
					}
				}
				HashSet h = new HashSet(PartitionResult);
				PartitionResult.clear();
				PartitionResult.addAll(h);
			} else {
				// 返回所有连接池
				AppLogger.info("运算条件不支持，返回all");
				PartitionResult.add("all");
			}
		}
		return PartitionResult;
	}

	public static Integer PartitionByMod(String columnvalue,
			long algorithmparameter) {
		try {
			BigInteger bigNum = new BigInteger(columnvalue).abs();
			return (bigNum.mod(BigInteger.valueOf(algorithmparameter)))
					.intValue();
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					new StringBuilder()
							.append("columnvalue:")
							.append(columnvalue)
							.append(" Please eliminate any quote and non number within it.")
							.toString(), e);
		}
	}

	public static Integer PartitionByMurmurHash(int node_num, int shards,
			String ColumnValue) {
		TreeMap<Long, Integer> nodes = new TreeMap<Long, Integer>();
		for (int i = 0; i != node_num; ++i) {
			for (int n = 0; n < shards; n++)
				nodes.put(hash("SHARD-" + i + "-NODE-" + n), i);
		}
		// long k = new Long(hash(ColumnValue)).intValue();
		SortedMap<Long, Integer> tail = nodes.tailMap(hash(ColumnValue));
		if (tail.size() == 0) {
			return nodes.get(nodes.firstKey());
		}
		return tail.get(tail.firstKey());
	}

	private static Long hash(String key) {
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(
					ByteOrder.LITTLE_ENDIAN);
			// finish.position(8-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return h;
	}

	/**
	 * @category HERS登记外设异常
	 * @param poolName
	 *            入参|连接池|{@link java.lang.String}
	 * @param abc_ip
	 *            入参|终端ip|{@link java.lang.String}
	 * @param groupLineNoList
	 *            入参|行号列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param errInfoList
	 *            入参|错误信息列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param md5List
	 *            入参|错误信息MD5|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池", type = java.lang.String.class),
			@Param(name = "abc_ip", comment = "终端ip", type = java.lang.String.class),
			@Param(name = "groupLineNoList", comment = "行号列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "errInfoList", comment = "错误信息列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "md5List", comment = "错误信息MD5", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "HERS登记外设异常", style = "判断型", type = "同步组件", comment = "根据agent上送的错误信息（行号列表与错误信息列表），登记到数据库表ABC_ERRMSG_INFO中", author = "Anonymous", date = "2017-06-26 05:50:21")
	public static TCResult A_writeDeviceError(String poolName, String abc_ip,
			JavaList groupLineNoList, JavaList errInfoList, JavaList md5List) {
		try {
			Connection conn = DBConnProvider.getConnection(poolName);
			Statement st = conn.createStatement();
			// PreparedStatement pst1 = conn.prepareStatement(__sql__1);
			// PreparedStatement pst2 = conn.prepareStatement(__sql__2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
			java.util.Date date = new java.util.Date();
			for (int i = 0; i < errInfoList.size(); i++) {
				// pst1.setString(1, abc_ip);
				// pst1.setString(2, sdf.format(date));
				// pst1.setString(3, sdf2.format(date));
				// pst1.setString(4, "1");
				// pst1.setString(5, md5List.getStringItem(i));
				// int __rowcount__ = pst1.executeUpdate();
				String __sql__1 = "insert into T_ABC_ERRMSG_INFO (ABC_IP,ERR_DT,ERR_TM,ERR_TYPE,ERR_MSG_MD5) values ('"
						+ abc_ip
						+ "','"
						+ sdf.format(date)
						+ "','"
						+ sdf2.format(date)
						+ "','"
						+ "0"
						+ "','"
						+ md5List.getStringItem(i) + "')";
				AppLogger.info(__sql__1);
				int __rownum__ = st.executeUpdate(__sql__1);
				// pst2.setString(1,md5List.getStringItem(i));
				// pst2.setString(2,errInfoList.getStringItem(i));
				// pst2.executeUpdate();
				File dir = new File("./errdata");
				if (!dir.exists())
					dir.mkdirs();
				File file = new File("./errdata/" + md5List.getStringItem(i));
				if (!file.exists())
					FileUtils.writeStringToFile(file,
							errInfoList.getStringItem(i), "UTF-8");
			}
			conn.commit();
			// try{pst2.close();}catch(Exception e){e.printStackTrace();}
			// try{pst1.close();}catch(Exception e){e.printStackTrace();}
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}

		return TCResult.newSuccessResult();
	}

	/**
	 * @category 列出策略待更新的终端
	 * @param poolName
	 *            入参|数据库连接池|{@link java.lang.String}
	 * @param agentList
	 *            入参|当前活跃的终端集合|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param agentPolicyDict
	 *            出参|策略待更新的终端清单|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
//	@InParams(param = {
//			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
//			@Param(name = "agentList", comment = "当前活跃的终端集合", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
//	@OutParams(param = { @Param(name = "agentPolicyDict", comment = "策略待更新的终端清单", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
//	@Returns(returns = { @Return(id = "0", desp = "失败"),
//			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
//	@Component(label = "列出策略待更新的终端", style = "选择型", type = "同步组件", comment = "列出终端清单，以及每个终端应该采用的新策略信息", author = "Anonymous", date = "2017-07-01 12:29:00")
//	public static TCResult A_listAgentToBeUpdatePolicy(String poolName,
//			JavaDict agentList) {
//		try {
//			JavaDict ret = new JavaDict();
//			JavaDict policyDict = null;
//			Connection conn = DBConnProvider.getConnection(poolName);
//			Statement stmt = conn.createStatement();
//			/*********** 查询所有终端信息 *************/
//			String sqlStr = "select CLIENT_IP,BRNO,ZONE,UPDATE_MODE,ISENABLED,POLICY_ID from T_AGENT_INFO where ISENABLED='0'";
//			AppLogger.info("sql:" + sqlStr);
//			ResultSet rs2 = stmt.executeQuery(sqlStr);
//			JavaDict agentInfoDict = new JavaDict();
//			AgentInfoBean tmpAgentInfo = null;
//			while (rs2.next()) {
//				tmpAgentInfo = new AgentInfoBean();
//				tmpAgentInfo.setClient_ip(rs2.getString(1));
//				tmpAgentInfo.setPolicy_id(rs2.getString(6));
//				agentInfoDict.put(tmpAgentInfo.getClient_ip(), tmpAgentInfo);
//			}
//			/*********** 遍历当前活跃的agent *********/
//			Object[] agentKeyArray = agentList.keySet().toArray();
//			JavaDict agentHeartBeating = null;
//			for (int i = 0; i < agentKeyArray.length; i++) {
//				if (!agentInfoDict.containsKey(agentKeyArray[i])) {
//					AppLogger.info("终端注册表中没有该终端信息，该终端[" + agentKeyArray[i]
//							+ "]可能已停用或未注册，不处理");
//					continue;
//				}
//				agentHeartBeating = agentList.getDictItem(agentKeyArray[i]);
//				if (agentHeartBeating.getStringItem("policy_id").equals(
//						((AgentInfoBean) agentInfoDict.get(agentKeyArray[i]))
//								.getPolicy_id())) {
//					// 当前策略无变化
//					continue;
//				}
//				// 下面推送新策略
//				if (policyDict == null) {
//					// 先初始化策略信息
//					policyDict = new JavaDict();
//					sqlStr = "select POLICY_ID,POLICY_RANGE,START_DT,END_DT,DEVICE_ERR_KEY,TRD_CODE,EVE_TRDFILE_MAXNUM,ALL_TRDFILE_MAXNUM from T_AGENT_POLICY_INFO where POLICY_STATUS='0'";
//					AppLogger.info("sql:" + sqlStr);
//					ResultSet rs1 = stmt.executeQuery(sqlStr);
//					JavaDict policyBean = null;
//					while (rs1.next()) {
//						policyBean = new JavaDict();
//						policyBean.put("Policy_id", rs1.getString(1));
//						policyBean.put("Policy_range", rs1.getString(2));
//						policyBean.put("Start_date", rs1.getString(3));
//						policyBean.put("End_date", rs1.getString(4));
//						policyBean.put("Device_err_key", rs1.getString(5));
//						policyBean.put("Trd_code", rs1.getString(6));
//						policyBean.put("Trd_file_maxnum", rs1.getString(7));
//						policyBean.put("All_file_maxnum", rs1.getString(8));
//						policyDict.put(policyBean.get("Policy_id"), policyBean);
//						AppLogger.info("添加基础策略信息:["
//								+ policyBean.get("Policy_id") + "]");
//					}
//					try {
//						rs1.close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				AppLogger.info("该终端["
//						+ agentKeyArray[i]
//						+ "]指定的策略id:["
//						+ ((AgentInfoBean) agentInfoDict.get(agentKeyArray[i]))
//								.getPolicy_id() + "]");
//				ret.put(agentKeyArray[i], policyDict
//						.get(((AgentInfoBean) agentInfoDict
//								.get(agentKeyArray[i])).getPolicy_id()));
//			}
//			try {
//				stmt.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (ret.size() == 0)
//				return new TCResult(2, "AAAAAA", "没有需要更新策略的终端");
//			return TCResult.newSuccessResult(ret);
//			// try{pst1.close();}catch(Exception e){e.printStackTrace();}
//		} catch (Exception e) {
//			return TCResult.newFailureResult("BBBBBBB", e);
//		}
//	}

	/**
	 * @category 查询终端的最新策略
	 * @param poolName
	 *            入参|数据库连接池|{@link java.lang.String}
	 * @param abc_ip
	 *            入参|终端ip|{@link java.lang.String}
	 * @param policyId
	 *            入参|终端当前策略id|{@link java.lang.String}
	 * @param needUpdate
	 *            出参|是否需要更新(0-不需要，1-需要)|{@link java.lang.String}
	 * @param policy
	 *            出参|策略信息的json串|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
			@Param(name = "abc_ip", comment = "终端ip", type = java.lang.String.class),
			@Param(name = "policyId", comment = "终端当前策略id", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "needUpdate", comment = "是否需要更新(0-不需要，1-需要)", type = java.lang.String.class),
			@Param(name = "policy", comment = "策略信息dict", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "查询终端的最新策略", style = "判断型", type = "同步组件", comment = "根据上送的终端ip和当前策略，查询后台配置的策略信息，返回是否需要更新以及新的策略信息", author = "Anonymous", date = "2017-07-01 04:52:04")
	public static TCResult A_queryPolicyByAgentIP(String poolName,
			String abc_ip, String policyId) {
		Statement stmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			JavaDict policyBean = new JavaDict();
			Connection conn = DBConnProvider.getConnection(poolName);
			stmt = conn.createStatement();
			/*********** 查询所有终端信息 *************/
			String sqlStr = "select CLIENT_IP,BRNO,ZONE,UPDATE_MODE,ISENABLED,POLICY_ID from T_AGENT_INFO where ISENABLED='0' and CLIENT_IP='"
					+ abc_ip + "'";
			AppLogger.info("sql:" + sqlStr);
			rs2 = stmt.executeQuery(sqlStr);
			if (!rs2.next()) {
				return TCResult.newSuccessResult("0", policyBean);
			}
			String curPolicyId = rs2.getString(6);
			if (curPolicyId.equals(policyId)) {
				return TCResult.newSuccessResult("0", policyBean);
			}
			sqlStr = "select POLICY_ID,POLICY_RANGE,START_DT,END_DT,DEVICE_ERR_KEY,TRD_CODE,EVE_TRDFILE_MAXNUM,ALL_TRDFILE_MAXNUM from T_AGENT_POLICY_INFO where POLICY_STATUS='0' and POLICY_ID='"
					+ curPolicyId + "'";
			AppLogger.info("sql:" + sqlStr);
			rs1 = stmt.executeQuery(sqlStr);
			if (!rs1.next()) {
				return TCResult.newSuccessResult("0", policyBean);
			}
			policyBean.put("Policy_id", rs1.getString(1));
			policyBean.put("Policy_range", rs1.getString(2));
			policyBean.put("Start_date", rs1.getString(3));
			policyBean.put("End_date", rs1.getString(4));
			policyBean.put("Device_err_key", rs1.getString(5));
			if (policyBean.getStringItem("Policy_id").equals("00000001")) {
				// 全行基准策略，交易码为空
				policyBean.put("Trd_list", "");
				policyBean.put("Trd_file_maxnum", "");
			} else {
				policyBean.put("Trd_list",
						rs1.getString(6).replaceAll("\\|", ","));
				policyBean.put("Trd_file_maxnum", rs1.getString(7));
			}
			policyBean.put("All_file_maxnum", rs1.getString(8));
			// policyDict.put(policyBean.get("Policy_id"), policyBean);
			AppLogger.info("添加基础策略信息:[" + policyBean.get("Policy_id") + "]");
			return TCResult.newSuccessResult("1", policyBean);

		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		} finally {
			if (rs1 != null)
				try {
					rs1.close();
				} catch (Exception e) {
				}
			if (rs2 != null)
				try {
					rs2.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
	}
	/**
	 * @category 查询策略信息
	 * @param poolName
	 *            入参|数据库连接池|{@link java.lang.String}
	 * @param policyId
	 *            入参|终端当前策略id|{@link java.lang.String}
	 * @param policy
	 *            出参|策略信息的json串|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
			@Param(name = "policyId", comment = "策略id", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "policy", comment = "策略信息dict", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "查询策略信息", style = "判断型", type = "同步组件", comment = "根据policyId查询策略信息", author = "Anonymous", date = "2017-07-01 04:52:04")
	public static TCResult A_queryPolicyByID(String poolName,
			 String policyId) {
		Statement stmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			JavaDict policyBean = new JavaDict();
			Connection conn = DBConnProvider.getConnection(poolName);
			stmt = conn.createStatement();
			String sqlStr = "select POLICY_ID,POLICY_RANGE,START_DT,END_DT,DEVICE_ERR_KEY,TRD_CODE,EVE_TRDFILE_MAXNUM,ALL_TRDFILE_MAXNUM from T_AGENT_POLICY_INFO where POLICY_ID='"+policyId+"'";
			AppLogger.info("sql:" + sqlStr);
			rs1 = stmt.executeQuery(sqlStr);
			if (!rs1.next()) {
				return TCResult.newSuccessResult("0", policyBean);
			}
			policyBean.put("Policy_id", rs1.getString(1));
			policyBean.put("Policy_range", rs1.getString(2));
			policyBean.put("Start_date", rs1.getString(3));
			policyBean.put("End_date", rs1.getString(4));
			policyBean.put("Device_err_key", rs1.getString(5));
			if (policyBean.getStringItem("Policy_id").equals("00000001")) {
				// 全行基准策略，交易码为空
				policyBean.put("Trd_list", "");
				policyBean.put("Trd_file_maxnum", "");
			} else {
//				policyBean.put("Trd_list",
//						rs1.getString(6).replaceAll("\\|", ","));
				policyBean.put("Trd_list",rs1.getString(6));
				policyBean.put("Trd_file_maxnum", rs1.getString(7));
			}
			policyBean.put("All_file_maxnum", rs1.getString(8));
			// policyDict.put(policyBean.get("Policy_id"), policyBean);
			AppLogger.info("添加基础策略信息:[" + policyBean.get("Policy_id") + "]");
			return TCResult.newSuccessResult("1", policyBean);

		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		} finally {
			if (rs1 != null)
				try {
					rs1.close();
				} catch (Exception e) {
				}
			if (rs2 != null)
				try {
					rs2.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * @category 广播预警处理
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param thresholdValue
	 *            入参|阀值|{@link java.lang.String}
	 * @param is_frame
	 *            入参|是否发往主框架（1-是，0-否）|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "thresholdValue", comment = "阀值", type = java.lang.String.class),
			@Param(name = "is_frame", comment = "是否发往主框架（1-是，0-否）", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "广播预警处理", style = "判断型", type = "同步组件", comment = "根据配置中的FRAME_BRO_CON或NORMAL_BRO_CON字段（分钟|条数 如 1|200），判断单位时间内发生的广播数量是否超出限制，若超出则登记预警表ABMSG_WARN_INFO和现场保护表ABMSG_PROT_INFO", author = "Anonymous", date = "2017-07-03 04:46:28")
	public static TCResult A_BroadcastAlert(String poolName,
			String thresholdValue, String is_frame) {
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		try {
			String[] threshold = thresholdValue.split("\\|");
			double thresholdTime = Double.parseDouble(threshold[0]);
			int thresholdCount = Integer.parseInt(threshold[1]);
			Connection conn = DBConnProvider.getConnection(poolName);
			stmt = conn.createStatement();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			Date date = new Date();
			String max_date_time = sdf1.format(date);
			Date mdate = new Date(
					(long) (date.getTime() - thresholdTime * 60 * 1000));
			String min_date_time = sdf1.format(mdate);
//			String sqlStr = "select max(AM_ID) AS max_am_id,min(AM_ID) AS min_am_id,SERVER_IP,ABS_NAME FROM T_ABMSG_INFO WHERE MSG_DT||MSG_TM BETWEEN '"
//					+ min_date_time + "' AND '" + max_date_time + "' GROUP BY SERVER_IP,ABS_NAME";
			String sqlStr = "select max(AM_ID) AS max_am_id,min(AM_ID) AS min_am_id,SERVER_IP,ABS_NAME  FROM T_ABMSG_INFO WHERE MSG_DT||MSG_TM BETWEEN '"
					+ min_date_time + "' AND '" + max_date_time + "' GROUP BY SERVER_IP,ABS_NAME";
			PreparedStatement ps = conn
					.prepareStatement(
							sqlStr,
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			AppLogger.info("查询时间范围内的最大与最小AM_ID.sql[" + sqlStr + "]");
//			rs = stmt.executeQuery(sqlStr);
//			if (!rs.next() || rs.getString(1) == null) {
//				AppLogger.info("该语句返回空,不进行后续处理");
//				return TCResult.newSuccessResult();
//			}
			rs = ps.executeQuery();
//		int rowCount = rs.getRow();
//		AppLogger.info("查到了["+rowCount+"]条数据");
//		for(int i=0;i<rowCount;i++){
		if(rs.next()){
			rs.previous();
		}else{
			AppLogger.info("该语句返回空,不进行后续处理");
			return TCResult.newSuccessResult();
		}
		while(rs.next()){
			String max_AM_ID = rs.getString(1);// 当前最大AM_ID
			String min_AM_ID = rs.getString(2);// 时间段内最小AM_ID
			String server_ip = rs.getString(3);
			String abs_name  = rs.getString(4);
			// 至此，已经锁定了AM_ID的范围，下面就可以根据这两个id所限定的范围，进行分析了！！！！！！！！！！！！！！！！！！！！！！！！！！
			sqlStr = "SELECT count(*) as mycount FROM T_ABMSG_INFO WHERE SERVER_IP='"+server_ip+"' AND ABS_NAME='"+abs_name+"' AND IS_FRAME='"
					+ is_frame
					+ "' AND MSG_TYPE='0' AND AM_ID BETWEEN '"
					+ min_AM_ID + "' AND '" + max_AM_ID + "'";// 发往主框架的广播数量
			// sqlStr =
			// "SELECT count(*) as mycount FROM ABMSG_INFO WHERE AM_ID BETWEEN '"+min_AM_ID+"' AND '"+max_AM_ID+"'";//仅仅为了测试有数，实际还是要以上面一行的为准
			AppLogger.info("查询消息个数.sql[" + sqlStr + "]");
			rs2 = stmt.executeQuery(sqlStr);
			if (!rs2.next()) {
				return TCResult.newSuccessResult();
			}
			int myCount = rs2.getInt(1);
			if (myCount < thresholdCount) {
				AppLogger.info("实际数量[" + myCount + "]小于阀值[" + thresholdCount
						+ "]，不处理.");
				return TCResult.newSuccessResult();
			}
			// 开始处理预警信息
			sqlStr = "SELECT  nextval for SEQ_ABMSG_WARN from sysibm.sysdummy1";
			rs3 = stmt.executeQuery(sqlStr);
			if (!rs3.next()) {
				return TCResult.newSuccessResult();
			}
			String __tmp__serno = rs3.getString(1);
			AppLogger.info("新增id[" + __tmp__serno + "]");
			String curDateTime = sdf.format(new Date());
			String __sql__ = "insert into T_ABMSG_WARN_INFO (WARN_ID,WT_ID,SERVER_IP,ABS_NAME,WARN_DT,WARN_TM,MSG_RATE,WARN_RATE,IS_FRAME,HD_TYPE) VALUES ("
					+ "'"
					+ __tmp__serno
					+ "','7','"+server_ip+"','"+abs_name+"','"
					+ curDateTime.substring(0, 10)
					+ "','"
					+ curDateTime.substring(10)
					+ "','"
					+ threshold[0]
					+ "|"
					+ myCount
					+ "','"
					+ thresholdValue
					+ "','"
					+ is_frame
					+ "','0')";
			AppLogger.info("插入预警信息表,sql[" + __sql__ + "]");
			int __rownum__ = stmt.executeUpdate(__sql__);
			__sql__ = "INSERT INTO T_ABMSG_PROT_INFO (SERVER_IP,ABS_NAME,MSG_TYPE,MSG_DT,MSG_TM,SEND_BRNO,SEND_TELLER,SEND_TRADE,IS_FRAME,SEND_ABC_OID,RE_ABC_OID,MSG_INFO,WARN_ID) "
					+ " SELECT SERVER_IP,ABS_NAME,MSG_TYPE,MSG_DT,MSG_TM,SEND_BRNO,SEND_TELLER,SEND_TRADE,IS_FRAME,SEND_ABC_OID,RE_ABC_OID,MSG_INFO,'"
					+ __tmp__serno
					+ "'"
					+ " FROM T_ABMSG_INFO WHERE SERVER_IP='"+server_ip+"' AND ABS_NAME='"+abs_name+"' AND IS_FRAME='"
					+ is_frame
					+ "' AND MSG_TYPE='0' AND AM_ID BETWEEN '"
					+ min_AM_ID
					+ "' AND '" + max_AM_ID + "'";
			// __sql__="INSERT INTO ABMSG_PROT_INFO (SERVER_IP,ABS_NAME,MSG_TYPE,MSG_DATE,MSG_TIME,SEND_BRNO,SEND_TRADE,IS_FRAME,SEND_ABC_OID,RE_ABC_OID,MSG_INFO,WARN_ID) "
			// +
			// " SELECT SERVER_IP,ABS_NAME,MSG_TYPE,MSG_DATE,MSG_TIME,SEND_BRNO,SEND_TRADE,IS_FRAME,SEND_ABC_OID,RE_ABC_OID,MSG_INFO,'"+__tmp__serno+"'"
			// +
			// " FROM ABMSG_INFO WHERE AM_ID BETWEEN '"+min_AM_ID+"' AND '"+max_AM_ID+"'";//仅仅为了测试有数，实际还是要以上面一行的为准
			AppLogger.info("插入现场保护表,sql[" + __sql__ + "]");
			__rownum__ = stmt.executeUpdate(__sql__);
			try {
				conn.commit();
			} catch (Exception e1) {
				AppLogger.info("数据库commit时出错." + e1.getMessage());
				e1.printStackTrace();
			}
			AppLogger.info("判断是否到了最后一个:"+rs.isLast());
			if(rs.isLast())break;
		}
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB",
					"处理失败." + e.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (rs2 != null)
				try {
					rs2.close();
				} catch (Exception e) {
				}
			if (rs3 != null)
				try {
					rs3.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}

		return TCResult.newSuccessResult();
	}

	/**
	 * @category 同一oid发出单点消息预警处理
	 * @param poolName
	 *            入参|连接池名称|{@link java.lang.String}
	 * @param thresholdValue
	 *            入参|阀值|{@link java.lang.String}
	 * @param is_frame
	 *            入参|是否发往主框架（1-是，0-否）|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "连接池名称", type = java.lang.String.class),
			@Param(name = "thresholdValue", comment = "阀值", type = java.lang.String.class),
			@Param(name = "is_frame", comment = "是否发往主框架（1-是，0-否）", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "同一oid发出单点消息预警处理", style = "判断型", type = "同步组件", comment = "根据配置中的NORMAL_BRO_CON字段（分钟|条数 如 1|200），判断单位时间内同一oid发出的目标为主框架交易的单点消息数量是否超出限制，若超出则登记预警表ABMSG_WARN_INFO和现场保护表ABMSG_PROT_INFO", author = "Anonymous", date = "2017-07-03 04:46:28")
	public static TCResult A_singlemsgAlert(String poolName,
			String thresholdValue, String is_frame) {
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		PreparedStatement ps2  = null;
		PreparedStatement ps =null;
		try {
			String[] threshold = thresholdValue.split("\\|");
			double thresholdTime = Double.parseDouble(threshold[0]);
			int thresholdCount = Integer.parseInt(threshold[1]);
			Connection conn = DBConnProvider.getConnection(poolName);
			stmt = conn.createStatement();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			Date date = new Date();
			String max_date_time = sdf1.format(date);
			Date mdate = new Date(
					(long) (date.getTime() - thresholdTime * 60 * 1000));
			String min_date_time = sdf1.format(mdate);
			String sqlStr = "select max(AM_ID) AS max_am_id,min(AM_ID) AS min_am_id,SERVER_IP,ABS_NAME  FROM T_ABMSG_INFO WHERE MSG_DT||MSG_TM BETWEEN '"
					+ min_date_time + "' AND '" + max_date_time + "' GROUP BY SERVER_IP,ABS_NAME";
			ps = conn
					.prepareStatement(
							sqlStr,
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			AppLogger.info("查询时间范围内的最大与最小AM_ID.sql[" + sqlStr + "]");
			
//			rs = stmt.executeQuery(sqlStr);
//			if (!rs.next() || rs.getString(1) == null) {
//				AppLogger.info("该语句返回空,不进行后续处理");
//				return TCResult.newSuccessResult();
//			}
			rs = ps.executeQuery();
			if(rs.next()){
				rs.previous();
			}else{
				AppLogger.info("该语句返回空,不进行后续处理");
				return TCResult.newSuccessResult();
			}
			while(rs.next()){
	//		int rowCount = rs.getRow();
	//		AppLogger.info("查到了["+rowCount+"]条数据");
	//		for(int i=0;i<rowCount;i++){
	//			rs.next();
				if(rs.getString(1)==null){
					AppLogger.info("该语句返回空,不进行后续处理");
					return TCResult.newSuccessResult();
				}
				String max_AM_ID = rs.getString(1);// 当前最大AM_ID
				String min_AM_ID = rs.getString(2);// 时间段内最小AM_ID
				String server_ip = rs.getString(3);
				String abs_name  = rs.getString(4);
				// 至此，已经锁定了AM_ID的范围，下面就可以根据这两个id所限定的范围，进行分析了！！！！！！！！！！！！！！！！！！！！！！！！！！
				// 单一oid。发往主框架（IS_FRAME=1）,单点消息（MSG_TYPE='1'）
				sqlStr = "SELECT SEND_ABC_OID,MYCOUNT FROM (select SEND_ABC_OID,COUNT(*) AS MYCOUNT FROM T_ABMSG_INFO WHERE SERVER_IP='"+server_ip+"' AND ABS_NAME='"+abs_name+"' AND IS_FRAME='"
						+ is_frame
						+ "' AND MSG_TYPE='1' AND AM_ID BETWEEN '"
						+ min_AM_ID
						+ "' AND '"
						+ max_AM_ID
						+ "' GROUP BY SEND_ABC_OID) WHERE MYCOUNT>"
						+ thresholdCount;
				AppLogger.info("查询OID.sql[" + sqlStr + "]");
				ps2 = conn
						.prepareStatement(
								sqlStr,
								ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
	//			rs2 = stmt.executeQuery(sqlStr);
				rs2 = ps2.executeQuery();
				if(rs2.next()){
					rs2.previous();
				}else{
					AppLogger.info("该语句返回空,不进行后续处理");
					return TCResult.newSuccessResult();
				}
				while (rs2.next()) {
	//				try {
	//					boolean next = rs2.next();
	//					if (!next) {
	//						AppLogger.info("找不到下一条A，跳出循环.");
	//						break;
	//					}
	//				} catch (Exception e2) {
	//					e2.printStackTrace();
	//					AppLogger.info("找不到下一条B，跳出循环.");
	//					break;
	//				}
					AppLogger.info("1......");
					int myCount = rs2.getInt(2);
					String oid = rs2.getString(1);
					AppLogger.info("2......");
					// 开始处理预警信息
					sqlStr = "SELECT  nextval for SEQ_ABMSG_WARN from sysibm.sysdummy1";
					rs3 = stmt.executeQuery(sqlStr);
					if (!rs3.next()) {
						return TCResult.newSuccessResult();
					}
					String __tmp__serno = rs3.getString(1);
					try {
						rs3.close();
					} catch (Exception e3) {
						e3.printStackTrace();
						AppLogger.info("rs3关闭出错:" + e3.getMessage());
					}
					AppLogger.info("新增id[" + __tmp__serno + "]");
					String curDateTime = sdf.format(new Date());
					String __sql__ = "insert into T_ABMSG_WARN_INFO (WARN_ID,WT_ID,SERVER_IP,ABS_NAME,WARN_DT,WARN_TM,MSG_RATE,WARN_RATE,IS_FRAME,HD_TYPE) VALUES ("
							+ "'"
							+ __tmp__serno
							+ "','8','"+server_ip+"','"+abs_name+"','"
							+ curDateTime.substring(0, 10)
							+ "','"
							+ curDateTime.substring(10)
							+ "','"
							+ threshold[0]
							+ "|"
							+ myCount
							+ "','"
							+ thresholdValue
							+ "','"
							+ is_frame + "','0')";
					AppLogger.info("插入预警信息表,sql[" + __sql__ + "]");
					int __rownum__ = stmt.executeUpdate(__sql__);
					__sql__ = "INSERT INTO T_ABMSG_PROT_INFO (SERVER_IP,ABS_NAME,MSG_TYPE,MSG_DT,MSG_TM,SEND_BRNO,SEND_TELLER,SEND_TRADE,IS_FRAME,SEND_ABC_OID,RE_ABC_OID,MSG_INFO,WARN_ID) "
							+ " SELECT SERVER_IP,ABS_NAME,MSG_TYPE,MSG_DT,MSG_TM,SEND_BRNO,SEND_TELLER,SEND_TRADE,IS_FRAME,SEND_ABC_OID,RE_ABC_OID,MSG_INFO,'"
							+ __tmp__serno
							+ "'"
							+ " FROM T_ABMSG_INFO WHERE SERVER_IP='"+server_ip+"' AND ABS_NAME='"+abs_name+"' AND IS_FRAME='"
							+ is_frame
							+ "' AND MSG_TYPE='1' AND SEND_ABC_OID='"
							+ oid
							+ "' AND AM_ID BETWEEN '"
							+ min_AM_ID
							+ "' AND '"
							+ max_AM_ID + "'";
					AppLogger.info("插入现场保护表,sql[" + __sql__ + "]");
					__rownum__ = stmt.executeUpdate(__sql__);
				}
				try {
					conn.commit();
				} catch (Exception e1) {
					AppLogger.info("数据库commit时出错." + e1.getMessage());
					e1.printStackTrace();
				}
				AppLogger.info("判断是否到了最后一个:"+rs.isLast());
				if(rs.isLast())break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB",
					"处理失败." + e.getMessage());
		} finally {
			if (rs != null)try {rs.close();} catch (Exception e) {}
			if (rs2 != null)try {rs2.close();} catch (Exception e) {}
			if (rs3 != null)try {rs3.close();} catch (Exception e) {}
			if (ps != null)try {ps.close();} catch (Exception e) {}
			if (ps2 != null)try {ps2.close();} catch (Exception e) {}
			
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 检查注册码是否使用
	 * @param poolName
	 *            入参|数据库连接池|{@link java.lang.String}
	 * @param devrandom
	 *            入参|注册码|{@link java.lang.String}
	 * @param devid
	 *            出参|参数值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
			@Param(name = "devrandom", comment = "注册码", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "devid", comment = "设备id", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "检查注册码是否使用", style = "选择型", type = "同步组件", author = "Anonymous", date = "2017-10-14 17:34:15")
	public static TCResult A_checkAgentIp(String poolName, String devrandom) {
		if (devrandom == null || "".equals(devrandom)){
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), "参数devrandom不存在");
		}
		String sqlcmd = "select devid from t_pcva_devinfo where devrandom = ? and devinuse not in ('0','3')";
		JavaList values = new JavaList();
		values.add(devrandom);
		int rownum = 1;
		String devid = null; // 参数值
		try {
			TCResult preparedSelect = P_Jdbc.preparedSelect(poolName, sqlcmd,
					values, rownum);
			List<?> outputParams = preparedSelect.getOutputParams();
			if (outputParams != null && outputParams.get(1) != null) {
				JavaList retList = (JavaList) outputParams.get(1);
				if (retList != null && retList.size() > 0) {
					JavaList colList = (JavaList) retList.get(0);
					devid = (String) colList.get(0);
				}
			} else {
				return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), "devid不存在");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(devid);
	}
	
	/**
	 * @category 检查Agent-IP是否可用
	 * @param poolName
	 *            入参|数据库连接池|{@link java.lang.String}
	 * @param agent_ip
	 *            入参|代理端ip地址|{@link java.lang.String}
	 * @param devid
	 *            出参|设备id|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
			@Param(name = "agent_ip", comment = "代理端ip地址", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "devid", comment = "设备id", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "检查Agent-IP是否可用", style = "选择型", type = "同步组件", author = "lk", date = "2018-02-04 17:34:15")
	public static TCResult A_checkAgentIpIsExist(String poolName, String agent_ip) {
		if (agent_ip == null || "".equals(agent_ip)){
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), "参数agent_ip不存在");
		}
		String sqlcmd = "select devNum from AUMS_DEV_INFO where DEVIP = ?";
		JavaList values = new JavaList();
		values.add(agent_ip);
		int rownum = 1;
		String devid = null; // 参数值
		try {
			TCResult preparedSelect = P_Jdbc.preparedSelect(poolName, sqlcmd,
					values, rownum);
			List<?> outputParams = preparedSelect.getOutputParams();
			if (outputParams != null && outputParams.get(1) != null) {
				JavaList retList = (JavaList) outputParams.get(1);
				if (retList != null && retList.size() > 0) {
					JavaList colList = (JavaList) retList.get(0);
					devid = (String) colList.get(0);
				}
			} else {
				return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), "devid不存在");
			}
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
		}
		return TCResult.newSuccessResult(devid);
	}

	/**
	 * @category jvm占用率预警
	 * @param poolName
	 *            入参|数据库连接池|{@link java.lang.String}
	 * @param JVM_SCAN_SPACE
	 *            入参|采集间隔（单位：秒）|{@link java.lang.String}
	 * @param JVM_WARN_VALUE
	 *            入参|JVM占用内存预警值（单位%）|{@link java.lang.String}
	 * @param JVM_RISE_RATIO_VALUE
	 *            入参|JVM占用内存比上升率（单位%/秒）预警值|{@link java.lang.String}
	 * @param JVM_RISE_CON
	 *            入参|触发上升率预警判断条件（单位 %）|{@link java.lang.String}
	 * @param DUMP_PATH
	 *            入参|dump文件生成路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
			@Param(name = "JVM_SCAN_SPACE", comment = "采集间隔（单位：秒）", type = java.lang.String.class),
			@Param(name = "JVM_WARN_VALUE", comment = "JVM占用内存预警值（单位%）", type = java.lang.String.class),
			@Param(name = "JVM_RISE_RATIO_VALUE", comment = "JVM占用内存比上升率（单位%/秒）预警值", type = java.lang.String.class),
			@Param(name = "JVM_RISE_CON", comment = "触发上升率预警判断条件（单位 %）", type = java.lang.String.class),
			@Param(name = "DUMP_PATH", comment = "dump文件生成路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "jvm占用率预警", style = "判断型", type = "同步组件", comment = "扫描T_JVM_INFO表，根据T_JVM_CONF表中的配置项（JVM_WARN_VALUE字段）判断并处理预警信息", author = "Anonymous", date = "2017-07-05 07:52:14")
	public static TCResult A_jvmUsageAlert(String poolName,
			String JVM_SCAN_SPACE,String JVM_WARN_VALUE,String JVM_RISE_RATIO_VALUE,String JVM_RISE_CON,String DUMP_PATH) {
		double jvm_warn_value = 0;
		try{
			jvm_warn_value = Double.parseDouble(JVM_WARN_VALUE);
		}catch(Exception e){
			AppLogger.info("入参thresholdValue["+JVM_WARN_VALUE+"]格式错误,不是数字型，不处理");
			return TCResult.newSuccessResult();
		}
		//思路：查询每个实例最新的id，再根据该id查出内存占用值，与入参阈值作比较，超过则预警
		Statement stmt = null;
		try {
			Connection conn = DBConnProvider.getConnection(poolName);
			stmt = conn.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			String curDate = sdf.format(new Date());
			String sqlStr = "INSERT INTO T_JVM_MEM_WARN_INFO (SERVER_IP,ABS_NAME,WARN_DT,WARN_TM,WT_ID,JVM_XMX,JVM_XMS,JVM_IDL,JVM_USAGE,JVM_RISE_RATIO,JVM_SCAN_SPACE,JVM_WARN_VALUE,JVM_RISE_RATIO_VALUE,JVM_RISE_CON,HD_TYPE) "
					+ "SELECT B.SERVER_IP,B.ABS_NAME,'"+curDate.substring(0, 10)+"','"+curDate.substring(10)+"','5',A.JVM_XMX,A.JVM_XMS,A.JVM_IDL,A.JVM_USAGE,'0','"+JVM_SCAN_SPACE+"','"+JVM_WARN_VALUE+"','"+JVM_RISE_RATIO_VALUE+"','"+JVM_RISE_CON+"','0' FROM T_JVM_MEM_INFO A,T_INS_TABLE_MAPPING B WHERE A.JVM_ID IN (SELECT MAX(JVM_ID) AS JVM_ID FROM T_JVM_MEM_INFO GROUP BY PARTITION_NO) AND INTEGER(A.JVM_USAGE)>"+jvm_warn_value+" AND A.PARTITION_NO=B.PARTITION_NO";
//			sqlStr = "select JVM_USAGE FROM T_JVM_MEM_INFO WHERE JVM_ID='"+jvm_id+"'";
			AppLogger.info("插入预警信息.sql[" + sqlStr + "]");
			stmt.executeUpdate(sqlStr);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB",
					"处理失败." + e.getMessage());
		} finally {
			if (stmt != null)try {stmt.close();} catch (Exception e) {}
		}
		
		
		return TCResult.newSuccessResult();
	}
//	/**
//	 * @category jvm上升速率预警
//	 * @param poolName
//	 *            入参|数据库连接池|{@link java.lang.String}
//	 * @param JVM_SCAN_SPACE
//	 *            入参|采集间隔（单位：秒）|{@link java.lang.String}
//	 * @param JVM_WARN_VALUE
//	 *            入参|JVM占用内存预警值（单位%）|{@link java.lang.String}
//	 * @param JVM_RISE_RATIO_VALUE
//	 *            入参|JVM占用内存比上升率（单位%/秒）预警值|{@link java.lang.String}
//	 * @param JVM_RISE_CON
//	 *            入参|触发上升率预警判断条件（单位 %）|{@link java.lang.String}
//	 * @return 0 失败<br/>
//	 *         1 成功<br/>
//	 */
//	@InParams(param = {
//			@Param(name = "poolName", comment = "数据库连接池", type = java.lang.String.class),
//			@Param(name = "JVM_SCAN_SPACE", comment = "采集间隔（单位：秒）", type = java.lang.String.class),
//			@Param(name = "JVM_WARN_VALUE", comment = "JVM占用内存预警值（单位%）", type = java.lang.String.class),
//			@Param(name = "JVM_RISE_RATIO_VALUE", comment = "JVM占用内存比上升率（单位%/秒）预警值", type = java.lang.String.class),
//			@Param(name = "JVM_RISE_CON", comment = "触发上升率预警判断条件（单位 %）", type = java.lang.String.class)})
//	@Returns(returns = { @Return(id = "0", desp = "失败"),
//			@Return(id = "1", desp = "成功") })
//	@Component(label = "jvm上升速率预警", style = "判断型", type = "同步组件", comment = "扫描T_JVM_INFO表，根据T_JVM_CONF表中的配置项（JVM_RISE_RATIO_VALUE和JVM_RISE_CON字段）判断并处理预警信息", author = "Anonymous", date = "2017-07-05 07:52:14")
//	public static TCResult A_jvmIncreaseSpeedAlert(String poolName,
//			String JVM_RISE_RATIO_VALUE,String JVM_RISE_CON) {
//		return TCResult.newSuccessResult();
//	}

}
