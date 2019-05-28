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
import tc.bank.product.B_AppInterfaceMng;
import tc.bank.product.B_ConstantOperate;
import tc.bank.product.B_DBUnityAltOper;
import tc.platform.P_Communition;
import tc.platform.P_Dict;
import tc.platform.P_Jdbc;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_Object;
import tc.platform.P_Time;

/**
 * 业务组件包名称：HostInteractiveManage <br/>
 *
 * 业务组件包描述：HostInteractiveManage <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class HostInteractiveManage {

    public static class GatewayComm extends BCScript {
        private INode startNode;
        public GatewayComm(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node32();
                log(LogLevel.INFO, "开始运行业务组件 网关通讯组件");
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
                log(LogLevel.INFO, "GatewayComm_Node1 响应时间戳");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node1", "响应时间戳", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node1", "响应时间戳", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("RSPDATETIME", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node1", "响应时间戳", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node22();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node1", "响应时间戳", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node2 更新通讯结果信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("RSPDATE", ((JavaDict)__RSP__.getItem("_CommRSP_")).getStringItem("RSPDATE",""), "RSPSERNO", ((JavaDict)__RSP__.getItem("_CommRSP_")).getStringItem("RSPSERNO",""), "TRADESTATUS", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("TRADESTATUS"), "RSPCODE", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_code"), "RSPMSG", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_desc"), "RECVMSG", __REQ__.getItem("RECVMSG"), "RSPDATETIME", __REQ__.getItem("RSPDATETIME"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "Upd_ExchMsgBook";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("WORKDATE", "=", __REQ__.getItem("workdate"), "and"), new JavaList("AGENTSERIALNO", "=", __REQ__.getItem("agentserialno"), "and"), new JavaList("TRADESTEP", "=", __REQ__.getItem("TRADESTEP"))));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "HostInteractiveManage", "transcode", "ESBComm");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node2", "更新通讯结果信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node2", "更新通讯结果信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node2", "更新通讯结果信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node43();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node2", "更新通讯结果信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node3 成功信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("h_ret_status", "S"), new JavaList("TRADESTATUS", "0"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node3", "成功信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node3", "成功信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node49();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node3", "成功信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node4 创建请求字典");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("_ESBReq_", new JavaDict()), new JavaList("_ESBRsp_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node4", "创建请求字典", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node4", "创建请求字典", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node25();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node4", "创建请求字典", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node5 登记信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("TRADESTEP", "1"), new JavaList("WORKDATE", __REQ__.getItem("workdate")), new JavaList("AGENTSERIALNO", __REQ__.getItem("agentserialno")), new JavaList("PRODUCTCODE", __REQ__.getItem("SERVERCD")), new JavaList("SERVICECODE", __REQ__.getItem("E_SERVICECD")), new JavaList("REQDATE", __REQ__.getItem("REQDATE")), new JavaList("REQSERNO", __REQ__.getItem("REQSERNO")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node5", "登记信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node5", "登记信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node33();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node5", "登记信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node6 NATP客户端3.0");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ESBReq_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = __REQ__.getItem("modulecode");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = __REQ__.getItem("transcode");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = __REQ__.getItem("transcode");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    String _arg4_ = ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("IP");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    int _arg5_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("PORT").toString());
                    logVar(LogLevel.valueOf(4), "入参5", _arg5_);
                    String _arg6_ = ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("encoding");
                    logVar(LogLevel.valueOf(4), "入参6", _arg6_);
                    int _arg7_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("TIMEOUT").toString());
                    logVar(LogLevel.valueOf(4), "入参7", _arg7_);
                    TCResult result = P_Communition.natpClient_3(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_, _arg5_, _arg6_, _arg7_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node6", "NATP客户端3.0", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node6", "NATP客户端3.0", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("_ESBRsp_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node6", "NATP客户端3.0", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node17();
                    case 1:
                        return new Node24();
                    case 2:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node6", "NATP客户端3.0", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node8 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "Success";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node8", "获取响应码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node8", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        ((JavaDict)__RSP__.getItem("_CommRSP_")).setItem("h_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node8", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node40();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node8", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node9 必输参数检查");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("workdate"), new JavaList("agentserialno"), new JavaList("SERVERCD"), new JavaList("E_SERVICECD"), new JavaList("E_OPERATION"), new JavaList("_ver_"), new JavaList("_CLIENTCD_"), new JavaList("_TRADETYPE_"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = B_BusiCheck.B_CheckValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node9", "必输参数检查", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node9", "必输参数检查", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node9", "必输参数检查", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node10 公共报文头赋值");
                return new Node28();
            }    
        }
        
        private class Node11 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node11 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node12 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node12 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node14 响应时间戳");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node14", "响应时间戳", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node14", "响应时间戳", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("RSPDATETIME", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node14", "响应时间戳", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node41();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node14", "响应时间戳", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node15 通讯是否成功");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("RESCODE").equals("000000");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node15", "通讯是否成功", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node15", "通讯是否成功", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node30();
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node15", "通讯是否成功", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node16 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node16 公共响应码判断");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        bcScript = new bc.CIPPublicOutManage.PubRspCodeJudge(__REQ__, __RSP__, __BUILTIN__TEMP__);
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
                    		gatherStat("GatewayComm_Node16", "公共响应码判断", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 0:
                            return new Node38();
                        case 1:
                            return new Node39();
                        }
                    }
                    return getExceptionHandler(new Node11());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node16", "公共响应码判断", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node17 响应时间戳");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node17", "响应时间戳", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node17", "响应时间戳", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("RSPDATETIME", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node17", "响应时间戳", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node27();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node17", "响应时间戳", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node18 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node18 更新通讯超时信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("TRADESTATUS", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("TRADESTATUS"), "RSPCODE", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_code"), "RSPMSG", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("h_ret_desc"), "RSPDATETIME", __REQ__.getItem("RSPDATETIME"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "Upd_ExchMsgBook";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("WORKDATE", "=", __REQ__.getItem("workdate"), "and"), new JavaList("AGENTSERIALNO", "=", __REQ__.getItem("agentserialno"), "and"), new JavaList("TRADESTEP", "=", __REQ__.getItem("TRADESTEP"))));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "HostInteractiveManage", "transcode", "ESBComm");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node18", "更新通讯超时信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node18", "更新通讯超时信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node18", "更新通讯超时信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node18", "更新通讯超时信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node20 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node20 获取交易时间");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Time.P_getTime();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node20", "获取交易时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node20", "获取交易时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("REQDATE", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __REQ__.setItem("REQTIME", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node20", "获取交易时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node23();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node20", "获取交易时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node22 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node22 获取超时响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "HostCommTimeout";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node22", "获取超时响应码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node22", "获取超时响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        ((JavaDict)__RSP__.getItem("_CommRSP_")).setItem("h_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node22", "获取超时响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node52();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node22", "获取超时响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node23 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node23 取通讯流水");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "ESB_MESGSERNO";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = __REQ__.getItem("_DatabaseType_");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    int _arg3_ = 4;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Jdbc.getSequence(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node23", "取通讯流水", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node23", "取通讯流水", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("REQSERNO", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node23", "取通讯流水", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node23", "取通讯流水", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node24 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node24 响应时间戳");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node24", "响应时间戳", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node24", "响应时间戳", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("RSPDATETIME", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node24", "响应时间戳", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node53();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node24", "响应时间戳", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node25 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node25 创建响应字典");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__;
                    log(LogLevel.valueOf(4), "入参0=__RSP__");
                    JavaList _arg1_ = new JavaList(new JavaList("ESBRESULT", new JavaDict()), new JavaList("_CommRSP_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node25", "创建响应字典", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node25", "创建响应字典", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node25", "创建响应字典", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node27 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node27 异常信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("TRADESTATUS", "2"), new JavaList("h_ret_desc", __REQ__.getItem("p_ret_desc")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node27", "异常信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node27", "异常信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node27", "异常信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node28 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node28 处理接口请求信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "COMMON";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(__REQ__.getItem("E_SERVICECD")+".req");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "2";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = __REQ__.getItem("_ESBReq_");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    boolean _arg5_ = true;
                    logVar(LogLevel.valueOf(4), "入参5", _arg5_);
                    boolean _arg6_ = true;
                    logVar(LogLevel.valueOf(4), "入参6", _arg6_);
                    boolean _arg7_ = true;
                    logVar(LogLevel.valueOf(4), "入参7", _arg7_);
                    TCResult result = B_AppInterfaceMng.B_ProcInMsg(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_, _arg5_, _arg6_, _arg7_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node28", "处理接口请求信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node28", "处理接口请求信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node28", "处理接口请求信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node29 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node29 处理接口请求信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__.getItem("_ESBRsp_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "COMMON";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList("GatewayRspHead", __REQ__.getItem("E_SERVICECD")+".rsp");
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "1";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    boolean _arg5_ = false;
                    logVar(LogLevel.valueOf(4), "入参5", _arg5_);
                    boolean _arg6_ = true;
                    logVar(LogLevel.valueOf(4), "入参6", _arg6_);
                    boolean _arg7_ = true;
                    logVar(LogLevel.valueOf(4), "入参7", _arg7_);
                    TCResult result = B_AppInterfaceMng.B_ProcInMsg(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_, _arg5_, _arg6_, _arg7_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node29", "处理接口请求信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node29", "处理接口请求信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node29", "处理接口请求信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node30 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node30 通讯失败赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("h_ret_code", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("RESCODE")), new JavaList("h_ret_desc", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("RESTEXT")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node30", "通讯失败赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node30", "通讯失败赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node38();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node30", "通讯失败赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node32 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node32 开始");
                return new Node51();
            }    
        }
        
        private class Node33 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node33 请求时间戳");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node33", "请求时间戳", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node33", "请求时间戳", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                        __REQ__.setItem("REQDATETIME", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node33", "请求时间戳", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node35();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node33", "请求时间戳", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node35 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node35 登记通讯信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "Int_ExchMsgBook";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "HostInteractiveManage", "transcode", "ESBComm");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node35", "登记通讯信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node35", "登记通讯信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node35", "登记通讯信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node45();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node35", "登记通讯信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node37 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node37 取全局错误到容器");
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
                        	gatherStat("GatewayComm_Node37", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node37", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node37", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node38 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node38 错误信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("h_ret_status", "F"), new JavaList("TRADESTATUS", "1"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node38", "错误信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node38", "错误信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node38", "错误信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node39 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node39 成功信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("h_ret_status", "S"), new JavaList("TRADESTATUS", "0"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node39", "成功信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node39", "成功信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node39", "成功信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node40 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node40 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node41 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node41 返回报文赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("RECVMSG", "非账务类不登记"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node41", "返回报文赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node41", "返回报文赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node41", "返回报文赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node43 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node43 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node44 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node44 返回报文赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("RECVMSG", "非账务类不登记"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node44", "返回报文赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node44", "返回报文赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node29();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node44", "返回报文赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node45 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node45 是否不接收响应");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getStringItem("__isReturn__","").equals("0");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node45", "是否不接收响应", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node45", "是否不接收响应", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node6();
                    case 1:
                        return new Node54();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node45", "是否不接收响应", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node47 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node47 服务码及ID赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("E_OPERATION", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("serviceoper")), new JavaList("SERTRANSCODE", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("sertranscode")), new JavaList("_TRADETYPE_", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("servicetype")), new JavaList("SERVERCD", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("serviceid")), new JavaList("E_SERVICECD", ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("servicecode")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node47", "服务码及ID赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node47", "服务码及ID赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node47", "服务码及ID赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node49 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node49 更新通讯结果信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("RSPDATE", ((JavaDict)__RSP__.getItem("_CommRSP_")).getStringItem("RSPDATE",""), "RSPSERNO", ((JavaDict)__RSP__.getItem("_CommRSP_")).getStringItem("RSPSERNO",""), "TRADESTATUS", ((JavaDict)__RSP__.getItem("_CommRSP_")).getItem("TRADESTATUS"), "RSPCODE", "000000", "RSPMSG", "通讯成功", "RECVMSG", __REQ__.getItem("RECVMSG"), "RSPDATETIME", __REQ__.getItem("RSPDATETIME"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "Upd_ExchMsgBook";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("WORKDATE", "=", __REQ__.getItem("workdate"), "and"), new JavaList("AGENTSERIALNO", "=", __REQ__.getItem("agentserialno"), "and"), new JavaList("TRADESTEP", "=", __REQ__.getItem("TRADESTEP"))));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "HostInteractiveManage", "transcode", "ESBComm");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node49", "更新通讯结果信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
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
                	        	gatherStat("GatewayComm_Node49", "更新通讯结果信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node11());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node49", "更新通讯结果信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node49", "更新通讯结果信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node51 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node51 默认逻辑错误委托");
                setExceptionHandler(new Node37());
                log(LogLevel.INFO, "将默认异常委托到Node37节点");
                return new Node47();
            }    
        }
        
        private class Node52 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node52 超时信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __RSP__.getItem("_CommRSP_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("TRADESTATUS", "2"), new JavaList("h_ret_desc", "发送后台信息超时"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node52", "超时信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node52", "超时信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node52", "超时信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node53 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node53 是否账务类");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("_TRADETYPE_").equals("0");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node53", "是否账务类", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node53", "是否账务类", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node44();
                    case 1:
                        return new Node55();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node53", "是否账务类", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node54 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node54 TCP客户端，不接收响应");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("IP");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    int _arg1_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("PORT").toString());
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("TIMEOUT").toString());
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    int _arg3_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("headlen").toString());
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    int _arg4_ = Integer.parseInt(((JavaDict)__REQ__.getItem("_ExchangeCfg_")).getItem("containflag").toString());
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    byte[] _arg5_ = ((JavaDict)__REQ__.getItem("_ESBReq_")).getItem("testbyte");
                    logVar(LogLevel.valueOf(4), "入参5", _arg5_);
                    TCResult result = P_Communition.simpleClientWithoutRsp(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_, _arg5_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node54", "TCP客户端，不接收响应", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node54", "TCP客户端，不接收响应", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node17();
                    case 1:
                        return new Node14();
                    case 2:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node54", "TCP客户端，不接收响应", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
        private class Node55 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GatewayComm_Node55 返回报文赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("RECVMSG", ((JavaDict)__REQ__.getItem("_ESBRsp_")).getItem("RspPkg")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GatewayComm_Node55", "返回报文赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node11());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GatewayComm_Node55", "返回报文赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node29();
                    default:
                        return getExceptionHandler(new Node11());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GatewayComm_Node55", "返回报文赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node11());
                }
            }    
        }
        
    
    }

      
}