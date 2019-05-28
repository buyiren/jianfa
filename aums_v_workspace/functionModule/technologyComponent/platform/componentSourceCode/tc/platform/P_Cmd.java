package tc.platform;

import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.Param;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Return;
import cn.com.agree.afa.svc.javaengine.TCResult;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.Returns;

import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 命令处理类
 * @author 姚园-03958
 * @date 2015-12-16 18:14:56
 */
@ComponentGroup(level = "平台", groupName = "命令处理类")
public class P_Cmd {

	private static final String WINDOWS = "Windows";
	private static final String LINUX = "Linux";

	/**
	 * @category 获取操作系统类型
	 */
	private static String getOSType() {
		String osName = System.getProperty("os.name");
		if (osName.contains(WINDOWS)) {
			return WINDOWS;
		} else {
			return LINUX;
		}
	}

	/**
	 * @category Windows操作系统类型执行命令
	 * 
	 * @param cmd
	 *            入参|待执行命令|{@link java.lang.String}
	 * @param argList
	 *            入参|参数列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 */
	private static String[] generateCommand4Windows(String cmd, JavaList argList) {
		String[] commandWithArgs = new String[3 + argList.size()];
		commandWithArgs[0] = "cmd";
		commandWithArgs[1] = "/c";
		commandWithArgs[2] = cmd;
		String[] args = P_String.toStringArray(argList);
		System.arraycopy(args, 0, commandWithArgs, 3, args.length);
		return commandWithArgs;
	}

	/**
	 * @category Linux操作系统类型执行命令
	 * 
	 * @param cmd
	 *            入参|待执行命令|{@link java.lang.String}
	 * @param argList
	 *            入参|参数列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 */
	private static String[] generateCommand4Linux(String cmd, JavaList argList) {
		String[] commandWithArgs = new String[1 + argList.size()];
		commandWithArgs[0] = cmd;
		String[] args = P_String.toStringArray(argList);
		System.arraycopy(args, 0, commandWithArgs, 1, args.length);
		return commandWithArgs;
	}

	/**
	 * @category 获取windows本地ip地址
	 * 
	 */
	private static String getWinLocalIp() throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		String hostip = inet.toString();
		return hostip;
	}

	/**
	 * 
	 * @category 获取Linux本地IP地址
	 */
	private static String getUnixLocalIp() throws SocketException {
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface
				.getNetworkInterfaces();
		InetAddress ip = null;
		String hostip = "";
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) netInterfaces
					.nextElement();
			if (!ni.getName().equals("eth0")){
				continue;
			}
			else{
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()){
					ip = (InetAddress)addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						hostip =  ip.getHostAddress();
						break;
					} else {
						continue;
					}
				}
			}
		}
		return hostip;
	}

	/**
	 * @param path
	 *            入参|路径|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return1 失败<br/>
	 *          2 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "path", comment = "路径", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "创建路径", style = "判断型", type = "同步组件", comment = "创建递归的目录树，可以是相对或者绝对路径", author = "姚园-3958", date = "2015-12-16 06:14:58")
	public static TCResult P_MakeDirs(String path, String ver, String area) {
		try {
			if(ver==null&&area==null||ver.equals("V1.0.020130818")&&area.equals("PRD")){
				String filePath = path;
				filePath = filePath.toString();
				java.io.File myFilePath = new java.io.File(filePath);
				if (!myFilePath.exists()) {
					myFilePath.mkdir();
					return TCResult.newSuccessResult();
				} else {
					return TCResult.newFailureResult(ErrorCode.AGR, "目录已经存在，不需要创建");
				}
			}else{
				return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
			}
		} catch (Exception e) {
			return TCResult.newFailureResult("FBE099", "路径创建失败"+e.getMessage());
		}
	}

	/**
	 * @param cmd
	 *            入参|命令执行串|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param rs
	 *            出参|执行结果|{@link java.lang.String}
	 * @return1 失败<br/>
	 *          2 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "cmd", comment = "命令执行串", type = java.lang.String.class),
			@Param(name = "argList", comment = "参数列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rs", comment = "执行结果", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "命令执行器", style = "判断型", type = "同步组件", comment = "执行linux/unix命令,并返回执行结果", author = "姚园-3958", date = "2015-12-16 06:14:58")
	public static TCResult P_Cmd_Exec(String cmd, JavaList argList, String ver,
			String area) {
		Process process = null;
		try {
			if(ver==null&&area==null||ver.equals("V1.0.020130818")&&area.equals("PRD")){
				if (cmd == null || cmd.isEmpty()) {
					return TCResult
							.newFailureResult(ErrorCode.AGR, "入参参数列表参数非法，为空");
				}
				String osname = getOSType();
				if (osname == "") {
					process = Runtime.getRuntime().exec(
							generateCommand4Windows(cmd, argList));
				} else {
					process = Runtime.getRuntime().exec(
							generateCommand4Linux(cmd, argList));
				}

				int exitCode = process.waitFor();
				if (exitCode != 0) {
					return TCResult.newFailureResult(ErrorCode.HANDLING,
							"命令执行返回非零结果码:" + exitCode);
				}
				return TCResult.newSuccessResult();
			}else{
				return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
			}	
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				return TCResult.newFailureResult("FBE999", "执行命令异常:"+e.getMessage());
			}
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	/**
	 * @param __REQ__
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return1 失败<br/>
	 *          2 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "__REQ__", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取交易日志路径", style = "判断型", type = "同步组件", author = "姚园-3958", date = "2015-12-16 06:14:58")
	public static TCResult P_GetTradeLogPath(JavaDict __REQ__, String ver,
			String area) {
		try{
			if(ver==null&&area==null||ver.equals("V1.0.020130818")&&area.equals("PRD")){
				__REQ__.put("tradelogpath",System.getenv("HOME")+"/log/"+__REQ__.get("workdate")+"/"+__REQ__.get("__MC__")+"/"+__REQ__.get("__TC__"));
				return TCResult.newSuccessResult();
			}else{
				return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
			}
		}catch(Exception e){
			return TCResult.newFailureResult("FBE999", "获取交易日志路径异常:"+e.getMessage());
		}
	}

	/**
	 * @param ethname
	 *            入参|网卡名称|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param ip
	 *            出参|系统ip|{@link java.lang.String}
	 * @return1 失败<br/>
	 *          2 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "ethname", comment = "网卡名称", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "ip", comment = "系统ip", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取系统IP", style = "判断型", type = "同步组件", comment = "根据网卡名称，获取该网卡对应的ip，默认网卡名称为“eth0”", author = "姚园-3958", date = "2015-12-16 06:14:58")
	public static TCResult P_GetIp(String ethname, String ver, String area) {
		try{
			if(ver==null&&area==null||ver.equals("V1.0.020130818")&&area.equals("PRD")){
				String inet = null;
				String osname = getOSType();
				try {
					// window系统
					if (osname.equalsIgnoreCase("Windows XP")) {
						try {
							inet = getWinLocalIp();
						} catch (UnknownHostException e) {
							return TCResult
									.newFailureResult(ErrorCode.AGR, "主机的ip地址未知");
						}
						// linux系统
					} else if (osname.equalsIgnoreCase("Linux")) {
						inet = getUnixLocalIp();
					}
					if (null == inet) {
						return TCResult.newFailureResult(ErrorCode.AGR, "主机的ip地址未知");
					}
				} catch (SocketException e) {
					return TCResult.newFailureResult(ErrorCode.AGR,
							"获取本机ip错误" + e.getMessage());
				}
				return TCResult.newSuccessResult(inet);
			}else{
				return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
			}
		}catch(Exception e){
			return TCResult.newFailureResult("FBE999", "获取系统ip异常:"+e.getMessage());
		}
	}

	/**
	 * @param path
	 *            入参|路径|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return1 失败<br/>
	 *          2 不存在<br/>
	 *          3 存在<br/>
	 */
	@InParams(param = {
			@Param(name = "path", comment = "路径", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "不存在"), @Return(id = "2", desp = "存在") })
	@Component(label = "路径是否存在", style = "选择型", type = "同步组件", comment = "路径是否存在", author = "姚园-3958", date = "2015-12-16 06:14:58")
	public static TCResult P_IfDirsExist(String path, String ver, String area) {
		try {
			if(ver==null&&area==null||ver.equals("V1.0.020130818")&&area.equals("PRD")){
				String filePath = path;
				filePath = filePath.toString();
				java.io.File myFilePath = new java.io.File(filePath);
				if (!myFilePath.exists()) {
					return TCResult.newSuccessResult();
				} else {
					return new TCResult(2);
				}
			}else{
				return TCResult.newFailureResult("FBC000", "版本日期或适用区域错误,请联系版本管理员");
			}
		} catch (Exception e) {
			return TCResult.newFailureResult("FBE099", "判断失败"+e.getMessage());
		}
	}
}
