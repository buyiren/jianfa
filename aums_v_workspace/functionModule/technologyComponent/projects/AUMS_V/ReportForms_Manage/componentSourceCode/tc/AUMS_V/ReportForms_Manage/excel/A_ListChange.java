package tc.AUMS_V.ReportForms_Manage.excel;

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
import cn.com.agree.afa.svc.javaengine.context.JavaDict;

/**
 * 将List中的jsonObject转化为双层List
 * 
 * @date 2018-08-29 10:39:48
 */
@ComponentGroup(level = "应用", groupName = "导出excel组件", projectName = "AUMS_V", appName = "ReportForms_Manage")
public class A_ListChange {

	/**
	 * @category 将List中的jsonObject转化为双层List
	 * @param resultSetJson
	 *            入参|查询结果集|{@link java.util.List}
	 * @param order
	 *            入参|字段顺序|{@link java.lang.String}
	 * @param list
	 *            出参|双层list|{@link java.util.List}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@InParams(param = {
			@Param(name = "resultSetJson", comment = "查询结果集", type = java.util.List.class),
			@Param(name = "order", comment = "字段顺序", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "list", comment = "双层list", type = java.util.List.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "将List中的jsonObject转化为双层List", style = "判断型", type = "同步组件", comment = "将List中的jsonObject转化为双层List", date = "2018-08-29 11:10:07")
	public static TCResult A_JsonObjectToList(List<JavaDict> resultSetJson, String order) {
		String[] ordersplit = order.split(",");
		List list = null;
		List<List<Object>> resultSet = new ArrayList();
		for (int i = 0; i < resultSetJson.size(); i++) {
			list = new ArrayList();
			for (int j = 0; j < ordersplit.length; j++) {
				list.add((resultSetJson.get(i).get(ordersplit[j])).toString());
			}
			resultSet.add(list);
		}
		return TCResult.newSuccessResult(resultSet);
	}

}

