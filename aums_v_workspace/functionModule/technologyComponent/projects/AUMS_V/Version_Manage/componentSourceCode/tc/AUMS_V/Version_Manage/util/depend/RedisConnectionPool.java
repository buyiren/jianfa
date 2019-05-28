package tc.AUMS_V.Version_Manage.util.depend;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

public class RedisConnectionPool {
	
	private static JedisSentinelPool pool;
	static{
		createConnectionPool();
	}

	public synchronized static Jedis getJedits(){
		try{
			if(pool !=null){
				Jedis resource = pool.getResource();
				//尝试下redis的连接是否可用
				resource.set("lklk123123", "99");
				return resource;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			//如果此时redis的连接不可用，则进行初始化操作
			createConnectionPool();
			if(pool !=null){
				Jedis resource = pool.getResource();
				//尝试下redis的连接是否可用
				resource.set("lklk123123", "99");
				return resource;
			}else{
				return null;
			}
		}
	} 
	public static void close(final Jedis jedis){
		if(jedis!=null){
			jedis.close();
		}
	}
	
	public static void createConnectionPool(){
		InputStream in=null;
		try{
			//数据库配置方式获取redis配置信息
			String getSentinelInfo = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='AUMS' AND tpi.TRANSCODE='redis' AND tpi.PARAMKEYNAME='sentinels'";
			String getMaster = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='AUMS' AND tpi.TRANSCODE='redis' AND tpi.PARAMKEYNAME='master'";
			String getPassword = "select PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE ='AUMS' AND tpi.TRANSCODE='redis' AND tpi.PARAMKEYNAME='password'";
			
			JavaList sentinelInfoList = (JavaList) P_Jdbc.dmlSelect(null, getSentinelInfo, -1).getOutputParams().get(1);
			JavaList masterList = (JavaList) P_Jdbc.dmlSelect(null, getMaster, -1).getOutputParams().get(1);
			JavaList passwordList = (JavaList) P_Jdbc.dmlSelect(null, getPassword, -1).getOutputParams().get(1);
			
			String sentinelInfo = sentinelInfoList.getListItem(0).get(0).toString();
			String password = EncryptUtil.decryptor(passwordList.getListItem(0).get(0).toString());
			String master = masterList.getListItem(0).get(0).toString();
			
			String[] sentinelsArray = sentinelInfo.split(";");
			Set<String> sentinels = new HashSet<String>();
			for (int i = 0; i < sentinelsArray.length; i++) {
				sentinels.add(sentinelsArray[i]);
			}
			//获取Redis数据库连接
			pool = new JedisSentinelPool(master, sentinels,new GenericObjectPoolConfig(), 1000,password);
		}catch(Exception e){
			AppLogger.info("初始化redis连接池错误...");
			AppLogger.info(e.getMessage());
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					AppLogger.info("初始化redis连接池时关闭输入流异常...");
					AppLogger.info(e.getMessage());
				}
			}
		}
	}
}
