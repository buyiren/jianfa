package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 文件打包处理
 * 
 * @date 2018-01-19 11:1:13
 */
@ComponentGroup(level = "银行", groupName = "文件打包操作")
public class B_FileZip {

	/**
	 * 压缩文件或者目录到指定的路径
	 * 
	 * @category 日志文件压缩打包
	 * @param zipFileName
	 *            入参|压缩目标文件|{@link java.lang.String}
	 * @param inputPath
	 *            入参|被压缩的文件或目录|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "zipFileName", comment = "压缩目标文件", type = java.lang.String.class),
			@Param(name = "inputPath", comment = "被压缩的文件或目录", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "日志文件压缩打包", style = "判断型", type = "同步组件", author = "Anonymous", date = "2018-01-19 11:22:52")
	public static TCResult B_zipLogFile(String zipFileName, String inputPath) {

		File inputFile = new File(inputPath);

		ZipOutputStream out;

		try {

			out = new ZipOutputStream(new FileOutputStream(zipFileName));

			zip(out, inputFile, inputFile.getName());

			AppLogger.info("压缩完成！");

			out.close();
			return TCResult.newSuccessResult();

		} catch (Exception e) {
			return TCResult.newFailureResult("BBBBBB", e.getMessage());
		}

	}

	
	
	private static void zip(ZipOutputStream out, File f, String base)

	throws Exception {

		AppLogger.info("正在压缩：" + f.getName() + "... ...");

		if (f.isDirectory()) {

			File[] fs = f.listFiles();

			base += "/";

			out.putNextEntry(new ZipEntry(base)); // 生成相应的目录

			for (int i = 0; i < fs.length; i++) {
				// 对本目录下的所有文件对象递归调用本方法
				zip(out, fs[i], base + fs[i].getName());
			}

		} else {
			AppLogger.info("新增文件条目：" + f.getName());

			out.putNextEntry(new ZipEntry(base));

			InputStream is = new FileInputStream(f);

			byte[] buf = new byte[1024];

			int len = 0;

			while ((len = is.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			is.close();
		}
	}

	public static void main(String[] args) {
		String logname=" G1_PCVA_B038_56@43.log";
		String logpath="";
		String[] str=logname.split("_");
		for(int i=1;i<str.length;i++){
			if(i==1){
				logpath=str[i]+"/";
			}else if(i==str.length-1){
				logpath=logpath.substring(0,logpath.length()-1)+"/";
			}else{
				logpath=logpath+str[i]+"_";
			}
		}
		System.out.println(logpath);
	}

	/**
	 * @category 获取日志目录
	 * @param logname
	 *            入参|日志名称|{@link java.lang.String}
	 * @param logpath
	 *            出参|日志相对目录|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "logname", comment = "日志名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "logpath", comment = "日志相对目录", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取日志目录", style = "判断型", type = "同步组件", author = "Anonymous", date = "2018-01-19 01:37:28")
	public static TCResult B_getLogPath(String logname) {
		
		String logpath="";
		String[] str=logname.split("_");
		for(int i=1;i<str.length;i++){
			if(i==1){
				logpath=str[i]+"/";
			}else if(i==str.length-1){
				logpath=logpath.substring(0,logpath.length()-1)+"/";
			}else{
				logpath=logpath+str[i]+"_";
			}
			
		}
		return TCResult.newSuccessResult(logpath);
	}

}

