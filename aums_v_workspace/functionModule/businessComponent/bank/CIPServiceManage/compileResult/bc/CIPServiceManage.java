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
import tc.bank.product.B_ConstantOperate;
import tc.bank.product.B_DBUnityRptOper;
import tc.platform.P_Dict;
import tc.platform.P_JudgmentStatement;

/**
 * 业务组件包名称：CIPServiceManage <br/>
 *
 * 业务组件包描述：CIPServiceManage <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class CIPServiceManage {

    public static class ServiceChlCheck extends BCScript {
        private INode startNode;
        public ServiceChlCheck(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node10();
                log(LogLevel.INFO, "开始运行业务组件 服务开放渠道校验");
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
                log(LogLevel.INFO, "ServiceChlCheck_Node1 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node2 服务开通渠道未配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "服务开通渠道未配置"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node2", "服务开通渠道未配置", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node2", "服务开通渠道未配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node2", "服务开通渠道未配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node4 服务开通渠道启用");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("svrchlstatus").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node4", "服务开通渠道启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node4", "服务开通渠道启用", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node5();
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node4", "服务开通渠道启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node5 服务未开通该渠道");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "服务未开通该渠道"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node5", "服务未开通该渠道", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node5", "服务未开通该渠道", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node19();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node5", "服务未开通该渠道", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node6 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node7 默认逻辑错误委托");
                setExceptionHandler(new Node17());
                log(LogLevel.INFO, "将默认异常委托到Node17节点");
                return new Node22();
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node8 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node9 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_ServiceChlInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node9", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node9", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node9", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node10 开始");
                return new Node7();
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node11 服务渠道失效");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "服务开通渠道未生效或已失效"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node11", "服务渠道失效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node11", "服务渠道失效", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node11", "服务渠道失效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node12 服务渠道是否生效");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("svrchleffectdate").toString())>=0&&__REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("svrchlinvaliddate").toString())<=0;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node12", "服务渠道是否生效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node12", "服务渠道是否生效", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node11();
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node12", "服务渠道是否生效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node13 是否在服务时间");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("p_worktime").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("servicesttime").toString())>=0&&__REQ__.getItem("p_worktime").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("serviceendtime").toString())<=0;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node13", "是否在服务时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node13", "是否在服务时间", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node14();
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node13", "是否在服务时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node14 不在服务开放时间");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_code", "CIC014"), new JavaList("p_ret_desc", "请求不在服务的开放时间内"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node14", "不在服务开放时间", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node14", "不在服务开放时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node21();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node14", "不在服务开放时间", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node15 服务开放渠道赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("ServiceChlInfo", ((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)), new JavaList("svrinputcode", ((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("svrinputcode")), new JavaList("svroutputcode", ((JavaDict)((List)__REQ__.getItem("_ServiceChlInfo_")).get(0)).getItem("svroutputcode")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node15", "服务开放渠道赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node15", "服务开放渠道赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node15", "服务开放渠道赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node16 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node16 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceChlCheck_Node17 取全局错误到容器");
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
                        	gatherStat("ServiceChlCheck_Node17", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node17", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node17", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node18 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerOpenChlNotExist";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node18", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceChlCheck_Node18", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node18", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node18", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node19 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerNotOpenChl";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node19", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceChlCheck_Node19", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node19", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node19", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node20 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerOpenChlNotEffective";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node20", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceChlCheck_Node20", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node20", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node20", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node21 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "RequestIsNotInSerTime";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node21", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceChlCheck_Node21", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node21", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node21", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceChlCheck_Node22 查询服务开通渠道信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetServiceChlInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("p_servicecode", "=", __REQ__.getItem("p_servicecode"), "and"), new JavaList("channelcode", "in", "*,"+__REQ__.getItem("channelcode"))), "orderlist", new JavaList(new JavaList("channelcode"), new JavaList("DESC")), "pagelist", new JavaList(4, 1, 1));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "ServiceChlCheck");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceChlCheck_Node22", "查询服务开通渠道信息", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("ServiceChlCheck_Node22", "查询服务开通渠道信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("_ServiceChlInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceChlCheck_Node22", "查询服务开通渠道信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    case 2:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceChlCheck_Node22", "查询服务开通渠道信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }
    public static class ServiceBranchCheck extends BCScript {
        private INode startNode;
        public ServiceBranchCheck(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node10();
                log(LogLevel.INFO, "开始运行业务组件 服务开通机构校验");
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
                log(LogLevel.INFO, "ServiceBranchCheck_Node1 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_ServiceBranchInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node1", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node1", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node1", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node2 服务开通机构未配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "HOST"), new JavaList("p_ret_desc", "渠道对应的服务和机构信息未配置"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node2", "服务开通机构未配置", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node2", "服务开通机构未配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node2", "服务开通机构未配置", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node4 渠道对应服务机构启用");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__REQ__.getItem("_ServiceBranchInfo_")).get(0)).getItem("servicestatus").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node4", "渠道对应服务机构启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node4", "渠道对应服务机构启用", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node5();
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node4", "渠道对应服务机构启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node5 服务未开通该机构");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "HOST"), new JavaList("p_ret_desc", "外围渠道机构未开通该服务"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node5", "服务未开通该机构", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node5", "服务未开通该机构", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node5", "服务未开通该机构", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node6 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node7 默认逻辑错误委托");
                setExceptionHandler(new Node13());
                log(LogLevel.INFO, "将默认异常委托到Node13节点");
                return new Node17();
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node8 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node9 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node9 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node10 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node10 开始");
                return new Node7();
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node11 服务机构失效");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "HOST"), new JavaList("p_ret_desc", "外围渠道机构对应的服务未生效或已失效"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node11", "服务机构失效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node11", "服务机构失效", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node11", "服务机构失效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceBranchCheck_Node12 服务机构是否生效");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceBranchInfo_")).get(0)).getItem("svreffectdate").toString())>=0&&__REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceBranchInfo_")).get(0)).getItem("svrvaliddate").toString())<=0;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node12", "服务机构是否生效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node12", "服务机构是否生效", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node11();
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node12", "服务机构是否生效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceBranchCheck_Node13 取全局错误到容器");
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
                        	gatherStat("ServiceBranchCheck_Node13", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node13", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node13", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node14 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerOpenOrgNotExist";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node14", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceBranchCheck_Node14", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node14", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node14", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceBranchCheck_Node15 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerNotOpenOrg";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node15", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceBranchCheck_Node15", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node15", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node15", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceBranchCheck_Node16 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerOpenOrgNotEffective";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node16", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceBranchCheck_Node16", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node16", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node16", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceBranchCheck_Node17 查询服务开通机构信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetServiceBranch";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("p_servicecode", "in", "*,"+__REQ__.getItem("p_servicecode"), "and"), new JavaList("chanelcode", "in", "*,"+__REQ__.getItem("channelcode"), "and"), new JavaList("branch", "in", "*,"+__REQ__.getItem("branch"))), "orderlist", new JavaList(new JavaList("p_servicecode", "chanelcode", "branch"), new JavaList("DESC", "DESC", "DESC")), "pagelist", new JavaList(4, 1, 1));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "ServiceBranchCheck");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceBranchCheck_Node17", "查询服务开通机构信息", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("ServiceBranchCheck_Node17", "查询服务开通机构信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("_ServiceBranchInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceBranchCheck_Node17", "查询服务开通机构信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    case 2:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceBranchCheck_Node17", "查询服务开通机构信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }
    public static class ServiceMapping extends BCScript {
        private INode startNode;
        public ServiceMapping(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node2();
                log(LogLevel.INFO, "开始运行业务组件 服务码映射");
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
                log(LogLevel.INFO, "ServiceMapping_Node1 默认逻辑错误委托");
                setExceptionHandler(new Node3());
                log(LogLevel.INFO, "将默认异常委托到Node3节点");
                return new Node4();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node2 开始");
                return new Node1();
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node3 取全局错误到容器");
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
                        	gatherStat("ServiceMapping_Node3", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node3", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node3", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceMapping_Node4 查询服务映射信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetServiceMapInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("e_servicecode", "=", __REQ__.getItem("e_servicecode")+__REQ__.getStringItem("e_servicescene",""), "and"), new JavaList("channelcode", "in", "*,"+__REQ__.getItem("channelcode"))), "orderlist", new JavaList(new JavaList("channelcode"), new JavaList("DESC")), "pagelist", new JavaList(4, 1, 1));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "ServiceMapping");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceMapping_Node4", "查询服务映射信息", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("ServiceMapping_Node4", "查询服务映射信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node5());
                        }
                        __REQ__.setItem("_ServiceMapInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node4", "查询服务映射信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    case 2:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node4", "查询服务映射信息", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceMapping_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node6 内部服务码同外部");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_servicecode", __REQ__.getItem("e_servicecode")+__REQ__.getStringItem("e_servicescene","")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceMapping_Node6", "内部服务码同外部", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node6", "内部服务码同外部", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node6", "内部服务码同外部", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node7 内部服务码赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_servicecode", ((JavaDict)((List)__REQ__.getItem("_ServiceMapInfo_")).get(0)).getItem("p_servicecode")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceMapping_Node7", "内部服务码赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node7", "内部服务码赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node7", "内部服务码赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node8 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node9 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_ServiceMapInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceMapping_Node9", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node9", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node9", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
    
    }
    public static class CheckBranch extends BCScript {
        private INode startNode;
        public CheckBranch(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node1();
                log(LogLevel.INFO, "开始运行业务组件 机构信息校验");
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
                log(LogLevel.INFO, "CheckBranch_Node1 开始");
                return new Node2();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node2 默认逻辑错误委托");
                setExceptionHandler(new Node9());
                log(LogLevel.INFO, "将默认异常委托到Node9节点");
                return new Node5();
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node3 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node4 机构信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("BranchName", ((JavaDict)((List)__REQ__.getItem("_BranchInfo_")).get(0)).getItem("branchname")), new JavaList("BranchStatus", ((JavaDict)((List)__REQ__.getItem("_BranchInfo_")).get(0)).getItem("branchstatus")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node4", "机构信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node4", "机构信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node4", "机构信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node5 是否未取到机构代码");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getStringItem("branch","").equals("");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node5", "是否未取到机构代码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node5", "是否未取到机构代码", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node11();
                    case 1:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node5", "是否未取到机构代码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node6 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node7 机构不存在");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "HOST"), new JavaList("p_ret_desc", "渠道对应的机构不存在"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node7", "机构不存在", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node7", "机构不存在", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node7", "机构不存在", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node8 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node9 取全局错误到容器");
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
                        	gatherStat("CheckBranch_Node9", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node9", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node9", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node10 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "OrgNotExist";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node10", "获取响应码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
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
                	        	gatherStat("CheckBranch_Node10", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node10", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node10", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node11 查询机构信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetBranchInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("chanelcode", "in", "*,"+__REQ__.getItem("channelcode"), "and"), new JavaList("branch", "=", __REQ__.getItem("branch"))), "orderlist", new JavaList(new JavaList("branch", "chanelcode"), new JavaList("DESC", "DESC")), "pagelist", new JavaList(4, 1, 1));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "CheckBranch");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node11", "查询机构信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
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
                	        	gatherStat("CheckBranch_Node11", "查询机构信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __REQ__.setItem("_BranchInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node11", "查询机构信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    case 2:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node11", "查询机构信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node12 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node12 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "OrgCodeNotFound";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node12", "获取响应码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
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
                	        	gatherStat("CheckBranch_Node12", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node12", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node12", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node13 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_BranchInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node13", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node13", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node13", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node14 机构未上送");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "HOST"), new JavaList("p_ret_desc", "机构代码未上送或未配置渠道的虚拟机构"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node14", "机构未上送", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node14", "机构未上送", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node14", "机构未上送", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node15 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node15 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node16 是否渠道机构是启用");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("BranchStatus").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node16", "是否渠道机构是启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node16", "是否渠道机构是启用", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node17();
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node16", "是否渠道机构是启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node17 渠道对应机构未开通");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "HOST"), new JavaList("p_ret_desc", "渠道对应的机构未开通"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node17", "渠道对应机构未开通", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node17", "渠道对应机构未开通", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node17", "渠道对应机构未开通", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node18 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node18 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "OrgNotOpenChl";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckBranch_Node18", "获取响应码", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
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
                	        	gatherStat("CheckBranch_Node18", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckBranch_Node18", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node19();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckBranch_Node18", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node19 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckBranch_Node19 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
    
    }
    public static class BranchChlCheck extends BCScript {
        private INode startNode;
        public BranchChlCheck(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node4();
                log(LogLevel.INFO, "开始运行业务组件 机构开通渠道校验");
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
                log(LogLevel.INFO, "BranchChlCheck_Node1 机构开通渠道未配置");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "机构开通渠道未配置"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node1", "机构开通渠道未配置", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node1", "机构开通渠道未配置", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node1", "机构开通渠道未配置", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "BranchChlCheck_Node2 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node3 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_BranchChlInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node3", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node3", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node3", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node4 开始");
                return new Node6();
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node6 默认逻辑错误委托");
                setExceptionHandler(new Node13());
                log(LogLevel.INFO, "将默认异常委托到Node13节点");
                return new Node17();
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node7 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node8 机构渠道失效");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "机构开通渠道未生效或已失效"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node8", "机构渠道失效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node8", "机构渠道失效", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node8", "机构渠道失效", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node9 机构开通渠道启用");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__REQ__.getItem("_BranchChlInfo_")).get(0)).getItem("branchchlstatus").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node9", "机构开通渠道启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node9", "机构开通渠道启用", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node10();
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node9", "机构开通渠道启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node10 机构未开通该渠道");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "机构未开通该渠道"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node10", "机构未开通该渠道", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node10", "机构未开通该渠道", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node10", "机构未开通该渠道", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node11 机构渠道是否生效");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_BranchChlInfo_")).get(0)).getItem("branchchleffectdate").toString())>=0&&__REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_BranchChlInfo_")).get(0)).getItem("branchchlinvaliddate").toString())<=0;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node11", "机构渠道是否生效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node11", "机构渠道是否生效", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node8();
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node11", "机构渠道是否生效", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node12 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node12 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node13 取全局错误到容器");
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
                        	gatherStat("BranchChlCheck_Node13", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node13", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node13", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node14 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "OrgOpenChlNotExist";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node14", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("BranchChlCheck_Node14", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node14", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node14", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node15 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "OrgNotOpenChl";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node15", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("BranchChlCheck_Node15", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node15", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node15", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node16 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "OrgOpenChlNotEffective";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node16", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("BranchChlCheck_Node16", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node16", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node16", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "BranchChlCheck_Node17 查询机构开通渠道信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetBranchOpenChl";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("channelcode", "in", "*,"+__REQ__.getItem("channelcode"), "and"), new JavaList("branch", "in", "*,"+__REQ__.getItem("branch"))), "orderlist", new JavaList(new JavaList("branch", "channelcode"), new JavaList("DESC", "DESC")), "pagelist", new JavaList(4, 1, 1));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "BranchChlCheck");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("BranchChlCheck_Node17", "查询机构开通渠道信息", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("BranchChlCheck_Node17", "查询机构开通渠道信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("_BranchChlInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("BranchChlCheck_Node17", "查询机构开通渠道信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    case 2:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("BranchChlCheck_Node17", "查询机构开通渠道信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
    
    }
    public static class CheckService extends BCScript {
        private INode startNode;
        public CheckService(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node1();
                log(LogLevel.INFO, "开始运行业务组件 校验服务信息");
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
                log(LogLevel.INFO, "CheckService_Node1 开始");
                return new Node2();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node2 默认逻辑错误委托");
                setExceptionHandler(new Node14());
                log(LogLevel.INFO, "将默认异常委托到Node14节点");
                return new Node18();
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node4 服务是否启用");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__REQ__.getItem("_ServiceInfo_")).get(0)).getItem("servicestatus").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node4", "服务是否启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node4", "服务是否启用", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node6();
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node4", "服务是否启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node5 服务信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("ServiceInfo", ((List)__REQ__.getItem("_ServiceInfo_")).get(0)));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node5", "服务信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node5", "服务信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node5", "服务信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node6 服务未启用");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "服务未启用"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node6", "服务未启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node6", "服务未启用", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node16();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node6", "服务未启用", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node7 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node8 服务是否生效");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceInfo_")).get(0)).getItem("svreffectdate").toString())>=0&&__REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ServiceInfo_")).get(0)).getItem("svrinvaliddate").toString())<=0;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node8", "服务是否生效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node8", "服务是否生效", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node9();
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node8", "服务是否生效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node9 服务失效");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "服务未生效或已失效"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node9", "服务失效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node9", "服务失效", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node9", "服务失效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node10 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node12 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node12 服务不存在");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "服务未注册"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node12", "服务不存在", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node12", "服务不存在", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node12", "服务不存在", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node13 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node13 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node14 取全局错误到容器");
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
                        	gatherStat("CheckService_Node14", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node14", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node14", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node15 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "SerNotRegist";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node15", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckService_Node15", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node15", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node15", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckService_Node16 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "ChlNotOpen";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node16", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckService_Node16", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node16", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node16", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node17 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "serNotEffective";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node17", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckService_Node17", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node17", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node17", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node18 查询服务信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetServiceInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "CheckService");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node18", "查询服务信息", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 5) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("CheckService_Node18", "查询服务信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __REQ__.setItem("_ServiceInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node18", "查询服务信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    case 2:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node18", "查询服务信息", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckService_Node20 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_ServiceInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckService_Node20", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckService_Node20", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckService_Node20", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }
    public static class CheckChannel extends BCScript {
        private INode startNode;
        public CheckChannel(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node8();
                log(LogLevel.INFO, "开始运行业务组件 渠道检查");
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
                log(LogLevel.INFO, "CheckChannel_Node1 渠道失效");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "渠道未生效或已失效"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node1", "渠道失效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node1", "渠道失效", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node19();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node1", "渠道失效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckChannel_Node2 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node3 容器变量删除");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList("_ChannelInfo_");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.delete(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node3", "容器变量删除", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node3", "容器变量删除", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node3", "容器变量删除", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node4 默认逻辑错误委托");
                setExceptionHandler(new Node16());
                log(LogLevel.INFO, "将默认异常委托到Node16节点");
                return new Node20();
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node6 渠道信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("ChannelInfo", ((List)__REQ__.getItem("_ChannelInfo_")).get(0)));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node6", "渠道信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node6", "渠道信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node6", "渠道信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node7 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node8 开始");
                return new Node4();
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node9 渠道是否启用");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__REQ__.getItem("_ChannelInfo_")).get(0)).getItem("channelstatus").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node9", "渠道是否启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node9", "渠道是否启用", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node11();
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node9", "渠道是否启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node10 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node10 渠道是否失效");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ChannelInfo_")).get(0)).getItem("chleffectdate").toString())>=0&&__REQ__.getItem("p_workdate").toString().compareTo(((JavaDict)((List)__REQ__.getItem("_ChannelInfo_")).get(0)).getItem("chlinvaliddate").toString())<=0;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node10", "渠道是否失效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node10", "渠道是否失效", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node1();
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node10", "渠道是否失效", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node11 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node11 渠道未启用");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "渠道未启用"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node11", "渠道未启用", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node11", "渠道未启用", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node11", "渠道未启用", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node12 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node12 是否使用虚拟机构柜员");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)__REQ__.getItem("ChannelInfo")).getItem("virtualflag").equals("1");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node12", "是否使用虚拟机构柜员", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node12", "是否使用虚拟机构柜员", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node3();
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node12", "是否使用虚拟机构柜员", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node13 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node13 虚拟机构柜员赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("branch", ((JavaDict)__REQ__.getItem("ChannelInfo")).getItem("virtualbranch")), new JavaList("teller", ((JavaDict)__REQ__.getItem("ChannelInfo")).getItem("virtualteller")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node13", "虚拟机构柜员赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node13", "虚拟机构柜员赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node13", "虚拟机构柜员赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node14 渠道不存在");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    JavaList _arg1_ = new JavaList(new JavaList("p_excptype", "CIP"), new JavaList("p_ret_desc", "渠道未注册"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node14", "渠道不存在", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node14", "渠道不存在", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node14", "渠道不存在", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node15 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node15 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node16 取全局错误到容器");
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
                        	gatherStat("CheckChannel_Node16", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node16", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node16", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node17 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node17 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "ChlNotRegist";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node17", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckChannel_Node17", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node17", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node17", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node18 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node18 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "ChlNotOpen";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node18", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckChannel_Node18", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node18", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node18", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node19 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node19 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "ChlNotEffect";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node19", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckChannel_Node19", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node19", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node19", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node20 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckChannel_Node20 查询渠道信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __REQ__;
                    log(LogLevel.valueOf(4), "入参0=__REQ__");
                    String _arg1_ = "GetChannelInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CIPServiceManage", "transcode", "CheckChannel");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckChannel_Node20", "查询渠道信息", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckChannel_Node20", "查询渠道信息", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("_ChannelInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckChannel_Node20", "查询渠道信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    case 2:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckChannel_Node20", "查询渠道信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
    
    }

      
}