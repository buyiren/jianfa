package tc.AUMS_V.Version_Manage.util.depend;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.aweb.asapi.codecs.Base64;

public class ReadFile
{
	
  public static void readfile(String filepath, String versionId, List<VersiondetailinfoVo> list)
    throws FileNotFoundException, IOException
  {
    File file = new File(filepath);
    FileChannel fc = null;
    try
    {
      if (!file.isDirectory())
      {
        FileInputStream fis = new FileInputStream(file);
        VersiondetailinfoVo info = new VersiondetailinfoVo();
        info.setPATH(getpath(file.getPath(), versionId + ""));
        info.setFILEID(file.getName());
        info.setMD5(getMd5ByFile(file));
        info.setVERSIONID(versionId);
        info.setTYPE(getsuffix(file.getPath()));
        fc = fis.getChannel();
        info.setFILESIZE(String.valueOf(fc.size()));
        list.add(info);
        fis.close();
      }
      else if (file.isDirectory())
      {
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++)
        {
          AppLogger.info("下层路径" + filepath + "/" + filelist[i]);
          File readfile = new File(filepath + "/" + filelist[i]);
          if (!readfile.isDirectory())
          {
        	AppLogger.info("当前路径" + file.getPath());
            FileInputStream fis = new FileInputStream(readfile);
            fc = fis.getChannel();
            
            VersiondetailinfoVo info = new VersiondetailinfoVo();
            info.setPATH(getpath(readfile.getPath(), versionId + ""));
            AppLogger.info("setpath"+getpath(readfile.getPath(), versionId + ""));
            info.setFILENAME(readfile.getName());
            info.setMD5(getMd5ByFile(readfile));
            info.setVERSIONID(versionId);
            info.setTYPE(getsuffix(readfile.getPath()));
            info.setFILESIZE(String.valueOf(fc.size()));
            list.add(info);
            fis.close();
          }
          else if (readfile.isDirectory())
          {
            readfile(filepath + "/" + filelist[i], versionId, list);
          }
        }
      }
      if (null != fc) {
        try
        {
          fc.close();
        }
        catch (IOException e)
        {
        	AppLogger.info(e);
        }
      }
      return;
    }
    catch (FileNotFoundException e)
    {
      if (null != fc) {
        try
        {
          fc.close();
        }
        catch (IOException e1)
        {
        	AppLogger.info(e1);
        }
      }
    }
    catch (IOException e)
    {
      AppLogger.info(e);
      if (null != fc) {
        try
        {
          fc.close();
        }
        catch (IOException e2)
        {
        	AppLogger.info(e2);
        }
      }
    }
    finally
    {
      if (null != fc) {
        try
        {
          fc.close();
          
        }
        catch (IOException e)
        {
        	AppLogger.info(e);
        }
      }
    }
  }
  
  public static String getMd5ByFile(File file)
    throws FileNotFoundException
  {
    String value = null;
    FileInputStream in = new FileInputStream(file);
    try
    {
      MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(byteBuffer);
      value =Base64.encodeBytes(md5.digest());
      
//      value = Base64.encodeBase64String(md5.digest());
      if (null != in) {
        try
        {
          in.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      return value;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      if (null != in) {
        try
        {
          in.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }
    }
    finally
    {
      if (null != in) {
        try
        {
          in.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
	return value;
  }
  
  public static String getpath(String oldPath, String versionId)
  {
    System.out.println(oldPath);
    AppLogger.info("oldpath--"+oldPath+"--versionId--"+versionId);
    //20180920韩斌修改，原因，C端返回的地址不正确，原始方法不包含unzip，现修改截取规则
    //String str = oldPath.substring(oldPath.indexOf(versionId) + versionId.length() + 1);
    String str = oldPath.substring(oldPath.indexOf("unzip"));
    if (str.indexOf("\\") != -1) {
      str = str.replaceAll("\\\\\\\\", "\\\\");
    }
    return str;
  }
  
  public static String getsuffix(String oldPath)
  {
	String file_separator = System.getProperty("file.separator");
	if (oldPath.contains(file_separator)) {
		String [] temp = oldPath.split(file_separator);
		String laststr = temp[temp.length-1];
		if (laststr.contains(".")) {
			return laststr.substring(laststr.lastIndexOf("."));
		}
	} else {
		if (oldPath.contains(".")) {
			return oldPath.substring(oldPath.lastIndexOf("."));
		}
	}
	return null;
  }
  
  public static List<VersiondetailinfoVo> getFileInfo(String filepath, String versionId)
    throws FileNotFoundException, IOException
  {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	List<VersiondetailinfoVo> list = new ArrayList();
    AppLogger.info("filepath$$$$$$$$$$$$$$$$$"+filepath);
    readfile(filepath, versionId, list);
    return list;
  }
}
