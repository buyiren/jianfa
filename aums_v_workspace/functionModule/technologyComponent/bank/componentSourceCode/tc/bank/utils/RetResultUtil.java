package tc.bank.utils;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.constant.IErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 技术组件返回结果工具类
 * 
 * @author AlanMa
 * 
 */
public class RetResultUtil {

    /**
     * 获取警告返回结果
     * 
     * @param errorCode
     * @param errMsgKey
     * @return
     */
    public static TCResult getWarningTCResult(String errorCode, String... errMsgKey) {
        String[] errInfo = getErrorMsg(errorCode, errMsgKey);
        return new TCResult(2, errInfo[0], errInfo[1]);
    }
    
    /**
     * 获取警告返回结果(附带页码信息)
     * 
     * @param errorCode
     * @param errMsgKey
     * @return
     */
    public static TCResult getWarningTCResult(String errorCode, int recordNum,int totalrownum, int totalpagenum, int nowpagenum,String... errMsgKey) {
    	TCResult result=getErrorTCResult(errorCode, "");
    	result.setStatus(CommonConstant.NONE);
    	result.setOutputParams(null,recordNum,totalrownum,totalpagenum,nowpagenum);
        return result;
    }
    
    /**
     * 获取错误返回结果
     * 
     * @param errorCode
     * @param errMsgKey
     * @return
     */
    public static TCResult getErrorTCResult(String errorCode, String... errMsgKey) {
        String[] errInfo = getErrorMsg(errorCode, errMsgKey);
        return TCResult.newFailureResult(errInfo[0], errInfo[1]);
    }

    /**
     * 获取错误信息
     * 
     * @param errorCode
     * @param errMsgKey
     * @return
     */
    private static String[] getErrorMsg(String errorCode, String... errMsgKey) {
        String[] errInfo = errorCode.split("\\|");
        for (int index = 0; index < errMsgKey.length; index++) {
            if (errInfo[1].indexOf("%s") != -1) {
                errInfo[1] = errInfo[1].replaceFirst("%s", errMsgKey[index]);
            }
        }
        return errInfo;
    }

    /**
     * 获取内部结果
     * 
     * @param e
     * @return
     */
    public static TCResult getTCResult(BusException e) {
        return getErrorTCResult(e.getErrorCode(), e.getErrMsgKey());
    }


    /**
     * 获取外部返回结果
     * 
     * @param e
     * @return
     */
    public static TCResult getTCResToExternal(BusException e) {
        TCResult tcResult = getTCResult(e);
        if (ErrorCodeModule.IMD004.equals(e.getErrorCode())) {
            tcResult.setStatus(CommonConstant.NONE);
            tcResult.setErrorCode(IErrorCode.getCode("RecorDoesNotMeet"));
        }
        else {
            tcResult.setStatus(CommonConstant.FAILURE);
            tcResult.setErrorCode(IErrorCode.getCode("SystemException"));
        }

        return tcResult;
    }

    /**
     * 获取外部返回结果
     * 
     * @param tcResult
     * @return
     */
    public static TCResult getTCResToExternal(TCResult tcResult) {
    	//无记录
        if (StringUtil.isNotEmpty(tcResult.getErrorCode()) && CommonConstant.NONE == tcResult.getStatus()) {
            tcResult.setErrorCode(IErrorCode.getCode("RecorDoesNotMeet"));
        }
        //系统异常
        else if(StringUtil.isNotEmpty(tcResult.getErrorCode()) && CommonConstant.FAILURE == tcResult.getStatus()){
            tcResult.setErrorCode(IErrorCode.getCode("SystemException"));
        }
        return tcResult;
    }

    /**
     * 打印返回结果
     * 
     * @param tcResult
     */
    public static void printTCResult(TCResult tcResult) {
        AppLogger.info("[TCResult.Status]:" + tcResult.getStatus());
        if (CommonConstant.SUCCESS != tcResult.getStatus()) {
            AppLogger.info("[TCResult.ErrorCode]:" + tcResult.getErrorCode());
            AppLogger.info("[TCResult.ErrorMsg]:" + tcResult.getErrorMsg());
        }
        if (ListUtil.isNotEmpty(tcResult.getOutputParams())) {
            AppLogger.info("[TCResult.OutputParams]:\n" + ListUtil.printList(tcResult.getOutputParams(), null, 0));
        }

    }
}
