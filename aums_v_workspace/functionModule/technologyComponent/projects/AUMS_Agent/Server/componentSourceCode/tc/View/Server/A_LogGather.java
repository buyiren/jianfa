package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import tc.View.Server.utils.AbserverLogThread;
import tc.View.Server.utils.EncryptUtil;
import tc.View.Server.utils.HersSendMsgLogThread;
import tc.View.Server.utils.TellerLogThread;
import tc.View.Server.utils.CpuGathererThread;
import cn.com.agree.afa.jcomponent.CacheOperationImpl;
import cn.com.agree.afa.jcomponent.CacheOperationImpl.CacheOperationException;
import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import tc.View.Server.utils.MemGathererThread;

/**
 * 采集日志组件
 * 
 * @date 2017-06-21 18:2:30
 */
@ComponentGroup(level = "应用", groupName = "日志采集组件", projectName = "AAAA", appName = "server")
public class A_LogGather {

	/**
	 * @category 采集abserver.log
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "采集abserver.log", style = "判断型", type = "同步组件", comment = "ccc", author = "Anonymous", date = "2017-06-21 06:17:01")
	public static TCResult A_absRuntime(/* String param1, JavaDict param2 */) {
		// /////////查询实例信息开始
		int JVM_SCAN_SPACE = 0;
		String JVM_WARN_VALUE = null;
		double JVM_RISE_RATIO_VALU = 0;
		double JVM_RISE_CON = 0;
		String DUMP_PATH = null;
		ArrayList<HashMap<String, String>> svcInfo = new ArrayList<HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> machineInfo = new HashMap<String, HashMap<String, String>>();
		Connection myconn = null;
		Statement stmt = null;
		try {
			myconn = DBConnProvider.getConnection("db2");
			stmt = myconn.createStatement();
			String sqlStr = "select JVM_SCAN_SPACE,JVM_WARN_VALUE,JVM_RISE_RATIO_VALU,JVM_RISE_CON,DUMP_PATH from T_JVM_CONF where 1=1";
			ResultSet rs3 = stmt.executeQuery(sqlStr);
			while (rs3.next()) {
				try {
					JVM_SCAN_SPACE = Integer.parseInt(rs3.getString(1));
					JVM_WARN_VALUE = rs3.getString(2);
					JVM_RISE_RATIO_VALU = Double.parseDouble(rs3.getString(3));
					JVM_RISE_CON = Double.parseDouble(rs3.getString(4));
					DUMP_PATH = rs3.getString(5);
				} catch (Exception e) {
					AppLogger.info("读取jvm预警配置出错。" + e.getMessage());
					throw e;
				}

			}
			try {
				rs3.close();
			} catch (Exception e) {
			}
			sqlStr = "select SERVER_IP,PARTITION_NO,INS_PATH,RMIPORT,RMIJNDIPORT from T_INS_TABLE_MAPPING where ISENABLED='1'";
			ResultSet rs1 = stmt.executeQuery(sqlStr);
			while (rs1.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("SERVER_IP", rs1.getString(1));
				tmpMap.put("PARTITION_INDEX", rs1.getString(2));
				tmpMap.put("INS_PATH", rs1.getString(3));
				tmpMap.put("RMIPORT", rs1.getString(4));
				tmpMap.put("RMIJNDIPORT", rs1.getString(5));
				svcInfo.add(tmpMap);
			}
			try {
				rs1.close();
			} catch (Exception e) {
			}
			sqlStr = "select SERVER_IP,LOGIN_NAME,LOGIN_PASSWD from T_MACHINE_INFO";
			ResultSet rs2 = stmt.executeQuery(sqlStr);
			while (rs2.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("LOGIN_NAME", rs2.getString(2));
				tmpMap.put("LOGIN_PASSWD", rs2.getString(3));
				machineInfo.put(rs2.getString(1), tmpMap);
			}
			try {
				rs2.close();
			} catch (Exception e) {
			}

			try {
				stmt.close();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.info("处理失败。" + e.getMessage());
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
		// /////////查询实例信息完成
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		// HashMap<String,String> svc_date =new HashMap<String,String>();
		AppLogger.info("svcInfo.size():" + svcInfo.size());
		for (int i = 0; i < svcInfo.size(); i++) {
			String host = svcInfo.get(i).get("SERVER_IP"), svcId = svcInfo.get(
					i).get("PARTITION_INDEX"), path = svcInfo.get(i).get(
					"INS_PATH")
					+ "log/";
			String account = machineInfo.get(host).get("LOGIN_NAME");
			int rmiPort = Integer.parseInt(svcInfo.get(i).get("RMIPORT")), rmiJndiPort = Integer
					.parseInt(svcInfo.get(i).get("RMIJNDIPORT"));
			AppLogger.info("采集，server_ip[" + host + "]，PARTITION_INDEX["
					+ svcId + "],path[" + path + "],account[" + account + "]");
			String password = "";
			AppLogger.info("服务器信息。ip[" + host + "],account[" + account
					+ "],password[" + password + "]");

			Connection conn = null;
			Statement st = null;
			try {
				conn = DBConnProvider.getConnection("db2");
				st = conn.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
				AppLogger.info("数据库操作失败" + e.getMessage());
			}
			results.add(exec.submit(new AbserverLogThread(host, account,
					password, svcId, path, conn, st, rmiPort, rmiJndiPort,
					60000, JVM_SCAN_SPACE, JVM_WARN_VALUE, JVM_RISE_RATIO_VALU,
					JVM_RISE_CON, DUMP_PATH)));
		}
		AppLogger.info("results size():" + results.size());
		boolean findFlag = true;// 是否找到了尚未结束的线程，true：找到了，false：没找到。初始时，假定能找到，进入循环
		while (findFlag) {
			// 循环等待全部线程处理完毕
			findFlag = false;// 是否找到了尚未结束的线程，true：找到了，false：没找到
			for (int j = 0; j < results.size(); j++) {
				Future<String> fs = results.get(j);
				if (!fs.isDone()) {
					findFlag = true;
					break;
				}
			}
			if (!findFlag)
				break;
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		AppLogger.info("关闭线程池");
		exec.shutdown();
		AppLogger.info("关闭线程池ok");
		return TCResult.newSuccessResult();
	}

	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "采集hersSendMsg.log", style = "判断型", type = "同步组件", comment = "采集abs集群的hersSendMsg.log", author = "Anonymous", date = "2017-06-21 06:17:01")
	public static TCResult A_hersSendMsg() {
		String serviceIdentifier = "redis";
		String cacheName = "cache";
		int msg_scan_space=60;//采集间隔
		// /////////查询实例信息开始
		ArrayList<HashMap<String, String>> svcInfo = new ArrayList<HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> machineInfo = new HashMap<String, HashMap<String, String>>();
		ArrayList<String> frameTradeList = new ArrayList<String>();
		Connection myconn = null;
		Statement stmt = null;
		try {
			myconn = DBConnProvider.getConnection("db2");
			stmt = myconn.createStatement();
			String sqlStr = "select SERVER_IP,PARTITION_NO,INS_PATH,ABS_NAME from T_INS_TABLE_MAPPING where ISENABLED='1'";
			ResultSet rs1 = stmt.executeQuery(sqlStr);
			while (rs1.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("SERVER_IP", rs1.getString(1));
				tmpMap.put("PARTITION_INDEX", rs1.getString(2));
				tmpMap.put("INS_PATH", rs1.getString(3));
				tmpMap.put("ABS_NAME", rs1.getString(4));
				svcInfo.add(tmpMap);
			}
			try {
				rs1.close();
			} catch (Exception e) {
			}
			sqlStr = "select SERVER_IP,LOGIN_NAME,LOGIN_PASSWD from T_MACHINE_INFO";
			ResultSet rs2 = stmt.executeQuery(sqlStr);
			while (rs2.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("LOGIN_NAME", rs2.getString(2));
				tmpMap.put("LOGIN_PASSWD", rs2.getString(3));
				machineInfo.put(rs2.getString(1), tmpMap);
			}
			try {
				rs2.close();
			} catch (Exception e) {
			}
			sqlStr = "select FRAME_TRADE_PATH,FRAME_TRADE_PATH1,FRAME_TRADE_PATH2,FRAME_TRADE_PATH3,MSG_SCAN_SPACE from T_ABMSG_CONF";
			ResultSet rs3 = stmt.executeQuery(sqlStr);
			while (rs3.next()) {
				frameTradeList.add(rs3.getString(1));
				if (!(rs3.getString(2) == null || rs3.getString(2).equals("")))
					frameTradeList.add(rs3.getString(2));
				if (!(rs3.getString(3) == null || rs3.getString(3).equals("")))
					frameTradeList.add(rs3.getString(3));
				if (!(rs3.getString(4) == null || rs3.getString(4).equals("")))
					frameTradeList.add(rs3.getString(4));
				msg_scan_space = Integer.parseInt(rs3.getString(5));
			}
			try {
				rs3.close();
			} catch (Exception e) {
			}

			try {
				stmt.close();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.info("数据库操作失败" + e.getMessage());
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
		// /////////查询实例信息完成
		String redisHrefPre = "hersSendMsgGatherHref_";
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		HashMap<String, String> svc_date = new HashMap<String, String>();
		AppLogger.info("svcInfo.size():" + svcInfo.size());
		HashMap<String,String> threadMap = new HashMap<String,String>();
		for (int i = 0; i < svcInfo.size(); i++) {
			String host = svcInfo.get(i).get("SERVER_IP"), svcId = svcInfo.get(
					i).get("PARTITION_INDEX"), svcName = svcInfo.get(i).get(
					"ABS_NAME"), path = svcInfo.get(i).get("INS_PATH") + "log/";
			String account = machineInfo.get(host).get("LOGIN_NAME");
			Object href = CacheOperationImpl.get(serviceIdentifier, cacheName, redisHrefPre+svcId);
			if(href!=null){
				AppLogger.info("svcId["+svcId+"]正在由其他线程采集，本次不做处理");
				continue;
			}else{
				try{
					CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, redisHrefPre+svcId,"");
				}catch(Exception e){
					AppLogger.info("redis写入缓存,key["+redisHrefPre+svcId+"]，失败，本svc["+svcId+"]不作处理");
					continue;
				}
			}
			AppLogger.info("采集，server_ip[" + host + "]，PARTITION_INDEX["
					+ svcId + "],path[" + path + "],account[" + account + "]");
			String password;
			try {
				password = EncryptUtil.decryptor(machineInfo.get(host).get(
						"LOGIN_PASSWD"));
			} catch (Exception e) {
				AppLogger.info("密码解密错误，该服务器将不会被采集任何信息!server_ip:" + host
						+ ",e:" + e.getMessage());
				continue;
			}
			AppLogger.info("服务器信息。ip[" + host + "],account[" + account
					+ "],password[" + password + "]");
			String curDateStr = null;
			Object curDate = CacheOperationImpl.get(serviceIdentifier,
					cacheName, "hersSendMsg_" + svcId + "_curDate");
			if (curDate == null || ((String) curDate).equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
				curDateStr = sdf.format(date);
				try {
					CacheOperationImpl.putAndReplicate(serviceIdentifier,
							cacheName, "hersSendMsg_" + svcId + "_curDate",
							curDateStr);
				} catch (Exception e) {
					AppLogger.info("写入redis失败，key:[" + "hersSendMsg_" + svcId
							+ "_curDate" + "]," + e.getMessage()+",本svc["+svcId+"]不做采集");
					continue;
				}
			} else {
				curDateStr = (String) curDate;
			}
			svc_date.put(svcId, curDateStr);
			long startLine = 1;
			Object startLineObj = CacheOperationImpl.get(serviceIdentifier,
					cacheName, "hersSendMsg_" + svcId + "_" + curDateStr
							+ "_startLine");
			if (startLineObj != null) {
				startLine = Long.parseLong((String) startLineObj);
			}else{
				try{
					CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, "hersSendMsg_" + svcId + "_" + curDateStr
							+ "_startLine", "1");
				}catch(Exception e){}
			}

			Connection conn = null;
			Statement st = null;
			try {
				conn = DBConnProvider.getConnection("db2");
				st = conn.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
				AppLogger.info("数据库操作失败" + e.getMessage()+",本svc["+svcId+"]不做采集");
				continue;
			}
			try{
				CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, redisHrefPre+svcId, "");
				CacheOperationImpl.expire(serviceIdentifier, cacheName, redisHrefPre+svcId, 300);
			}catch(Exception e){
				AppLogger.info("写入redis缓存失败，key["+redisHrefPre+svcId+"],本svc["+svcId+"]不做采集");
				continue;
			}
			threadMap.put(svcId,"");
			results.add(exec.submit(new HersSendMsgLogThread(host, account,
					password, svcId, svcName, path, conn, st, startLine,
					curDateStr, frameTradeList,msg_scan_space)));
		}
		AppLogger.info("results size():" + results.size());
		boolean findFlag = true;// 是否找到了尚未结束的线程，true：找到了，false：没找到。初始时，假定能找到，进入循环
		while (findFlag) {
			// 循环等待全部线程处理完毕
			findFlag = false;// 是否找到了尚未结束的线程，true：找到了，false：没找到
//			ArrayList<String> finishedSvcIdThread= new ArrayList<String>();
			for (int j = 0; j < results.size(); j++) {
				Future<String> fs = results.get(j);
				try {
					// TODO 将返回的当前行数写入redis缓存
					if(!fs.isDone()){
						findFlag = true;
					}else{
						//该线程已结束
						try {
							String ret = fs.get();
							// TODO 将返回的当前行数写入redis缓存
							// 返回值的格式为
							// "abserverLog_"+svcId+"_"+curDate+"_curLine_"+lineCount
							String[] strs = ret.split("_");
							threadMap.remove(strs[1]);
							AppLogger.info("删除svcId["+strs[1]+"]的redisHref");
							CacheOperationImpl.remove(serviceIdentifier, cacheName, redisHrefPre+strs[1]);
							if (!strs[2].equals(((String) svc_date.get(strs[1])))) {
								// 返回的日期和当前日期已经不同，需要更新redis的缓存值
								CacheOperationImpl.putAndReplicate(serviceIdentifier,
										cacheName, "hersSendMsg_" + strs[1]
												+ "_curDate", strs[2]);
							}
							String key = "hersSendMsg_" + strs[1] + "_" + strs[2]
									+ "_startLine";//ret.substring(0, ret.lastIndexOf("_"));
							String value = strs[3];
							AppLogger.info("写入缓存key[" + key + "]serviceIdentifier["
									+ serviceIdentifier + "]cacheName[" + cacheName
									+ "]\n");
							CacheOperationImpl.putAndReplicate(serviceIdentifier,
									cacheName, key, value);
							AppLogger.info("写入缓存ok");
							CacheOperationImpl.expire(serviceIdentifier, cacheName,
									key, 60*60*24);
							AppLogger.info("设置超时ok");
						} catch (CacheOperationException e) {
							e.printStackTrace();
							AppLogger.info("redis操作失败[" + e.getMessage() + "]\n");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			for(int i=0;i<finishedSvcIdThread.size();i++){
//				threadMap.remove(finishedSvcIdThread.get(i));
//			}
			//threadMap中剩下的部分，就是尚未完成的svc线程，下面进行redis expire刷新处理
			Object[] unfinishedSvcId=threadMap.keySet().toArray();
			for(int i=0;i<unfinishedSvcId.length;i++){
				try{
					AppLogger.info("设置有效期为60秒,serviceIdentifier["+serviceIdentifier+"],cacheName["+cacheName+"],key["+redisHrefPre+unfinishedSvcId[i]+"]");
					CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, redisHrefPre+unfinishedSvcId[i],"");
					CacheOperationImpl.expire(serviceIdentifier, cacheName, redisHrefPre+unfinishedSvcId[i], 60);
				}catch(Exception e){
					e.printStackTrace();
					AppLogger.info("更新redis中的redisHrefPre失败，svc["+unfinishedSvcId[i]+"]，"+e.getMessage());
				}
			}
			if (!findFlag)break;
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		AppLogger.info("关闭线程池");
		exec.shutdown();
		AppLogger.info("关闭线程池ok");
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 采集各服务器cpu信息
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "采集各服务器cpu信息", style = "判断型", type = "同步组件", comment = "采集各abs服务器cpu信息，并根据判断条件进行预警", author = "Anonymous", date = "2017-07-07 03:14:24")
	public static TCResult A_cpu_Info() {
		int C_SCAN_SPACE=0;
		double C_PER_WARN_VALUE=0,C_RISE_RATIO_VALUE=0,C_RISE_CON=0;
		
		HashMap<String, HashMap<String, String>> machineInfo = new HashMap<String, HashMap<String, String>>();
		Connection myconn = null;
		Statement stmt = null;
		try {
			myconn = DBConnProvider.getConnection("db2");
			stmt = myconn.createStatement();
			String sqlStr = "SELECT C_SCAN_SPACE,C_PER_WARN_VALUE,C_RISE_RATIO_VALUE,C_RISE_CON FROM T_CPU_CONF where 1=1";
			ResultSet rs3 = stmt.executeQuery(sqlStr);
			while (rs3.next()) {
				try {
					C_SCAN_SPACE = Integer.parseInt(rs3.getString(1));
					C_PER_WARN_VALUE = Double.parseDouble(rs3.getString(2));
					C_RISE_RATIO_VALUE = Double.parseDouble(rs3.getString(3));
					C_RISE_CON = Double.parseDouble(rs3.getString(4));
				} catch (Exception e) {
					AppLogger.info("读取cpu预警配置出错。" + e.getMessage());
					throw e;
				}

			}
			try {rs3.close();} catch (Exception e) {}
			sqlStr = "select SERVER_IP,LOGIN_NAME,LOGIN_PASSWD from T_MACHINE_INFO";
			ResultSet rs2 = stmt.executeQuery(sqlStr);
			while (rs2.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("LOGIN_NAME", rs2.getString(2));
				tmpMap.put("LOGIN_PASSWD",rs2.getString(3));
				machineInfo.put(rs2.getString(1), tmpMap);
			}
			try {rs2.close();} catch (Exception e) {}
			try {stmt.close();} catch (Exception e) {}
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.info("处理失败。" + e.getMessage());
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
		// /////////查询实例信息完成
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		// HashMap<String,String> svc_date =new HashMap<String,String>();
		Object[] hosts = machineInfo.keySet().toArray();
		String redisKeyPre = "hersCpuGatherHref_";
		HashMap<String,String> threadMap = new HashMap<String,String>();
		for (int i = 0; i < hosts.length; i++) {
			String host = (String)hosts[i];
			Object redisHref = CacheOperationImpl.get("redis","cache",redisKeyPre + host);
			if(redisHref!=null){
				AppLogger.info("有采集动作正在执行，忽略本服务器【"+host+"】的处理");
				continue;
			}
			
			String account = machineInfo.get(host).get("LOGIN_NAME");
			String password= machineInfo.get(host).get("LOGIN_PASSWD");
			AppLogger.info("服务器信息。ip[" + host + "],account[" + account
					+ "],password[" + password + "]");
			try{
				password= EncryptUtil.decryptor(password);
			}catch(Exception e){
				AppLogger.error("error:解密失败host["+host+"]passwd["+password+"]");
				e.printStackTrace();
				continue;
			}
			Connection conn = null;
			Statement st = null;
			HashMap<String,HashMap<String,String>> portMap = new HashMap<String,HashMap<String,String>>();
			try {
				conn = DBConnProvider.getConnection("db2");
				st = conn.createStatement();
				String sqlStr = "select MP_ID,PORT,RMIPORT,RMIJNDIPORT FROM T_INS_TABLE_MAPPING WHERE SERVER_IP='"+host+"'";
				ResultSet rs4 = st.executeQuery(sqlStr);
				
				while(rs4.next()){
					String mp_id=rs4.getString(1);
					HashMap tmpMap = new HashMap<String,String>();
					tmpMap.put("port", rs4.getString(2));
					tmpMap.put("rmiport",rs4.getString(3));
					tmpMap.put("rmijndiport", rs4.getString(4));
					portMap.put(mp_id,tmpMap);
				}
				try{rs4.close();}catch(Exception e){}
			} catch (Exception e) {
				e.printStackTrace();
				AppLogger.info("数据库操作失败" + e.getMessage());
			}
			
			
			//Connection conn,Statement st,String host,String account,String password,int C_SCAN_SPACE,double C_PER_WARN_VALUE,double C_RISE_RATIO_VALUE,double C_RISE_CON,double C_PERCENT,double C_RISE
			try {
				CacheOperationImpl.putAndReplicate("redis",
						"cache", redisKeyPre + host,"");
			} catch (Exception e) {
				AppLogger.info("写入redis失败，key:[" +redisKeyPre + host + "]," + e.getMessage());
			}
			threadMap.put(host,"");
			results.add(exec.submit(new CpuGathererThread(conn,st,host,account,password,C_SCAN_SPACE,C_PER_WARN_VALUE,C_RISE_RATIO_VALUE,C_RISE_CON,portMap)));
		}
		AppLogger.info("results size():" + results.size());
		boolean findFlag = true;// 是否找到了尚未结束的线程，true：找到了，false：没找到。初始时，假定能找到，进入循环
//		ArrayList<String> finishedThreadList = new ArrayList<String>();
		while (findFlag) {
			// 循环等待全部线程处理完毕
			findFlag = false;// 是否找到了尚未结束的线程，true：找到了，false：没找到
			for (int j=0;j<results.size();j++){
				Future<String> fs = results.get(j);
				if(fs.isDone()){
					try{
						String ret = fs.get();//返回值为ip（host）
						threadMap.remove(ret);
						AppLogger.info("线程已经结束，返回值["+ret+"]");
						CacheOperationImpl.remove("redis", "cache", redisKeyPre + ret);
					}catch(Exception e){
						e.printStackTrace();
						AppLogger.info("获取线程返回值,同时删除redis缓存，失败:"+e.getMessage());
					}
				}
			}
			for (int j = 0; j < results.size(); j++) {
				Future<String> fs = results.get(j);
				if (!fs.isDone()) {
					findFlag = true;
					break;
				}
			}
//			for(int i=0;i<finishedThreadList.size();i++){
//				threadMap.remove(finishedThreadList.get(i));
//			}
			//threadMap中剩下的部分，就是尚未完成的svc线程，下面进行redis expire刷新处理
			Object[] unfinishedHostId=threadMap.keySet().toArray();
			for(int i=0;i<unfinishedHostId.length;i++){
				try{
					AppLogger.info("设置有效期为60秒,key["+redisKeyPre+unfinishedHostId[i]+"]");
					CacheOperationImpl.expire("redis", "cache", redisKeyPre+unfinishedHostId[i], 60);
				}catch(Exception e){
					AppLogger.info("更新redis中的redisHref失败，host["+unfinishedHostId[i]+"]");
				}
			}
			if (!findFlag)
				break;
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		AppLogger.info("关闭线程池");
		exec.shutdown();
		AppLogger.info("关闭线程池ok");
		return TCResult.newSuccessResult();
	}
	/**
	 * @category 采集各服务器内存信息
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "采集各服务器内存信息", style = "判断型", type = "同步组件", comment = "采集各abs服务器内存信息，并根据判断条件进行预警", author = "Anonymous", date = "2017-07-07 03:14:24")
	public static TCResult A_mem_Info() {
		int M_SCAN_SPACE=0;
		String redisKeyPre="hersMemGatherHref_";
		double M_PER_WARN_VALUE=0,M_RISE_RATIO_VALUE=0,M_RISE_CON=0,M_PERCENT=0,M_RISE=0;
		HashMap<String, HashMap<String, String>> machineInfo = new HashMap<String, HashMap<String, String>>();
		Connection myconn = null;
		Statement stmt = null;
		try {
			myconn = DBConnProvider.getConnection("db2");
			stmt = myconn.createStatement();
			String sqlStr = "SELECT M_SCAN_SPACE,M_PER_WARN_VALUE,M_RISE_RATIO_VALUE,M_RISE_CON FROM T_MEM_CONF where 1=1";
			ResultSet rs3 = stmt.executeQuery(sqlStr);
			while (rs3.next()) {
				try {
					M_SCAN_SPACE = Integer.parseInt(rs3.getString(1));
					M_PER_WARN_VALUE = Double.parseDouble(rs3.getString(2));
					M_RISE_RATIO_VALUE = Double.parseDouble(rs3.getString(3));
					M_RISE_CON = Double.parseDouble(rs3.getString(4));
				} catch (Exception e) {
					AppLogger.info("读取cpu预警配置出错。" + e.getMessage());
					throw e;
				}

			}
			try {rs3.close();} catch (Exception e) {}
			sqlStr = "select SERVER_IP,LOGIN_NAME,LOGIN_PASSWD from T_MACHINE_INFO";
			ResultSet rs2 = stmt.executeQuery(sqlStr);
			while (rs2.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("LOGIN_NAME", rs2.getString(2));
				tmpMap.put("LOGIN_PASSWD",rs2.getString(3));
				machineInfo.put(rs2.getString(1), tmpMap);
			}
			
			try {
				rs2.close();
			} catch (Exception e) {
			}

			try {
				stmt.close();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.info("处理失败。" + e.getMessage());
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
		// /////////查询实例信息完成
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		// HashMap<String,String> svc_date =new HashMap<String,String>();
		Object[] hosts = machineInfo.keySet().toArray();
		HashMap<String,String> threadMap = new HashMap<String,String>();
		for (int i = 0; i < hosts.length; i++) {
			String host = (String)hosts[i];
			Object redisHref = CacheOperationImpl.get("redis","cache",redisKeyPre + host);
			if(redisHref!=null){
				AppLogger.info("有采集动作正在执行，忽略本服务器【"+host+"】的处理");
				continue;
			}
			String account = machineInfo.get(host).get("LOGIN_NAME");
			String password= machineInfo.get(host).get("LOGIN_PASSWD");
			AppLogger.info("服务器信息。ip[" + host + "],account[" + account
					+ "],password[" + password + "]");
			try{
				password= EncryptUtil.decryptor(password);
			}catch(Exception e){
				AppLogger.error("error:解密失败host["+host+"]passwd["+password+"]");
				e.printStackTrace();
				continue;
			}
			Connection conn = null;
			Statement st = null;
			try {
				conn = DBConnProvider.getConnection("db2");
				st = conn.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
				AppLogger.info("数据库操作失败" + e.getMessage());
			}
			//Connection conn,Statement st,String host,String account,String password,int C_SCAN_SPACE,double C_PER_WARN_VALUE,double C_RISE_RATIO_VALUE,double C_RISE_CON,double C_PERCENT,double C_RISE
			try {
				CacheOperationImpl.putAndReplicate("redis",
						"cache", redisKeyPre + host,"");
				CacheOperationImpl.expire("redis", "cache", redisKeyPre+host, 60);
			} catch (Exception e) {
				AppLogger.info("写入redis失败，key:[" +redisKeyPre + host + "]," + e.getMessage());
			}
			threadMap.put(host,"");
			results.add(exec.submit(new MemGathererThread(conn,st,host,account,password,M_SCAN_SPACE,M_PER_WARN_VALUE,M_RISE_RATIO_VALUE,M_RISE_CON,M_PERCENT,M_RISE)));
		}
		if(results.size()==0){
			exec.shutdown();
			return TCResult.newSuccessResult();
		}
		AppLogger.info("results size():" + results.size());
		boolean findFlag = true;// 是否找到了尚未结束的线程，true：找到了，false：没找到。初始时，假定能找到，进入循环
		while (findFlag) {
			// 循环等待全部线程处理完毕
			findFlag = false;// 是否找到了尚未结束的线程，true：找到了，false：没找到
			for (int j=0;j<results.size();j++){
				Future<String> fs = results.get(j);
				if(fs.isDone()){
					try{
						String ret = fs.get();//返回值为ip（host）
						System.out.println("线程已经结束，返回值["+ret+"]");
						AppLogger.info("线程已经结束，返回值["+ret+"]");
						CacheOperationImpl.remove("redis", "cache", redisKeyPre + ret);
					}catch(Exception e){
						e.printStackTrace();
						AppLogger.info("获取线程返回值,同时删除redis缓存，失败:"+e.getMessage());
					}
				}
			}
			for (int j = 0; j < results.size(); j++) {
				Future<String> fs = results.get(j);
				if (!fs.isDone()) {
					findFlag = true;
					break;
				}
			}
			//threadMap中剩下的部分，就是尚未完成的svc线程，下面进行redis expire刷新处理
			Object[] unfinishedHostId=threadMap.keySet().toArray();
			for(int i=0;i<unfinishedHostId.length;i++){
				try{
					AppLogger.info("设置有效期为60秒,key["+redisKeyPre+unfinishedHostId[i]+"]");
					CacheOperationImpl.expire("redis", "cache", redisKeyPre+unfinishedHostId[i], 60);
				}catch(Exception e){
					AppLogger.info("更新redis中的redisHref失败，host["+unfinishedHostId[i]+"]");
				}
			}
			if (!findFlag)
				break;
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		AppLogger.info("关闭线程池");
		exec.shutdown();
		AppLogger.info("关闭线程池ok");
		return TCResult.newSuccessResult();
	}
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "采集柜员日志", style = "判断型", type = "同步组件", comment = "采集abs集群的柜员日志", author = "Anonymous", date = "2017-07-19 10:58:01")
	public static TCResult A_tellerLogGatherer() {
		String serviceIdentifier = "redis";
		String cacheName = "cache";
		int msg_scan_space=60;//采集间隔
		// /////////查询实例信息开始
		ArrayList<HashMap<String, String>> svcInfo = new ArrayList<HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> machineInfo = new HashMap<String, HashMap<String, String>>();
		ArrayList<String> frameTradeList = new ArrayList<String>();
		Connection myconn = null;
		Statement stmt = null;
		int log_scan_space=60;
		String err_key = "";
		try {
			myconn = DBConnProvider.getConnection("db2");
			stmt = myconn.createStatement();
			String sqlStr = "select MP_ID,SERVER_IP,PARTITION_NO,INS_PATH,ABS_NAME from T_INS_TABLE_MAPPING where ISENABLED='1'";
			ResultSet rs1 = stmt.executeQuery(sqlStr);
			while (rs1.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("MP_ID", rs1.getString(1));
				tmpMap.put("SERVER_IP", rs1.getString(2));
				tmpMap.put("PARTITION_INDEX", rs1.getString(3));
				tmpMap.put("INS_PATH", rs1.getString(4));
				tmpMap.put("ABS_NAME", rs1.getString(5));
				svcInfo.add(tmpMap);
			}
			try {
				rs1.close();
			} catch (Exception e) {
			}
			sqlStr = "select SERVER_IP,LOGIN_NAME,LOGIN_PASSWD from T_MACHINE_INFO";
			ResultSet rs2 = stmt.executeQuery(sqlStr);
			while (rs2.next()) {
				HashMap<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("LOGIN_NAME", rs2.getString(2));
				tmpMap.put("LOGIN_PASSWD", rs2.getString(3));
				machineInfo.put(rs2.getString(1), tmpMap);
			}
			try {rs2.close();} catch (Exception e) {}
			sqlStr = "select TELLERLOG_SCAN_SPACE,TELLERLOG_ERR_KEY from T_TELLERLOG_CONF";
			ResultSet rs3 = stmt.executeQuery(sqlStr);
			while (rs3.next()) {
				log_scan_space = Integer.parseInt(rs3.getString(1));
				err_key = rs3.getString(2);
			}
			try {rs3.close();} catch (Exception e) {}
			
			try {stmt.close();} catch (Exception e) {}
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.info("数据库操作失败" + e.getMessage());
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
		}
		// /////////查询实例信息完成
		String redisHrefPre = "hersTellerLogHref_";
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		HashMap<String, String> svc_date = new HashMap<String, String>();
		AppLogger.info("svcInfo.size():" + svcInfo.size());
		HashMap<String,String> threadMap = new HashMap<String,String>();
		for (int i = 0; i < svcInfo.size(); i++) {
			String host = svcInfo.get(i).get("SERVER_IP"), svcId = svcInfo.get(
					i).get("MP_ID"), svcName = svcInfo.get(i).get(
					"ABS_NAME"), path = svcInfo.get(i).get("INS_PATH") ;
			String account = machineInfo.get(host).get("LOGIN_NAME");
			Object href = CacheOperationImpl.get(serviceIdentifier, cacheName, redisHrefPre+svcId);
			if(href!=null){
				AppLogger.info("svcId["+svcId+"]正在由其他线程采集，本次不做处理");
				continue;
			}
			AppLogger.info("采集，server_ip[" + host + "]，svcId["
					+ svcId + "],path[" + path + "],account[" + account + "]");
			String password;
			try {
				password = EncryptUtil.decryptor(machineInfo.get(host).get(
						"LOGIN_PASSWD"));
			} catch (Exception e) {
				AppLogger.info("密码解密错误，该服务器将不会被采集任何信息!server_ip:" + host
						+ ",e:" + e.getMessage());
				continue;
			}
			AppLogger.info("服务器信息。ip[" + host + "],account[" + account
					+ "],password[" + password + "]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String curDate = sdf.format(date);
//			List<String> allTellerKeyList = CacheOperationImpl.keys(serviceIdentifier, cacheName, "hersTellerLog_"+curDate+"_"+svcId+"_"+"_*");
//			AppLogger.info("全部的teller日志的key清单 size["+allTellerKeyList.size()+"]\n下面读取每一个的起始行");
//			//下面集合的结构为：HashMap<tellerId,startLine>
//			HashMap<String,String> allTellerMap = new HashMap<String,String>();
//			for(int ii=0;ii<allTellerKeyList.size();ii++){
//				String startLine = (String)CacheOperationImpl.get(serviceIdentifier, cacheName,allTellerKeyList.get(ii));
//				allTellerMap.put(allTellerKeyList.get(ii).split("_")[3], startLine);
//			}
			//取出上一次记录的起始行号信息，key为"hersTellerLog_"+curDate+"_"+svcId，value为json串
			Object tellerStartLineJSON = CacheOperationImpl.get(serviceIdentifier, cacheName,"hersTellerLog_"+curDate+"_"+svcId);
			if(tellerStartLineJSON==null){
				JSONObject defaultRetJson = new JSONObject();
				defaultRetJson.put("svcId", svcId);
				defaultRetJson.put("date", curDate);
				defaultRetJson.put("tellerStartLine",new JSONObject());
				tellerStartLineJSON=defaultRetJson.toJSONString();
			}
			Connection conn = null;
			Statement st = null;
			try {
				conn = DBConnProvider.getConnection("db2");
				st = conn.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
				AppLogger.info("数据库操作失败" + e.getMessage()+",本svc["+svcId+"]不做采集");
				continue;
			}
			try{
				CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, redisHrefPre+svcId, "");
				CacheOperationImpl.expire(serviceIdentifier, cacheName, redisHrefPre+svcId, 300);
			}catch(Exception e){
				AppLogger.info("写入redis缓存失败，key["+redisHrefPre+svcId+"],本svc["+svcId+"]不做采集");
				continue;
			}
			threadMap.put(svcId,"");
			results.add(exec.submit(new TellerLogThread(host, account,
					password, svcId, svcName, path, conn, st, curDate,(String)tellerStartLineJSON,log_scan_space,err_key)));
		}
		AppLogger.info("results size():" + results.size());
		boolean findFlag = true;// 是否找到了尚未结束的线程，true：找到了，false：没找到。初始时，假定能找到，进入循环
		while (findFlag) {
			// 循环等待全部线程处理完毕
			findFlag = false;// 是否找到了尚未结束的线程，true：找到了，false：没找到
//			ArrayList<String> finishedSvcIdThread= new ArrayList<String>();
			for (int j = 0; j < results.size(); j++) {
				Future<String> fs = results.get(j);
				try {
					// TODO 将返回的当前行数写入redis缓存
					if(!fs.isDone()){
						findFlag = true;
					}else{
						//该线程已结束
						try {
							String ret = fs.get();
							// TODO 将返回的当前行数写入redis缓存
							// 返回值的格式为{"svcId":"1","date":"20170719","tellerStartLine":{"chenmobj1":"1000","wangleibj1":"500"}}
							JSONObject retJson = JSONObject.parseObject(ret);
							String svcId=retJson.getString("svcId");
							String curDate = retJson.getString("date");
//							JSONObject tellerStartLine = retJson.getJSONObject("tellerStartLine");
//							String[] strs = ret.split("_");
							threadMap.remove(svcId);
							CacheOperationImpl.remove(serviceIdentifier, cacheName, redisHrefPre+svcId);
							AppLogger.info("删除svcId["+svcId+"]的redisHref");
//							if (!strs[2].equals(((String) svc_date.get(strs[1])))) {
//								// 返回的日期和当前日期已经不同，需要更新redis的缓存值
//								CacheOperationImpl.putAndReplicate(serviceIdentifier,
//										cacheName, "hersSendMsg_" + strs[1]
//												+ "_curDate", strs[2]);
//							}
							String key = "hersTellerLog_"+curDate+"_"+svcId;
							AppLogger.info("写入缓存key[" + key + "]serviceIdentifier["
									+ serviceIdentifier + "]cacheName[" + cacheName
									+ "]\n");
							CacheOperationImpl.putAndReplicate(serviceIdentifier,
									cacheName, key, ret);
							AppLogger.info("写入缓存ok");
							CacheOperationImpl.expire(serviceIdentifier, cacheName,
									key, 60*60*24);
							AppLogger.info("设置超时ok");
						} catch (CacheOperationException e) {
							e.printStackTrace();
							AppLogger.info("redis操作失败[" + e.getMessage() + "]\n");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			for(int i=0;i<finishedSvcIdThread.size();i++){
//				threadMap.remove(finishedSvcIdThread.get(i));
//			}
			//threadMap中剩下的部分，就是尚未完成的svc线程，下面进行redis expire刷新处理
			Object[] unfinishedSvcId=threadMap.keySet().toArray();
			for(int i=0;i<unfinishedSvcId.length;i++){
				try{
					AppLogger.info("设置有效期为60秒,key["+redisHrefPre+unfinishedSvcId[i]+"]");
					CacheOperationImpl.putAndReplicate(serviceIdentifier, cacheName, redisHrefPre+unfinishedSvcId[i],"");
					CacheOperationImpl.expire(serviceIdentifier, cacheName, redisHrefPre+unfinishedSvcId[i], 60);
				}catch(Exception e){
					AppLogger.info("更新redis中的redisHrefPre失败，svc["+unfinishedSvcId[i]+"]，"+e.getMessage());
				}
			}
			
			if (!findFlag)break;
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		AppLogger.info("关闭线程池");
		exec.shutdown();
		AppLogger.info("关闭线程池ok");
		return TCResult.newSuccessResult();
	}

}
