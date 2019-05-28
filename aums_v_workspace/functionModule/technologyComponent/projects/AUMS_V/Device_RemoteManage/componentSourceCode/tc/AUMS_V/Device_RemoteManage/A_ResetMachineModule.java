package tc.AUMS_V.Device_RemoteManage;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 远程机具复位列表
 * 
 * @date 2018-07-31 20:6:16
 */
@ComponentGroup(level = "应用", groupName = "远程机具操作", projectName = "AUMS_V", appName = "Device_RemoteManage")
public class A_ResetMachineModule {

	/**
	 * @category 机具复位列表
	 * @param devNum
	 *            入参|机具编号|{@link java.lang.String}
	 * @param moduleView
	 *            出参|机具外设列表|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "devNum", comment = "机具编号", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "moduleView", comment = "机具外设列表", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "机具复位列表", style = "判断型", type = "同步组件", comment = "远程机具复位列表", author = "hanbin", date = "2018-07-31 08:09:04")
	public static TCResult A_resetMachineModule(String devNum) {
		String sqlcmd1="select A.DEVNUM,C.DEVMODULELIST from AUMS_DEV_INFO A,AUMS_DEV_ASSORTMENT B,AUMS_DEV_MODEL C where A.DEVNUM='"+devNum+"' and A.DEVASSORTMENTID=B.ASSORTMENTID and B.DEVMODELID=C.DEVMODELID";
		String sqlcmd2="select * from AUMS_DEV_MODULEINFO";
		List<List<String>> list1 = (List) P_Jdbc.dmlSelect("qdzh_oracle", sqlcmd1, 0).getOutputParams().get(1);
		List<List<String>> list2 = (List) P_Jdbc.dmlSelect("qdzh_oracle", sqlcmd2, 0).getOutputParams().get(1);
		P_Logger.info(list1.get(0).get(1));
		P_Logger.info(list2);
		JSONObject jb = new JSONObject();
		StringBuffer sb=null;
		String[] modulestr = list1.get(0).get(1).split("\\|");
			for(int i=0;i<modulestr.length;i++){
				for (List<String> sp : list2) {
				if(sp.get(0).equals(modulestr[i])){
					sb=new StringBuffer();
					sb.append(sp.get(1));
					sb.append(",");
					jb.put(sp.get(0), sb.substring(0,sb.length()-1));	
				}
			}	
		}
		P_Logger.info(jb);
		//jb.put(list1.get(1).get(0).split(",")[0], sb.substring(0, sb.length()-1));
		return TCResult.newSuccessResult(jb);
	}
}
