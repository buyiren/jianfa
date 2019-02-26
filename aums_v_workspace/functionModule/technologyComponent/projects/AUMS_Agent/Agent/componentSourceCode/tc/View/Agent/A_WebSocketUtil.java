package tc.View.Agent;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import tc.platform.P_Logger;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 通过websocket方式访问外设
 * 
 * @date 2018-08-04 11:41:38
 */
@ComponentGroup(level = "应用", groupName = "webSocket", projectName = "AUMS_Agent", appName = "Agent")
public class A_WebSocketUtil extends WebSocketClient{
	public String sendJSON;
	public String recvJSON;
	public boolean finishFlag=false;
	public boolean isFinishFlag() {
		return finishFlag;
	}

	public String getRecvJSON() {
		P_Logger.info("WebSocketUtil,等待应答..." );
		
		int intervalInMillSecond=10;int waitTimeInMillSecond=20000;
		while(waitTimeInMillSecond>0){
			try{
				TimeUnit.MILLISECONDS.sleep(intervalInMillSecond);
				if(isFinishFlag())break;
				waitTimeInMillSecond-=intervalInMillSecond;
			}catch(Exception e){}
		}
		P_Logger.info("WebSocketUtil,收到应答" );
		
		return recvJSON;
	}

	public A_WebSocketUtil( URI serverUri , Draft draft ) {
		super( serverUri, draft );
	}

	public A_WebSocketUtil( URI serverURI,String sendJSON ) {
		super( serverURI );
		P_Logger.info("WebSocketUtil,构造方法" );
		this.sendJSON=sendJSON;
	}

	public A_WebSocketUtil( URI serverUri, Map<String, String> httpHeaders ) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
//		send("{\"Code\":\"US001\",\"Id\":\"AB123BDEF3432DFFAC\"}");
		P_Logger.info("WebSocketUtil,opened connection" );
		send(this.sendJSON);
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage( String message ) {
		P_Logger.info("WebSocketUtil,received: " + message );
		this.finishFlag=true;
		this.recvJSON=message;
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		P_Logger.info("WebSocketUtil,Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	
	
	//---------------------------------------------------------
	

	/**
	 * @category websocket访问
	 * @param URI
	 *            入参|URI地址|{@link java.lang.String}
	 * @param json
	 *            入参|请求json|{@link java.lang.String}
	 * @param recvJSON
	 *            出参|socket请求返回json|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "URI", comment = "URI地址", type = java.lang.String.class),
			@Param(name = "json", comment = "请求json", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "recvJSON", comment = "socket请求返回json", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "websocket访问", style = "判断型", type = "同步组件", comment = "websocket访问", author = "hanbin", date = "2018-08-04 11:43:54")
	public static TCResult A_websocket(String URI, String json) {
		A_WebSocketUtil c = null;
		try {
//			c = new A_WebSocketUtil( new URI( "ws://localhost:8200" ),
//					"{\"Code\":\"AS001\",\"Id\":\"9b787954-fba4-43c1-8214-c8b457941e1b\"}"); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
			c = new A_WebSocketUtil( new URI( URI ),json); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
			c.connect();
			
			P_Logger.info("得到返回:"+c.getRecvJSON());
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			c.close();
		}
		return TCResult.newSuccessResult(c.getRecvJSON());
	}
}
