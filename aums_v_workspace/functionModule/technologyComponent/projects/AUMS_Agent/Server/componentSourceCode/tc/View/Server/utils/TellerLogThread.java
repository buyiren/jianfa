package tc.View.Server.utils;
import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.jcomponent.CacheOperationImpl.CacheOperationException;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharSet;

import tc.View.Server.utils.Md5CaculateUtil;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public class TellerLogThread implements Callable<String> {
	private final String cmd_wc = "sh hers/herssh_wc abs/log/abserver.log";
	private final String cmd_date = "sh hers/herssh_curDate";
	private final String cmd_sed_grep="sh hers/herssh_sed_grep \"410000,490000p\" abs/log/teller/20170615/zone_other/branch_other/ TUSER74100103";
	
	private String host , account , password ,svcId;
	private Connection conn;
	private Statement st;
//	private long defaultStartLine=-1;
	private String path;
	private String oldDate;
	private String svcName;
	private int log_scan_space;
	private String tellerStartLineJSON;
	private String err_key;
	private ArrayList<String> frameTradeList;
	//host, account,password, svcId, svcName, path, conn, st, curDate,(String)tellerStartLineJSON,log_scan_space,err_key
	public TellerLogThread (String host,String account,String password,String svcId,String svcName,String path,Connection conn,Statement st,
			String oldDate,String tellerStartLineJSON,int log_scan_space,String err_key){
		this.host=host;this.account=account;this.password=password;this.svcId=svcId;this.svcName=svcName;this.path=path;this.conn=conn;this.st=st;
		this.oldDate=oldDate;
		this.tellerStartLineJSON=tellerStartLineJSON;this.log_scan_space=log_scan_space;this.err_key=err_key;
	}
	private void writeLog(String str){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			FileUtils.writeStringToFile(new File("./tellerLog"), sdf.format(new Date())+" 采集柜员日志svcId["+svcId+"]:"+str+"\n", "utf-8", true);
		}catch(Exception e){
		}
	}
	//27 七月 2017 10:06:23,814
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
		
		return curDateArray[2]+"-"+month+"-"+curDateArray[0]+curDateArray[3].substring(0,8);
	}
	/*采集某一个实例的absserver.log，将最新内容解析出来并记录数据库*/
	public String call(){
		JSONObject defaultRetJson= JSONObject.parseObject(tellerStartLineJSON);
		
//		writeLog("AbserverLogThread started!");
		Date beginT1 = new Date();
		SSHClient base = new SSHClient(host);
		base.setAccount(account);
		base.setPassword(password);
		int retryTimes=20;
		while(retryTimes>0){
//			writeLog("无限次尝试，直到连接成功......");
			boolean isConnect = base.connect();
//			writeLog("connecting..."+isConnect);
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
			return defaultRetJson.toJSONString();//"hersSendMsg_"+svcId+"_"+oldDate+"_"+startLine;
		}
		Date endT1 = new Date();
		writeLog("ssh 连接耗时"+(endT1.getTime()-beginT1.getTime())+"毫秒");
		String retStr = "";
//		int i=0;
//		while(true){
		String curDate=oldDate;
		long lineCount=0;//defaultStartLine;
			try{
//				int times=0;
				while(true){
//					if(times>0)throw new Exception("调试用的，测试退出循环");//仅用于测试退出时的处理
//					times++;
					Date beginT = new Date();
	//				writeLog(cmd_one+"=======================");
					String ret = base.interaction(cmd_date,"GBK");//第二个参数为对方操作系统的语言编码
	//				writeLog(ret);
					curDate=ret.substring(0, ret.indexOf("\n"));
					writeLog("当前日期["+curDate+"]");
					if(!curDate.equals(oldDate)){//日期有变化，从1开始！
//						defaultStartLine=1;//换日了，所有文件都从第一行开始
						defaultRetJson.put("date", curDate);
						defaultRetJson.put("tellerStartLine", new JSONObject());//清空所有文件的起始行记录
						oldDate=curDate;//记下本次采集时的服务器时间，用于下次采集时进行比较
					}
//					String dateInPath = oldDate.substring(0, 4)+oldDate.substring(5, 7)+oldDate.substring(8);
					String dateInPath = oldDate.replaceAll("-", "");
					String cmd="ls "+path+"log/teller/"+dateInPath+"/zone_other/branch_other/";
					writeLog("下一条指令是["+cmd+"]");
					ret = base.interaction(cmd,"GBK");//列出当天所有的日志文件名
					writeLog("读出来的文件清单是["+ret+"]");
					String[] tellerFileNames= ret.split("\n");
					for(int i=0;i<tellerFileNames.length;i++){
						if(tellerFileNames[i].equals(""))continue;
						JSONObject tellerStartLine = defaultRetJson.getJSONObject("tellerStartLine");
						//循环处理每一个文件
						String tmpFileName = tellerFileNames[i];
						cmd = "sh hers/herssh_wc "+path+"log/teller/"+dateInPath+"/zone_other/branch_other/"+tmpFileName;
						writeLog("下一条指令是["+cmd+"]");
						ret = base.interaction(cmd,"GBK");//第二个参数为对方操作系统的语言编码
						lineCount=Long.parseLong(ret.substring(0,ret.indexOf(" ")));
						writeLog("读出来的行数是["+lineCount+"]");
						long startLine = 1;
						if(tellerStartLine.containsKey(tmpFileName)){//存在该文件的当前行号，根据该行号读取
							startLine = Long.parseLong(tellerStartLine.getString(tmpFileName));
						}
						
						if(startLine>lineCount){
							writeLog("文件内容无变化。起始行["+startLine+"]总行数["+lineCount+"]，本次跳过本次扫描");
							continue;
						}
						if((lineCount-startLine)>2000)lineCount=startLine+2000;//每次最多读取2000行，以避免内存溢出
						cmd="sh hers/herssh_sed \""+startLine+","+lineCount+"p\" "+path+"log/teller/"+dateInPath+"/zone_other/branch_other/ "+tmpFileName;
						writeLog("下一条指令是["+cmd+"]");
						ret = base.interaction(cmd,"GBK");
//						writeLog("读出来的内容是["+ret+"]");
						String[] lines = ret.split("\n");
						processErrInfo(tmpFileName,startLine,lines);//处理异常信息
						//最后，替换该文件在json中的起始行号
						tellerStartLine.put(tmpFileName, ""+(lineCount+1));
						defaultRetJson.put("tellerStartLine", tellerStartLine);
						//替换起始行号结束
					}
					//defaultStartLine=-1;//最后，将defaultStartLine恢复成-1，下次循环重新判断“日期是否发生变化”
					
					
//					retStr = "hersSendMsg_"+svcId+"_"+curDate+"_startLine_"+lineCount;
					Date endT = new Date();
					//间隔M_SCAN_SPACE之后继续
					if(endT.getTime()-beginT.getTime()<(log_scan_space*1000)){
						long interVal = log_scan_space*1000-(endT.getTime()-beginT.getTime());
						try{TimeUnit.MILLISECONDS.sleep(interVal);}catch(Exception e){}
					}
					writeLog("继续下一次采集！===================");
				}
			}catch(Exception e){
				e.printStackTrace();
				writeLog("svc["+svcId+"]错误:"+e.getMessage());
				
//				retStr = "error:"+e.getMessage();
			}finally{
				try{base.close();}catch(Exception e){}
				if(st!=null){try{st.close();}catch(Exception e){}}
			}
			return defaultRetJson.toJSONString();
	}
	private void processErrInfo(String tmpFileName,long startLine,String[] lines) throws Exception{
//		String[] keyword = this.err_key.split("\\|");
//		StringBuffer rows = new StringBuffer("");
		long lineNumber = -1;
//		String line="";
		for (int ii=0;ii<lines.length;ii++) {
			lineNumber++;
			lines[ii]=(lineNumber + 1) + ":" +lines[ii];
//			rows.append((lineNumber + 1) + ":" + line + "\n");
//			line = lines[ii];
//			// writeLog("line:"+lineNumber+",content:"+line);
//			if (line.indexOf("Exception") >= 0
//					|| line.trim().startsWith("at ")
//					|| line.indexOf(" ERROR ") >= 0
//					|| line.indexOf(" FATAL ") >= 0) {
//				rows.append((lineNumber + 1) + ":" + line + "\n");
//				continue;
//			}
//			// 按策略要求进一步查找
//			for (int i = 0; i < keyword.length; i++) {
//				if (line.indexOf(keyword[i]) >= 0) {
//					rows.append((lineNumber + 1) + ":" + line + "\n");
//					break;
//				}
//			}
		}
//		String allExceptions = rows.toString();
		writeLog("采集到的内容:[" + lines.toString()+"]");
		JavaList errInfoList = new JavaList();
//		JavaList groupLineNoList = new JavaList();
		JavaList dateTimeList = new JavaList();
		// 下面对取出来的rows进行分组，以"\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3} (ERROR|FATAL) Acceptor\\d{1,5}.Processor\\d{1,5}.Worker\\d{1,5} "作为一个分组的开始
		if (lines.length == 0) {
			return;
		}
		String pattern1     = "\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3}";
		Pattern p1 = Pattern.compile(pattern1);
		String pattern2 = " Acceptor\\d{1,5}.Processor\\d{1,5}.Worker\\d{1,5} ";
		Pattern p2 = Pattern.compile(pattern2);
		String pattern3 = "nn 某月 20nn nn:nn:nn,nnn (ERROR|FATAL) \\w+ \\d{13}\\|\\d{1,8}";
		Pattern p3 = Pattern.compile(pattern3);
		String headPattern1 = "\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3} (ERROR|FATAL) Acceptor\\d{1,5}.Processor\\d{1,5}.Worker\\d{1,5} ";
		String headPattern2 = "\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3} (ERROR|FATAL) \\w+ \\d{13}\\|\\d{1,8} ";
		Pattern headP1 = Pattern.compile(headPattern1);
		Pattern headP2 = Pattern.compile(headPattern2);
		// 找到了异常信息
//		String[] lines = allExceptions.split("\n");
//		long preLineNum = 0;
		StringBuffer errInfo = new StringBuffer();
		String groupLineNo = "";
		String srcDateTime ="";
		for (int i = 0; i < lines.length; i++) {
			Matcher m = headP1.matcher(lines[i]);
			Matcher m2 = headP2.matcher(lines[i]);
			if(m.find()||m2.find()){
				// 匹配上log4j的格式，另起一组
//				writeLog("当前组内容:" + errInfo.toString());
//				if(errInfo.toString().length()>0)errInfoList.add(errInfo.toString());
				errInfo = new StringBuffer();
//				groupLineNo = lines[i].substring(0,
//						lines[i].indexOf(":"));
				writeLog("当前组所在行:" + groupLineNo);
//				groupLineNoList.add(groupLineNo);
				m = p1.matcher(lines[i]);
				if (m.find()) {
					writeLog("find[" + m.group(0) + "]");
					srcDateTime = convertDateFormat(m.group(0).split(" "));
					dateTimeList.add(srcDateTime);
					writeLog("替换src[" + lines[i] + "],by ["
							+ pattern1 + "]");
					lines[i] = lines[i].replaceFirst(pattern1,
							"nn 某月 20nn nn:nn:nn,nnn");
				}
				m = p3.matcher(lines[i]);
				if (m.find()){
					writeLog("find[" + m.group(0) + "]");
					writeLog("替换src[" + lines[i] + "],by ["
							+ pattern3 + "]");
					lines[i] = lines[i].replaceFirst(pattern3, 
							"nn 某月 20nn nn:nn:nn,nnn "+lines[i].split(" ")[4]+" TELLERNO 9999999999999|9999");
				}
				m = p2.matcher(lines[i]);
				if (m.find()) {
					writeLog("find[" + m.group(0) + "]");
					writeLog("替换src[" + lines[i] + "],by ["
							+ pattern2 + "]");
					lines[i] = lines[i].replaceFirst(pattern2,
							" AcceptorN.ProcessorN.WorkerN ");
				}
				errInfo.append(lines[i]+"\n");
				while(true){
					//这里继续读取后面的行，直到碰到下一个log4j格式
					i++;
					if(i>=lines.length)break;
					m = p1.matcher(lines[i]);
					if(!m.find()){//不匹配log4j格式，说明是普通行,追加
						lines[i] = lines[i].substring(lines[i].indexOf(":") + 1);
						errInfo.append(lines[i] + "\n");
						continue;
					}else{
						writeLog("当前组内容:" + errInfo.toString());
						errInfoList.add(errInfo.toString());
						//跳过后面所有文件内容，直到再次碰到异常的log4j开始标志
						while(true){
							m = headP1.matcher(lines[i]);
							m2 = headP2.matcher(lines[i]);
							if(!(m.find()||m2.find())){
								i++;
								if(i>=lines.length)break;
								continue;
							}
							i--;
							break;
						}
						break;
					}
					
				}
			}
//			// 替换
//			// 去掉行号
//			lines[i] = lines[i].substring(lines[i].indexOf(":") + 1);
//			writeLog("append[" + lines[i] + "]");
//			errInfo.append(lines[i] + "\n");
//			preLineNum = tmpLineNum;
		}
//		if (errInfo.length() > 0) {
//			writeLog("当前组内容:" + errInfo.toString());
//			errInfoList.add(errInfo.toString());
//		}
//		writeLog("groupLineNoList size:" + groupLineNoList.size());
		writeLog( "errInfoList size:" + errInfoList.size());
		writeLog( "dateTimeList size:" + dateTimeList.size());
		// 分组完成，下面开始向hers服务端上报异常
		// TODO
		JavaList md5List = new JavaList();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String date=sdf1.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		String time = sdf2.format(new Date());
		for (int i = 0; i < errInfoList.size(); i++) {
			
//			try{md5List.add());}catch(Exception e){}
			//这里插入数据库
			try {
				String errInfo1 = errInfoList.getStringItem(i);
				String sLine = errInfo1.substring(0,errInfo1.indexOf(":"));
				errInfo1 = errInfo1.substring(sLine.length()+1);
				long lineNo = startLine + Long.parseLong(sLine)-1;
				String md5 = Md5CaculateUtil.getHash(errInfo1);
				
				String sqlstr="INSERT INTO T_TELLERLOG_INFO (MP_ID, TELLER_ID, ERR_DT, ERR_TM, LOG_LINE_NUM,ERR_MSG_MD5) "
						+ "VALUES ('"+svcId+"', '"+tmpFileName+"', '"+dateTimeList.getStringItem(i).substring(0, 10)+"', '"+dateTimeList.getStringItem(i).substring(10)+"',"+lineNo+", '"+md5+"')";
				writeLog(sqlstr);
				st.execute(sqlstr);
				writeLog("提交");
				conn.commit();
				FileUtils.writeStringToFile(new File("./errdata/"+md5), errInfo1,"UTF-8");
				writeLog("数据库结束");
			} catch(Exception e1){
				e1.printStackTrace();
				writeLog("线程处理失败["+e1.getMessage()+"]");
				throw e1;
			}
		}
		
	}
}
