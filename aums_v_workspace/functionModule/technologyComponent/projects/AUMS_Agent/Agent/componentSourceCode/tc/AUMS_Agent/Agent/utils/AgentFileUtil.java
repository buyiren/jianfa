package tc.AUMS_Agent.Agent.utils;

import java.io.File;

import cn.com.agree.afa.svc.javaengine.AppLogger;

import com.alibaba.fastjson.JSONObject;

public class AgentFileUtil {
	public static JSONObject listAllDir(String basePath,String path,
			String recursion) {
		File base = new File(basePath + "\\" + path);
		File[] files = base.listFiles();
		JSONObject baseDict = new JSONObject();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {// 是文件
				JSONObject tmpFile = new JSONObject();
				String tmpName = files[i].getName();
				tmpFile.put("fileName", tmpName);
				tmpFile.put("lastModified", files[i].lastModified());
				tmpFile.put("absolutePath", files[i].getAbsolutePath().replaceAll("\\\\", "//"));
				tmpFile.put("relativePath", files[i].getAbsolutePath().replaceAll("\\\\", "//")
						.substring(basePath.length()));
				tmpFile.put("size", files[i].length());
				tmpFile.put(
						"extension",
						tmpName.indexOf(".") > 0 ? tmpName.substring(tmpName
								.lastIndexOf(".") + 1) : "");
				tmpFile.put("isDirectory", false);
				baseDict.put(tmpName, tmpFile);
			} else {// 是目录
				JSONObject tmpDir = new JSONObject();
				String tmpName = files[i].getName();
				tmpDir.put("fileName", tmpName);
				tmpDir.put("isDirectory", true);
				tmpDir.put("absolutePath", files[i].getAbsolutePath().replaceAll("\\\\", "//"));
				AppLogger.info("===========files[i].getAbsolutePath():["+files[i].getAbsolutePath().replaceAll("\\\\", "//")+"]");
				AppLogger.info("===========basePath.getBytes().length:["+basePath+"]");
				tmpDir.put("relativePath", files[i].getAbsolutePath().replaceAll("\\\\", "//")
						.substring(basePath.length()));
				tmpDir.put("lastModified", files[i].lastModified());
				if (recursion.equals("1")) {
					// 递归
					tmpDir.put(
							"content",
							listAllDir(basePath,
									(String) tmpDir.get("relativePath"),
									recursion));
				}
				baseDict.put(tmpName, tmpDir);
			}
		}
		return baseDict;
	}
}
