package tc.View.Server.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;

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
		ArrayList<File> list = FileUtil.listInDirectory("d:\\HERS\\runtime\\AIM_Agent_2.3_20170413\\workspace\\java");
		for(File file : list){
			System.out.println("结果:"+file.getPath());
		}
		System.out.println("文件个数:"+list.size());
		File a = new File("d:\\a\\a\\a\\b.txt");
		System.out.println("a parent is:["+a.getParent());
	}

}
