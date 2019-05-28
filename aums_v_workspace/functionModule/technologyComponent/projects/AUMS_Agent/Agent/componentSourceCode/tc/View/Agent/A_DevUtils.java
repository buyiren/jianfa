package tc.View.Agent;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 完成所有与机具的交互功能
 * @author 李阔
 * @date 2017-08-28 14:25:14
 */
@ComponentGroup(level = "应用", groupName = "与机具交互", projectName = "View", appName = "Agent")
public class A_DevUtils {
	
	/**
	 * @category http请求Afa4j
	 * @param serverIp
	 *            入参|服务端afa的ip地址|{@link java.lang.String}
	 * @param serverPort
	 *            入参|服务端afa的监听端口|int
	 * @param reqJson
	 *            入参|请求json串|{@link java.lang.String}
	 * @param rspJson
	 *            出参|应答json串|
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serverIp", comment = "服务端的ip地址", type = java.lang.String.class),
			@Param(name = "serverPort", comment = "服务端的监听端口", type = int.class),
			@Param(name = "reqJson", comment = "请求json串", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rspJson", comment = "应答json串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "http客户端post方式请求", style = "判断型", type = "同步组件", comment = "客户端发起请求到服务端，采用http协议+json报文", author = "李阔", date = "2017-08-28 14:38:06")
	public static TCResult A_httpClientPost(String serverIp, int serverPort,
			String reqJson) {
		try{
			String url="http://"+serverIp+":"+serverPort+"/";
	//		String body=body_transferLaunch_2to1;
			System.out.println("请求报文:"+reqJson);
			HttpClient client = new HttpClient();
			client.getHostConfiguration().setHost(url, serverPort);
			PostMethod  postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Content-Type","text/json");
			postMethod.setRequestHeader("charset","utf-8");  //utf-8
			postMethod.setRequestEntity(new ByteArrayRequestEntity(reqJson.getBytes("utf-8")));
			int statusCode = client.executeMethod(postMethod);
			if(statusCode == HttpStatus.SC_OK){
	            BufferedInputStream bis = new BufferedInputStream(postMethod.getResponseBodyAsStream());
	            byte[] bytes = new byte[1024];
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            int count = 0;
	            while((count = bis.read(bytes))!= -1){
	                bos.write(bytes, 0, count);
	            }    
	            byte[] strByte = bos.toByteArray();
	            String responseString = new String(strByte,0,strByte.length,"utf-8");
	            AppLogger.info("收到的应答:"+responseString);
	            bos.close();
	            bis.close();
	            return TCResult.newSuccessResult(responseString);
	        }
		}catch(UnsupportedEncodingException e1){
			return TCResult.newFailureResult("BBBBBBB", "不支持的编码："+e1.getMessage());
		}catch(IOException e){
			return TCResult.newFailureResult("BBBBBBB", "连接服务端服务器异常："+e.getMessage());
		}
		return TCResult.newSuccessResult("");
	}
	
	
	/**
	 * @category Agenthttp请求C端机具
	 * 1:测试C端程序是否连通和正常通讯 http://127.0.0.1:53002/Echo
	 * 2:设置机器状态的服务可用性（ServiceAvailability）为所有人都可用（All）,主要是应用于把机器状态从仅管理员可用调整为可提供给客户使用 http://127.0.0.1:53002/Start
	 * 3:设置机器状态的服务可用性（ServiceAvailability）为仅管理员可用（None）,主要是应用于机具需要维护  http://127.0.0.1:53002/Pause
	 * 4:设置机器状态服务可用性（ServiceAvailability）为都不可用（None），同时关闭守护进程和自助平台应用程序  http://127.0.0.1:53002/Exit
	 * 5:此命令是获取机器状态信息，机器状态信息主要包括服务可用性（ServiceAvailability）、设备占用状态（OccupancyStatus） http://127.0.0.1:53002/CatchStatus
	 * @param reqUrl
	 *            入参|请求url|{@link java.lang.String}
	 * @param rspJson
	 *            出参|应答json|
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "reqUrl", comment = "请求url", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rspJson", comment = "应答json串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "http客户端get方式请求", style = "判断型", type = "同步组件", comment = "Agent与C端机具进行通讯，采用http协议+json报文", author = "李阔", date = "2017-08-28 15:38:06")
	public static TCResult A_httpClientGet(String reqUrl) {
			AppLogger.info("请求URL:"+reqUrl);
			//构造HttpClient的实例
			
			//创建GetMethod的实例
			GetMethod getMethod=new GetMethod(reqUrl);
			
			HttpClient httpClient=new HttpClient();
			try {
	            //设置超时时间，将来通过afa平台的参数内存化来获取
				/*连接超时时间*/
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
				/*读取超时时间*/
				httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
	            //使用系统的默认的恢复策略
	            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); 
	            
	            //执行getMethod，调用http接口
            	httpClient.executeMethod(getMethod);
            	//读取响应码
            	int responseStatusLine = getMethod.getStatusLine().getStatusCode();
            	//读取响应内容 
                byte[] responseBody=getMethod.getResponseBody();
                //处理返回的内容
                String responseMsg=new String(responseBody,"UTF-8");
                AppLogger.info("响应内容:" + responseMsg);
                AppLogger.info("-------------------------------------------------");
                AppLogger.info("响应码:" + responseStatusLine);
                AppLogger.info("-------------------------------------------------");
                return TCResult.newSuccessResult(responseMsg);
                
	        } catch (Exception e) {
	            e.printStackTrace();
	            return TCResult.newFailureResult("BBBBBBB", "连接C端机具异常："+e.getMessage());
	        } finally {
	            getMethod.releaseConnection();
	        }
	}
}