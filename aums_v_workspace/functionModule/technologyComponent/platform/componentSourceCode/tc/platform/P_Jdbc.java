package tc.platform;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * Jdbc操作类组件
 * 
 * @author linsheng
 * @date 2015-06-29 15:28:46
 * @version 2.0
 */
@ComponentGroup(level = "平台", groupName = "Jdbc操作类组件")
public class P_Jdbc {

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param sqlstr
	 *            入参|执行的SQL语句|{@link java.lang.String}
	 * @param commitFlg
	 *            入参|事务提交标识|boolean
	 * @param rows
	 *            出参|执行影响的条数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlstr", comment = "执行的SQL语句String", type = java.lang.String.class),
			@Param(name = "commitFlg", comment = "事务提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "rows", comment = "执行影响的条数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "SQL执行", style = "判断型", type = "同步组件", comment = "执行传入的SQL语句,执行影响的条数放到返回结果的list[3][0]中", date = "Mon Jun 29 15:35:00 CST 2015")
	public static TCResult executeSQL(String poolName, String sqlstr,
			boolean commitFlg) {
		AppLogger.info("要执行的SQL语句为：" + sqlstr);
		AppLogger.info("事务标识为：" + commitFlg);
		Statement stmt = null;
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"SQL执行组件数据库连接异常，获取数据库连接失败");
		}
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sqlstr);
			if (commitFlg) {
				conn.commit();
			}
			AppLogger.info("影响到的笔数为：" + result);
			return TCResult.newSuccessResult(result);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param sqlstr
	 *            入参|执行的SQL语句，如：select * from xxx where id = ?|
	 *            {@link java.lang.String}
	 * @param values
	 *            入参|SQL语句中?的值，列中的值顺序要和?的位置对应，如：[val1,val2...]|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|事务提交标识|boolean
	 * @param rows
	 *            出参|执行影响的条数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlstr", comment = "执行的SQL语句String", type = java.lang.String.class),
			@Param(name = "values", comment = "SQL语句中?的值，列中的值顺序要和?的位置对应JavaList，如：[val1,val2...]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "事务提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "rows", comment = "执行影响的条数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "预编译SQL执行", style = "判断型", type = "同步组件", comment = "用预编译的方式执行传入的SQL语句,执行影响的条数放到返回结果的list[3][0]中", date = "Mon Jun 29 15:35:00 CST 2015")
	public static TCResult preparedExecuteSQL(String poolName, String sqlstr,
			JavaList values, boolean commitFlg) {
		AppLogger.info("要执行的SQL语句为：" + sqlstr);
		AppLogger.info("事务标识为：" + commitFlg);
		PreparedStatement stmt = null;
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"SQL执行组件数据库连接异常，获取数据库连接失败");
		}
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.prepareStatement(sqlstr);
			for (int i = 1; i <= values.size(); i++) {
				stmt.setObject(i, values.get(i - 1));
			}
			int result = stmt.executeUpdate();
			if (commitFlg) {
				conn.commit();
			}
			AppLogger.info("影响到的笔数为：" + result);
			return TCResult.newSuccessResult(result);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param seqName
	 *            入参|序号名称|{@link java.lang.String}
	 * @param dbType
	 *            入参|数据库类型|{@link java.lang.String}
	 * @param seqLen
	 *            入参|序号长度|int
	 * @param outname
	 *            出参|序号的当前值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "seqName", comment = "序号名称String", type = java.lang.String.class),
			@Param(name = "dbType", comment = "数据库类型String，如：\"ORA\"、\"DB2\"或\"INF\"", type = java.lang.String.class),
			@Param(name = "seqLen", comment = "序号长度int", type = int.class) })
	@OutParams(param = { @Param(name = "outname", comment = "序号的当前值string", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "序列号操作", style = "判断型", type = "同步组件", comment = "获取数据库中指定sequence的当前值,返回值保存在返回的list[3]中,现仅支持Oracle/DB2/Informix三种数据库,对于Informix,dual表是手工创建的", date = "Mon Jun 29 15:37:29 CST 2015")
	public static TCResult getSequence(String poolName, String seqName,
			String dbType, int seqLen) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"获取序号值信息异常，获取数据库连接失败");
		}
		AppLogger.info("进入P_DBGetSequence,输入参数信息：");
		AppLogger.info("序号名称：" + seqName);
		AppLogger.info("数据库类型：" + dbType);
		AppLogger.info("序号长度：" + seqLen);
		StringBuilder sqlcmd = new StringBuilder();
		if (dbType.equalsIgnoreCase("DB2")) {
			sqlcmd.append("SELECT  nextval for ").append(seqName.trim())
					.append(" FROM sysibm.sysdummy1");
		} else if (dbType.equalsIgnoreCase("ORA")
				|| dbType.equalsIgnoreCase("INF")) {
			sqlcmd.append("SELECT ").append(seqName.trim())
					.append(".NEXTVAL FROM DUAL");
		} else {
			return TCResult.newFailureResult(ErrorCode.AGR, "数据库类型 " + dbType
					+ " 非法，仅支持Oracle、DB2和Informix");
		}
		Statement stmt = null;
		ResultSet result = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			result = stmt.executeQuery(sqlcmd.toString());
			int rows = 0;
			String sequenceStr = null;
			while (result.next()) {
				rows++;
				sequenceStr = result.getString(1);
			}
			AppLogger.info("获取到的结果集为：" + rows);
			if (rows != 1) {
				return TCResult.newFailureResult(ErrorCode.REMOTE,
						"获取序号值信息异常,结果集不为1条");
			}
			// sequence小于设定的长度时在左边补0
			int resultLength = sequenceStr.length();
			if (sequenceStr != null && !sequenceStr.isEmpty()
					&& sequenceStr.length() < seqLen) {
				for (int i = 0; i < (seqLen - resultLength); i++) {
					sequenceStr = "0" + sequenceStr;
				}
			}
			AppLogger.info("获取到的序号值为：" + sequenceStr);
			return TCResult.newSuccessResult(sequenceStr);
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(result);
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据库提交", style = "判断型", type = "同步组件", comment = "提交数据库的事务,0-系统异常,1-提交成功", date = "Mon Jun 29 15:38:16 CST 2015")
	public static TCResult commit(String poolName) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"提交事务时数据库异常，获取数据库连接失败");
		}
		try {
			conn.commit();
			return TCResult.newSuccessResult();
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数据库回滚", style = "判断型", type = "同步组件", comment = "回滚数据库事务,0-回滚失败,1-回滚成功", date = "Mon Jun 29 15:38:56 CST 2015")
	public static TCResult rollBack(String poolName) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"回滚事务时数据库异常，获取数据库连接失败");
		}
		try {
			conn.rollback();
			return TCResult.newSuccessResult();
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "关闭连接", style = "判断型", type = "同步组件", comment = "关闭连接", date = "Tue Jul 21 11:45:05 CST 2015")
	public static TCResult closeConnection(String poolName) {
		Connection conn = getConnection(poolName);
		try {
			conn.close();
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.CONN, e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|DDL创建语句SQL|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "DDL创建SQL语句String", type = java.lang.String.class) })
	@OutParams(param = {})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对象创建", style = "判断型", type = "同步组件", comment = "执行数据库对象DDL创建语句", date = "Mon Jun 29 15:40:04 CST 2015")
	public static TCResult ddlCreate(String poolName, String sqlcmd) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"数据库DDL创建时数据库异常，获取数据库连接失败");
		}
		Statement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlcmd);
			return TCResult.newSuccessResult();
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|DDL删除语句SQL|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "DDL删除SQL语句String", type = java.lang.String.class) })
	@OutParams(param = {})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对象删除", style = "判断型", type = "同步组件", comment = "执行数据库DDL删除语句", date = "Mon Jun 29 15:41:03 CST 2015")
	public static TCResult ddlDelete(String poolName, String sqlcmd) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"执行数据库DDL删除语句时数据库连接异常，获取数据库连接失败");
		}
		Statement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlcmd);
			return TCResult.newSuccessResult();
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|DDL修改语句SQL|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "DDL修改SQL语句String", type = java.lang.String.class) })
	@OutParams(param = {})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对象更改", style = "判断型", type = "同步组件", comment = "执行数据库DDL修改语句", date = "Mon Jun 29 15:42:09 CST 2015")
	public static TCResult ddlModify(String poolName, String sqlcmd) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"执行数据库DDL修改语句时数据库连接异常，获取数据库连接失败");
		}
		Statement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlcmd);
			return TCResult.newSuccessResult();
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|指定的数据库连接池名，如果不填则从第一个连接池中获取连接|{@link java.lang.String}
	 * @param tableName
	 *            入参|表名|{@link java.lang.String}
	 * @param values
	 *            入参|新增列值key,value的list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|boolean
	 * @param rows
	 *            出参|执行插入影响的条数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "tableName", comment = "表名String", type = java.lang.String.class),
			@Param(name = "values", comment = "新增列值JavaList，如：[[\"COL1\",VAL1],[\"COL2\",VAL2]...]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "rows", comment = "执行插入影响的条数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "数据插入", style = "选择型", type = "同步组件", comment = "登记数据到数据库中,values参数格式为[[\"列1\",值1],[\"列2\",值2],...]", date = "Mon Jun 29 15:43:40 CST 2015")
	public static TCResult dmlInsert(String poolName, String tableName,
			JavaList values, boolean commitFlg) {
		AppLogger.info("dbDMLInsert函数输入信息：");
		AppLogger.info("库表名称：" + tableName);
		AppLogger.info("列值key,value列表值：" + String.valueOf(values));
		AppLogger.info("提交标识：" + commitFlg);
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"数据库连接异常，获取数据库连接失败");
		}

		// 拼接插入SQL语句
		List<Object> columnValues = new ArrayList<Object>();
		StringBuilder sqlcmd = new StringBuilder();
		sqlcmd.append("INSERT INTO ").append(tableName).append("(");
		for (Object value : values) {
			if (value instanceof JavaList) {
				if (((JavaList) value).size() != 2) {
					return TCResult.newFailureResult(ErrorCode.AGR,
							"List中的每个单元列表值非法,里面要素个数不为2");
				}
				sqlcmd.append(((JavaList) value).get(0));
				if (values.indexOf(value) != values.size() - 1) {
					sqlcmd.append(",");
				}
				columnValues.add(((JavaList) value).get(1));
			} else {
				return TCResult.newFailureResult(ErrorCode.AGR,
						"列值列表中的每个单元类型非法,不是列表");
			}
		}
		sqlcmd.append(") ").append("VALUES(");
		for (int i = 1; i <= columnValues.size(); i++) {
			Object val = columnValues.get(i - 1);
			if (val == null) {
				sqlcmd.append("\'\'");
			} else {
				if (val instanceof String || val instanceof Character) {
					sqlcmd.append("\'").append(val).append("\'");
				} else {
					sqlcmd.append(String.valueOf(val));
				}
				if (i != columnValues.size()) {
					sqlcmd.append(",");
				}
			}
		}
		sqlcmd.append(")");
		AppLogger.info("insert 的语句为：" + sqlcmd.toString());

		Statement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			int rows = stmt.executeUpdate(sqlcmd.toString());
			if (commitFlg) {
				conn.commit();
			}
			if (rows == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "新增记录时影响的笔数为0");
			}
			return TCResult.newSuccessResult(rows);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param tableName
	 *            入参|表名|{@link java.lang.String}
	 * @param values
	 *            入参|新增列值key,value的list|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|boolean
	 * @param rows
	 *            出参|执行插入影响的条数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "tableName", comment = "表名String", type = java.lang.String.class),
			@Param(name = "values", comment = "新增列值JavaList，如：[[\"COL1\",VAL1],[\"COL2\",VAL2]...]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "rows", comment = "执行插入影响的条数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "预编译数据插入", style = "选择型", type = "同步组件", comment = "用预编译的方式登记数据到数据库中,values参数格式为[[\"列1\",值1],[\"列2\",值2],...]", date = "Mon Jun 29 15:43:40 CST 2015")
	public static TCResult preparedInsert(String poolName, String tableName,
			JavaList values, boolean commitFlg) {
		AppLogger.info("dbDMLInsert函数输入信息：");
		AppLogger.info("库表名称：" + tableName);
		AppLogger.info("列值key,value列表值：" + String.valueOf(values));
		AppLogger.info("提交标识：" + commitFlg);
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN,
					"数据库连接异常，获取数据库连接失败");
		}

		// 拼接插入SQL语句
		List<Object> columnValues = new ArrayList<Object>();
		StringBuilder sqlcmd = new StringBuilder();
		sqlcmd.append("INSERT INTO ").append(tableName).append("(");
		for (Object value : values) {
			if (value instanceof JavaList) {
				if (((JavaList) value).size() != 2) {
					return TCResult.newFailureResult(ErrorCode.AGR,
							"List中的每个单元列表值非法,里面要素个数不为2");
				}
				sqlcmd.append(((JavaList) value).get(0));
				if (values.indexOf(value) != values.size() - 1) {
					sqlcmd.append(",");
				}
				columnValues.add(((JavaList) value).get(1));
			} else {
				return TCResult.newFailureResult(ErrorCode.AGR,
						"列值列表中的每个单元类型非法,不是列表");
			}
		}
		sqlcmd.append(") ").append("VALUES(");
		for (int i = 1; i <= columnValues.size(); i++) {
			sqlcmd.append("?");
			if (i != columnValues.size()) {
				sqlcmd.append(",");
			}
		}
		sqlcmd.append(")");
		AppLogger.info("insert 的语句为：" + sqlcmd.toString());

		PreparedStatement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.prepareStatement(sqlcmd.toString());
			for (int i = 1; i <= columnValues.size(); i++) {
				stmt.setObject(i, columnValues.get(i - 1));
			}
			int rows = stmt.executeUpdate();
			if (commitFlg) {
				conn.commit();
			}
			if (rows == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "新增记录时影响的笔数为0");
			}
			return TCResult.newSuccessResult(rows);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param tableName
	 *            入参|表名|{@link java.lang.String}
	 * @param condition
	 *            入参|条件语句的值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|boolean
	 * @param rows
	 *            出参|执行删除影响的条数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "tableName", comment = "表名String", type = java.lang.String.class),
			@Param(name = "condition", comment = "条件的值JavaList，如：[\"列\",\"=\",值]或[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "rows", comment = "执行插入影响的条数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "数据删除", style = "选择型", type = "同步组件", comment = "删除指定条件的记录,其中条件语句的值用法为：[[\"COL1\",\"=\",VAL1,\"AND\"],[\"COL2\",\"!=\",VAL2,null]] = COL1=VAL1 AND COL2=VAL2", date = "Mon Jun 29 15:45:19 CST 2015")
	public static TCResult dmlDelete(String poolName, String tableName,
			JavaList condition, boolean commitFlg) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		StringBuilder sqlcmd = new StringBuilder();
		sqlcmd.append("DELETE FROM ").append(tableName);
		if (condition == null || condition.size() == 0) {
			sqlcmd.append("");
		} else {
			sqlcmd.append(" WHERE (");
			for (Object o : condition) {
				if (o instanceof JavaList) {
					if (((JavaList) o).size() < 3) {
						return TCResult
								.newFailureResult(ErrorCode.AGR,
										"输入的条件参数错误，用法：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(((JavaList) o).get(0)).append(
							((JavaList) o).get(1));
					if (((JavaList) o).get(2) instanceof String
							|| ((JavaList) o).get(2) instanceof Character) {
						sqlcmd.append("\'").append(((JavaList) o).get(2))
								.append("\'");
					} else {
						sqlcmd.append(String.valueOf(((JavaList) o).get(2)));
					}
					if (((JavaList) o).size() == 4
							&& ((JavaList) o).get(3) != null) {
						sqlcmd.append(" ").append(((JavaList) o).get(3))
								.append(" ");
					}
				} else {
					if (condition.size() != 3) {
						return TCResult
								.newFailureResult(ErrorCode.AGR,
										"输入的条件参数错误，用法：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(condition.get(0)).append(condition.get(1));
					if (condition.get(2) instanceof String
							|| condition.get(2) instanceof Character) {
						sqlcmd.append("\'").append(condition.get(2))
								.append("\'");
					} else {
						sqlcmd.append(String.valueOf(condition.get(2)));
					}
					break;
				}
			}
			sqlcmd.append(")");
		}
		Statement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			AppLogger.info("delete 的语句为：" + sqlcmd.toString());
			stmt = conn.createStatement();
			int rows = stmt.executeUpdate(sqlcmd.toString());
			if (rows == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "删除时未找到记录");
			}
			if (commitFlg) {
				conn.commit();
			}
			return TCResult.newSuccessResult(rows);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param tableName
	 *            入参|表名|{@link java.lang.String}
	 * @param condition
	 *            入参|条件语句的值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|boolean
	 * @param rows
	 *            出参|执行删除影响的条数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "tableName", comment = "表名String", type = java.lang.String.class),
			@Param(name = "condition", comment = "条件的值JavaList，如：[\"列\",\"=\",值]或[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "rows", comment = "执行插入影响的条数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "预编译数据删除", style = "选择型", type = "同步组件", comment = "用预编译的方式删除指定条件的记录,其中条件语句的值用法为：[[\"COL1\",\"=\",VAL1,\"AND\"],[\"COL2\",\"!=\",VAL2,null]] = COL1=VAL1 AND COL2=VAL2", date = "Mon Jun 29 15:45:19 CST 2015")
	public static TCResult preparedDelete(String poolName, String tableName,
			JavaList condition, boolean commitFlg) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		StringBuilder sqlcmd = new StringBuilder();
		sqlcmd.append("DELETE FROM ").append(tableName);
		List<Object> condValues = new ArrayList<Object>();
		if (condition == null || condition.size() == 0) {
			sqlcmd.append("");
		} else {
			sqlcmd.append(" WHERE (");
			for (Object o : condition) {
				if (o instanceof JavaList) {
					if (((JavaList) o).size() < 3) {
						return TCResult
								.newFailureResult(ErrorCode.AGR,
										"输入的条件参数错误，用法：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(((JavaList) o).get(0))
							.append(((JavaList) o).get(1)).append("?");
					if (((JavaList) o).size() == 4
							&& ((JavaList) o).get(3) != null) {
						sqlcmd.append(" ").append(((JavaList) o).get(3))
								.append(" ");
					}
					condValues.add(((JavaList) o).get(2));
				} else {
					if (condValues.size() > 0) {
						return TCResult
								.newFailureResult(ErrorCode.AGR,
										"输入的条件参数错误，用法：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(condition.get(0)).append(condition.get(1))
							.append("?");
					condValues.add(condition.get(2));
					break;
				}
			}
			sqlcmd.append(")");
		}
		PreparedStatement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			AppLogger.info("delete 的语句为：" + sqlcmd.toString());
			stmt = conn.prepareStatement(sqlcmd.toString());
			for (int i = 1; i <= condValues.size(); i++) {
				stmt.setObject(i, condValues.get(i - 1));
			}
			int rows = stmt.executeUpdate();
			if (rows == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "删除时未找到记录");
			}
			if (commitFlg) {
				conn.commit();
			}
			return TCResult.newSuccessResult(rows);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param tableName
	 *            入参|表名|{@link java.lang.String}
	 * @param colInfos
	 *            入参|列信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param condition
	 *            入参|sql语句条件的值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|boolean
	 * @param result
	 *            出参|影响到的笔数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "tableName", comment = "表名String", type = java.lang.String.class),
			@Param(name = "colInfos", comment = "列信息JavaList，如：[[\"COL1\",VAL1],[\"COL2\",VAL2]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "condition", comment = "条件信息JavaList，如：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "result", comment = "影响到的笔数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "数据更新", style = "选择型", type = "同步组件", comment = "更新指定条件指定库表的指定列值，条件信息的用法为：[[\"tid\",\"=\",\"20090101\",\"and\"],[\"pid\",\"=\",\"ppasdf\"]] = (列1=值1 and 列2=值2)，更新影响的笔数放到list[3]中", date = "Mon Jun 29 15:47:33 CST 2015")
	public static TCResult dmlUpdate(String poolName, String tableName,
			JavaList colInfos, JavaList condition, boolean commitFlg) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		// 拼接更新SQL语句
		StringBuilder sqlcmd = new StringBuilder();
		sqlcmd.append("UPDATE ").append(tableName).append(" SET ");
		for (Object colInfo : colInfos) {
			if (colInfo instanceof JavaList) {
				if (((JavaList) colInfo).size() != 2) {
					return TCResult
							.newFailureResult(ErrorCode.AGR,
									"列信息的每个单元列表值非法，里面要素个数不为2，用法：[[\"列1\",值1],[\"列2\",值2]...]");
				}
				sqlcmd.append(((JavaList) colInfo).get(0)).append("=");
				if (((JavaList) colInfo).get(1) instanceof String
						|| ((JavaList) colInfo).get(1) instanceof Character) {
					sqlcmd.append("\'").append(((JavaList) colInfo).get(1))
							.append("\'");
				} else {
					sqlcmd.append(String.valueOf(((JavaList) colInfo).get(1)));
				}
				if (colInfos.indexOf(colInfo) != colInfos.size() - 1) {
					sqlcmd.append(",");
				}
			} else {
				return TCResult
						.newFailureResult(ErrorCode.AGR,
								"列信息的每个单元值非法，每个单元必须是List，用法：[[\"列1\",值1],[\"列2\",值2]...]");
			}
		}
		if (condition != null && condition.size() != 0) {
			sqlcmd.append(" WHERE ");
			for (Object o : condition) {
				if (o instanceof JavaList) {
					if (((JavaList) o).size() < 3) {
						return TCResult
								.newFailureResult(
										ErrorCode.AGR,
										"条件信息错误，参数个数不正确，用法：[\"列\",\"=\",值]或者[['\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(((JavaList) o).get(0)).append(
							((JavaList) o).get(1));
					if (((JavaList) o).get(2) instanceof String
							|| ((JavaList) o).get(2) instanceof Character) {
						sqlcmd.append("\'").append(((JavaList) o).get(2))
								.append("\'");
					} else {
						sqlcmd.append(String.valueOf(((JavaList) o).get(2)));
					}
					if (((JavaList) o).size() == 4
							&& ((JavaList) o).get(3) != null) {
						sqlcmd.append(((JavaList) o).get(3)).append(" ");
					}
				} else {
					if (condition.size() != 3) {
						return TCResult
								.newFailureResult(ErrorCode.AGR,
										"条件信息错误，用法：[\"列\",\"=\",值]或者[[\"列1\",'\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(condition.get(0)).append(condition.get(1));
					if (condition.get(2) instanceof String
							|| condition.get(2) instanceof Character) {
						sqlcmd.append("\'").append(condition.get(2))
								.append("\'");
					} else {
						sqlcmd.append(String.valueOf(condition.get(2)));
					}
					break;
				}
			}
		}
		Statement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sqlcmd.toString());
			if (result == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "更新无满足条件记录");
			}
			if (commitFlg) {
				conn.commit();
			}
			return TCResult.newSuccessResult(result);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param tableName
	 *            入参|表名|{@link java.lang.String}
	 * @param colInfos
	 *            入参|表的列名|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param condition
	 *            入参|sql语句条件的值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param commitFlg
	 *            入参|提交标识|boolean
	 * @param result
	 *            出参|影响到的笔数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "tableName", comment = "表名String", type = java.lang.String.class),
			@Param(name = "colInfos", comment = "列信息JavaList，如：[[\"COL1\",VAL1],[\"COL2\",VAL2]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "condition", comment = "条件信息JavaList，如：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "commitFlg", comment = "提交标识Boolean", type = boolean.class) })
	@OutParams(param = { @Param(name = "result", comment = "影响到的笔数int", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "预编译数据更新", style = "选择型", type = "同步组件", comment = "用预编译的方式更新指定条件指定库表的指定列值，条件信息的用法为：[[\"列1\",\"=\",值,\"and\"],[\"列2\",\"=\",值2]] = (列1=值1 and 列2=值2)，更新影响的笔数放到list[3]中", date = "Mon Jun 29 15:47:33 CST 2015")
	public static TCResult preparedUpdate(String poolName, String tableName,
			JavaList colInfos, JavaList condition, boolean commitFlg) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		List<Object> values = new ArrayList<Object>();
		// 拼接更新SQL语句
		StringBuilder sqlcmd = new StringBuilder();
		sqlcmd.append("UPDATE ").append(tableName).append(" SET ");
		for (Object colInfo : colInfos) {
			if (colInfo instanceof JavaList) {
				if (((JavaList) colInfo).size() != 2) {
					return TCResult
							.newFailureResult(ErrorCode.AGR,
									"列信息的每个单元列表值非法，里面要素个数不为2，用法：[[\"列1\",值1],[\"列2\",值2]...]");
				}
				sqlcmd.append(((JavaList) colInfo).get(0)).append("=")
						.append("?");
				if (colInfos.indexOf(colInfo) != colInfos.size() - 1) {
					sqlcmd.append(",");
				}
				values.add(((JavaList) colInfo).get(1));
			} else {
				return TCResult
						.newFailureResult(ErrorCode.AGR,
								"列信息的每个单元非法，每个单元必须是List，用法：[[\"列1\",值1],[\"列2\",值2],...]");
			}
		}
		if (condition != null && condition.size() != 0) {
			sqlcmd.append(" WHERE (");
			for (Object o : condition) {
				if (o instanceof JavaList) {
					if (((JavaList) o).size() < 3) {
						return TCResult
								.newFailureResult(
										ErrorCode.AGR,
										"条件信息错误，参数个数不正确，用法：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(((JavaList) o).get(0))
							.append(((JavaList) o).get(1)).append("?");
					if (((JavaList) o).size() == 4
							&& ((JavaList) o).get(3) != null) {
						sqlcmd.append(((JavaList) o).get(3)).append(" ");
					}
					values.add(((JavaList) o).get(2));
				} else {
					if (condition.size() != 3) {
						return TCResult
								.newFailureResult(ErrorCode.AGR,
										"条件信息错误，用法：[\"列\",\"=\",值]或者[[\"列1\",\"=\",值1,\"and\"],[\"列2\",\"<\",值2]]");
					}
					sqlcmd.append(condition.get(0)).append(condition.get(1))
							.append("?");
					values.add(condition.get(2));
					break;
				}
			}
			sqlcmd.append(")");
		}
		// AppLogger.debug("sqlcmd.toString():"+sqlcmd.toString());
		PreparedStatement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.prepareStatement(sqlcmd.toString());
			for (int i = 1; i <= values.size(); i++) {
				stmt.setObject(i, values.get(i - 1));
			}
			int result = stmt.executeUpdate();
			if (result == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "更新无满足条件记录");
			}
			if (commitFlg) {
				conn.commit();
			}
			return TCResult.newSuccessResult(result);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				return TCResult.newFailureResult(ErrorCode.SQL, e1);
			}
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|查询sql语句|{@link java.lang.String}
	 * @param rownum
	 *            入参|需要获取的数据笔数|int
	 * @param num
	 *            出参|查询到的行数|int
	 * @param result
	 *            出参|所有的行数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "查询sql语句String", type = java.lang.String.class),
			@Param(name = "rownum", comment = "需要获取的数据笔数int", type = int.class) })
	@OutParams(param = {
			@Param(name = "num", comment = "查询到的行数int", type = int.class),
			@Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "数据查询", style = "选择型", type = "同步组件", comment = "根据sql查询语句获取查询的数据,默认是取所有,可以指定需要获取的行数,list[3]中存放查询到的笔数和数据,格式为:[笔数,所有行的list]", date = "Mon Jun 29 15:49:28 CST 2015")
	public static TCResult dmlSelect(String poolName, String sqlcmd, int rownum) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			if (rownum > 0) {
				stmt.setMaxRows(rownum);
			}
			rs = stmt.executeQuery(sqlcmd);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			JavaList result = new JavaList();
			while (rs.next()) {
				JavaList list = new JavaList();
				for (int i = 1; i <= colCount; i++) {
					list.add(rs.getObject(i));
				}
				result.add(list);
			}
			if (result.size() == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
			}
			return TCResult.newSuccessResult(result.size(), result);
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.REMOTE, e);
		} finally {
			closeResource(rs);
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|查询sql语句|{@link java.lang.String}
	 * @param values
	 *            入参|SQL语句中?的值，列中的值顺序要和?的位置对应|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param rownum
	 *            入参|需要获取的数据笔数|int
	 * @param num
	 *            出参|查询到的行数|int
	 * @param result
	 *            出参|所有的行数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "查询sql语句String", type = java.lang.String.class),
			@Param(name = "values", comment = "SQL语句中?的值JavaList，列中的值顺序要和?的位置对应，如：[VAL1,VAL2...]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "rownum", comment = "需要获取的数据笔数int", type = int.class) })
	@OutParams(param = {
			@Param(name = "num", comment = "查询到的行数int", type = int.class),
			@Param(name = "result", comment = "所有的行数据list", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "预编译数据查询", style = "选择型", type = "同步组件", comment = "根据sql查询语句获取查询的数据,默认是取所有,可以指定需要获取的行数,list[3]中存放查询到的笔数和数据,格式为:[笔数,所有行的list]", date = "Mon Jun 29 15:49:28 CST 2015")
	public static TCResult preparedSelect(String poolName, String sqlcmd,
			JavaList values, int rownum) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.prepareStatement(sqlcmd);
			for (int i = 1; i <= values.size(); i++) {
				stmt.setObject(i, values.get(i - 1));
			}
			if (rownum > 0) {
				stmt.setMaxRows(rownum);
			}
			rs = stmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			JavaList result = new JavaList();
			while (rs.next()) {
				JavaList list = new JavaList();
				for (int i = 1; i <= colCount; i++) {
					list.add(rs.getObject(i));
				}
				result.add(list);
			}
			if (result.size() == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
			}
			return TCResult.newSuccessResult(result.size(), result);
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.REMOTE, e);
		} finally {
			closeResource(rs);
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param busidataKey
	 *            入参|sql关键字|{@link java.lang.String}
	 * @param result
	 *            出参|所有的行数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "busidataKey", comment = "sql关键字String", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "格式化SQL查询", style = "选择型", type = "同步组件", comment = "根据数据库SQLKey查询SQL", date = "Mon Jun 29 15:51:11 CST 2015")
	public static TCResult sqlKeySelect(String poolName, String busidataKey) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sqlcmd = "select sqlstr, proctablename from AUMS_SYS_DATAPROCSQL_VIEW where busidatakey = ?";
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.prepareStatement(sqlcmd);
			stmt.setString(1, busidataKey);
			stmt.setMaxRows(1);
			rs = stmt.executeQuery(sqlcmd);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			JavaList result = new JavaList();
			while (rs.next()) {
				JavaList list = new JavaList();
				for (int i = 1; i <= colCount; i++) {
					list.add(rs.getObject(i));
				}
				result.add(list);
			}
			if (result.size() == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "无满足条件记录");
			}
			return TCResult.newSuccessResult(result);
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(rs);
			closeResource(stmt);
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param procname
	 *            入参|存储过程名称|{@link java.lang.String}
	 * @param parasList
	 *            入参|输入参数列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param result
	 *            出参|结果信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "procname", comment = "存储过程名称String", type = java.lang.String.class),
			@Param(name = "parasList", comment = "输入参数列表JavaList，用法：[[\"in\",入参值],[\"out\",\"参数类型(如string)\"],[\"inout\",出入参值,\"参数类型(如string)\"]]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "result", comment = "结果信息JavaList，如果只有IN参数没有OUT和INOUT参数，结果为空[]；有OUT或者INOUT出参结果：[100,\"xx\"]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "调用存储过程", style = "判断型", type = "同步组件", comment = "调用存储过程，参数列表用法：[[\"in\",入参值],[\"out\",\"参数类型(如string)\"],[\"inout\",出入参值,\"参数类型(如string)\"]]；返回的结果信息中，如果只有IN参数没有OUT和INOUT参数，结果为空[]；有OUT或者INOUT出参结果：[100,\"xx\"]；出参类型为以下之一：string、int、short、long、float、double、byte、boolean、date、time", date = "Mon Jun 29 16:06:33 CST 2015")
	public static TCResult callProcedure(String poolName, String procname,
			JavaList parasList) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		// 拼接调用存储过程SQL语句
		StringBuilder sqlcmd = new StringBuilder();
		Map<Integer, Object> inParas = new HashMap<Integer, Object>();
		Map<Integer, Integer> outParas = new HashMap<Integer, Integer>();
		Map<Integer, JavaList> inOutParas = new HashMap<Integer, JavaList>();
		sqlcmd.append("{call ").append(procname).append("(");
		if (parasList.size() > 0) {
			for (int i = 0; i <= parasList.size() - 1; i++) {
				Object par = parasList.get(i);
				if ((par instanceof JavaList) && ((JavaList) par).size() >= 2
						&& ((JavaList) par).get(0) instanceof String) {
					String inOut = (String) ((JavaList) par).get(0);
					if (inOut.trim().equalsIgnoreCase("in")) {
						inParas.put(i + 1, ((JavaList) par).get(1));
					} else if (inOut.trim().equalsIgnoreCase("out")) {
						if (!(((JavaList) par).get(1) instanceof String)) {
							return TCResult
									.newFailureResult(
											ErrorCode.AGR,
											"参数列表中的出参列的错误，出参类型为以下之一：string、int、short、long、float、double、byte、boolean、date、time");
						}
						String paraType = (String) ((JavaList) par).get(1);
						int type = getParasType(paraType.trim());
						if (type == 0) {
							return TCResult
									.newFailureResult(
											ErrorCode.AGR,
											"出参列中的参数类型错误，出参类型为以下之一：string、int、short、long、float、double、byte、boolean、date、time");
						}
						outParas.put(i + 1, type);
					} else if (inOut.trim().equalsIgnoreCase("inout")) {
						if (((JavaList) par).size() != 3) {
							return TCResult
									.newFailureResult(ErrorCode.AGR,
											"参数列表中的参数列错误，inout参数类型的列个数必须为3，用法：[[\"inout\",参数值,\"参数类型(如string)\"]]");
						}
						if (!(((JavaList) par).get(2) instanceof String)) {
							return TCResult
									.newFailureResult(
											ErrorCode.AGR,
											"参数列表中的出参列的错误，出参类型为以下之一：string、int、short、long、float、double、byte、boolean、date、time");
						}
						Object paraValue = ((JavaList) par).get(1);
						String paraType = (String) ((JavaList) par).get(2);
						int type = getParasType(paraType.trim());
						if (type == 0) {
							return TCResult
									.newFailureResult(
											ErrorCode.AGR,
											"出参列中的参数类型错误，出参类型为以下之一：string、int、short、long、float、double、byte、boolean、date、time");
						}
						JavaList inOutParaInfo = new JavaList();
						inOutParaInfo.add(paraValue);
						inOutParaInfo.add(type);
						inOutParas.put(i + 1, inOutParaInfo);
					} else {
						return TCResult
								.newFailureResult(
										ErrorCode.AGR,
										"存储过程的参数列表错误，用法：[[\"in\",入参值],[\"out\",\"参数类型(如string)\"],[\"inout\",参数值,\"参数类型(如string)\"]]");
					}
				} else {
					return TCResult
							.newFailureResult(
									ErrorCode.AGR,
									"存储过程的参数列表错误，用法：[[\"in\",入参值],[\"out\",\"参数类型(如string)\"],[\"inout\",参数值,\"参数类型(如string)\"]]");
				}

				sqlcmd.append("?");
				if (i != parasList.size() - 1) {
					sqlcmd.append(",");
				}
			}
		}
		sqlcmd.append(")}");

		CallableStatement stmt = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.prepareCall(sqlcmd.toString());

			if (inParas.size() > 0) { // 设置in入参
				for (Entry<Integer, Object> entry : inParas.entrySet()) {
					stmt.setObject(entry.getKey(), entry.getValue());
				}
			}

			Set<Integer> resultsIndex = new TreeSet<Integer>(); // 这里用TreeSet是为了保证输出结果的顺序和传入inout或out参数的顺序一致

			if (inOutParas.size() > 0) { // 设置inout入参
				for (Entry<Integer, JavaList> entry : inOutParas.entrySet()) {
					stmt.setObject(entry.getKey(), entry.getValue().get(1));
				}
			}

			if (inOutParas.size() > 0) { // 设置inout出参
				for (Entry<Integer, JavaList> entry : inOutParas.entrySet()) {
					stmt.registerOutParameter(entry.getKey(), (Integer) entry
							.getValue().get(2));
					resultsIndex.add(entry.getKey());
				}
			}

			if (outParas.size() > 0) { // 设置out出参
				for (Entry<Integer, Integer> entry : outParas.entrySet()) {
					stmt.registerOutParameter(entry.getKey(), entry.getValue());
					resultsIndex.add(entry.getKey());
				}
			}

			stmt.execute();
			JavaList results = new JavaList();
			if (resultsIndex.size() > 0) {
				for (int index : resultsIndex) {
					Object result = stmt.getObject(index);
					results.add(result);
				}
			}
			return TCResult.newSuccessResult(results);
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.SQL, e);
		} finally {
			closeResource(stmt);
		}
	}

	private static int getParasType(String type) {
		if (type.equalsIgnoreCase("string")) {
			return Types.VARCHAR;
		} else if (type.equalsIgnoreCase("int")) {
			return Types.INTEGER;
		} else if (type.equalsIgnoreCase("long")) {
			return Types.BIGINT;
		} else if (type.equalsIgnoreCase("short")) {
			return Types.SMALLINT;
		} else if (type.equalsIgnoreCase("float")) {
			return Types.FLOAT;
		} else if (type.equalsIgnoreCase("double")) {
			return Types.DOUBLE;
		} else if (type.equalsIgnoreCase("byte")) {
			return Types.BINARY;
		} else if (type.equalsIgnoreCase("boolean")) {
			return Types.BOOLEAN;
		} else if (type.equalsIgnoreCase("date")) {
			return Types.DATE;
		} else if (type.equalsIgnoreCase("time")) {
			return Types.TIME;
		} else {
			return 0;
		}
	}

	private static void closeResource(Object resource) {
		if (resource == null) {
			return;
		}
		try {
			if (resource instanceof ResultSet) {
				((ResultSet) resource).close();
			} else if (resource instanceof Statement) {
				((Statement) resource).close();
			}
		} catch (SQLException e) {
			AppLogger.error(e);
		}
	}

	private static Connection getConnection(String poolName) {
		try {
			if (poolName == null || poolName.isEmpty()) {
				return DBConnProvider.getConnection();
			} else {
				return DBConnProvider.getConnection(poolName);
			}
		} catch (SQLException e) {
			AppLogger.error(e);
			return null;
		}
	}

	/**
	 * @param poolName
	 *            入参|数据库连接池名|{@link java.lang.String}
	 * @param sqlcmd
	 *            入参|查询sql语句|{@link java.lang.String}
	 * @param rownum
	 *            入参|需要获取的数据笔数|int
	 * @param num
	 *            出参|查询到的行数|int
	 * @param result
	 *            出参|所有的行数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "poolName", comment = "指定的数据库连接池名，如果不填则从第一个连接池中获取连接String", type = java.lang.String.class),
			@Param(name = "sqlcmd", comment = "查询sql语句String", type = java.lang.String.class),
			@Param(name = "rownum", comment = "需要获取的数据笔数int", type = int.class) })
	@OutParams(param = {
			@Param(name = "num", comment = "查询到的行数int", type = int.class),
			@Param(name = "result", comment = "所有的行数据JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "数据查询(返dict)", style = "选择型", type = "同步组件", comment = "根据sql查询语句获取查询的数据,默认是取所有,可以指定需要获取的行数,list[3]中存放查询到的笔数和数据,格式为:[笔数,所有行的list]", date = "Mon Jun 29 15:49:28 CST 2015")
	public static TCResult dmlSelectDict(String poolName, String sqlcmd,
			int rownum) {
		Connection conn = getConnection(poolName);
		if (conn == null) {
			return TCResult.newFailureResult(ErrorCode.CONN, "获取数据库连接失败");
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (conn.isClosed()) {
				return TCResult.newFailureResult(ErrorCode.CONN, "数据库连接已关闭");
			}
			stmt = conn.createStatement();
			if (rownum > 0) {
				stmt.setMaxRows(rownum);
			}
			rs = stmt.executeQuery(sqlcmd);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			JavaList result = new JavaList();
			while (rs.next()) {
				JavaDict dict = new JavaDict();
				
				
				for (int i = 1; i <= colCount; i++) {
					// list.add(rs.getObject(i));
					dict.put(rsmd.getColumnName(i).toUpperCase(),
							rs.getObject(i));
				}
				result.add(dict);
			}
			if (result.size() == 0) {
				return new TCResult(2, ErrorCode.REMOTE, "无满足条件的记录");
			}
			return TCResult.newSuccessResult(result.size(), result);
		} catch (SQLException e) {
			return TCResult.newFailureResult(ErrorCode.REMOTE, e);
		} finally {
			closeResource(rs);
			closeResource(stmt);
		}
	}

	/**
	 * @category 写入sqldata到文件中
	 * @param sqldata
	 *            入参|查询出的数据内容|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param path
	 *            入参|生成文件的路径|{@link java.lang.String}
	 * @param filename
	 *            入参|文件名|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "sqldata", comment = "查询出的数据内容", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "path", comment = "生成文件的路径", type = java.lang.String.class),
			@Param(name = "sqlkey", comment = "查询sqlkey", type = java.lang.String.class),
			@Param(name = "filename", comment = "文件名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "写入sqldata到文件中", style = "处理型", type = "同步组件", comment = "将查出来的sqldict 写成txt传到目录中", date = "2017-10-14 12:20:58")
	public static TCResult P_WriteSqlData(JavaList sqldata,String path, String sqlkey,
			String filename) {
		String append = "";
		byte[] content = null;
		long count = 0;
		Boolean flag=false;
		try {
			if (path == null || path.length() == 0) {
				path = File.createTempFile(filename, ".txt").getAbsolutePath();
			} else {
				File file=new File(path);
				if (file.exists()) {
					file.delete();
				}
				path = new File(path).getAbsolutePath();
			}
			OutputStream os = null;
			try {
				os = new BufferedOutputStream(new FileOutputStream(path, true));
				
				String[] keyAry=sqlkey.split(",");
				
				for (int j = 0; j < keyAry.length; j++) {
					String key = (String) keyAry[j].trim();
					append += key;
					append += "|";
				}
				append += "\n";
				count++;
				for (int i = 0; i < sqldata.size(); i++) {
					Map dataMap = new HashMap();
					dataMap = ((Map) sqldata.get(i));
					for (int j = 0; j < keyAry.length; j++) {
						String key = keyAry[j].trim();
						String value=(String)dataMap.get(key);
						if (null==value||"null".equals(value)){
							value="";
						}
						append +=  value;
						append += "|";
					}
					append += "\n";
					count++;
					if (count % 100 == 0 && count - 1 < sqldata.size()) {
						content = append.getBytes("UTF-8");
						os.write(content);
						os.flush();
						append="";
					} else if (count - 1 == sqldata.size()) {
						content = append.getBytes("UTF-8");
						os.write(content);
						os.flush();
					}
				}
			} finally {
				if (os != null) {
					os.close();
				}
			}
		} catch (IOException e) {
			return TCResult.newFailureResult(ErrorCode.REMOTE, e);
		}
		return TCResult.newSuccessResult();
	}

}
