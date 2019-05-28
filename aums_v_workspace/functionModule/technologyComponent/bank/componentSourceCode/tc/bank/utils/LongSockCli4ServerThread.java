package tc.bank.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

public class LongSockCli4ServerThread implements Callable {
	String ar_ip_port_cfg=null;
	String targetIp=null;
	String reqStr=null;
	public LongSockCli4ServerThread(String ar_ip_port_cfg, String targetIp,
			String reqStr) {
		// TODO 自动生成的构造函数存根
		this.ar_ip_port_cfg=ar_ip_port_cfg;
		this.targetIp=targetIp;
		this.reqStr=reqStr;
	}

	@Override
	public String call() throws Exception {
		ArrayList<ArrayList<String>> arCfgList = LongSockCliUtil.arCfg2List(ar_ip_port_cfg);

		int ar_port = 0;
		String ar_ip = null;
		Socket sock = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		String uuid = UUID.randomUUID().toString();
		AppLogger.info("uuid:[" + uuid + "]");
		AppLogger.info("客户端ip:[" + targetIp + "]");
		String ip = StringUtil.fill(targetIp, 15, (byte) 0x20, 1);// StringUtils.fill(InetAddress.getLocalHost().getHostAddress(),15,(byte)0x20,1);
		byte[] reqByte = ("0" + ip + uuid + reqStr).getBytes();
		String rspStr = null;
		try {
			long startTime = new Date().getTime();
			while (arCfgList.size() > 0) {
				ArrayList<String> tmpList = arCfgList.remove(0);
				ar_ip = tmpList.get(0);
				ar_port = Integer.parseInt(tmpList.get(1));
				AppLogger.info("尝试连接到ar实例。ip:[" + ar_ip + "],port:[" + ar_port
						+ "]");
				try {
					sock = new Socket(ar_ip, ar_port);
					break;
				} catch (Exception e) {
					AppLogger.info("ar实例。ip:[" + ar_ip + "],port:[" + ar_port
							+ "]连接失败!");
				}
			}
			dos = new DataOutputStream(sock.getOutputStream());
			dis = new DataInputStream(sock.getInputStream());
			dos.writeInt(reqByte.length);
			dos.write(reqByte);
			dos.flush();
			int len = dis.readInt();
			byte[] rspByte = new byte[len];
			dis.readFully(rspByte);
			long endTime = new Date().getTime();
			rspStr = new String(rspByte);
			AppLogger.info("收到的应答,耗时:" + (endTime - startTime) + "毫秒，返回报文:"
					+ rspStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			AppLogger.info("UnknownHostException！");
			e.printStackTrace();
			return "BBBBBBB|UnknownHostException！";
		} catch (IOException e) {
			AppLogger.info("IOException！");
			e.printStackTrace();
			return "BBBBBBB|IOException！";
		} finally {
			try {
				dos.close();
			} catch (Exception e) {
			}
			try {
				dis.close();
			} catch (Exception e) {
			}
			try {
				sock.close();
			} catch (Exception e) {
			}
		}
		// TODO 自动生成的方法存根
		return StringUtil.fill(targetIp, 15, (byte)0x20, 1)+rspStr.substring(52);
	}

}
