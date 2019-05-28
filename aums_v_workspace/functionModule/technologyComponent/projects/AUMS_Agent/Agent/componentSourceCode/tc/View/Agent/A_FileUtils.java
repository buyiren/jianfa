package tc.View.Agent;

import fileTransTool.FileTransTool;
import fileTransTool.base.CommKeys;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.PrefixFileFilter;

import tc.View.Agent.utils.Base64;
import tc.View.Agent.utils.FileUtil;
import tc.View.Agent.utils.Md5CaculateUtil;
import tc.View.Agent.utils.ZipCompressing;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import cn.com.agree.afa.util.EncipherAndDecipherUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @date 2017-05-15 11:15:47
 */
@ComponentGroup(level = "应用", groupName = "高效文件处理组件", projectName = "AAAA", appName = "agent")
public class A_FileUtils {
	/**
	 * @category 对比代理端当前文件版本
	 * @param agentFileList
	 *            入参|服务端的文件名及md5的清单|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param localFileInfo
	 *            入参|代理端的文件名及md5的清单| {@link java.lang.String}
	 * @param fileNameList
	 *            出参|待更新文件名清单|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "agentFileList", comment = "服务端的文件名及md5的清单", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "localFileInfo", comment = "代理端的文件名及md5的清单", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "fileNameList", comment = "待更新文件名清单", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对比代理端当前文件版本", style = "判断型", type = "同步组件", comment = "对比传入的文件名清单和待比较的文件清单，将md5不一致以及代理端不存在的文件名，生成一个文件名清单返回。文件名不带路径，\n传入的代理端文件名清单格式例如：agent.heartBeat.jar:0b41ffab31fc84b3e5635fa15751c162|agent.logGatherer.jar:569d8acdc04e8da9962757b11f7537e3|agent.modCommonConf.jar:aa92eed8e3632d204b3d003e161d9002|agent.modConnectionUrl.jar:26765d459ef14d8d824ce5812aa0cba8|agent.watchLog.jar:1845eae27866f51896c325e3fbb6a33d|", author = "Anonymous", date = "2017-06-06 05:16:32")
	public static TCResult A_compareAgentFileList(JavaDict agentFileList,
			String localFileInfoStr) {
		JavaList result = new JavaList();
		try {
			String[] curLocalFilenameAndMD5 = localFileInfoStr.split("\\|");
			JSONObject localFileInfo = new JSONObject();
			for (int i = 0; i < curLocalFilenameAndMD5.length; i++) {
				String[] fileInfo = curLocalFilenameAndMD5[i].split(":");
				String fileName = fileInfo[0];
				String md5str = fileInfo[1];
				localFileInfo.put(fileName, md5str);
			}

			// String[] curFilenameAndMD5 = agentFileList.split("\\|");
			// AppLogger.info("curFilenameAndMD5 len:" +
			// curFilenameAndMD5.length);
			Object[] agentFileKeys = agentFileList.keySet().toArray();
			for (int i = 0; i < agentFileKeys.length; i++) {
				String fileName = (String) agentFileKeys[i];
				String md5str = agentFileList.getStringItem(agentFileKeys[i]);
				if (!localFileInfo.containsKey(fileName)) {
					result.add(fileName);
					continue;// 服务端若不存在，则添加
				}
				String newMd5str = localFileInfo.getString(fileName);
				if (!newMd5str.equals(md5str)) {
					// md5不相等，则加入更新列表
					AppLogger.info("md5比较不同！，[" + fileName + "]:[" + newMd5str
							+ "]:[" + md5str + "]");
					result.add(fileName);
				}
				localFileInfo.remove(fileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("HERS402", e);
		}
		if (result.size() == 0) {
			return TCResult.newFailureResult("HERS402", "没有需要更新版本的内容");
		}
		return TCResult.newSuccessResult(result);
	}

	/**
	 * 快速读取文件（占用少量内存）的方法
	 * 
	 * @author 唐韶东
	 * @param theFile
	 *            File 要读取的文件对象
	 * @param encode
	 *            String 文件编码，一般有"UTF-8"或"GB18030"、"GBK"等可选
	 * */
	private void commonsIO_demo(File theFile, String encode) throws IOException {
		LineIterator it = FileUtils.lineIterator(theFile, "UTF-8");
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				// do something with line
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}

	/**
	 * @category 修改abc的通讯配置
	 * @param parentConnectionUrl
	 *            入参|abs地址串|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "parentConnectionUrl", comment = "abs地址串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "修改abc的通讯配置", style = "判断型", type = "同步组件", comment = "修改abc.properties中的配置项：cn.com.agree.ab.communication/parentConnectionUrl", author = "Anonymous", date = "2017-05-19 05:22:16")
	public static TCResult A_modifyConnectionUrl(String parentConnectionUrl) {
		try {
			String abcPath = getABCPath();
			AppLogger.info("abcPath:" + abcPath);
			File abc_properties = new File(abcPath + "\\abc.properties");
			LineIterator it = null;
			it = FileUtils.lineIterator(abc_properties, "GB18030");

			String line = null;
			ArrayList<String> rows = new ArrayList<String>();
			boolean found = false;
			while (it.hasNext()) {
				line = it.nextLine();
				if (line.trim().startsWith(
						"cn.com.agree.ab.communication/parentConnectionUrl")) {
					rows.add("cn.com.agree.ab.communication/parentConnectionUrl = "
							+ parentConnectionUrl);
					found = true;
				} else {
					rows.add(line);
				}
				// do something with line
				System.out.println("line:" + line);
			}
			if (!found)
				rows.add("cn.com.agree.ab.communication/parentConnectionUrl = "
						+ parentConnectionUrl);
			FileUtils.writeLines(abc_properties, "GB18030", rows);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * 根据运行中的abc进程，读取abc.exe所在路径
	 * */
	private static String getABCPath() throws Exception {
		String path = null;
		try {
			File abcPath = new File("abc_path");
			if (abcPath.exists()) {
				LineIterator it = FileUtils.lineIterator(abcPath, "GB18030");
				ArrayList<String> rows = new ArrayList<String>();
				while (it.hasNext()) {
					rows.add(it.nextLine());
				}
				if (rows.size() > 0)
					return rows.get(0);
			}
			ProcessBuilder pb = new ProcessBuilder("wmic", "process");
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.getOutputStream().close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				tmp = tmp.trim();
				if (tmp.startsWith("abc.exe")) {
					// AppLogger.info("tmp:"+tmp);
					int idx = tmp.indexOf("\"");
					path = tmp.substring(idx + 1, tmp.indexOf("\"", idx + 1));
					path = path.substring(0, path.lastIndexOf("\\"));
					break;
				}
			}
			br.close();
			int exitValue = p.waitFor();
			if (path == null)
				throw new Exception("没有找到abc.exe进程");
			FileUtils.write(abcPath, path, "GB18030");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;

	}

	/**
	 * @category 修改abc配置通用组件
	 * @param configKey
	 *            入参|配置项|{@link java.lang.String}
	 * @param configValue
	 *            入参|配置值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "configKey", comment = "配置项", type = java.lang.String.class),
			@Param(name = "configValue", comment = "配置值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "修改abc配置通用组件", style = "判断型", type = "同步组件", comment = "根据指定的配置key和值，替换abc.properties中的相应内容", author = "Anonymous", date = "2017-05-22 10:34:02")
	public static TCResult A_modifyABCproperties(String configKey,
			String configValue) {
		try {
			String abcPath = getABCPath();
			AppLogger.info("abcPath:" + abcPath);
			File abc_properties = new File(abcPath + "\\abc.properties");
			LineIterator it = null;
			it = FileUtils.lineIterator(abc_properties, "GB18030");

			String line = null;
			ArrayList<String> rows = new ArrayList<String>();
			boolean found = false;
			while (it.hasNext()) {
				line = it.nextLine();
				if (line.trim().startsWith(configKey)) {
					rows.add(configKey + " = " + configValue);
					found = true;
				} else {
					rows.add(line);
				}
				// do something with line
				System.out.println("line:" + line);
			}
			if (!found)
				rows.add(configKey + " = " + configValue);
			FileUtils.writeLines(abc_properties, "GB18030", rows);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 读取日志内容
	 * @param fileName
	 *            入参|相对于abc安装目录的文件路径|{@link java.lang.String}
	 * @param logLevel
	 *            入参|日志级别|{@link java.lang.String}
	 * @param content
	 *            出参|日志内容|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "fileName", comment = "相对于abc安装目录的文件路径", type = java.lang.String.class),
			@Param(name = "logLevel", comment = "日志级别。0-全部，1-不包含debug，2-不包含debug/info，3-不包含debug/info/warn，4、不包含debug/info/warn/error，5、不包含debug/info/warn/error/fatal", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "content", comment = "日志内容", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取日志内容", style = "判断型", type = "同步组件", comment = "根据传入的参数，获取相关的日志内容", author = "Anonymous", date = "2017-05-22 02:15:12")
	public static TCResult A_getLog(String fileName, String logLevel) {
		try {
			int intLogLevel = Integer.parseInt(logLevel);
			String abcPath = getABCPath();
			AppLogger.info("abcPath:" + abcPath);
			File abcLog = new File(abcPath + "\\" + fileName);
			LineIterator it = null;
			it = FileUtils.lineIterator(abcLog, "GB18030");

			String line = null;
			StringBuffer rows = new StringBuffer();
			boolean found = false;
			int lineNumber = 0;
			while (it.hasNext()) {
				lineNumber++;
				line = it.nextLine();
				if (line.indexOf("Exception") >= 0
						|| line.trim().startsWith("at ")) {
					rows.append(lineNumber + ":" + line + "\n");
				} else if (intLogLevel < 1) {// 全部
					rows.append(lineNumber + ":" + line + "\n");
				} else if (intLogLevel < 2) {// 不包含debug
					if (line.indexOf("[INFO]") >= 0
							|| line.indexOf("[WARN]") >= 0
							|| line.indexOf("[ERROR]") >= 0
							|| line.indexOf("[FATAL]") >= 0) {
						rows.append(lineNumber + ":" + line + "\n");
					}
				} else if (intLogLevel < 3) {// 不包含debug/info
					if (line.indexOf("[WARN]") >= 0
							|| line.indexOf("[ERROR]") >= 0
							|| line.indexOf("[FATAL]") >= 0) {
						rows.append(lineNumber + ":" + line + "\n");
					}
				} else if (intLogLevel < 4) {// 不包含debug/info/warn
					if (line.indexOf("[ERROR]") >= 0
							|| line.indexOf("[FATAL]") >= 0) {
						rows.append(lineNumber + ":" + line + "\n");
					}
				} else if (intLogLevel < 5) {// 不包含debug/info/warn
					if (line.indexOf("[FATAL]") >= 0) {
						rows.append(lineNumber + ":" + line + "\n");
					}
				}
			}
			return TCResult.newSuccessResult(rows.toString());
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}

	}

	/**
	 * @category base64转为文件
	 * @param zipFilePathName
	 *            入参|zip文件名|{@link java.lang.String}
	 * @param parent
	 *            入参|目标路径|{@link java.lang.String}
	 * @param base64Str
	 *            入参|文件内容的Base64编码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "parent", comment = "zip文件名", type = java.lang.String.class),
			@Param(name = "base64Str", comment = "文件内容的Base64编码", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "base64转为文件", style = "判断型", type = "同步组件", comment = "传入zip文件的base64编码,写入指定的文件中", author = "Anonymous", date = "2017-06-01 02:15:06")
	public static TCResult A_base64ToFile(String zipFilePathName,
			String base64Str) {
		// BASE64Decoder decoder = new BASE64Decoder();
		if (base64Str.equals(""))
			return TCResult.newSuccessResult();
		// Decoder decoder = Base64.getDecoder();
		try {
			// "workspace\\java\\projects\\AAAA\\apps\\agent\\trade\\update"
			FileOutputStream fos = new FileOutputStream(new File(updatePath
					+ "\\" + zipFilePathName));
			// fos.write(new BASE64Decoder().decodeBuffer(base64Str));
			fos.write(Base64.decode(base64Str.getBytes()));
			fos.flush();
			fos.close();
		} catch (IOException e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 更新代理端版本号
	 * @param currentVer
	 *            入参|版本号|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "currentVer", comment = "版本号", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "更新代理端版本号", style = "判断型", type = "同步组件", comment = "根据传入的版本号，写入ver.cfg文件", author = "Anonymous", date = "2017-06-01 04:36:45")
	public static TCResult A_setCurrentVer(String currentVer) {
		try {
			File abcLog = new File("conf\\ver.cfg");
			FileUtils.writeStringToFile(abcLog, currentVer, "GB10830");
			return TCResult.newSuccessResult();
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}

	/**
	 * @category 获取abc安装路径
	 * @param abc_path
	 *            出参|abc安装路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "abc_path", comment = "abc安装路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取abc安装路径", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-06 08:50:19")
	public static TCResult A_getABC_Path() {
		String path;
		try {
			path = getABCPath();
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult(path);
	}

	/**
	 * @category 读取本地交易清单
	 * @param tradeList
	 *            出参|交易文件名清单|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "tradeList", comment = "交易文件名清单", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取本地交易清单", style = "判断型", type = "同步组件", comment = "返回用|分隔的文件名清单，不带路径", author = "Anonymous", date = "2017-06-06 08:56:06")
	public static TCResult A_getTradeList() {
		StringBuffer fileList = new StringBuffer();
		try {
			Collection<File> listFiles = FileUtils.listFiles(new File(
					".\\workspace\\java\\projects\\AAAA\\apps\\agent\\trade"),
					new String[] { "jar" }, false);
			for (File file : listFiles) {
				fileList.append(file.getName());
				fileList.append(":");
				fileList.append(Md5CaculateUtil.getHash(file, "MD5"));
				fileList.append("|");
			}
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult(fileList.toString());
	}

	// private final static String updatePath = "D:\\Self";
	// private final static String updatePath = ".\\workspace\\java";
	private final static String updatePath = System.getProperty("afa.home");

	/**
	 * @category 读取本地文件清单
	 * @param tradeList
	 *            出参|本地文件清单|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "tradeList", comment = "交易文件名清单", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取本地文件清单", style = "判断型", type = "同步组件", comment = "遍历代理端安装目录下workspace子目录下的所有文件，返回用|分隔的文件名清单，含相对路径名", author = "Anonymous", date = "2017-06-06 08:56:06")
	public static TCResult A_getLocalFileList() {
		StringBuffer fileList = new StringBuffer();
		try {
			Collection<File> listFiles = FileUtil.listInDirectory(updatePath);
			AppLogger.info("找到了文件个数:" + listFiles.size());
			for (File file : listFiles) {
				fileList.append((file.getPath().substring(file.getPath()
						.lastIndexOf(updatePath) + updatePath.length() + 1))
						.replaceAll("\\\\", "/"));
				fileList.append(":");
				fileList.append(Md5CaculateUtil.getHash(file, "MD5"));
				fileList.append("|");
			}
			AppLogger.info("return:" + fileList.toString());
			// System.out.println(fileList.toString());
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult(fileList.toString());
	}

	/**
	 * @category 读取本地技术组件清单
	 * @param tradeList
	 *            出参|技术组件文件名清单|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = { @Param(name = "tradeList", comment = "交易文件名清单", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取技术组件清单", style = "判断型", type = "同步组件", comment = "返回用|分隔的文件名清单，不带路径", author = "Anonymous", date = "2017-06-06 08:56:06")
	public static TCResult A_getComponentList() {
		StringBuffer fileList = new StringBuffer();
		try {
			Collection<File> listFiles = FileUtils
					.listFiles(
							new File(
									".\\workspace\\java\\projects\\AAAA\\apps\\agent\\tech_component"),
							new String[] { "jar" }, false);
			for (File file : listFiles) {
				fileList.append(file.getName());
				fileList.append(":");
				fileList.append(Md5CaculateUtil.getHash(file, "MD5"));
				fileList.append("|");
			}
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult(fileList.toString());
	}

	/**
	 * @category 解压交易zip包
	 * @param parent
	 *            入参|目标路径|{@link java.lang.String}
	 * @param zipFileName
	 *            入参|zip文件名及路径|{@link java.lang.String}
	 * @param fileNameList
	 *            出参|解压出来的文件名清单|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "parent", comment = "目标路径", type = java.lang.String.class),
			@Param(name = "zipFileName", comment = "zip文件名及路径", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "fileNameList", comment = "解压出来的文件名清单", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "解压交易zip包", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-07 02:12:08")
	public static TCResult A_unzipTrade(String parent, String zipFileName) {
		// parent:"./workspace/java/"
		// jarFilePathName:"../../temp/update_file.jar"
		try {
			// String
			// cmd=System.getProperty("afa.home")+"\\"+"bin\\unpackUpdpackage.bat "+System.getProperty("afa.home")+"\\"+parent+" "+System.getProperty("afa.home")+"\\"+jarFilePathName;
			// AppLogger.info("compress command : ["+cmd+"]");
			// Runtime.getRuntime().exec(cmd);
			ZipCompressing.unzip(
					System.getProperty("afa.home") + "\\" + parent,
					System.getProperty("afa.home") + "\\" + zipFileName);
			return TCResult.newSuccessResult(new JavaList());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return TCResult.newSuccessResult(new JavaList());
		// return TCResult.newSuccessResult(ZipCompressing.unzip(parent,
		// zipFileName));
	}

	/**
	 * @category 读取C端机具Agent配置
	 * @param keyList
	 *            入参|参数名列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param propDict
	 *            出参|参数集合|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "keyList", comment = "参数名列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "propDict", comment = "参数集合", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取C端机具Agent配置", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-08 11:37:04")
	public static TCResult A_getCproxy_conf(JavaList keyList) {
		try {
			JavaDict ret = new JavaDict();
			InputStream in = new BufferedInputStream(new FileInputStream(
					System.getProperty("afa.home") + "/conf/cproxy.conf"));
			Properties p = new Properties();
			p.load(in);
			for (int i = 0; i < keyList.size(); i++) {
				ret.put((String) keyList.get(i), (String) p.get(keyList.get(i)));
			}
			return TCResult.newSuccessResult(ret);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e);
		} catch (IOException e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}

	/**
	 * @category 清理本地日志
	 * @param remain_count
	 *            入参|保留天数|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "remain_count", comment = "保留天数", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "清理本地日志", style = "判断型", type = "同步组件", comment = "检索./log/app/agent/下的所有子目录，子目录名如20170608，保留最近3天的内容，其他都删掉", author = "Anonymous", date = "2017-06-08 02:14:42")
	public static TCResult A_cleanLog(int remain_count) {
		ArrayList<String> dayList = new ArrayList<String>();
		Date dt = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		for (int i = 0; i < remain_count; i++) {
			dayList.add(sdf.format(rightNow.getTime()));
			rightNow.add(Calendar.DAY_OF_YEAR, -1);// (在刚才的结果上)减天
		}
		System.out.println("dayList:" + dayList.toString());
		File[] dirs = new File(System.getProperty("afa.home")
				+ "\\log\\app\\agent").listFiles();
		for (int i = 0; i < dirs.length; i++) {
			if (dayList.contains(dirs[i].getName()))
				continue;
			// System.out.println("delete this subdir:" + dirs[i].getName());
			try {
				FileUtils.deleteDirectory(dirs[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// deleteAllFilesOfDir(dirs[i]);

		}
		return TCResult.newSuccessResult();
	}

	// public static void deleteAllFilesOfDir(File path) {
	// if (!path.exists())
	// return;
	// if (path.isFile()) {
	// path.delete();
	// return;
	// }
	// File[] files = path.listFiles();
	// for (int i = 0; i < files.length; i++) {
	// deleteAllFilesOfDir(files[i]);
	// }
	// path.delete();
	// }

	/**
	 * @category 列出目录下所有文件和文件夹
	 * @param path
	 *            入参|绝对路径|{@link java.lang.String}
	 * @param recursion
	 *            入参|是否递归|{@link java.lang.String}
	 * @param result
	 *            出参|目录内容| {@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "path", comment = "绝对路径", type = java.lang.String.class),
			@Param(name = "recursion", comment = "是否递归", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "result", comment = "目录内容", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "列出目录下所有文件和文件夹", style = "判断型", type = "同步组件", comment = "路径为空则可列出所有可读可见盘符", author = "Anonymous", date = "2017-06-09 09:07:50")
	public static TCResult A_listDir(String path,String recursion) {
		JSONObject result = FileUtil.listAllDir(path, recursion);// 初次调用时，path为“./”，ret为{}
		return TCResult.newSuccessResult(result.toJSONString());
	}

	/**
	 * @category 提取文件内容
	 * @param path
	 *            入参|绝对路径|{@link java.lang.String}
	 * @param filename
	 *            入参|文件名|{@link java.lang.String}
	 * @param content
	 *            出参|文件内容|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "path", comment = "绝对路径", type = java.lang.String.class),
			@Param(name = "filename", comment = "文件名", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "content", comment = "文件内容的base64", type = java.lang.String.class),
			@Param(name = "isCompressed", comment = "是否需要解压缩.true:是，false:否", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "提取文件内容",style = "判断型", type = "同步组件", comment = "path结尾不能有正斜线或反斜线.支持文件或文件夹，如果是文件夹，则在文件夹所在位置生成相应的压缩包，返回压缩包的base64", author = "Anonymous", date = "2017-06-09 01:31:16")
	public static TCResult A_grabFileContent(String path, String filename) {
		try {
			if(path.startsWith("./")||path.startsWith(".\\"))path=path.substring(2);
			while(path.startsWith("\\")||path.startsWith("/"))path=path.substring(1);
			String absolutePath = path+"\\"+filename;
			File tmpFile=new File(absolutePath);
			if(tmpFile.isFile()){
				return TCResult.newSuccessResult(new String(Base64.encode(FileUtils
					.readFileToByteArray(tmpFile))),"false");
			}else{
				String basePath=absolutePath.replaceAll("\\\\", "/");
				basePath = basePath.substring(0,basePath.lastIndexOf("/")+1);
				String dirName = absolutePath.substring(basePath.lastIndexOf("/")+1);
				String uuid=UUID.randomUUID().toString();
				String zipFileName = basePath + uuid + ".zip";
				ZipCompressing.zip(zipFileName, absolutePath);
				File jarFile = new File(zipFileName);
				String ret = new String(Base64.encode(FileUtils
						.readFileToByteArray(jarFile)));
				jarFile.delete();
				return TCResult.newSuccessResult(ret, "true");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}


	/**
	 * @category 重写abc端的文件
	 * @param abc_path
	 *            入参|abc安装路径|{@link java.lang.String}
	 * @param path
	 *            入参|文件相对路径|{@link java.lang.String}
	 * @param content
	 *            入参|文件内容|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "abc_path", comment = "abc安装路径", type = java.lang.String.class),
			@Param(name = "path", comment = "文件相对路径", type = java.lang.String.class),
			@Param(name = "content", comment = "文件内容", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "重写abc端的文件", style = "判断型", type = "同步组件", comment = "以全量覆盖的方式更新某文件的内容", author = "Anonymous", date = "2017-06-09 01:49:02")
	public static TCResult A_replaceAbcFileContent(String abc_path,
			String path, String content) {
		try {
			// FileUtils.writeByteArrayToFile(new File(abc_path + "\\" + path),
			// new BASE64Decoder().decodeBuffer(content));
			AppLogger.info("write file to[" + abc_path + "\\" + path + "]");
			if(path==null || "".equals(path)){
				FileUtils.writeByteArrayToFile(new File(abc_path ),
						Base64.decode(content.getBytes()));
			}else{
				FileUtils.writeByteArrayToFile(new File(abc_path + "\\" + path),
						Base64.decode(content.getBytes()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("HERS012", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category aaa
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 * 
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "aaa", style = "选择型", type = "同步组件", author = "Anonymous", date = "2017-06-26 04:44:04")
	public static TCResult A_aaaa() {
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 采集外设日志
	 * @param count
	 *            出参|错误个数|int
	 * @param groupLineNoList
	 *            出参|所在行号列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param errInfoList
	 *            出参|错误信息列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param md5List
	 *            出参|错误信息MD5列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = {
			@Param(name = "count", comment = "错误个数", type = int.class),
			@Param(name = "groupLineNoList", comment = "所在行号列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "errInfoList", comment = "错误信息列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "md5List", comment = "错误信息MD5列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "采集外设日志", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-27 09:42:04")
	public static TCResult A_gather1DeviceLog() {
		try {
			String abcPath = getABCPath();
			AppLogger.info("abcPath:" + abcPath);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			long startLine = 0;// 开始行号，从0开始
			long lastSize = 0;
			Date nowDate = new Date();
			String lastDate = sdf.format(nowDate);
			File deviceLogLock = new File(".\\temp\\deviceLog.lck");// 文件内容为：日期、文件大小、截止行号
			if (deviceLogLock.exists()) {
				LineIterator it1 = FileUtils.lineIterator(deviceLogLock,
						"GB18030");
				String href = it1.nextLine();
				String[] lock = href.split("\\|");
				lastDate = lock[0];
				lastSize = Long.parseLong(lock[1]);
				startLine = Long.parseLong(lock[2]);
			}
			String curDate = sdf.format(nowDate);
			if (!curDate.equals(lastDate)) {
				// 换日了，从第一行开始读了
				startLine = 0;
			}
			// 读取策略要求的额外关键字配置
			Properties pps = new Properties();
			pps.load(new FileInputStream("conf\\hers.conf"));
			String keywords = (String) pps.get("device_exception_keywords");
			String[] keyword = {};
			if (keywords.length() > 0)
				keyword = keywords.split("\\|");
			File deviceLog = new File(abcPath + "\\log\\device.log");
			LineIterator it = null;
			long curSize = FileUtils.sizeOf(deviceLog);
			it = FileUtils.lineIterator(deviceLog, "GB18030");
			String line = null;
			StringBuffer rows = new StringBuffer();
			long lineNumber = -1;
			while (it.hasNext()) {
				lineNumber++;
				line = it.nextLine();
				// AppLogger.info("line:"+lineNumber+",content:"+line);
				if (lineNumber <= startLine) {
					continue;
				}
				if (line.indexOf("Exception") >= 0
						|| line.trim().startsWith("at ")
						|| line.indexOf(" ERROR ") >= 0
						|| line.indexOf(" FATAL ") >= 0) {
					rows.append((lineNumber + 1) + ":" + line + "\n");
					continue;
				}
				// 按策略要求进一步查找
				for (int i = 0; i < keyword.length; i++) {
					if (line.indexOf(keyword[i]) >= 0) {
						rows.append((lineNumber + 1) + ":" + line + "\n");
						break;
					}
				}
			}
			String allExceptions = rows.toString();
			AppLogger.info("采集到的内容:" + allExceptions);
			FileUtils.writeStringToFile(deviceLogLock, curDate + "|" + curSize
					+ "|" + lineNumber, "GB18030");
			String pattern1 = "\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3}";
			Pattern p1 = Pattern.compile(pattern1);
			String pattern2 = " pool-\\d{1,5}-thread-\\d{1,5} ";
			Pattern p2 = Pattern.compile(pattern2);
			JavaList errInfoList = new JavaList();
			JavaList groupLineNoList = new JavaList();
			// 下面对取出来的rows进行分组，行号连续的内容都认为是同组异常，一起上报
			if (allExceptions.length() > 0) {
				// 找到了异常信息
				String[] lines = allExceptions.split("\n");
				long preLineNum = 0;
				StringBuffer errInfo = new StringBuffer();
				String groupLineNo = "";
				for (int i = 0; i < lines.length; i++) {
					long tmpLineNum = Long.parseLong(lines[i].substring(0,
							lines[i].indexOf(":")));
					if (i == 0) {
						// 第一行，preLineNum初始化
						preLineNum = tmpLineNum - 1;
						groupLineNo = lines[i].substring(0,
								lines[i].indexOf(":"));
						groupLineNoList.add(groupLineNo);
						AppLogger.info("当前组所在行:" + groupLineNo);
					}
					// AppLogger.info("preLineNum:"+preLineNum+",tmpLineNum:"+tmpLineNum+",content:"+lines[i]);
					if (preLineNum + 1 < tmpLineNum) {
						// 另起一组
						AppLogger.info("当前组内容:" + errInfo.toString());
						errInfoList.add(errInfo.toString());
						errInfo = new StringBuffer();
						groupLineNo = lines[i].substring(0,
								lines[i].indexOf(":"));
						groupLineNoList.add(groupLineNo);
						AppLogger.info("当前组所在行:" + groupLineNo);

					}
					// 替换
					Matcher m = p1.matcher(lines[i]);
					if (m.find()) {
						AppLogger.info("find[" + m.group(0) + "]");
						AppLogger.info("替换src[" + lines[i] + "],by ["
								+ pattern1 + "]");
						lines[i] = lines[i].replaceFirst(pattern1,
								"nn 某月 20xx nn:nn:nn,nnn");
					}
					m = p2.matcher(lines[i]);
					if (m.find()) {
						AppLogger.info("find[" + m.group(0) + "]");
						AppLogger.info("替换src[" + lines[i] + "],by ["
								+ pattern2 + "]");
						lines[i] = lines[i].replaceFirst(pattern2,
								" pool-n-thread-n ");
					}
					// 去掉行号
					lines[i] = lines[i].substring(lines[i].indexOf(":") + 1);
					AppLogger.info("append[" + lines[i] + "]");
					errInfo.append(lines[i] + "\n");
					preLineNum = tmpLineNum;
				}
				if (errInfo.length() > 0) {
					AppLogger.info("当前组内容:" + errInfo.toString());
					errInfoList.add(errInfo.toString());
				}
				AppLogger.info("groupLineNoList:" + groupLineNoList.toString()
						+ "\nerrInfoList:" + errInfoList.toString());
				// 分组完成，下面开始向hers服务端上报异常
				// TODO
			}
			if (groupLineNoList.size() == 0) {
				return TCResult.newFailureResult("AAAAAAA", "无记录");
			}
			JavaList md5List = new JavaList();
			for (int i = 0; i < errInfoList.size(); i++) {
				md5List.add(Md5CaculateUtil.getHash(errInfoList
						.getStringItem(i)));
			}
			return TCResult.newSuccessResult(groupLineNoList.size(),
					groupLineNoList, errInfoList, md5List);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}

	/**
	 * @category 采集abc外设日志
	 * @param count
	 *            出参|错误个数|int
	 * @param groupLineNoList
	 *            出参|所在行号列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param errInfoList
	 *            出参|错误信息列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param md5List
	 *            出参|错误信息MD5列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@OutParams(param = {
			@Param(name = "count", comment = "错误个数", type = int.class),
			@Param(name = "groupLineNoList", comment = "所在行号列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "errInfoList", comment = "错误信息列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "md5List", comment = "错误信息MD5列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "采集abc外设日志", style = "选择型", type = "同步组件", author = "Anonymous", date = "2017-06-27 09:46:35")
	public static TCResult A_gatherDeviceLog() {
		try {
			String abcPath = getABCPath();
			AppLogger.info("abcPath:" + abcPath);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			long startLine = 0;// 开始行号，从0开始
			long lastSize = 0;
			Date nowDate = new Date();
			String lastDate = sdf.format(nowDate);
			File deviceLogLock = new File(".\\temp\\deviceLog.lck");// 文件内容为：日期、文件大小、截止行号
			if (deviceLogLock.exists()) {
				LineIterator it1 = FileUtils.lineIterator(deviceLogLock,
						"GB18030");
				String href = it1.nextLine();
				String[] lock = href.split("\\|");
				lastDate = lock[0];
				lastSize = Long.parseLong(lock[1]);
				startLine = Long.parseLong(lock[2]);
			}
			String curDate = sdf.format(nowDate);
			if (!curDate.equals(lastDate)) {
				// 换日了，从第一行开始读了
				startLine = 0;
			}
			// 读取策略要求的额外关键字配置
			Properties pps = new Properties();
			pps.load(new FileInputStream("conf\\hers.conf"));
			String keywords = (String) pps.get("device_exception_keywords");
			String[] keyword = {};
			if (keywords.length() > 0)
				keyword = keywords.split("\\|");
			File deviceLog = new File(abcPath + "\\log\\device.log");
			LineIterator it = null;
			long curSize = FileUtils.sizeOf(deviceLog);
			it = FileUtils.lineIterator(deviceLog, "GB18030");
			String line = null;
			StringBuffer rows = new StringBuffer("");
			long lineNumber = -1;
			while (it.hasNext()) {
				lineNumber++;
				line = it.nextLine();
				// AppLogger.info("line:"+lineNumber+",content:"+line);
				if (lineNumber <= startLine) {
					continue;
				}
				if (line.indexOf("Exception") >= 0
						|| line.trim().startsWith("at ")
						|| line.indexOf(" ERROR ") >= 0
						|| line.indexOf(" FATAL ") >= 0) {
					rows.append((lineNumber + 1) + ":" + line + "\n");
					continue;
				}
				// 按策略要求进一步查找
				for (int i = 0; i < keyword.length; i++) {
					if (line.indexOf(keyword[i]) >= 0) {
						rows.append((lineNumber + 1) + ":" + line + "\n");
						break;
					}
				}
			}
			String allExceptions = rows.toString();
			if (allExceptions.length() == 0)
				return TCResult.newFailureResult("AAAAAAA",
						"device.log文件无变化.星期六12:37！");
			AppLogger.info("采集到的内容:" + allExceptions);
			FileUtils.writeStringToFile(deviceLogLock, curDate + "|" + curSize
					+ "|" + lineNumber, "GB18030");
			String pattern1 = "\\d{1,2} [\\u4e00-\\u9fa5]{2,3} \\d{4} \\d{2}:\\d{2}:\\d{2},\\d{3}";
			Pattern p1 = Pattern.compile(pattern1);
			String pattern2 = "pool-\\d{1,10}-thread-\\d{1,10}";
			Pattern p2 = Pattern.compile(pattern2);
			JavaList errInfoList = new JavaList();
			JavaList groupLineNoList = new JavaList();
			// 下面对取出来的rows进行分组，行号连续的内容都认为是同组异常，一起上报
			if (allExceptions.length() > 0) {
				// 找到了异常信息
				String[] lines = allExceptions.split("\n");
				long preLineNum = 0;
				StringBuffer errInfo = new StringBuffer();
				String groupLineNo = "";
				for (int i = 0; i < lines.length; i++) {
					long tmpLineNum = Long.parseLong(lines[i].substring(0,
							lines[i].indexOf(":")));
					if (i == 0) {
						// 第一行，preLineNum初始化
						preLineNum = tmpLineNum - 1;
						groupLineNo = lines[i].substring(0,
								lines[i].indexOf(":"));
						groupLineNoList.add(groupLineNo);
						AppLogger.info("当前组所在行:" + groupLineNo);
					}
					// AppLogger.info("preLineNum:"+preLineNum+",tmpLineNum:"+tmpLineNum+",content:"+lines[i]);
					if (preLineNum + 1 < tmpLineNum) {
						// 另起一组
						AppLogger.info("当前组内容:" + errInfo.toString());
						errInfoList.add(errInfo.toString());
						errInfo = new StringBuffer();
						groupLineNo = lines[i].substring(0,
								lines[i].indexOf(":"));
						groupLineNoList.add(groupLineNo);
						AppLogger.info("当前组所在行:" + groupLineNo);

					}
					// 替换
					Matcher m = p1.matcher(lines[i]);
					if (m.find()) {
						AppLogger.info("find[" + m.group(0) + "]");
						AppLogger.info("替换src[" + lines[i] + "],by ["
								+ pattern1 + "]");
						lines[i] = lines[i].replaceFirst(pattern1,
								"nn 某月 20xx nn:nn:nn,nnn");
					}
					m = p2.matcher(lines[i]);
					if (m.find()) {
						AppLogger.info("find[" + m.group(0) + "]");
						AppLogger.info("替换src[" + lines[i] + "],by ["
								+ pattern2 + "]");
						lines[i] = lines[i].replaceFirst(pattern2,
								"pool-n-thread-n");
					}
					// 去掉行号
					lines[i] = lines[i].substring(lines[i].indexOf(":") + 1);
					AppLogger.info("append[" + lines[i] + "]");
					errInfo.append(lines[i] + "\n");
					preLineNum = tmpLineNum;
				}
				if (errInfo.length() > 0) {
					AppLogger.info("当前组内容:" + errInfo.toString());
					errInfoList.add(errInfo.toString());
				}
				AppLogger.info("groupLineNoList:" + groupLineNoList.toString()
						+ "\nerrInfoList:" + errInfoList.toString());
				// 分组完成，下面开始向hers服务端上报异常
				// TODO
			}
			if (groupLineNoList.size() == 0) {
				return TCResult.newFailureResult("AAAAAAA", "无记录");
			}
			JavaList md5List = new JavaList();
			for (int i = 0; i < errInfoList.size(); i++) {
				md5List.add(Md5CaculateUtil.getHash(errInfoList
						.getStringItem(i)));
			}
			return TCResult.newSuccessResult(groupLineNoList.size(),
					groupLineNoList, errInfoList, md5List);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}

	/**
	 * @category 获取当前柜员号
	 * @param abc_path
	 *            出参|abc安装路径|{@link java.lang.String}
	 * @param loginName
	 *            出参|柜员名称|{@link java.lang.String}
	 * @param windowTitle
	 *            出参|窗口标题|{@link java.lang.String}
	 * @param absId
	 *            出参|abs实例名|{@link java.lang.String}
	 * @param brno
	 *            出参|机构号|{@link java.lang.String}
	 * @param zone
	 *            出参|分行号|{@link java.lang.String}
	 * @param orgName
	 *            出参|机构名称|{@link java.lang.String}
	 * @param userName
	 *            出参|柜员姓名|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "abc_path", comment = "abc安装路径", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "loginName", comment = "柜员名称", type = java.lang.String.class),
			@Param(name = "windowTitle", comment = "窗口标题", type = java.lang.String.class),
			@Param(name = "absId", comment = "abs实例名", type = java.lang.String.class),
			@Param(name = "brno", comment = "机构号", type = java.lang.String.class),
			@Param(name = "zone", comment = "分行号", type = java.lang.String.class),
			@Param(name = "orgName", comment = "机构名称", type = java.lang.String.class),
			@Param(name = "userName", comment = "柜员姓名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取当前柜员号", style = "判断型", type = "同步组件", comment = "获取当前柜员号", author = "Anonymous", date = "2017-06-30 01:28:28")
	public static TCResult A_getLoginInfo(String abc_path) {
		try {
			String filePath = abc_path + "\\hers\\login.properties";
			File loginInfoFile = new File(filePath);
			if (!loginInfoFile.exists()) {
				AppLogger.info(filePath + ",文件不存在");
				return TCResult.newSuccessResult("", "", "", "", "", "", "");
			}
			AppLogger.info(filePath + ",文件找到了");
			LineIterator it = FileUtils.lineIterator(loginInfoFile, "GB18030");
			String userno = "";
			String windowTitle = "";
			String absId = "";
			String brno = "";
			String zone = "";
			String orgName = "";
			String userName = "";
			while (it.hasNext()) {
				String line = it.nextLine();
				if (line.startsWith("userno="))
					userno = line.length() == "userno=".length() ? "" : line
							.substring("userno=".length());
				else if (line.startsWith("windowTitle="))
					windowTitle = line.length() == "windowTitle=".length() ? ""
							: line.substring("windowTitle=".length());
				else if (line.startsWith("absId="))
					absId = line.length() == "absId=".length() ? "" : line
							.substring("absId=".length());
				else if (line.startsWith("orgNo="))
					brno = line.length() == "orgNo=".length() ? "" : line
							.substring("orgNo=".length());
				else if (line.startsWith("G_BRID="))
					zone = line.length() == "G_BRID=".length() ? "" : line
							.substring("G_BRID=".length());
				else if (line.startsWith("orgName="))
					orgName = line.length() == "orgName=".length() ? "" : line
							.substring("orgName=".length());
				else if (line.startsWith("userName="))
					userName = line.length() == "userName=".length() ? ""
							: line.substring("userName=".length());
				// do something with line
			}
			// InputStream in = new BufferedInputStream(new FileInputStream(
			// filePath));
			// Properties p = new Properties();
			// p.load(in);
			// AppLogger.info("文件内容:"+p.keySet().toString());
			// AppLogger.info("contains userno(key):"+p.containsKey("userno"));
			// AppLogger.info("contains userno:"+p.contains("userno"));
			//
			// String userno =
			// p.containsKey("userno")?p.getProperty("userno"):"";
			// String windowTitle =
			// p.containsKey("windowTitle")?p.getProperty("windowTitle"):"";
			// windowTitle = new String(windowTitle.getBytes("GB18030"));
			AppLogger.info("userno:" + userno);
			AppLogger.info("windowTitle:" + windowTitle);
			return TCResult.newSuccessResult(userno, windowTitle, absId, brno,
					zone, orgName, userName);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}

	}

	/**
	 * @category 判断是否需要更新策略
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2异常<br/>
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "判断是否需要更新策略", style = "判断型", type = "同步组件", comment = "读取conf下的updatePolicy.lck文件，判断是否需要更新。需要则返回成功，否则返回失败。", author = "Anonymous", date = "2017-07-01 04:05:43")
	public static TCResult A_checkIfNeedUpdatePolicy() {
		String filePath = "conf\\updatePolicy.lck";
		try {
			File lckFile = new File(filePath);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
			String sysDate = sdf.format(new Date());
			if (!lckFile.exists()) {
				// FileUtils.writeStringToFile(lckFile, sysDate, "utf-8");
				return TCResult.newSuccessResult();
			}
			LineIterator it = FileUtils.lineIterator(lckFile, "utf-8");
			if (it.hasNext()) {
				String date = it.nextLine();
				if (date.equals(sysDate))
					return new TCResult(2, "BBBBBBB", "策略不需要进行更新");
			}
			return TCResult.newSuccessResult();
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}

	}

	/**
	 * @category 修改策略更新锁文件
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "修改策略更新锁文件", style = "判断型", type = "同步组件", comment = "修改conf下的updatePolicy.lck，将里面内容改为最新的标志", author = "Anonymous", date = "2017-07-01 04:18:29")
	public static TCResult A_updatePolicyLckFile() {
		String lckFile = "conf\\updatePolicy.lck";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		String sysDate = sdf.format(new Date());
		try {
			FileUtils.writeStringToFile(new File(lckFile), sysDate, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 修改hers策略配置通用组件
	 * @param configContent
	 *            入参|配置信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "configContent", comment = "配置信息.字典类型", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "修改hers策略配置通用组件", style = "判断型", type = "同步组件", comment = "根据指定的配置key和值，替换 conf\\hers.conf 中的相应内容", author = "Anonymous", date = "2017-05-22 10:34:02")
	public static TCResult A_modifyHersConf(JavaDict configContent) {
		try {
			File agent_conf = new File("conf\\hers.conf");
			LineIterator it = null;
			it = FileUtils.lineIterator(agent_conf, "UTF-8");

			String line = null;
			ArrayList<String> rows = new ArrayList<String>();
			while (it.hasNext()) {
				line = it.nextLine();
				if (line.indexOf("=") <= 0) {// 没有等号的行，一概跳过
					rows.add(line);
					continue;
				}
				String lineKey = line.substring(0, line.indexOf("=")).trim();
				if (configContent.containsKey(lineKey)) {
					rows.add(lineKey + "="
							+ configContent.getStringItem(lineKey));
					configContent.removeItem(lineKey);
				} else {
					rows.add(line);
				}
				// do something with line
			}
			Object[] keys = configContent.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				rows.add(keys[i] + "=" + configContent.getStringItem(keys[i]));
			}
			FileUtils.writeLines(agent_conf, "UTF-8", rows);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 修改abc的交易录制策略配置通用组件
	 * @param configContent
	 *            入参|配置信息|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "configContent", comment = "配置信息.字典类型", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "修改abc的交易录制策略配置通用组件", style = "判断型", type = "同步组件", comment = "根据指定的配置key和值，替换 abc_path\\hers\\trade.properties 中的相应内容", author = "Anonymous", date = "2017-05-22 10:34:02")
	public static TCResult A_abcTradeProperties(JavaDict configContent) {
		try {
			String abc_path = getABCPath();
			File agent_conf = new File(abc_path + "\\hers\\trade.properties");
			if (!agent_conf.exists()) {
				// 文件不存在，则直接创建
				StringBuffer content = new StringBuffer("");
				Object[] keys = configContent.keySet().toArray();
				// ArrayList<String> rows = new ArrayList<String>();
				for (int i = 0; i < keys.length; i++) {
					// rows.add(keys[i] + "=" +
					// configContent.getStringItem(keys[i]));
					content.append(keys[i] + "="
							+ configContent.getStringItem(keys[i]));
					content.append("\n");
				}
				// FileUtils.writeLines(agent_conf,"GB18030", rows);
				FileUtils.writeStringToFile(agent_conf, content.toString(),
						"GB18030");
				return TCResult.newSuccessResult();
			}
			LineIterator it = null;
			it = FileUtils.lineIterator(agent_conf, "GB18030");

			String line = null;
			// ArrayList<String> rows = new ArrayList<String>();
			StringBuffer content = new StringBuffer("");
			while (it.hasNext()) {
				line = it.nextLine();
				if (line.indexOf("=") <= 0) {// 没有等号的行，一概跳过
					// rows.add(line);
					content.append(line);
					content.append("\n");
					continue;
				}
				String lineKey = line.substring(0, line.indexOf("=")).trim();
				if (configContent.containsKey(lineKey)) {
					// rows.add(lineKey + "=" +
					// configContent.getStringItem(lineKey));
					content.append(lineKey + "="
							+ configContent.getStringItem(lineKey));
					content.append("\n");
					configContent.removeItem(lineKey);
				} else {
					// rows.add(line);
					content.append(line);
					content.append("\n");

				}
				// do something with line
			}
			Object[] keys = configContent.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				// rows.add(keys[i] + "=" +
				// configContent.getStringItem(keys[i]));
				content.append(keys[i] + "="
						+ configContent.getStringItem(keys[i]));
				content.append("\n");
			}
			// FileUtils.writeLines(agent_conf,"GB18030", rows);
			FileUtils.writeStringToFile(agent_conf, content.toString(),
					"GB18030");
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 压缩文件夹
	 * @param basePath
	 *            入参|base文件夹路径-压缩文件夹的父路径| {@link java.lang.String}
	 * @param path
	 *            入参|文件夹路径-被压缩的子目录,多个子目录|分隔| {@link java.lang.String}
	 * @param zipFilePathName
	 *            入参|压缩包路径-压缩包的绝对路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "basePath", comment = "base文件夹路径-压缩文件夹的父路径(最后一个路径符号不带)", type = java.lang.String.class),
			@Param(name = "path", comment = "文件夹路径-被压缩的子目录,多个子目录|分隔", type = java.lang.String.class),
			@Param(name = "zipFilePathName", comment = "压缩包路径-压缩包的绝对路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "压缩文件夹", style = "判断型", type = "同步组件", comment = "按文件夹压缩", author = "Anonymous", date = "2017-07-20 03:19:52")
	public static TCResult A_zipFiles(String basePath, String path,String zipFilePathName) {
		ArrayList<String> fileList = new ArrayList<String>();
		String[] fileDir = path.split("\\|");
		for(int i=0;i<fileDir.length;i++){
			String singlePath = fileDir[i];
			Collection<File> listFiles = FileUtils.listFilesAndDirs(new File(basePath+"\\"+singlePath),
					new PrefixFileFilter("", IOCase.INSENSITIVE),
					new PrefixFileFilter("", IOCase.INSENSITIVE));
			for (File file : listFiles) {
				if (file.isFile()) {
					fileList.add(file.getAbsolutePath());
				}
			}
		}
		try {
			ZipCompressing.zip(basePath, zipFilePathName, fileList);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 压缩文件列表
	 * @param fileNameList
	 *            入参|文件列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param zipFilePathName
	 *            入参|压缩包路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "fileNameList", comment = "文件列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "zipFilePathName", comment = "压缩包名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "压缩文件列表", style = "判断型", type = "同步组件", comment = "按文件压缩", author = "Anonymous", date = "2017-07-20 03:19:52")
	public static TCResult A_zipFile(JavaList fileNameList,
			String zipFilePathName) {
		ArrayList<String> fileList = new ArrayList<String>();
		for (int i = 0; i < fileNameList.size(); i++) {
			String fileName = (String) fileNameList.get(i);
			File file = new File(fileName);
			if (file.isFile()) {
				System.out.println("add:" + fileName);
				fileList.add(file.getAbsolutePath());
			} else {
				diGuiFolder(fileName, fileList);
			}
		}
		try {
			ZipCompressing.zip(zipFilePathName, fileList);
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult();
	}

	public static void diGuiFolder(String path, ArrayList list) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹为空");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						diGuiFolder(file2.getAbsolutePath(), list);
					} else {
						list.add(file2.getAbsolutePath());
					}
				}
			}
		}
	}


	/**
	 * @category 获取屏幕截图
	 * @param filePath
	 *            入参|文件夹路径| {@link java.lang.String}
	 * @param fileName
	 *            入参|文件名| {@link java.lang.String}
	 * @param fileType
	 *            入参|文件类型|{@link java.lang.String}
	 * @param AFAPath
	 *            出参|AFA路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "filePath", comment = "文件夹路径", type = java.lang.String.class),
			@Param(name = "fileName", comment = "文件名", type = java.lang.String.class),
			@Param(name = "fileType", comment = "文件类型", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "AFAPath", comment = "AFAPath", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取屏幕截图", style = "判断型", type = "同步组件", comment = "获取屏幕截图，支持jpg、png等格式,", author = "Anonymous", date = "2017-10-16 19:19:52")
	public static TCResult A_captureScreen(String filePath, String fileName,
			String fileType) {
		String AFAPath = System.getProperty("afa.home");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRectangle = new Rectangle(screenSize);
		try {
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRectangle);
			// 保存路径
			File screenFile = new File(AFAPath + "\\" + filePath);
			if (!screenFile.exists()) {
				screenFile.mkdir();
			}
			File f = new File(screenFile, fileName);
			ImageIO.write(image, fileType, f);
		} catch (AWTException e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		} catch (IOException e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult(AFAPath);
	}

	/**
	 * @category 密码解密
	 * @param password
	 *            入参|加密密码| {@link java.lang.String}
	 * @param decodeType
	 *            入参|加密类型|{@link java.lang.String}
	 * @param origPwd
	 *            出参|解密后密码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "password", comment = "加密密码", type = java.lang.String.class),
			@Param(name = "decodeType", comment = "加密类型", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "origPwd", comment = "解密后密码", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "密码解密", style = "判断型", type = "同步组件", comment = "支持3DES、BASE64解密", author = "Anonymous", date = "2017-10-19 14:19:52")
	public static TCResult A_decodePassword(String password, String decodeType) {
		String origPwd = "";
		if ("3des".equalsIgnoreCase(decodeType)) {
			origPwd = EncipherAndDecipherUtils.decode3Des(password);
		} else if ("base64".equalsIgnoreCase(decodeType)) {
			origPwd = EncipherAndDecipherUtils.decodeBase64(password);
		} else {
			return TCResult.newFailureResult("BBBBBBB", "不支持的解密类型");
		}
		return TCResult.newSuccessResult(origPwd);
	}

	/**
	 * 文件同步下载
	 * 
	 * @param localFilePath
	 *            本地文件下载路径
	 * @param remoteFilePath
	 *            远程文件路径
	 * @param ip
	 *            文件服务器ip
	 * @param port
	 *            文件服务器端口
	 * @return 成功返回 success，失败抛出异常
	 * @throws Exception
	 *             传输过程中出现异常，或文件服务器连接数超限等原因连接失败
	 */
	@InParams(param = {
			@Param(name = "localFilePath", comment = "本地路径", type = java.lang.String.class),
			@Param(name = "remoteFilePath", comment = "服务器端路径", type = java.lang.String.class),
			@Param(name = "ip", comment = "IP地址", type = java.lang.String.class),
			@Param(name = "port", comment = "端口号", type = java.lang.Integer.class)}
			)
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "文件下载", style = "判断型", type = "同步组件", comment = "文件下载", author = "Anonymous", date = "2017-10-19 14:19:52")
	public static TCResult fileDownloadSyn(String localFilePath,
			String remoteFilePath, String ip, int port) throws Exception {

		String res = "";
		try {

			Map dataMap = new HashMap();
			dataMap.put(CommKeys.FILE_CHECK_FLAG, "false");
			long start = System.currentTimeMillis();

			res = FileTransTool.fileDownloadSyn(updatePath+"\\"+localFilePath, remoteFilePath,
					ip, port, dataMap);

			long end = System.currentTimeMillis();

			AppLogger.info("下载文件：" + remoteFilePath + " 耗时（ms）:"
					+ (end - start));

		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", e);

		}

		return "success".equalsIgnoreCase(res) ? TCResult.newSuccessResult(res)
				: TCResult.newFailureResult("BBBBBBB", res);
	}
	
	/**
	 * @param fileName
	 *            入参|要写入的文件名|{@link java.lang.String}
	 * @param strToFile
	 *            入参|写入文件的字符串列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param fAutoSkipLine
	 *            入参|1:列表换行写入，0：列表不换行|{@link int}
	 * @param charset
	 *            入参|文件字符集编码名称，如果传入为null则使用默认编码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 * @使用范例 :
	 * @example :writeContent("D:/test.txt",["111"],1,null)
	 * @example :writeContent("D:/test.txt",["111"],0,null)
	 * @example :writeContent("D:/test.txt",["111","2333"],1,null)
	 */
	@InParams(param = {
			@Param(name = "fileName", comment = "要写入的文件名", type = java.lang.String.class),
			@Param(name = "strToFile", comment = "写入文件的字符串列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "fAutoSkipLine", comment = "1:列表换行写入，0：列表不换行", type = int.class),
			@Param(name = "charset", comment = "文件字符集编码名称，如果传入为null则使用默认编码", type = java.lang.String.class) })
	@OutParams(param = {})
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "文件内容写入", style = "判断型", type = "同步组件", comment = "文件存不追加写入", date = "Thu Jul 16 16:15:30 CST 2015")
	public static TCResult writeContent(String fileName, JavaList strToFile,
			int fAutoSkipLine, String charset) {
		if (strToFile == null) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数strToFile不能为null");
		}
		
		if (fileName == null||fileName.equals("")) {
			return TCResult.newFailureResult(ErrorCode.AGR, "参数fileName不能为null或空字符串");
		}
		if (System.getProperty("os.name").toUpperCase()
				.contains("windows".toUpperCase())) {
			if (!(fileName.contains(":"))) {
				if (fileName.charAt(0) == '/') {
					fileName = System.getenv("HOME").replace('\\', '/')
							+ fileName;
				} else {
					fileName = System.getenv("HOME").replace('\\', '/') + "/"
							+ fileName;
				}
			}
		} else {
			if (System.getProperty("os.name").toUpperCase()
					.contains("Linux".toUpperCase())) {
				if (fileName.charAt(0) != '/') {
					fileName = System.getenv("HOME") + "/" + fileName;
				}
			}
		}
		File file = new File(fileName);
		BufferedWriter bufout = null;
		boolean append = false;
		if (charset == null) {
			charset = Charset.defaultCharset().toString();
		}
		/*if (file.exists()) {
			append = true;
		}*/
		try {
			bufout = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append), charset));
			for (Object item : strToFile) {
				String info = (String) item;
				bufout.write(info);
				if (fAutoSkipLine == 1)
					bufout.newLine();
			}
			bufout.flush();
			return TCResult.newSuccessResult();
		} catch (Exception e) {
			return TCResult.newFailureResult(ErrorCode.FILECTL,
					e);
		} finally {
			if (bufout != null) {
				try {
					bufout.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
				bufout = null;
			}
		}
	}
   public static void main(String[] args) throws Exception{
	   
	   fileDownloadSyn("C:\\tmp\\h2DB.mv.db","/home/afa4sj/agent-update/AIM_Agent/workspace/h2DB.mv.db","32.114.67.43",5566);
	   
   }
}
