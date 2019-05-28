package tc.View.Agent;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;

import java.io.UnsupportedEncodingException;
/**
 * @date 2017-05-22 17:18:57
 */
@ComponentGroup(level = "应用", groupName = "http通讯组件", projectName = "AAAA", appName = "agent")
public class A_HttpClient {

	/**
	 * @category http客户端
	 * @param serverIp
	 *            入参|服务端afa的ip地址|{@link java.lang.String}
	 * @param serverPort
	 *            入参|服务端afa的监听端口|int
	 * @param reqDict
	 *            入参|请求容器|{@link java.lang.String}
	 * @param rspDict
	 *            出参|应答容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "serverIp", comment = "服务端afa的ip地址", type = java.lang.String.class),
			@Param(name = "serverPort", comment = "服务端afa的监听端口", type = int.class),
			@Param(name = "reqJson", comment = "请求json串", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rspJson", comment = "应答json串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "http客户端", style = "判断型", type = "同步组件", comment = "向hers服务端的afa4j请求数据，采用http协议+json报文", author = "Anonymous", date = "2017-05-22 05:38:06")
	public static TCResult A_httpClient(String serverIp, int serverPort,
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
			return TCResult.newFailureResult("BBBBBBB", "连接HERS服务器异常："+e.getMessage());
		}
		return TCResult.newSuccessResult("");
	}

}
