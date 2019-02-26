package tc.bank.utils;

import java.util.Map;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.ErrorCodeModule;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 入参校验类
 * 
 * @author AlanMa
 *
 */
public class CheckUtil {

    /**
     * 入参校验
     * 
     * @param cond_data_context
     * @param busioper
     * @param ext_context
     * @param map_context
     * @throws BusException
     */
    public static void checkInpParamDB(JavaDict cond_data_context, String busioper, JavaDict ext_context, JavaDict map_context) throws BusException {
        if (cond_data_context != null && !(cond_data_context instanceof JavaDict)) {
            throw new BusException(ErrorCodeModule.IMD001, "cond_data_context");
        }
        if (busioper == null || !(busioper instanceof String)) {
            throw new BusException(ErrorCodeModule.IMD002, "busioper");

        }
        if (ext_context != null && !(ext_context instanceof JavaDict)) {
            throw new BusException(ErrorCodeModule.IMD001, "ext_context");
        }
        if (map_context != null && !(map_context instanceof JavaDict)) {
            throw new BusException(ErrorCodeModule.IMD001, "map_context");
        }
        if (map_context != null && map_context instanceof JavaDict) {
            String[] chkvar = { "modulecode", "transcode" };
            for (int index = 0; index < chkvar.length; index++) {
                if (StringUtil.isEmpty(map_context.getStringItem(chkvar[index])))
                    throw new BusException(ErrorCodeModule.IMD003, "modulecode,transcode");
            }
        }
    }

    /**
     * 扩展条件是否为空<br>
     * 0-空<br>
     * 1-非空<br>
     * 
     * @param inputMap
     * @return
     */
    public static int[] isExtCondNotEmpty(Map<String, Object> inputMap) {
        JavaDict extCond = (JavaDict) inputMap.get("ext_context");
        int[] notEmptyFlag = new int[3];
        if (extCond != null) {
            if (ListUtil.isNotEmpty((JavaList) extCond.get("dyncondlist"))) {
                AppLogger.debug(ListUtil.printList((JavaList) extCond.get("dyncondlist"), "[dyncondlist]:", CommonConstant.NOR_PRINT));
                notEmptyFlag[0] = 1;
            }
            if (ListUtil.isNotEmpty((JavaList) extCond.get("orderlist"))) {
                AppLogger.debug(ListUtil.printList((JavaList) extCond.get("orderlist"), "[orderlist]：", CommonConstant.NOR_PRINT));
                notEmptyFlag[1] = 1;
            }
            if (ListUtil.isNotEmpty((JavaList) extCond.get("pagelist"))) {
                AppLogger.debug(ListUtil.printList((JavaList) extCond.get("pagelist"), "[pagelist]：", CommonConstant.NOR_PRINT));
                notEmptyFlag[2] = 1;
            }
        }
        return notEmptyFlag;
    }
}
