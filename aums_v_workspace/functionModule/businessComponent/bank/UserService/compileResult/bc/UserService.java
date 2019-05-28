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
import tc.bank.product.B_DBUnityRptOper;
import tc.platform.P_Debug;
import tc.platform.P_Dict;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_List;
import tc.platform.P_String;

/**
 * 业务组件包名称：UserService <br/>
 *
 * 业务组件包描述：UserService <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class UserService {

    public static class LoginService extends BCScript {
        private INode startNode;
        public LoginService(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node1();
                log(LogLevel.INFO, "开始运行业务组件 登录模块");
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
                log(LogLevel.INFO, "LoginService_Node1 开始");
                return new Node2();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node2 默认逻辑错误委托");
                setExceptionHandler(new Node4());
                log(LogLevel.INFO, "将默认异常委托到Node4节点");
                return new Node7();
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node3 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node4 取全局错误到容器");
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
                        	gatherStat("LoginService_Node4", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node4", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node4", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "LoginService_Node5 是否允许用户的登录方式");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("loginMethod");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = __BUILTIN__.getItem("loginMethodList");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_List.P_StrInListTrue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node5", "是否允许用户的登录方式", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("LoginService_Node5", "是否允许用户的登录方式", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node5", "是否允许用户的登录方式", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node16();
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node5", "是否允许用户的登录方式", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "LoginService_Node6 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node7 查询用户登录数据");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("userId", __BUILTIN__.getItem("userId"), "systemId", __BUILTIN__.getItem("systemId"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "QueryUserLoginInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "UserService", "transcode", "LoginService");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node7", "查询用户登录数据", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("LoginService_Node7", "查询用户登录数据", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __BUILTIN__.setItem("loginInfo", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node7", "查询用户登录数据", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node17();
                    case 2:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node7", "查询用户登录数据", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node8 登录方式，1凭密，2指纹");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("loginMethod");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "1";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "1";
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
                        	gatherStat("LoginService_Node8", "登录方式，1凭密，2指纹", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node8", "登录方式，1凭密，2指纹", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    case 2:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node8", "登录方式，1凭密，2指纹", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node9 判断密码是否相同");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __BUILTIN__.getItem("password").equals(((JavaDict)((List)__BUILTIN__.getItem("loginInfo")).get(0)).getItem("password"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node9", "判断密码是否相同", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node9", "判断密码是否相同", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node15();
                    case 1:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node9", "判断密码是否相同", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "LoginService_Node10 空组件");
                startTime = System.currentTimeMillis();
                try {
                    TCResult result = P_Debug.pass();
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node10", "空组件", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node10", "空组件", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node10", "空组件", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "LoginService_Node11 查询用户权限");
                return new Node3();
            }    
        }
        
        private class Node13 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node13 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node14 判断是否首次登录");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = ((JavaDict)((List)__BUILTIN__.getItem("loginInfo")).get(0)).getItem("firstLoginFlag").equals("0");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node14", "判断是否首次登录", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node14", "判断是否首次登录", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node11();
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node14", "判断是否首次登录", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node15 密码错误信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("loginErrorMessage", "密码错误"), new JavaList("loginErrorCode", "LOE001"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node15", "密码错误信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node15", "密码错误信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node15", "密码错误信息赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node16 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "LoginService_Node16 登录方式错误信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("loginErrorMessage", "不允许用户以此登录方式登录"), new JavaList("loginErrorCode", "LOE002"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node16", "登录方式错误信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node16", "登录方式错误信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node16", "登录方式错误信息赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "LoginService_Node17 分割登录方式");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)((List)__BUILTIN__.getItem("loginInfo")).get(0)).getItem("loginMethod");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "|";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_String.split(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("LoginService_Node17", "分割登录方式", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("LoginService_Node17", "分割登录方式", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __BUILTIN__.setItem("loginMethodList", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("LoginService_Node17", "分割登录方式", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("LoginService_Node17", "分割登录方式", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
    
    }

      
}