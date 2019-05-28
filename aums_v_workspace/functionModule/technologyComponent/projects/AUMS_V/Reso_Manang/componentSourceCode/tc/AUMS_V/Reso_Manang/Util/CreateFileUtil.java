package tc.AUMS_V.Reso_Manang.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class CreateFileUtil {
 
  /**
   * 生成.json格式文件
   */
  public static void createJsonFile(String jsonString, String filePath, String fileName) {
    // 标记文件生成是否成功
//    boolean flag = true;
 
    // 拼接文件完整路径
    String fullPath = filePath + File.separator + fileName + ".json";
    String oldPath = filePath + File.separator + "ads.json";
    // 生成json格式文件
    File file = new File(fullPath);
    File oldfile = new File(oldPath);
    try {
      // 保证创建一个新文件      
      if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
        file.getParentFile().mkdirs();
      }
      if (file.exists()) { // 如果已存在,删除旧文件
        file.delete();
      }
      file.createNewFile();
      //第一次写入json文件
      if(!oldfile.exists()){
		file.renameTo(oldfile);		
      }
      //格式化json字符串
      jsonString = JsonFormatTool.formatJson(jsonString);
      //将格式化后的字符串写入文件
      Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
      write.write(jsonString);
      write.flush();
      write.close();
      //删除旧文件，才能rename
      oldfile.delete();
      file.renameTo(oldfile);
    } catch (Exception e) {
//      flag = false;
      file.delete();
    }
  } 
  /**
   * 写入JSON文件信息，先写成功一个JSON文件，如果原来的文件存在，删除原有的， 将新生成的文件改为原来的名字
   * 如果写文件异常，不做操作
   * @param jsonString
   * @param path
   * @param fileName
   * @return
   */
  public static boolean makeJsonFile(String jsonString, String path,
			String fileName) {
	  	//path:  /home/weblogic/resource/Menu/json/MENU_" + devid + "/"
		String filePath = path + File.separator + fileName + ".json";
		String newFilePath = path + File.separator + fileName + "_1.json";
		File file = new File(filePath);
		File newFile = new File(newFilePath);
		if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
			file.getParentFile().mkdirs();
		}
		try {
			newFile.createNewFile();
			jsonString = JsonFormatTool.formatJson(jsonString);
			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(newFile),
					"UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
			if (file.exists()) {
				file.delete();
				newFile.renameTo(file);
			}else{
				newFile.renameTo(file);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
