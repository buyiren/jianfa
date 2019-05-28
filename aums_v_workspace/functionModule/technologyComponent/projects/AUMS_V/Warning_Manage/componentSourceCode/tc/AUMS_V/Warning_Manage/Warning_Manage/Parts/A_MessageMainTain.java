package tc.AUMS_V.Warning_Manage.Warning_Manage.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.security.cert.PolicyNode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * MessageMainTain
 * 
 * @date 2018-07-14 18:58:1
 */
@ComponentGroup(level = "应用", groupName = "MessageMainTain", projectName = "AUMS_V", appName = "Warning_Manage")
public class A_MessageMainTain {

	//static String poolName = "qdzh_oracle";
	static boolean commitFlg = true;

	/**
	 * @category 初始化短信联系人表格
	 * @param tableData
	 *            出参|短信联系人查询结果|{@link com.alibaba.fastjson.JSONArray}
	 * @return 1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "tableData", comment = "短信联系人查询结果", type = com.alibaba.fastjson.JSONArray.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "初始化短信联系人表格", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-14 06:59:14")
	public static TCResult A_LoadMessageMaintainTableDate() {

		JSONArray tableData = new JSONArray();
		try {
			String sql = "select * from AUMS_WARN_MESSAGEMAINTAIN";
			if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
				List messageMaintainList = (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
				P_Logger.info("messageMaintainList******: " + messageMaintainList);
				if (!messageMaintainList.isEmpty()) {
					P_Logger.info("**************messageMaintainList.size():  " + messageMaintainList.size());//8
					for (int i = 0; i < messageMaintainList.size(); i++) {
						JSONObject jo = new JSONObject();
						List list = (List) messageMaintainList.get(i);
						P_Logger.info("------------------list: " + list);
						jo.put("msgId", (String)list.get(0));
						jo.put("umId", (String) list.get(1));
						jo.put("uName", (String) list.get(5));
						jo.put("phoneNumber", (String) list.get(3));
						jo.put("emails", (String) list.get(4));
						jo.put("isSend", (String)list.get(8));
						jo.put("startTime",(String) list.get(6));
						jo.put("endTime",(String) list.get(7));
						
						String brnostr = (String) list.get(2);
						P_Logger.info("*********:brnostr  " + brnostr);
						String branchIdSql = "select BRANCHID from AUMS_BRANCHINFO where BRANCHNO like '"+brnostr+"'";
						if(P_Jdbc.dmlSelect(null, branchIdSql, -1).getOutputParams() != null){
							List list1 = (List) P_Jdbc.dmlSelect(null, branchIdSql, -1).getOutputParams().get(1);
							List list2 = (List) list1.get(0);
							String branchId = (String) list2.get(0);
							P_Logger.info("*********:branchId  " + branchId);
							jo.put("branchId", branchId);
						}
						jo.put("brno", brnostr);
						jo.put("role", (String)list.get(12));
						tableData.add(jo);
						P_Logger.info("返回表格数据为：" + tableData);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new Exception("查询短信联系人失败，数据库操作异常");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return TCResult.newSuccessResult(tableData);
	}

	/**
	 * 将HHmmss解析成HH:mm:ss
	 * 
	 * @param String
	 *            时间
	 * @return String
	 */
	public static String parseTime(String time) {
		String ftime = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
			Date date = (Date) formatter.parse(time);
			formatter = new SimpleDateFormat("HH:mm:ss");
			ftime = formatter.format(date);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ftime;
	}

	/**
	 * @category 添加短信联系人信息
	 * @param umID
	 *            入参|短信联系人工号|{@link java.lang.String}
	 * @param umName
	 *            入参|短信联系人姓名|{@link java.lang.String}
	 * @param phoneNumber
	 *            入参|短信联系人电话|{@link java.lang.String}
	 * @param phoneNumber1
	 *            入参|备用电话1|{@link java.lang.String}
	 * @param phoneNumber2
	 *            入参|备用电话2|{@link java.lang.String}
	 * @param email
	 *            入参|邮箱地址|{@link java.lang.String}
	 * @param email1
	 *            入参|备用邮箱1|{@link java.lang.String}
	 * @param email2
	 *            入参|备用有效2|{@link java.lang.String}
	 * @param isSend
	 *            入参|是否发送|{@link java.lang.String}
	 * @param startTime
	 *            入参|发送开始时间|{@link java.lang.String}
	 * @param endTime
	 *            入参|结束时间|{@link java.lang.String}
	 * @param brno
	 *            入参|机构号|{@link java.lang.String}
	 * @param role
	 *            入参|人员技能|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "umID", comment = "短信联系人工号", type = java.lang.String.class),
			@Param(name = "umName", comment = "短信联系人姓名", type = java.lang.String.class),
			@Param(name = "phoneNumber", comment = "短信联系人电话", type = java.lang.String.class),
			@Param(name = "phoneNumber1", comment = "备用电话1", type = java.lang.String.class),
			@Param(name = "phoneNumber2", comment = "备用电话2", type = java.lang.String.class),
			@Param(name = "email", comment = "邮箱地址", type = java.lang.String.class),
			@Param(name = "email1", comment = "备用邮箱1", type = java.lang.String.class),
			@Param(name = "email2", comment = "备用有效2", type = java.lang.String.class),
			@Param(name = "isSend", comment = "是否发送", type = java.lang.String.class),
			@Param(name = "startTime", comment = "发送开始时间", type = java.lang.String.class),
			@Param(name = "endTime", comment = "结束时间", type = java.lang.String.class),
			@Param(name = "brno", comment = "机构号", type = java.lang.String.class),
			@Param(name = "role", comment = "人员技能", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "添加短信联系人信息", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-16 03:25:55")
	public static TCResult A_AddMessageMainTain(String umID, String umName,
			String phoneNumber, String phoneNumber1, String phoneNumber2,
			String email, String email1, String email2, String isSend,
			String startTime, String endTime, String brno, String role) {

		JSONObject obj = new JSONObject();
		if ((phoneNumber1 == null || phoneNumber1.equals(""))
				&& (phoneNumber2 == null || phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "";
		} else if ((phoneNumber1 == null || phoneNumber1.equals(""))
				&& (phoneNumber2 != null && !phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "," + phoneNumber2;
		} else if ((phoneNumber1 != null && !phoneNumber1.equals(""))
				&& (phoneNumber2 == null || phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "," + phoneNumber1;
		} else if ((phoneNumber1 != null && !phoneNumber1.equals(""))
				&& (phoneNumber2 != null && !phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "," + phoneNumber1 + "," + phoneNumber2;
		}
		if ((email1 == null || email1.equals(""))
				&& (email2 == null || email2.equals(""))) {
			email = email + "";
		} else if ((email1 == null || email1.equals(""))
				&& (email2 != null && !email2.equals(""))) {
			email = email + "," + email2;
		} else if ((email1 != null && !email1.equals(""))
				&& (email2 == null || email2.equals(""))) {
			email = email + "," + email1;
		} else if ((email1 != null && !email1.equals(""))
				&& (email2 != null && !email2.equals(""))) {
			email = email + "," + email1 + "," + email2;
		}
		String getMsgIDSql = "select nvl((max(to_number(MSGID))),0)+1 from AUMS_WARN_MESSAGEMAINTAIN";
		if(P_Jdbc.dmlSelect(null, getMsgIDSql, -1).getOutputParams() != null){
			List<?> msgIDlist = (List<?>) P_Jdbc.dmlSelect(null, getMsgIDSql, -1).getOutputParams().get(1);
			P_Logger.info("msgIDlist***************:   " + msgIDlist);
			List list = (List) msgIDlist.get(0);
			// list.get(0) 为BigDecimal格式
			String msgID = list.get(0).toString();
			String sql = "insert into AUMS_WARN_MESSAGEMAINTAIN (MSGID, UMID, BRNO, PHONENUMBER, EMAILS, UMNAME, STARTTIME, ENDTIME, ISSEND, REMARK1, REMARK2, REMARK3, ROLE) VALUES "
					+ "('"
					+ msgID
					+ "','"
					+ umID
					+ "',(select BRANCHNO from AUMS_BRANCHINFO where BRANCHID like '"+brno+"'),'"
					+ phoneNumber
					+ "','"
					+ email
					+ "','"
					+ umName
					+ "','"
					+ startTime
					+ "','"
					+ endTime
					+ "','"
					+ isSend
					+ "','','','','" + role + "')";
			try {
				P_Jdbc.executeSQL(null, sql, commitFlg);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					throw new Exception("保存短信联系人信息失败，数据库操作异常！");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		obj.put("resultFlag", true);
		obj.put("alertMsg", "添加短信联系人成功！");

		return TCResult.newSuccessResult(obj);

	}

	/**
	 * @category 修改短信联系人信息
	 * @param umID
	 *            入参|短信联系人工号|{@link java.lang.String}
	 * @param umName
	 *            入参|短信联系人姓名|{@link java.lang.String}
	 * @param phoneNumber
	 *            入参|短信联系人电话|{@link java.lang.String}
	 * @param phoneNumber1
	 *            入参|备用电话1|{@link java.lang.String}
	 * @param phoneNumber2
	 *            入参|备用电话2|{@link java.lang.String}
	 * @param email
	 *            入参|邮箱地址|{@link java.lang.String}
	 * @param email1
	 *            入参|备用邮箱1|{@link java.lang.String}
	 * @param email2
	 *            入参|备用有效2|{@link java.lang.String}
	 * @param isSend
	 *            入参|是否发送|{@link java.lang.String}
	 * @param startTime
	 *            入参|发送开始时间|{@link java.lang.String}
	 * @param endTime
	 *            入参|结束时间|{@link java.lang.String}
	 * @param brno
	 *            入参|机构号|{@link java.lang.String}
	 * @param role
	 *            入参|人员技能|{@link java.lang.String}
	 * @param msgId
	 *            入参|msg主键ID|{@link java.lang.String}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "umID", comment = "短信联系人工号", type = java.lang.String.class),
			@Param(name = "umName", comment = "短信联系人姓名", type = java.lang.String.class),
			@Param(name = "phoneNumber", comment = "短信联系人电话", type = java.lang.String.class),
			@Param(name = "phoneNumber1", comment = "备用电话1", type = java.lang.String.class),
			@Param(name = "phoneNumber2", comment = "备用电话2", type = java.lang.String.class),
			@Param(name = "email", comment = "邮箱地址", type = java.lang.String.class),
			@Param(name = "email1", comment = "备用邮箱1", type = java.lang.String.class),
			@Param(name = "email2", comment = "备用有效2", type = java.lang.String.class),
			@Param(name = "isSend", comment = "是否发送", type = java.lang.String.class),
			@Param(name = "startTime", comment = "发送开始时间", type = java.lang.String.class),
			@Param(name = "endTime", comment = "结束时间", type = java.lang.String.class),
			@Param(name = "brno", comment = "机构号", type = java.lang.String.class),
			@Param(name = "role", comment = "人员技能", type = java.lang.String.class),
			@Param(name = "msgId", comment = "msg主键ID", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "修改短信联系人信息", style = "处理型", type = "同步组件", date = "2018-07-16 04:59:18")
	public static TCResult A_UpdateMessageMainTain(String umID, String umName,
			String phoneNumber, String phoneNumber1, String phoneNumber2,
			String email, String email1, String email2, String isSend,
			String startTime, String endTime, String brno, String role,
			String msgId) {
		JSONObject obj = new JSONObject();
		if ((phoneNumber1 == null || phoneNumber1.equals(""))
				&& (phoneNumber2 == null || phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "";
		} else if ((phoneNumber1 == null || phoneNumber1.equals(""))
				&& (phoneNumber2 != null && !phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "," + phoneNumber2;
		} else if ((phoneNumber1 != null && !phoneNumber1.equals(""))
				&& (phoneNumber2 == null || phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "," + phoneNumber1;
		} else if ((phoneNumber1 != null && !phoneNumber1.equals(""))
				&& (phoneNumber2 != null && !phoneNumber2.equals(""))) {
			phoneNumber = phoneNumber + "," + phoneNumber1 + "," + phoneNumber2;
		}
		if ((email1 == null || email1.equals(""))
				&& (email2 == null || email2.equals(""))) {
			email = email + "";
		} else if ((email1 == null || email1.equals(""))
				&& (email2 != null && !email2.equals(""))) {
			email = email + "," + email2;
		} else if ((email1 != null && !email1.equals(""))
				&& (email2 == null || email2.equals(""))) {
			email = email + "," + email1;
		} else if ((email1 != null && !email1.equals(""))
				&& (email2 != null && !email2.equals(""))) {
			email = email + "," + email1 + "," + email2;
		}
		String sql = "update AUMS_WARN_MESSAGEMAINTAIN set UMID = '" + umID
				+ "', BRNO = (select BRANCHNO from AUMS_BRANCHINFO where BRANCHID like '"+brno+"'), PHONENUMBER = '" + phoneNumber
				+ "', EMAILS = '" + email + "', " + "UMNAME = '" + umName
				+ "', STARTTIME = '" + startTime + "', ENDTIME = '"
				+ endTime + "', ISSEND = '" + isSend + "', "
				+ "REMARK1 = '', REMARK2 = '', REMARK3 = '', ROLE = '" + role
				+ "' where MSGID = '" + msgId + "'";
		try {
			P_Jdbc.executeSQL(null, sql, commitFlg);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new Exception("修改短信联系人信息失败，数据库操作异常！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		obj.put("resultFlag", true);
		obj.put("alertMsg", "修改短信联系人成功！");
		return TCResult.newSuccessResult(obj);
	}

	/**
	 * @category 删除短信联系人
	 * @param umID
	 *            入参|短信联系人ID|{@link java.lang.String}
	 * @return 1 成功<br/>
	 * @throws Exception 
	 */
	@InParams(param = { @Param(name = "umID", comment = "短信联系人ID", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "删除短信联系人", style = "处理型", type = "同步组件", author = "bj", date = "2018-07-16 06:14:48")
	public static TCResult A_delMessageMainTain(String umID) throws Exception {
		
		String sql = "select count(policyID) countNum from AUMS_WARN_POLICYPEOPLEMAPPING where UMID_A = '"
				+ umID + "' or UMID_B = '" + umID + "'";
		String delMsgPeoplesql = "delete from AUMS_WARN_MESSAGEMAINTAIN where UMID = '"
				+ umID + "'";
		String policyIDNum = null;
		try {
			if(P_Jdbc.dmlSelect(null, sql, -1).getOutputParams() != null){
				List listcountNum =  (List) P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
				P_Logger.info("***********************listcountNum" + listcountNum);
				List list = (List) listcountNum.get(0);
				policyIDNum = list.get(0).toString();
				P_Logger.info("*******policyIDNum:  " + policyIDNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("查询短信联系人绑定的预警策略失败，数据库操作异常！");
		}
		if (Integer.parseInt(policyIDNum) > 0) {
			throw new Exception("该短信联系人绑定了预警策略，请先解除绑定后在试！");
		} else {
				P_Jdbc.executeSQL(null, delMsgPeoplesql, commitFlg);
				return TCResult.newSuccessResult();
		}
		
		
		
	}

}
