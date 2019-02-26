package tc.View.Agent.utils;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;

/**
 * 获取WINDOWS的输出
 * 
 * @date 2018-01-18 9:13:58
 */
public class B_RuntimeUtil {
	
	public static String B_getRunStdOutInfo(String command, String[] envp,
			String dirPath, boolean isAsyn, int waitTime) {
		ByteArrayOutputStream result = null;
		InputStream is = null;
		Process proc = null;
		String res = "";
		File dir = null;
		try {
			result = new ByteArrayOutputStream(64);
			if (dirPath != null && !dirPath.equals("")) {
				dir = new File(dirPath);
			}
			proc = Runtime.getRuntime().exec(
					"C:\\Windows\\Sysnative\\cmd.exe /c " + command, envp, dir);
			if (isAsyn) {
				if (waitTime > 0) {
					try {
						Thread.sleep(waitTime * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return "";
			}
			is = proc.getInputStream();
			byte[] buf = new byte[32];
			int length;
			while ((length = is.read(buf)) >= 0) {
				result.write(buf, 0, length);
			}
			res = new String(result.toByteArray());
			return res.trim();
		} catch (IOException e) {
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (is != null) {
					is.close();
				}
				if (proc != null) {
					proc.destroy();
				}
			} catch (Exception e) {
				result = null;
				is = null;
				proc = null;
			}
		}
		return null;
	}

	/**
	 * 获取CPU信息
	 * 
	 * @category 获取CPU信息
	 * @param res
	 *            出参|系统命令执行返回|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	public static JavaDict B_getCPUInfo() {
		
		ByteArrayOutputStream result = null;
		InputStream is = null;
		String res = "";
		try {
			
			ProcessBuilder pb = new ProcessBuilder("wmic", "cpu");
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.getOutputStream().close();
			
			result = new ByteArrayOutputStream(64);

			is = p.getInputStream();
			byte[] buf = new byte[32];
			int length;
			while ((length = is.read(buf)) >= 0) {
				result.write(buf, 0, length);
			}
			res = new String(result.toByteArray(),"GBK");
			AppLogger.info("CPU: \n" + res.trim());
			p.destroy();
			return DealString(res);
		} catch (IOException e) {
			AppLogger.error("获取CPU信息发生异常：" + e.getMessage());
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (is != null) {
					is.close();
				}
				
			} catch (Exception e) {
				result = null;
				is = null;
			}
		}
		return null;
	}

	/**
	 * 获取操作系统信息
	 * 
	 * @category 获取操作系统信息
	 * @param res
	 *            出参|标准系统命令返回信息|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	public static JavaDict B_getSystemInfo() {
		
		ByteArrayOutputStream result = null;
		InputStream is = null;
		String res = "";
		try {
			
			ProcessBuilder pb = new ProcessBuilder("wmic", "os");
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.getOutputStream().close();
			
			result = new ByteArrayOutputStream(64);
			

			is = p.getInputStream();
			byte[] buf = new byte[32];
			int length;
			while ((length = is.read(buf)) >= 0) {
				result.write(buf, 0, length);
			}
			res = new String(result.toByteArray(),"GBK");
			AppLogger.info("系统: \n" + res.trim());
			p.destroy();
			return DealString(res);
		} catch (IOException e) {
			AppLogger.error("获取系统信息发生异常：" + e.getMessage());
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (is != null) {
					is.close();
				}
				
			} catch (Exception e) {
				result = null;
				is = null;
			}
		}
		return null;
	}

	/**
	 * 处理字符串
	 * 
	 * @param res
	 * @return
	 */
	public static JavaDict DealString(String res) {
		JavaDict javadict = new JavaDict();
		String[] data = res.split("\n");
		String[] titlestr = data[0].trim().split("|");
		String valuestr = data[1];
		String key="";
		String value="";
		int index=0;
		int chineseCharas=0;
		boolean isKeyBlank=false;
		for (int i = 1; i < titlestr.length; i++) {
			if(!titlestr[i].equals(" ")){
				if(isKeyBlank){
					value=valuestr.substring(index-chineseCharas,i-1-chineseCharas).trim();
					javadict.put(key.trim(), value);
					if(value.getBytes().length>value.length()){
						chineseCharas=chineseCharas+getChineseCharas(value);
					}
					index=i-1;
					isKeyBlank=false;
					key=titlestr[i];
				}else{
					key=key+titlestr[i];
				}
			}else{
				isKeyBlank=true;
				key=key+titlestr[i];
			}
		}
		javadict.put(key.trim(), valuestr.substring(index-chineseCharas).trim());
		return javadict;
	}
	
	private static int getChineseCharas(String res){
		String[] data = res.trim().split("|");
		int num=0;
		for (int i = 0; i < data.length; i++) {
			if(data[i].getBytes().length>1){
				num++;
			}
		}
		return num;
	}
	

	/**
	 * 获取磁盘空间信息
	 * 
	 * @category 获取磁盘信息
	 * @param id
	 *            入参|盘符|{@link java.lang.String}
	 * @param res
	 *            出参|标准命令返回信息|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	public static JavaDict B_getDiskInfo(String id) {
		
		ByteArrayOutputStream result = null;
		InputStream is = null;
		String res = "";
		try {
			ProcessBuilder pb = new ProcessBuilder("wmic", "LogicalDisk","where","Caption=\""+id+":\"");
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.getOutputStream().close();
			
			result = new ByteArrayOutputStream(64);

			is = p.getInputStream();
			byte[] buf = new byte[32];
			int length;
			while ((length = is.read(buf)) >= 0) {
				result.write(buf, 0, length);
			}
			res = new String(result.toByteArray(),"GBK");
			AppLogger.info("DISK: \n" + res);
			p.destroy();
			return DealString(res);
		} catch (IOException e) {
			AppLogger.error("获取DISK信息发生异常：" + e.getMessage());
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (is != null) {
					is.close();
				}
				
			} catch (Exception e) {
				result = null;
				is = null;
			}
		}
		return null;
	}

	public static void main(String[] args) {

		String id="c";
		System.out.println("Caption=\""+id+":\"");
		JavaDict res = B_RuntimeUtil.B_getDiskInfo("c");
		
	     System.out.println(res.toString());


		
	}
}
