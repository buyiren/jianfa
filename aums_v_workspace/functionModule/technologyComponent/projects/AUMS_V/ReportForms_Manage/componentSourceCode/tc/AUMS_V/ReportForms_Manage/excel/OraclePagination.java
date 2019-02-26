package tc.AUMS_V.ReportForms_Manage.excel;


import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.IOException;

import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * Oracle分页插件
 * 
 * @date 2018-06-04 14:37:6
 */
@ComponentGroup(level = "应用", groupName = "导出excel组件", projectName = "AUMS_V", appName = "ReportForms_Manage")
public class OraclePagination {
	//将字符串转化，不转化请求报类型转换错误
	public static Integer rn(String  QueryPageNo, String  QueryRowNum) {
		int rn = Integer.parseInt(QueryPageNo) * Integer.parseInt(QueryRowNum) - (Integer.parseInt(QueryRowNum) - 1);
		return rn;
	}
	public static Integer rownum(String  QueryPageNo, String  QueryRowNum) {
		int rownum = Integer.parseInt(QueryPageNo) * Integer.parseInt(QueryRowNum);
		return rownum;
	}
	/**
	 * @throws IOException
	 * @category Oracle分页插件
	 * @param QueryPageNo
	 *            入参|第几页|{@link java.lang.Integer}
	 * @param QueryRowNum
	 *            入参|每页多少行|{@link java.lang.Integer}
	 * @param rn
	 *            出参|起始行|{@link java.lang.Integer}
	 * @param rownum
	 *            出参|终止行|{@link java.lang.Integer}
	 * @return 1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "QueryPageNo", comment = "第几页", type = java.lang.Integer.class),
			@Param(name = "QueryRowNum", comment = "每页多少行", type = java.lang.Integer.class) })
	@OutParams(param = {
			@Param(name = "rn", comment = "起始行", defaultParam = "1", type = java.lang.Integer.class),
			@Param(name = "rownum", comment = "终止行", type = java.lang.Integer.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "Oracle分页插件", style = "处理型", type = "同步组件", date = "2018-06-04 03:19:57")
	public static TCResult A_ExcelPOI(Integer QueryPageNo, Integer QueryRowNum) {
		Integer rn = rn(QueryPageNo.toString(), QueryRowNum.toString());
		Integer rownum = rownum(QueryPageNo.toString(), QueryRowNum.toString());
		return TCResult.newSuccessResult(rn, rownum);
	}
}