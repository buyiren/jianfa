package tc.View.Server.utils;

import org.apache.commons.io.FileUtils;

import cn.com.agree.afa.svc.javaengine.AppLogger;

import java.util.concurrent.*;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
public class CpuGathererThread implements Callable<String> {
	private Connection conn;
	private Statement st;
	private String host , account , password ;
	private int C_SCAN_SPACE;
	private double C_PER_WARN_VALUE,C_RISE_RATIO_VALUE,C_RISE_CON;
	private final String cmd_cpu = "sh hers/herssh_cpu";
	//采集各实例的端口号的监听状态，key1为实例id，key2为端口名字（包括port，rmiport，rmijndiport），value2为端口号
	private HashMap<String,HashMap<String,String>> InsPortMap = new HashMap<String,HashMap<String,String>>();
	
	private void writeLog(String str){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			FileUtils.writeStringToFile(new File("./tttt"), sdf.format(new Date())+" 采集CPU ip["+this.host+"]:"+str+"\n", "utf-8", true);
		}catch(Exception e){
		}
	}
	public CpuGathererThread(Connection conn,Statement st,String host,String account,String password,int C_SCAN_SPACE,double C_PER_WARN_VALUE,double C_RISE_RATIO_VALUE,double C_RISE_CON,HashMap<String,HashMap<String,String>> InsPortMap){
		this.host=host;this.conn=conn;this.st=st;this.account=account;this.password=password;
		this.C_SCAN_SPACE=C_SCAN_SPACE;
		this.C_PER_WARN_VALUE=C_PER_WARN_VALUE;this.C_RISE_RATIO_VALUE=C_RISE_RATIO_VALUE;this.C_RISE_CON=C_RISE_CON;
		this.InsPortMap=InsPortMap;
	}
	//采集一个abs服务器的cpu信息
	public String call(){
		SSHClient base = new SSHClient(host);
		base.setAccount(account);
		base.setPassword(password);
//		try{
//		base.setPassword( EncryptUtil.decryptor(password));
//		}catch(Exception e){
//			System.out.println("error:解密失败host["+host+"]passwd["+password+"]");
//			e.printStackTrace();
//			writeLog("error:采集cpu。解密失败host["+host+"]passwd["+password+"]");
//			return host;
//		}
		int retryTimes=20;
		while(retryTimes>0){
			boolean isConnect = base.connect();
			if(isConnect){
				break;
			}
			try{Thread.sleep(500);}catch(Exception e){}
			retryTimes--;
			writeLog("再次尝试ssh，直到连接成功......");
		}
		if(retryTimes==0){
			System.out.println("error:无法连接到服务器["+host+"]");
			writeLog("error:无法连接到服务器["+host+"]");
			return host;
		}
//		String retStr = "";
		try{
//			int times=0;
			while(true){
//				if(times>0)throw new Exception("调试用的，测试退出循环");//仅用于测试退出时的处理
//				times++;
				Date beginT = new Date();
//				AppLogger.info(cmd_one+"=======================");
				//先采集端口号监听状态
				Object[] svcId = InsPortMap.keySet().toArray();
				for(int i=0;i<svcId.length;i++){
					HashMap<String,String> portMap = InsPortMap.get(svcId[i]);
					String port = (String)portMap.get("port");
					String rmiport = (String)portMap.get("rmiport");
					String rmijndiport = (String)portMap.get("rmijndiport");
					String netstat_port = base.interaction("netstat -an|grep \":::"+port+"\"", "GBK");
					if(netstat_port==null||netstat_port.equals(""))netstat_port = "No LISTEN";
					else netstat_port = getPortStatus(netstat_port);
					String netstat_rmiport = base.interaction("netstat -an|grep \":::"+rmiport+"\"", "GBK");
					if(netstat_rmiport==null||netstat_rmiport.equals(""))netstat_rmiport = "No LISTEN";
					else netstat_rmiport = getPortStatus(netstat_rmiport);
					String netstat_rmijndiport = base.interaction("netstat -an|grep \":::"+rmijndiport+"\"", "GBK");
					if(netstat_rmijndiport==null||netstat_rmijndiport.equals(""))netstat_rmijndiport = "No LISTEN";
					else netstat_rmijndiport = getPortStatus(netstat_rmijndiport);
					String sqlstr = "UPDATE T_INS_LISTEN_STATUS VALUES SET PORT_STATUS='"+netstat_port+"',RMIPORT_STATUS='"+netstat_rmiport+"',RMIJNDIPORT_STATUS='"+netstat_rmijndiport+"' WHERE MP_ID='"+(String)svcId[i]+"'";
					int rowCount = st.executeUpdate(sqlstr);
					if(rowCount==0){
						sqlstr = "INSERT INTO T_INS_LISTEN_STATUS (MP_ID,PORT_STATUS,RMIPORT_STATUS,RMIJNDIPORT_STATUS) VALUES('"+(String)svcId[i]+"','"+netstat_port+"','"+netstat_rmiport+"','"+netstat_rmijndiport+"')";
						st.executeUpdate(sqlstr);
					}
					
				}
				String ret = base.interaction(cmd_cpu,"GBK");//第二个参数为对方操作系统的语言编码
				double cpu_usage = Double.parseDouble(ret.substring(0,ret.indexOf("%")));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
				String dateTime=sdf.format(new Date());
				if(cpu_usage>C_PER_WARN_VALUE){
					//cpu占比预警
					//TODO 插入预警表
					String sqlstr="INSERT INTO T_CPU_WARN_INFO (SERVER_IP,WT_ID,WARN_DT,WARN_TM,C_SCAN_SPACE,C_PER_WARN_VALUE,C_RISE_RATIO_VALUE,C_RISE_CON,C_PERCENT,C_RISE,HD_TYPE)"
							+ " VALUES ('"+host+"','"+1+"','"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','"+C_SCAN_SPACE+"','"+doubleProcess(C_PER_WARN_VALUE)+"','"+doubleProcess(C_RISE_RATIO_VALUE)+"','"+doubleProcess(C_RISE_CON)+"','"+doubleProcess(cpu_usage)+"','"+0+"','"+0+"')";
					writeLog("host["+host+"]插入预警表。sqlstr["+sqlstr+"]");
					st.executeUpdate(sqlstr);
				}
				String sqlstr = "SELECT CPU_PERCENT FROM T_CPU_INFO WHERE C_INFO_ID =(SELECT MAX(C_INFO_ID) FROM T_CPU_INFO WHERE SERVER_IP='"+host+"')";
				writeLog("host["+host+"]查询上一次的jvm数据。sqlstr["+sqlstr+"]");
				ResultSet rs = st.executeQuery(sqlstr);
				if(rs.next()){
					double old_cpu_usage = Double.parseDouble(rs.getString(1));
					writeLog("host["+host+"]cpu_usage前值["+old_cpu_usage+"]当前值["+cpu_usage+"]上升率预警值["+C_RISE_RATIO_VALUE+"]采集间隔（秒）["+C_SCAN_SPACE+"]上升率判断基数["+C_RISE_CON+"]");
					double rise_ratio = ((cpu_usage-old_cpu_usage)/C_SCAN_SPACE);
					if(cpu_usage>=C_RISE_CON&&rise_ratio>=C_RISE_RATIO_VALUE){
						sqlstr="INSERT INTO T_CPU_WARN_INFO (SERVER_IP,WT_ID,WARN_DT,WARN_TM,C_SCAN_SPACE,C_PER_WARN_VALUE,C_RISE_RATIO_VALUE,C_RISE_CON,C_PERCENT,C_RISE,HD_TYPE)"
								+ " VALUES ('"+host+"','"+2+"','"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','"+C_SCAN_SPACE+"','"+doubleProcess(C_PER_WARN_VALUE)+"','"+doubleProcess(C_RISE_RATIO_VALUE)+"','"+doubleProcess(C_RISE_CON)+"','"+doubleProcess(cpu_usage)+"','"+doubleProcess(rise_ratio)+"','"+0+"')";
						writeLog("host["+host+"]插入预警表。sqlstr["+sqlstr+"]");
						st.executeUpdate(sqlstr);
					}
				}
				sqlstr = "INSERT INTO T_CPU_INFO(SERVER_IP,INFO_DT,INFO_TM,CPU_PERCENT) VALUES ('"+host+"','"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','"+doubleProcess(cpu_usage)+"')";
				writeLog("host["+host+"]插入T_CPU_INF表.sqlstr["+sqlstr+"]");
				st.executeUpdate(sqlstr);
				conn.commit();
				try{rs.close();}catch(Exception e){}
				Date endT = new Date();
			//间隔M_SCAN_SPACE之后继续
				if(endT.getTime()-beginT.getTime()<(C_SCAN_SPACE*1000)){
					long interVal = C_SCAN_SPACE*1000-(endT.getTime()-beginT.getTime());
					try{TimeUnit.MILLISECONDS.sleep(interVal);}catch(Exception e){}
				}
				writeLog("继续下一次采集！===================");
			}
		}catch(Exception e){
			e.printStackTrace();
			try{
				writeLog("错误:"+e.getMessage());
			}catch(Exception e1){
				e1.printStackTrace();
			}
//			retStr = "error:"+e.getMessage();
		}finally{
			try{st.close();}catch(Exception e){}
			try{base.close();}catch(Exception e){}
		}
		return host;
	}
	private String doubleProcess(double d){
		String a3=""+d;
		if(a3.length()-a3.indexOf(".")>2)a3=a3.substring(0, a3.indexOf(".")+3);
		return a3;
	}
	private String getPortStatus(String str){
		str = str.trim();
		return str.substring(str.lastIndexOf(" ")+1);
		
		
	}
}
