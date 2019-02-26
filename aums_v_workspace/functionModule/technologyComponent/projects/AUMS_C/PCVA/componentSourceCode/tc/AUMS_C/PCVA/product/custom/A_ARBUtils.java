package tc.AUMS_C.PCVA.product.custom;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import tc.bank.constant.IErrorCode;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import cn.com.agree.afa.util.Base64;

import com.sunyard.client.SunEcmClientApi;
import com.sunyard.client.bean.ClientBatchBean;
import com.sunyard.client.bean.ClientBatchFileBean;
import com.sunyard.client.bean.ClientBatchIndexBean;
import com.sunyard.client.bean.ClientFileBean;
import com.sunyard.client.impl.SunEcmClientSocketApiImpl;

/**
 * 远程客服工具类
 * 
 * @date 2017-10-09 21:52:42
 */
@ComponentGroup(level = "应用", groupName = "远程客服")
public class A_ARBUtils {
	
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();   
	
	/**
	 * @category 计算文件的MD5
	 * @param FilePath
	 *            入参|文件路径|{@link java.lang.String}
	 * @param FileMd5
	 *            出参|文件的Md5值|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "FilePath", comment = "文件路径", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "FileMd5", comment = "文件的Md5值", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "计算文件的MD5", style = "判断型", type = "同步组件", comment = "计算文件的MD5", author = "Anonymous", date = "2017-09-26 11:29:09")
	public static TCResult A_FileMd5Calculate(String FilePath) {
		File file1 = new File(FilePath);
		if (!file1.exists()) {
			return TCResult.newFailureResult(ErrorCode.AGR, "待生成MD5码对应的文件不存在!");
		}
		ByteArrayOutputStream baos = null;
		InputStream is = null;
		byte[] result = null;
		String md5String = "";

		baos = new ByteArrayOutputStream();
		try {
			is = new FileInputStream(file1);
		} catch (FileNotFoundException e) {
			return TCResult.newFailureResult(ErrorCode.AGR, "待生成MD5码对应的文件不存在,"
					+ e.toString());
		}
		byte[] buff = new byte[2048];
		int length = 0;
		try {
			while ((length = is.read(buff)) > 0) {
				baos.write(buff, 0, length);
			}
		} catch (IOException e) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"读待生成md5文件异常," + e.toString());
		}
		MessageDigest md;
		try {

			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"生成文件md5异常," + e.toString());
		}
		result = md.digest(baos.toByteArray());
		// BigInteger bigInt = new BigInteger(result);
		// System.out.println(bigInt.toString(16).toUpperCase());
		if (result != null) {
			md5String = new String(Base64.encodeBase64(result));
		} else {
			return TCResult.newFailureResult(ErrorCode.AGR, "计算文件md5异常");
		}
		return TCResult.newSuccessResult(md5String);
	}
	
	/**
	 * @category 影像上传
	 * @param ip
	 *            入参|影像地址|{@link java.lang.String}
	 * @param socketport
	 *            入参|端口|{@link java.lang.Integer}
	 * @param modelCode
	 *            入参|模型代码|{@link java.lang.String}
	 * @param filePartName
	 *            入参|模型部件|{@link java.lang.String}
	 * @param userName
	 *            入参|用户名|{@link java.lang.String}
	 * @param passWord
	 *            入参|密码|{@link java.lang.String}
	 * @param busi_start_date
	 *            入参|业务日期|{@link java.lang.String}
	 * @param busi_serial_no
	 *            入参|业务流水号|{@link java.lang.String}
	 * @param filePath
	 *            入参|文件列表|{@link java.lang.String}
	 * @param message
	 *            出参|响应信息|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "ip", comment = "影像地址", type = java.lang.String.class),
			@Param(name = "socketport", comment = "端口", type = java.lang.Integer.class),
			@Param(name = "userName", comment = "用户名", type = java.lang.String.class),
			@Param(name = "passWord", comment = "密码", type = java.lang.String.class),
			@Param(name = "modelCode", comment = "模型代码", type = java.lang.String.class),
			@Param(name = "filePartName", comment = "模型部件", type = java.lang.String.class),
			@Param(name = "busi_start_date", comment = "业务日期", type = java.lang.String.class),
			@Param(name = "busi_serial_no", comment = "业务流水号", type = java.lang.String.class),
			@Param(name = "filePath", comment = "文件列表", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "message", comment = "响应信息", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "影像上传", style = "判断型", type = "同步组件", date = "2017-04-12 06:39:48")
	public static TCResult A_ECMUpload(String ip, Integer socketport,
			String userName, String passWord, String modelCode,
			String filePartName, String busi_start_date, String busi_serial_no,
			String filePath) {

		String[] filePathArr = filePath.split(",");

		SunEcmClientApi api = new SunEcmClientSocketApiImpl(ip, socketport);
		ClientBatchBean batchBean = new ClientBatchBean();
		// ---------------设置批次对象----------------
		batchBean.setUser(userName);
		batchBean.setPassWord(passWord);
		batchBean.setBreakPoint(false);
		batchBean.setModelCode(modelCode);
		batchBean.setOwnMD5(false);
		// ---------------设置索引对象----------------
		ClientBatchIndexBean batchIndexBean = new ClientBatchIndexBean();
		batchIndexBean.addCustomMap("BUSI_START_DATE", busi_start_date);// 业务日期
		batchIndexBean.addCustomMap("BUSI_SERIAL_NO", busi_serial_no);// 业务流水号--唯一索引
		batchIndexBean.setAmount(String.valueOf(filePathArr.length));// 上传文件数目
		batchIndexBean.addCustomMap("AMOUNT",
				String.valueOf(filePathArr.length));// 上传文件数目
		// ---------------设置文档部件信息----------------
		ClientBatchFileBean batchFileBean = new ClientBatchFileBean();
		batchFileBean.setFilePartName(filePartName);
		// ---------------添加文件1-----------------------
		for (int i = 0; i < filePathArr.length; i++) {
			ClientFileBean fileBean = new ClientFileBean();
			fileBean.setFileName(filePathArr[i]);// 本地文件路径
			fileBean.setFileFormat(filePathArr[i].substring(filePathArr[i]
					.lastIndexOf(".") + 1));
			fileBean.addOtherAtt("PS_LEVEL","0");//主件为0 附件为1 
			// bean.setFilesize("9106");//文件大小,实际大小
			// bean.addOtherAtt("BUSI_FILE_PAGENUM","1");//文件页码
			// bean.addOtherAtt("BUSI_FILE_SCANUSER","zhangsan");//文件扫描人，柜员号
			batchFileBean.addFile(fileBean);
		}

		// -------------批次添加索引对象，文档部件对象-----
		batchBean.addDocument_Object(batchFileBean);
		batchBean.setIndex_Object(batchIndexBean);

		String message = "";
		try {
			message = api.upload(batchBean, "");
		} catch (Exception e) {
			// AppLogger.error(e);
			e.printStackTrace();
			return TCResult.newFailureResult(ErrorCode.AGR,
					"影像提交异常:" + e.toString());
		}
		
		if(message.startsWith("SUCCESS")){
			return TCResult.newSuccessResult(message.split("<<::>>")[1]);
		}else{
			return TCResult.newFailureResult(ErrorCode.AGR,
					"影像上传失败" );
		}
		// SUCCESS<<::>>201704_327_4A6015C3-38D3-A1E3-57A3-7924FDC55BF5-1
		// FAIL<<::>>201704_375_7610D414-1E9B-EF81-410F-DE57B8F62E61-1<<::>>708
	}

	
	/**   
     * Discription:[getAllFileType,常见文件头信息] 
     */     
    private static void getAllFileType() {     
        FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg"); //JPEG (jpg)     
        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); //PNG (png)     
        FILE_TYPE_MAP.put("47494638396126026f01", "gif"); //GIF (gif)     
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); //TIFF (tif)     
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); //16色位图(bmp)     
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); //24位位图(bmp)     
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); //256色位图(bmp)     
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); //CAD (dwg)     
        FILE_TYPE_MAP.put("3c21444f435459504520", "html"); //HTML (html)
        FILE_TYPE_MAP.put("3c21646f637479706520", "htm"); //HTM (htm)
        FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); //css
        FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); //js
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); //Rich Text Format (rtf)     
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); //Photoshop (psd)     
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); //Email [Outlook Express 6] (eml)       
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); //MS Excel 注意：word、msi 和 excel的文件头一样     
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "vsd"); //Visio 绘图     
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); //MS Access (mdb)      
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");     
        FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); //Adobe Acrobat (pdf)   
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); //rmvb/rm相同  
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); //flv与f4v相同  
        FILE_TYPE_MAP.put("00000020667479706d70", "mp4"); 
        FILE_TYPE_MAP.put("49443303000000002176", "mp3"); 
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //     
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); //wmv与asf相同    
        FILE_TYPE_MAP.put("52494646e27807005741", "wav"); //Wave (wav)  
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");  
        FILE_TYPE_MAP.put("4d546864000000060001", "mid"); //MIDI (mid)   
        FILE_TYPE_MAP.put("504b0304140000000800", "zip");    
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");   
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");   
        FILE_TYPE_MAP.put("504b03040a0000000000", "jar"); 
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");//可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");//jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");//MF文件
        FILE_TYPE_MAP.put("3c3f786d6c2076657273", "xml");//xml文件
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");//xml文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java");//java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");//bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");//gz文件
        FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");//bat文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");//bat文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm");//bat文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");//bat文件
        FILE_TYPE_MAP.put("504b0304140006000800", "docx");//docx文件
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "wps");//WPS文字wps、表格et、演示dps都是一样的
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");

        FILE_TYPE_MAP.put("6D6F6F76", "mov"); //Quicktime (mov)  
        FILE_TYPE_MAP.put("FF575043", "wpd"); //WordPerfect (wpd)   
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); //Outlook Express (dbx)     
        FILE_TYPE_MAP.put("2142444E", "pst"); //Outlook (pst)      
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); //Quicken (qdf)     
        FILE_TYPE_MAP.put("E3828596", "pwl"); //Windows Password (pwl)         
        FILE_TYPE_MAP.put("2E7261FD", "ram"); //Real Audio (ram)     
    }                       
    
    /**
     * 得到上传文件的文件头
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /**
     * 根据制定文件的文件头判断其文件类型
     * @param filePaht
     * @return
     */
    public static String getFileType(byte[] b){
        String res = null;
        try {
        	byte[] nb = new byte[10];
        	System.arraycopy(b, 0, nb, 0, 10);
            String fileCode = bytesToHexString(nb);    
            AppLogger.info(fileCode);
            if(FILE_TYPE_MAP.isEmpty()){
            	getAllFileType();
            }
            //这种方法在字典的头代码不够位数的时候可以用但是速度相对慢一点
            Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
            while(keyIter.hasNext()){
                String key = keyIter.next();
                if(key.toLowerCase().startsWith(fileCode.toLowerCase()) || fileCode.toLowerCase().startsWith(key.toLowerCase())){
                    res = FILE_TYPE_MAP.get(key);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

	
	/**
	 * @category 将base64串转为图片
	 * @param imgStr
	 *            入参|base64字符串|{@link java.lang.String}
	 * @param path
	 *            入参|文件路径|{@link java.lang.String}
	 * @param filename
	 *            入参|文件名|{@link java.lang.String}
	 * @param filetype
	 *            出参|文件类型|{@link java.lang.String}
	 * @param filename
	 *            出参|文件名|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "imgStr", comment = "base64字符串", type = java.lang.String.class),
			@Param(name = "path", comment = "文件路径", type = java.lang.String.class),
			@Param(name = "filename", comment = "文件名", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "filetype", comment = "文件类型", type = java.lang.String.class),
			@Param(name = "filename", comment = "文件名", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "将base64串转为图片", style = "判断型", type = "同步组件", date = "2017-10-19 02:02:58")
	public static TCResult A_GenerateImage(String imgStr, String path,
			String filename) {
		try {
			if (imgStr == null)
				return TCResult.newFailureResult(ErrorCode.AGR,
						"base64字符串数据不能为空");
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b = decoder.decodeBuffer(imgStr);
			String res = getFileType(b);
			AppLogger.info("======文件类型为=====" + res);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			
			OutputStream out = new FileOutputStream(path +filename+ "." + res);
			out.write(b);
			out.flush();
			out.close();
			return TCResult.newSuccessResult(res,filename+ "." + res);
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
	}

	/**
	 * @category 图片转换为base64字符串
	 * @param imgFile
	 *            入参|图片名称|{@link java.lang.String}
	 * @param data
	 *            出参|base64图片数据|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "imgFile", comment = "图片名称", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "data", comment = "base64图片数据", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "图片转换为base64字符串", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-09-29 05:12:45")
	public static TCResult A_GetImageStr(String imgFile) {
		try{
			InputStream inputStream = null;
			byte[] data = null;
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
			// 加密
			BASE64Encoder encoder = new BASE64Encoder();
			return TCResult.newSuccessResult(encoder.encode(data));
		} catch (Exception e) {
			AppLogger.error(e.getMessage());
			AppLogger.error(e);
			return TCResult.newFailureResult(
					IErrorCode.getCode("SystemException"), e);
		}
		
		
		
		
		
	}
	
	/**
	 * @category 把Dict添加到List
	 * @param reqdata
	 *            入参|请求数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param reqlist
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param rsplist
	 *            出参|返回容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "reqdata", comment = "请求数据", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "reqlist", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "rsplist", comment = "返回容器", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "把Dict添加到List", style = "判断型", type = "同步组件", author = "cai", date = "2016-09-26 04:34:34")
	public static TCResult A_ListAddDict(JavaDict reqdata, JavaList reqlist) {
		reqlist.add(reqdata);
		return TCResult.newSuccessResult(reqlist);
	}
	
	
}
