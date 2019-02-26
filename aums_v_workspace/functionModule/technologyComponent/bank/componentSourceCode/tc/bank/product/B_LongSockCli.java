package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import tc.bank.utils.LongSockCli4ServerThread;
import tc.bank.utils.LongSockCliUtil;
import tc.bank.utils.StringUtil;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 对接长连接服务模块
 * 
 * @date 2018-04-17 13:45:12
 */
@ComponentGroup(level = "银行", groupName = "长连接通讯代理")
public class B_LongSockCli {

	/**
	 * @category 长连接客户端（用于服务端请求设备端）
	 * @param ar_ip_port_cfg
	 *            入参|长连接服务集群信息。格式为[ip:port,ip:port,......]|
	 *            {@link java.lang.String}
	 * @param reqStr
	 *            入参|请求报文|{@link java.lang.String}
	 * @param targetIp
	 *            入参|目标设备ip|{@link java.lang.String}
	 * @param rspStr
	 *            出参|响应报文|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "ar_ip_port_cfg", comment = "长连接服务集群信息。格式为[ip:port,ip:port,......]", type = java.lang.String.class),
			@Param(name = "reqStr", comment = "请求报文", type = java.lang.String.class),
			@Param(name = "targetIp", comment = "目标设备ip", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rspStr", comment = "响应报文", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "长连接客户端（用于服务端请求设备端）", style = "判断型", type = "同步组件", author = "唐韶东", date = "2018-04-17 01:48:42")
	public static TCResult B_longSockCli4Server(String ar_ip_port_cfg,
			String reqStr, String targetIp) {
		ArrayList<ArrayList<String>> arCfgList = LongSockCliUtil
				.arCfg2List(ar_ip_port_cfg);

		int ar_port = 0;
		String ar_ip = null;
		Socket sock = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		String uuid = UUID.randomUUID().toString();
		AppLogger.info("uuid:[" + uuid + "]");
		AppLogger.info("客户端ip:[" + targetIp + "]");
		String ip = StringUtil.fill(targetIp, 15, (byte) 0x20, 1);
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
			return TCResult
					.newFailureResult("BBBBBBB", "UnknownHostException！");
		} catch (IOException e) {
			AppLogger.info("IOException！");
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "IOException！");
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
		return TCResult.newSuccessResult(rspStr.substring(52));
	}

	/**
	 * @category 长连接客户端（用于设备端请求服务端）
	 * @param arc_port
	 *            入参|长连接客户端监听端口|{@link java.lang.String}
	 * @param reqStr
	 *            入参|请求报文|{@link java.lang.String}
	 * @param rspStr
	 *            出参|响应报文|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "arc_port", comment = "长连接客户端监听端口", type = java.lang.String.class),
			@Param(name = "reqStr", comment = "请求报文", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rspStr", comment = "响应报文", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "长连接客户端（用于设备端请求服务端）", style = "判断型", type = "同步组件", author = "唐韶东", date = "2018-04-17 04:28:29")
	public static TCResult B_longSockCli4Client(String arc_port, String reqStr) {
		int ar_port = Integer.parseInt(arc_port);
		Socket sock = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid:[" + uuid + "]");
		String ip = StringUtil.fill("1.1.1.1", 15, (byte) 0x20, 1);// StringUtils.fill(InetAddress.getLocalHost().getHostAddress(),15,(byte)0x20,1);
		System.out
				.println("客户端ip:[" + "1.1.1.1" + "]，客户端端口号:[" + ar_port + "]");
		byte[] reqBytes = ("0" + ip + uuid + reqStr).getBytes();
		String rspStr = null;
		try {
			long startTime = new Date().getTime();
			sock = new Socket("0.0.0.0", ar_port);
			dos = new DataOutputStream(sock.getOutputStream());
			dis = new DataInputStream(sock.getInputStream());
			dos.writeInt(reqBytes.length);
			dos.write(reqBytes);
			dos.flush();
			int len = dis.readInt();
			byte[] rspBytes = new byte[len];
			dis.readFully(rspBytes);
			long endTime = new Date().getTime();
			rspStr = new String(rspBytes);
			System.out.println("收到的应答,耗时:" + (endTime - startTime) + "毫秒，返回报文:"
					+ rspStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			AppLogger.info("UnknownHostException！");
			e.printStackTrace();
			return TCResult
					.newFailureResult("BBBBBBB", "UnknownHostException！");
		} catch (IOException e) {
			AppLogger.info("IOException！");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", "IOException！");
		}
		return TCResult.newSuccessResult(rspStr.substring(52));
	}

	/**
	 * @category 长连接客户端-批量（用于服务端请求设备端）
	 * @param ar_ip_port_cfg
	 *            入参|长连接服务集群信息。格式为[ip:port,ip:port,......]|
	 *            {@link java.lang.String}
	 * @param reqStr
	 *            入参|请求报文|{@link java.lang.String}
	 * @param targetIp
	 *            入参|目标设备ip列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param rspStrList
	 *            出参|响应报文列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "ar_ip_port_cfg", comment = "长连接服务集群信息。格式为[ip:port,ip:port,......]", type = java.lang.String.class),
			@Param(name = "reqStr", comment = "请求报文", type = java.lang.String.class),
			@Param(name = "targetIp", comment = "目标设备ip列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "rspStrList", comment = "响应报文列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "长连接客户端-批量（用于服务端请求设备端）", style = "判断型", type = "同步组件", author = "唐韶东", date = "2018-04-17 05:26:27")
	public static TCResult B_longSockCli4ServerBatch(String ar_ip_port_cfg,
			String reqStr, JavaList targetIp) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		for (int i = 0; i < targetIp.size(); i++) {
			results.add(exec.submit(new LongSockCli4ServerThread(
					ar_ip_port_cfg, (String) targetIp.get(i), reqStr)));
		}
		JavaList rspStrList = new JavaList();
		boolean findFlag = true;// 是否找到了尚未结束的线程，true：找到了，false：没找到。初始时，假定能找到，进入循环
		while (findFlag) {
			// 循环等待全部线程处理完毕
			findFlag = false;// 是否找到了尚未结束的线程，true：找到了，false：没找到
			for (int j = 0; j < results.size(); j++) {
				Future<String> fs = results.get(j);
				if (!fs.isDone()) {
					findFlag = true;
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
		// 获取所有线程的返回值，写入返回对象中
		for (int i = 0; i < results.size(); i++) {
			try {
				rspStrList.add(results.get(i).get());
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
		return TCResult.newSuccessResult(rspStrList);
	}

	/**
	 * @category 简化多维JavaList为1维JavaList
	 * @param inputList
	 *            入参|多维JavaList|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param key
	 *            入参|要提取的key|{@link java.lang.String}
	 * @param outputList
	 *            出参|1维的JavaList|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "inputList", comment = "多维JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "key", comment = "要提取的key", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "outputList", comment = "1维的JavaList", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "简化多维JavaList为1维JavaList", style = "判断型", type = "同步组件", comment = "比如将JavaList对象[{'DevId':'1','DevIp':'10.10.10.1','DevCashboxBrno':'5678'},{'DevId':'2','DevIp':'10.10.10.2','DevCashboxBrno':'5679'}]转化为JavaList对象：['10.10.10.1','10.10.10.2']", author = "唐韶东", date = "2018-04-19 04:29:07")
	public static TCResult B_SimplifyList(JavaList inputList, String key) {
		JavaList outputList =new JavaList();
		for(int i=0;i<inputList.size();i++){
			outputList.add(((JavaDict)inputList.get(i)).getStringItem(key));
		}
		return TCResult.newSuccessResult(outputList);
	}

}
