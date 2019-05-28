package tc.platform;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialClob;

import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;

/**
 * 对象转换类组件
 * 
 * @date 2015-09-07 19:59:50
 */
@ComponentGroup(level = "平台", groupName = "对象转换类组件")
public class P_ObjectTranslator {

	/**
	 * @param bean
	 *            入参|Bean|{@link Object}
	 * @param dict
	 *            出参|Dict|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "bean", comment = "Bean", type = Object.class) })
	@OutParams(param = { @Param(name = "dict", comment = "Dict", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "Bean转成字典", style = "判断型", type = "同步组件", comment = "把Bean转成字典，bean属性的名称作为key，属性值（原封不动）作为value", date = "2015-09-07 08:03:44")
	public static TCResult convertBeanToDict(Object bean) {
		JavaDict dict = new JavaDict();
        TCResult result = fillPropsIntoDict(bean, dict);
        if (result.getStatus() == TCResult.STAT_SUCCESS) {
            return TCResult.newSuccessResult(dict);
        } else {
            return result;
        }
	}

	/**
	 * @param bean
	 *            入参|Bean|{@link Object}
	 * @param dict
	 *            入参|Dict|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "bean", comment = "Bean", type = Object.class),
			@Param(name = "dict", comment = "Dict", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "填充Bean属性到字典", style = "判断型", type = "同步组件", comment = "bean属性的名称作为key，属性值（原封不动）作为value", date = "2015-09-07 08:05:50")
	public static TCResult fillPropsIntoDict(Object bean, JavaDict dict) {
		Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field.setAccessible(fields, true);
        try {
            for (Field f : fields) {
                dict.setItem(f.getName(), f.get(bean));
            }
        } catch (Exception e) {
            return TCResult.newFailureResult(ErrorCode.HANDLING, e);
        }
        return TCResult.newSuccessResult();
	}
	
	
	/**
	 * @category blob转字节数组
	 * @param blob
	 *            入参|blob|{@link java.sql.Blob}
	 * @param bytes
	 *            出参|字节数组|byte
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	public static TCResult BlobToByte(Blob blob) {
		BufferedInputStream bis = null ;
		byte[] bytes = null ;
		try{
			bis = new BufferedInputStream(blob.getBinaryStream());
			bytes = new byte[(int)blob.length()];
			int len = bytes.length;
			int offset = 0 ;
			int readByte = 0 ;
			while(offset < len &&(readByte = (bis.read(bytes,offset,len - offset))) >= 0){
				offset += readByte ;
			}
			//关闭缓冲流，返回字节数组
			bis.close();
			return TCResult.newSuccessResult(bytes);
		}catch(SQLException e){
			AppLogger.info(e);
			return TCResult.newFailureResult("", "访问 BLOB值时发生错误");
		}catch(Exception e){
			try {
				bis.close();
			} catch (IOException e1) {
				AppLogger.info(e1);
				return TCResult.newFailureResult("", "缓冲流关闭发生异常");
			}
			AppLogger.info(e);
			return TCResult.newFailureResult("", "blob转字节数组异常");
		}
	}
	
	/**
	 * @category clob转字符串
	 * @paramc lob
	 *            入参|clob|{@link java.sql.Clob}
	 * @param bytes
	 *            出参|字符串|str
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	public static TCResult ClobToString(Clob clob) {
		try{
			String  str =  clob!=null ? clob.getSubString(1,( int) clob.length()):null;
			return TCResult.newSuccessResult(str);
		}catch(Exception e){
			AppLogger.info(e);
			return TCResult.newFailureResult("", "clob转字符串异常");
		}
	}
	
	/**
	 * @category 字符串转clob
	 *   @param bytes
	 *            入参|字符串|str
	 * @paramc lob
	 *            出参|clob|{@link java.sql.Clob}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	public static TCResult StringToClob(String str) {
		Clob clob=null ;
		try{
			clob = str!=null ?new SerialClob(str.toCharArray()):null;
			return TCResult.newSuccessResult(clob);
		}catch(Exception e){
			AppLogger.info(e);
			return TCResult.newFailureResult("", "字符串转clob异常");
		}
	}

}
