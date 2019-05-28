package tc.bank.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.DBConstant;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.domain.EntityTAD;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 实体操作工具类
 * 
 * @author AlanMa
 * 
 */
public class EntityUtil {

    /**
     * 数据库实体转换为Java对象
     * 
     * @param list
     * @param tabColName
     * @return
     * @throws BusException
     */
    @SuppressWarnings("rawtypes")
    public static List<EntityTAD> getEntityTAD(JavaList entitys) throws BusException {
        List<EntityTAD> entityPOJOs = new ArrayList<EntityTAD>();
        try {
            for (int i = 0; i < entitys.size(); i++) {
                JavaList entity = (JavaList) entitys.get(i);
                EntityTAD entityPOJO = new EntityTAD();
                Class cls = (Class) entityPOJO.getClass();
                Field[] fields = cls.getDeclaredFields();
                for (int j = 0; j < entity.size(); j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    field.set(entityPOJO, entity.get(j));
                }
                entityPOJOs.add(entityPOJO);
            }
        }
        catch (Exception e) {
            AppLogger.error(e);
            throw new BusException(ErrorCodeModule.IMD005);
        }
        
        return entityPOJOs;
    }

    /**
     * 获取标准条件语句
     * 
     * @param condition
     * @param dbCondition
     * @param condValue
     * @return
     */
    public static String getStaConditionQ(String condition ,String dbCondition) {
        if (condition == null || dbCondition == null ) {
            return null;
        }
        String[] platColNames = condition.split(":");  //平台对应的条件列
        String[] tabColNames = dbCondition.split(":"); //数据库表对应的条件列
        if (tabColNames.length == 0 || ("".equals(tabColNames[0]))) {
            return null;
        }
        if(platColNames.length == 0 || platColNames.length != tabColNames.length){
        	return null;
        }
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < tabColNames.length; i++) {
            strBuf.append(tabColNames[i]);
            strBuf.append("=?");
            /**
             * 如果不是最后一个字段，则使用逻辑连接符 and 拼接
             */
            if (i + 1 != tabColNames.length) {
                strBuf.append(" and ");
            }
        }
        AppLogger.info(strBuf.toString());
        return strBuf.toString();
    }

    /**
     * 获取标准SQL条件值集合
     * 
     * @param ddCond
     * @param condValue
     * @return
     */
    public static JavaList getCondValues(String ddCond, JavaDict condValue) {
        if (ddCond == null) {
            return null;
        }
        String[] ddCondNames = ddCond.split(":");
        if (ddCondNames.length == 0) {
            return null;
        }
        JavaList condValues = new JavaList();

        for (int i = 0; i < ddCondNames.length; i++) {
            if (condValue.get(ddCondNames[i]) != null || !"".equals(condValue.get(ddCondNames[i]))) {
                condValues.add(condValue.get(ddCondNames[i]));
            }else{
            	 condValues.add("''");
            }
        }
        return condValues;
    }

    /**
     * 获取更新条件值<br>
     * 例：<br>
     * mesgdate:mesgserno 【20151218】【0000000432】<br>
     * [[mesgdate, =, 20151218, and], [mesgserno, =, 0000000432]]<br>
     * 
     * @param ddCond
     * @param condValue
     * @return
     */
    public static JavaList getCondValuesU(String ddCond, String dbCond, JavaDict condValue) {
        if (ddCond == null) {
            return null;
        }
        String[] ddCondNames = ddCond.split(":");
        String[] dbCondNames = dbCond.split(":");
        if (ddCondNames.length == 0) {
            return null;
        }
        JavaList condValues = new JavaList();
        if (ddCondNames.length == 1) {
            condValues.add(dbCondNames[0]);
            condValues.add("=");
            condValues.add(condValue.get(ddCondNames[0]));
        }
        else if (ddCondNames.length > 1) {
            for (int i = 0; i < ddCondNames.length; i++) {
                JavaList subCondValues = new JavaList();
                subCondValues.add(dbCondNames[i]);
                subCondValues.add("=");
                subCondValues.add(condValue.get(ddCondNames[i]));
                if (i != ddCondNames.length - 1) {
                    subCondValues.add("and");
                }
                condValues.add(subCondValues);
            }
        }

        return condValues;
    }

    /**
     * 获取插入语句的值
     * 
     * @param ddCond
     * @param condValue
     * @return
     */
    public static JavaList getModifyValues(String dbColNames, String ddColNames, JavaDict context) {
        String[] tabColNamesDD = ddColNames.split(",");
        String[] tabColNamesDB = dbColNames.split(",");
        JavaList retCondValues = new JavaList();
        for (int i = 0; i < tabColNamesDD.length; i++) {
            if (context.get(tabColNamesDD[i]) != null) {
                JavaList condValues = new JavaList();
                condValues.add(tabColNamesDB[i]);
                condValues.add(context.get(tabColNamesDD[i]));
                retCondValues.add(condValues);
            }
        }

        return retCondValues;
    }

    /**
     * 获取查询的列名称
     * 
     * @param dataDic
     * @param dbCols
     * @return
     * @throws BusException
     */
    public static String getColNames(String dataDic, String dbCols) throws BusException {
        if (dataDic == null || dbCols == null) {
            throw new BusException(ErrorCodeModule.IMD006);
        }
        String[] tabColNamesDD = dataDic.split(",");
        String[] tabColNamesDB = dbCols.split(",");
        StringBuffer colName = new StringBuffer();
        for (int i = 0; i < tabColNamesDD.length; i++) {
            colName.append(tabColNamesDB[i]);
            colName.append(" ");
            colName.append(tabColNamesDD[i]);
            if (++i != tabColNamesDD.length) {
                colName.append(", ");
            }
            --i;
        }
        
        return colName.toString();
    }

    /**
     * 获取JDBC中SQL
     * 替换%s为问号
     * @param sql
     * @return
     */
    public static String getPSSQL(String sql) {
    	//如果数据库字段为varchar类型的，使用'%s',需要加单引号
        String retSQL1 = sql.replaceAll("'%s'", "?");
        //整数或数值类型，使用%s，不加单引号
        String retSQL2 = retSQL1.replace("%s", "?");
        /**
         * 使用like关键字(以前后%为例)
         * python: like '%%%s%%' ,前后必须再加%，进行转义
         * java: like '%%s%'或 like '%%%s%%' ,两种都可以
         */
        String retSQL3 = retSQL2.replace("%%", "%");
        
        return retSQL3;
    }

    /**
     * 组装查询扩展条件
     * 
     * @param dyncondlist
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map getExtCondition(List dyncondlist) {
    	/*
    	 * [
    		[
    		  ["a", "in", "3,2,1", "and"],["b", "is", "Null"], "or"
    		],
    		[
    		 ["c", "=",  None, "and"], ["b", "!=", None]
    		]
    	  ]
    	  转化成
    	  (a in('3','2','1') and b is Null) or (c=None and b != None)
    	*/
        Map outMap = new HashMap();
        StringBuffer extCond = new StringBuffer();
        List condValues = new JavaList();
        Boolean isAddBrcFront = false;
        Boolean isAddBrcEnd = false;
        for (int i = 0; i < dyncondlist.size(); i++) {
            int length = dyncondlist.size();
            if ("or".equals(dyncondlist.get(length - 1)) || "and".equals(dyncondlist.get(length - 1))) {
                if (dyncondlist.get(length - 2) instanceof List) {
                    // 子条件逻辑关键字 前后加括号
                    isAddBrcFront = true;
                    isAddBrcEnd = true;
                }
            }
            Object subElement = dyncondlist.get(i);
            if (subElement instanceof List) {
                if (isAddBrcFront) {
                    isAddBrcFront = false;
                    extCond.append("(");
                }
                Map retMap = getExtCondition((List) subElement);
                extCond.append(retMap.get("extCond"));
                condValues.addAll((List) retMap.get("condValues"));
                if (i == dyncondlist.size() - 1 && isAddBrcEnd) {
                    isAddBrcEnd = false;
                    extCond.append(")");
                }
            }
            if (subElement instanceof String) {
                if ("or".equals(subElement) || "and".equals(subElement)) {
                    extCond.append(" " + subElement + " ");
                }
                if (isAddBrcFront) {
                    isAddBrcFront = false;
                    extCond.append("(");
                }
                /**
                 * 组装查询扩展子条件
                 */
                Map retMap = getSubExtCond(dyncondlist);
                extCond.append(retMap.get("extCond"));
                condValues.addAll((List) retMap.get("condValues"));
                if (i == dyncondlist.size() - 1 && isAddBrcEnd) {
                    isAddBrcEnd = false;
                    extCond.append(")");
                }
                break;
            }
        }
        outMap.put("extCond", extCond.toString());
        outMap.put("condValues", condValues);
        
        return outMap;
    }

    /**
     * 组装查询扩展子条件
     * 
     * @param dyncondlist
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map getSubExtCond(List dyncondlist) {
        Map outMap = new HashMap();
        List condValues = new ArrayList();
        StringBuffer extCond = new StringBuffer();

        if (dyncondlist.size() != 3 && dyncondlist.size() != 4) {
            AppLogger.error("[====条件非法====]");
        }

        if ("in".equals(dyncondlist.get(1))) {
            AppLogger.info("[in]条件构造");
            extCond.append(" " + dyncondlist.get(0) + " ");
            extCond.append(dyncondlist.get(1));
            extCond.append(" (");
            /**
             * 获取动态子条件的?占位符 	
             */
            extCond.append(getOperInCondtion((String) dyncondlist.get(2)));
            extCond.append(") ");
            /**
             * 获取动态子条件的?占位符具体的值
             */
            condValues.addAll(getOperInValues((String) dyncondlist.get(2)));
        }
        else
        if ("is".equals(dyncondlist.get(1))) {
            AppLogger.info("[is]条件构造");
            extCond.append(" " + dyncondlist.get(0) + " ");
            extCond.append(dyncondlist.get(1));
            extCond.append(" ");
            extCond.append(dyncondlist.get(2));
        }
        else
        if (DBConstant.COND_SYMBOLS.contains(dyncondlist.get(1))) {
            AppLogger.info("[" + dyncondlist.get(1) + "]" + "条件构造");
            extCond.append(" " + dyncondlist.get(0) + " ");
            extCond.append(dyncondlist.get(1));
            extCond.append(" ? ");
            condValues.add(dyncondlist.get(2));
        }
        else {
            AppLogger.info("无法识别操作标识：" + dyncondlist.get(1));
        }

        if (dyncondlist.size() == 4) {
            extCond.append(" " + dyncondlist.get(3) + " ");
        }

        outMap.put("extCond", extCond.toString());
        outMap.put("condValues", condValues);
        
        return outMap;

    }
    
    /**
     * 获取动态子条件的?占位符
     * @param conditon
     * @return
     */
    private static String getOperInCondtion(String conditon) {
        StringBuffer str = new StringBuffer();
        String[] cond = conditon.split(",");
        for (int i = 0; i < cond.length; i++) {
            str.append("?");
            if (i != cond.length - 1) {
                str.append(",");
            }
        }
        
        return str.toString();
    }
    
    /**
     * 获取动态子条件的?占位符具体的值
     * @param conditon
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List getOperInValues(String conditon) {
        List retList = new ArrayList();
        String[] cond = conditon.split(",");
        for (int i = 0; i < cond.length; i++) {
            retList.add(cond[i]);
        }
        
        return retList;
    }

    /**
     * 通过条件获取排序语句
     * 
     * @param orderlist
     * @return
     */
    public static String getOderByCond(JavaList orderlist) {
        StringBuffer extCond = new StringBuffer();
        extCond.append(" order by ");
        JavaList cols = ((JavaList) orderlist.get(0));
        JavaList arrLogo = ((JavaList) orderlist.get(1));
        for (int i = 0; i < cols.size(); i++) {
            extCond.append(cols.get(i));
            extCond.append(" ");
            extCond.append(arrLogo.get(i));
            if (++i != cols.size()) {
                extCond.append(", ");
            }
            --i;
        }
        AppLogger.info("[ext oder by condition is :]" + extCond.toString());
        
        return extCond.toString();
    }

    /**
     * 获取分页属性<br>
     * 
     * @param pagelist
     * @return
     */
    public static int[] getPageParams(JavaList origPagelist) {
        int[] pageParam = new int[2];
        JavaList pagelist = new JavaList();
        if (origPagelist.get(0) instanceof String) {
            for (int i = 0; i < origPagelist.size(); i++) {
                pagelist.add(Integer.parseInt((String) origPagelist.get(i)));
            }
        }
        else {
            pagelist.addAll(origPagelist);
        }
        pageParam[1] = (Integer) pagelist.get(2);
        int pageOper = (Integer) pagelist.get(0);
        switch (pageOper) {
        case 1:
            // 最后一页:-1
            pageParam[0] = -1;
            break;
        case 2:
            // 上一页:(当前页面码-2)*每页的记录数+1
            pageParam[0] = ((Integer) pagelist.get(1) - 2) * (Integer) pagelist.get(2) + 1;
            break;
        case 3:
            // 下一页:当前页面码*每页的记录数+1
            pageParam[0] = (Integer) pagelist.get(1) * (Integer) pagelist.get(2) + 1;
            break;
        case 4:
            // 当前页:(当前页面码-1)*每页的记录数+1
            pageParam[0] = ((Integer) pagelist.get(1) - 1) * (Integer) pagelist.get(2) + 1;
            break;

        default:
            break;
        }

        return pageParam;
    }

    /**
     * 获取SQL
     * 替换?为具体的值
     * @param sqlstr
     * @param condList
     * @return
     */
    public static String getSQLCmd(String sqlstr, JavaList condList) {
        for (int i = 0; i < condList.size(); i++) {
            if (condList.get(i) instanceof String) {
                sqlstr.replaceFirst("?", "'" + (String) condList.get(i) + "'");
            }
            if (condList.get(i) instanceof Integer) {
                sqlstr.replaceFirst("?", String.valueOf((Integer) condList.get(i)));
            }
        }

        return sqlstr;
    }

    /**
     * 格式化查询出参
     * 
     * @param tc_result_trans
     * @param infocols
     * @return
     */
    public static JavaList formateOutParams(List<?> outPutParams, String infocols) {
        AppLogger.info(ListUtil.printList(outPutParams, "[outPutParams]:", CommonConstant.NOR_PRINT));
        AppLogger.info("[infocols]:" + infocols);
        String[] tabColNamesDD = infocols.split(",");
        JavaList outPutParamsNew = new JavaList();

        for (int i = 0; i < outPutParams.size(); i++) {
            JavaDict rowMap = new JavaDict();
            List<?> rowList = (List<?>) outPutParams.get(i);
            for (int colNum = 0; colNum < rowList.size(); colNum++) {
                if (rowList.get(colNum) == null) {
                    rowMap.put(tabColNamesDD[colNum], "");
                }
                else {
                    rowMap.put(tabColNamesDD[colNum], rowList.get(colNum));
                }
            }
            outPutParamsNew.add(rowMap);
        }

        return outPutParamsNew;
    }

    /**
     * 转换查询结果集
     * 
     * @param tc_result_trans
     * @param infocols
     * @return
     */
    public static TCResult getQueryTCR(TCResult tc_result_trans, String infocols) {
        if (CommonConstant.SUCCESS == tc_result_trans.getStatus()) {
        	/**
        	 * 格式化查询出参
        	 * formateOutParams
        	 */
            return TCResult.newSuccessResult(formateOutParams((List<?>) tc_result_trans.getOutputParams().get(1), infocols), tc_result_trans.getOutputParams().get(0),
                    tc_result_trans.getOutputParams().get(2), tc_result_trans.getOutputParams().get(3), tc_result_trans.getOutputParams().get(4));
        }
        else {
            return tc_result_trans;
        }
    }

    /**
     * 获取扩展条件相关信息<br>
     * hasExtCond-是否有扩展条件<br>
     * sqlCmd-SQL条件<br>
     * condValues-条件属性值<br>
     * pageParams-分页条件<br>
     * 
     * @param inputMap
     * @param sqlCmdStr
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map getExtCondInfo(Map<String, Object> inputMap, String sqlCmdStr) {
        //AppLogger.info("[sqlCmd before where]:" + sqlCmdStr);
       // AppLogger.info("[inputMap]:" + inputMap.toString());
        JavaList condValues = null;
        String extCondStr = null;
        StringBuffer sqlCmd = new StringBuffer();
        String orderByStr = null;
        int[] pageParams = null;
        String hasExtCond = CommonConstant.FALSE;
        Map returnMap;
        // [0]-查询条件 [1]-排序条件 [2]-分页条件
        int[] emptyFlag = CheckUtil.isExtCondNotEmpty(inputMap);
        JavaDict extCond = (JavaDict) inputMap.get("ext_context");
        if (emptyFlag[0] == 1) {
            JavaList dyncondlist = (JavaList) extCond.get("dyncondlist");
            /**
             * 组装查询扩展条件
             */
            Map map = getExtCondition(dyncondlist);
            extCondStr = (String) map.get("extCond");
            condValues = (JavaList) map.get("condValues");
            if (sqlCmdStr.indexOf(" where ") == -1) {
                sqlCmd.append(" where ");
            }
            sqlCmd.append(extCondStr);
            hasExtCond = CommonConstant.TRUE;
        }
        if (emptyFlag[1] == 1) {
            JavaList orderlist = (JavaList) extCond.get("orderlist");
            /**
             * 通过条件获取排序语句
             */
            orderByStr = getOderByCond(orderlist);
            sqlCmd.append(" ");
            sqlCmd.append(orderByStr);
            hasExtCond = CommonConstant.TRUE;
        }
        if (emptyFlag[2] == 1) {
            JavaList pagelist = (JavaList) extCond.get("pagelist");
            /**
             * 获取分页属性
             */
            pageParams = getPageParams(pagelist);
            hasExtCond = CommonConstant.TRUE;
        }

        //AppLogger.info("[hasExtCond]:" + hasExtCond);
        //AppLogger.info("[sqlCmd extcondition]:" + sqlCmd.toString());
        //AppLogger.info("[condValues]:" + (ListUtil.isNotEmpty(condValues) ? condValues.toString() : condValues));
        //AppLogger.info("[pageParams]:" + (pageParams == null ? pageParams : (pageParams[0] + "," + pageParams[1])));
        returnMap = new HashMap();
        returnMap.put(CommonConstant.HAS_EXT_COND, hasExtCond);
        returnMap.put("sqlCmd", sqlCmd.toString());
        returnMap.put("condValues", condValues);
        returnMap.put("pageParams", pageParams);
        return returnMap;
    }

}
