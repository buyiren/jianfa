package tc.View.Agent;
import tc.bank.constant.IErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * @date 2018-02-03 12:40:52
 */
@ComponentGroup(level = "银行", groupName = "机具网络")
public class A_OperNetwork {
	
	/**
	 * @param ipStr
	 *            入参|IP地址字符串|{@link java.lang.String}
	 * @param count
	 *            入参|响应次数|{@link java.lang.String}
	 * @param timeOut
	 *            入参|超时时间|{@link java.lang.String}
	 * @param responseDesc
	 *            出参|响应描述|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "ipStr", comment = "IP地址字符串，逗号分隔", type = java.lang.String.class),
			@Param(name = "count", comment = "响应次数", type = java.lang.String.class),
			@Param(name = "timeOut", comment = "超时时间(单位/毫秒)", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "descDetail", comment = "响应描述", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "网络通讯检测", style = "判断型", type = "同步组件", comment = "根据传入的ip地址，检测是否能ping通", date = "2018-02-03 10:18:52")
	public static TCResult A_CheckNetwork(String ipStr, String count ,String timeOut) {
		if(ipStr == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "IP地址字符串不能为空");
		}
		if(count == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "响应次数不能为空");
		}
		if(timeOut == null){
			return TCResult.newFailureResult(IErrorCode.getCode("FieldMustBeEntered"), "超时时间不能为空");
		}		
		String descDetail = "";   //返回的详细信息
		Process process = null;
		String line = "";
		String returnMsg = "";	
		String succIpStr = "";
		String failIpStr = "";		
		String[] deviceIpList = ipStr.split(",");
		InputStreamReader inputRead =null;
		BufferedReader returnData =null;
		Runtime runtime = Runtime.getRuntime();
		for (String ipaddress : deviceIpList){
    		try {
    			process = runtime.exec("ping " + ipaddress + " -n " + count + " -w " + timeOut);
    			inputRead = new InputStreamReader(process.getInputStream());
            	returnData = new BufferedReader(inputRead);
            	
            	while ((line = returnData.readLine()) != null) {
            		AppLogger.info(line);
            		returnMsg += line;
            		}
            	 if (null != returnMsg && !returnMsg.equals("")) { 
            		 if ( returnMsg.indexOf("TTL") > 0 || returnMsg.indexOf("ttl") > 0) { 
                    	 succIpStr += ipaddress+"网络状态正常#";// 网络畅通       
                     } else {     
                         failIpStr += ipaddress+"网络状态不正常#";// 网络不畅通      
                     }
            		 try {
            			 returnData.close();
        			 } catch (Exception e) {
        				AppLogger.error(e);
        			 }
        		}
        		if (inputRead != null) {
        			try {
        				inputRead.close();
        			} catch (Exception e) {
        				AppLogger.error(e);
        			}
        		}	
    		}catch (Exception e) {
    			AppLogger.error(e.getMessage());
    			AppLogger.error(e);
    		    return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
    		}
    		returnMsg = "";
        	line = "";			
        }

        if (succIpStr.length()>0){
    	descDetail += ""+ succIpStr.substring(0, succIpStr.length()-1);
        }
        if (failIpStr.length()>0){
    	descDetail += ""+ failIpStr.substring(0, failIpStr.length()-1);
        }
        return TCResult.newSuccessResult(descDetail);
	}
	
	/**
	 * @param ReturnStr
	 *            出参|返回进程信息二维数组|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@OutParams(param = {
			@Param(name = "processList", comment = "返回进程信息二维数组", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })//java.lang.String.class
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "获取进程列表信息", style = "判断型", type = "同步组件", comment = "获取本机进程信息", date = "2018-02-03 20:18:52")
	public static TCResult A_getProcessList() {

		String ostr = "";
		BufferedReader out = null;
		BufferedReader err = null;
		List<List> processList = new ArrayList<List>();
		
		try{
			ProcessBuilder pb = new ProcessBuilder("tasklist");
	        Process p = pb.start();
		    out = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
		    err = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getErrorStream())));
		    AppLogger.info("Window 系统进程列表");
		    int i=0;
		    int [] len = new int [5];
		    
		    
		    while ((ostr = out.readLine()) != null){
		  	  if (i<2) {
		  		  i ++ ;
		  		  continue;
		  	  }
		  	  if (i==2) {
		  		  String [] temp = ostr.split(" ");
		  		  for (int j =0;j<temp.length;j++) {
		  			   len [j] = temp[j].length();
		  		  }
		  		  i++;
		  	  } else {
		  		List<String> test = new ArrayList<String>();
		  	  int begin =0;
		  	  for (int k=0;k<len.length;k++) {
		  		test.add(ostr.substring(begin, begin + len[k]).trim());
		  		  begin = begin + len[k]+1;//跳过空格列
		  	   }
		  	  processList.add(test);
		  	  }
		    }
		    
		    String estr = err.readLine();
		    if (estr != null) {
		    	AppLogger.info("Error Info");
		    	AppLogger.info(estr);
		   }
		    AppLogger.info("size=="+processList.size());
		   // String [] [] processInfo =  processList.toArray(new String[1][1]); 
		    return TCResult.newSuccessResult(processList);
		  }catch (Exception e) {
  			AppLogger.error(e.getMessage());
  			AppLogger.error(e);
  		    return TCResult.newFailureResult(IErrorCode.getCode("SystemException"), e);
  		}
		
	}
} 