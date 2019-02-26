package tc.bank.constant;

/**
 * 用于定义错误码和错误信息 <br>
 * 错误码规则：应用类型分类{2位}+错误类型分类{1位}+错误序号{3位}<br>
 * 应用类型分类：<br>
 * IM——内部技术组件<br>
 * 错误类型分类：<br>
 * C-信息校验类<br>     
 * B-业务信息规则类<br> 
 * D-数据库操作类<br>   
 * T-系统间通讯类<br>   
 * E-其他系统异常类
 * @version 1.0.0
 * @author AlanMa
 *
 */
public class ErrorCodeModule {
    /**
     * 入参[%s]不能为空
     */
    public static final String IMC001 = "IMC001|入参[%s]不能为空";
    /**
     * 无法处理入参[%s]
     */
    public static final String IMC002 = "IMC002|无法处理入参[%s]";
    
    
    /**
     * 字段超出配置的最大长度[%s]
     */
    public static final String IMC003 = "IMC003|字段[%s]超出配置的最大长度";
    /**
     * 无满足条件的记录
     */
    
    public static final String IMB001 = "IMB001|无满足条件的记录";
    /**
     * 数据插入失败
     */
    public static final String IMB002 = "IMB002|插入失败";
    /**
     * [%s]入参条件数据容器非字典类型
     */
    public static final String IMD001 = "IMD001|[%s]入参条件数据容器非字典类型";
    /**
     * [%s]入参条件数据容器非字符串类型
     */
    public static final String IMD002 = "IMD001|[%s]入参条件数据容器非字符串类型";
    /**
     * 数据容器中缺少必输的键值[%s]
     */
    public static final String IMD003 = "IMD003|数据容器中缺少必输的键值[%s]";
    /**
     * 查询[%s]结果为空
     */
    public static final String IMD004 = "IMD004|查询[%s]结果为空";
    /**
     * 反射调用SQL模板类型处理类出错
     */
    public static final String IMD005 = "IMD005|反射调用SQL模板类型处理类出错";
    /**
     * 没有配置需要获取的列
     */
    public static final String IMD006 = "IMD006|没有配置需要获取的列";
    /**
     * dyncondlist动态扩展条件配置错误
     */
    public static final String IMD007 = "IMD007|动态扩展条件配置错误";
    /**
     * 无法通过[%s]获取SQL模板KEY值(BUSIDATAKEY)
     */
    public static final String IMD008 = "IMD008|无法通过[%s]获取SQL模板KEY值(BUSIDATAKEY)";
    /**
     * 无法通过KEY值(BUSIDATAKEY)-[%s]获取SQL模板信息
     */
    public static final String IMD009 = "IMD009|无法通过KEY值(BUSIDATAKEY)-[%s]获取SQL模板信息";
    /**
     * 数据库连接失败
     */
    public static final String IMD010 = "IMD010|数据库连接失败";
    /**
     * 数据库连接已关闭
     */
    public static final String IMD011 = "IMD011|数据库连接已关闭";
    /**
     * SQL语句执行出错:[%s]
     */
    public static final String IMD012 = "IMD012|SQL语句执行出错:[%s]";
    /**
     * [业务数据操作映射表]与[数据操作信息表]配置信息不匹配
     */
    public static final String IMD013 = "IMD013|[业务数据操作映射表]与[数据操作信息表]配置信息不匹配";
    /**
     * 格式转换异常：[%s]
     */
    public static final String IME001 = "IME001|格式转换异常：[%s]";
    /**
     * 不支持[%s]编码集
     */
    public static final String IME002 = "IME002|不支持[%s]编码集";
    /**
     * Document处理异常
     */
    public static final String IME003 = "IME003|Document处理异常";
    /**
     * 报文异常
     */
    public static final String IME004 = "IME004|报文异常";
    /**
     * 流程配置异常
     */
    public static final String IME005 = "IME005|流程节点配置异常";
    /**
     * 公共异常
     */
    public static final String IME999 = "IME999|公共异常适配多种情况";
    
}
