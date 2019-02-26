package tc.View.Agent;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;


/**
 * 调用各种系统命令
 * 
 * @date 2017-05-23 13:54:37
 */
@ComponentGroup(level = "应用", groupName = "系统命令调用组件", projectName = "AAAA", appName = "agent")
public class A_CommandProcesser {

	/**
	 * @category 判断abc是否正在运行
	 * @param runningFlag
	 *            出参|是否正在运行|boolean
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "runningFlag", comment = "是否正在运行", type = boolean.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "判断abc是否正在运行", style = "判断型", type = "同步组件", comment = "判断abc是否正在运行", author = "Anonymous", date = "2017-05-23 02:00:30")
	public static TCResult A_isABCRunning() {
		try {
			boolean ret = false;
			ProcessBuilder pb = new ProcessBuilder("tasklist");
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.getOutputStream().close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				if (tmp.trim().startsWith("abc.exe")) {
					System.out.println(tmp);
					ret = true;
					break;
				}
			}
			br.close();
			int exitValue = p.waitFor();
			return TCResult.newSuccessResult(ret);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 获取本机ip
	 * @param ip
	 *            出参|本机ip|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "ip", comment = "本机ip", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取本机ip", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-05-23 04:43:30")
	public static TCResult A_getHostAddress() {
		try{
			String ip = InetAddress.getLocalHost().getHostAddress();
			return TCResult.newSuccessResult(ip);
		}catch (IOException e){
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		
	}

}
