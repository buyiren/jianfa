package tc.platform;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 列表处理类
 * 
 * @author 姚园-03958
 * @date 2016-01-04 21:50:15
 */
@ComponentGroup(level = "平台", groupName = "列表处理类")
public class P_List {
	/**
	 * @category 列表None值过滤
	 * @param resource
	 *            入参|源数据|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param result
	 *            入参|指定字符串|{@link java.lang.String}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @param resource
	 *            出参|指定字符串|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "resource", comment = "源数据", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "result", comment = "指定字符串", type = java.lang.String.class),
			@Param(name = "ver", comment = "版本日期", type = java.lang.String.class),
			@Param(name = "area", comment = "适用区域", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "resource", comment = "指定字符串", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "列表None值过滤", style = "判断型", type = "同步组件", comment = "将None或者List中的None转换成指定字符串", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_List_FilterNone(JavaList resource, String result,
			String ver, String area) {
		if (resource == null) {
			return TCResult.newSuccessResult(result);
		}
		if (!(resource instanceof JavaList)) {
			return TCResult.newSuccessResult(resource);
		}

		for (int i = 0; i < resource.size(); i++) {
			resource.set(
					i,
					P_List_FilterNone((JavaList) resource.get(i), result, ver,
							area).getOutputParams().get(0));
		}
		return TCResult.newSuccessResult(resource);
	}

	/**
	 * @category 列表追加信息
	 * @param __REQ__
	 *            入参|数据字典容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param varlist
	 *            入参|要追加的变量名称|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param list_idx
	 *            入参|追加的下标|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param value_list
	 *            入参|要追加的值|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param ver
	 *            入参|版本日期|{@link java.lang.String}
	 * @param area
	 *            入参|适用区域|{@link java.lang.String}
	 * @return 0 追加成功<br/>
	 *         1 追加失败<br/>
	 */
	@InParams(param = {
			@Param(name = "__REQ__", comment = "数据字典容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "varlist", comment = "要追加的变量名称", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "list_idx", comment = "追加的下标", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "value_list", comment = "要追加的值", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class), })
	@Returns(returns = { @Return(id = "0", desp = "追加成功"),
			@Return(id = "1", desp = "追加失败") })
	@Component(label = "列表追加信息", style = "判断型", type = "同步组件", comment = "将要追加的变量名追加至入参字典中", author = "姚园-3958", date = "2016-01-04 09:50:17")
	public static TCResult P_List_Append(JavaDict __REQ__, JavaList varlist,
			JavaList list_idx, JavaList value_list, String ver, String area) {
		if (!(__REQ__ instanceof JavaDict)) {
			return TCResult.newFailureResult(ErrorCode.AGR,
					"数据字典容器参数类型错误,不为数据字典");
		}
		if (!(varlist instanceof JavaList)) {
			return TCResult.newFailureResult(ErrorCode.AGR, "变量名称不可为空");
		}
		for (int idx = 0; idx < varlist.size(); idx++) {
			AppLogger.info((String) varlist.get(idx));
			if (list_idx.getIntItem(idx) == -1) {
				JavaList l = (JavaList) __REQ__.get(varlist.get(idx));
				l.add(value_list.get(idx));
				__REQ__.put(varlist.get(idx), l);
			} else {
				JavaList L = (JavaList) __REQ__.get(varlist.get(idx));
				JavaList l = (JavaList) ((JavaList) __REQ__.get(varlist
						.get(idx))).get(list_idx.getIntItem(idx));
				l.add(value_list.get(idx));
				L.set(list_idx.getIntItem(idx), l);
				__REQ__.put(varlist.get(idx), L);
			}
		}
		return TCResult.newSuccessResult();
	}

	/**
	 * @category 判断字符串是否在列表中
	 * @param currstr
	 *            入参|需要判断的字符|{@link java.lang.String}
	 * @param listdata
	 *            入参|列表集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param index
	 *            出参|下标|int
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "currstr", comment = "需要判断的字符", type = java.lang.String.class),
			@Param(name = "listdata", comment = "列表集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "index", comment = "下标", type = int.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "判断字符串是否在列表中", style = "判断型", type = "同步组件", date = "2017-10-18 05:55:38")
	public static TCResult P_StrInListTrue(String currstr, JavaList listdata) {
		if (listdata.contains(currstr)) {
			int index=0;
			for(int i=0;i<listdata.size();i++){
				if(listdata.get(i).equals(currstr)){
					index=i;
					break;
				}
			}
			return TCResult.newSuccessResult(index);
		} else {
			return TCResult.newFailureResult(ErrorCode.AGR, "字符串不在列表中");
		}
	}

}
