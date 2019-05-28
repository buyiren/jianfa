package tc.AUMS_V.Version_Manage.util;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.SimpleDateFormat;
import java.util.Date;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * P2P基础服务类
 * 
 * @date 2018-03-15 16:42:40
 */
@ComponentGroup(level = "应用", groupName = "P2P基础服务类")


public class A_P2PServer {

	public static final String dateTime = "yyyyMMddHHmmss";
	
	
	/**
	 * @category 根据设备汇报的P2P信息更新服务端相应状态
	 * @param devUniqueID
	 *            入参|设备唯一标识|{@link java.lang.String}
	 * @param useFileServiceUrl
	 *            入参|C端完成更新时使用的文件服务Url地址|{@link java.lang.String}
     * @param unavailableFileServiceUrls
	 *            入参|C端尝试下载文件得到的无效的文件服务Url地址|{@link JavaList}
     * @param peerFileServiceUrl
	 *            入参|C端P节点文件服务Url地址|{@link java.lang.String}
     * @param status
	 *            入参|报告更新状态|{@link java.lang.String}
     * @param totalProgress
	 *            入参|进度百分比|{@link java.lang.String}
	 */
	@InParams(param = { 
			@Param(name = "devUniqueID", comment = "设备唯一标识", type = java.lang.String.class) ,
			@Param(name = "useFileServiceUrl", comment = "C端完成更新时使用的文件服务Url地址", type = java.lang.String.class),
			@Param(name = "unavailableFileServiceUrls", comment = "C端尝试下载文件得到的无效的文件服务Url地址", type = JavaList.class),
			@Param(name = "peerFileServiceUrl", comment = "C端P节点文件服务Url地址", type = java.lang.String.class),
			@Param(name = "reportStatus", comment = "报告更新状态", type = java.lang.String.class),
			@Param(name = "reportTotalProgress", comment = "进度百分比", type = java.lang.String.class)})
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "更新失败"),
			@Return(id = "1", desp = "更新成功") })
	@Component(label = "接收C端P2P汇报服务", comment = "接收C端P2P汇报服务", style = "判断型", type = "同步组件",author = "alpha", date = "2018-03-15 02:26:38")
	
	public static TCResult A_p2pUpdateReports(String devUniqueID,String useFileServiceUrl,JavaList unavailableFileServiceUrls,String peerFileServiceUrl,String reportStatus,String reportTotalProgress) {
	
		String devrandom = devUniqueID;
		//C端完成更新时使用的文件服务Url地址
		String usefulUrl = useFileServiceUrl;
		//C端尝试下载文件得到的无效的文件服务Url地址
//		JSONArray unuserfulUrl = JSONArray.parseArray(unavailableFileServiceUrls);
		JavaList unuserfulUrl = unavailableFileServiceUrls;
		//C端IP地址
		String devip = getDevIpByUniqueNo(devrandom);
		//报告更新状态
		int status = Integer.valueOf(reportStatus).intValue();
		@SuppressWarnings("unused")
		//进度百分比
		int totalProgress = Integer.valueOf(reportTotalProgress);
		//获取设备ID
		String devId = getDevId(devrandom);
		
		JSONObject object = new JSONObject();
		if (devId == null) {
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "上送的设备未激活");
			obj.put("details", "上送的设备未激活");
			object.put("error", obj);
			AppLogger.info("返回C端报文：【"+object+"】");
			return TCResult.newSuccessResult(object.toJSONString());
		}
		
		String versionInfoSQL = "select versionid,createtime from aums_ver_to_dev where updatepolicyid = (select  max(to_number(updatepolicyid)) from aums_ver_to_dev where devid = '" + devId + "')";
		
		TCResult versionInfoResult = P_Jdbc.dmlSelect(null, versionInfoSQL, -1);
		if(versionInfoResult==null){
			AppLogger.error("查询aums_ver_to_dev数据库异常");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端查询版本设备关系异常");
			obj.put("details", "v端查询版本设备关系异常");
			object.put("error", obj);
			AppLogger.info("返回C端报文：【"+object.toString()+"】");
			return TCResult.newSuccessResult(object.toJSONString());
		}
		if(versionInfoResult.getStatus()==2){
			AppLogger.error("查询aums_ver_to_dev数据内容为空");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端查询版本设备关系内容为空");
			obj.put("details", "v端查询版本设备关系内容为空");
			object.put("error", obj);
			AppLogger.info("返回C端报文：【"+object.toString()+"】");
			return TCResult.newSuccessResult(object.toJSONString());
		}
		JavaList dateList = (JavaList)versionInfoResult.getOutputParams().get(1);
		String versionid = dateList.getListItem(0).get(0).toString();
		String createtime = dateList.getListItem(0).get(1).toString();
		AppLogger.info("取得的最新版本号和业务日期为:" + versionid + ";" + createtime);
		
		//获取V端服务地址
		String addr = null;
		try {
			String p2pServiceIpSql = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'VerServiceIp'";
			
			TCResult p2pServiceIPResult = P_Jdbc.dmlSelect(null, p2pServiceIpSql, -1);
			if(p2pServiceIPResult==null){
				AppLogger.error("查询aums_ver_to_dev数据库异常");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数异常");
				obj.put("details", "v端查询系统参数异常");
				object.put("error", obj);
				AppLogger.info("返回C端报文：【"+object.toString()+"】");
				return TCResult.newSuccessResult(object.toJSONString());
			}
			if(p2pServiceIPResult.getStatus()==2){
				AppLogger.error("查询aums_ver_to_dev数据内容为空");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数内容为空");
				obj.put("details", "v端查询系统参数内容为空");
				object.put("error", obj);
				AppLogger.info("返回C端报文：【"+object.toString()+"】");
				return TCResult.newSuccessResult(object.toJSONString());
			}
			JavaList p2pServiceIpList = (JavaList)p2pServiceIPResult.getOutputParams().get(1);
			addr = p2pServiceIpList.getListItem(0).get(0).toString();
			AppLogger.info("获取到的IP地址为：【"+addr+"】");
		} catch (Exception e) {
			AppLogger.info("处理异常:"+e.getLocalizedMessage());
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端获取设备地址异常");
			obj.put("details", "v端获取设备地址异常");
			object.put("error", obj);
			AppLogger.info("返回C端报文：【"+object.toString()+"】");
			return TCResult.newSuccessResult(object.toJSONString());
		}
		AppLogger.info("C端汇报版本更新进度，对应的状态为：【"+status+"】");
		if (status == 3) {
			AppLogger.info("C端版本更新完成");
			if (!"".equals(unuserfulUrl) && null!=unuserfulUrl && !unuserfulUrl.isEmpty()) {
				for (Object ips : unuserfulUrl) {
					AppLogger.info("获取到的无效的URL信息为：【"+ips+"】");
					String devip1 = splitUrl(ips.toString());
					String devid = selectDevidByIp(devip1);
					//20180328新增 lk
					String port = splitUrlGetPort(ips.toString());
					//设备ID【devid】返回：09，则表示未查询到相关设备信息
					if(devid.equals("09")){
						//如果返回的是V端地址，则不更新aums_p2pserver
						if(devip1.equals(addr)){
							//不做任何处理
							continue;
						}else{
							object.put("result", "");
							object.put("success", false);
							JSONObject obj = new JSONObject();
							obj.put("code", "09");
							obj.put("message", "IP地址：【"+devip1+"】,对应的设备未激活");
							obj.put("details", "IP地址：【"+devip1+"】,对应的设备未激活");
							object.put("error", obj);
							AppLogger.info("返回C端报文：【"+object.toString()+"】");
							return TCResult.newSuccessResult(object.toJSONString());
						}
					}
					//20180328 lk 增加 保证一个设备ID在aums_p2pserver只有一条【且这条的一定是最新的版本号,保证C端作为P来说都是当前C端最新的版本】
					String queryDevID = "select devid from aums_p2pserver where devid='" + devid + "'";//不带版本号进行查询，设备ID是唯一的
					
					TCResult queryDevIDResult = P_Jdbc.dmlSelect(null, queryDevID, -1);
					if(queryDevIDResult==null){
						AppLogger.error("查询aums_p2pserver数据库异常");
						object.put("result", "");
						object.put("success", false);
						JSONObject obj = new JSONObject();
						obj.put("code", "01");
						obj.put("message", "v端查询P2P服务异常");
						obj.put("details", "v端查询P2P服务异常");
						object.put("error", obj);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
						return TCResult.newSuccessResult(object.toJSONString());
					}
					
					if(queryDevIDResult.getStatus()==2){
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','0','" + createtime + "','0')";
						P_Jdbc.executeSQL(null, insertSQL, true);
					}else{
						//先删除，然后添加
						String delSQL = "delete from aums_p2pserver where devid='" + devid + "'";
						P_Jdbc.executeSQL(null, delSQL, true);
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','0','" + createtime + "','0')";
						P_Jdbc.executeSQL(null, insertSQL, true);
					}
				}
				object.put("result", "");
				object.put("success", true);
				object.put("error", null);
				AppLogger.info("返回C端报文：【"+object.toString()+"】");
//				return object;
			}
			//处理有效的URL信息
			if (!"".equals(usefulUrl) && usefulUrl != null) {
				AppLogger.info("获取到的有效的URL信息为：【"+usefulUrl+"】");
				String devip1 = splitUrl(usefulUrl);
				String devid = selectDevidByIp(devip1);
				//20180328新增 lk
				String port = splitUrlGetPort(usefulUrl);
				//设备ID【devid】返回：09，则表示未查询到相关设备信息
				if(devid.equals("09")){
					//如果返回的是V端地址，则不更新aums_p2pserver
					if(devip1.equals(addr)){
						//直接返回成功
						object.put("result", "");
						object.put("success", true);
						object.put("error", null);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
//						return object;
					}else{
						object.put("result", "");
						object.put("success", false);
						JSONObject obj = new JSONObject();
						obj.put("code", "09");
						obj.put("message", "IP地址：【"+devip1+"】,对应的设备未激活");
						obj.put("details", "IP地址：【"+devip1+"】,对应的设备未激活");
						object.put("error", obj);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
						return TCResult.newSuccessResult(object.toJSONString());
					}
				}else{
					//20180328 lk 增加 保证一个设备ID在aums_p2pserver只有一条【且这条的一定是最新的版本号,保证C端作为P来说都是当前C端最新的版本】
					String queryDevId = "select usetimes,versionid from aums_p2pserver where devid='" + devid + "'";//不带版本号进行查询，设备ID是唯一的
					TCResult queryDevResult = P_Jdbc.dmlSelect(null, queryDevId, -1);
					if(queryDevResult==null){
						AppLogger.error("查询aums_p2pserver数据库异常");
						object.put("result", "");
						object.put("success", false);
						JSONObject obj = new JSONObject();
						obj.put("code", "01");
						obj.put("message", "v端查询P2P服务异常");
						obj.put("details", "v端查询P2P服务异常");
						object.put("error", obj);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
						return TCResult.newSuccessResult(object.toJSONString());
					}
					if(queryDevResult.getStatus()==2){
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','1','" + createtime + "','0')";
						P_Jdbc.executeSQL(null, insertSQL, true);
					}else{
						//先删除，然后添加
						JavaList queryList = (JavaList)queryDevResult.getOutputParams().get(1);
						String usetimes = "";
						//判断是否收新的版本，如果是新的版本则计数器充值为0，否则计数器+1
						if(queryList.getListItem(0).get(1).toString().equals(versionid)){
							usetimes = String.valueOf(Integer.valueOf(queryList.getListItem(0).get(0).toString())+1);//计数器+1
						}else{
							usetimes = "0";
						}
						String delSQL = "delete from aums_p2pserver where devid='" + devid + "'";
						P_Jdbc.executeSQL(null, delSQL, true);
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','1','" + createtime + "','" + usetimes + "')";
						P_Jdbc.executeSQL(null, insertSQL, true);
					}
					//20180328 lk 屏蔽
//					String updatesql = "update aums_p2pserver set isused='1',usetimes = usetimes+1 where devid ='"
//							+ devid
//							+ "' and versionid = '"
//							+ versionid
//							+ "'";
//					dbSupport.executeSQL(updatesql);
					object.put("result", "");
					object.put("success", true);
					object.put("error", null);
					AppLogger.info("返回C端报文：【"+object.toString()+"】");
				}
//				return object;
			}
			//处理C端成为P时的情况
			if (!"".equals(devip) && devip != null) {
				AppLogger.info("获取到C成为P时的URL地址：【"+devip+"】");
				String devip1 = splitUrl(devip);
				//String port = splitUrlGetPort(devip);
				String port = splitUrlGetPort(usefulUrl);
				String devid = selectDevidByIp(devip1);
				//设备ID【devid】返回：09，则表示未查询到相关设备信息
				if(devid.equals("09")){
					//如果返回的是V端地址，则不更新aums_p2pserver
					if(devip1.equals(addr)){
						//不做任何处理
						object.put("result", "");
						object.put("success", true);
						object.put("error", null);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
					}else{
						object.put("result", "");
						object.put("success", false);
						JSONObject obj = new JSONObject();
						obj.put("code", "09");
						obj.put("message", "IP地址：【"+devip1+"】,对应的设备未激活");
						obj.put("details", "IP地址：【"+devip1+"】,对应的设备未激活");
						object.put("error", obj);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
						return TCResult.newSuccessResult(object.toJSONString());
					}
				}else{
					//20180328 lk 增加 保证一个设备ID在aums_p2pserver只有一条【且这条的一定是最新的版本号,保证C端作为P来说都是当前C端最新的版本】
					String queryDevId = "select usetimes,versionid from aums_p2pserver where devid='" + devid + "'";//不带版本号进行查询，设备ID是唯一的
					TCResult queryDevResult = P_Jdbc.dmlSelect(null, queryDevId, -1);
					if(queryDevResult==null){
						AppLogger.error("查询aums_p2pserver数据库异常");
						object.put("result", "");
						object.put("success", false);
						JSONObject obj = new JSONObject();
						obj.put("code", "01");
						obj.put("message", "v端查询P2P服务异常");
						obj.put("details", "v端查询P2P服务异常");
						object.put("error", obj);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
						return TCResult.newSuccessResult(object.toJSONString());
					}
					if(queryDevResult.getStatus()==2){
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','1','" + createtime + "','0')";
						P_Jdbc.executeSQL(null, insertSQL, true);
						object.put("result", "");
						object.put("success", true);
						object.put("error", null);
						AppLogger.info("成为P时插入操作,返回C端报文：【"+object.toString()+"】");
					}else{
						JavaList queryList = (JavaList)queryDevResult.getOutputParams().get(1);
						//C端每次更新都会发送自己成为P的情况，此时并不是被使用，只是成为P的情况，暂时计数器先不加1
						//先删除，然后添加
						String delSQL = "delete from aums_p2pserver where devid='" + devid + "'";
						String usetimes = "";
						//判断是否收新的版本，如果是新的版本则计数器充值为0，否则计数器+1
						if(queryList.getListItem(0).get(1).toString().equals(versionid)){
							usetimes = queryList.getListItem(0).get(0).toString();
						}else{
							usetimes = "0";
						}
						P_Jdbc.executeSQL(null, delSQL, true);
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','1','" + createtime + "','" + usetimes + "')";
						P_Jdbc.executeSQL(null, insertSQL, true);
						object.put("result", "");
						object.put("success", true);
						object.put("error", null);
						AppLogger.info("成为P时已经存在不更新,返回C端报文：【"+object.toString()+"】");
					}
				}
			}
			return TCResult.newSuccessResult(object.toJSONString());
		} else {
			//状态不为3的时候，只处理unuserfulUrl列表里的URL
			if (!"".equals(unuserfulUrl) && null!=unuserfulUrl  && !unuserfulUrl.isEmpty()) {
				//处理不可用的Peer信息
				for (Object ips : unuserfulUrl) {
					String devip1 = splitUrl(ips.toString());
					String devid = selectDevidByIp(devip1);
					//20180328新增 lk
					String port = splitUrlGetPort(ips.toString());
					//设备ID【devid】返回：09，则表示未查询到相关设备信息
					if(devid.equals("09")){
						//如果返回的是V端地址，则不更新aums_p2pserver
						if(devip1.equals(addr)){
							//不做任何处理
							continue;
						}else{
							object.put("result", "");
							object.put("success", false);
							JSONObject obj = new JSONObject();
							obj.put("code", "09");
							obj.put("message", "IP地址：【"+devip1+"】,对应的设备未激活");
							obj.put("details", "IP地址：【"+devip1+"】,对应的设备未激活");
							object.put("error", obj);
							AppLogger.info("返回C端报文：【"+object.toString()+"】");
							return TCResult.newSuccessResult(object.toJSONString());
						}
					}
					//20180328 lk 增加 保证一个设备ID在aums_p2pserver只有一条【且这条的一定是最新的版本号,保证C端作为P来说都是当前C端最新的版本】
					String queryDevId = "select usetimes from aums_p2pserver where devid='" + devid + "'";//不带版本号进行查询，设备ID是唯一的
					
					TCResult queryDevResult = P_Jdbc.dmlSelect(null, queryDevId, -1);
					if(queryDevResult==null){
						AppLogger.error("查询aums_p2pserver数据库异常");
						object.put("result", "");
						object.put("success", false);
						JSONObject obj = new JSONObject();
						obj.put("code", "01");
						obj.put("message", "v端查询P2P服务异常");
						obj.put("details", "v端查询P2P服务异常");
						object.put("error", obj);
						AppLogger.info("返回C端报文：【"+object.toString()+"】");
						return TCResult.newSuccessResult(object.toJSONString());
					}
					
					if(queryDevResult.getStatus()==2){
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','0','" + createtime + "','0')";
						P_Jdbc.executeSQL(null, insertSQL, true);
						object.put("result", "");
					}else{
						//先删除，然后添加
						String delSQL = "delete from aums_p2pserver where devid='" + devid + "'";
						P_Jdbc.executeSQL(null, delSQL, true);
						//进行添加
						String insertSQL = "insert into aums_p2pserver(devid,devip,peerport,versionid,isused,workdate,usetimes) values('"
								+ devid
								+ "','"
								+ devip1
								+ "','"
								+ port
								+ "','"
								+ versionid
								+ "','0','" + createtime + "','0')";
						P_Jdbc.executeSQL(null, insertSQL, true);
						object.put("result", "");
					}
					//20180328 lk 屏蔽
//					String updatesql = "update aums_p2pserver set isused='0' where devid ='"
//							+ devid
//							+ "'and versionid = '"
//							+ versionid
//							+ "'";
//					dbSupport.executeSQL(updatesql);
//					object.put("result", "");
				}
				object.put("success", true);
				object.put("error", null);
				AppLogger.info("返回C端报文：【"+object.toString()+"】");
				return TCResult.newSuccessResult(object.toJSONString());
			}else{
				//状态不为3的时候，只处理unuserfulUrl列表里的URL
				AppLogger.info("C端汇报状态不为完成，同时不可用列表状态为空,系统暂不处理");
				object.put("result", "");
				object.put("success", true);
				object.put("error", null);
				AppLogger.info("返回C端报文：【"+object.toString()+"】");
				return TCResult.newSuccessResult(object.toJSONString());
			}
		}
		//不处理，直接返回已收到
//		object.put("result", "");
//		object.put("success", true);
//		object.put("error", null);
//		AppLogger.info("返回C端报文：【"+object.toString()+"】");
//		return object;
	}

	// 根据设备码找到设备id
	private static String getDevId(String devrandom)
	{
		String devidsql = "select devid from aums_Dev_info where devuniqueid='" + devrandom + "'";
		
		TCResult tmpResult = P_Jdbc.dmlSelect(null, devidsql, -1);
		if (tmpResult == null) {
			AppLogger.info("数据库操作异常");
			return null;
		}
		if (tmpResult.getStatus()==2) {
			return null;
		}
		JavaList tmpList = (JavaList)tmpResult.getOutputParams().get(1);
		String devId = tmpList.getListItem(0).get(0).toString();
		return devId;
	}
	
	/**
	 * 依照设备唯一码获取机具IP地址
	 * @param devrandom
	 * @return
	 */
	private static String getDevIpByUniqueNo(String devrandom)
	{
		String devidsql = "select devip from aums_Dev_info where devuniqueid='" + devrandom + "'";
		
		TCResult tmpResult = P_Jdbc.dmlSelect(null, devidsql, -1);
		if (tmpResult == null) {
			AppLogger.info("数据库操作异常");
			return null;
		}
		if (tmpResult.getStatus()==2) {
			return null;
		}
		JavaList tmpList = (JavaList)tmpResult.getOutputParams().get(1);
		String devIp = tmpList.getListItem(0).get(0).toString();
		return devIp;
	}

	// 将http://192.168.0.1:8090/PCVA/截取成192.168.0.1
	/**
	 * 根据URL返回IP
	 * @param Url
	 * @return 返回Ip地址
	 */
	private static String splitUrl(String Url) {
		String[] ipports = Url.split("/");
		if(ipports.length ==1){
			return Url;
		}
		String[] ips = ipports[2].split(":");
		String ip = ips[0];
		return ip;
	}

	/**
	 * 根据URL返回端口
	 * @param Url
	 * @return
	 */
	private static String splitUrlGetPort(String Url) {
		String[] ipports = Url.split("/");
		if(ipports.length ==1){
			return Url;
		}
		String[] ips = ipports[2].split(":");
		String port = ips[1];
		return port;
	}
	
	// 根据设备IP找到设备Id
	private static String selectDevidByIp(String devip)
	{
		String devidsql = "select devid from aums_Dev_info where DEVIP='"
				+ devip + "'";
		TCResult tmpResult = P_Jdbc.dmlSelect(null, devidsql, -1);
		if (tmpResult == null) {
			AppLogger.info("数据库操作异常");
			return null;
		}
		if (tmpResult.getStatus() == 2) {
			AppLogger.info("IP地址：【"+devip+"】,对应的设备未激活"+"】");
			String code="09";
			return code;
		}
		JavaList tmpList = (JavaList)tmpResult.getOutputParams().get(1);
		String devId = tmpList.getListItem(0).get(0).toString();
		return devId;
	}

	/**
	 * @category 根据设备唯一标识返回可作为PEER资源的列表
	 * @param devUniqueID
	 *            入参|设备唯一标识|{@link java.lang.String}
	 * @return
	 */
	@InParams(param = { @Param(name = "devUniqueID", comment = "设备唯一标识", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "PEER可用列表下载", comment = "PEER可用列表下载", style = "选择型", type = "同步组件",author = "alpha", date = "2018-03-15 02:26:38")
	
	public static TCResult A_P2PController(String devUniqueID) {
		
		String devrandom = devUniqueID;
		JSONObject result = new JSONObject();
		JSONObject object = new JSONObject();
		JSONArray fileServiceUrl1 = new JSONArray();
		JSONObject versionServiceInfo = new JSONObject();
		String addr = null;
		String port = null;
		try {
			//获取V端版本下载提供的服务IP地址
			//String p2pServiceIpSql = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'VerServiceIp'";
			//20180907韩斌修改下载基地址为nginx地址
			String p2pServiceIpSql="select  PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE='Version_Manage' AND tpi.TRANSCODE='verFileDownLoad' AND tpi.PARAMKEYNAME='rootPath'";
			
			//获取V端版本下载提供的服务端口
			String p2pServicePortSql = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'VerServicePort'";
			
			
			
			
			
			TCResult p2pServerResult = P_Jdbc.dmlSelect(null, p2pServiceIpSql, -1);
			if (p2pServerResult == null) {
				AppLogger.info("v端查询系统参数异常");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数异常");
				obj.put("details", "v端查询系统参数异常");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			if (p2pServerResult.getStatus() == 2) {
				AppLogger.info("v端查询系统参数内容为空");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数内容为空");
				obj.put("details", "v端查询系统参数内容为空");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			JavaList p2pServerList = (JavaList)p2pServerResult.getOutputParams().get(1);
			
			TCResult p2pServerPortResult = P_Jdbc.dmlSelect(null, p2pServicePortSql, -1);
			if (p2pServerPortResult == null) {
				AppLogger.info("v端查询系统参数异常");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数异常");
				obj.put("details", "v端查询系统参数异常");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			if (p2pServerPortResult.getStatus() == 2) {
				AppLogger.info("v端查询系统参数内容为空");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数内容为空");
				obj.put("details", "v端查询系统参数内容为空");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			JavaList p2pServerPortList = (JavaList)p2pServerPortResult.getOutputParams().get(1);
			
			addr = p2pServerList.getListItem(0).get(0).toString();
			port = p2pServerPortList.getListItem(0).get(0).toString();
			
			AppLogger.info("获取到的IP地址为：【"+addr+"】,服务端口为：【"+port+"】");
		} catch (Exception e) {
			AppLogger.info("处理异常：【"+e.getLocalizedMessage()+"】");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端获取设备地址异常");
			obj.put("details", "v端获取设备地址异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		
		//检查设备是否已注册
		String devidSql = "select devid,devip from aums_dev_info where devuniqueid='" + devrandom + "'";
		
		TCResult devidResult = P_Jdbc.dmlSelect(null, devidSql, -1);
		if (devidResult == null) {
			AppLogger.info("v端查询设备信息异常");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端查询设备信息异常");
			obj.put("details", "v端查询设备信息异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		if (devidResult.getStatus() == 2) {
			AppLogger.info("v端查询设备信息内容为空");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "设备未激活");
			obj.put("details", "设备未激活");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		JavaList devIDList = (JavaList)devidResult.getOutputParams().get(1);
		String devId = devIDList.getListItem(0).get(0).toString();
		AppLogger.info("设备ID为：【"+devId+"】");
		String devip = devIDList.getListItem(0).get(1).toString();
		AppLogger.info("设备IP为：【"+devip+"】");
		//机具ip地址的前三段
		String ip = devip.substring(0,devip.lastIndexOf("."));
		String versioninfosql = "select versionid,createtime from aums_ver_to_dev where updatepolicyid = (select  max(to_number(updatepolicyid)) from  aums_ver_to_dev where devid = '" + devId + "')";
		
		
		TCResult versionResult = P_Jdbc.dmlSelect(null, versioninfosql, -1);
		if (versionResult == null) {
			AppLogger.info("v端数据库操作异常");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端数据库操作异常");
			obj.put("details", "v端数据库操作异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		if (versionResult.getStatus() == 2) {
			AppLogger.info("v端查询设备对应的版本信息内容为空");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端查询设备对应的版本信息内容为空");
			obj.put("details", "v端查询设备对应的版本信息内容为空");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		JavaList versionList = (JavaList)versionResult.getOutputParams().get(1);
		String versionid = versionList.getListItem(0).get(0).toString();
		String createtime = versionList.getListItem(0).get(1).toString();
		//获取V端URL
		String v_url = createUrl(addr, Integer.valueOf(port),versionid);
		//查询该市场是否支持P2P
		String isP2Psql = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'ISSUPPORT'";
		
		TCResult isP2PResult = P_Jdbc.dmlSelect(null, isP2Psql, -1);
		if (isP2PResult == null) {
			AppLogger.info("v端数据库操作异常");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端数据库操作异常");
			obj.put("details", "v端数据库操作异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		if (isP2PResult.getStatus() == 2) {
			AppLogger.info("v端查询参数信息内容为空");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端查询参数信息内容为空");
			obj.put("details", "v端查询参数信息内容为空");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		JavaList isP2PList = (JavaList)isP2PResult.getOutputParams().get(1);
		
		String isP2P = isP2PList.getListItem(0).get(0).toString();
		if (isP2P.equals("0")) {
			fileServiceUrl1.add(v_url);
			result.put("fileServiceUrls", fileServiceUrl1);
			result.put("isKeepWaiting", false);
			result.put("reconnectInterval", 1000);
			result.put("peerServerResourceDir", "");
			versionServiceInfo.put("result", result);
			versionServiceInfo.put("success", true);
			versionServiceInfo.put("error", null);
			AppLogger.info("返回C端报文：【"+versionServiceInfo.toString()+"】");
			return  TCResult.newSuccessResult(versionServiceInfo.toJSONString());
		}
		//查询同一网段已经有几个设备成为P的可能
		String countsql = "select devip,devid from aums_p2pcontrol where versionid='" + versionid + "' and devip like '" + ip + "%'";
		
		TCResult p2pCountResult = P_Jdbc.dmlSelect(null, countsql, -1);
		if (p2pCountResult == null) {
			AppLogger.info("v端数据库操作异常");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端数据库操作异常");
			obj.put("details", "v端数据库操作异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTime);
		String currentTime = simpleDateFormat.format(new Date());
		
		if (p2pCountResult.getStatus()==2) {
			//没有的话，返回V端地址，并登记此IP为有可能成为P的可能
			fileServiceUrl1.add(v_url);
			result.put("fileServiceUrls", fileServiceUrl1);
			result.put("isKeepWaiting", false);
			result.put("reconnectInterval", 1000);
			result.put("peerServerResourceDir", "");
			versionServiceInfo.put("result", result);
			versionServiceInfo.put("success", true);
			versionServiceInfo.put("error", null);
			String regsql = "insert into aums_p2pcontrol(requestdate,requesttime,devid,devip,versionid) values('"
					+ createtime
					+ "','"
					+ currentTime
					+ "','" + devId + "','" + devip + "','" + versionid + "')";
			try {
				P_Jdbc.executeSQL(null, regsql, true);
			} catch (Exception e) {
				AppLogger.info("数据库异常:"+e.getLocalizedMessage());
				object.put("result", "");
				object.put("success", true);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端登记P2P服务表异常");
				obj.put("details", "v端登记P2P服务表异常");
				object.put("error", obj);
				AppLogger.info("返回C端报文：【"+object.toString()+"】");
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			AppLogger.info("同一网段内没有成为P的节点，直接返回V端路径，返回C端报文：【"+versionServiceInfo.toString()+"】");
			return TCResult.newSuccessResult(versionServiceInfo.toJSONString());
		} else {
			JavaList devipList = (JavaList)p2pCountResult.getOutputParams().get(1);
			//判断下同一网段允许出现Peer的次数，该次数通过tp_cip_sysparameters配置
			String P2PNumSQL = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'P2PNUM'";
			
			TCResult getP2PNumResult = P_Jdbc.dmlSelect(null, P2PNumSQL, -1);
			if (getP2PNumResult == null) {
				AppLogger.info("v端数据库操作异常");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端获取系统参数异常");
				obj.put("details", "v端获取系统参数异常");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			if (getP2PNumResult.getStatus() == 2) {
				AppLogger.info("v端查询参数信息内容为空");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询参数信息内容为空");
				obj.put("details", "v端查询参数信息内容为空");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			JavaList getP2PNumList = (JavaList)getP2PNumResult.getOutputParams().get(1);
			
			String p2pNum = getP2PNumList.getListItem(0).get(0).toString();
			AppLogger.info("同一网段内配置的最大Peer个数为：【"+p2pNum+"】");
			if(devipList.size()<Integer.valueOf(p2pNum)){
				//aums_p2pcontrol + 1
				//小于配置的P个数，暂时返回V端的URL地址作为下载地址
				fileServiceUrl1.add(v_url);
				result.put("fileServiceUrls", fileServiceUrl1);
				result.put("isKeepWaiting", false);
				result.put("reconnectInterval", 1000);
				result.put("peerServerResourceDir", "");
				versionServiceInfo.put("result", result);
				versionServiceInfo.put("success", true);
				versionServiceInfo.put("error", null);
				//准备成为P的节点+1
				String checkSQL = "select DEVID from aums_p2pcontrol where devid='" + devId + "' and VERSIONID='" + versionid +"'";
				
				TCResult checkSQLResult = P_Jdbc.dmlSelect(null, checkSQL, -1);
				if (checkSQLResult == null) {
					AppLogger.info("v端数据库操作异常");
					object.put("result", "");
					object.put("success", false);
					JSONObject obj = new JSONObject();
					obj.put("code", "01");
					obj.put("message", "v端获取系统参数异常");
					obj.put("details", "v端获取系统参数异常");
					object.put("error", obj);
					return  TCResult.newSuccessResult(object.toJSONString());
				}
				if (checkSQLResult.getStatus() == 2) {
					try {
						String regsql = "insert into aums_p2pcontrol(requestdate,requesttime,devid,devip,versionid) values('"
								+ createtime
								+ "','"
								+ currentTime
								+ "','" + devId + "','" + devip + "','" + versionid + "')";
						P_Jdbc.executeSQL(null, regsql, true);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					AppLogger.error("数据库异常:"+e.getLocalizedMessage());
					object.put("result", "");
					object.put("success", false);
					JSONObject obj = new JSONObject();
					obj.put("code", "01");
					obj.put("message", "v端登记P2P服务表异常");
					obj.put("details", "v端登记P2P服务表异常");
					object.put("error", obj);
					AppLogger.info("返回C端报文：【"+object.toString()+"】");
					return  TCResult.newSuccessResult(object.toJSONString());
				}
					
				}
				AppLogger.info("返回C端报文：【"+versionServiceInfo.toString()+"】");
				return  TCResult.newSuccessResult(versionServiceInfo.toJSONString());
			}
			//超过配置的个数，则再上来的请求需要从peer里面进行返回了，判断对应的准备返回给C端的P是否存在升级记录
			String checkIsusedSQL = "select devip,peerport from aums_p2pserver where versionid = '" + versionid + "' and isused = '1' and devip like '" + ip + "%' and devip!='"+ devip +"' order by to_number(usetimes) asc";
			
			TCResult checkIsusedResult = P_Jdbc.dmlSelect(null, checkIsusedSQL, -1);
			if (checkIsusedResult == null) {
				AppLogger.info("v端数据库操作异常");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端获取系统参数异常");
				obj.put("details", "v端获取系统参数异常");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			JavaList useList = (JavaList)checkIsusedResult.getOutputParams().get(1);
			if (checkIsusedResult.getStatus()==2) {
				//判断所有的peer节点是否可以重置时间
				//20180328 lk modify 将时间作为判断条件去掉
				//时间：20180914，修改人：韩斌，修改原因：暂时不确定表：aums_p2pcontrol没有isused字段，去掉该字段的where查询条件
				//String timeQuerySql = "select max(requesttime) from aums_p2pcontrol where isused = '0' and devip like '" + ip + "%'";
				String timeQuerySql = "select max(requesttime) from aums_p2pcontrol where devip like '" + ip + "%'";
				
				TCResult timeQueryResult = P_Jdbc.dmlSelect(null, timeQuerySql, -1);
				if (timeQueryResult == null) {
					AppLogger.info("v端数据库操作异常");
					object.put("result", "");
					object.put("success", false);
					JSONObject obj = new JSONObject();
					obj.put("code", "01");
					obj.put("message", "v端获取系统参数异常");
					obj.put("details", "v端获取系统参数异常");
					object.put("error", obj);
					return  TCResult.newSuccessResult(object.toJSONString());
				}
				//20180324,dxj修改
				String timeout = "0";
				JavaList timeOutQueryList = (JavaList)timeQueryResult.getOutputParams().get(1);
				if(!(timeOutQueryList.size()>0)){
					timeout = timeOutQueryList.getListItem(0).get(0).toString();
				}
				
				//获取配置的超时时间：
				String P2PWaitTimeSQL = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'WAITTIME'";
				TCResult waitTimeQueryResult = P_Jdbc.dmlSelect(null, P2PWaitTimeSQL, -1);
				if (waitTimeQueryResult == null) {
					AppLogger.info("v端数据库操作异常");
					object.put("result", "");
					object.put("success", false);
					JSONObject obj = new JSONObject();
					obj.put("code", "01");
					obj.put("message", "v端获取系统参数异常");
					obj.put("details", "v端获取系统参数异常");
					object.put("error", obj);
					return  TCResult.newSuccessResult(object.toJSONString());
				}
				if(waitTimeQueryResult.getStatus()==2){
					AppLogger.info("参数查询内容为空");
					object.put("result", "");
					object.put("success", false);
					JSONObject obj = new JSONObject();
					obj.put("code", "01");
					obj.put("message", "v端获取系统参数内容为空");
					obj.put("details", "v端获取系统参数内容为空");
					object.put("error", obj);
					return  TCResult.newSuccessResult(object.toJSONString());
				}
				JavaList P2PWaitTimeList = (JavaList)waitTimeQueryResult.getOutputParams().get(1);
				
				String p2pWaitTime = P2PWaitTimeList.getListItem(0).get(0).toString();
				AppLogger.info("同一网段内配置的最大超时时间为：【"+p2pWaitTime+"】");
				if (Long.parseLong(currentTime) - Long.parseLong(timeout) > Integer.valueOf(p2pWaitTime)) {
					//超过超时时间aums_p2pcontrol没有成为P的，则直接返回对应的V端作为下载服务
					fileServiceUrl1.add(v_url);
					result.put("fileServiceUrls", fileServiceUrl1);
					result.put("isKeepWaiting", false);
					result.put("reconnectInterval", 1000);
					result.put("peerServerResourceDir", "");
					versionServiceInfo.put("result", result);
					versionServiceInfo.put("success", true);
					versionServiceInfo.put("error", null);
					return TCResult.newSuccessResult(versionServiceInfo.toJSONString());
				} else {
					//继续等待
					result.put("fileServiceUrls", null);
					result.put("isKeepWaiting", true);
					result.put("reconnectInterval", 1000);
					result.put("peerServerResourceDir", "");
					versionServiceInfo.put("result", result);
					versionServiceInfo.put("success", true);
					versionServiceInfo.put("error", null);
					AppLogger.info("返回C端报文：【"+versionServiceInfo.toString()+"】");
					return TCResult.newSuccessResult(versionServiceInfo.toJSONString());
				}
			} else {
				JSONArray ipArr = new JSONArray();
				//组织Peer的列表返回给C端
				String ipl = "";
				for(int i = 0; i < useList.size(); i++){
					String ipAddress = useList.getListItem(i).get(0).toString();
					String ipPort = useList.getListItem(i).get(1).toString();
					ipl = "http://"+ipAddress + ":" + ipPort;
					ipArr.add(ipl);
				}
				//dqxu20180621增加一个V端的地址
				ipArr.add(v_url);
				result.put("fileServiceUrls", ipArr);
				result.put("isKeepWaiting", false);
				result.put("reconnectInterval", 1000);
				result.put("peerServerResourceDir", "");
				versionServiceInfo.put("result", result);
				versionServiceInfo.put("success", true);
				versionServiceInfo.put("error", null);
				AppLogger.info("返回C端报文：【"+versionServiceInfo.toString()+"】");
				return TCResult.newSuccessResult(versionServiceInfo.toJSONString());
			}
		}
	} 
	
	
	/**
	 * @category NEW根据设备唯一标识返回可作为PEER资源的列表
	 * @param machineUniqueId
	 *            入参|设备唯一标识|{@link java.lang.String}
	 * @param machineOrganNo
	 *            入参|设备所属机构号|{@link java.lang.String}
	 * @param machineBranchNo
	 *            入参|设备所属分行号|{@link java.lang.String}
	 * @return
	 */
	@InParams(param = { @Param(name = "devUniqueID", comment = "设备唯一标识", type = java.lang.String.class),
						@Param(name = "machineOrganNo", comment = "设备所属机构号", type = java.lang.String.class),
						@Param(name = "machineBranchNo", comment = "设备所属分行号", type = java.lang.String.class)})
	@OutParams(param = { @Param(name = "result", comment = "返回C端报文", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "无满足条件记录") })
	@Component(label = "PEER可用列表下载-NEW", comment = "PEER可用列表下载-NEW", style = "选择型", type = "同步组件",author = "alpha", date = "2018-03-15 02:26:38")
	
	public static TCResult A_P2PControllerNew(String devUniqueID,
			String machineOrganNo,String machineBranchNo) {
		
		JSONObject result = new JSONObject();
		JSONObject object = new JSONObject();
		JSONArray fileServiceUrl1 = new JSONArray();
		JSONObject versionServiceInfo = new JSONObject();
		String addr = null;
		JavaList p2pServerList = null;
		try {
			//获取V端版本下载提供的服务IP地址
			//String p2pServiceIpSql = "select paramvalue from tp_cip_sysparameters where MODULECODE='Version_Manage' and TRANSCODE ='P2P' and PARAMKEYNAME = 'VerServiceIp'";
			//20180907韩斌修改下载基地址为nginx地址
			//String p2pServiceIpSql="select dow.SERVERADDRESS,dow.SERVERPORT from AUMS_VER_DOWNLOADINFO dow where dow.DOWNLOADTYPE='verFileDownLoadRootPath' and dow.BRNO='"+ machineBranchNo +"' order by dow.SEQNO";
			String p2pServiceIpSql="select IP,DOWNLOADPORT from AUMS_VER_DEPSERVERINFO where BRANCHNO='"+ machineBranchNo +"' and SERVERSTATUS='1' order by UPDATETIME desc";

			TCResult p2pServerResult = P_Jdbc.dmlSelect(null, p2pServiceIpSql, -1);
			if (p2pServerResult == null) {
				AppLogger.info("v端查询系统参数异常");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数异常");
				obj.put("details", "v端查询系统参数异常");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			if (p2pServerResult.getStatus() == 2) {
				AppLogger.info("v端查询系统参数内容为空");
				object.put("result", "");
				object.put("success", false);
				JSONObject obj = new JSONObject();
				obj.put("code", "01");
				obj.put("message", "v端查询系统参数内容为空");
				obj.put("details", "v端查询系统参数内容为空");
				object.put("error", obj);
				return  TCResult.newSuccessResult(object.toJSONString());
			}
			p2pServerList = (JavaList)p2pServerResult.getOutputParams().get(1);
		} catch (Exception e) {
			AppLogger.info("处理异常：【"+e.getLocalizedMessage()+"】");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端获取设备地址异常");
			obj.put("details", "v端获取设备地址异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}
		
		
		if (p2pServerList!= null) {
			String ip;
			String port;
			for (int i = 0; i < p2pServerList.size(); i++) {
				//获取V端URL
				//addr = p2pServerList.getListItem(i).get(0).toString();   //http://192.9.200.133:8023/
				ip = p2pServerList.getListItem(i).get(0).toString();
				port = p2pServerList.getListItem(i).get(1).toString();
				fileServiceUrl1.add("http://" + ip + ":" + port +"/");
			}
		} else {
			AppLogger.info("数据库异常！");
			object.put("result", "");
			object.put("success", false);
			JSONObject obj = new JSONObject();
			obj.put("code", "01");
			obj.put("message", "v端获取设备地址异常");
			obj.put("details", "v端获取设备地址异常");
			object.put("error", obj);
			return  TCResult.newSuccessResult(object.toJSONString());
		}

		result.put("fileServiceUrls", fileServiceUrl1);
		result.put("isKeepWaiting", false);
		result.put("reconnectInterval", 1000);
		result.put("peerServerResourceDir", "");
		versionServiceInfo.put("result", result);
		versionServiceInfo.put("success", true);
		versionServiceInfo.put("error", null);
		AppLogger.info("返回C端报文：【"+versionServiceInfo.toString()+"】");
		return  TCResult.newSuccessResult(versionServiceInfo.toJSONString());
		
	} 
	
	/**
	 * 
	 * @param ip ip地址
	 * @param port 端口
	 * @param versionid 版本号
	 * @return
	 */
	private static String createUrl(String ip,int port,String versionid){
//		String v_url  = "http://"+ip+":"+port+"/PCVA/files/"+versionid+"/";
		//20180906修改 李阔 将返回C端的下载URL改为post方式的下载路径
		//String v_url  = "http://"+ip+":"+port+"/Version_Manage/GetVerCurrentVersion";
		//20180907修改 韩斌将返回的C端下载地址为nginx对应地址加文件下载基地址
		String unzip = "select  FILEPATH from AUMS_VER_DETAILINFO where VERSIONID='"+versionid+"'";
		P_Logger.info("执行的sql是"+unzip);
		 TCResult unzipList = P_Jdbc.dmlSelect(null, unzip, -1);
		 JavaList tmpList = (JavaList)unzipList.getOutputParams().get(1);
			String path = tmpList.getListItem(0).get(0).toString();
			String path1 = path.substring(0, path.indexOf("/",path.indexOf("/")+1 ));
			
		String v_url  = ip+path1+"/dotnet/";
		return v_url;
	}
}