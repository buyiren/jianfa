package tc.bank.domain;

/**
 * t_arsm_dataprocsql(数据操作信息表)
 * 映射为实例对象
 * @author AlanMa
 * 
 */
public class EntityTAD {

    // 数据操作关键字
    private String busidatakey;
    // 创建方式 0-工具创建 1-手工创建
    private String crttype;
    // 操作类型
    private String opertype;
    // sql语句
    private String sqlstr;
    // 平台字段列表
    private String infocols;
    // 数据库字段列表
    private String dbinfocols;
    // 平台条件列表
    private String condcols;
    // 数据库条件列表
    private String dbcondcols;
    // 操作表名
    private String proctablename;
    // 操作说明
    private String busidatadesc;
    
    /**
     * 数据操作关键字
     * get方法
     * @return
     */
    public String getBusidatakey() {
        return busidatakey;
    }
    /**
     * 数据操作关键字
     * set方法
     * @param busidatakey
     */
    public void setBusidatakey(String busidatakey) {
        this.busidatakey = busidatakey;
    }
    /**
     * 创建方式
     * get方法
     * @return
     */
    public String getCrttype() {
        return crttype;
    }
    /**
     * 创建方式
     * set方法
     * @param crttype
     */
    public void setCrttype(String crttype) {
        this.crttype = crttype;
    }
    /**
     * 操作类型
     * get方法
     * @return
     */
    public String getOpertype() {
        return opertype;
    }
    /**
     * 操作类型
     * set方法
     * @param opertype
     */
    public void setOpertype(String opertype) {
        this.opertype = opertype;
    }
    /**
     * sql语句
     * get方法
     * @return
     */
    public String getSqlstr() {
        return sqlstr;
    }
    /**
     * sql语句
     * set方法
     * @param sqlstr
     */
    public void setSqlstr(String sqlstr) {
        this.sqlstr = sqlstr;
    }
    /**
     * 平台字段列表
     * get方法
     * @return
     */
    public String getInfocols() {
        return infocols;
    }
    /**
     * 平台字段列表
     * set方法
     * @param infocols
     */
    public void setInfocols(String infocols) {
        this.infocols = infocols;
    }
    /**
     * 数据库字段列表
     * get方法
     * @return
     */
    public String getDbinfocols() {
        return dbinfocols;
    }
    /**
     * 数据库字段列表
     * set方法
     * @param dbinfocols
     */
    public void setDbinfocols(String dbinfocols) {
        this.dbinfocols = dbinfocols;
    }
    /**
     * 平台条件列表
     * get方法
     * @return
     */
    public String getCondcols() {
        return condcols;
    }
    /**
     * 平台条件列表
     * set方法
     * @param condcols
     */
    public void setCondcols(String condcols) {
        this.condcols = condcols;
    }
    /**
     * 数据库条件列表
     * get方法
     * @return
     */
    public String getDbcondcols() {
        return dbcondcols;
    }
    /**
     * 数据库条件列表
     * set方法
     * @param dbcondcols
     */
    public void setDbcondcols(String dbcondcols) {
        this.dbcondcols = dbcondcols;
    }
    /**
     * 操作表名
     * get方法
     * @return
     */
    public String getProctablename() {
        return proctablename;
    }
    /**
     * 操作表名
     * set方法
     * @param proctablename
     */
    public void setProctablename(String proctablename) {
        this.proctablename = proctablename;
    }
    /**
     * 操作说明
     * get方法
     * @return
     */
    public String getBusidatadesc() {
        return busidatadesc;
    }
    /**
     * 操作说明
     * set方法
     * @param busidatadesc
     */
    public void setBusidatadesc(String busidatadesc) {
        this.busidatadesc = busidatadesc;
    }

}
