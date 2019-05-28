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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
public class HersSendMsgLogThread implements Callable<String> {
	private String host , account , password ,svcId;
	private final String cmd_wc = "sh hers/herssh_wc abs/log/abserver.log";
	private final String cmd_date = "sh hers/herssh_curDate";
	private final String cmd_sed_grep="sh hers/herssh_sed_grep \"410000,490000p\" abs/log/teller/20170615/zone_other/branch_other/ TUSER74100103";
	

	private Connection conn;
	private Statement st;
	private long startLine;
	private String path;
	private String oldDate;
	private String svcName;
	private int msg_scan_space;
	private ArrayList<String> frameTradeList;
	public HersSendMsgLogThread (String host,String account,String password,String svcId,String svcName,String path,Connection conn,Statement st,long startLine,String oldDate,ArrayList<String> frameTradeList,int msg_scan_space){
		this.host=host;this.account=account;this.password=password;this.svcId=svcId;this.path=path;
		this.conn=conn;this.st=st;this.startLine=startLine;this.oldDate=oldDate;this.svcName=svcName;
		this.frameTradeList=frameTradeList;this.msg_scan_space=msg_scan_space;
	}
	private void writeLog(String str){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			FileUtils.writeStringToFile(new File("./tttt"), sdf.format(new Date())+" 采集消息日志:"+str+"\n", "utf-8", true);
		}catch(Exception e){
		}
	}
	private String convertDateFormat(String[] curDateArray){
		String month = "01";
		if(curDateArray[1].equals("一月")){month="01";}
		else if(curDateArray[1].equals("二月")){month="02";}
		else if(curDateArray[1].equals("三月")){month="03";}
		else if(curDateArray[1].equals("四月")){month="04";}
		else if(curDateArray[1].equals("五月")){month="05";}
		else if(curDateArray[1].equals("六月")){month="06";}
		else if(curDateArray[1].equals("七月")){month="07";}
		else if(curDateArray[1].equals("八月")){month="08";}
		else if(curDateArray[1].equals("九月")){month="09";}
		else if(curDateArray[1].equals("十月")){month="10";}
		else if(curDateArray[1].equals("十一月")){month="11";}
		else if(curDateArray[1].equals("十二月")){month="12";}
		return curDateArray[2]+"-"+month+"-"+curDateArray[0];
	}
	/*采集某一个实例的absserver.log，将最新内容解析出来并记录数据库*/
	public String call(){
//		AppLogger.info("AbserverLogThread started!");
		SSHClient base = new SSHClient(host);
		base.setAccount(account);
		base.setPassword(password);
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
		if(retryTimes==0){
			System.out.println("error:无法连接到服务器["+host+"]");
			writeLog("error:无法连接到服务器["+host+"]");
			return "hersSendMsg_"+svcId+"_"+oldDate+"_"+startLine;
		}
		String retStr = "";
//		int i=0;
//		while(true){
		String curDate=oldDate;
		long lineCount=startLine;
			try{
//				int times=0;
				while(true){
//					if(times>0)throw new Exception("调试用的，测试退出循环");//仅用于测试退出时的处理
//					times++;
					Date beginT = new Date();
	//				AppLogger.info(cmd_one+"=======================");
					String ret = base.interaction(cmd_date,"GBK");//第二个参数为对方操作系统的语言编码
	//				AppLogger.info(ret);
					curDate=ret.substring(0, ret.indexOf("\n"));
					writeLog("当前日期["+curDate+"]");
					if(!curDate.equals(oldDate)){//日期有变化，从1开始！
						startLine=1;
						oldDate=curDate;//记下本次采集时的服务器时间，用于下次采集时进行比较
					}
					ret = base.interaction("sh hers/herssh_wc "+path+"hersSendMsg.log","GBK");//第二个参数为对方操作系统的语言编码
	//				AppLogger.info(ret);
					writeLog(ret);
					lineCount=Long.parseLong(ret.substring(0,ret.indexOf(" ")));
					writeLog("读出来的行数是["+lineCount+"]");
					if(lineCount>startLine){
						//文件有变化才进行采集
						String cmd="sh hers/herssh_sed \""+startLine+","+lineCount+"p\" "+path+" hersSendMsg.log";
						writeLog("下一条指令是["+cmd+"]");
						ret = base.interaction(cmd,"GBK");
						writeLog("读出来的内容是["+ret+"]");
						String[] lines = ret.split("\n");
						Pattern p= Pattern.compile("\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3}");
						Pattern p_time = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
						for(int i=0;i<lines.length;i++){
							Matcher m = p.matcher(lines[i]);
							if(m.find()&&lines[i].indexOf(m.group(0))==0){
								//这就是一条消息的开始
								System.out.println("这是一条消息的开始......line:"+lines[i]);
								String[] curDateArray = lines[i].split(" ");
								String curDate1=convertDateFormat(curDateArray);
								if(!curDate1.equals(curDate)){
									/**********************日期已经变化，但文件没有切换，此种情况不采集***********************************/							
//									base.close();
//									if(st!=null){try{st.close();}catch(Exception e){}}
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									writeLog("host["+host+"]日志里面的日期["+curDate1+"]abs服务器日期["+curDate+"]"+"=======================日期不对");
									return "hersSendMsg_"+svcId+"_"+curDate+"_"+1;
								}
								Matcher m2 = p_time.matcher(lines[i]);
								boolean findFlag = m2.find();
								System.out.println("找到时间了:"+findFlag);
								String msgTime = m2.group(0);
								i=i+1;
								String sUSERID=lines[i++];if(sUSERID.indexOf(":")+1<sUSERID.length())sUSERID=sUSERID.substring(sUSERID.indexOf(":")+1);else sUSERID="*";
								String sORGID = lines[i++];if(sORGID.indexOf(":")+1<sORGID.length())sORGID=sORGID.substring(sORGID.indexOf(":")+1);else sORGID="*";
								String sABCIP = lines[i++];if(sABCIP.indexOf(":")+1<sABCIP.length())sABCIP=sABCIP.substring(sABCIP.indexOf(":")+1);else sABCIP="*";
								String sSOID = lines[i++];if(sSOID.indexOf(":")+1<sSOID.length())sSOID=sSOID.substring(sSOID.indexOf(":")+1);else sSOID="*";
								String sTOID = lines[i++];if(sTOID.indexOf(":")+1<sTOID.length())sTOID=sTOID.substring(sTOID.indexOf(":")+1);else sTOID="*";
								String sNAME = lines[i++];if(sNAME.indexOf(":")+1<sNAME.length())sNAME=sNAME.substring(sNAME.indexOf(":")+1);else sNAME="*";//通讯单元
								String sSTRADE=lines[i++];if(sSTRADE.indexOf(":")+1<sSTRADE.length())sSTRADE=sSTRADE.substring(sSTRADE.indexOf(":")+1);else sSTRADE="*";//发起方交易码
								String sTTRADE=lines[i++];if(sTTRADE.indexOf(":")+1<sTTRADE.length())sTTRADE=sTTRADE.substring(sTTRADE.indexOf(":")+1);else sTTRADE="*";//接收方交易码
								String sAPP = lines[i++];if(sAPP.indexOf(":")+1<sAPP.length())sAPP=sAPP.substring(sAPP.indexOf(":")+1);else sAPP="*";//接收方交易的分支
								writeLog("msgTime["+msgTime+"],sUSERID["+sUSERID+"],sORGID["+sORGID+"],sABCIP["+sABCIP+"],sSOID["+sSOID+"],sTOID["+sTOID+"],sNAME["+sNAME+"],sSTRADE["+sSTRADE+"],sTTRADE["+sTTRADE+"],sAPP["+sAPP+"]");
								StringBuffer sCONTENT = new StringBuffer(lines[i++]);
								while(i<lines.length){
									String newLine = lines[i++];
									m = p.matcher(newLine);
									if(m.find()&&newLine.indexOf(m.group(0))==0){
										//另起的消息日志
										i--;
										break;
									}else{
										sCONTENT.append("\n");
										sCONTENT.append(newLine);
									}
								}
								byte[] content = sCONTENT.toString().substring(8).replaceAll("'","''").getBytes();
								byte[] newContent = null;
								if(content.length>2000){
									newContent = new byte[2000];
									System.arraycopy(content, 0, newContent, 0, 1997);
									System.arraycopy("...".getBytes(), 0, newContent, 1997, 3);
								}else{
									newContent = content;
								}
								int msgType=1;//默认为单点。1-单点，0-广播
								if(sTOID.endsWith(".*"))msgType=0;
								int ISFRAME=0;
								for(int ii=0;ii<frameTradeList.size();ii++){
									if(sTTRADE.equals(frameTradeList.get(ii)))ISFRAME=1;
								}
								try {
									SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
									msgTime = sdf.format(new Date());//为了匹配afa服务器时间，这里的msgTime取本地时间
									String sqlstr="INSERT INTO T_ABMSG_INFO (SERVER_IP, ABS_NAME, MSG_TYPE, MSG_DT, MSG_TM, SEND_BRNO,SEND_TELLER, SEND_TRADE, IS_FRAME, SEND_ABC_OID, RE_ABC_OID, MSG_INFO) "
											+ "VALUES ('"+host+"', '"+svcName+"', '"+msgType+"', '"+curDate+"', '"+msgTime+"', '"+sORGID+"','"+sUSERID+"', '"+sSTRADE+"', '"+ISFRAME+"','"+sSOID+"','"+sTOID+"','"+new String(newContent)+"')";
									writeLog(sqlstr);
									st.execute(sqlstr);
									writeLog("提交");
									conn.commit();
									writeLog("数据库结束");
								} catch(Exception e1){
									e1.printStackTrace();
									writeLog("线程处理失败["+e1.getMessage()+"]");
									throw e1;
								}
								writeLog("sCONTENT["+sCONTENT+"]");
								
							}
						}
//						if(st!=null){try{st.close();}catch(Exception e){}}
						writeLog("处理结束");
						startLine = lineCount;
					}
					
					/****************最后，将数据插入数据库*********************/
//					retStr = "hersSendMsg_"+svcId+"_"+curDate+"_startLine_"+lineCount;
					Date endT = new Date();
					//间隔M_SCAN_SPACE之后继续
					if(endT.getTime()-beginT.getTime()<(msg_scan_space*1000)){
						long interVal = msg_scan_space*1000-(endT.getTime()-beginT.getTime());
						try{TimeUnit.MILLISECONDS.sleep(interVal);}catch(Exception e){}
					}
					writeLog("继续下一次采集！===================");
				}
			}catch(Exception e){
				e.printStackTrace();
				try{
					writeLog("svc["+svcId+"]错误:"+e.getMessage());
				}catch(Exception e1){
					e1.printStackTrace();
				}
//				retStr = "error:"+e.getMessage();
			}finally{
				try{base.close();}catch(Exception e){}
				if(st!=null){try{st.close();}catch(Exception e){}}
			}
		return "hersSendMsg_"+svcId+"_"+curDate+"_"+startLine;
	}
}
