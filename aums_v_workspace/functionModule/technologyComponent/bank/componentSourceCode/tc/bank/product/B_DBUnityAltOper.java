package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.List;
import java.util.Map;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.domain.EntityTAD;
import tc.bank.utils.CheckUtil;
import tc.bank.utils.DBBusiUtil;
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
 * 数据新增更新删除类统一接口
 * 
 * @author AlanMa
 * 
 */
@ComponentGroup(level = "银行", groupName = "数据库操作")
public class B_DBUnityAltOper {

	/**
	 * @category 数据新增更新删除类统一接口
	 * @param data_context
	 *            入参|数据容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param busioper
	 *            入参|业务操作关键字|{@link java.lang.String}
	 * @param ext_context
	 *            入参|扩展参数字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param commitFlg
	 *            入参|事务提交标识|{@link boolean}
	 * @param map_context
	 *            入参|数据操作映射数据容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param rownum
	 *            出参|影响的条数|{@link int}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 影响的记录数为零<br/>
	 */
	@InParams(param = {
			@Param(name = "data_context", comment = "数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "busioper", comment = "业务操作关键字", type = java.lang.String.class),
			@Param(name = "ext_context", comment = "扩展参数字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "commitFlg", comment = "事务提交标识", type = boolean.class),
			@Param(name = "map_context", comment = "数据操作映射数据容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = { @Param(name = "rownum", comment = "影响的条数", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"),
			@Return(id = "2", desp = "影响的记录数为零") })
	@Component(label = "数据新增更新删除类统一接口", style = "选择型", type = "同步组件", comment = "非查询类业务数据操作的入口函数,所有对数据库的非查询类操纵均需通过本接口进行操作ext_context为扩展参数字典,里面现在有如下KEY dyncondlist格式为条件的嵌套list,如[[[\"a\", \"in\", \"'3','2','1'\", \"and\"],[\"b\", \"is\", \"Null\"], \"or\"], [[\"c\", \"=\",  None, \"and\"], [\"b\", \"!=\", None]]],标识 (a in('3','2','1') and b is null) or (c=cond_data_context['c'] and b != cond_data_context['b'])", date = "2016-03-25 10:33:53")
	public static TCResult B_DBUnityAltOpr(JavaDict data_context,
			String busioper, JavaDict ext_context, boolean commitFlg,
			JavaDict map_context) {
		TCResult tcResult;
		try {
			AppLogger.info("【enter B_DBUnityAltOpr】");
			/**入参校验*/
			CheckUtil.checkInpParamDB(data_context, busioper, ext_context,
					map_context);
			/**
			 * 如果commitFlg为false，默认设置commitFlg为true
			 * 保证事务的提交
			 */
//			if(!commitFlg){
//				commitFlg = true ;
//			}
			AppLogger.info("pass input param check");
			/**获取数据操作关键字*/
			JavaList busidatakeys = DBBusiUtil.getBusidateKey(busioper,
					map_context);
			AppLogger.info("[busidatakey]:"
					+ (ListUtil.isNotEmpty(busidatakeys) ? busidatakeys
							.toString() : busidatakeys));
			/**通过数据关键字获取SQL模板*/
			List<EntityTAD> entitys = DBBusiUtil.getSQLModel(busidatakeys);
			map_context.put(CommonConstant.COMMIT_FLAG,
					(commitFlg ? CommonConstant.TRUE : CommonConstant.FALSE));
			/**调用方法反射*/
			tcResult = DBBusiUtil.reflectMethod(entitys,
					"tc.bank.product.B_DBUnityAltOper", data_context, ext_context,
					map_context);
		} catch (BusException e) {
			AppLogger.error(e);
			TCResult tcResultRet = RetResultUtil.getTCResToExternal(e);
			RetResultUtil.printTCResult(tcResultRet);
			return tcResultRet;
		}
		TCResult tcResultRet = RetResultUtil.getTCResToExternal(tcResult);
		RetResultUtil.printTCResult(tcResultRet);
		return tcResultRet;
	}

    /**
     * D-标准删除
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    @SuppressWarnings("unchecked")
    public static TCResult standDelete(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityAltOper.standDelete]");
        JavaDict mapContext = (JavaDict) inputMap.get("map_context");
        JavaDict condValue = (JavaDict) inputMap.get("cond_data_context");
        boolean commitFlg = CommonConstant.TRUE.equals((String) mapContext.get(CommonConstant.COMMIT_FLAG)) ? true : false;
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        EntityTAD entity;
        String tableName;
        TCResult tc_result_trans = null;
        for (int i = 0; i < entitys.size(); i++) {
            entity = (EntityTAD) entitys.get(i);
            tableName = entity.getProctablename();
            JavaList condition = EntityUtil.getCondValuesU(entity.getCondcols(), entity.getDbcondcols(), condValue);

            boolean inputComFlg = false;
            if (i == entitys.size() - 1) {
                inputComFlg = commitFlg;
            }

            AppLogger.debug("[tableName]:" + tableName);
            AppLogger.debug(ListUtil.printList(condition, "[condition value]:", CommonConstant.NOR_PRINT));
            AppLogger.debug("[commitFlg]:" + inputComFlg);

            tc_result_trans = P_Jdbc.preparedDelete(null, tableName, condition, inputComFlg);
            AppLogger.info("No." + i + "-[P_Jdbc.preparedDelete return status]:" + tc_result_trans.getStatus());
        }

        return tc_result_trans;
    }

    /**
     * I-标准插入
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    @SuppressWarnings({ "unchecked" })
    public static TCResult standInsert(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityAltOper.standInsert]");
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        EntityTAD entity;
        JavaDict context = (JavaDict) inputMap.get("cond_data_context");
        JavaDict mapContext = (JavaDict) inputMap.get("map_context");
        String tableName;
        boolean commitFlg = CommonConstant.TRUE.equals((String) mapContext.get(CommonConstant.COMMIT_FLAG)) ? true : false;
        TCResult tc_result_trans = null;
        for (int i = 0; i < entitys.size(); i++) {
            entity = (EntityTAD) entitys.get(i);
            tableName = entity.getProctablename();
            /**获取插入的列和值*/
            JavaList values = EntityUtil.getModifyValues(entity.getDbinfocols(), entity.getInfocols(), context);
            boolean inputComFlg = false;
            if (i == entitys.size() - 1) {
                inputComFlg = commitFlg;
            }
            AppLogger.debug("[tableName]:" + tableName);
            AppLogger.debug(ListUtil.printList(values, "[values]:",CommonConstant.NOR_PRINT));
            AppLogger.debug("[commitFlg]:" + inputComFlg);

        	AppLogger.debug("[insert Start]");
        	 tc_result_trans = P_Jdbc.preparedInsert(null, tableName, values, inputComFlg);
             AppLogger.info("No." + i + "-[P_Jdbc.preparedInsert return status]:" + tc_result_trans.getStatus());
             if (tc_result_trans.getStatus()==0) {
            	 AppLogger.debug(tc_result_trans.getErrorMsg());
            	 AppLogger.debug("[insert]:" +"--[tableName]"+tableName+"(errorCode) "+ErrorCode.AGR+"--[插入失败]");
            	 return RetResultUtil.getTCResToExternal(new BusException(ErrorCodeModule.IMB002));
 				//return TCResult.newFailureResult(ErrorCode.AGR, tableName+" 插入失败");
			}
        }

        return tc_result_trans;
    }

    /**
     * U-标准更新<br>
     * 更新列可配置，数据库配置条件 or 扩展(优先)
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    @SuppressWarnings("unchecked")
    public static TCResult standUpdate(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityAltOper.standUpdate]");
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        EntityTAD entity;
        JavaDict context = (JavaDict) inputMap.get("cond_data_context");
        JavaDict extCond = (JavaDict) inputMap.get("ext_context");
        JavaDict mapContext = (JavaDict) inputMap.get("map_context");
        String tableName;
        JavaList dyncondlist = null;
        JavaList colInfos = null;
        JavaList condition = null;
        TCResult tc_result_trans = null;
        boolean commitFlg = CommonConstant.TRUE.equals((String) mapContext.get(CommonConstant.COMMIT_FLAG)) ? true : false;
        if (extCond != null && extCond.get("dyncondlist") != null) {
            dyncondlist = (JavaList) extCond.get("dyncondlist");
        }

        for (int i = 0; i < entitys.size(); i++) {
            entity = (EntityTAD) entitys.get(i);
            tableName = entity.getProctablename();
            condition = new JavaList();
            /**获取要更新的列和值*/
            colInfos = EntityUtil.getModifyValues(entity.getDbinfocols(), entity.getInfocols(), context);
            if (!CommonConstant.TRUE.equals((String) inputMap.get(CommonConstant.IS_FROM_DA))) {
                // 标准-standUpdate
                if (ListUtil.isNotEmpty(dyncondlist)) {
                    // 有扩展条件
                    condition.addAll(dyncondlist);
                }
                else {
                    // 标准条件
                    condition = EntityUtil.getCondValuesU(entity.getCondcols(), entity.getDbcondcols(), context);
                }
            }
            else {
                // 动态-extConIDUOpr
                // 标准条件
                condition = EntityUtil.getCondValuesU(entity.getCondcols(), entity.getDbcondcols(), context);
                // 扩展条件
                if (ListUtil.isNotEmpty(dyncondlist)) {
                    if (condition != null) {
                        condition.addAll(dyncondlist);
                    }
                    else {
                        condition = dyncondlist;
                    }
                }
            }

            boolean inputComFlg = false;
            if (i == entitys.size() - 1) {
                inputComFlg = commitFlg;
            }
            AppLogger.debug("[tableName]:" + tableName);
            AppLogger.debug(ListUtil.printList(colInfos, "[colInfos value]:", CommonConstant.NOR_PRINT));
            AppLogger.debug(ListUtil.printList(condition, "[condition value]:", CommonConstant.NOR_PRINT));
            AppLogger.debug("[commitFlg]:" + inputComFlg);
            /**预编译SQL执行*/
            tc_result_trans = P_Jdbc.preparedUpdate(null, tableName, colInfos, condition, inputComFlg);
            AppLogger.info("[P_Jdbc.preparedUpdate return status]:" + tc_result_trans.getStatus());
        }
        return tc_result_trans;
    }

    /**
     * DA-动态条件更新<br>
     * 支持动态传入扩展条件，数据库配置条件 and 扩展
     * 
     * @param inputMap
     * @return
     * @throws BusException
     */
    public static TCResult extConIDUOpr(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityAltOper.extConIDUOpr]");
        inputMap.put(CommonConstant.IS_FROM_DA, CommonConstant.TRUE);
        return standUpdate(inputMap);
    }

    /**
     * DL-手工SQL更新
     * 不支持传入任何扩展条件
     * @param inputMap
     * @return
     * @throws BusException
     */
    @SuppressWarnings("unchecked")
    public static TCResult handworkUpd(Map<String, Object> inputMap) throws BusException {
        AppLogger.info("[eneter B_DBUnityAltOper.handworkUpd]");
        List<EntityTAD> entitys = (List<EntityTAD>) inputMap.get("entity");
        EntityTAD entity;
        JavaDict context = (JavaDict) inputMap.get("cond_data_context");
        JavaDict extCond = (JavaDict) inputMap.get("ext_context");
        JavaDict mapContext = (JavaDict) inputMap.get("map_context");
        JavaList condition;
        TCResult tc_result_trans = null;
        String sqlstr;
        boolean commitFlg = CommonConstant.TRUE.equals((String) mapContext.get(CommonConstant.COMMIT_FLAG)) ? true : false;
        if (extCond != null && extCond.get("dyncondlist") != null) {
            if (ListUtil.isNotEmpty((JavaList) extCond.get("dyncondlist"))) {
                AppLogger.error("[DL is not support to deal with ext_context]");
                return TCResult.newFailureResult(ErrorCodeModule.IMC002, "ext_context");
            }
        }
        for (int i = 0; i < entitys.size(); i++) {
            entity = (EntityTAD) entitys.get(i);
            /**获取手工sql*/
            sqlstr = EntityUtil.getPSSQL(entity.getSqlstr());
            condition = EntityUtil.getCondValues(entity.getCondcols(), context);

            boolean inputComFlg = false;
            if (i == entitys.size() - 1) {
                inputComFlg = commitFlg;
            }
            AppLogger.debug(ListUtil.printList(condition, "[condition value]:",CommonConstant.NOR_PRINT));
            AppLogger.debug("[commitFlg]:" + inputComFlg);

            tc_result_trans = P_Jdbc.preparedExecuteSQL(null, sqlstr, condition, commitFlg);
            AppLogger.info("[P_Jdbc.preparedUpdate return status]:" + tc_result_trans.getStatus());
        }
        return tc_result_trans;
    }
}
