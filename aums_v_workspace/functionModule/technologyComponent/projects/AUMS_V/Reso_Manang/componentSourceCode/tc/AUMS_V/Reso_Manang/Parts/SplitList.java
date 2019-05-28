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

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 判断并分解List
 * 
 * @date 2018-07-03 12:49:5
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class SplitList {
	public static String devid(Object publicType,Object typeVel){
		StringBuffer sb = new StringBuffer();
		switch  (Integer.parseInt((String) publicType)){
		case 1:  
			String[] split = ((String)(typeVel)).split(",");
					for(String str:split){
						sb.append(str);
						sb.append("','");
					}		
					P_Logger.info("按设备发布1");
				break;
		case 2 :
			P_Logger.info("按机构发布");
			StringBuffer typeVelList= new StringBuffer();
			for(String str:((String)(typeVel)).split(",")){
				typeVelList.append(str);
				typeVelList.append("','");
			}
			
			String string = "'"+typeVelList.substring(0,typeVelList.length()-2).toString();
			P_Logger.info("string     "+string);
			String sqlstr="select devid from AUMS_DEV_INFO where DEVBRANCHID in ("+string+")";
			P_Logger.info("执行的sql="+sqlstr);
			List<List<String>> list =   (List<List<String>>) P_Jdbc.dmlSelect(null, sqlstr, -1).getOutputParams().get(1);
			for(List<String> list1:list){
				sb.append(list1.get(0));
				sb.append("','");
			}
			P_Logger.info("list"+list);
		
			break;
		case 3 :
			P_Logger.info("按区域发布");
			String sqlstr2="select DEVID from AUMS_DEV_INFO where DEVBRANCHID in "
					+ "(select BRANCHID from AUMS_BRANCH_GROUP_RELATION where BRANGROUPID ='"+typeVel+"')";
			P_Logger.info("执行的sql="+sqlstr2);
			List<List<String>> list2 =   (List<List<String>>) P_Jdbc.dmlSelect(null, sqlstr2, -1).getOutputParams().get(1);
			P_Logger.info("list"+list2);
			for(List<String> list1:list2){
				sb.append(list1.get(0));
				sb.append("','");
			}
			if(list2==null){
				P_Logger.info("该区域下没有设备");
				break;
				
			}break;
		case 4:
			P_Logger.info("按设备分类发布");
			StringBuffer typeVelList1= new StringBuffer();
			for(String str:((String)(typeVel)).split(",")){
				typeVelList1.append(str);
				typeVelList1.append("','");
			}
			
			String string1 = "'"+typeVelList1.substring(0,typeVelList1.length()-2).toString();
			P_Logger.info("string     "+string1);
			
			String sqlstr3="select DEVID from AUMS_DEV_INFO where DEVASSORTMENTID in ("+string1+")";
			P_Logger.info("执行的sql="+sqlstr3);
			List<List<String>> list3 =   (List<List<String>>) P_Jdbc.dmlSelect(null, sqlstr3, -1).getOutputParams().get(1);
			for(List<String> list1:list3){
				sb.append(list1.get(0));
				sb.append("','");
			}
			P_Logger.info("list"+list3);
		
			break;
			}
		
		
		String devids = "'"+sb.substring(0,sb.length()-2);
		return devids;
	}
	/**
	 * @category 判断并拆分list
	 * @param publicType
	 *            入参|发布类型|{@link Object}
	 * @param typeVel
	 *            入参|类型值|{@link java.lang.String}
	 * @param devId
	 *            出参|设备Id|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "publicType", comment = "发布类型", type = Object.class),
			@Param(name = "typeVel", comment = "类型值", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "devId", comment = "设备Id", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "判断并拆分list", style = "判断型", type = "同步组件", date = "2018-07-05 08:25:56")
	public static TCResult A_splittostring(Object publicType, Object typeVel) {
		String devids = devid(publicType, typeVel);
		P_Logger.info("devids>>>>>>>>>>>>>>"+devids);
		return TCResult.newSuccessResult(devids);
	}
}
