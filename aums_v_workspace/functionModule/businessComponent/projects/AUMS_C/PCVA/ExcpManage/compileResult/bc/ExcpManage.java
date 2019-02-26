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
import tc.AUMS_C.PCVA.product.custom.A_DealTaskMethod;
import tc.platform.P_Dict;
import tc.platform.P_Json;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_Object;

/**
 * 业务组件包名称：ExcpManage <br/>
 *
 * 业务组件包描述：ExcpManage <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class ExcpManage {

    public static class PCVA_C004 extends BCScript {
        private INode startNode;
        public PCVA_C004(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node2();
                log(LogLevel.INFO, "开始运行业务组件 吞卡处理类");
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
                log(LogLevel.INFO, "PCVA_C004_Node1 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Result_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
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
                        	gatherStat("PCVA_C004_Node1", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node1", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node1", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node2 开始");
                return new Node4();
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node4 默认逻辑错误委托");
                setExceptionHandler(new Node1());
                log(LogLevel.INFO, "将默认异常委托到Node1节点");
                return new Node9();
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node5 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node6 SelectCardType结点");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "SelectCardType".equals(__REQ__.getStringItem("NODEID",""));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node6", "SelectCardType结点", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node6", "SelectCardType结点", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node7();
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node6", "SelectCardType结点", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node7 判断流程结点");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "TakeCard".equals(__REQ__.getStringItem("NODEID",""));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node7", "判断流程结点", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node7", "判断流程结点", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node10();
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node7", "判断流程结点", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node8 业务数据判断");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("BUSIDATA")==null||"".equals(__REQ__.getItem("BUSIDATA"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node8", "业务数据判断", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node8", "业务数据判断", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node17();
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node8", "业务数据判断", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node9 初始化临时容器");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_Tmp_Map_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node9", "初始化临时容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node9", "初始化临时容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node9", "初始化临时容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node10 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node10 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node11 处理赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Tmp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("RETCARDFLAG", "1"), new JavaList("ERRORCODE", __REQ__.getItem("DEVERRORCODE")), new JavaList("ERRORMSG", ((JavaDict)__REQ__.getItem("_ExpTradeData_")).getStringItem("SCENEDESC","")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node11", "处理赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node11", "处理赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node11", "处理赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "PCVA_C004_Node12 处理赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Tmp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("RETCARDFLAG", "0"), new JavaList("ERRORCODE", __REQ__.getItem("DEVERRORCODE")), new JavaList("ERRORMSG", ((JavaDict)__REQ__.getItem("_ExpTradeData_")).getStringItem("SCENEDESC","")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node12", "处理赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node12", "处理赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node12", "处理赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "PCVA_C004_Node13 javaDict转换格式化json串");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Tmp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Json.dictToFormattedStr(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node13", "javaDict转换格式化json串", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("PCVA_C004_Node13", "javaDict转换格式化json串", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("_Tmp_Map_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node13", "javaDict转换格式化json串", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node13", "javaDict转换格式化json串", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node15 直接取赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("BUSIDATA", __REQ__.getItem("_Tmp_Map_")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node15", "直接取赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node15", "直接取赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node15", "直接取赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PCVA_C004_Node17 合并Json");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __REQ__.getItem("_Tmp_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = __REQ__.getItem("BUSIDATA");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = A_DealTaskMethod.B_GerNewJson(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PCVA_C004_Node17", "合并Json", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("PCVA_C004_Node17", "合并Json", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("BUSIDATA", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PCVA_C004_Node17", "合并Json", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PCVA_C004_Node17", "合并Json", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }
    public static class ProcessValues extends BCScript {
        private INode startNode;
        public ProcessValues(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node1();
                log(LogLevel.INFO, "开始运行业务组件 处理子交易相关值");
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
                log(LogLevel.INFO, "ProcessValues_Node1 开始");
                return new Node2();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node2 默认逻辑错误委托");
                setExceptionHandler(new Node4());
                log(LogLevel.INFO, "将默认异常委托到Node4节点");
                return new Node11();
            }    
        }
        
        private class Node3 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node3 动态调用");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        __BUILTIN__TEMP__.setItem("pkgName", "ExcpManage");
                        logVar(LogLevel.valueOf(4), "入参0", __BUILTIN__TEMP__.getItem("pkgName"));
                        __BUILTIN__TEMP__.setItem("bcName", __BUILTIN__.getItem("TradeFlow"));
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
                    		gatherStat("ProcessValues_Node3", "动态调用", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 0:
                            return new Node9();
                        case 1:
                            return new Node8();
                        }
                    }
                    return getExceptionHandler(new Node5());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ProcessValues_Node3", "动态调用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node4 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_Result_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
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
                        	gatherStat("ProcessValues_Node4", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ProcessValues_Node4", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ProcessValues_Node4", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ProcessValues_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node6 交易流程匹配");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("TradeFlow");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "PCVA_C004";
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
                        	gatherStat("ProcessValues_Node6", "交易流程匹配", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ProcessValues_Node6", "交易流程匹配", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node7();
                    case 1:
                        return new Node3();
                    case 2:
                        return new Node3();
                    case 3:
                        return new Node3();
                    case 4:
                        return new Node3();
                    case 5:
                        return new Node3();
                    case 6:
                        return new Node3();
                    case 7:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ProcessValues_Node6", "交易流程匹配", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node7 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node8 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node9 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node9 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node10 javaDict转换json串");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("BUSIDATA");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Json.dictToStr(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ProcessValues_Node10", "javaDict转换json串", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ProcessValues_Node10", "javaDict转换json串", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node5());
                        }
                        __REQ__.setItem("BUSIDATA", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ProcessValues_Node10", "javaDict转换json串", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ProcessValues_Node10", "javaDict转换json串", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ProcessValues_Node11 业务数据判断");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("BUSIDATA")==null||"".equals(__REQ__.getItem("BUSIDATA"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ProcessValues_Node11", "业务数据判断", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ProcessValues_Node11", "业务数据判断", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node10();
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ProcessValues_Node11", "业务数据判断", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
    
    }

      
}