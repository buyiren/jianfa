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
import tc.bank.product.B_DBUnityRptOper;
import tc.bank.product.B_ObjectOperate;
import tc.bank.product.B_SysParam;
import tc.platform.P_CodecTools;
import tc.platform.P_Dict;
import tc.platform.P_Jdbc;
import tc.platform.P_Json;
import tc.platform.P_JudgmentStatement;
import tc.platform.P_Object;
import tc.platform.P_String;
import tc.platform.P_Time;

/**
 * 业务组件包名称：PublicService <br/>
 *
 * 业务组件包描述：PublicService <br/>
 *
 * @author AFA Compiler <br/>
 * @version 1.0 <br/>
 *
 */
public class PublicService {

    public static class GetChildBranchIdList extends BCScript {
        private INode startNode;
        public GetChildBranchIdList(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node8();
                log(LogLevel.INFO, "开始运行业务组件 V端获取子机构ID列表");
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
                log(LogLevel.INFO, "GetChildBranchIdList_Node1 取全局错误到容器");
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
                        	gatherStat("GetChildBranchIdList_Node1", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node1", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node1", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetChildBranchIdList_Node2 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchIdList_Node3 是否继续循环");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(__BUILTIN__.getItem("i"))<toInt(__BUILTIN__.getItem("brnoinfonum"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchIdList_Node3", "是否继续循环", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node3", "是否继续循环", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node6();
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node3", "是否继续循环", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetChildBranchIdList_Node4 默认逻辑错误委托");
                setExceptionHandler(new Node1());
                log(LogLevel.INFO, "将默认异常委托到Node1节点");
                return new Node10();
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchIdList_Node5 循环赋值");
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
                        	gatherStat("GetChildBranchIdList_Node5", "循环赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node5", "循环赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node5", "循环赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetChildBranchIdList_Node6 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchIdList_Node7 循环+1");
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
                        	gatherStat("GetChildBranchIdList_Node7", "循环+1", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node7", "循环+1", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node7", "循环+1", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchIdList_Node8 开始");
                return new Node4();
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchIdList_Node9 数据查询");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "select branchid,(prior branchid) as fatherbranchid,branchname,(prior branchname) as fatherbranchname,level as branchlevel,decode(CONNECT_BY_ISLEAF,1,1)  as leafflag,branchadress,branchphone from AUMS_BRANCHINFO start with branchid = '"+__BUILTIN__.getItem("branchId")+"'  connect by (prior branchid) = fatherbranchid";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = 0;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_Jdbc.dmlSelect(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchIdList_Node9", "数据查询", startTime, "技术组件返回值不能为空");
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
                        if (outputParams.size() != 2) {
                            log(LogLevel.ERROR, "出参的实参个数与形参个数不一致");
                            if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                	        	gatherStat("GetChildBranchIdList_Node9", "数据查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __BUILTIN__.setItem("brnoinfonum", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __BUILTIN__.setItem("brnoinfolist", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node9", "数据查询", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    case 2:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node9", "数据查询", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetChildBranchIdList_Node10 创建信息的数据对象");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("childBranchIdList", new JavaList()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchIdList_Node10", "创建信息的数据对象", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node10", "创建信息的数据对象", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node9();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node10", "创建信息的数据对象", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetChildBranchIdList_Node11 List追加信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList("childBranchIdList");
                    logVar(LogLevel.valueOf(0), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(-1);
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaList _arg3_ = new JavaList(((List)((List)__BUILTIN__.getItem("brnoinfolist")).get(toInt(__BUILTIN__.getItem("i")))).get(0));
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Object.appendList(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchIdList_Node11", "List追加信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchIdList_Node11", "List追加信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchIdList_Node11", "List追加信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
    
    }
    public static class GetChildBranchNoList extends BCScript {
        private INode startNode;
        public GetChildBranchNoList(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node2();
                log(LogLevel.INFO, "开始运行业务组件 V端获取子机构号列表");
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
                log(LogLevel.INFO, "GetChildBranchNoList_Node1 数据查询");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "select branchno from AUMS_BRANCHINFO start with branchid = '"+__BUILTIN__.getItem("branchId")+"'  connect by (prior branchid) = fatherbranchid";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    int _arg2_ = 0;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_Jdbc.dmlSelect(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchNoList_Node1", "数据查询", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
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
                	        	gatherStat("GetChildBranchNoList_Node1", "数据查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node10());
                        }
                        __BUILTIN__.setItem("brnoinfonum", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                        __BUILTIN__.setItem("brnoinfolist", outputParams.get(1));
                        logVar(LogLevel.valueOf(4), "出参1", outputParams.get(1));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node1", "数据查询", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    case 2:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node1", "数据查询", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node2 开始");
                return new Node11();
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node3 循环+1");
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
                        	gatherStat("GetChildBranchNoList_Node3", "循环+1", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node3", "循环+1", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node3", "循环+1", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node4 是否继续循环");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = toInt(__BUILTIN__.getItem("i"))<toInt(__BUILTIN__.getItem("brnoinfonum"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchNoList_Node4", "是否继续循环", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node4", "是否继续循环", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node9();
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node4", "是否继续循环", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node5 取全局错误到容器");
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
                        	gatherStat("GetChildBranchNoList_Node5", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node5", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node5", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node6 循环赋值");
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
                        	gatherStat("GetChildBranchNoList_Node6", "循环赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node6", "循环赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node6", "循环赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node7 创建信息的数据对象");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("childBranchNoList", new JavaList()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchNoList_Node7", "创建信息的数据对象", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node7", "创建信息的数据对象", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node7", "创建信息的数据对象", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node8 List追加信息");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList("childBranchNoList");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaList _arg2_ = new JavaList(-1);
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaList _arg3_ = new JavaList(((List)((List)__BUILTIN__.getItem("brnoinfolist")).get(toInt(__BUILTIN__.getItem("i")))).get(0));
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Object.appendList(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetChildBranchNoList_Node8", "List追加信息", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node10());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetChildBranchNoList_Node8", "List追加信息", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node10());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetChildBranchNoList_Node8", "List追加信息", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node10());
                }
            }    
        }
        
        private class Node9 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node9 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node10 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node10 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node11 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetChildBranchNoList_Node11 默认逻辑错误委托");
                setExceptionHandler(new Node5());
                log(LogLevel.INFO, "将默认异常委托到Node5节点");
                return new Node7();
            }    
        }
        
    
    }
    public static class SequenceGenerate extends BCScript {
        private INode startNode;
        public SequenceGenerate(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node8();
                log(LogLevel.INFO, "开始运行业务组件 序号生成");
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
                log(LogLevel.INFO, "SequenceGenerate_Node1 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node2 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node3 取全局错误到容器");
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
                        	gatherStat("SequenceGenerate_Node3", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node3", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node3", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node4 原序号查询");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("seqCondition", __BUILTIN__.getItem("sequenceCondition"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetAumsSequence";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "PublicService", "transcode", "SequenceGenerate");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node4", "原序号查询", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("SequenceGenerate_Node4", "原序号查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __BUILTIN__.setItem("aumsSequenceList", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node4", "原序号查询", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node1();
                    case 1:
                        return new Node17();
                    case 2:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node4", "原序号查询", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node5 序号赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("aumsSequence", __BUILTIN__.getItem("newSequence")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node5", "序号赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node5", "序号赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node5", "序号赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node6 默认逻辑错误委托");
                setExceptionHandler(new Node3());
                log(LogLevel.INFO, "将默认异常委托到Node3节点");
                return new Node9();
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node7 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node8 开始");
                return new Node6();
            }    
        }
        
        private class Node9 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node9 初始化系统参数");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList("3");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "PublicServer";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    String _arg3_ = "SequenceGenerate";
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_SysParam.B_InitSysParam(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node9", "初始化系统参数", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node9", "初始化系统参数", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node4();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node9", "初始化系统参数", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node10 序号补足");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("newSequence").toString();
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    int _arg1_ = toInt(__BUILTIN__.getItem(__BUILTIN__.getItem("sequenceKey")+"_length"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "left";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    char _arg3_ = '0';
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_String.fillStr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node10", "序号补足", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("SequenceGenerate_Node10", "序号补足", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                        __BUILTIN__.setItem("newSequence", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node10", "序号补足", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node10", "序号补足", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node11 目标序号长度判断");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = __BUILTIN__.getItem("newSequence").toString().length()<=toInt(__BUILTIN__.getItem(__BUILTIN__.getItem("sequenceKey")+"_length"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node11", "目标序号长度判断", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node11", "目标序号长度判断", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node13();
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node11", "目标序号长度判断", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node12 创建信息的数据对象");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("_Query_Map_", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node12", "创建信息的数据对象", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node12", "创建信息的数据对象", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node15();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node12", "创建信息的数据对象", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node13 生成新序号");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("newSequence", "1"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node13", "生成新序号", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node13", "生成新序号", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node10();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node13", "生成新序号", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node14 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node14 序号更新");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__.getItem("_Query_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "updateAumsSequence";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "PublicService", "transcode", "SequenceGenerate");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node14", "序号更新", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("SequenceGenerate_Node14", "序号更新", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node14", "序号更新", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node14", "序号更新", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node15 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node15 序号赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__.getItem("_Query_Map_");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("seqCondition", __BUILTIN__.getItem("sequenceCondition")), new JavaList(__BUILTIN__.getItem("sequenceKey"), __BUILTIN__.getItem("newSequence")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node15", "序号赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node15", "序号赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node14();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node15", "序号赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node16 生成新序号");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("newSequence", toInt(((JavaDict)((List)__BUILTIN__.getItem("aumsSequenceList")).get(0)).getItem(__BUILTIN__.getItem("sequenceKey")))+1));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node16", "生成新序号", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node16", "生成新序号", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node16", "生成新序号", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "SequenceGenerate_Node17 原序号为空");
                startTime = System.currentTimeMillis();
                try {
                    Object _arg0_ = "".equals(((JavaDict)((List)__BUILTIN__.getItem("aumsSequenceList")).get(0)).getItem(__BUILTIN__.getItem("sequenceKey")))||((JavaDict)((List)__BUILTIN__.getItem("aumsSequenceList")).get(0)).getItem(__BUILTIN__.getItem("sequenceKey"))==null;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_JudgmentStatement.boolFrame(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node17", "原序号为空", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node1());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node17", "原序号为空", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node16();
                    case 1:
                        return new Node13();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node17", "原序号为空", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
        private class Node18 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "SequenceGenerate_Node18 新增序号key");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("seqCondition", __BUILTIN__.getItem("sequenceCondition"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "InsertAumsSequence";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    boolean _arg3_ = true;
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    JavaDict _arg4_ = new JavaDict("modulecode", "PublicService", "transcode", "SequenceGenerate");
                    logVar(LogLevel.valueOf(4), "入参4", _arg4_);
                    TCResult result = B_DBUnityAltOper.B_DBUnityAltOpr(_arg0_, _arg1_, _arg2_, _arg3_, _arg4_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("SequenceGenerate_Node18", "新增序号key", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("SequenceGenerate_Node18", "新增序号key", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node1());
                        }
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("SequenceGenerate_Node18", "新增序号key", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node1();
                    case 1:
                        return new Node17();
                    case 2:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node1());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("SequenceGenerate_Node18", "新增序号key", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node1());
                }
            }    
        }
        
    
    }
    public static class ServiceQuery extends BCScript {
        private INode startNode;
        public ServiceQuery(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node4();
                log(LogLevel.INFO, "开始运行业务组件 服务信息查询");
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
                log(LogLevel.INFO, "ServiceQuery_Node1 服务信息赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("p_modulecode", ((JavaDict)((List)__BUILTIN__.getItem("_ServiceQueryInfo_")).get(0)).getItem("p_modulecode")), new JavaList("p_transcode", ((JavaDict)((List)__BUILTIN__.getItem("_ServiceQueryInfo_")).get(0)).getItem("p_transcode")), new JavaList("p_checkRuleCode", ((JavaDict)((List)__BUILTIN__.getItem("_ServiceQueryInfo_")).get(0)).getItem("p_checkRuleCode")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceQuery_Node1", "服务信息赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceQuery_Node1", "服务信息赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceQuery_Node1", "服务信息赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceQuery_Node2 服务信息查询");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("p_servicecode", __BUILTIN__.getItem("p_servicecode"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetServiceQueryInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "PUB", "transcode", "ServiceQuery");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceQuery_Node2", "服务信息查询", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceQuery_Node2", "服务信息查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node5());
                        }
                        __BUILTIN__.setItem("_ServiceQueryInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceQuery_Node2", "服务信息查询", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node7();
                    case 1:
                        return new Node1();
                    case 2:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceQuery_Node2", "服务信息查询", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node5());
                }
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceQuery_Node3 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceQuery_Node4 开始");
                return new Node8();
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceQuery_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceQuery_Node6 取全局错误到容器");
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
                        	gatherStat("ServiceQuery_Node6", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node5());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceQuery_Node6", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node5());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceQuery_Node6", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceQuery_Node7 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node8 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceQuery_Node8 默认逻辑错误委托");
                setExceptionHandler(new Node6());
                log(LogLevel.INFO, "将默认异常委托到Node6节点");
                return new Node2();
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
                startNode = new Node3();
                log(LogLevel.INFO, "开始运行业务组件 服务映射");
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
                log(LogLevel.INFO, "ServiceMapping_Node1 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node2 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node3 开始");
                return new Node6();
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node4 取全局错误到容器");
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
                        	gatherStat("ServiceMapping_Node4", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node4", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node4", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "ServiceMapping_Node5 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node6 默认逻辑错误委托");
                setExceptionHandler(new Node4());
                log(LogLevel.INFO, "将默认异常委托到Node4节点");
                return new Node7();
            }    
        }
        
        private class Node7 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node7 服务映射查询");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = new JavaDict("e_servicecode", __BUILTIN__.getItem("e_servicecode"));
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetServiceMapInfo";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("e_servicecode", "=", __BUILTIN__.getItem("e_servicecode"), "and"), new JavaList("channelcode", "in", "*,"+__BUILTIN__.getItem("channelcode"))), "orderlist", new JavaList(new JavaList("channelcode"), new JavaList("DESC")));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "PUB", "transcode", "ServiceMapping");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceMapping_Node7", "服务映射查询", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("ServiceMapping_Node7", "服务映射查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node2());
                        }
                        __BUILTIN__.setItem("_ServiceMapInfo_", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node7", "服务映射查询", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node2();
                    case 1:
                        return new Node8();
                    case 2:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node7", "服务映射查询", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "ServiceMapping_Node8 服务映射赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("p_servicecode", ((JavaDict)((List)__BUILTIN__.getItem("_ServiceMapInfo_")).get(0)).getItem("p_servicecode")), new JavaList("svrinputcode", ((JavaDict)((List)__BUILTIN__.getItem("_ServiceMapInfo_")).get(0)).getItem("svrinputcode")), new JavaList("svroutputcode", ((JavaDict)((List)__BUILTIN__.getItem("_ServiceMapInfo_")).get(0)).getItem("svroutputcode")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("ServiceMapping_Node8", "服务映射赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node2());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("ServiceMapping_Node8", "服务映射赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node2());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("ServiceMapping_Node8", "服务映射赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node2());
                }
            }    
        }
        
    
    }
    public static class tokenGenerate extends BCScript {
        private INode startNode;
        public tokenGenerate(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node4();
                log(LogLevel.INFO, "开始运行业务组件 生成token");
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
                log(LogLevel.INFO, "tokenGenerate_Node1 bas64编码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("aumsJWT_header_json");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_CodecTools.b64Encode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node1", "bas64编码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node1", "bas64编码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __BUILTIN__.setItem("aumsJWT_header_base64", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node1", "bas64编码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node8();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node1", "bas64编码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node2 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenGenerate_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenGenerate_Node4 开始");
                return new Node7();
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenGenerate_Node5 bas64编码");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("aumsJWT_playload_json");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_CodecTools.b64Encode(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node5", "bas64编码", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node5", "bas64编码", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __BUILTIN__.setItem("aumsJWT_playload_base64", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node5", "bas64编码", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node12();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node5", "bas64编码", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node6 javaDict转换json串");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__.getItem("aumsJWT_header");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Json.dictToStr(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node6", "javaDict转换json串", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node6", "javaDict转换json串", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __BUILTIN__.setItem("aumsJWT_header_json", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node6", "javaDict转换json串", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node1();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node6", "javaDict转换json串", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node7 默认逻辑错误委托");
                setExceptionHandler(new Node10());
                log(LogLevel.INFO, "将默认异常委托到Node10节点");
                return new Node19();
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "tokenGenerate_Node8 javaDict转换json串");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__.getItem("aumsJWT_playload");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Json.dictToStr(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node8", "javaDict转换json串", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node8", "javaDict转换json串", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __BUILTIN__.setItem("aumsJWT_playload_json", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node8", "javaDict转换json串", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node5();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node8", "javaDict转换json串", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node10 取全局错误到容器");
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
                        	gatherStat("tokenGenerate_Node10", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node10", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node10", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node11 token赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("aumsToken", __BUILTIN__.getItem("aumsJWT_header_base64").toString()+"."+__BUILTIN__.getItem("aumsJWT_playload_base64").toString()+"."+__BUILTIN__.getItem("aumsJWT_signature").toString()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node11", "token赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node11", "token赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node11", "token赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node12 SHA256加密");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = __BUILTIN__.getItem("aumsJWT_header_base64").toString()+__BUILTIN__.getItem("aumsJWT_playload_base64").toString()+"aums";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = B_ObjectOperate.getSHA256StrJava(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node12", "SHA256加密", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node12", "SHA256加密", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __BUILTIN__.setItem("aumsJWT_signature", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node12", "SHA256加密", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node11();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node12", "SHA256加密", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node18 jwt头赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__.getItem("aumsJWT_header");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("typ", "JWT"), new JavaList("alg", "SHA256"));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node18", "jwt头赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node18", "jwt头赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node20();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node18", "jwt头赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node19 创建信息的数据对象");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("aumsJWT_header", new JavaDict()), new JavaList("aumsJWT_playload", new JavaDict()));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Object.createObject(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node19", "创建信息的数据对象", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node19", "创建信息的数据对象", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node18();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node19", "创建信息的数据对象", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node20 容器间变量参数化拷贝");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__.getItem("playloadDict");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaDict _arg1_ = __BUILTIN__.getItem("aumsJWT_playload");
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    Object _arg2_ = null;
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaList _arg3_ = new JavaList();
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = P_Dict.copy(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node20", "容器间变量参数化拷贝", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node20", "容器间变量参数化拷贝", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node21();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node20", "容器间变量参数化拷贝", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node21 参数化取当前日期时间");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = "timestamp";
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    TCResult result = P_Time.getFormatTime(_arg0_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node21", "参数化取当前日期时间", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node21", "参数化取当前日期时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        ((JavaDict)__BUILTIN__.getItem("aumsJWT_playload")).setItem("iat", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node21", "参数化取当前日期时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node22();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node21", "参数化取当前日期时间", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "tokenGenerate_Node22 参数化累加时间");
                startTime = System.currentTimeMillis();
                try {
                    String _arg0_ = ((JavaDict)__BUILTIN__.getItem("aumsJWT_playload")).getItem("iat");
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    long _arg1_ = 2;
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    String _arg2_ = "hours";
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    TCResult result = P_Time.formatCalculateTime(_arg0_, _arg1_, _arg2_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("tokenGenerate_Node22", "参数化累加时间", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("tokenGenerate_Node22", "参数化累加时间", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        ((JavaDict)__BUILTIN__.getItem("aumsJWT_playload")).setItem("exp", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("tokenGenerate_Node22", "参数化累加时间", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("tokenGenerate_Node22", "参数化累加时间", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }
    public static class GetBranchId extends BCScript {
        private INode startNode;
        public GetBranchId(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node1();
                log(LogLevel.INFO, "开始运行业务组件 获取机构ID");
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
                log(LogLevel.INFO, "GetBranchId_Node1 开始");
                return new Node5();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node2 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node3 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node3 机构ID赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("branchId", ((JavaDict)((List)__BUILTIN__.getItem("branchIdQuery")).get(0)).getItem("branchId")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetBranchId_Node3", "机构ID赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetBranchId_Node3", "机构ID赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetBranchId_Node3", "机构ID赋值", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node4 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node4 取全局错误到容器");
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
                        	gatherStat("GetBranchId_Node4", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node6());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetBranchId_Node4", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetBranchId_Node4", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
        private class Node5 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node5 默认逻辑错误委托");
                setExceptionHandler(new Node4());
                log(LogLevel.INFO, "将默认异常委托到Node4节点");
                return new Node8();
            }    
        }
        
        private class Node6 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node6 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node7 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node7 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetBranchId_Node8 机构信息查询");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetBranchId";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("branchNo", "=", __BUILTIN__.getItem("branchNo"))));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "PUB", "transcode", "GetBranchId");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetBranchId_Node8", "机构信息查询", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("GetBranchId_Node8", "机构信息查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node6());
                        }
                        __BUILTIN__.setItem("branchIdQuery", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetBranchId_Node8", "机构信息查询", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node6();
                    case 1:
                        return new Node3();
                    case 2:
                        return new Node6();
                    default:
                        return getExceptionHandler(new Node6());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetBranchId_Node8", "机构信息查询", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node6());
                }
            }    
        }
        
    
    }
    public static class GetDevId extends BCScript {
        private INode startNode;
        public GetDevId(JavaDict __REQ__, JavaDict __RSP__, JavaDict __BUILTIN__) {
            super(__REQ__, __RSP__, __BUILTIN__);
        }
        
        @Override
        public INode execute() {
            if (startNode == null) {
                startNode = new Node1();
                log(LogLevel.INFO, "开始运行业务组件 获取设备ID");
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
                log(LogLevel.INFO, "GetDevId_Node1 开始");
                return new Node4();
            }    
        }
        
        private class Node2 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetDevId_Node2 正常结束");
                return EndNode.NORMAL_END;
            }    
        }
        
        private class Node3 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetDevId_Node3 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node4 implements INode {
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetDevId_Node4 默认逻辑错误委托");
                setExceptionHandler(new Node8());
                log(LogLevel.INFO, "将默认异常委托到Node8节点");
                return new Node6();
            }    
        }
        
        private class Node5 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetDevId_Node5 设备ID赋值");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    JavaList _arg1_ = new JavaList(new JavaList("devId", ((JavaDict)((List)__BUILTIN__.getItem("devIdQuery")).get(0)).getItem("devId")));
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    TCResult result = P_Dict.setValue(_arg0_, _arg1_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetDevId_Node5", "设备ID赋值", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetDevId_Node5", "设备ID赋值", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node2();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetDevId_Node5", "设备ID赋值", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetDevId_Node6 设备信息查询");
                startTime = System.currentTimeMillis();
                try {
                    JavaDict _arg0_ = __BUILTIN__;
                    logVar(LogLevel.valueOf(4), "入参0", _arg0_);
                    String _arg1_ = "GetDevId";
                    logVar(LogLevel.valueOf(4), "入参1", _arg1_);
                    JavaDict _arg2_ = new JavaDict("dyncondlist", new JavaList(new JavaList("branchNo", "=", __BUILTIN__.getItem("branchNo"), " and "), new JavaList("devNum", "=", __BUILTIN__.getItem("devNum"))));
                    logVar(LogLevel.valueOf(4), "入参2", _arg2_);
                    JavaDict _arg3_ = new JavaDict("modulecode", "PUB", "transcode", "GetDevId");
                    logVar(LogLevel.valueOf(4), "入参3", _arg3_);
                    TCResult result = B_DBUnityRptOper.B_DBUnityRptOpr(_arg0_, _arg1_, _arg2_, _arg3_);
                    if (result == null) {
                        log(LogLevel.ERROR, "技术组件返回值不能为空");
                        if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                        	gatherStat("GetDevId_Node6", "设备信息查询", startTime, "技术组件返回值不能为空");
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
                	        	gatherStat("GetDevId_Node6", "设备信息查询", startTime, "出参的实参个数与形参个数不一致");
                	        }
                            return getExceptionHandler(new Node3());
                        }
                        __BUILTIN__.setItem("devIdQuery", outputParams.get(0));
                        logVar(LogLevel.valueOf(4), "出参0", outputParams.get(0));
                    }
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetDevId_Node6", "设备信息查询", status, startTime);
                    }
                    switch (status) {
                    case 0:
                        return new Node3();
                    case 1:
                        return new Node5();
                    case 2:
                        return new Node3();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetDevId_Node6", "设备信息查询", startTime, ExceptionUtils.toDetailString(e));
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
                log(LogLevel.INFO, "GetDevId_Node7 异常结束");
                return EndNode.EXCEPTION_END;
            }    
        }
        
        private class Node8 implements INode {
            private long startTime;
            
            @Override
            public INode execute() {
                log(LogLevel.INFO, "GetDevId_Node8 取全局错误到容器");
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
                        	gatherStat("GetDevId_Node8", "取全局错误到容器", startTime, "技术组件返回值不能为空");
                        }
                        return getExceptionHandler(new Node3());
                    }
                    
                    int status = result.getStatus();
                    log(LogLevel.INFO, "逻辑返回值=" + status);
                    
                    if (result.getErrorCode() != null || result.getErrorMsg() != null) {
                        setGlobalError("D", result.getErrorCode(), result.getErrorMsg());
                    }
                    
                    if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                    	gatherStat("GetDevId_Node8", "取全局错误到容器", status, startTime);
                    }
                    switch (status) {
                    case 1:
                        return new Node7();
                    default:
                        return getExceptionHandler(new Node3());
                    }
                } catch (Throwable e) {
                	if (__REQ__.getItem("__TRADE_MONITOR__") != null) {
                		gatherStat("GetDevId_Node8", "取全局错误到容器", startTime, ExceptionUtils.toDetailString(e));
                	}
                    setGlobalError("E", "ACMP0E001", e.toString());
                    log(LogLevel.ERROR, e);
                    return getExceptionHandler(new Node3());
                }
            }    
        }
        
    
    }

      
}