package tc.bank.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import tc.bank.constant.BusException;
import tc.bank.constant.DBConstant;
import tc.bank.constant.ErrorCodeModule;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 数据库通用业务处理工具类
 * 
 * @author AlanMa
 * 
 */
public class DBTechUtil {

    /**
     * 获取数据库连接
     * 
     * @param poolName
     * @return
     */
    public static Connection getConnection(String poolName) {
        try {
            if (poolName == null || poolName.isEmpty()) {
            	return DBConnProvider.getConnection();
           }
            else {
                return DBConnProvider.getConnection(poolName);
            }
        }
        catch (SQLException e) {
            AppLogger.error(e);
            return null;
        }
    }

    /**
     * 关闭数据库资源
     * 
     * @param resource
     */
    public static void closeResource(Object resource) {
        if (resource == null) {
            return;
        }
        try {
            if (resource instanceof ResultSet) {
                ((ResultSet) resource).close();
            }
            else if (resource instanceof Statement) {
                ((Statement) resource).close();
            }
        }
        catch (SQLException e) {
            AppLogger.error(e);
        }
    }

    /**
     * 分页预编译查询
     * 
     * @param poolName
     * @param sqlcmd
     * @param values
     * @param startNo
     *            从哪一条记录开始
     * @param maxCount
     *            总共取多少条记录
     * @param coutflagdata
     *           总记录计算标识 ,不为空就用这个字段,否则调用count(*)算出总笔数
     * @return
     */
    public static TCResult preparedSelectPage(String poolName, String sqlcmd, JavaList values, int startNo, int maxCount,JavaDict coutflagdata) {

        AppLogger.info("【eneter B_DBUnityRptOper.preparedSelectPage】");
        Connection conn = getConnection(poolName);
        int count = 0;
        if (conn == null) {
            AppLogger.error("【fail to get database connection】");
            return RetResultUtil.getErrorTCResult(ErrorCodeModule.IMD010);
        }
        PreparedStatement stmtCount = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet resSet = null;
        int totalrownum = 0;
        int totalpagenum = 0;
        int nowpagenum = 0;
        String tellerno="";
        String e_servicecode="";
        
        try {
            if (conn.isClosed()) {
                AppLogger.error("【database connection is closed】");
                return RetResultUtil.getErrorTCResult(ErrorCodeModule.IMD011);
            }
            
            /**
             * 统计结果数
             */
            StringBuffer sqlCount = new StringBuffer();
            sqlCount.append("select  count(*)");
            sqlCount.append(sqlcmd.subSequence(sqlcmd.indexOf(" from "), sqlcmd.length()));
            String rowCount = sqlCount.toString().toUpperCase();
            if(rowCount.contains("ORDER")){
            	rowCount = rowCount.substring(0,rowCount.lastIndexOf("ORDER")-1);            	
            }
           
			// queryflag 是否需要查询总记录 1 是 0 否
			int queryflag = 1;
			// insertflag 是否需要插入临时表 1 是 0 否
			int insertflag = 0;
			if (coutflagdata != null) {
				String flag = coutflagdata.getStringItem("flag");
				tellerno = coutflagdata.getStringItem("tellerno");
				e_servicecode = coutflagdata.getStringItem("e_servicecode");
				if (flag.equals(null) || flag.equals("") || flag.equals("0")
						|| tellerno.equals(null) || tellerno.equals("")) {
					queryflag = 1;
					insertflag = 0;
				} else {
					// 获取当前时间和时间戳
					// 获取当前时间戳
					Calendar calendar = Calendar.getInstance();
					DateFormat dfdate = new SimpleDateFormat("yyyyMMdd");
					String nowdate = dfdate.format(calendar.getTime());

					String sqlstr = "select querynum,crtdatetime from tp_cip_querypagetmp where querydate='"
							+ nowdate
							+ "' "
							+ "and tellerno='"
							+ tellerno
							+ "' and p_servicecode='"
							+ e_servicecode
							+ "' and querywhere='"
							+ rowCount
							+ ":"
							+ values
							+ "'";

					// 查询临时表是否已登记
					TCResult tc_result = P_Jdbc.dmlSelect(poolName, sqlstr, 0);
					if (tc_result == null) {
						return RetResultUtil.getTCResult(new BusException(
								ErrorCodeModule.IMD012, sqlstr));
					}
					int status = tc_result.getStatus();

					if (status == 0) {
						AppLogger.debug("查询分页临时表，异常结束");
						return RetResultUtil.getTCResult(new BusException(
								ErrorCodeModule.IMD012,
								"执行P_Jdbc.executeSQL未成功,"
										+ tc_result.getErrorCode() + ":"
										+ tc_result.getErrorMsg()));
					} else if (status == 2) {
						AppLogger.debug("查询分页临时表，无满足条件记录");
						queryflag = 1;
						insertflag = 1;
					} else if (status == 1) {
						AppLogger.debug("查询分页临时表，有查询临时表数据");
						List<?> outputParams = tc_result.getOutputParams();
						JavaList countdata = (JavaList) ((JavaList) outputParams
								.get(1)).getListItem((0));
						count = Integer.parseInt(countdata.getStringItem(0));
						String crtdatetime = countdata.getStringItem(1);
						// 判断间隔时间
						DateFormat df3 = new SimpleDateFormat(
								"yyyyMMddHHmmssSSSSSS");
						String nowdatetime = df3.format(calendar.getTime());
						AppLogger.debug("当前时间戳:" + nowdatetime + ",创建时间戳:"
								+ crtdatetime);
						try {
							long timecrtdatetime = df3.parse(crtdatetime)
									.getTime();
							long nowtimecrtdatetime = df3.parse(nowdatetime)
									.getTime();
							long tmptimes = nowtimecrtdatetime
									- timecrtdatetime;
							if (tmptimes >= 5 * 60000) {
								String sqlstr1 = "delete  from tp_cip_querypagetmp where querydate='"
										+ nowdate
										+ "' "
										+ "and tellerno='"
										+ tellerno
										+ "' and p_servicecode='"
										+ e_servicecode
										+ "' and querywhere='"
										+ rowCount + ":" + values + "'";
								TCResult tc_result1 = P_Jdbc.executeSQL(
										poolName, sqlstr1, true);
								if (tc_result1 == null) {
									return RetResultUtil
											.getTCResult(new BusException(
													ErrorCodeModule.IMD012,
													sqlstr1));
								}
								int status1 = tc_result1.getStatus();
								if (status1 != 1) {
									return RetResultUtil
											.getTCResult(new BusException(
													ErrorCodeModule.IMD012,
													sqlstr1));
								}
								AppLogger.debug("超过查询的间隔时间戳5分钟");
								queryflag = 1;
								insertflag = 1;

							} else {
								AppLogger.debug("未超过查询的间隔时间戳5分钟");
								queryflag = 0;
								insertflag = 0;
							}

						} catch (ParseException e) {
							return RetResultUtil.getTCResult(new BusException(
									ErrorCodeModule.IMD012,
									"执行ParseException异常" + e.getMessage()));
						}

					}

				}

			}
			
			//coutflagdata 未上送走默认查询。
            
			if (queryflag==1){
            stmtCount = conn.prepareStatement(rowCount, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            for (int i = 1; values != null && i <= values.size(); i++) {
                stmtCount.setObject(i, values.get(i - 1));
            }
            rs = stmtCount.executeQuery();
            if (rs.next()) {
                if (rs.getObject(1) != null && rs.getObject(1) instanceof BigDecimal) {
                    count = ((BigDecimal) rs.getObject(1)).intValue();
                }
                if (rs.getObject(1) != null && rs.getObject(1) instanceof Long) {
                    count = ((Long) rs.getObject(1)).intValue();
                }
                if (rs.getObject(1) != null && rs.getObject(1) instanceof Integer) {
                    count = ((Integer) rs.getObject(1)).intValue();
                }
            }
            AppLogger.info("[count is :]" + count);
            
            if(insertflag==1)
            {
            	AppLogger.debug("准备插入临时表数据");
            	// 获取当前时间戳
				Calendar calendar = Calendar.getInstance();
				DateFormat dfdate = new SimpleDateFormat("yyyyMMdd");
				String nowdate = dfdate.format(calendar.getTime());
				DateFormat df3 = new SimpleDateFormat(
						"yyyyMMddHHmmssSSSSSS");
				String nowdatetime = df3.format(calendar.getTime());
				String sqlstr2 = "insert into tp_cip_querypagetmp (QUERYDATE, TELLERNO, P_SERVICECODE, QUERYNUM, QUERYWHERE, CRTDATETIME)values " +
						"('"+nowdate+"','"+tellerno+"','"+e_servicecode+"','"+count+"','"+rowCount + ":" + values+"','"+nowdatetime+"')";
				TCResult tc_result2 = P_Jdbc.executeSQL(
						poolName, sqlstr2, true);
				if (tc_result2 == null) {
					return RetResultUtil
							.getTCResult(new BusException(
									ErrorCodeModule.IMD012,
									sqlstr2));
				}
				int status2 = tc_result2.getStatus();
				if (status2 != 1) {
					return RetResultUtil
							.getTCResult(new BusException(
									ErrorCodeModule.IMD012,
									sqlstr2));
				}
				
				
				
            }
            
			}
            /**
             * 如果结果数为0，直接返回
             */
            if(count == 0){
            	AppLogger.info("【no records by count】");
                return RetResultUtil.getWarningTCResult(ErrorCodeModule.IMB001,DBConstant.NONE_REC,totalrownum, totalpagenum, nowpagenum);
            }
            
            totalrownum = count;
            /**
             * 全量查询（GA.GL）
             */
            if(maxCount == 0){
            	if (startNo == DBConstant.START_ROW_ALL) {
                    startNo = DBConstant.START_ROW;
                    maxCount = count;
                    totalpagenum = 1;
                    nowpagenum = 1;
                }
            }
            /**
             * 分页查询(S,SA,SL,PL)
             */
            else {
                totalpagenum = (count % maxCount == 0 ? count / maxCount : count / maxCount + 1);
                //计算最后一页的startNo
                if(startNo == -1){
                	startNo = (totalpagenum -1)*maxCount + 1 ;
                	nowpagenum = totalpagenum ;
                }
                //当前页（第一页）
                else if (startNo < maxCount) {
                    nowpagenum = 1;
                }
                //上一页和下一页
                else {
                    nowpagenum = (startNo % maxCount == 0 ? startNo / maxCount : startNo / maxCount + 1);
                }
            }
            
            /**
             * 预编译查询结果集
             */
            stmt = conn.prepareStatement(sqlcmd, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (ListUtil.isNotEmpty(values)) {
                for (int i = 1; i <= values.size(); i++) {
                    stmt.setObject(i, values.get(i - 1));
                }
            }
            stmt.setMaxRows(startNo + maxCount - 1);
            /*if (maxCount > 0) {
            	stmt.setMaxRows(startNo + maxCount - 1);
            }
            else {
               stmt.setMaxRows(startNo + DBConstant.MAX_ROW - 1);
            }*/
            resSet = stmt.executeQuery();
            // 将游标移动到第一条记录
            resSet.first();
            // 游标移动到要输出的第一条记录
            resSet.relative(startNo - 2);
            /*if (startNo > 0) {
                resSet.relative(startNo - 2);
            }
            else {
                resSet.relative(startNo - 1);
            }*/

            ResultSetMetaData rsmd = resSet.getMetaData();
            int colCount = rsmd.getColumnCount();
            JavaList result = new JavaList();
            while (resSet.next()) {
                JavaList list = new JavaList();
                for (int i = 1; i <= colCount; i++) {
                    list.add(resSet.getObject(i));
                }
                result.add(list);
            }
            return TCResult.newSuccessResult(result.size(), result, totalrownum, totalpagenum, nowpagenum);
        }
        catch (SQLException e) {
            AppLogger.error(e);
            return RetResultUtil.getTCResult(new BusException(ErrorCodeModule.IMD012, e.getMessage()));
        }
        finally {
            closeResource(rs);
            closeResource(resSet);
            closeResource(stmtCount);
            closeResource(stmt);
        }
    }
}
