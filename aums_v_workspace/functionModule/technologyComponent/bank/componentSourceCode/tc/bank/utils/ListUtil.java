package tc.bank.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.agree.afa.svc.javaengine.AppLogger;
import tc.bank.constant.CommonConstant;

/**
 * List工具类
 * 
 * @author AlanMa
 * 
 */
public class ListUtil {

    /**
     * List是否为空
     * 
     * @param outputParams
     * @return
     */
    public static Boolean isNotEmpty(List<?> outputParams) {
        if (outputParams != null && outputParams.size() > 0) {
            if (outputParams.get(0) != null) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * 
     * @param list
     * @param logInfo
     * @param layer
     * @return
     */
    public static String printList(List<?> list, String logInfoHead, int printType) {
        if (!ListUtil.isNotEmpty(list)) {
            return "";
        }
        String logContent = null;
        switch (printType) {
        case CommonConstant.TREE_PRINT:
            logContent = printListTree(list, null, 0);
            break;
        case CommonConstant.NOR_PRINT:
            logContent = list.toString();
            break;

        default:
            AppLogger.error("[Wrong PrintType:" + printType + "]");
            break;
        }
        
        return (StringUtil.isEmpty(logInfoHead) ? logContent : logInfoHead + logContent);
    }

    /**
     * 打印List树状结构（开发调试专用）
     * 
     * @param list
     * @param logInfo
     * @param layer
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String printListTree(List<?> list, String logInfo, int layer) {
        if (!ListUtil.isNotEmpty(list)) {
            return "";
        }

        StringBuffer strBuf = new StringBuffer();
        strBuf.append(strBuf);
        Iterator<?> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            Object element = it.next();
            /**
             * 打印层级
             */
            strBuf.append(printLayer(layer, strBuf.toString()));
            strBuf.append("[" + i++ + "] = ");
            if (element == null) {
                strBuf.append("\n");
                continue;
            }
            if (element instanceof Map) {
                strBuf.append("[Map]");
                strBuf.append("\n");
                /**
                 * 打印Map
                 */
                strBuf.append(MapUtil.printMap((Map<String, Object>) element, strBuf.toString(), layer + 1));
            }
            else if (element instanceof List) {
                strBuf.append("[List]");
                strBuf.append("\n");
                /**
                 * 递归调用，打印list树
                 */
                strBuf.append(printListTree((List<?>) element, strBuf.toString(), layer + 1));
            }
            else if (element instanceof byte[]) {
            	/**
            	 * 如果是字节数组
            	 * 将字节数组转化成十六进制字符串
            	 */
                strBuf.append(new String(StringUtil.bytesToHexString((byte[]) element)));
                strBuf.append("\n");
            }
            else {
                strBuf.append(element.toString());
                strBuf.append("\n");
            }

        }

        return strBuf.toString();
    }

    /**
     * 打印层级
     * 
     * @param layer
     * @param logInfo
     * @return
     */
    public static String printLayer(int layer, String logInfo) {
        StringBuffer strBuf = new StringBuffer();
        if (0 == layer) {
            return "";
        }
        for (int i = 0; i < layer; i++) {
            strBuf.append("    ");
        }
        strBuf.append("|" + "\n");
        for (int i = 0; i < layer; i++) {
            strBuf.append("    ");
        }
        strBuf.append("+----");
        return strBuf.toString();
    }

}
