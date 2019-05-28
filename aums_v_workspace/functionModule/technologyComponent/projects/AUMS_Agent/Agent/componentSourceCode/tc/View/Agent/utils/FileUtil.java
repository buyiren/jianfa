package tc.View.Agent.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;

import com.alibaba.fastjson.JSONObject;

public class FileUtil {

		public static ArrayList<File> listInDirectory(String path){
			ArrayList <File> result = new ArrayList<File>();
			Collection<File> listFiles = FileUtils.listFilesAndDirs(new File(
					path),
					new PrefixFileFilter("",IOCase.INSENSITIVE), new PrefixFileFilter("",IOCase.INSENSITIVE));
			for (File file : listFiles) {
				if(file.isFile()){
					result.add(file);
				}
			}
			return result;
		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ArrayList<File> list = FileUtil.listInDirectory("d:\\HERS\\runtime\\AIM_Agent_2.3_20170413\\workspace\\java");
//		for(File file : list){
//			System.out.println("结果:"+file.getPath());
//		}
//		System.out.println("文件个数:"+list.size());
//		File a = new File("d:\\a\\a\\a\\b.txt");
//		System.out.println("a parent is:["+a.getParent());
		File base = new File("C:/&");
		File[] files = base.listFiles();
		for(int i=0;i<files.length;i++){
			System.out.println(files[i].getAbsolutePath().replaceAll("\\\\", "/"));
		}
//		if(base.canRead()){
//			System.out.println(base.getAbsolutePath());
//		}else{
//			System.out.println("321");
//		}
		
	}
	public static JSONObject listAllDir(String path,String recursion) {
		JSONObject baseDict = new JSONObject();
		//如果路径为空列出所有盘符
		if(path==null||path.equals("")){
			File[] roots = File.listRoots();
			for (int i = 0; i < roots.length; i++) {
				JSONObject tmpDir = new JSONObject();
				String tmpName = roots[i].toString();
				if(roots[i].canRead()&&roots[i].isHidden()){//注意这里ishidden是反的，windows的盘符是隐藏的
					tmpDir.put("name", tmpName.substring(0,1));
					tmpDir.put("isDirectory", true);
					tmpDir.put("absolutePath", roots[i].getAbsolutePath().replaceAll("\\\\", "/"));
//					tmpDir.put("relativePath", roots[i].getAbsolutePath()
//							.substring(abc_path.getBytes().length + 1).replaceAll("\\\\", "/"));
					tmpDir.put("lastModified", roots[i].lastModified());
					tmpDir.put("size", "");
					baseDict.put(tmpName, tmpDir);
				}
			}
			return baseDict;
		}
		
		File base = new File(path);
		File[] files = base.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {// 是文件
				JSONObject tmpFile = new JSONObject();
				if(files[i].canRead()&&!files[i].isHidden()){
					String tmpName = files[i].getName();
					tmpFile.put("name", tmpName);
					tmpFile.put("isDirectory", false);
					tmpFile.put("absolutePath", files[i].getAbsolutePath().replaceAll("\\\\", "/"));
					tmpFile.put("lastModified", files[i].lastModified());
					tmpFile.put("size", (files[i].length()/1024+1)+" KB");
	//				tmpFile.put("relativePath", files[i].getAbsolutePath()
	//						.substring(abc_path.getBytes().length + 1));
	//				tmpFile.put(
	//						"extension",
	//						tmpName.indexOf(".") > 0 ? tmpName.substring(tmpName
	//								.lastIndexOf(".") + 1) : "");
					baseDict.put(tmpName, tmpFile);
				}
			} else {// 是目录
				JSONObject tmpDir = new JSONObject();
				if(files[i].canRead()&&!files[i].isHidden()){
					String tmpName = files[i].getName();
					//add by lk 20180307 chengdu 针对系统文件夹外部命令无法打开，则直接不加载此文件夹，路：win7系统下的：C:\\Documents and Settings
					File check = new File(files[i].getAbsolutePath().replaceAll("\\\\", "/"));
					File[] checkfiles = check.listFiles();
					if(checkfiles==null){
						continue;
					}
					//add by lk end
					tmpDir.put("name", tmpName);
					tmpDir.put("isDirectory", true);
					tmpDir.put("absolutePath", files[i].getAbsolutePath().replaceAll("\\\\", "/"));
	//				tmpDir.put("relativePath", files[i].getAbsolutePath()
	//						.substring(abc_path.getBytes().length + 1).replaceAll("\\\\", "/"));
					tmpDir.put("lastModified", files[i].lastModified());
					tmpDir.put("size", "");
					if ("true".equals(recursion)) {
						// 递归
						tmpDir.put(
								"content",
								listAllDir((String) tmpDir.get("absolutePath"),
										recursion));
					}
					baseDict.put(tmpName, tmpDir);
				}
			}
		}
		return baseDict;
	}
}
