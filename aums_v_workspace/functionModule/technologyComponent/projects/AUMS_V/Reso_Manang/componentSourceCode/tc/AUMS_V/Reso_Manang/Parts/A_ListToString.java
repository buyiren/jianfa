package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.ArrayList;
import java.util.List;

import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 集合转字符串
 * 
 * @date 2018-07-10 11:16:51
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class A_ListToString {
		public static String splist(List<List<String>> list){
			StringBuffer sb = new StringBuffer();
			for (List<String> list2 : list) {
				sb.append(list2.get(0));
				sb.append("','");
			}
			String devids="'"+sb.substring(0,sb.length()-2).toString();
			return devids;
			
		}
	

	/**
	 * @category 转换
	 * @param list
	 *            入参|传入集合格式|{@link java.util.List}
	 * @param string
	 *            出参|输入字符串格式|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "list", comment = "传入集合格式", type = java.util.List.class) })
	@OutParams(param = { @Param(name = "string", comment = "输入字符串格式", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "转换", style = "判断型", type = "同步组件", comment = "转换类型", date = "2018-07-10 11:23:25")
	public static TCResult A_parse(List<List<String>> list) {
		
		return TCResult.newSuccessResult(splist(list));
	}

}
