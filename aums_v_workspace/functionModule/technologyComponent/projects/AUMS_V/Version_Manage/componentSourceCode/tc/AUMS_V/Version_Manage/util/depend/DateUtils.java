package tc.AUMS_V.Version_Manage.util.depend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String dateTime = "yyyyMMddHHmmss";

	private static String defaultDatePattern = "yyyy-MM-dd";

	private static String easyDatePattern = "yyyyMMdd";

	private static String easyTimePattern = "HHmmss";

	public static String complicatedDatePattern = "yyyy-MM-dd HH:mm:ss";

	public static String easyComplicatedDatePattern = "yyyyMMddHHmmssSSS";

	public static String printDatePattern = "yyyy/MM/dd HH:mm:ss";

	public static String timePattern = "HH:mm:ss";

	public static String accountDate;
	
	/**
	 * 取得服务器系统日期
	 * 
	 * @return yyyyMMdd
	 */
	public static String getWindowsSysDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(easyDatePattern);
		return simpleDateFormat.format(new Date());
	}
	/**
	 * 取得服务器系统日期时间
	 * 
	 * @return yyyyMMddHHmmss
	 */
	public static String getWindowsSysDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(easyDatePattern);
		return simpleDateFormat.format(new Date());
	}


	/**
	 * 取得服务器系统时间
	 * 
	 * @return HHmmss
	 */
	public static String getWindowsSysTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTime);
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 取得服务器系统时间，精确到毫秒
	 * 
	 * @return HHmmssSSS
	 */
	public static String getWindowsSysTimeSSS() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmssSSS");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 得到完整的服务器的时间-格式为yyyyMMddHHmmssSSS
	 * 
	 * @param trade
	 * @return
	 */
	public static String getWindowsSysTimeFull() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(easyComplicatedDatePattern);
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 取得服务器系统时间的毫秒
	 * 
	 * @return SSS
	 */
	public static String getWindowsSysSSS() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSS");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 得到会计时间，格式为："HHmmss"
	 * 
	 * @return 会计时间
	 * @throws Exception
	 */
	public static String getSysTime() throws Exception {
		return getWindowsSysTime();
	}

	/**
	 * Date类型转String
	 * 
	 * @param Date
	 *            日期
	 * @return String 格式："yyyy-MM-dd"
	 */
	public static String getTimeString1(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(defaultDatePattern);
		return sdf.format(date);
	}
	/**
	 * Date类型转String
	 * 
	 * @param Date
	 *            日期
	 * @return String 格式："yyyy-MM-dd HH:mm:ss"
	 */
	public static String getTimeString3(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(complicatedDatePattern);
		return sdf.format(date);
	}
	/**
	 * Date类型转String
	 * 
	 * @param Date
	 *            日期
	 * @return String 格式："HH:mm:ss"
	 */
	public static String getTimeString4(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
		return sdf.format(date);
	}
	/**
	 * Date类型转String
	 * 
	 * @param Date
	 *            日期
	 * @return String 格式："HHmmss"
	 */
	public static String getTimeString6(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(easyTimePattern);
		return sdf.format(date);
	}
	/**
	 * String类型转Date HHMMSSNNN
	 * 
	 * @param String
	 *            格式："yyyyMMdd"
	 * @return Date 日期
	 */
	public static Date getStringTime5(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(easyDatePattern);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 比较两个日期，返回它们之间相差的天数,不足一天返回0
	 * 
	 * @param date1
	 *            日期
	 * @param date2
	 *            日期
	 * @return 相差的天数，如果 date1 小于 date2 返回 负数
	 */
	public static int compareDateOnDay(Date date1, Date date2) {
		long ss = date1.getTime() - date2.getTime();
		long day = 24 * 60 * 60 * 1000;
		return Integer.parseInt(ss / day + "");
	}
	/**
	 * 比较两个日期，返回它们之间相差的天数,不足一天返回0
	 * 
	 * @param dateString1
	 *            格式"yyyyMMdd"
	 * @param dateString2
	 *            格式"yyyyMMdd"
	 * @return 相差的天数，如果 date1 小于 date2 返回 负数
	 * @throws ParseException
	 */
	public static int compareDateOnDay(String dateString1, String dateString2) throws ParseException {
		Date date1 = parse(easyDatePattern, dateString1);
		Date date2 = parse(easyDatePattern, dateString2);
		return compareDateOnDay(date1, date2);
	}
	
	/**
	 * String 类型转Date
	 * 
	 * @param pattern
	 *            日期格式
	 * @param date
	 *            日期
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parse(String pattern, String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(date);
	}
	/**
	 * 从原日期转换为目标日期
	 * 
	 * @param sourcePattern
	 *            源日期格式
	 * @param targetPattern
	 *            目标日期格式
	 * @param dateStr
	 *            日期
	 * @return 转换格式后的日期
	 * @throws Exception
	 * @author reborn
	 */
	public static String dateFormat(String sourcePattern, String targetPattern, String dateStr)
			throws Exception {
		if (dateStr == null || "".equals(dateStr))
			return "";
		SimpleDateFormat format = new SimpleDateFormat(sourcePattern);
		Date date = format.parse(dateStr);
		format = new SimpleDateFormat(targetPattern);
		String newDate = format.format(date);
		return newDate;
	}
	public static String dateFormatNormal(String dateStr){
		if (dateStr == null || "".equals(dateStr))
			return "";
		SimpleDateFormat format = new SimpleDateFormat(defaultDatePattern);
		Date date=null;
		try {
			date= format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		format = new SimpleDateFormat(easyDatePattern);
		return format.format(date);
	}
	
	/**
	 * 将yyyyMMdd解析成yyyy-MM-dd
	 * 
	 * @param String
	 *            日期
	 * @return String 
	 */
	public static String parseDate(String time) {
		String fdate="";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date date=formatter.parse(time);
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			fdate=formatter.format(date);
			
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return fdate;
	}
	
	/**
	 * 将HHmmss解析成HH:mm:ss
	 * 
	 * @param String
	 *            时间
	 * @return String 
	 */
	public static String parseTime(String time) {
		String ftime="";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
			Date date=formatter.parse(time);
			formatter = new SimpleDateFormat("HH:mm:ss");
			ftime=formatter.format(date);
			
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return ftime;
	}
}
