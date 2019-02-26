package tc.AUMS_V.Version_Manage.util.depend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cn.com.agree.afa.svc.javaengine.AppLogger;

public class unpackZip
{

  public static void unZipFiles(String zipPath, String descDir)
    throws IOException
  {
    unZipFiles(new File(zipPath), descDir);
  }
  
  public static void unZipFiles(File zipFile, String descDir)
    throws IOException
  {
    @SuppressWarnings("resource")
	ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
    AppLogger.info("创建zipFile成功!!!");
    //本地测试
    //String name = zip.getName().substring(zip.getName().lastIndexOf('\\') + 1, zip.getName().lastIndexOf('.'));
    //服务器测试
    String name = zip.getName().substring(zip.getName().lastIndexOf('/') + 1, zip.getName().lastIndexOf('.'));
    AppLogger.info("上传到服务器的文件名为:【"+name+"】");
    File pathFile = new File(descDir + name);
    AppLogger.info("上传到服务器的文件路径:【"+pathFile+"】");
    if (!pathFile.exists()) {
      pathFile.mkdirs();
    }
    for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();)
    {
      ZipEntry entry = (ZipEntry)entries.nextElement();
      String zipEntryName = entry.getName();
      InputStream in = zip.getInputStream(entry);
      String outPath = (descDir + name + "/" + zipEntryName).replaceAll("\\*", "/");
      

      File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
      if (!file.exists()) {
        file.mkdirs();
      }
      if (!new File(outPath).isDirectory())
      {
    	AppLogger.info("输出文件路径为：【" + outPath + "】");
        FileOutputStream out = new FileOutputStream(outPath);
        byte[] buf1 = new byte[1024];
        int len;
        while ((len = in.read(buf1)) > 0) {
          out.write(buf1, 0, len);
        }
        in.close();
        out.close();
      }
    }
  }
}

