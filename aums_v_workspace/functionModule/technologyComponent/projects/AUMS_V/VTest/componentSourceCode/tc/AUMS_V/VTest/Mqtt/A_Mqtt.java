package tc.AUMS_V.VTest.Mqtt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tc.bank.constant.BusException;
import tc.platform.P_Jdbc;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;



import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 导入机构信息
 * 
 * @date 2018-07-04 11:22:5
 */
@ComponentGroup(level = "应用", groupName = "MQTT服务", projectName = "AUMS_V", appName = "Branch_Manage")
public class A_Mqtt {


	public static final String HOST = "tcp://192.9.200.223:61613";
    //定义一个主题
    public static String TOPIC = "";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "afa4sj";
    
    private static MqttClient client;
    private static MqttTopic topicpub;
    private static String userName = "admin";
    private static String passWord = "password";

    private static MqttMessage message;

	/**
	 * @category 导入机构
	 * @param filename
	 *            入参|文件名| {@link String}
	 * @throws BusException
	 */
	@InParams(param = {
			@Param(name = "topic", comment = "主题", type = java.lang.String.class),
	        @Param(name = "pubLishContext", comment = "发布内容", type = java.lang.String.class)})
	@Returns(returns = { @Return(id = "0", desp = "不可用"),
			@Return(id = "1", desp = "可用") })
	@Component(label = "发布主题", style = "判断型", type = "同步组件", comment = "发布主题", author = "gaoxin", date = "2018-05-07 07:13:02")
	
	public static TCResult ImportBranchInfo(String topic,String pubLishContext)
			throws BusException {
        try {
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
			TOPIC=topic;
	        connect();
	        message = new MqttMessage();
	        message.setQos(2);
	        message.setRetained(true);

	        message.setPayload(pubLishContext.getBytes());
	        publish(topicpub , message);



	        AppLogger.info(message.isRetained() + "------ratained状态");
	        disConnect();
		} catch (MqttException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}



		return new TCResult(1, "000000", "发布成功");
        


	}
	
    /**
    *
    * @param topic
    * @param message
    * @throws MqttPersistenceException
    * @throws MqttException
    */
    public static void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
    MqttException {
    	MqttDeliveryToken token = topic.publish(message);
    	token.waitForCompletion();
    	AppLogger.info("message is published completely! "+ token.isComplete());
}
    private static void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);

            topicpub = client.getTopic(TOPIC);
            int[] Qos = {2};
            String[] topic1 = {TOPIC};

            client.subscribe(topic1,Qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void disConnect(){
        try {
            client.disconnect();
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
    public static String getTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }
}
