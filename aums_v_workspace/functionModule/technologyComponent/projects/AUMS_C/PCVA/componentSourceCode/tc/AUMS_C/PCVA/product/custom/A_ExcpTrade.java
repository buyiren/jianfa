package tc.AUMS_C.PCVA.product.custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 异常处理类
 * 
 * @date 2017-10-19 21:27:56
 */
@ComponentGroup(level = "应用", groupName = "异常处理类")
public class A_ExcpTrade {

	/**
	 * @category 错误码匹配交易规则ID
	 * @param TradeRuleList
	 *            入参|交易规则列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param TradeRuleCount
	 *            入参|交易规则记录大小|int
	 * @param TradeErrorCode
	 *            入参|错误码|{@link java.lang.String}
	 * @param TradeRuleData
	 *            出参|交易规则数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 无记录<br/>
	 */
	@InParams(param = {
			@Param(name = "TradeRuleList", comment = "交易规则列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "TradeRuleCount", comment = "交易规则记录大小", type = int.class),
			@Param(name = "TradeErrorCode", comment = "错误码", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "TradeRuleData", comment = "交易规则数据", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无记录") })
	@Component(label = "错误码匹配规则ID", style = "判断型", type = "同步组件", comment = "通过错误匹配到规则ID", date = "2017-10-19 09:56:40")
	public static TCResult A_GetTradeBycode(JavaList TradeRuleList,
			int TradeRuleCount, String TradeErrorCode) {

		JavaDict returndata = new JavaDict();
		String flag = "0";
		for (int i = 0; i < TradeRuleCount; i++) {
			JavaDict tmpreturndata = new JavaDict();
			tmpreturndata = (JavaDict) TradeRuleList.get(i);
			//System.out.println(tmpreturndata);
			AppLogger.debug("处理规则表数据 :"+tmpreturndata);

			String tmpopr = tmpreturndata.getStringItem("CODEOPR");
			String tmplistcode = tmpreturndata.getStringItem("ERRORCODELIST");

			// 操作符 = in !=  *(所有的都适用)
			if ("=".equals(tmpopr)) {
				if (tmplistcode.equalsIgnoreCase(TradeErrorCode)) {
					flag = "1";
				}

			} else if ("!=".equals(tmpopr)) {
				if (!tmplistcode.equalsIgnoreCase(TradeErrorCode)) {
					flag = "1";
				}
				
			} else if ("*".equals(tmpopr)) {
			
				flag = "1";	

			} else if ("in".equals(tmpopr)) {
				if(tmplistcode.indexOf(TradeErrorCode)!=-1)
				{
					flag = "1";
				}

			}

			// flag等于1，表示已经匹配，直接返回。不等于1表示未匹配，继续循环
			if ("1".equals(flag)) {
				returndata = tmpreturndata;
				break;
			}

		}
		
		if ("1".equals(flag)) {
			return TCResult.newSuccessResult(returndata);
		}else
		{
			return new TCResult(2, ErrorCode.AGR, "根据错误码未找到对应的规则ID");
		}
		
		
	}
		
	
	/**
	 * @category 计算日期区间
	 * @param NowDate
	 *            入参|当前日期|{@link java.lang.String}
	 * @param LongDay
	 *            入参|区间天数|long
	 * @param DateList
	 *            出参|区间日期|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param TotalDay
	 *            出参|总天数|{@link int}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "NowDate", comment = "当前日期", type = java.lang.String.class),
			@Param(name = "LongDay", comment = "区间天数", type = int.class) })
	@OutParams(param = {
			@Param(name = "DateList", comment = "区间日期", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "TotalDay", comment = "总天数", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "计算日期区间", style = "判断型", type = "同步组件", comment = "根据当前日期,计算区间里的日期列表，并返回总天数", date = "2017-05-02 04:19:50")
	public static TCResult A_CalculateDate(String NowDate, int LongDay) {

		JavaList datelist = new JavaList();

		int TotalDay = Math.abs(LongDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = new Date(sdf.parse(NowDate).getTime());
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			return TCResult.newFailureResult(ErrorCode.AGR, e.toString());
		}

		// 添加当前日期
		datelist.add(NowDate);
		// 去除当前记录数
		int Foreachday = TotalDay - 1;
		for (int i = 1; i <= Foreachday; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if (LongDay > 0) {
				cal.add(Calendar.DATE, i);
			} else {
				cal.add(Calendar.DATE, -i);
			}
			String date1 = null;
			date1 = sdf.format(cal.getTime());
			datelist.add(date1.toString());
		}

		return TCResult.newSuccessResult(datelist, datelist.size());
	}

	

	/**
	 * @category 整数减法
	 * @param IntValue
	 *            入参|整型值|{@link java.lang.String}
	 * @param SubNum
	 *            入参|减几操作|int
	 * @param IntReturnValue
	 *            出参|操作返回值|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "IntValue", comment = "整型值", type = int.class),
			@Param(name = "SubNum", comment = "减几操作", type = int.class) })
	@OutParams(param = { @Param(name = "IntReturnValue", comment = "操作返回值", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "整数减法", style = "判断型", type = "同步组件", comment = "对变量进行减几操作", author = "Anonymous", date = "2017-05-03 03:44:33")
	public static TCResult A_IntSub(int IntValue, int SubNum) {
		int returnint = 0;
		returnint = IntValue - SubNum;
		return TCResult.newSuccessResult(returnint);
	}

	public static void main(String[] args) {

//		JavaList tt = new JavaList();
//		JavaDict dt = new JavaDict();
//		dt.put("aa", "111");
//		dt.put("bb", "222");
//		JavaDict dt1 = new JavaDict();
//		dt1.put("aa", "333");
//		dt1.put("bb", "444");
//
//		tt.add(dt);
//		tt.add(dt1);
//		System.out.println(tt);
//
//		for (int i = 0; i < 2; i++) {
//
//			JavaDict javadata = (JavaDict) tt.get(i);
//			System.out.println(javadata);
//			System.out.println(javadata.get("aa"));
//
//			// String linestr = (String) ((JavaDict) tt.get(i))
//			// .get("aa");
//			// System.out.println(linestr);
//
//		}
		
		String data="-215,-204,-212,-201,-205,-216,-217,-210";
		System.out.println(data.indexOf("-205"));
		
	

	}

}
