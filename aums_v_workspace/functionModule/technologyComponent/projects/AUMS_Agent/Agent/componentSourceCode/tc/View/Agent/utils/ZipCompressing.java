package tc.View.Agent.utils;

import java.io.*;
import java.util.zip.*;

import org.apache.commons.io.FileUtils;



import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;

import cn.com.agree.afa.svc.javaengine.context.JavaList;

import java.util.ArrayList;
import java.util.Collection;
public class ZipCompressing {
	public static JavaList unzip(String parent, String zipFileName) {
		try {
			JavaList ret = new JavaList();
			ZipInputStream Zin = new ZipInputStream(new FileInputStream(
					zipFileName));// 输入源zip路径
			BufferedInputStream Bin = new BufferedInputStream(Zin);
			File Fout = null;
			ZipEntry entry;
			while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
				ret.add(entry.getName());
				Fout = new File(parent, entry.getName());
				if (!Fout.exists()) {
					(new File(Fout.getParent())).mkdirs();
				}
				FileOutputStream out = new FileOutputStream(Fout);
				BufferedOutputStream Bout = new BufferedOutputStream(out);
				int b;
				while ((b = Bin.read()) != -1) {
					Bout.write(b);
				}
				Bout.close();
				out.close();
				System.out.println(Fout + "解压成功");
			}
			Bin.close();
			Zin.close();
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}
    public static void zip(String basePath,String zipFileName, ArrayList<String> fileList) throws Exception {  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(  
                zipFileName));  
        BufferedOutputStream bo = new BufferedOutputStream(out);  
        zip(out, fileList, bo,basePath);  
        bo.close();  
        out.close(); // 输出流关闭   
    }  
  
    private static void zip(ZipOutputStream out, ArrayList<String> fileList,
    		BufferedOutputStream bo,String basePath) throws Exception { // 方法重载  
    	for(int i=0;i<fileList.size();i++){
    		String filePathName=fileList.get(i);
    		//System.out.println(filePathName.substring(basePath.length())); 
    		out.putNextEntry(new ZipEntry(filePathName.substring(basePath.length()))); // 创建zip压缩进入点base  
    		//System.out.println(basePath);  
    		FileInputStream in = new FileInputStream(filePathName);  
    		BufferedInputStream bi = new BufferedInputStream(in);  
    		int b;  
    		while ((b = bi.read()) != -1) {  
    			bo.write(b); // 将字节流写入当前zip目录  
    		}  
    		bi.close();  
    		in.close(); // 输入流关闭  
    	}

    }
    
    //这里的压缩文件不设basePath路径，所有文件压缩后都是在压缩文件的根目录
    public static void zip(String zipFileName, ArrayList<String> fileList) throws Exception {   
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(  
                zipFileName));  
        BufferedOutputStream bo = new BufferedOutputStream(out);  
        zip(out, fileList, bo);  
        bo.close();  
        out.close(); // 输出流关闭   
    }  
    
  //这里的压缩文件不设basePath路径，所有文件压缩后都是在压缩文件的根目录
    private static void zip(ZipOutputStream out, ArrayList<String> fileList,
    		BufferedOutputStream bo) throws Exception { // 方法重载  
    	for(int i=0;i<fileList.size();i++){
    		String filePathName=fileList.get(i);
    		out.putNextEntry(new ZipEntry(filePathName.substring(filePathName.lastIndexOf("\\")+1)));	   
    		FileInputStream in = new FileInputStream(filePathName);  
    		BufferedInputStream bi = new BufferedInputStream(in);  
    		int b;  
    		while ((b = bi.read()) != -1) {  
    			bo.write(b); // 将字节流写入当前zip目录  
    		}  
    		bi.close();  
    		in.close(); // 输入流关闭  
    	} 
    }
    
    /**压缩目录
     * @param path 被压缩的路径名，必须是./开头的相对路径，且路径分隔符必须为正斜线[/]，不允许使用反斜线[\]
     * @param zipFileName 相对路径或绝对路径的压缩文件名
     * */
    public static void zip(String zipFileName,String path) throws Exception {
    	File curDir = new File("./");
    	System.out.println("当前路径         ["+curDir.getAbsolutePath()+"]");
    	String parentPath = path.substring(0,path.lastIndexOf("/"));
    	Collection<File> listFiles = FileUtils.listFilesAndDirs(new File(path),
				new PrefixFileFilter("",IOCase.INSENSITIVE), new PrefixFileFilter("",IOCase.INSENSITIVE));
    	ArrayList<String> fileEntryList = new ArrayList<String>();
    	ArrayList<File> fileList = new ArrayList<File>();
         for (File file : listFiles) {
			if(file.isFile()){
				String tmpPath = file.getAbsolutePath().substring(parentPath.length()+1);
				fileEntryList.add(tmpPath.replaceAll("\\\\", "/"));
				fileList.add(file);
			}
		}
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(  
                 zipFileName));  
         BufferedOutputStream bo = new BufferedOutputStream(out);  
//         zip(out, fileList, bo);  
         for(int i=0;i<fileEntryList.size();i++){
         	String filePathName=fileEntryList.get(i);
         	out.putNextEntry(new ZipEntry(fileEntryList.get(i))); // 创建zip压缩进入点base  
//         System.out.println(base);  
         	System.out.println("压缩文件["+filePathName+"]");
             FileInputStream in = new FileInputStream((fileList.get(i)));  
             BufferedInputStream bi = new BufferedInputStream(in);  
             int len;  
             byte[] tmpBytes=new byte[1024];
//             while ((b = bi.read()) != -1) {
             long tmpLen = 0;
//             byte[] tmp=null;
             while ((len= bi.read(tmpBytes))>0) {  
             	bo.write(tmpBytes, 0, len); // 将字节流写入当前zip目录
             	tmpLen+=len;
             }  
             bo.flush();
         	System.out.println("写入的文件长度["+tmpLen+"]");
             bi.close();  
             in.close(); // 输入流关闭  
         }
         bo.close();  
         out.close(); // 输出流关闭  
         System.out.println("压缩完成");  
        
    }
}
