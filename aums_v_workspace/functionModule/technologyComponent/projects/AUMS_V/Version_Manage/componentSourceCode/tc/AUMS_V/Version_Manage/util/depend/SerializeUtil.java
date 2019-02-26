package tc.AUMS_V.Version_Manage.util.depend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.com.agree.afa.svc.javaengine.AppLogger;
/*
 * 文件序列号工具类
 * author 曹志鹏
 * 实现序列化和反序列化以便将信息存入redis
 */
public class SerializeUtil {
	public static byte[] serialize(Object object){
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos =null;
		try{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}catch(Exception e){
			e.printStackTrace();
			AppLogger.info("序列化异常！");
			return null;
		}
	}
	public static Object unserialize(byte[] bytes){
		ByteArrayInputStream bais = null;
		try{
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
			AppLogger.info("反序列化异常！");
			return null;
		}
	}
}
