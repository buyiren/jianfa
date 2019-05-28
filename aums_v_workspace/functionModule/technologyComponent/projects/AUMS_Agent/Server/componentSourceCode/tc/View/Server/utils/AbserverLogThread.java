package tc.View.Server.utils;
import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.jcomponent.CacheOperationImpl.CacheOperationException;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharSet;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.text.SimpleDateFormat;
public class AbserverLogThread implements Callable<String> {
	private String host , account , password ,svcId;
	private final String cmd_wc = "sh hers/herssh_wc abs/log/abserver.log";
	private final String cmd_date = "sh hers/herssh_curDate";
	private final String cmd_sed_grep="sh hers/herssh_sed_grep \"410000,490000p\" abs/log/teller/20170615/zone_other/branch_other/ TUSER74100103";
	

	private Connection conn;
	private Statement st;
//	private long startLine;
	private String path;
//	private String oldDate;
	private int rmiPort;
	private int rmiJndiPort;
	private int logInterval;
	private int JVM_SCAN_SPACE;
	private String JVM_WARN_VALUE;
	private double JVM_RISE_RATIO_VALU;
	private double JVM_RISE_CON;
	private String DUMP_PATH;
	public AbserverLogThread (String host,String account,String password,String svcId,String path,Connection conn,Statement st,int rmiPort,int rmiJndiPort,int logInterval,int JVM_SCAN_SPACE,String JVM_WARN_VALUE,double JVM_RISE_RATIO_VALU,double JVM_RISE_CON,String DUMP_PATH){
		this.host=host;this.account=account;this.password=password;this.svcId=svcId;this.path=path;
		this.conn=conn;this.st=st;
//		this.startLine=startLine;this.oldDate=oldDate;
		this.rmiJndiPort=rmiJndiPort;this.rmiPort=rmiPort;this.logInterval=logInterval;
		this.JVM_SCAN_SPACE=JVM_SCAN_SPACE;this.JVM_WARN_VALUE=JVM_WARN_VALUE;this.JVM_RISE_RATIO_VALU=JVM_RISE_RATIO_VALU;this.JVM_RISE_CON=JVM_RISE_CON;this.DUMP_PATH=DUMP_PATH;
	}
	private void writeLog(String str){
//		try{
//			FileUtils.writeStringToFile(new File("./tttt"), str+"\n", "utf-8", true);
//		}catch(Exception e){
//		}
	}
	/*采集某一个实例的absserver.log，将最新内容解析出来并记录数据库*/
	public String call(){
		try {
			RuntimeInfoBean runtime=null;
			try{
				runtime=RMIClient.getRuntimeInfo(host, rmiPort, rmiJndiPort, logInterval);
			}catch(Exception e){
				return "error:"+"读取"+host+":"+rmiPort+"的jvm信息失败."+e.getMessage();
			}
			int cur_jvm_usage = Integer.parseInt(runtime.getMemoryUsage());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			String dateTime=sdf.format(new Date());
			String sqlstr = "SELECT B.SERVER_IP,B.ABS_NAME,'"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','6',A.JVM_XMX,A.JVM_XMS,A.JVM_IDL,A.JVM_USAGE,'0','"+JVM_SCAN_SPACE+"','"+JVM_WARN_VALUE+"','"+JVM_RISE_RATIO_VALU+"','"+JVM_RISE_CON+"','"+DUMP_PATH+"','0' FROM T_JVM_MEM_INFO A,T_INS_TABLE_MAPPING B WHERE A.JVM_ID IN (SELECT MAX(JVM_ID) AS JVM_ID FROM T_JVM_MEM_INFO where PARTITION_NO="+svcId+") AND A.PARTITION_NO=B.PARTITION_NO";
			writeLog("查询上一次的jvm数据。sqlstr["+sqlstr+"]");
			ResultSet rs = st.executeQuery(sqlstr);
			if(rs.next()){
				String server_ip = rs.getString(1);
				String abs_name  = rs.getString(2);
				String warn_dt   = rs.getString(3);
				String warn_tm   = rs.getString(4);
				String wt_id     = rs.getString(5);
				String jvm_xmx   = rs.getString(6);
				String jvm_xms   = rs.getString(7);
				String jvm_idl   = rs.getString(8);
				int old_jvm_usage = Integer.parseInt(rs.getString(9));
				double jvm_rise_ratio = Double.parseDouble(rs.getString(10));//这个值要重新计算
				int jvm_scan_space= Integer.parseInt(rs.getString(11));
				String jvm_warn_value=rs.getString(12);
				double jvm_rise_ratio_valu=Double.parseDouble(rs.getString(13));
				double jvm_rise_con=Double.parseDouble(rs.getString(14));
				String dump_path=rs.getString(15);
				String hd_type=rs.getString(16);
				jvm_rise_ratio=((double)cur_jvm_usage-(double)old_jvm_usage)/jvm_scan_space;
				writeLog("partiton_no["+svcId+"]前值["+old_jvm_usage+"]当前值["+cur_jvm_usage+"]间隔时间（秒）["+jvm_scan_space+"]增长率["+jvm_rise_ratio+"]占用率判断基准值["+jvm_rise_con+"]");
				if(jvm_rise_ratio>=jvm_rise_ratio_valu&&cur_jvm_usage>jvm_rise_con){
					//超出预警值，进行预警
					sqlstr="INSERT INTO T_JVM_MEM_WARN_INFO (SERVER_IP,ABS_NAME,WARN_DT,WARN_TM,WT_ID,JVM_XMX,JVM_XMS,JVM_IDL,JVM_USAGE,JVM_RISE_RATIO,JVM_SCAN_SPACE,JVM_WARN_VALUE,JVM_RISE_RATIO_VALUE,JVM_RISE_CON,DUMP_PATH,HD_TYPE)"
							+ "VALUES ('"+server_ip+"','"+abs_name+"','"+warn_dt+"','"+warn_tm+"','"+wt_id+"','"+jvm_xmx+"','"+jvm_xms+"','"+jvm_idl+"','"+cur_jvm_usage+"','"+jvm_rise_ratio+"','"+jvm_scan_space+"','"+jvm_warn_value+"','"+jvm_rise_ratio_valu+"','"+jvm_rise_con+"','"+dump_path+"','"+hd_type+"')";
					writeLog("插入预警表。sqlstr["+sqlstr+"]");
					st.executeUpdate(sqlstr);
				}
				
			}
			sqlstr="INSERT INTO T_JVM_MEM_INFO (PARTITION_NO, JVM_DT, JVM_TM, JVM_XMX, JVM_XMS, JVM_IDL, JVM_USAGE,ABC_NUM,UP_TIME) "
					+ "VALUES ("+svcId+", '"+dateTime.substring(0, 10)+"', '"+dateTime.substring(10)+"', '"+runtime.getMaxMemory()+"', '"+runtime.getTotalMemory()+"', '"+runtime.getFreeMemory()+"', '"+runtime.getMemoryUsage()+"','"+runtime.getChildCount()+"','"+runtime.getUpTime()+"')";
			writeLog(sqlstr);
			st.execute(sqlstr);
			writeLog("提交");
			conn.commit();
			if(st!=null){try{st.close();}catch(Exception e){}}
			writeLog("数据库结束");
			return "abserverLog_"+svcId+"_"+dateTime.substring(0, 10)+"_startLine_-1";
		} catch(Exception e1){
			writeLog("线程处理失败["+e1.getMessage()+"]");
			return "error:"+"读取"+host+":"+rmiPort+"的jvm信息失败."+e1.getMessage();
		}
		
/***********下面代码是通过扫描abserver.log采集jvm信息，已经作废*/
//		//		AppLogger.info("AbserverLogThread started!");
//		SSHClient base = new SSHClient(host);
//		base.setAccount(account);
//		base.setPassword(password);
//		while(true){
////			AppLogger.info("无限次尝试，直到连接成功......");
//			boolean isConnect = base.connect();
////			AppLogger.info("connecting..."+isConnect);
//			if(isConnect){
//				break;
//			}
//			try{Thread.sleep(500);}catch(Exception e){}
//		}
//		String retStr = "";
////		int i=0;
////		while(true){
//			try{
////				AppLogger.info(cmd_one+"=======================");
//				String ret = base.interaction(cmd_date,"GBK");//第二个参数为对方操作系统的语言编码
////				AppLogger.info(ret);
//				String curDate=ret.substring(0, ret.indexOf("\n"));
//				writeLog("当前日期["+curDate+"]");
//				if(!curDate.equals(oldDate)){//日期有变化，从1开始！
//					startLine=1;
//				}
////				ret = base.interaction("sh hers/herssh_wc "+path+"abserver.log","GBK");//第二个参数为对方操作系统的语言编码
////				AppLogger.info(ret);
//				writeLog(ret);
//				long lineCount=Long.parseLong(ret.substring(0,ret.indexOf(" ")));
//				writeLog("读出来的行数是["+lineCount+"]");
//				if(lineCount>startLine){
//					//文件有变化才进行采集
//					String cmd="sh hers/herssh_sed \""+startLine+","+lineCount+"p\" "+path+" abserver.log";
//					writeLog("下一条指令是["+cmd+"]");
//					ret = base.interaction(cmd,"GBK");
//					writeLog("读出来的内容是["+ret+"]");
//					String timeStr=ret.substring(ret.lastIndexOf("  INFO RuntimeInfoLogThread  RuntimeInfo:doLog")-12,ret.lastIndexOf("  INFO RuntimeInfoLogThread  RuntimeInfo:doLog"));
//					writeLog("时间["+timeStr+"]");
////					timeStr=timeStr.substring(0, 2)+timeStr.substring(3, 5)+timeStr.substring(6, 8);//+timeStr.substring(9);//去掉中间的冒号
//					timeStr=timeStr.substring(0, 8);
//					writeLog("时间2["+timeStr+"]");
//					writeLog("日期["+curDate+"]");
//					//totalMemory
//					String totalMemoryStr = ret.substring(ret.lastIndexOf("    TotalMemory = ")+"    TotalMemory = ".length());
//					String totalMemory = totalMemoryStr.substring(0, totalMemoryStr.indexOf("\n"));
//					writeLog("totalMemory["+totalMemory+"]");
//					//MaxMemory
//					String maxMemoryStr = ret.substring(ret.lastIndexOf("    MaxMemory = ")+"    MaxMemory = ".length());
//					String maxMemory = maxMemoryStr.substring(0,maxMemoryStr.indexOf("\n"));
//					writeLog("maxMemory["+maxMemory+"]");
//					//MemoryUsage
//					String memoryUsageStr = ret.substring(ret.lastIndexOf("    MemoryUsage = ")+"    MemoryUsage = ".length());
//					String memoryUsage = memoryUsageStr.substring(0,memoryUsageStr.indexOf("%\n"));
//					writeLog("memoryUsage["+memoryUsage+"]");
//					//FreeMemory
//					String freeMemoryStr = ret.substring(ret.lastIndexOf("    FreeMemory = ")+"    FreeMemory = ".length());
//					String freeMemory = freeMemoryStr.substring(0,freeMemoryStr.indexOf("\n"));
//					writeLog("freeMemory["+freeMemory+"]");
//					try {
//						String sqlstr="INSERT INTO JVM_MEM_INFO (PARTITION_INDEX, INFO_DATE, INFO_TIME, JVM_MAX, JVM_TOTAL, JVM_FREE, JVM_USAGE) "
//								+ "VALUES ("+svcId+", '"+curDate+"', '"+timeStr+"', '"+maxMemory+"', '"+totalMemory+"', '"+freeMemory+"', '"+memoryUsage+"')";
//						writeLog(sqlstr);
//						st.execute(sqlstr);
//						writeLog("提交");
//						conn.commit();
//						if(st!=null){try{st.close();}catch(Exception e){}}
//						writeLog("数据库结束");
//					} catch(Exception e1){
//						writeLog("线程处理失败["+e1.getMessage()+"]");
//					}
//					writeLog("处理结束");
//					
//				}
//				/****************最后，将数据插入数据库*********************/
//				retStr = "abserverLog_"+svcId+"_"+curDate+"_startLine_"+lineCount;
//			}catch(Exception e){
//				e.printStackTrace();
//				try{
//					writeLog("错误:"+e.getMessage());
//				}catch(Exception e1){
//					e1.printStackTrace();
//				}
//				retStr = "error:"+e.getMessage();
//			}
//		base.close();
//		if(st!=null){try{st.close();}catch(Exception e){}}
//		return retStr;
//		
	}
}
