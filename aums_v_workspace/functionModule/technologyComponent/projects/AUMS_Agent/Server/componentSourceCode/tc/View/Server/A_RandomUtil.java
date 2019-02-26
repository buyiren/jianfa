package tc.View.Server;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.Random;

import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 数值操作
 * 
 * @date 2018-03-07 16:9:5
 */
@ComponentGroup(level = "应用", groupName = "数值操作类", projectName = "View", appName = "Server")
public class A_RandomUtil {
	
	/**
	 * @category c
	 * @param bit
	 *            入参|位数
	 *            {@link java.lang.String}
	 * @param resultStr
	 *            出参|随机数产生的对应的字符串
	 *            {@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {@Param(name = "bit", comment = "位数", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "resultStr", comment = "随机数产生的对应的字符串", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "随机数产生的对应的字符串", style = "判断型", type = "同步组件", author = "lk", date = "2018-03-07 01:29:26")
	public static TCResult A_ParseRandom(String bit) {
		String sources = "0123456789";//加上一些字母，就可以生成pc站的验证码了
		Random rand = new Random();
		StringBuffer resultStr = new StringBuffer();
		for(int i=0;i<Integer.valueOf(bit).intValue();i++){
			resultStr.append(sources.charAt(rand.nextInt(9))+ "");
		}
		return TCResult.newSuccessResult(resultStr.toString());
	}
}
