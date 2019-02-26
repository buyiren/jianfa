package tc.bank.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库操作相关常量类
 * 
 * @author AlanMa
 * 
 */
public class DBConstant {

    /**
     * ORACLE数据库
     */
    public static final String ORACLE = "ORA";    
    /**
     * DB2数据库
     */
    public static final String DB2 = "DB2";
    /**
     * MySQL数据库
     */
    public static final String MYSQL = "MySQL";
    /**
     * SCHEMAS
     */
    // public static final String SCHEMAS = "ECIP.";
    /**
     * 最大查询条数
     */
    public static final int MAX_ROW = 20;
    /**
     * 起始记录数
     */
    public static final int START_ROW = 1;
    /**
     * 起始记录数（全量查询）
     */
    public static final int START_ROW_ALL = -1;
    /**
     * 查询结果为空
     */
    public static final int NONE_REC = 0;
    /**
     * T_ARSM_DATAPROCSQL列值
     */
    public static final String T_ARSM_DATAPROCSQL_C = "busidatakey,crttype,opertype,sqlstr,infocols,dbinfocols,condcols,dbcondcols,proctablename,busidatadesc";
    /**
     * 创建类型
     */
    public static final String CRTTYPE = "crttype";
    /**
     * 操作类型
     */
    public static final String OPERTYPE = "opertype";
    /**
     * 条件连接符
     */
    public static final List<String> COND_SYMBOLS = Arrays.asList("=", "!=", ">", "<", ">=", "<=","like");
    
}
