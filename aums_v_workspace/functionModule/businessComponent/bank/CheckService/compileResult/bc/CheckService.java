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
import tc.bank.product.B_ObjectOperate;
import tc.platform.P_Dict;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_String;

/**
 * 业务组件包名称：CheckService <br/>
 *
 * 业务组件包描述：CheckService <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class CheckService {

    public static class tokenCheck extends BCScript {
        private INode startNode;
        public tokenCheck(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node3();
                log(LogLevel.INFO, "开始运行业务组件 token校验");
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
                log(LogLevel.INFO, "tokenCheck_Node1 SHA256加密");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("oriJwtHead").toString()+__BUILTIN__.getItem("oriJwtPlayload").toString()+"aums";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ObjectOperate.getSHA256StrJava(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenCheck_Node1", "SHA256加密", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenCheck_Node1", "SHA256加密", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __BUILTIN__.setItem("aumsJWT_signature", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node1", "SHA256加密", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node1", "SHA256加密", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenCheck_Node2 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenCheck_Node3 开始");
                return new Node7();
            }    
        }
        
        private class Node4 implements INode {
            private BCScript bcScript;
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenCheck_Node4 生成token");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        __BUILTIN__TEMP__.setItem("playloadDict", new JavaDict());
                        logVar(LogLevel.valueOf(4), "入参0", __BUILTIN__TEMP__.getItem("playloadDict"));
                        bcScript = new bc.PublicService.tokenGenerate(__REQ__, __RSP__, __BUILTIN__TEMP__);
                    } else {
                        __BUILTIN__TEMP__ = bcScript.getBuiltinDict();
                    }
                    
                    INode node = bcScript.execute();
                    
                    
                    if (node == EndNode.SUSPEND_END) {
                        startNode = this;
                        return node;
                    }
                    
                    if (__BUILTIN__TEMP__.hasKey("aumsToken")) {
                        __REQ__.setItem("aumsToken", __BUILTIN__TEMP__.getItem("aumsToken"));
                        logVar(LogLevel.valueOf(4), "出参0", __BUILTIN__TEMP__.getItem("aumsToken"));
                    }
                    
                    log(LogLevel.INFO, "逻辑返回值=" + node);
                    
                    if (node instanceof EndNode) {
                    	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    		gatherStat("tokenCheck_Node4", "生成token", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 1:
                            return new Node5();
                        }
                    }
                    return getExceptionHandler(new Node2());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node4", "生成token", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenCheck_Node5 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenCheck_Node6 取全局错误到容器");
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
                        	gatherStat("tokenCheck_Node6", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node6", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node6", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenCheck_Node7 默认逻辑错误委托");
                setExceptionHandler(new Node6());
                log(LogLevel.INFO, "将默认异常委托到Node6节点");
                return new Node13();
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenCheck_Node8 token是否有效");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __BUILTIN__.getItem("aumsJWT_signature").equals(__BUILTIN__.getItem("oriJwtSignature"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenCheck_Node8", "token是否有效", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node8", "token是否有效", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node11();
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node8", "token是否有效", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenCheck_Node9 jwt头赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("oriJwtHead", ((List)__BUILTIN__.getItem("oriaumsTokenList")).get(0)), new JavaList("oriJwtPlayload", ((List)__BUILTIN__.getItem("oriaumsTokenList")).get(1)), new JavaList("oriJwtSignature", ((List)__BUILTIN__.getItem("oriaumsTokenList")).get(2)));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenCheck_Node9", "jwt头赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node9", "jwt头赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node9", "jwt头赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenCheck_Node10 字符串分割");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __REQ__.getItem("oriaumsToken");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "\\.";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_String.split(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenCheck_Node10", "字符串分割", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenCheck_Node10", "字符串分割", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __BUILTIN__.setItem("oriaumsTokenList", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node10", "字符串分割", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node10", "字符串分割", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenCheck_Node11 获取响应码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "TokenCheckFailed";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ConstantOperate.B_GetRespCode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenCheck_Node11", "获取响应码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenCheck_Node11", "获取响应码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __REQ__.setItem("p_ret_code", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node11", "获取响应码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenCheck_Node11", "获取响应码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenCheck_Node13 是否存在token");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __REQ__.getItem("oriaumsToken")==null||"".equals(__REQ__.getItem("oriaumsToken"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenCheck_Node13", "是否存在token", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenCheck_Node13", "是否存在token", status, startTime);
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
                		gatherStat("tokenCheck_Node13", "是否存在token", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
    
    }
    public static class PermissionCheck extends BCScript {
        private INode startNode;
        public PermissionCheck(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node2();
                log(LogLevel.INFO, "开始运行业务组件 权限校验");
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
                log(LogLevel.INFO, "PermissionCheck_Node1 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PermissionCheck_Node2 开始");
                return new Node3();
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PermissionCheck_Node3 默认逻辑错误委托");
                setExceptionHandler(new Node6());
                log(LogLevel.INFO, "将默认异常委托到Node6节点");
                return new Node8();
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PermissionCheck_Node4 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PermissionCheck_Node5 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "PermissionCheck_Node6 取全局错误到容器");
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
                        	gatherStat("PermissionCheck_Node6", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PermissionCheck_Node6", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PermissionCheck_Node6", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "PermissionCheck_Node8 数据查询类统一接口");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("aumschannel", __BUILTIN__.getItem("aumschannel"), "afamccode", __BUILTIN__.getItem("afamccode"), "afafccode", __BUILTIN__.getItem("afafccode"), "userid", __BUILTIN__.getItem("userid"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "QueryUserFunction";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = __REQ__.getItem("_DB_Map_");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("PermissionCheck_Node8", "数据查询类统一接口", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("PermissionCheck_Node8", "数据查询类统一接口", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __BUILTIN__.setItem("uf", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __BUILTIN__.setItem("ufnum", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("PermissionCheck_Node8", "数据查询类统一接口", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node1();
                    case 1:
                        return new Node5();
                    case 2:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("PermissionCheck_Node8", "数据查询类统一接口", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
    
    }
    public static class CheckControl extends BCScript {
        private INode startNode;
        public CheckControl(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node4();
                log(LogLevel.INFO, "开始运行业务组件 校验控制模块");
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
                log(LogLevel.INFO, "CheckControl_Node1 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node2 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckControl_Node2 数据查询类统一接口");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("rulename", __BUILTIN__.getItem("p_checkRuleCode"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "QueryCheckFunction";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "CheckService", "transcode", "CheckControl");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckControl_Node2", "数据查询类统一接口", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("CheckControl_Node2", "数据查询类统一接口", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __BUILTIN__.setItem("checkFunction", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __BUILTIN__.setItem("checkFunctionNum", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckControl_Node2", "数据查询类统一接口", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    case 2:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckControl_Node2", "数据查询类统一接口", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckControl_Node3 默认逻辑错误委托");
                setExceptionHandler(new Node6());
                log(LogLevel.INFO, "将默认异常委托到Node6节点");
                return new Node2();
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckControl_Node4 开始");
                return new Node3();
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckControl_Node5 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "CheckControl_Node6 取全局错误到容器");
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
                        	gatherStat("CheckControl_Node6", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckControl_Node6", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckControl_Node6", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckControl_Node7 循环+1");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", toInt(__BUILTIN__.getItem("i"))+1));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckControl_Node7", "循环+1", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckControl_Node7", "循环+1", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckControl_Node7", "循环+1", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckControl_Node8 循环赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("i", 0));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckControl_Node8", "循环赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckControl_Node8", "循环赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckControl_Node8", "循环赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckControl_Node9 是否继续循环");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(__BUILTIN__.getItem("i"))<toInt(__BUILTIN__.getItem("checkFunctionNum"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("CheckControl_Node9", "是否继续循环", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("CheckControl_Node9", "是否继续循环", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node5();
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckControl_Node9", "是否继续循环", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "CheckControl_Node10 调用检查组件");
                try {
                    JavaDict __BUILTIN__TEMP__;
                    if (bcScript == null) {
                    	startTime = System.currentTimeMillis();
                        __BUILTIN__TEMP__ = new JavaDict();
                        __BUILTIN__TEMP__.setItem("pkgName", ((JavaDict)((List)__BUILTIN__.getItem("checkFunction")).get(toInt(__BUILTIN__.getItem("i")))).getItem("modulecode"));
                        logVar(LogLevel.valueOf(4), "入参0", __BUILTIN__TEMP__.getItem("pkgName"));
                        __BUILTIN__TEMP__.setItem("bcName", ((JavaDict)((List)__BUILTIN__.getItem("checkFunction")).get(toInt(__BUILTIN__.getItem("i")))).getItem("tradecode"));
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
                    		gatherStat("CheckControl_Node10", "调用检查组件", ((EndNode) node).getType(), startTime);
                    	}
                        switch (((EndNode) node).getType()) {
                        case 0:
                            return new Node1();
                        case 1:
                            return new Node7();
                        }
                    }
                    return getExceptionHandler(new Node1());
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("CheckControl_Node10", "调用检查组件", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);    
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
    
    }

      
}