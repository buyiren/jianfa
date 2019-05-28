package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;

import tc.View.Server.utils.Base64;
import tc.View.Server.utils.FileUtil;
import tc.View.Server.utils.Md5CaculateUtil;
import tc.View.Server.utils.ZipCompressing;
import cn.com.agree.afa.jcomponent.DBConnProvider;
import cn.com.agree.afa.svc.javaengine.AppLogger;
//import sun.misc.BASE64Encoder;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * @date 2017-06-01 15:37:35
 */
@ComponentGroup(level = "应用", groupName = "文件处理类", projectName = "AAAA", appName = "server")
public class A_FileUtils {
	/**
	 * @category 二进制文件编码为base64
	 * @param filePathName
	 *            入参|含路径的文件名|{@link java.lang.String}
	 * @param base64Str
	 *            出参|文件内容的Base64编码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "filePathName", comment = "含路径的文件名", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "base64Str", comment = "文件内容的Base64编码", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "二进制文件编码为base64", style = "判断型", type = "同步组件", comment = "传入文件名，编码为base64", author = "Anonymous", date = "2017-06-01 02:15:06")
	public static TCResult A_encodingFileToBase64(String filePathName) {
		// BASE64Encoder encoder = new BASE64Encoder();
		// Encoder encoder = Base64.getEncoder();
		try {
			byte[] b = getContent(filePathName);
			// String ret = new String(new BASE64Encoder().encode(b));
			String ret = new String(Base64.encode(b));
			return TCResult.newSuccessResult(ret);
		} catch (IOException e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}

	/* 读取文件内容到byte[] */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			throw new IOException("文件超长...");
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		fi.close();
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		return buffer;
	}

	// /**
	// * @category 对比代理端当前交易版本
	// * @param agentFileList
	// * 入参|代理端的文件名及md5的清单|{@link java.lang.String}
	// * @param fileNameList
	// * 出参|待更新文件名清单|
	// * {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	// * @return 0 失败<br/>
	// * 1 成功<br/>
	// */
	// @InParams(param = { @Param(name = "agentFileList", comment =
	// "代理端的文件名及md5的清单", type = java.lang.String.class) })
	// @OutParams(param = { @Param(name = "fileNameList", comment = "待更新文件名清单",
	// type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	// @Returns(returns = { @Return(id = "0", desp = "失败"),
	// @Return(id = "1", desp = "成功") })
	// @Component(label = "对比代理端当前交易版本", style = "判断型", type = "同步组件", comment =
	// "对比传入的文件名清单和本地update/trade子目录下的文件名清单，将md5不一致以及代理端不存在的文件名，生成一个文件名清单返回。文件名不带路径，默认从./update下面（不递归子目录）读取本地文件清单加以对比。\n传入的代理端文件名清单格式例如：agent.heartBeat.jar:0b41ffab31fc84b3e5635fa15751c162|agent.logGatherer.jar:569d8acdc04e8da9962757b11f7537e3|agent.modCommonConf.jar:aa92eed8e3632d204b3d003e161d9002|agent.modConnectionUrl.jar:26765d459ef14d8d824ce5812aa0cba8|agent.watchLog.jar:1845eae27866f51896c325e3fbb6a33d|",
	// author = "Anonymous", date = "2017-06-06 05:16:32")
	// public static TCResult A_compareAgentTradeList(String agentFileList) {
	// JavaList result = new JavaList();
	// // 先列出本地update目录下所有的jar文件
	// Collection<File> listFiles = FileUtils.listFiles(new File(
	// "./update/trade"), new String[] { "jar" }, false);
	// ArrayList<String> localFileList = new ArrayList<String>();
	// for (File file : listFiles) {
	// localFileList.add(file.getName());
	// }
	// try {
	// String[] curFilenameAndMD5 = agentFileList.split("\\|");
	// AppLogger.info("curFilenameAndMD5 len:" + curFilenameAndMD5.length);
	// for (int i = 0; i < curFilenameAndMD5.length; i++) {
	// String[] fileInfo = curFilenameAndMD5[i].split(":");
	// String fileName = fileInfo[0];
	// String md5str = fileInfo[1];
	// if (!localFileList.contains(fileName))
	// continue;// 服务端若不存在，则跳过
	// File tmpFile = new File("./update/trade/" + fileName);
	// String newMd5str = Md5CaculateUtil.getHash(tmpFile, "MD5");
	// if (!newMd5str.equals(md5str)) {
	// // md5不相等，则加入更新列表
	// result.add(fileName);
	// }
	// localFileList.remove(fileName);
	// }
	// // localFileList中剩下的fileName，属于新增的交易，也要加入更新列表
	// for (int i = 0; i < localFileList.size(); i++) {
	// result.add((String) localFileList.get(i));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return TCResult.newFailureResult("BBBBBBB", e);
	// }
	//
	// return TCResult.newSuccessResult(result);
	// }

	/**
	 * @category 对比代理端当前文件版本
	 * @param agentFileList
	 *            入参|代理端的文件名及md5的清单|{@link java.lang.String}
	 * @param localFileInfo
	 *            入参|服务端的文件名及md5的清单|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param fileNameList
	 *            出参|待更新文件名清单|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "agentFileList", comment = "代理端的文件名及md5的清单", type = java.lang.String.class),
			@Param(name = "localFileInfo", comment = "服务端的文件名及md5的清单", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@OutParams(param = { @Param(name = "fileNameList", comment = "待更新文件名清单", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "对比代理端当前文件版本", style = "判断型", type = "同步组件", comment = "对比传入的文件名清单和待比较的文件清单，将md5不一致以及代理端不存在的文件名，生成一个文件名清单返回。文件名不带路径，\n传入的代理端文件名清单格式例如：agent.heartBeat.jar:0b41ffab31fc84b3e5635fa15751c162|agent.logGatherer.jar:569d8acdc04e8da9962757b11f7537e3|agent.modCommonConf.jar:aa92eed8e3632d204b3d003e161d9002|agent.modConnectionUrl.jar:26765d459ef14d8d824ce5812aa0cba8|agent.watchLog.jar:1845eae27866f51896c325e3fbb6a33d|", author = "Anonymous", date = "2017-06-06 05:16:32")
	public static TCResult A_compareAgentFileList(String agentFileList,
			JavaDict localFileInfo) {
		JavaList result = new JavaList();
		try {

			String[] curFilenameAndMD5 = agentFileList.split("\\|");
			AppLogger.info("curFilenameAndMD5 len:" + curFilenameAndMD5.length);
			for (int i = 0; i < curFilenameAndMD5.length; i++) {
				String[] fileInfo = curFilenameAndMD5[i].split(":");
				String fileName = fileInfo[0];
				String md5str = fileInfo[1];
				if (!localFileInfo.containsKey(fileName))
					continue;// 服务端若不存在，则跳过
				String newMd5str = localFileInfo.getStringItem(fileName);
				if (!newMd5str.equals(md5str)) {
					// md5不相等，则加入更新列表
					AppLogger.info("md5比较不同！，[" + fileName + "]:[" + newMd5str
							+ "]:[" + md5str + "]");
					result.add(fileName);
				}
				localFileInfo.removeItem(fileName);
			}
			// localFileList中剩下的fileName，属于新增的交易，也要加入更新列表
			Object[] keys = localFileInfo.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				result.add((String) keys[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e);
		}

		return TCResult.newSuccessResult(result);
	}

	/**
	 * @category 计算agent文件的MD5
	 * @param path
	 *            入参|遍历的路径|{@link java.lang.String}
	 * @param fileInfoDict
	 *            出参|文件信息的字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "path", comment = "遍历的路径,如./update/trade，./update/component", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "fileInfoDict", comment = "文件信息的字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "计算agent文件的MD5", style = "判断型", type = "同步组件", comment = "遍历入餐指定的目录下文件，计算出MD5值，返回dict对象。格式如{'projects/AAAA/apps/server/tech_component/agent.modConnectionUrl.jar':'9bbf55cf4fd4b37189cdaf1098678f2d','projects/AAAA/apps/server/tech_component/agent.watchLog.jar':'110c71f506c594a0110189a082458106'}", author = "Anonymous", date = "2017-06-13 09:00:20")
	public static TCResult A_createFileMD5(String path) {
		// JavaList result = new JavaList();
		JavaDict ret = new JavaDict();
		// 先列出本地update目录下所有的jar文件
		File tmp = new File(".");
		AppLogger.info("当前路径:" + tmp.getAbsolutePath());
		ArrayList<File> listFiles = FileUtil.listInDirectory(path);
		try {
			for (File file : listFiles) {
				ret.put(file.getPath().substring(
						file.getPath().lastIndexOf("agent-update/")
								+ "agent-update/".length()),
						Md5CaculateUtil.getHash(file, "MD5"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e);
		}
		return TCResult.newSuccessResult(ret);
	}

	/**
	 * @category 交易文件打包zip组件
	 *@param rootPath
	 *            入参|文件根目录|{@link java.lang.String}
	 * @param zipFileName
	 *            入参|zip文件名|{@link java.lang.String}
	 * @param fileList
	 *            入参|文件名列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param zipFilePathName
	 *            出参|zip文件名及路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "rootPath", comment = "文件根目录", type = java.lang.String.class),
			@Param(name = "zipFileName", comment = "zip文件名", type = java.lang.String.class),
			@Param(name = "fileList", comment = "文件名列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "zipFilePathName", comment = "zip文件名及路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "交易文件打包zip组件", style = "判断型", type = "同步组件", comment = "输入文件名列表（JavaList）,相对路径在./agent-update/下，然后打包成zip格式", author = "Anonymous", date = "2017-06-07 12:55:06")
	public static TCResult A_packZip(String rootPath,String zipFileName, JavaList fileList) {
		// zipFileName:"trade_"+__REQ__["BODY"]["abc_ip"]+".jar"
		//
		
		String RootPath=System.getProperty("user.home") + "/"+rootPath+"/";
		try {
			
//			ZipCompressing book = new ZipCompressing();
			ArrayList<String> fileList2 = new ArrayList<String>();
			for (int i = 0; i < fileList.size(); i++) {
				AppLogger.info("zip file:" + fileList.getStringItem(i));
				fileList2.add(RootPath+"/agent-update/" + fileList.getStringItem(i));
			}
//			try {
//				book.zip("./temp/" + zipFileName, fileList2);
//			} catch (Exception e) {
//				// 
//				e.printStackTrace();
//				AppLogger.info("压缩异常:" + e.getMessage());
//				return TCResult.newFailureResult("BBBBBBB", e.getMessage());
//			}
			try {
				//"jar cfM temp/trade_22.5.22.250.jar -C ./agent-update projects/AAAA/apps/agent/tech_component/tc.app.AAAA.agent-1.0.jar -C ./agent-update projects";
				ZipCompressing.zip(RootPath+"/temp/"+zipFileName, fileList2,RootPath+"/agent-update/");
//				StringBuffer cmd=new StringBuffer("jar cfM temp/" + zipFileName);
//				for(int i=0;i<fileList.size();i++){
//					cmd.append(" -C ./agent-update ");
//					cmd.append(fileList.get(i));
//				}
//				AppLogger.info("compress command : ["+cmd.toString()+"]");
//				Process process= Runtime.getRuntime().exec(cmd.toString());
//				BufferedReader br = new BufferedReader(new InputStreamReader(
//						process.getInputStream()));
//				String tmp = null;
//				while ((tmp = br.readLine()) != null) {
//					AppLogger.info("ret["+tmp+"]");
//				}
//				br.close();
//				int exitValue = process.waitFor();
//				AppLogger.info("process exit["+exitValue+"]");
//				
				
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
				return TCResult.newFailureResult("HERS404", e1.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("HERS405", e.getMessage());
		}
		return TCResult.newSuccessResult(RootPath+"/temp/"+zipFileName);
	}

	/**
	 * @category 组件文件打包zip组件
	 * @param zipFileName
	 *            入参|zip文件名|{@link java.lang.String}
	 * @param fileList
	 *            入参|文件名列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param zipFilePathName
	 *            出参|zip文件名及路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "zipFileName", comment = "zip文件名", type = java.lang.String.class),
			@Param(name = "fileList", comment = "文件名列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "zipFilePathName", comment = "zip文件名及路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "组件文件打包zip组件", style = "判断型", type = "同步组件", comment = "输入文件名列表（JavaList），然后打包成zip格式", author = "Anonymous", date = "2017-06-07 12:55:06")
	public static TCResult A_packComponentZip(String zipFileName,
			JavaList fileList) {
		// "zip -pj z.zip ./update/a.txt ./update/tmp/b.txt"
		try {
			ZipCompressing book = new ZipCompressing();
			ArrayList<String> fileList2 = new ArrayList<String>();
			for (int i = 0; i < fileList.size(); i++) {
				fileList2
						.add("./update/component/" + fileList.getStringItem(i));
			}
			try {
				book.zip("./temp/" + zipFileName, fileList2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB", e.getMessage());
		}
		return TCResult.newSuccessResult("./temp/" + zipFileName);
	}

	/**
	 * @category 清理本地日志
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "清理本地日志", style = "判断型", type = "同步组件", comment = "检索./log/app/下的所有子目录，子目录名如20170608，保留最近3天的内容，其他都删掉", author = "Anonymous", date = "2017-06-08 02:14:42")
	public static TCResult A_cleanLog() {
		//读取配置
		Statement stmt=null;
//		HashMap<String,String> remainDaysMap = new HashMap<String,String>();
		ArrayList<String> cleanList = new ArrayList<String>();
		try{
			Connection conn = DBConnProvider.getConnection("pcva");
			stmt = conn.createStatement();
			String sqlStr = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='cleanLogFile' and paramkeyname='remain_count'";		
			AppLogger.info("sql:" + sqlStr);
			ResultSet rs1 = stmt.executeQuery(sqlStr);
			while(rs1.next()){
//				remainDaysMap.put(rs1.getString(1),rs1.getString(2));
				cleanList.add(rs1.getString(1));
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
			return TCResult.newFailureResult("BBBBBBB",
					"处理失败." + e.getMessage());
		} finally {
			if (stmt != null)try {stmt.close();} catch (Exception e) {}
		}
	
		ArrayList<String> dayList = new ArrayList<String>();
		Date dt = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		int remain_count=3;//默认保留3天
		if(cleanList.size()==1){
			remain_count=Integer.parseInt(cleanList.get(1));
		}
		for (int i = 0; i < remain_count; i++) {
			dayList.add(sdf.format(rightNow.getTime()));
			rightNow.add(Calendar.DAY_OF_YEAR, -1);// (在刚才的结果上)减天
		}
		AppLogger.info("dayList:" + dayList.toString());
		File[] dirs = new File("./log/app").listFiles();
		for (int i = 0; i < dirs.length; i++) {
			if (dayList.contains(dirs[i].getName()))
				continue;
			try {
				FileUtils.deleteDirectory(dirs[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try{
			File startupLog = new File("./log/server/startup.log");
			FileUtils.writeStringToFile(startupLog, "","UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		//清理temp目录
//		remain_count=1;//默认保留30天
//		if(remainDaysMap.containsKey("3"))remain_count=Integer.parseInt(remainDaysMap.get("3"));
//		Collection<File> listFiles = FileUtils.listFilesAndDirs(new File(
//				"./temp"),
//				new PrefixFileFilter("",IOCase.INSENSITIVE), new PrefixFileFilter("",IOCase.INSENSITIVE));
//		long curTime = dt.getTime();
//		long remainTime = 24*60*60*1000*remain_count;
//		for (File file : listFiles) {
//			if(file.isFile()){
//				long modTime = file.lastModified();
//				if((curTime-modTime)>=remainTime)file.delete();
//			}
//		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 根据MD5值读取文件内容
	 * @param md5
	 *            入参|文件md5值|{@link java.lang.String}
	 * @param fileContent
	 *            出参|文件内容|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "md5", comment = "文件md5值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "fileContent", comment = "文件内容", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "根据MD5值读取文件内容", style = "判断型", type = "同步组件", comment = "根据MD5值读取文件内容，路径为./errdata", author = "Anonymous", date = "2017-07-11 07:53:11")
	public static TCResult A_getMd5File(String md5) {
		File file = new File("./errdata/" + md5);
		if (!file.exists())
			return TCResult.newFailureResult("BBBBBBB", "找不到文件");
		String content = "";
		try {
			content = FileUtils.readFileToString(file, "UTF-8");
		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBBB", "文件读取失败");
		}
		return TCResult.newSuccessResult(content);
	}

	/**
	 * @category base64写入二进制文件
	 * @param filePathName
	 *            入参|含路径的文件名|{@link java.lang.String}
	 * @param base64Str
	 *            入参|文件内容的Base64编码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "filePathName", comment = "含路径的文件名", type = java.lang.String.class),
			@Param(name = "base64Str", comment = "文件内容的Base64编码", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "base64写入二进制文件", style = "判断型", type = "同步组件", comment = "将base64字符串内容解码并写入指定文件", author = "Anonymous", date = "2017-06-01 02:15:06")
	public static TCResult A_Base64ToFile(String filePathName, String base64Str) {
		// BASE64Encoder encoder = new BASE64Encoder();
		// Encoder encoder = Base64.getEncoder();
		try {
			byte[] content = Base64.decode(base64Str.getBytes());
			FileUtils.writeByteArrayToFile(new File(filePathName), content);
			return TCResult.newSuccessResult();
		} catch (IOException e) {
			return TCResult.newFailureResult("BBBBBBB", e);
		}
	}

	/**
	 * @category 解压jar文件
	 * @param jarFilePathName
	 *            入参|jar文件路径|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "jarFilePathName", comment = "jar文件绝对路径或相对于AFA_HOME的相对路径", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "resultPath", comment = "解压后的文件夹所在路径", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "解压jar文件", style = "判断型", type = "同步组件", comment = "解压缩到jar文件所在位置", author = "Anonymous", date = "2017-07-20 05:24:54")
	public static TCResult A_unJarFile(String jarFilePathName) {
		try {
			String basePath=jarFilePathName.substring(0,jarFilePathName.lastIndexOf("/")+1);
			String jarName = jarFilePathName.substring(jarFilePathName.lastIndexOf("/")+1);
//			String cmd="cd "+basePath+";mv "+jarName+" "+jarName+".jar;"+"mkdir "+jarName+";jar xf "+jarName+".jar";
			String cmd="bin/unJarFile "+basePath+" "+jarName;
			AppLogger.info("compress command : ["+cmd+"]");
			Process process= Runtime.getRuntime().exec(cmd);
			int exitValue = process.waitFor();
			AppLogger.info("process exit["+exitValue+"]");
			return TCResult.newSuccessResult(basePath+jarName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return TCResult.newSuccessResult("");
	}
	
	public static void main(String[] args) {

	}
}
