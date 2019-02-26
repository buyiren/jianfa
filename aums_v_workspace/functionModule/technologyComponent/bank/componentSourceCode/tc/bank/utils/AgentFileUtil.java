package tc.bank.utils;

import java.io.File;

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
				tmpFile.put("name", tmpName);
				tmpFile.put("lastModified", files[i].lastModified());
				tmpFile.put("relativePath", files[i].getAbsolutePath()
						.substring(basePath.getBytes().length + 1));
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
				tmpDir.put("name", tmpName);
				tmpDir.put("isDirectory", true);
				tmpDir.put("absolutePath", files[i].getAbsolutePath().replaceAll("\\\\", "/"));
				tmpDir.put("relativePath", files[i].getAbsolutePath()
						.substring(basePath.getBytes().length + 1).replaceAll("\\\\", "/"));
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
