package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.DBConstant;
import tc.bank.domain.EntityTAD;
import tc.bank.utils.CheckUtil;
import tc.bank.utils.DBBusiUtil;
import tc.bank.utils.DBTechUtil;
import tc.bank.utils.EntityUtil;
import tc.bank.utils.ListUtil;
import tc.bank.utils.RetResultUtil;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 数据统一操作类
 * 
 * @author AlanMa
 * @date 2015-12-03 15:51:13
 */
@ComponentGroup(level = "银行", groupName = "数据库操作")
public class B_DBUnityRptOper {

	/**
	 * @category 数据查询类统一接口
	 * @param cond_data_context
	 *            入参|条件数据容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param busioper
	 *            入参|业务操作关键字|{@link java.lang.String}
	 * @param ext_context
	 *            入参|扩展参数字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param map_context
	 *            入参|数据操作映射数据容器(业务扩展信息）|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param out_context
	 *            出参|获取到的数据字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param rownum
	 *            出参|查询到的数据条数|{@link int}
	 * @param totalrownum
	 *            出参|总记录数|{@link int}
	 * @param totalpagenum
	 *            出参|总页数|{@link int}
	 * @param nowpagenum
	 *            出参|当前页码|{@link int}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 查询类无记录<br/>
	 */
	@InParams(param = {
			@Param(name = "cond_data_context", comment = "条件数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "busioper", comment = "业务操作关键字", type = java.lang.String.class),
			@Param(name = "ext_context", comment = "扩展参数字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "map_context", comment = "数据操作映射数据容器(业务扩展信息）", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = {
			@Param(name = "out_context", comment = "获取到的数据字典", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "rownum", comment = "查询到的数据条数", type = int.class),
			@Param(name = "totalrownum", comment = "总记录数", type = int.class),
			@Param(name = "totalpagenum", comment = "总页数", type = int.class),
			@Param(name = "nowpagenum", comment = "当前页码", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "查询类无记录") })
	@Component(label = "数据查询类统一接口", style = "选择型", type = "同步组件", comment = "对数据库查询类操作接口\r\next_context为扩展参数字典,里面现在有如下KEY\r\norderlist为排序数据信息,格式为[['排序字段1,排序字段2,...,排序字段n'], ['ASC/DESC','ASC/DESC',...,'ASC/DESC']]\r\npagelist为分页获取业务数据信息，格式为[分页标识,当前页面码,每页的记录数];(分页标识:1-最后一页,2-上一页,3-下一页,4-当前页)\r\ndyncondlist格式为动态条件的嵌套list,如[[[\"a\", \"in\", \"'3','2','1'\", \"and\"],[\"b\", \"is\", \"Null\"], \"or\"], [[\"c\", \"=\",  None, \"and\"], [\"b\", \"!=\", None]]],表示 (a in('3','2','1') and b is null) or (c=cond_data_context['c'] and b != cond_data_context['b'])", date = "2016-03-25 10:07:34")
	public static TCResult B_DBUnityRptOpr(JavaDict cond_data_context,
			String busioper, JavaDict ext_context, JavaDict map_context) {
		TCResult tcResult;
		try {
			AppLogger.info("【enter B_DBUnityRptOpr】");
			//校验入参是否为空
			CheckUtil.checkInpParamDB(cond_data_context, busioper, ext_context,map_context);
			AppLogger.info("pass input param check");
			//获取数据操作关键字
			JavaList busidatakeys = DBBusiUtil.getBusidateKey(busioper,
					map_context);
			//获取sql模板，转化为实体对象，以便通过get/set方法取值，而不是通过集合的下标取值
			List<EntityTAD> entitys = DBBusiUtil.getSQLModel(busidatakeys);
			//通过模板配置的操作类型，利用反射调用具体的查询方法
			tcResult = DBBusiUtil.reflectMethod(entitys,"tc.bank.product.B_DBUnityRptOper", 
												cond_data_context, ext_context,map_context);
		} catch (BusException e) {
			AppLogger.error(e);
			return RetResultUtil.getTCResToExternal(e);
		}
		//如果无记录或系统异常，设置平台内部错误码
		TCResult tcResultRet = RetResultUtil.getTCResToExternal(tcResult);
		//树形打印结果集
		//RetResultUtil.printTCResult(tcResultRet);
		//返回最终的结果集
		return tcResultRet;
	}

    /**
     * S-标准查询<br>
     * 查询条件可配置，数据库配置条件/扩展(优先)
     * 
     * @param entity
     * @return
     * @throws BusException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static TCResult standQuery(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityRptOper.standQuery]");
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        /**一次性只能配置一条查询模板，不可以配置多条*/
        EntityTAD entity = (EntityTAD) entitys.get(0);
        JavaDict condValue = (JavaDict) inputMap.get("cond_data_context");
        JavaList condList = new JavaList();
        int[] pageParams = null;
        
        
        //获取扩展字段数据
        JavaDict coutflagdata= (JavaDict)condValue.get("coutflagdata");
        AppLogger.debug("汇总标识查询数据[coutflagdata]:" + coutflagdata);
        
        // 组装SQL
        StringBuffer sqlCmd = new StringBuffer();
        String colum = EntityUtil.getColNames(entity.getInfocols(), entity.getDbinfocols());
        String tableName = entity.getProctablename();

        sqlCmd.append("select ");
        sqlCmd.append(colum);
        sqlCmd.append(" from ");
        sqlCmd.append(tableName);
        /**组装动态条件*/
        Map extCondInfo = EntityUtil.getExtCondInfo(inputMap, sqlCmd.toString());
        /**
         * S-标准查询，动态条件和数据库条件字段只能选择一种（并的关系）
         * 如果有扩展条件，忽略数据库条件字段，优先选择扩展条件
         * 如果没有扩展条件，则选择标准条件
         */
        if (!CommonConstant.TRUE.equals((String) inputMap.get(CommonConstant.IS_FROM_SA))) {
            // 标准-standQuery
            if (CommonConstant.TRUE.equals((String) extCondInfo.get(CommonConstant.HAS_EXT_COND))) {
                // 有扩展条件
                sqlCmd.append(extCondInfo.get("sqlCmd"));
                if ((JavaList) extCondInfo.get("condValues") != null) {
                    condList.addAll((JavaList) extCondInfo.get("condValues"));
                }
                pageParams = (int[]) extCondInfo.get("pageParams");
            }
            else {
                // 标准条件
                String condition = EntityUtil.getStaConditionQ(entity.getCondcols(),entity.getDbcondcols());
                sqlCmd.append(" where ");
                sqlCmd.append(condition);
                JavaList condValuesList = EntityUtil.getCondValues(entity.getCondcols(), condValue);
                if(ListUtil.isNotEmpty(condValuesList)){
                    condList.addAll(condValuesList);
                }
            }
        }
        /**
         * SA-动态条件查询，动态条件和数据库条件字段同时作为查询条件（或的关系）
         */
        else {
            // 动态-extStdQry
            // 标准条件
            String condition = EntityUtil.getStaConditionQ(entity.getCondcols(),entity.getDbcondcols());
            sqlCmd.append(" where ");
            sqlCmd.append(condition);
            condList.addAll(EntityUtil.getCondValues(entity.getCondcols(), condValue));
            // 扩展条件
            if (CommonConstant.TRUE.equals((String) extCondInfo.get(CommonConstant.HAS_EXT_COND))) {
                // 有扩展条件
                sqlCmd.append(extCondInfo.get("sqlCmd"));
                if ((JavaList) extCondInfo.get("condValues") != null) {
                    condList.addAll((JavaList) extCondInfo.get("condValues"));
                }
                pageParams = (int[]) extCondInfo.get("pageParams");
            }
        }
        /**
         * 判断分页条件是否为空
         */
        if (pageParams == null) {
            pageParams = new int[2];
            pageParams[0] = DBConstant.START_ROW_ALL;
            //高鑫修改20180705，全部为全量
          /*  if (CommonConstant.TRUE.equals((String) inputMap.get(CommonConstant.IS_QUERY_ALL))) {
                pageParams[0] = DBConstant.START_ROW_ALL;
            }
            //不是全量，起始数=1，最大数=20
            else {
                pageParams[0] = DBConstant.START_ROW;
                pageParams[1] = DBConstant.MAX_ROW;
            }
            */
        }

        AppLogger.debug("[preparedSelectPage SQL]:" + sqlCmd.toString());
        AppLogger.debug(ListUtil.printList(condList, "[condition value]:", CommonConstant.NOR_PRINT));
        AppLogger.debug("[pageParams]:" + pageParams[0] + "," + pageParams[1]);
        /**
         * 调用预编译分页查询
         */
        TCResult tc_result_trans = DBTechUtil.preparedSelectPage(null, sqlCmd.toString(), condList, pageParams[0], pageParams[1],coutflagdata);
        AppLogger.info("[preparedSelectPage return status]:" + tc_result_trans.getStatus());
        /**
         * 转换查询结果集，以List<Map<String,Object>>返回
         * 方便通过Map的键取值
         */
        return EntityUtil.getQueryTCR(tc_result_trans, entity.getInfocols());
    }

    /**
     * SA-动态条件查询<br>
     * 查询结果列可配置，传入查询条件，数据库配置条件 and 扩展(如果有)
     * 
     * @param entity
     * @param condValue
     * @return
     * @throws BusException
     */

    public static TCResult extStdQry(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityRptOper.extStaQry]");
        inputMap.put(CommonConstant.IS_FROM_SA, CommonConstant.TRUE);
        return standQuery(inputMap);
    }

    /**
     * GA-全量查询
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    public static TCResult extStdQryAll(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityRptOper.extStdQryAll]");
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.putAll(inputMap);
        requestMap.put(CommonConstant.IS_QUERY_ALL, CommonConstant.TRUE);
        return extStdQry(requestMap);
    }

    /**
     * SL-手工SQL查询类
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TCResult handworkQry(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityRptOper.handworkQry]");
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        /**一次性只能配置一条查询模板，不可以配置多条*/
        EntityTAD entity = (EntityTAD) entitys.get(0);
        JavaDict cond_data_context = (JavaDict) inputMap.get("cond_data_context");
        StringBuffer sqlCmd = new StringBuffer();
        /**
         * 替换手工sql中的%s为?
         */
        sqlCmd.append(EntityUtil.getPSSQL(entity.getSqlstr()));
        int[] pageParams = null;

        /**
         * 组装扩展条件
         * 手工类型的sql，不支持传入排序和动态条件
         * 支持传入分页条件
         */
        Map extCondInfo = EntityUtil.getExtCondInfo(inputMap, sqlCmd.toString());
        if (CommonConstant.TRUE.equals(extCondInfo.get(CommonConstant.HAS_EXT_COND))) {
            pageParams = (int[]) extCondInfo.get("pageParams");
        }
        JavaList condList = EntityUtil.getCondValues(entity.getCondcols(), cond_data_context);
        /**
         * 如果分页条件为空
         */
        if (pageParams == null) {
            pageParams = new int[2];
            //如果是GL全量手工查询，起始数=-1
            if (CommonConstant.TRUE.equals((String) inputMap.get(CommonConstant.IS_QUERY_ALL))) {
                pageParams[0] = DBConstant.START_ROW_ALL;
            }
            //如果不是GL，起始数=1，最大数=20
            else {
                pageParams[0] = DBConstant.START_ROW;
                pageParams[1] = DBConstant.MAX_ROW;
            }
        }

        AppLogger.debug("[SQL]:" + sqlCmd.toString());
        AppLogger.debug(ListUtil.printList(condList, "[condition value]:", CommonConstant.NOR_PRINT));
        AppLogger.debug("[pageParams]:" + pageParams[0] + "," + pageParams[1]);
        /**
         * 预编译分页查询
         */
        TCResult tc_result = DBTechUtil.preparedSelectPage(null, sqlCmd.toString(), condList, pageParams[0], pageParams[1],null);

        AppLogger.info("[preparedSelectPage return status]:" + tc_result.getStatus());
        /**
         * 转换查询结果集，以List<Map<String,Object>>返回
         * 方便通过Map的键取值
         */
        return EntityUtil.getQueryTCR(tc_result, entity.getInfocols());
    }

    /**
     * GL-全量手工SQL查询类
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    public static TCResult handwkQryAll(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityRptOper.handwkQryAll]");
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.putAll(inputMap);
        requestMap.put(CommonConstant.IS_QUERY_ALL, CommonConstant.TRUE);
        return handworkQry(requestMap);
    }

    /**
     * PL-JDBC查询
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    @SuppressWarnings("unchecked")
    public static TCResult queryJDBC(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityRptOper.queryJDBC]");
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        EntityTAD entity = (EntityTAD) entitys.get(0);
        JavaDict cond_data_context = (JavaDict) inputMap.get("cond_data_context");
        String sqlstr = EntityUtil.getPSSQL(entity.getSqlstr());

        JavaList condList = EntityUtil.getCondValues(entity.getCondcols(), cond_data_context);
        String sqlCmd = EntityUtil.getSQLCmd(sqlstr, condList);

        AppLogger.debug("[SQL]:" + sqlCmd);
        AppLogger.debug(ListUtil.printList(condList, "[condition value]:", CommonConstant.NOR_PRINT));

        TCResult tc_result = P_Jdbc.dmlSelect(null, sqlCmd, 0);
        AppLogger.info("[P_Jdbc.executeSQL return status]:" + tc_result.getStatus());

        return EntityUtil.getQueryTCR(tc_result, entity.getInfocols());
    }
    
	/**
	 * @category 通用查询
	 * @param cond_text
	 *            入参|数据容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param busidatakey
	 *            入参|业务操作关键字|{@link java.lang.String}
	 * @param ext_context
	 *            入参|扩展数据容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param out_context
	 *            出参|获取到的数据列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param rownum
	 *            出参|查询到的数据条数|int
	 * @param totalrownum
	 *            出参|总记录数|int
	 * @param totalpagenum
	 *            出参|总页数|int
	 * @param nowpagenum
	 *            出参|当前页码|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 查询类无记录<br/>
	 */
	@InParams(param = {
			@Param(name = "cond_text", comment = "数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "busidatakey", comment = "业务操作关键字", type = java.lang.String.class),
			@Param(name = "ext_context", comment = "扩展数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = {
			@Param(name = "out_context", comment = "获取到的数据列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "rownum", comment = "查询到的数据条数", type = int.class),
			@Param(name = "totalrownum", comment = "总记录数", type = int.class),
			@Param(name = "totalpagenum", comment = "总页数", type = int.class),
			@Param(name = "nowpagenum", comment = "当前页码", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "查询类无记录") })
	@Component(label = "通用查询", style = "选择型", type = "同步组件", comment = "通用查询", date = "2016-12-01 10:21:31")
	public static TCResult B_DBComQue(JavaDict cond_text, String busidatakey,
			JavaDict ext_context) {
		TCResult tcResult;
		JavaDict map_context = new JavaDict();
		try {
			/**通过数据关键字获取SQL模板*/
			List<EntityTAD> entitys = DBBusiUtil.getSqlModel(busidatakey);
			tcResult = DBBusiUtil.reflectMethod(entitys,
					"tc.bank.product.B_DBUnityRptOper", cond_text, ext_context,
					map_context);
		} catch (BusException e) {
			AppLogger.error(e);
			return RetResultUtil.getTCResToExternal(e);
		}
		TCResult tcResultRet = RetResultUtil.getTCResToExternal(tcResult);
		RetResultUtil.printTCResult(tcResultRet);
		return tcResultRet;
	}
    


	/**
	 * @param inContext
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param ignoreValue
	 *            入参|忽略值|
	 *            {@link java.lang.String.class}           
	 * @param paramList
	 *            入参|变量赋值列表，如：[[key1,value1],[key2,value2],[key3,value3]...]|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "inContext", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "ignoreValue", comment = "忽略值", type = java.lang.String.class),
			@Param(name = "paramList", comment = "变量赋值列表,值为string时赋值等号，值为list时赋值in，如：[value1,value2]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = {})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "组合条件查询Dyncondlist创建", style = "判断型", type = "同步组件", comment = "组合条件查询Dyncondlist创建", date = "Thu Jul 02 11:48:56 CST 2015")
	public static TCResult B_Dyncondlist_Create(JavaDict inContext , String ignoreValue ,JavaList paramList) {
		if (paramList == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "入参变量赋值列表不能为空");
		}
		JavaList dyncondList = new JavaList();//.add(valueItem);
		JavaList dynconditem = new JavaList();
		dynconditem.add("1");
		dynconditem.add("=");
		dynconditem.add("1");
		dynconditem.add("and");
		dyncondList.add(dynconditem);
		for (int i = 0; i < paramList.size(); i++) {
			Object param = paramList.getItem(i);
			if (!(param instanceof JavaList)) {
				return TCResult.newFailureResult(ErrorCode.AGR,
						"入参列表子元素必须是列表");
			}

			JavaList param0 = (JavaList) param;
			dynconditem = new JavaList();
			if (!(param0.getItem(1)==null||param0.getItem(1).equals(ignoreValue)))
			{
			if(param0.getItem(1) instanceof JavaList&&(param0.getItem(1) instanceof JavaList&&(((JavaList) param0.getItem(1)).size()!=0)))
			{
				//公共
				dynconditem.add(param0.getItem(0));
				//公共结束
				JavaList paramcondlist = param0.getItem(1);
				dynconditem.add("in");
				String Condlist = "";
				for(int j=0;j<paramcondlist.size();j++){
					//Condlist+="\'";
				    Condlist+=paramcondlist.get(j);
				    //Condlist+="\'";
				    Condlist+=",";
				}
				Condlist=Condlist.substring(0,Condlist.length() - 1);
				dynconditem.add(Condlist);
				//公共
				dynconditem.add("and");
				AppLogger.info("新建节点:" + dynconditem.toString());
				dyncondList.add(dynconditem);
				//公共结束
			}else if(param0.getItem(1) instanceof String&&param0.getItem(1)!="")
			{
				//公共
				dynconditem.add(param0.getItem(0));
				//公共结束
				if(param0.size()==2)
				{
					dynconditem.add("=");
					dynconditem.add(param0.getItem(1));
				}
				else if(param0.size()==3)
				{
					ArrayList<String> conditionSign = new ArrayList<String>(Arrays.asList("<",">","<=",">="));
					if(conditionSign.contains((String)param0.getItem(2))){
						dynconditem.add(param0.getItem(2));
						dynconditem.add(param0.getItem(1));
					}
				}
				//公共
				dynconditem.add("and");
				AppLogger.info("新建节点:" + dynconditem.toString());
				dyncondList.add(dynconditem);
				//公共结束
			}

			}
		}
		int dyncondListlast=dyncondList.size()-1;
		if (dyncondListlast > -1)
		{
			((JavaList)dyncondList.get(dyncondListlast)).remove(3);
		}
		AppLogger.info("新建列表:" + dyncondList.toString());
		//(JavaList)dyncondList[dyncondListsize].remove();
		String keyname = "dyncondlist";
		inContext.setItem(keyname, dyncondList);
		AppLogger.info("容器对象:" + inContext.toString());
		return TCResult.newSuccessResult();
	}
    

	/**
	 * @param inContext
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param ignoreValue
	 *            入参|忽略值|
	 *            {@link java.lang.String.class}           
	 * @param paramList
	 *            入参|变量赋值列表，如：[[key1,value1],[key2,value2],[key3,value3]...]|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "inContext", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "ignoreValue", comment = "忽略值", type = java.lang.String.class),
			@Param(name = "paramList", comment = "变量赋值列表,值为string时赋值等号，值为list时赋值in，如：[value1,value2]", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = {})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "组合条件查询Dyncondlist创建2----list拼为字符串对象用", style = "判断型", type = "同步组件", comment = "组合条件查询Dyncondlist创建2----list拼为字符串对象用", date = "Thu Jul 02 11:48:56 CST 2015")
	public static TCResult B_Dyncondlist_Create2(JavaDict inContext , String ignoreValue ,JavaList paramList) {
		if (paramList == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "入参变量赋值列表不能为空");
		}
		JavaList dyncondList = new JavaList();//.add(valueItem);
		JavaList dynconditem = new JavaList();
		dynconditem.add("1");
		dynconditem.add("=");
		dynconditem.add("1");
		dynconditem.add("and");
		dyncondList.add(dynconditem);
		for (int i = 0; i < paramList.size(); i++) {
			Object param = paramList.getItem(i);
			if (!(param instanceof JavaList)) {
				return TCResult.newFailureResult(ErrorCode.AGR,
						"入参列表子元素必须是列表");
			}

			JavaList param0 = (JavaList) param;
			dynconditem = new JavaList();
			if (!(param0.getItem(1)==null||param0.getItem(1).equals(ignoreValue)))
			{
			if(param0.getItem(1) instanceof JavaList&&(param0.getItem(1) instanceof JavaList&&(((JavaList) param0.getItem(1)).size()!=0)))
			{
				//公共
				dynconditem.add(param0.getItem(0));
				//公共结束
				JavaList paramcondlist = param0.getItem(1);
				dynconditem.add("in");
				String Condlist = "";
				for(int j=0;j<paramcondlist.size();j++){
					Condlist+="\'";
				    Condlist+=paramcondlist.get(j);
				    Condlist+="\'";
				    Condlist+=",";
				}
				Condlist=Condlist.substring(0,Condlist.length() - 1);
				dynconditem.add(Condlist);
				//公共
				dynconditem.add("and");
				AppLogger.info("新建节点:" + dynconditem.toString());
				dyncondList.add(dynconditem);
				//公共结束
			}else if(param0.getItem(1) instanceof String&&param0.getItem(1)!="")
			{
				//公共
				dynconditem.add(param0.getItem(0));
				//公共结束
				if(param0.size()==2)
				{
					dynconditem.add("=");
					dynconditem.add(param0.getItem(1));
				}
				else if(param0.size()==3)
				{
					ArrayList<String> conditionSign = new ArrayList<String>(Arrays.asList("<",">","<=",">="));
					if(conditionSign.contains((String)param0.getItem(2))){
						dynconditem.add(param0.getItem(2));
						dynconditem.add(param0.getItem(1));
					}
				}
				//公共
				dynconditem.add("and");
				AppLogger.info("新建节点:" + dynconditem.toString());
				dyncondList.add(dynconditem);
				//公共结束
			}

			}
		}
		int dyncondListlast=dyncondList.size()-1;
		if (dyncondListlast > -1)
		{
			((JavaList)dyncondList.get(dyncondListlast)).remove(3);
		}
		AppLogger.info("新建列表:" + dyncondList.toString());
		//(JavaList)dyncondList[dyncondListsize].remove();
		String keyname = "dyncondlist";
		inContext.setItem(keyname, dyncondList);
		AppLogger.info("容器对象:" + inContext.toString());
		return TCResult.newSuccessResult();
	}
    
    
}
