package tc.AUMS_V.Version_Manage.util.depend;

import java.util.HashMap;
import java.util.Map;

public class StringUtil {
	/**
	 * 判断是否是空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否不是空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if((str!=null)&&!"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 对字符串类型的对象进行输出
	 * null对象反悔空串
	 * 字符串对象输出去掉左右侧空格
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj){
		return obj==null ? "" :obj.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Map<String,String> getMappingInfo(String str){
		Map<String,String> result=new HashMap<String, String>();
		String[] key_value_list=str.split(",");
		for(int i=0;i<key_value_list.length;i++){
			String[] key_value=key_value_list[i].split("#");
			result.put(key_value[0],key_value[1]);
		}
		return result;
	}
}
