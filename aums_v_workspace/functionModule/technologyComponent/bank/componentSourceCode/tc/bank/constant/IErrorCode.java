package tc.bank.constant;

import cn.com.agree.afa.svc.javaengine.context.JavaDict;

/**
 * 用于定义错误码和错误信息 <br>
 * 错误码规则：应用类型分类{2位}+错误类型分类{1位}+错误序号{3位}<br>
 * 应用类型分类：<br>
 * CI——渠道整合平台<br>
 * 错误类型分类：<br>
 * C-信息校验类<br>     
 * B-业务信息规则类<br> 
 * D-数据库操作类<br>   
 * T-系统间通讯类<br>   
 * E-其他系统异常类
 * Org=Organization 机构
 * Ser=Service 服务
 * Config=Configuration 配置
 * Chl=channel 渠道
 * @version 1.0.0
 */
public class IErrorCode {
			public static final String  getCode(String Constant){
				JavaDict CodeConstant=new JavaDict();
				//成功
				CodeConstant.put("Success", 				"000000");	//成功
				
				//C-信息校验类
				CodeConstant.put("ChlNotRegist", 			"CIC001"); 	//渠道未注册
				CodeConstant.put("ChlNotOpen", 				"CIC002");	//渠道未启用
				CodeConstant.put("ChlNotEffect", 			"CIC003");	//渠道未生效或已失效
				CodeConstant.put("OrgNotExist", 			"CIC004");	//机构不存在
				CodeConstant.put("SerNotRegist", 			"CIC005");	//服务未注册
				CodeConstant.put("SerNotEnabled", 			"CIC006");	//服务未启用
				CodeConstant.put("serNotEffective", 		"CIC007");	//服务未生效或已失效
				CodeConstant.put("OrgOpenChlNotExist", 		"CIC008");	//机构开通渠道未配置
				CodeConstant.put("OrgNotOpenChl", 			"CIC009");	//机构未开通渠道
				CodeConstant.put("OrgOpenChlNotEffective", 	"CIC010");	//机构开通渠道未生效或已失效
				CodeConstant.put("SerOpenChlNotExist", 		"CIC011");	//服务开通渠道未配置
				CodeConstant.put("SerNotOpenChl", 			"CIC012");	//服务未开通渠道
				CodeConstant.put("SerOpenChlNotEffective", 	"CIC013");	//服务开通渠道未生效或已失效
				CodeConstant.put("RequestIsNotInSerTime", 	"CIC014");	//请求不在服务的开放时间内
				CodeConstant.put("SerOpenOrgNotExist", 		"CIC015");	//服务开通机构未配置
				CodeConstant.put("SerNotOpenOrg", 			"CIC016");	//服务未开通机构
				CodeConstant.put("SerOpenOrgNotEffective", 	"CIC017");	//服务开通机构未生效或已失效
				CodeConstant.put("RepeatRuleNotExist", 		"CIC018");	//未找到服务登记的判重规则
				CodeConstant.put("RepeatSerRequest", 		"CIC019");	//重复的服务请求
				CodeConstant.put("FieldMustBeEntered", 		"CIC020"); 	//字段必输
				CodeConstant.put("FieldFormatConversError", "CIC021"); 	//字段格式错误
				CodeConstant.put("OrgCodeNotFound", 		"CIC022"); 	//机构代码未上送或未配置渠道的虚拟机构
				CodeConstant.put("FieldExceedsMaximum",     "CIC023"); 	//字段超出最大长度限制
				CodeConstant.put("InputFieldVauleExist",    "CIC024"); 	//上送字段必须输入
				
				CodeConstant.put("PropertiesNotExist",      "CIC025"); 	//配置文件未找到
				CodeConstant.put("PropertiesCloseException", "CIC026"); 	//文件关闭异常
				CodeConstant.put("TokenCheckFailed",    "CIC027"); 	//token校验失败
				CodeConstant.put("TokenExpired",    "CIC028"); 	//token已过期
				
				
				
				//B-业务信息规则类
				CodeConstant.put("MachineOperateError", "CIB001"); 	//机具操作失败

				//D-数据库操作类
				CodeConstant.put("RecorDoesNotMeet", 		"CID001");	//无满足条件记录
				CodeConstant.put("SelectError", 			"CID022");	//数据库查询错误
				//T-系统间通讯类
				CodeConstant.put("HostCommTimeout",			"CIT001");	//主机通讯超时
				CodeConstant.put("HostCommNotExist",		"CIT002");	//主机通讯参数未配置

				//E-其他系统异常类
				CodeConstant.put("SystemException", 		"CIE999");	//系统异常
				
				//ACT账务类
					//B-业务信息类
					CodeConstant.put("ChlLmtNotExist", 			"ACB001"); 	//渠道限额未配置
					CodeConstant.put("ChlAmountTooLarge", 		"ACB002"); 	//渠道单笔金额超限
					CodeConstant.put("ChlDayLmtNotEnough", 		"ACB003"); 	//渠道日限额不足
					CodeConstant.put("ChlMonthLmtNotEnough", 	"ACB004"); 	//渠道月限额不足
					CodeConstant.put("OrgLmtNotExist", 			"ACB005"); 	//机构限额未配置
					CodeConstant.put("OrgAmountTooLarge", 		"ACB006"); 	//机构单笔金额超限
					CodeConstant.put("OrgDayLmtNotEnough", 		"ACB007"); 	//机构日限额不足
					CodeConstant.put("OrgMonthLmtNotEnough", 	"ACB008"); 	//机构月限额不足
					CodeConstant.put("ChlAuthCodeNotExist", 	"ACB009"); 	//渠道授权码不存在
					CodeConstant.put("OrgAuthCodeNotExist", 	"ACB010"); 	//机构授权码不存在
					CodeConstant.put("ChlAuthCodeNotMatch", 	"ACB011"); 	//渠道授权码不匹配
					CodeConstant.put("OrgAuthCodeNotMatch", 	"ACB012"); 	//机构授权码不匹配
				//CFC车友会
					//B-车友会业务信息  cfb001
					
					
				return CodeConstant.getStringItem(Constant);
			}
	}
