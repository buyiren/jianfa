package tc.View.Agent;

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
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import cn.com.agree.afa.svc.javaengine.TCResult;
/**
 * @date 2018-07-21 10:34:45
 */
@ComponentGroup(level = "应用", groupName = "socket通信", projectName = "AUMS_Agent", appName = "Agent")
public class A_SocketAgent {


	/**
	 * @category socket通信
	 * @param flag
	 *            入参|0请求,1应答|{@link java.lang.String}
	 * @param AgentIP
	 *            入参|Agent客户端IP地址|{@link java.lang.String}
	 * @param uuid
	 *            入参|UUID|{@link java.lang.String}
	 * @param Json
	 *            入参|miniAFA请求json串|{@link java.lang.String}
	 * @param charSet
	 *            入参|字符集|{@link java.lang.String}
	 * @param Port
	 *            入参|端口号|{@link java.lang.String}
	 * @param Timeout
	 *            入参|超时时间|{@link java.lang.String}
	 * @param SocketIP
	 *            入参|socket端IP|{@link java.lang.String}
	 * @param reqstr
	 *            出参|miniAFA发起Socket请求的响应结果|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "flag", comment = "0请求,1应答", type = java.lang.String.class),
			@Param(name = "AgentIP", comment = "Agent客户端IP地址", type = java.lang.String.class),
			@Param(name = "uuid", comment = "UUID", type = java.lang.String.class),
			@Param(name = "Json", comment = "miniAFA请求json串", type = java.lang.String.class),
			@Param(name = "charSet", comment = "字符集", type = java.lang.String.class),
			@Param(name = "Port", comment = "端口号", type = java.lang.String.class),
			@Param(name = "Timeout", comment = "超时时间", type = java.lang.String.class),
			@Param(name = "SocketIP", comment = "socket端IP", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "reqstr", comment = "miniAFA发起Socket请求的响应结果", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "socket通信", style = "判断型", type = "同步组件", comment = "miniAFA通过socket发送定时消息到agentCli", date = "2018-08-05 04:05:56")
	public static TCResult A_socketAgent(String flag, String AgentIP,
			String uuid, String Json, String charSet, String Port,
			String Timeout, String SocketIP) {
		String rspStr = null;
		byte[] reqBytes = null;
		int strlength = 15;
		if (AgentIP.length() < strlength) {
			while (AgentIP.length() < strlength) {
				StringBuffer sb = new StringBuffer();
				sb.append(AgentIP).append(" ");
				AgentIP = sb.toString();
			}
		}
		Socket socket = null;
		System.out.println(AgentIP);
		try {
			socket = new Socket(SocketIP, Integer.parseInt(Port));
			socket.setSoTimeout(Integer.parseInt(Timeout) * 1000);
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());
			byte[] retByte = (flag + AgentIP + uuid + Json).getBytes(charSet);
			out.writeInt(retByte.length);
			out.write(retByte);
			out.flush();
			int len = in.readInt();
			reqBytes = new byte[len];
			in.readFully(reqBytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			rspStr = new String(reqBytes, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return TCResult.newSuccessResult(rspStr.substring(52));
	}

}
