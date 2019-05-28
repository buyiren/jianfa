package tc.bank.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.DBConstant;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.constant.SysConstant;
import tc.bank.domain.EntityTAD;
import tc.platform.P_Jdbc;
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
public class DBBusiUtil {

    /**
     * 获取SQL模板业务唯一标识
     * 
     * @param busioper
     * @param cond_data_context
     * @return
     * @throws BusException
     */
    public static JavaList getBusidateKey(String busioper, JavaDict map_context) throws BusException {
        String sqlCmd = "select busidatakey from " + "AUMS_SYS_FDATAPROCMAPSQL_VIEW where modulecode=? and transcode=? and busioper=? order by operseqno asc";
        JavaList condList = new JavaList(map_context.getStringItem("modulecode"), map_context.	getStringItem("transcode"), busioper);
        AppLogger.info("[getBusidateKey sql is ]:" + sqlCmd + "\n" + "[condition is:]" + map_context.getStringItem("modulecode") + "," + map_context.getStringItem("transcode") + "," + busioper);
        AppLogger.info("[list size is]" + condList.size());
        TCResult tc_result = P_Jdbc.preparedSelect(null, sqlCmd, condList, DBConstant.MAX_ROW);
        JavaList busiDateKeys = null;
        List<?> outputParams = getOutputParams(tc_result);
        // AppLogger.info("\n"+ListUtil.printList(outputParams, null, 0));
        // AppLogger.info("[outputParams]:" + (ListUtil.isNotEmpty(outputParams)
        // ? outputParams.toString() : outputParams));
        if (outputParams != null && outputParams.size() > 0) {
            JavaList resultList = (JavaList) outputParams.get(1);
            if (ListUtil.isNotEmpty(resultList)) {
                busiDateKeys = new JavaList();
                /**
                 * 将业务操作关键字循环放入javaList中
                 */
                for (int i = 0; i < resultList.size(); i++) {
                    JavaList mapsqllist = (JavaList) resultList.get(i);
                    busiDateKeys.add((String) mapsqllist.get(0));
                }
            }
        }
        else {
            throw new BusException(ErrorCodeModule.IMD008, map_context.getStringItem("modulecode") + "|" + map_context.getStringItem("transcode") + "|" + busioper);
        }
        AppLogger.info(ListUtil.printList(busiDateKeys, "[busiDateKeys is]:", CommonConstant.NOR_PRINT));
        return busiDateKeys;

    }

    /**
     * 解析查询返回结果对象
     * 
     * @param tc_result
     * @return
     * @throws BusException
     */
    public static List<?> getOutputParams(TCResult tc_result) throws BusException {
        AppLogger.info("[service return status]:" + tc_result.getStatus());
        List<?> outputParams = null;
        boolean exFlag = false;
        switch (tc_result.getStatus()) {
        case CommonConstant.FAILURE:
            // 失败
            AppLogger.error(tc_result.getErrorCode() + ":" + tc_result.getErrorMsg());
            exFlag = true;
            break;
        case CommonConstant.SUCCESS:
            // 成功
            AppLogger.info("[return success]");
            outputParams = tc_result.getOutputParams();
            break;
        case CommonConstant.NONE:
            // 查询结果为空
            AppLogger.info("[no records]");
            break;
        default:
            AppLogger.error("[beyond return status and original error message is]:" + tc_result.getErrorCode() + tc_result.getErrorMsg());
            exFlag = true;
            break;
        }

        if (exFlag) {
            throw new BusException(tc_result.getErrorCode(), tc_result.getErrorMsg());
        }
        return outputParams;
    }

    /**
     * 获取SQL模板
     * 
     * @param busidatakey
     * @return
     * @throws BusException
     */
    public static List<EntityTAD> getSQLModel(JavaList busidatakey) throws BusException {
    	List<EntityTAD> entitys = null;
        StringBuffer sqlCmd = new StringBuffer();
        sqlCmd.append("select " + DBConstant.T_ARSM_DATAPROCSQL_C + " from " + "AUMS_SYS_DATAPROCSQL_VIEW where busidatakey in (");
        for (int i = 0; i < busidatakey.size(); i++) {
            sqlCmd.append("?");
            if (i != busidatakey.size() - 1) {
                sqlCmd.append(",");
            }
        }
        sqlCmd.append(")");
        /**
         * 获取指定顺序条件
         * 数据操作关键字排序
         */
        sqlCmd.append(getSpecSeqCond(SysConstant.getSysConstant("DatabaseType"), busidatakey, "busidatakey"));
        AppLogger.info("[sql is ]:" + sqlCmd.toString());
        TCResult tc_result = P_Jdbc.preparedSelect(null, sqlCmd.toString(), busidatakey, DBConstant.MAX_ROW);
        List<?> outputParams = getOutputParams(tc_result);
        if (ListUtil.isNotEmpty(outputParams)) {
            JavaList resultList = (JavaList) outputParams.get(1);
            if (ListUtil.isNotEmpty(resultList)) {
            	/**
            	 * 数据库结果集转化为java对象
            	 */
                entitys = EntityUtil.getEntityTAD(resultList);
                if (busidatakey.size() != entitys.size()) {
                    throw new BusException(ErrorCodeModule.IMD013);
                }
            }
            else {
                throw new BusException(ErrorCodeModule.IMD004, "AUMS_SYS_DATAPROCSQL_VIEW");
            }
        }
        else {
            throw new BusException(ErrorCodeModule.IMD004, "AUMS_SYS_DATAPROCSQL_VIEW");
        }
        return entitys;
    }

    /**
     * 获取指定顺序条件
     * 
     * @param dbType
     * @return
     */
    private static String getSpecSeqCond(String dbType, JavaList sequence, String key) {
        AppLogger.info("[dbType is ]:" + dbType);
        StringBuffer strSeq = new StringBuffer();
        for (int i = 0; i < sequence.size(); i++) {
            strSeq.append(sequence.get(i));
            if (i != sequence.size() - 1) {
                strSeq.append(",");
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" order by ");
        if (DBConstant.ORACLE.equals(dbType) || DBConstant.DB2.equals(dbType)) {
            sb.append("instr ('");
            sb.append(strSeq);
            sb.append("',");
            sb.append(key);
            sb.append(")");
        }
        else if (DBConstant.MYSQL.equals(dbType)) {
            sb.append("find_in_set(");
            sb.append(key);
            sb.append(",'");
            sb.append(strSeq);
            sb.append("')");
        }

        return sb.toString();
    }

    /**
     * 反射调用具体处理方法
     * 
     * @throws BusException
     */
    public static TCResult reflectMethod(List<EntityTAD> entitys, String className, JavaDict cond_data_context, JavaDict ext_context, JavaDict map_context) throws BusException {

        TCResult tcResult;
        Class<?> RefClass;
        try {
            RefClass = Class.forName(className);

            Method method = null;
            /**
             * 每次调用方法必须是相同操作类型
             */
            method = RefClass.getMethod(CommonConstant.reflectQM.get(((EntityTAD) entitys.get(0)).getOpertype()), Map.class);

            Map<String, Object> inputMap = new HashMap<String, Object>();
            inputMap.put("entity", entitys);
            inputMap.put("cond_data_context", cond_data_context);
            inputMap.put("ext_context", ext_context);
            inputMap.put("map_context", map_context);
            tcResult = (TCResult) method.invoke(RefClass.newInstance(), inputMap);
        }
        catch (Exception e) {
            AppLogger.error(e);
            throw new BusException(ErrorCodeModule.IMD005);
        }
        return tcResult;
    }
        

	/**
	 * 获取SQL模板
	 * @return
	 * @throws BusException
	 */
	public static List<EntityTAD> getSqlModel(String busidatakey) throws BusException{
		List<EntityTAD> entitys = null;
		StringBuffer sqlCmd = new StringBuffer();
		sqlCmd.append("SELECT ");
		sqlCmd.append(DBConstant.T_ARSM_DATAPROCSQL_C);
		sqlCmd.append(" FROM AUMS_SYS_DATAPROCSQL_VIEW WHERE BUSIDATAKEY = ?");
		AppLogger.info("查询busidatakey的SQL是：" + sqlCmd.toString());
		TCResult tc_result = P_Jdbc.preparedSelect(null, sqlCmd.toString(), new JavaList(busidatakey), 0);
		List<?> outputParams = getOutputParams(tc_result);
		if (ListUtil.isNotEmpty(outputParams)) {
			JavaList resultList = (JavaList) outputParams.get(1);
			if (ListUtil.isNotEmpty(resultList)) {
				/**
				 * 数据库结果集转化为java对象
				 */
				entitys = EntityUtil.getEntityTAD(resultList);
			}else {
				throw new BusException(ErrorCodeModule.IMD013);
			}
		}else {
			throw new BusException(ErrorCodeModule.IMD004, "AUMS_SYS_DATAPROCSQL_VIEW");
		}
		return entitys;
	}
    

}
