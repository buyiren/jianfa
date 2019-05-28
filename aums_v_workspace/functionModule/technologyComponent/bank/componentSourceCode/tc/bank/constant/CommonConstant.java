package tc.bank.constant;

import java.util.Map;

import tc.bank.utils.MapUtil;

/**
 * 增删改查相关的常量类
 * @author AlanMa
 *
 */
public class CommonConstant {
	/**
	 * 标准查询
	 */
	public static final String S = "S";
	/**
	 * 标准删除
	 */
	public static final String D = "D";
	/**
	 * 标准插入
	 */
	public static final String I = "I";
	/**
	 * 标准更新
	 */
	public static final String U = "U";
	/**
	 * 动态条件查询
	 */
	public static final String SA = "SA";
	/**
	 * 全量查询
	 */
	public static final String GA = "GA";
	/**
	 * 手工SQL查询类
	 */
	public static final String SL = "SL";
	/**
	 * 全量手工SQL查询类
	 */
	public static final String GL = "GL";
	/**
	 * 动态条件更新
	 */
	public static final String DA = "DA";
	/**
	 * JDBC查询
	 */
	public static final String PL = "PL";
	/**
     * 手工SQL更新
     */
    public static final String DL = "DL";
	/**
	 * 数据库操作类型-执行方法映射关系
	 */
	public static final Map<String, String> reflectQM = MapUtil.toStrMap(S, "standQuery",SA,"extStdQry",GA,"extStdQryAll",SL,"handworkQry",GL,"handwkQryAll",PL,"queryJDBC",
			D, "standDelete",I, "standInsert",U, "standUpdate",DA,"extConIDUOpr",DL,"handworkUpd");
	/**
	 * 是否全量查询
	 */
	public static final String IS_QUERY_ALL = "isQueryAll";
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 失败
	 */
	public static final int FAILURE = 0;
	/**
	 * 查询结果为空
	 */
	public static final int NONE = 2;
	/**
	 * TRUE
	 */
	public static final String TRUE = "1";
	/**
	 * FALSE
	 */
	public static final String FALSE = "0";
	/**
	 * 动态条件查询调用KEY
	 */
	public static final String IS_FROM_SA = "isFromSA";
	/**
	 * 动态条件更新调用KEY
	 */
	public static final String IS_FROM_DA = "isFromDA";
	/**
	 * 扩展条件KEY
	 */
	public static final String HAS_EXT_COND = "hasExtCond";
	/**
	 * 事物提交标识
	 */
	public static final String COMMIT_FLAG = "commitFlg";
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODE = "GB18030";
	/**
     * 普通打印List
     */
    public static final int NOR_PRINT = 1;
    /**
     * 树状结构打印List
     */
    public static final int TREE_PRINT = 0;
	
}
