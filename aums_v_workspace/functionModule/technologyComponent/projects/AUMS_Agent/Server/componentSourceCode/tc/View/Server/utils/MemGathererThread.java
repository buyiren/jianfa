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

public class MemGathererThread implements Callable<String> {
	private Connection conn;
	private Statement st;
	private String host , account , password ;
	private int M_SCAN_SPACE;
	private double M_PER_WARN_VALUE,M_RISE_RATIO_VALUE,M_RISE_CON,M_PERCENT,M_RISE;
	private final String cmd_mem = "sh hers/herssh_mem";
	
	private void writeLog(String str){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			FileUtils.writeStringToFile(new File("./tttt"), sdf.format(new Date())+" 采集MEM ip["+this.host+"]:"+str+"\n", "utf-8", true);
		}catch(Exception e){
		}
	}
	public MemGathererThread(Connection conn,Statement st,String host,String account,String password,int M_SCAN_SPACE,double M_PER_WARN_VALUE,double M_RISE_RATIO_VALUE,double M_RISE_CON,double M_PERCENT,double M_RISE){
		this.host=host;this.conn=conn;this.st=st;this.account=account;this.password=password;
		this.M_SCAN_SPACE=M_SCAN_SPACE;
		this.M_PER_WARN_VALUE=M_PER_WARN_VALUE;this.M_RISE_RATIO_VALUE=M_RISE_RATIO_VALUE;this.M_RISE_CON=M_RISE_CON;this.M_PERCENT=M_PERCENT;this.M_RISE=M_RISE;
		
	}
	//采集一个abs服务器的cpu信息
	public String call(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		SSHClient base = new SSHClient(host);
		Date date_a = new Date();
		base.setAccount(account);
		base.setPassword(password);
//		try{
//		base.setPassword( EncryptUtil.decryptor(password));
//		}catch(Exception e){
//			System.out.println("error:解密失败host["+host+"]passwd["+password+"]");
//			return host;
//		}
		int retryTimes=20;
		while(retryTimes>0){
			boolean isConnect = base.connect();
			if(isConnect){
				break;
			}
			base.closeSession();base.cancelCF();
			try{Thread.sleep(500);}catch(Exception e){}
			retryTimes--;
			writeLog("再次尝试ssh，直到连接成功......");
		}
		Date date_b =new Date();
		writeLog("ssh ["+host+"]连接耗时["+(date_b.getTime()-date_a.getTime())+"]毫秒");
		System.out.println("error:无法连接到服务器["+host+"]");
		if(retryTimes==0)return host;
		String retStr = "";
		try{
//			int times=0;
			while(true){
//				if(times>0)throw new Exception("调试用的，测试退出循环");
//				times++;
				Date beginT = new Date();
	//			AppLogger.info(cmd_one+"=======================");
				String memInfo = base.interaction(cmd_mem,"GBK");//第二个参数为对方操作系统的语言编码
				//返回格式为:"Mem:   7801856k total,  7636532k used,   165324k free,   173656k buffers";
				writeLog("host["+host+"]返回内存信息["+memInfo+"]");
				memInfo=memInfo.replaceAll(" ", "");
				String[] memArray = memInfo.split(",");
				String total = memArray[0].substring(4, memArray[0].indexOf("ktotal"));
				String used = memArray[1].substring(0,memArray[1].indexOf("kused"));
	//			String free = memArray[2].substring(0,memArray[2].indexOf("kfree"));
	//			String buffers = memArray[3].substring(0,memArray[3].indexOf("kbuffers"));
				int mem_total=Integer.parseInt(total);
				int mem_used =Integer.parseInt(used);
	//			int mem_free =Integer.parseInt(free);
	//			int mem_buff =Integer.parseInt(buffers);
				double mem_usage = ((double)mem_used)/mem_total*100;
				
				String dateTime=sdf.format(new Date());
				if(mem_usage>M_PER_WARN_VALUE){
					//cpu占比预警
					//TODO 插入预警表
					String sqlstr="INSERT INTO T_MEM_WARN_INFO (SERVER_IP,WT_ID,WARN_DT,WARN_TM,M_SCAN_SPACE,M_PER_WARN_VALUE,M_RISE_RATIO_VALUE,M_RISE_CON,M_PERCENT,M_RISE,HD_TYPE)"
							+ " VALUES ('"+host+"','"+3+"','"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','"+M_SCAN_SPACE+"','"+doubleProcess(M_PER_WARN_VALUE)+"','"+doubleProcess(M_RISE_RATIO_VALUE)+"','"+doubleProcess(M_RISE_CON)+"','"+doubleProcess(mem_usage)+"','"+0+"','"+0+"')";
					writeLog("host["+host+"]插入预警表。sqlstr["+sqlstr+"]");
					st.executeUpdate(sqlstr);
				}
				String sqlstr = "SELECT MEM_PERCENT FROM T_MEM_INFO WHERE M_INFO_ID =(SELECT MAX(M_INFO_ID) FROM T_MEM_INFO WHERE SERVER_IP='"+host+"')";
				writeLog("host["+host+"]查询上一次的jvm数据。sqlstr["+sqlstr+"]");
				ResultSet rs = st.executeQuery(sqlstr);
				if(rs.next()){
					double old_mem_usage = Double.parseDouble(rs.getString(1));
					writeLog("host["+host+"]mem_usage前值["+old_mem_usage+"]当前值["+mem_usage+"]上升率预警值["+M_RISE_RATIO_VALUE+"]采集间隔（秒）["+M_SCAN_SPACE+"]上升率判断基数["+M_RISE_CON+"]");
					writeLog("host["+host+"]上升率rise_ratio=(("+mem_usage+"-"+old_mem_usage+")/"+M_SCAN_SPACE+")");
					double rise_ratio = ((mem_usage-old_mem_usage)/M_SCAN_SPACE);
					if(mem_usage>=M_RISE_CON&&rise_ratio>=M_RISE_RATIO_VALUE){
						sqlstr="INSERT INTO T_MEM_WARN_INFO (SERVER_IP,WT_ID,WARN_DT,WARN_TM,M_SCAN_SPACE,M_PER_WARN_VALUE,M_RISE_RATIO_VALUE,M_RISE_CON,M_PERCENT,M_RISE,HD_TYPE)"
								+ " VALUES ('"+host+"','"+4+"','"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','"+M_SCAN_SPACE+"','"+doubleProcess(M_PER_WARN_VALUE)+"','"+doubleProcess(M_RISE_RATIO_VALUE)+"','"+doubleProcess(M_RISE_CON)+"','"+doubleProcess(mem_usage)+"','"+doubleProcess(rise_ratio)+"','"+0+"')";
						writeLog("host["+host+"]插入预警表。sqlstr["+sqlstr+"]");
						st.executeUpdate(sqlstr);
					}
				}
				sqlstr = "INSERT INTO T_MEM_INFO(SERVER_IP,INFO_DT,INFO_TM,MEM_TOTAL,MEM_USED,MEM_PERCENT) VALUES ('"+host+"','"+dateTime.substring(0, 10)+"','"+dateTime.substring(10)+"','"+mem_total+"','"+mem_used+"','"+doubleProcess(mem_usage)+"')";
				writeLog("host["+host+"]插入T_MEM_INF表.sqlstr["+sqlstr+"]");
				st.executeUpdate(sqlstr);
				conn.commit();
				try{rs.close();}catch(Exception e){}
				Date endT = new Date();
			//间隔M_SCAN_SPACE之后继续
				if(endT.getTime()-beginT.getTime()<(M_SCAN_SPACE*1000)){
					long interVal = M_SCAN_SPACE*1000-(endT.getTime()-beginT.getTime());
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
			retStr = "error:"+e.getMessage();
		}finally{
			try{st.close();}catch(Exception e){}
			try{base.close();}catch(Exception e){}
		}
		return host;
	}
	private String doubleProcess(double d){
		if(d<0.001&&d>0.0005)return "0.001";
		else if(d<=0.0005)return "0.000";
		String a3=""+d;
		if(a3.length()-a3.indexOf(".")>3)a3=a3.substring(0, a3.indexOf(".")+4);
		return a3;
	}
}
