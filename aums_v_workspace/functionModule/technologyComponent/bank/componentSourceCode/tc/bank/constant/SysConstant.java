package tc.bank.constant;

import cn.com.agree.afa.svc.javaengine.context.JavaDict;

/**
 * 用于定义系统常量信息<br>
 * @version 1.0.0
 */
public class SysConstant {
		public static final String  getSysConstant(String Constant){
			JavaDict SysConstant=new JavaDict();
			//第三方系统名称
			SysConstant.put("HostName_host", "host");//核心
			SysConstant.put("HostName_card", "card"); //卡系统
			SysConstant.put("HostName_credit", "credit");//信用卡中心
			SysConstant.put("HostName_cfcbank", "cfcbank");//车友会行内系统
			SysConstant.put("HostName_cfcjtgl", "cfcjtgl");//车友会交管局系统
			
			
			//数据库类型
			SysConstant.put("DatabaseType", "ORA");//数据库类型
			
			//特殊渠道代码
			SysConstant.put("SelfServiceChl", "02");//自助渠道
			
			return SysConstant.getStringItem(Constant);
		}
	}
