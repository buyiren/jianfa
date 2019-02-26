package tc.bank.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Map工具类
 * @author AlanMa
 *
 */
public class MapUtil {
	
	/**
	 * 构建map
	 * @param entry
	 * @return
	 */
	public static Map<String, String> toStrMap(String... entry) {
		Map<String, String> returnMap = new HashMap<String, String>();
		
		for (int i = 0; i < entry.length; i++) {
			returnMap.put(entry[i], entry[++i]);
		}
		
		return returnMap;
	}
	
	/**
	 * 构建Map
	 * @param entry
	 * @return
	 */
	public static Map<String, Object> toMap(Object... entry) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		for (int i = 0; i < entry.length; i++) {
			returnMap.put((String)entry[i], entry[++i]);
		}
		
		return returnMap;
	}
	
	/**
	 * 打印Map
	 * @param map
	 * @param logInfo
	 * @param layer
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String printMap(Map map, String logInfo, int layer) {
		StringBuffer strBuf = new StringBuffer();

		Iterator it = map.entrySet().iterator();
		Map.Entry entry = null;
		Object key = null;
		Object value = null;
		while (it.hasNext()) {
			entry = (Map.Entry) it.next();
			key = entry.getKey();
			value = entry.getValue();
			/**
			 * 打印层级
			 */
			strBuf.append(ListUtil.printLayer(layer, logInfo));
			strBuf.append("\"" + key + "\" = ");
			if (value == null) {
				strBuf.append("\n");
				continue;
			}
			if (value instanceof Map) {
				strBuf.append("[Map]");
				strBuf.append("\n");
				/**
				 * 递归调用
				 * 打印Map
				 */
				strBuf.append(printMap((Map) value, strBuf.toString(),
						layer + 1));
			} else if (value instanceof List) {
				strBuf.append("[List]");
			    /**
			     * 打印树状List
			     */
				strBuf.append(ListUtil.printList((List) value, strBuf.toString(),
						layer + 1));
			} else if (value instanceof byte[]) {
				/**
            	 * 如果是字节数组
            	 * 将字节数组转化成十六进制字符串
            	 */
				strBuf.append(new String(StringUtil.bytesToHexString((byte[]) value)));
				strBuf.append("\n");
			} else {
				strBuf.append(value.toString());
				strBuf.append("\n");
			}
		}

		return strBuf.toString();
	}

}
