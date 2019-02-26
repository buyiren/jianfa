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
import tc.bank.product.B_DBUnityAltOper;
import tc.bank.product.B_SysParam;
import tc.platform.P_Dict;
import tc.platform.P_Jdbc;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_Object;
import tc.platform.P_Time;

/**
 * 业务组件包名称：CIPPublicmethod <br/>
 *
 * 业务组件包描述：CIPPublicmethod <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class CIPPublicmethod {

    public static class HostInterfaceInvoke extends BCScript {
        private INode startNode;
        public HostInterfaceInvoke(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node12();
                log(LogLevel.INFO, "开始运行业务组件 主机接口调用");
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
                log(LogLevel.INFO, "HostInterfaceInvoke_Node1 取工作日期时间");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        bcScript = new bc.CIPPublicmethod.GetWorkDateTime(__REQ__, __RSP__, __BUILTIN__TEMP__);
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
                    		gatherStat("HostInterfaceInvoke_Node1", "取工作日期时间", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 0:
                            return new Node14();
                        case 1:
                            return new Node6();
                        }
                    }
                    return getExceptionHandler(new Node5());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node1", "取工作日期时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node2 取全局错误到容器");
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
                        	gatherStat("HostInterfaceInvoke_Node2", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node2", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node5();
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node2", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node3 登记主机通迅流水");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "RegHostReqInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "CIPPublicmethod", "transcode", "HostInterfaceInvoke");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node3", "登记主机通迅流水", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
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
                	        	gatherStat("HostInterfaceInvoke_Node3", "登记主机通迅流水", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node5());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node3", "登记主机通迅流水", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node3", "登记主机通迅流水", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node4 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node4 调用主机系统通讯");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        __BUILTIN__TEMP__.setItem("pkgName", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("modulecode"));
                        logVar(LogLevel.valueOf(4), "入参0", __BUILTIN__TEMP__.getItem("pkgName"));
                        __BUILTIN__TEMP__.setItem("bcName", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("transcode"));
                        logVar(LogLevel.valueOf(4), "入参1", __BUILTIN__TEMP__.getItem("bcName"));
                        bcScript = new cn.com.agree.afa.jcomponent.bc.BComponentInvoker(__REQ__, __RSP__, __BUILTIN__TEMP__);
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
                    		gatherStat("HostInterfaceInvoke_Node4", "调用主机系统通讯", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 0:
                            return new Node14();
                        case 1:
                            return new Node1();
                        }
                    }
                    return getExceptionHandler(new Node5());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node4", "调用主机系统通讯", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node6 主机响应时间赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("rspdatetime", __REQ__.getItem("_timestamp_")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node6", "主机响应时间赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node6", "主机响应时间赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node6", "主机响应时间赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node7 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node7 取工作日期时间");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        bcScript = new bc.CIPPublicmethod.GetWorkDateTime(__REQ__, __RSP__, __BUILTIN__TEMP__);
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
                    		gatherStat("HostInterfaceInvoke_Node7", "取工作日期时间", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 0:
                            return new Node14();
                        case 1:
                            return new Node13();
                        }
                    }
                    return getExceptionHandler(new Node5());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node7", "取工作日期时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node8 取主机序列号");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "sq_hostreqser";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = __REQ__.getItem("_DatabaseType_");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    int _arg3_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("reqseqlen").toString());
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.getSequence(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node8", "取主机序列号", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
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
                	        	gatherStat("HostInterfaceInvoke_Node8", "取主机序列号", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node5());
                        }
                        __REQ__.setItem("hostreqserno", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node8", "取主机序列号", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node8", "取主机序列号", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node9 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node9 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node10 更新主机通迅流水");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("serverhost", __REQ__.getItem("serverhost"), "hostreqdate", __REQ__.getItem("hostreqdate"), "hostreqserno", __REQ__.getItem("hostreqserno"), "hostrspdate", __REQ__.getItem("hostrspdate"), "hostrspserno", __REQ__.getItem("hostrspserno"), "h_ret_status", __REQ__.getItem("h_ret_status"), "h_ret_code", __REQ__.getItem("h_ret_code"), "h_ret_desc", __REQ__.getItem("h_ret_desc"), "rspdatetime", __REQ__.getItem("rspdatetime"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "UpdHostRspInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "CIPPublicmethod", "transcode", "HostInterfaceInvoke");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node10", "更新主机通迅流水", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
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
                	        	gatherStat("HostInterfaceInvoke_Node10", "更新主机通迅流水", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node5());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node10", "更新主机通迅流水", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node10", "更新主机通迅流水", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node11 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node11 默认逻辑错误委托");
                setExceptionHandler(new Node2());
                log(LogLevel.INFO, "将默认异常委托到Node2节点");
                return new Node16();
            }    
        }
        
        private class Node12 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node12 开始");
                return new Node11();
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node13 主机请求信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("hostreqdate", __REQ__.getItem("_workdate_")), new JavaList("reqdatetime", __REQ__.getItem("_timestamp_")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node13", "主机请求信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node13", "主机请求信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node13", "主机请求信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node14 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node14 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node15 主机返回是否成功");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("h_ret_status").equals("S");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node15", "主机返回是否成功", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node15", "主机返回是否成功", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node18();
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node15", "主机返回是否成功", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node16 创建通讯参数字典");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_ExchangeCfg_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node16", "创建通讯参数字典", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node16", "创建通讯参数字典", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node16", "创建通讯参数字典", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node17 获取主机通讯参数");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ExchangeCfg_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList("3");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "HostExchCfg";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = __REQ__.getItem("serverhost");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_SysParam.B_InitSysParam(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("HostInterfaceInvoke_Node17", "获取主机通讯参数", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("HostInterfaceInvoke_Node17", "获取主机通讯参数", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("HostInterfaceInvoke_Node17", "获取主机通讯参数", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node18 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "HostInterfaceInvoke_Node18 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
    
    }
    public static class GetWorkDateTime extends BCScript {
        private INode startNode;
        public GetWorkDateTime(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node4();
                log(LogLevel.INFO, "开始运行业务组件 取工作日期时间");
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
                log(LogLevel.INFO, "GetWorkDateTime_Node1 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetWorkDateTime_Node2 默认逻辑错误委托");
                setExceptionHandler(new Node8());
                log(LogLevel.INFO, "将默认异常委托到Node8节点");
                return new Node10();
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetWorkDateTime_Node4 开始");
                return new Node2();
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetWorkDateTime_Node6 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetWorkDateTime_Node8 取全局错误到容器");
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
                        	gatherStat("GetWorkDateTime_Node8", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetWorkDateTime_Node8", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetWorkDateTime_Node8", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetWorkDateTime_Node10 获取交易时间");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Time.P_getTime();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetWorkDateTime_Node10", "获取交易时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
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
                	        	gatherStat("GetWorkDateTime_Node10", "获取交易时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __REQ__.setItem("_workdate_", outputParams.get(0));
                        logVar(LogLevel.valueOf(3), "出参0", outputParams.get(0));
                        __REQ__.setItem("_worktime_", outputParams.get(1));
                        logVar(LogLevel.valueOf(0), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetWorkDateTime_Node10", "获取交易时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetWorkDateTime_Node10", "获取交易时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetWorkDateTime_Node11 参数化取时间戳");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(0), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetWorkDateTime_Node11", "参数化取时间戳", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
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
                	        	gatherStat("GetWorkDateTime_Node11", "参数化取时间戳", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __REQ__.setItem("_timestamp_", outputParams.get(0));
                        logVar(LogLevel.valueOf(3), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetWorkDateTime_Node11", "参数化取时间戳", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetWorkDateTime_Node11", "参数化取时间戳", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
    
    }

      
}