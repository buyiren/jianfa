package tc.View.Server.utils;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
public class RMIClient {

	public static void main(String[] args) throws IOException, MalformedObjectNameException, InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		String rmiHost = "22.5.65.27";
        int rmiPort = 10086;
        int rmiJndiPort = 10087;
        int logInterval=60000;
        RuntimeInfoBean ret = RMIClient.getRuntimeInfo(rmiHost,rmiPort,rmiJndiPort,logInterval);
        System.out.println("ret:"+ret.toString());
	}
	public static RuntimeInfoBean getRuntimeInfo(String rmiHost,int rmiPort,int rmiJndiPort,int logInterval) throws IOException, MalformedObjectNameException, InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
						// TODO Auto-generated method stub
		 // 使用方法：java cn.com.agree.monitor.RMIClientTest <ip> <rmiPort> <rmiJndiPort>
        // 0. 参数：
//        String rmiHost = "22.5.65.27";
//        int rmiPort = 10086;
//        int rmiJndiPort = 10087;
        // 连接
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + rmiHost
                + ":" + rmiPort + "/jndi/rmi://" + rmiHost + ":" + rmiJndiPort
                + "/server");
        RMIConnector conn = new RMIConnector(url, null);
        conn.connect();

        // Call the remote MBeanServer
        MBeanServerConnection mbsc = conn.getMBeanServerConnection();
        ObjectName mbeanCell = new ObjectName("AB Server:type=Cell");//获取Cell信息
        
        ObjectName mbean = new ObjectName("AB Server:type=RuntimeInfo");//获取runtime信息
  /*      MBeanInfo mBeanInfo = mbsc.getMBeanInfo(mbean);
        MBeanOperationInfo[] operations = mBeanInfo.getOperations();
        for (MBeanOperationInfo mBeanOperationInfo : operations) {
        	System.out.println(mBeanOperationInfo.getName());
		}*/
        //设置属性
        mbsc.setAttribute(mbean, new Attribute("LogInterval", logInterval));
        System.out.println((Long)mbsc.getAttribute(mbean, "FreeMemory"));
        System.out.println((String)mbsc.getAttribute(mbean, "UpTime"));
        RuntimeInfoBean ret = new RuntimeInfoBean();
        ret.setAvailableProcessors((Long)mbsc.getAttribute(mbean,"AvailableProcessors"));
        ret.setFreeMemory((Long)mbsc.getAttribute(mbean,"FreeMemory"));
        ret.setMaxMemory((Long)mbsc.getAttribute(mbean, "MaxMemory"));
        String usage = (String)mbsc.getAttribute(mbean,"MemoryUsage");
        ret.setMemoryUsage(usage.substring(0, usage.length()-1));
        ret.setTotalMemory((Long)mbsc.getAttribute(mbean, "TotalMemory"));
        ret.setUpTime((String)mbsc.getAttribute(mbean,"UpTime"));
        ret.setChildCount(((Integer)mbsc.getAttribute(mbeanCell, "ChildCount")).intValue());
        
        //mbsc.invoke(mbean, "doGC", null, null);//触发gc动作
        // wait for check
//        for (int i = 1; i < 100; i++)
//        {
//            System.out.println(i);
//            Thread.sleep(1000);
//        }
        
        conn.close();
        return ret;
	}
	
}
