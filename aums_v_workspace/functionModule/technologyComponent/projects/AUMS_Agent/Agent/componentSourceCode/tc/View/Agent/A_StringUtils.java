package tc.View.Agent;

import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

/**
 * @date 2017-06-08 13:33:20
 */
@ComponentGroup(level = "应用", groupName = "字符串处理类", projectName = "AAAA", appName = "agent")
public class A_StringUtils {
	/**
	 * @category 批量字符串转整型
	 * @param srcList
	 *            入参|源列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param resultList
	 *            出参|结果列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "srcList", comment = "源列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "resultList", comment = "结果列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "批量字符串转整型", style = "判断型", type = "同步组件", author = "Anonymous", date = "2017-06-08 01:29:26")
	public static TCResult A_batchParseInt(JavaList srcList) {
		JavaList result = new JavaList();
		for(int i=0;i<srcList.size();i++){
			result.add(Integer.parseInt(srcList.getStringItem(i)));
		}
		return TCResult.newSuccessResult(result);
	}
	
	
	/**
	 * @category 数组转化为字符串
	 * @param obj
	 *            入参|源数组|
	 *            {@link java.lang.Byte}
	 * @param code
	 *            入参|编码方式
	 *            {@link java.lang.String}
	 * @param resultStr
	 *            出参|数组对应的字符串
	 *            {@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "obj", comment = "源数组", type = java.lang.Byte.class),
			  @Param(name = "code", comment = "编码方式", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "resultStr", comment = "数组对应的字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数组转化为字符串", style = "判断型", type = "同步组件", author = "lk", date = "2018-02-03 01:29:26")
	public static TCResult A_ParseByte(byte[] obj,String code) {
		String result = new String(obj);
		return TCResult.newSuccessResult(result);
	}
}
