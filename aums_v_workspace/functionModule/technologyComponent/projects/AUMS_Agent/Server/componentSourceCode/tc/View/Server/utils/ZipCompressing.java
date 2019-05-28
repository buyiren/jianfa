package tc.View.Server.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.*;  

import cn.com.agree.afa.svc.javaengine.context.JavaList;
  
/** 
 * 程序实现了ZIP压缩。共分为2部分 ： 压缩（compression）与解压（decompression） 
 * <p> 
 * 大致功能包括用了多态，递归等JAVA核心技术，可以对单个文件和任意级联文件夹进行压缩和解压。 需在代码中自定义源输入路径和目标输出路径。 
 * <p> 
 * 在本段代码中，实现的是压缩部分；解压部分见本包中Decompression部分。 
 *  
 * @author HAN 
 *  
 */  
  
public class ZipCompressing {  
    private int k = 1; // 定义递归次数变量  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        ZipCompressing book = new ZipCompressing();  
        ArrayList<String> fileList = new ArrayList<String>();
        fileList.add("D:\\Self\\init.bat");
        fileList.add("D:\\Self\\startSet.reg");
        try {  
            book.zip("D:\\abc.zip",  
                   fileList,"D:\\Self");  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
  
    }  
  
    public ZipCompressing() {  
        // TODO Auto-generated constructor stub  
    }  
  
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

    public void zip(String zipFileName, ArrayList<String> fileList) throws Exception {  
        System.out.println("压缩中...");  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(  
                zipFileName));  
        BufferedOutputStream bo = new BufferedOutputStream(out);  
        zip(out, fileList, bo);  
        bo.close();  
        out.close(); // 输出流关闭  
        System.out.println("压缩完成");  
    }  
  
    private void zip(ZipOutputStream out, ArrayList<String> fileList,
            BufferedOutputStream bo) throws Exception { // 方法重载  
            for(int i=0;i<fileList.size();i++){
            	String filePathName=fileList.get(i);
            	out.putNextEntry(new ZipEntry(filePathName.substring("./agent-update/".length()))); // 创建zip压缩进入点base  
//            System.out.println(base);  
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
    
    /**按清单压缩文件
     * */
    public static void zip(String zipFileName, ArrayList<String> fileList,String startPath) throws Exception {  
        System.out.println("压缩中...");  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(  
                zipFileName));  
        BufferedOutputStream bo = new BufferedOutputStream(out);  
//        zip(out, fileList, bo);  
        for(int i=0;i<fileList.size();i++){
        	String filePathName=fileList.get(i);
        	out.putNextEntry(new ZipEntry((fileList.get(i)).substring(startPath.length()).replaceAll("\\\\", "/"))); // 创建zip压缩进入点base  
//        System.out.println(base);  
        	System.out.println("压缩文件["+filePathName+"]");
            FileInputStream in = new FileInputStream(filePathName);  
            BufferedInputStream bi = new BufferedInputStream(in);  
            int len;  
            byte[] tmpBytes=new byte[1024];
//            while ((b = bi.read()) != -1) {
            long tmpLen = 0;
//            byte[] tmp=null;
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
  
//    private void zip(ZipOutputStream out, ArrayList<String> fileList,
//            BufferedOutputStream bo) throws Exception { // 方法重载  
//        for(int i=0;i<fileList.size();i++){
//        	String filePathName=fileList.get(i);
//        	out.putNextEntry(new ZipEntry((fileList.get(i)).replaceAll("\\\\", "/"))); // 创建zip压缩进入点base  
////        System.out.println(base);  
//            FileInputStream in = new FileInputStream(filePathName);  
//            BufferedInputStream bi = new BufferedInputStream(in);  
//            int b;  
//            while ((b = bi.read()) != -1) {  
//                bo.write(b); // 将字节流写入当前zip目录  
//            }  
//            bi.close();  
//            in.close(); // 输入流关闭  
//        }
//    
//    }  
    
	public static byte[] zipBytes(byte[] data){
		byte[] b=null;
		try{
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data);;
			gzip.finish();
			gzip.close();
			b=bos.toByteArray();
			bos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
	public static byte[] unZipBytes(byte[] data){
		byte[] b=null;
		try{
			ByteArrayInputStream bis=new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf=new byte[1024];
			int num=-1;
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			while((num=gzip.read(buf,0,buf.length))!=-1){
				baos.write(buf,0,num);
			}
			b=baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
}  


