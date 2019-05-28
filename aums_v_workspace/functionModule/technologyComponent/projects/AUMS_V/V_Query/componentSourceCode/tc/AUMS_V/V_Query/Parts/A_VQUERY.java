package tc.AUMS_V.V_Query.Parts;

import tc.platform.P_Logger;
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
 * VQUERY
 * 
 * @date 2018-08-28 19:26:48
 */
@ComponentGroup(level = "应用", groupName = "VQUERY", projectName = "AUMS_V", appName = "V_Query")
public class A_VQUERY {
	
	/**
	 * @category 二位Javalist转以为Javalist
	 * @param Javalist2
	 *            入参|二维Javalist|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param Javalist1
	 *            出参|一维Javalist|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 1 成功<br/>
	 */
	@InParams(param = { @Param(name = "Javalist2", comment = "二维Javalist", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "Javalist1", comment = "一维Javalist", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "二位Javalist转以为Javalist", style = "处理型", type = "同步组件", author = "BHD", date = "2018-08-28 04:01:09")
	public static TCResult A_JavalistConvert(JavaList Javalist2) {
		JavaList Javalist1 = new JavaList();
		P_Logger.info("*********************入参二维Javalist： " + Javalist2);
		P_Logger.info("开始转格式：  ");
		if(Javalist2 != null){
			for(int i=0; i<Javalist2.size(); i++){
				P_Logger.info("*********************Javalist2.get(i).getClass： " + Javalist2.get(i).getClass());
				P_Logger.info("*********************Javalist2.get(i)： " + Javalist2.get(i));
				JavaList javalist = (JavaList) Javalist2.get(i);
				Javalist1.add(javalist.get(0));
			}
			P_Logger.info("*********************出参一维Javalist： " + Javalist1);
		}
		return TCResult.newSuccessResult(Javalist1);
	}
}
