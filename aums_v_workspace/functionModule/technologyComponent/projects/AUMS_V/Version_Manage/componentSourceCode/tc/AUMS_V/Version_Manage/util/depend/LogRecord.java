package tc.AUMS_V.Version_Manage.util.depend;

/**
 * 全局日志记录
 * @author wot_caozhipeng_zt
 *
 */
public class LogRecord {
	/**
	 * 全局操作日志记录
	 * @param descrption
	 * @param remark
	 * @param userno
	 * @throws Exception
	 */
	public static void LogRecordService(String descrption,String remark){
//		IDbSupport dbSupport = (IDbSupport) WebApplicationContextContainer
//				.getBean(IDbSupport.class);
		System.out.println("服务调起");
//		DefaultUser user = (DefaultUser) ASAPI.authenticator().getCurrentUser();
//		String username = user.getUserVO().getUsername();
//		String opidsql = "select nextval for T_PCVA_OPERATELOG_SEQ FROM sysibm.dual";//db2
//		String opidsql = "select T_PCVA_OPERATELOG_SEQ.NEXTVAL FROM DUAL";//oracle
//		List<?> opidList;
		try {
//			opidList = dbSupport.queryDataBySql(opidsql);
//			Integer opNum = (Integer) opidList.get(0);//db2
//			BigDecimal opNum = (BigDecimal) opidList.get(0);//oracle
//			String opid = String.valueOf(opNum.intValue());
//			String sql = "insert into T_PCVA_OPERATELOG ( WORKDATE, OPERATEEVENT,OPERATERESULE,OPERATETIME, OPERATEUSERNO, REMARK1)values ( '"+DateUtils.getWindowsSysDate()+"', '"+descrption+"','1', '"+DateUtils.getWindowsSysTime().substring(8,14)+"', '"+username+"', '"+remark+"')";
//			dbSupport.executeSQL(sql);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}
