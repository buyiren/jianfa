package tc.bank.utils;

import cn.com.agree.afa.svc.javaengine.TCResult;
import tc.bank.constant.ErrorCodeModule;
import tc.platform.P_Amount;
import tc.platform.P_Time;

/**
 * 字符串工具类
 * @author AlanMa
 *
 */
public class StringUtil {
	/**
	 * 字符串是否为空
	 * @param str
	 * @return
	 */
	public static Boolean isEmpty(String str) {
		return (str != null && !"".equals(str)) ? false : true;
	}
	
	/**
	 * 字符串是否不为空
	 * @param str
	 * @return
	 */
	public static Boolean isNotEmpty(String str) {
		return (str != null && !"".equals(str)) ? true : false;
	}
	
	/**
	 * 字节转换成16进制字符串
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 格式校验
	 * @param str
	 * @param coltype
	 * @return
	 */
	public static Boolean formatChk(String str, String coltype) {
		// 金额格式校验 暂不做处理
		// 日期格式校验
		// 平台的日期格式为yyyymmdd
		if (coltype.equals("ISODate")) {
			if (!str.matches("\\d*") || str.length() != 8) {
				return false;
			}
		}
		// 时间格式校验
		// 平台的时间格式为HHMMSS
		if (coltype.equals("ISOTime")) {
			if (!str.matches("\\d*") || str.length() != 6) {
				return false;
			}
		}
		// 日期时间格式校验
		// ISODateTime-ISO日期时间 yyyy-mm-dd HH:MM:SS格式的时间 平台的日期时间格式为
		// yyyymmddHHMMSS
		if (coltype.equals("ISODateTime")) {
			if (!str.matches("\\d*") || str.length() != 8) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 格式转化
	 * @param str
	 * @param coltype
	 * @return
	 * @throws BusException
	 */
	public static TCResult formatConvert(String str, String coltype){
		String res = str;
		try {
			if (coltype.equals("ISODate")) {
				res = (String) P_Time.formatDate(str, "yyyymmdd").getOutputParams().get(0);
			}
			if (coltype.equals("ISOTime")) {
				res = (String) P_Time.formatTime(str, "hhmmss").getOutputParams().get(0);
			}
			if (coltype.equals("NoPointDecimalNumber")) {
				res = (String) P_Amount.deleteDot(str).getOutputParams().get(0);
			}
			if (coltype.equals("ISODateTime")) {
				//怎么截取 ? -  / ?
				String str1 = str.split(" ")[0];
				String str2 = str.split(" ")[1];
				str1 = (String) P_Time.formatDate(str1, "yyyymmdd").getOutputParams().get(0);
				
				str2 = (String) P_Time.formatTime(str2, "hhmmss").getOutputParams().get(0);
				res = str1 + str2;
			}

		} catch (Exception e) {
			//AppLogger.info(e);
			return TCResult.newFailureResult(ErrorCodeModule.IMC002, str);
		}
		return TCResult.newSuccessResult("1",res);
	}
	/**
	 * 按指定长度补齐(按字符补位）
	 * @param srcStr 原串
	 * @param len 补齐后的目标长度
	 * @param fillChar 补位的字符
	 * @param leftAlign 对其方向（1-左对齐，2-右对齐） 
	 * */
	public static String fill(String srcStr, int len, byte fillChar, int leftAlign) {
		// TODO Auto-generated method stub
		byte[] bytes=srcStr.getBytes();
		if(len<=bytes.length)return srcStr;
		byte[] retBytes=new byte[len];
		if(leftAlign==1){//左对齐
			System.arraycopy(bytes, 0 ,retBytes, 0, bytes.length);
			for(int i=bytes.length;i<retBytes.length;i++){
				retBytes[i]=fillChar;
			}
			return new String (retBytes);
		}else{
			for(int i=0;i<len-bytes.length;i++){
				retBytes[i]=fillChar;
			}
			System.arraycopy(bytes, 0, retBytes, len-bytes.length, bytes.length);
		}
		return null;
	}

	public static TCResult doFormat(String value , String type , String defaultvalue , int len , String must) {
		
		boolean ismust = ("Y".equals(must) || "M".equals(must)) ? true:false;
		
		if (isEmpty(value)) {
			if (isNotEmpty(defaultvalue)) {
				value = defaultvalue;
			}
			if (isEmpty(defaultvalue)) {
				if (ismust) {
					return TCResult.newFailureResult(ErrorCodeModule.IMC001, value );
				}
				if ((len != 9999) && value.length()>len) {
					return TCResult.newFailureResult(ErrorCodeModule.IMC003, value);
				}
				return TCResult.newSuccessResult(value);
			}
		}
		return formatConvert(value, type);
	}
}
