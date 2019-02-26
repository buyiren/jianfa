package tc.View.Server.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.EnumSet; 

import org.apache.commons.io.FileUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;

//import com.hupu.iman.util.StringUtil;

/**
 * apache mina sshd功能演示
 * 依赖jar包：slf4j-api-1.7.16.jar，sshd-core-1.2.0.jar，mina-core-2.0.7.jar , expectit-core-0.8.2.jar
 * 依赖jdk版本：1.7以上
 * */
/**
 * SSH基类
 * @author 唐韶东
 * @date 2016-8-8 上午09:12:51
 * @version V3.0
 * @since Tomcat6.0,Jdk1.6
 * @copyright Copyright (c) 2016
 */
public class SSHClient {
//	private static final Logger logger = LogManager.getLogger(BaseSSH.class.getName());
	
	private SshClient client;
	private ClientSession session;
	
	public String account;
	public String password;
	public String host;
	public int port = 22;
	public int timeout = 30;
	ConnectFuture cf =null;
	
	public SSHClient(String host) {
		super();
		this.host = host;
		
	}

	private SSHClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private SSHClient(String host, int port, int timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * SSH连接
	 * @author 唐韶东
	 * @date 2016-8-8 上午10:13:16
	 * @version V3.0
	 * @since Tomcat6.0,Jdk1.6
	 * @copyright Copyright (c) 2016
	 */
	public boolean connect(){
		try {
			client = SshClient.setUpDefaultClient();
			client.start();
			cf = client.connect(account, host, port);
			System.out.println("connect连接......account:"+account+",host:"+host+",port:"+port);
			boolean isAwait = cf.awaitUninterruptibly(timeout, TimeUnit.SECONDS) ;
			boolean isConnected = cf.isConnected();
			System.out.println("isAwait:"+isAwait+";isConnect :"+isConnected);
//			if(cf.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS) && cf.isConnected()){
			if(isAwait&&isConnected){
				System.out.println("connect连接成功！");
				session = cf.getSession();
				session.addPasswordIdentity(password);
				return session.auth().awaitUninterruptibly(timeout, TimeUnit.SECONDS);
			}else{
				writeLog("连接失败["+this.host+"]，返回前关闭了client,和cf。");
				try{client.close();}catch(Exception e){}
				try{client.close(true);}catch(Exception e){}
				try{cf.cancel();}catch(Exception e){}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SSH Connect Error --> {}"+ e.getMessage());
		}
		return false;
	}
	private ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
	/**
	 * 向终端发送命令
	 * @param cmd String 发送的命令
	 * @param encoding String 对方操作系统的语言编码
	 * @author 唐韶东
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public String interaction(String cmd,String encoding) throws IOException, InterruptedException {
		ChannelExec ec=session.createExecChannel(cmd);
		ByteArrayOutputStream f = new ByteArrayOutputStream();
		ec.setOut(f);
        ec.open();
        ec.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.MINUTES.toMillis(1L));
        try{ec.close(true);}catch(Exception e){writeLog("ec Closing Error ========"+e.getMessage());}
        byte[] ret = f.toByteArray();
        f.close();
        return new String(ret,encoding);//这里填写对方的编码
	}
	
	public void closeSession(){
		try {
			if (null != session && session.isOpen()) {
				session.close(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Close Error ---> {}"+ e.getMessage());
		}
	}
	public void cancelCF(){
		try{cf.cancel();}catch(Exception e){}
	}
	private void writeLog(String str){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			FileUtils.writeStringToFile(new File("./tttt"), sdf.format(new Date())+" SshClient["+this.host+"]:"+str+"\n", "utf-8", true);
		}catch(Exception e){
		}
	}
	/**
	 * 关闭连接
	 * @author 唐韶东
	 */
	public void close(){
		writeLog("SSHClient closeing ---");
		try{session.close();}catch(Exception e){}
		try{session.close(true);}catch(Exception e){}
		try{client.close();}catch(Exception e){}
		try{client.close(true);}catch(Exception e){}
		try{cf.cancel();}catch(Exception e){}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
//		String host = "22.5.228.218", account = "abs", password = "abs@gonggong";
		String host = "22.5.229.18", account = "abs", password = "AbS@456";
		String cmd_one = "./myls ./srv01/log/message.log startPos";
		String cmd_two = "./myls2";
		
		SSHClient base = new SSHClient(host);
		base.setAccount(account);
		base.setPassword(password);
		while(true){
			System.out.println("无限次尝试，直到连接成功......");
			boolean isConnect = base.connect();
			System.out.println("connecting..."+isConnect);
			if(isConnect){
				break;
			}
			Thread.sleep(2000);
		}
		int i=0;
//		String a="1123";
//		String b="";
		while(true){
//			b=b+a;
//			if(b.length()>65535)b="";
//			if(false)break;
			try{
				System.out.println(cmd_two+"=======================");
				String ret = base.interaction(cmd_two,"GBK");//第二个参数为对方操作系统的语言编码
				System.out.println(ret);
				System.out.println(cmd_one+"=======================");
				ret = base.interaction(cmd_one,"GBK");//第二个参数为对方操作系统的语言编码
				System.out.println(ret);
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("循环处理不能被中断，所以这里要重建连接");
				base.close();
				while(true){
					System.out.println("无限次尝试，直到连接成功......");
					boolean isConnect = base.connect();
					System.out.println("connecting..."+isConnect);
					if(isConnect){
						break;
					}
					Thread.sleep(2000);
				}
			}
			i++;
			if(i>8640)break;
		}
		base.close();
	}
}
