package tc.bank.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import tc.bank.utils.Base64;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 
 * @date 2018-05-10 10:56:34
 */
@ComponentGroup(level = "银行", groupName = "文件操作组件")
public class B_File {

	/**
	 * @category 读取配置文件
	 * @param configDict
	 *            入参|存放结果的字典|{@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param filePath
	 *            入参|相对路径的文件名|{@link java.lang.String}
	 * @param keyList
	 *            入参|参数列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "dict", comment = "存放结果的字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "filePath", comment = "相对路径的文件名", type = java.lang.String.class),
					@Param(name = "keyList", comment = "参数列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "读取配置文件", style = "判断型", type = "同步组件", comment = "使用properties方式加载配置文件，根据指定的key返回相应的配置值", author = "唐韶东", date = "2018-05-10 10:58:32")
	public static TCResult B_getProperties(JavaDict dict, String filePath, JavaList keyList) {
		Properties properties = new Properties();
		FileInputStream in=null;
		try {
			in = new FileInputStream(System.getProperty("afa.home")+File.separator+filePath);
			properties.load(in);
			in.close();
			for(int i=0;i<keyList.size();i++){
				if(properties.containsKey(keyList.get(i))){
					dict.put(keyList.get(i), properties.get(keyList.get(i)));
				}else{
					dict.put(keyList.get(i), null);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			return TCResult.newFailureResult("999999", "读取配置文件["+filePath+"]失败");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			try{in.close();}catch(Exception e){}
		}


		return TCResult.newSuccessResult();
	}
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
	public static TCResult B_encodingFileToBase64(String filePathName) {
		// BASE64Encoder encoder = new BASE64Encoder();
		// Encoder encoder = Base64.getEncoder();
		try {
			byte[] b = getContent(System.getProperty("afa.home")+File.separator+filePathName);
			// String ret = new String(new BASE64Encoder().encode(b));
			String ret = new String(Base64.encode(b));
			return TCResult.newSuccessResult(ret);
		} catch (IOException e) {
			return TCResult.newFailureResult("HERS401", e);
		}
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
	public static TCResult B_Base64ToFile(String filePathName, String base64Str) {
		// BASE64Encoder encoder = new BASE64Encoder();
		// Encoder encoder = Base64.getEncoder();
		try {
			byte[] content = Base64.decode(base64Str.getBytes());
			FileUtils.writeByteArrayToFile(new File(System.getProperty("afa.home")+File.separator+filePathName), content);
			return TCResult.newSuccessResult();
		} catch (IOException e) {
			return TCResult.newFailureResult("HERS412", e);
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
}
