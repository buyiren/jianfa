package bc;

import cn.com.agree.afa.jcomponent.GlobalErrorHolder;
import cn.com.agree.afa.svc.javaengine.AppLogger.LogLevel;
import cn.com.agree.afa.svc.javaengine.BCScript;
import cn.com.agree.afa.svc.javaengine.EndNode;
import cn.com.agree.afa.svc.javaengine.INode;
import cn.com.agree.afa.svc.javaengine.JScript;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaContext;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import cn.com.agree.afa.util.ExceptionUtils;
import cn.com.agree.afa.util.future.IFuture;
import cn.com.agree.afa.util.future.IFutureListener;
import java.util.ArrayList;
import java.util.List;
import static cn.com.agree.afa.jcomponent.GlobalErrorHolder.setGlobalError;
import tc.View.Server.A_DevWarn;
import tc.platform.P_Dict;
import tc.platform.P_Jdbc;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_Logger;
import tc.platform.P_Object;
import tc.platform.P_String;
import tc.platform.P_Time;

/**
 * 业务组件包名称：WarningManage <br/>
 *
 * 业务组件包描述：WarningManage <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class WarningManage {

    public static class BusinessWarning extends BCScript {
        private INode startNode;
        public BusinessWarning(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node23();
                log(LogLevel.INFO, "开始运行业务组件 业务预警");
            }
            
            INode node = startNode;
            while (canExecute(node)) {
                node = node.execute();
            }
            
            return node;
        }
        
        private class Node1 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node1 策略跟踪信息发送");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        bcScript = new bc.WarningManage.SENDMSG(__REQ__, __RSP__, __BUILTIN__TEMP__);
                    } else {
                        __BUILTIN__TEMP__ = bcScript.getBuiltinDict();
                    }
                    
                    INode node = bcScript.execute();
                    
                    
                    if (node == EndNode.SUSPEND_END) {
                        startNode = this;
                        return node;
                    }
                    
                    log(LogLevel.INFO, "逻辑返回值=" + node);
                    
                    if (node instanceof EndNode) {
                    	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    		gatherStat("BusinessWarning_Node1", "策略跟踪信息发送", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 1:
                            return new Node19();
                        }
                    }
                    return getExceptionHandler(new Node14());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node1", "策略跟踪信息发送", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node2 循环因子赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", 0));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node2", "循环因子赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node2", "循环因子赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node2", "循环因子赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node3 变量赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_DevMap_", ((List)__REQ__.getItem("DevResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_Map_")).getItem("i")))), new JavaList("ExceptionFlag", ""), new JavaList("ExceptionMsg", ""), new JavaList("brno", ""));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node3", "变量赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node3", "变量赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node3", "变量赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node5 取预警表序列");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "AUMS_WARN_LOGID_SEQ";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = __REQ__.getItem("_DatabaseType_");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    int _arg3_ = 8;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.getSequence(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node5", "取预警表序列", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("BusinessWarning_Node5", "取预警表序列", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node14());
                        }
                        __REQ__.setItem("T_PCVA_WARNINGLOG_ID_SEQ", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node5", "取预警表序列", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node5", "取预警表序列", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node6 信息日志");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "策略ID:"+((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")+"未配制设备相关参数";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Logger.info(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node6", "信息日志", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node6", "信息日志", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node21();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node6", "信息日志", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node7 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "p_excptype";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "p_ret_code";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "p_ret_desc";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = GlobalErrorHolder.putGlobalErrorToDict(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node7", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node7", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node7", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node8 创建信息发送容器");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_SendMsgMap_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node8", "创建信息发送容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node8", "创建信息发送容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node8", "创建信息发送容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node9 是否继续循环");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(((JavaDict)__REQ__.getItem("_Temp_Map_")).getItem("i"))<toInt(__REQ__.getItem("DevCnt"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node9", "是否继续循环", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node9", "是否继续循环", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node16();
                    case 1:
                        return new Node22();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node9", "是否继续循环", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node10 信息发送赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_SendMsgMap_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("POLICYID", ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")), new JavaList("MESSAGE", __REQ__.getItem("ExceptionMsg")), new JavaList("REF_WORKDATE", __REQ__.getItem("workdate")), new JavaList("REF_AGENTSERIALNO", __REQ__.getItem("T_PCVA_WARNINGLOG_ID_SEQ")), new JavaList("BRNO", __REQ__.getItem("brno")), new JavaList("ROLE", "1"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node10", "信息发送赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node10", "信息发送赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node10", "信息发送赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node11 登记预警流水表");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "aums_warn_log";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(new JavaList("AGENTSERIALNO", __REQ__.getItem("T_PCVA_WARNINGLOG_ID_SEQ")), new JavaList("WORKDATE", __REQ__.getItem("workdate")), new JavaList("POLICYID", ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")), new JavaList("DEVID", ((JavaDict)__REQ__.getItem("_DevMap_")).getItem("DEVID")), new JavaList("DEVBRNO", __REQ__.getItem("brno")), new JavaList("STATUS", "0"), new JavaList("EXCEPTIONCOUNT", "0"), new JavaList("ERRORCODE", "DEV_ERROR"), new JavaList("RESULTMSG", __REQ__.getItem("ExceptionMsg")), new JavaList("ERRORMSG", __REQ__.getItem("ExceptionMsg")), new JavaList("CREATE_TIME", __REQ__.getItem("workdate")+""+__REQ__.getItem("worktime")));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.dmlInsert(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node11", "登记预警流水表", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("BusinessWarning_Node11", "登记预警流水表", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node14());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node11", "登记预警流水表", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node11", "登记预警流水表", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node12 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node12 默认逻辑错误委托");
                setExceptionHandler(new Node7());
                log(LogLevel.INFO, "将默认异常委托到Node7节点");
                return new Node13();
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node13 查询策略设备表");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "SELECT POLICYID,DEVID,DEVBRNO,DEVTYPE,CREATETIME,CREATEUSER,REMARK1,REMARK2,REMARK3 from AUMS_WARN_POLICYDEVMAPPING WHERE policyid='"+((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")+"'";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = 0;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_Jdbc.dmlSelectDict(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node13", "查询策略设备表", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("BusinessWarning_Node13", "查询策略设备表", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node14());
                        }
                        __REQ__.setItem("DevCnt", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("DevResult", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node13", "查询策略设备表", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    case 2:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node13", "查询策略设备表", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node14 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node14 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node15 创建循环list变量");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_Temp_Map_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node15", "创建循环list变量", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node15", "创建循环list变量", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node15", "创建循环list变量", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node16 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node16 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node17 获取交易时间");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Time.P_getTime();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node17", "获取交易时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("BusinessWarning_Node17", "获取交易时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node14());
                        }
                        __REQ__.setItem("workdate", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("worktime", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node17", "获取交易时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node17", "获取交易时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node18 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node18 判断预警的业务类型");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYPROPERTY");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "01";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = null;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    String _arg4_ = null;
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    String _arg5_ = null;
                    logVar(LogLevel.valueOf(4), "入参5", _arg5_);
                    String _arg6_ = null;
                    logVar(LogLevel.valueOf(4), "入参6", _arg6_);
                    String _arg7_ = null;
                    logVar(LogLevel.valueOf(4), "入参7", _arg7_);
                    TCResult result = P_JudgmentStatement.switchCaseFrame(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_, _arg5_, _arg6_, _arg7_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node18", "判断预警的业务类型", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node18", "判断预警的业务类型", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node19();
                    case 1:
                        return new Node25();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node18", "判断预警的业务类型", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node19 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node19 循环因子+1");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", toInt(((JavaDict)__REQ__.getItem("_Temp_Map_")).getItem("i"))+1));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node19", "循环因子+1", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node19", "循环因子+1", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node19", "循环因子+1", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node20 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node20 信息日志");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "处理消息通知模块";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Logger.info(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node20", "信息日志", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node20", "信息日志", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node20", "信息日志", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node21 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node21 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node22 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node22 创建临时容器");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_DevMap_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node22", "创建临时容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node22", "创建临时容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node22", "创建临时容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node23 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node23 开始");
                return new Node12();
            }    
        }
        
        private class Node24 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node24 是否有异常");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "0".equals(__REQ__.getItem("ExceptionFlag"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node24", "是否有异常", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node24", "是否有异常", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node5();
                    case 1:
                        return new Node19();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node24", "是否有异常", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
        private class Node25 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BusinessWarning_Node25 预警卡箱剩余张数");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)__REQ__.getItem("_DevMap_")).getItem("DEVID");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaDict _arg1_ = __REQ__.getItem("_Warn_Map_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = A_DevWarn.A_CheckCardStat(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BusinessWarning_Node25", "预警卡箱剩余张数", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node14());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 3) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("BusinessWarning_Node25", "预警卡箱剩余张数", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node14());
                        }
                        __REQ__.setItem("ExceptionFlag", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("ExceptionMsg", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                        __REQ__.setItem("brno", outputParams.get(2));
                        logVar(LogLevel.valueOf(4), "出参2", outputParams.get(2));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BusinessWarning_Node25", "预警卡箱剩余张数", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node19();
                    case 1:
                        return new Node24();
                    default:
                        return getExceptionHandler(new Node14());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BusinessWarning_Node25", "预警卡箱剩余张数", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node14());
                }
            }    
        }
        
    
    }
    public static class SENDMSG extends BCScript {
        private INode startNode;
        public SENDMSG(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node2();
                log(LogLevel.INFO, "开始运行业务组件 策略跟踪信息发送");
            }
            
            INode node = startNode;
            while (canExecute(node)) {
                node = node.execute();
            }
            
            return node;
        }
        
        private class Node1 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node1 默认逻辑错误委托");
                setExceptionHandler(new Node4());
                log(LogLevel.INFO, "将默认异常委托到Node4节点");
                return new Node9();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node2 开始");
                return new Node1();
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node4 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "p_excptype";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "p_ret_code";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "p_ret_desc";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = GlobalErrorHolder.putGlobalErrorToDict(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node4", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node4", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node4", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node5 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node6 字符串查找");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("ROLE");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("ROLE");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = 0;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_String.findStr(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node6", "字符串查找", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("SENDMSG_Node6", "字符串查找", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("index", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node6", "字符串查找", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node18();
                    case 1:
                        return new Node20();
                    case 2:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node6", "字符串查找", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node7 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node9 查询短信联系人");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "SELECT MSGID,UMID,BRNO,PHONENUMBER,EMAILS,UMNAME,STARTTIME,ENDTIME,ISSEND,REMARK1,REMARK2,REMARK3,ROLE from  aums_warn_messagemaintain where BRNO='"+((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("BRNO")+"'  and issend='1' ";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = 0;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_Jdbc.dmlSelectDict(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node9", "查询短信联系人", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("SENDMSG_Node9", "查询短信联系人", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("peopleCnt", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("PeoPleResult", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node9", "查询短信联系人", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    case 2:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node9", "查询短信联系人", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node10 信息日志");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "网点【"+((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("BRNO")+"】未维护短信接收人";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Logger.info(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node10", "信息日志", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node10", "信息日志", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node10", "信息日志", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node11 循环因子赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_PeopleMap_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", 0));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node11", "循环因子赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node11", "循环因子赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node11", "循环因子赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node12 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node12 创建循环变量");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_Temp_PeopleMap_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node12", "创建循环变量", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node12", "创建循环变量", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node12", "创建循环变量", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node13 是否继续循环");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i"))<toInt(__REQ__.getItem("peopleCnt"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node13", "是否继续循环", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node13", "是否继续循环", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node5();
                    case 1:
                        return new Node24();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node13", "是否继续循环", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node18 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node18 循环因子+1");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_PeopleMap_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i"))+1));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node18", "循环因子+1", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node18", "循环因子+1", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node18", "循环因子+1", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node19 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node19 短信发送登记");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "aums_warn_sendmessage";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(new JavaList("WORKDATE", __REQ__.getItem("workdate")), new JavaList("AGENTSERIALNO", __REQ__.getItem("SQ_MESGSERNO")), new JavaList("PHONE", ((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("PHONE")), new JavaList("BRNO", ((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("BRNO")), new JavaList("MESSAGE", ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("MESSAGE")), new JavaList("SENDTIMES", "0"), new JavaList("TYPE", "106"), new JavaList("STATUS ", "0"), new JavaList("REF_WORKDATE", ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("REF_WORKDATE")), new JavaList("REF_AGENTSERIALNO", ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("REF_AGENTSERIALNO")));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.dmlInsert(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node19", "短信发送登记", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("SENDMSG_Node19", "短信发送登记", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node19", "短信发送登记", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node19", "短信发送登记", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node20 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node20 序列号操作");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "SQ_MESGSERNO";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = __REQ__.getItem("_DatabaseType_");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    int _arg3_ = 8;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.getSequence(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node20", "序列号操作", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("SENDMSG_Node20", "序列号操作", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("SQ_MESGSERNO", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node20", "序列号操作", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node21();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node20", "序列号操作", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node21 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node21 获取交易时间");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Time.P_getTime();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node21", "获取交易时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("SENDMSG_Node21", "获取交易时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("workdate", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("worktime", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node21", "获取交易时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node22();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node21", "获取交易时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node22 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node22 判断是否在接受时间内");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(__REQ__.getItem("worktime"))>=toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("STARTTIME"))&&toInt(__REQ__.getItem("worktime"))<=toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("ENDTIME"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node22", "判断是否在接受时间内", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node22", "判断是否在接受时间内", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node23();
                    case 1:
                        return new Node19();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node22", "判断是否在接受时间内", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node23 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node23 短信发送登记");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "aums_warn_sendmessage";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(new JavaList("WORKDATE", __REQ__.getItem("workdate")), new JavaList("AGENTSERIALNO", __REQ__.getItem("SQ_MESGSERNO")), new JavaList("PHONE", ((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("PHONE")), new JavaList("BRNO", ((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("BRNO")), new JavaList("MESSAGE", ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("MESSAGE")), new JavaList(" SENDTIMES", "0"), new JavaList("TYPE", "106"), new JavaList(" STATUS ", "9"), new JavaList("REF_WORKDATE", ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("REF_WORKDATE")), new JavaList("REF_AGENTSERIALNO", ((JavaDict)__REQ__.getItem("_SendMsgMap_")).getItem("REF_AGENTSERIALNO")));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.dmlInsert(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node23", "短信发送登记", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("SENDMSG_Node23", "短信发送登记", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node23", "短信发送登记", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node23", "短信发送登记", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node24 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SENDMSG_Node24 短信联系人信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_PeopleMap_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("STARTTIME", ((JavaDict)((List)__REQ__.getItem("PeoPleResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i")))).getItem("STARTTIME")), new JavaList("ENDTIME", ((JavaDict)((List)__REQ__.getItem("PeoPleResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i")))).getItem("ENDTIME")), new JavaList("BRNO", ((JavaDict)((List)__REQ__.getItem("PeoPleResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i")))).getItem("BRNO")), new JavaList("PHONE", ((JavaDict)((List)__REQ__.getItem("PeoPleResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i")))).getItem("PHONENUMBER")), new JavaList("ROLE", ((JavaDict)((List)__REQ__.getItem("PeoPleResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_PeopleMap_")).getItem("i")))).getItem("ROLE")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SENDMSG_Node24", "短信联系人信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SENDMSG_Node24", "短信联系人信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SENDMSG_Node24", "短信联系人信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }
    public static class DeviceWarning extends BCScript {
        private INode startNode;
        public DeviceWarning(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node5();
                log(LogLevel.INFO, "开始运行业务组件 设备预警");
            }
            
            INode node = startNode;
            while (canExecute(node)) {
                node = node.execute();
            }
            
            return node;
        }
        
        private class Node1 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node1 查询策略设备表");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "SELECT POLICYID,DEVID,DEVBRNO,DEVTYPE,CREATETIME,CREATEUSER,REMARK1,REMARK2,REMARK3 from AUMS_WARN_POLICYDEVMAPPING WHERE policyid='"+((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")+"'";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = 0;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_Jdbc.dmlSelectDict(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node1", "查询策略设备表", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("DeviceWarning_Node1", "查询策略设备表", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node27());
                        }
                        __REQ__.setItem("DevCnt", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("DevResult", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node1", "查询策略设备表", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    case 2:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node1", "查询策略设备表", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node2 循环因子+1");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", toInt(((JavaDict)__REQ__.getItem("_Temp_Map_")).getItem("i"))+1));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node2", "循环因子+1", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node2", "循环因子+1", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node2", "循环因子+1", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node3 创建循环list变量");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_Temp_Map_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node3", "创建循环list变量", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node3", "创建循环list变量", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node3", "创建循环list变量", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node4 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node5 开始");
                return new Node18();
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node6 循环因子赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Temp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", 0));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node6", "循环因子赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node6", "循环因子赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node6", "循环因子赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node7 是否继续循环");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(((JavaDict)__REQ__.getItem("_Temp_Map_")).getItem("i"))<toInt(__REQ__.getItem("DevCnt"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node7", "是否继续循环", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node7", "是否继续循环", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node10();
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node7", "是否继续循环", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node8 变量赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_DevMap_", ((List)__REQ__.getItem("DevResult")).get(toInt(((JavaDict)__REQ__.getItem("_Temp_Map_")).getItem("i")))), new JavaList("ExceptionFlag", ""), new JavaList("ExceptionMsg", ""), new JavaList("brno", ""));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node8", "变量赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node8", "变量赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node25();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node8", "变量赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node9 信息日志");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "策略ID:"+((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")+"未配制设备相关参数";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Logger.info(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node9", "信息日志", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node9", "信息日志", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node9", "信息日志", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node10 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node10 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node11 创建临时容器");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_DevMap_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node11", "创建临时容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node11", "创建临时容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node11", "创建临时容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node13 是否有异常");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "0".equals(__REQ__.getItem("ExceptionFlag"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node13", "是否有异常", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node13", "是否有异常", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node17();
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node13", "是否有异常", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node17 取预警表序列");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "AUMS_WARN_LOGID_SEQ";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = __REQ__.getItem("_DatabaseType_");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    int _arg3_ = 8;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.getSequence(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node17", "取预警表序列", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("DeviceWarning_Node17", "取预警表序列", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node27());
                        }
                        __REQ__.setItem("T_PCVA_WARNINGLOG_ID_SEQ", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node17", "取预警表序列", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node21();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node17", "取预警表序列", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node18 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node18 默认逻辑错误委托");
                setExceptionHandler(new Node26());
                log(LogLevel.INFO, "将默认异常委托到Node26节点");
                return new Node1();
            }    
        }
        
        private class Node19 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node19 登记预警流水表");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "aums_warn_log";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(new JavaList("AGENTSERIALNO", __REQ__.getItem("T_PCVA_WARNINGLOG_ID_SEQ")), new JavaList("WORKDATE", __REQ__.getItem("workdate")), new JavaList("POLICYID", ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")), new JavaList("DEVID", ((JavaDict)__REQ__.getItem("_DevMap_")).getItem("DEVID")), new JavaList("DEVBRNO", __REQ__.getItem("brno")), new JavaList("STATUS", "0"), new JavaList("EXCEPTIONCOUNT", "0"), new JavaList("ERRORCODE", "DEV_ERROR"), new JavaList("RESULTMSG", __REQ__.getItem("ExceptionMsg")), new JavaList("ERRORMSG", __REQ__.getItem("ExceptionMsg")), new JavaList("CREATE_TIME", __REQ__.getItem("workdate")+""+__REQ__.getItem("worktime")));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.dmlInsert(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node19", "登记预警流水表", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 1) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("DeviceWarning_Node19", "登记预警流水表", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node27());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node19", "登记预警流水表", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node19", "登记预警流水表", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node20 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node20 信息日志");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "处理消息通知模块";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Logger.info(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node20", "信息日志", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node20", "信息日志", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node22();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node20", "信息日志", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node21 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node21 获取交易时间");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Time.P_getTime();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node21", "获取交易时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("DeviceWarning_Node21", "获取交易时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node27());
                        }
                        __REQ__.setItem("workdate", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("worktime", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node21", "获取交易时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node19();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node21", "获取交易时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node22 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node22 创建信息发送容器");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_SendMsgMap_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node22", "创建信息发送容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node22", "创建信息发送容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node23();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node22", "创建信息发送容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node23 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node23 信息发送赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_SendMsgMap_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("POLICYID", ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID")), new JavaList("MESSAGE", __REQ__.getItem("ExceptionMsg")), new JavaList("REF_WORKDATE", __REQ__.getItem("workdate")), new JavaList("REF_AGENTSERIALNO", __REQ__.getItem("T_PCVA_WARNINGLOG_ID_SEQ")), new JavaList("BRNO", __REQ__.getItem("brno")), new JavaList("ROLE", "1"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node23", "信息发送赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node23", "信息发送赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node24();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node23", "信息发送赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node24 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node24 策略跟踪信息发送");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        bcScript = new bc.WarningManage.SENDMSG(__REQ__, __RSP__, __BUILTIN__TEMP__);
                    } else {
                        __BUILTIN__TEMP__ = bcScript.getBuiltinDict();
                    }
                    
                    INode node = bcScript.execute();
                    
                    
                    if (node == EndNode.SUSPEND_END) {
                        startNode = this;
                        return node;
                    }
                    
                    log(LogLevel.INFO, "逻辑返回值=" + node);
                    
                    if (node instanceof EndNode) {
                    	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    		gatherStat("DeviceWarning_Node24", "策略跟踪信息发送", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 1:
                            return new Node2();
                        }
                    }
                    return getExceptionHandler(new Node27());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node24", "策略跟踪信息发送", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node25 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node25 设备预警状态处理");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)__REQ__.getItem("_DevMap_")).getItem("DEVID");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYPROPERTY");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = ((JavaDict)__REQ__.getItem("_Warn_Map_")).getItem("POLICYID");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = A_DevWarn.A_DeviceStat(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node25", "设备预警状态处理", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 3) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("DeviceWarning_Node25", "设备预警状态处理", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node27());
                        }
                        __REQ__.setItem("ExceptionFlag", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("ExceptionMsg", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                        __REQ__.setItem("brno", outputParams.get(2));
                        logVar(LogLevel.valueOf(4), "出参2", outputParams.get(2));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node25", "设备预警状态处理", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node2();
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node25", "设备预警状态处理", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node26 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node26 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "p_excptype";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "p_ret_code";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "p_ret_desc";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = GlobalErrorHolder.putGlobalErrorToDict(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("DeviceWarning_Node26", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node27());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("DeviceWarning_Node26", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node27();
                    default:
                        return getExceptionHandler(new Node27());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("DeviceWarning_Node26", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node27());
                }
            }    
        }
        
        private class Node27 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "DeviceWarning_Node27 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
    
    }

      
}