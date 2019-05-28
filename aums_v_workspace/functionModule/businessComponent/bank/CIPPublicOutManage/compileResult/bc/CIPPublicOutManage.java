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
import tc.bank.pcva.B_BusiCheck;
import tc.bank.product.B_ConstantOperate;
import tc.bank.product.B_DBUnityRptOper;
import tc.platform.P_Debug;
import tc.platform.P_Dict;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_Object;

/**
 * 业务组件包名称：CIPPublicOutManage <br/>
 *
 * 业务组件包描述：CIPPublicOutManage <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class CIPPublicOutManage {

    public static class PubRspCodeJudge extends BCScript {
        private INode startNode;
        public PubRspCodeJudge(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node6();
                log(LogLevel.INFO, "开始运行业务组件 公共响应码判断");
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
                log(LogLevel.INFO, "PubRspCodeJudge_Node1 响应码是否成功");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem(((JavaDict)((List)__REQ__.getItem("_CommRspCodeMapInfo_")).get(0)).getItem("JUDGEKEY").toString()).toString().equals(((JavaDict)((List)__REQ__.getItem("_CommRspCodeMapInfo_")).get(0)).getItem("JUDGEVALUE").toString());
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PubRspCodeJudge_Node1", "响应码是否成功", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PubRspCodeJudge_Node1", "响应码是否成功", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node4();
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PubRspCodeJudge_Node1", "响应码是否成功", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node2 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node3 统一成功响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "Success";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PubRspCodeJudge_Node3", "统一成功响应码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
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
                	        	gatherStat("PubRspCodeJudge_Node3", "统一成功响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        ((JavaDict)__RSP__.getItem("_CommRSP_")).setItem("h_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PubRspCodeJudge_Node3", "统一成功响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PubRspCodeJudge_Node3", "统一成功响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node4 获取错误信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("h_ret_code", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem(((JavaDict)((List)__REQ__.getItem("_CommRspCodeMapInfo_")).get(0)).getItem("RSPCODEKEY").toString())), new JavaList("h_ret_desc", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem(((JavaDict)((List)__REQ__.getItem("_CommRspCodeMapInfo_")).get(0)).getItem("RSPMSGKEY").toString(),"")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PubRspCodeJudge_Node4", "获取错误信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PubRspCodeJudge_Node4", "获取错误信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PubRspCodeJudge_Node4", "获取错误信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node6 开始");
                return new Node27();
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node8 默认响应码比较");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "Success";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_code");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = B_ConstantOperate.B_RespCodeCompare(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PubRspCodeJudge_Node8", "默认响应码比较", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PubRspCodeJudge_Node8", "默认响应码比较", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node2();
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PubRspCodeJudge_Node8", "默认响应码比较", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node17 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node17 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node20 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node20 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node21 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node21 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "h_excptype";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "h_ret_code";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "h_ret_desc";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = GlobalErrorHolder.putGlobalErrorToDict(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PubRspCodeJudge_Node21", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PubRspCodeJudge_Node21", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PubRspCodeJudge_Node21", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node22 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node22 查询通讯服务码配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("SERVICEID", __REQ__.getItem("SERVERCD"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetCommRspCodeMapInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPPublicOutManage", "transcode", "PubRspCodeJudge");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PubRspCodeJudge_Node22", "查询通讯服务码配置", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    List<?> outputParams = result.getOutputParams();
                    if (outputParams != null) {
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("PubRspCodeJudge_Node22", "查询通讯服务码配置", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("_CommRspCodeMapInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("_CommRspCodeMapInfoCount_", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PubRspCodeJudge_Node22", "查询通讯服务码配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    case 2:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PubRspCodeJudge_Node22", "查询通讯服务码配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node27 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PubRspCodeJudge_Node27 默认逻辑错误委托");
                setExceptionHandler(new Node21());
                log(LogLevel.INFO, "将默认异常委托到Node21节点");
                return new Node22();
            }    
        }
        
    
    }
    public static class PublicOutAdapter extends BCScript {
        private INode startNode;
        public PublicOutAdapter(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node18();
                log(LogLevel.INFO, "开始运行业务组件 公共接出配置");
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
                log(LogLevel.INFO, "PublicOutAdapter_Node1 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node2 查询通讯属性配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("COMMTYPE", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("COMMTYPE"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetCommArrtInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPPublicOutManage", "transcode", "PublicOutAdapter");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node2", "查询通讯属性配置", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("PublicOutAdapter_Node2", "查询通讯属性配置", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __REQ__.setItem("_CommAttrInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("_CommAttrInfoCount_", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node2", "查询通讯属性配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    case 2:
                        return new Node25();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node2", "查询通讯属性配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node3 没有指定超时时间");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("COMMTIMEOUT")==null||"".equals(((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("COMMTIMEOUT"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node3", "没有指定超时时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node3", "没有指定超时时间", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node6();
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node3", "没有指定超时时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node4 通讯超时赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ExchangeCfg_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("TIMEOUT", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("TIMEOUT")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node4", "通讯超时赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node4", "通讯超时赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node4", "通讯超时赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node5 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node6 通讯超时赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ExchangeCfg_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("TIMEOUT", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("COMMTIMEOUT")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node6", "通讯超时赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node6", "通讯超时赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node6", "通讯超时赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node7 创建请求配置容器");
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
                        	gatherStat("PublicOutAdapter_Node7", "创建请求配置容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node7", "创建请求配置容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node22();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node7", "创建请求配置容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node8 取全局错误到容器");
                setExceptionHandler(null);
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
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
                        	gatherStat("PublicOutAdapter_Node8", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node8", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node31();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node8", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node9 是否包含文件传输");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "true".equals(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("isfiletrans").toString());
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node9", "是否包含文件传输", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node9", "是否包含文件传输", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node10();
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node9", "是否包含文件传输", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node10 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node10 调用通讯组件");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        __BUILTIN__TEMP__.setItem("pkgName", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("PACKAGENAME"));
                        logVar(LogLevel.valueOf(4), "入参0", __BUILTIN__TEMP__.getItem("pkgName"));
                        __BUILTIN__TEMP__.setItem("bcName", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("COMPONENTNAME"));
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
                    		gatherStat("PublicOutAdapter_Node10", "调用通讯组件", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 1:
                            return new Node21();
                        }
                    }
                    return getExceptionHandler(new Node1());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node10", "调用通讯组件", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "PublicOutAdapter_Node11 查询文件传输配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("LOCALCODE", __REQ__.getItem("_localcode_"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetFileTransMapInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPPublicOutManage", "transcode", "PublicOutAdapter");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node11", "查询文件传输配置", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("PublicOutAdapter_Node11", "查询文件传输配置", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __REQ__.setItem("_FileTransMapInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("_FileTransMapInfoCount_", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node11", "查询文件传输配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node11", "查询文件传输配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node12 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node12 文件传输信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ExchangeCfg_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("filetranstype", ((JavaDict)((List)__REQ__.getItem("_FileTransMapInfo_")).get(0)).getItem("SERVICECODE")), new JavaList("fileflag", ((JavaDict)((List)__REQ__.getItem("_FileTransMapInfo_")).get(0)).getItem("SERVICESCENE")), new JavaList("fileserviceip", ((JavaDict)((List)__REQ__.getItem("_FileTransMapInfo_")).get(0)).getItem("SERVICEID")), new JavaList("fileserviceport", ((JavaDict)((List)__REQ__.getItem("_FileTransMapInfo_")).get(0)).getItem("SERVICEENAME")), new JavaList("fileserviceid", ((JavaDict)((List)__REQ__.getItem("_FileTransMapInfo_")).get(0)).getItem("ISFILETRANS")), new JavaList("fileserviceuser", ((JavaDict)((List)__REQ__.getItem("_FileTransMapInfo_")).get(0)).getItem("DESC")), new JavaList("fileservicepasswd", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("SERVICEIP")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node12", "文件传输信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node12", "文件传输信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node12", "文件传输信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node13 是否上传");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "up".equals(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("fileflag").toString());
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node13", "是否上传", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node13", "是否上传", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node10();
                    case 1:
                        return new Node27();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node13", "是否上传", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node14 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node14 默认逻辑错误委托");
                setExceptionHandler(new Node8());
                log(LogLevel.INFO, "将默认异常委托到Node8节点");
                return new Node29();
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node15 查询通讯服务配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("LOCALCODE", __REQ__.getItem("_localcode_"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetCommMapInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPPublicOutManage", "transcode", "PublicOutAdapter");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node15", "查询通讯服务配置", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("PublicOutAdapter_Node15", "查询通讯服务配置", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __REQ__.setItem("_CommMapInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("_CommMapInfoCount_", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node15", "查询通讯服务配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    case 2:
                        return new Node24();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node15", "查询通讯服务配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node16 是否下载");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "down".equals(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("fileflag").toString());
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node16", "是否下载", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node16", "是否下载", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node30();
                    case 1:
                        return new Node28();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node16", "是否下载", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node17 通讯信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ExchangeCfg_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("servicecode", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERVICECODE")), new JavaList("servicescene", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERVICESCENE")), new JavaList("serviceid", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERVICEID")), new JavaList("serviceename", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERVICEENAME")), new JavaList("isfiletrans", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("ISFILETRANS")), new JavaList("serviceoper", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERVICEOPER")), new JavaList("sertranscode", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERTRANSCODE")), new JavaList("servicetype", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("SERVICETYPE")), new JavaList("des", ((JavaDict)((List)__REQ__.getItem("_CommMapInfo_")).get(0)).getItem("DES")), new JavaList("IP", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("SERVICEIP")), new JavaList("PORT", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("SERVICEPORT")), new JavaList("headlen", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("HEADLEN")), new JavaList("containflag", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("ISSELF")), new JavaList("encoding", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("ENCODING")), new JavaList("rootname", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("ROOTNAME")), new JavaList("PACKAGENAME", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("PACKAGENAME")), new JavaList("COMPONENTNAME", ((JavaDict)((List)__REQ__.getItem("_CommAttrInfo_")).get(0)).getItem("COMPONENTNAME")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node17", "通讯信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node17", "通讯信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node17", "通讯信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node18 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node18 开始");
                return new Node14();
            }    
        }
        
        private class Node19 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node19 错误信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_excptype","")), new JavaList("p_ret_code", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_code","")), new JavaList("p_ret_desc", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_desc","")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node19", "错误信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node19", "错误信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node23();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node19", "错误信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node20 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node20 是否包含文件传输");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "true".equals(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("isfiletrans").toString());
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node20", "是否包含文件传输", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node20", "是否包含文件传输", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node30();
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node20", "是否包含文件传输", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node21 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node21 响应码比较");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "Success";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_code");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = B_ConstantOperate.B_RespCodeCompare(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node21", "响应码比较", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node21", "响应码比较", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node19();
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node21", "响应码比较", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node22 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node22 创建应答容器");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__;
                    log(LogLevel.valueOf(4), "入参0=__RSP__");
                    JavaList _arg1_ = new JavaList(new JavaList("_CommRSP_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node22", "创建应答容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node22", "创建应答容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node22", "创建应答容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node23 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node23 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node24 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node24 通讯服务未配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("p_ret_code", "COMM001"), new JavaList("p_ret_desc", "通讯服务未配置"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node24", "通讯服务未配置", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node24", "通讯服务未配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node26();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node24", "通讯服务未配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node25 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node25 通讯属性为配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("p_ret_code", "COMM002"), new JavaList("p_ret_desc", "通讯属性未配置"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node25", "通讯属性为配置", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node25", "通讯属性为配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node26();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node25", "通讯属性为配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node26 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node26 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node27 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node27 空组件");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Debug.pass();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node27", "空组件", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node27", "空组件", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node27", "空组件", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node28 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node28 空组件");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Debug.pass();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node28", "空组件", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node28", "空组件", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node30();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node28", "空组件", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node29 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node29 必输参数检查");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_localcode_"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = B_BusiCheck.B_CheckValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node29", "必输参数检查", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node29", "必输参数检查", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node29", "必输参数检查", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node30 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node30 删除通讯码");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_localcode_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node30", "删除通讯码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node30", "删除通讯码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node30", "删除通讯码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node31 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PublicOutAdapter_Node31 删除通讯码");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_localcode_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PublicOutAdapter_Node31", "删除通讯码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PublicOutAdapter_Node31", "删除通讯码", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node1();
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PublicOutAdapter_Node31", "删除通讯码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
    
    }

      
}