package tc.AUMS_V.Reso_Manang.Parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;

import com.alibaba.fastjson.JSONObject;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 广告回显展示拼接
 * 
 * @date 2018-07-05 17:55:16
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class AdView {
	public static List query(List<List<String>> typeResult,String type){
		String path="select PARAMVALUE from TP_CIP_SYSPARAMETERS where MODULECODE='Reso_Manang' and TRANSCODE='V105_AdTempDevInsert' and PARAMKEYNAME='rootPath'";
		List<List<String>> devIdList=null;
		String rootPath="http://192.9.200.225:8023/";
		P_Logger.info("執行的sql是"+path);
		List<?> outputParams = P_Jdbc.dmlSelect(null, path, -1).getOutputParams();
		if(outputParams!=null&&
				!(outputParams.isEmpty())){
			devIdList=(List<List<String>>) (outputParams.get(1));
			if (null!=devIdList.get(0).get(0)&&!(devIdList.get(0).get(0).isEmpty())) {
				rootPath=devIdList.get(0).get(0);
				P_Logger.info("rootPath"+rootPath);
				
			}
		}
        List<List<String>> result = new ArrayList();
		List<List<String>> list = null;
		String[] split = typeResult.get(0).get(0).split(",");
		for(int i=0;i<split.length;i++){
			P_Logger.info("admanageid"+split[i]);
			String sqlstr1="select ADMANAGEID,'"+rootPath+"'||ADPATH from AUMS_AD_MANAGE where ADMANAGEID='"+split[i]+"'";
			P_Logger.info("執行的是"+sqlstr1);
			list = (List<List<String>>) 
					(P_Jdbc.dmlSelect(null, sqlstr1, -1).getOutputParams().get(1));
			
			list.get(0).add(type);
			
			result.add(list.get(0));
		}
		return result;
	}
	public static String View(Object tempId){
		List<List<List<String>>> result = new ArrayList<>();
		List list1 = new ArrayList<>();
		List list2 = new ArrayList<>();
		List list3 = new ArrayList<>();
		List list4 = new ArrayList<>();
		JSONObject json = new JSONObject();
		JSONObject json2 = null;
		json.put("IDLETIMELIMIT", list1);
		json.put("IDLEDEFAULT", list2);
		json.put("TRANSTIMELIMIT", list3);
		json.put("TRANSDEFAULT", list4);
		String IDLETIMELIMIT = "select IDLETIMELIMIT from AUMS_AD_TEMP where ADTEMPID='"+tempId+"'";
		String IDLEDEFAULT = "select IDLEDEFAULT from AUMS_AD_TEMP where ADTEMPID='"+tempId+"'";
		String TRANSTIMELIMIT = "select TRANSTIMELIMIT from AUMS_AD_TEMP where ADTEMPID='"+tempId+"'";
		String TRANSDEFAULT = "select TRANSDEFAULT from AUMS_AD_TEMP where ADTEMPID='"+tempId+"'";
		List<List<String>> IDLETIMELIMITlist = (List<List<String>>) 
				(P_Jdbc.dmlSelect(null, IDLETIMELIMIT, -1).getOutputParams().get(1));
		P_Logger.info("IDLETIMELIMITlist是"+IDLETIMELIMITlist);
		List<List<String>> IDLEDEFAULTlist = (List<List<String>>) 
				(P_Jdbc.dmlSelect(null, IDLEDEFAULT, -1).getOutputParams().get(1));
		P_Logger.info("IDLEDEFAULTlist是"+IDLEDEFAULTlist);
        List<List<String>> TRANSTIMELIMITlist = (List<List<String>>) 
				(P_Jdbc.dmlSelect(null, TRANSTIMELIMIT, -1).getOutputParams().get(1));
		P_Logger.info("TRANSTIMELIMITlist是"+TRANSTIMELIMITlist);
		List<List<String>> TRANSDEFAULTlist = (List<List<String>>) 
				(P_Jdbc.dmlSelect(null, TRANSDEFAULT, -1).getOutputParams().get(1));
		P_Logger.info("TRANSDEFAULTlist是"+TRANSDEFAULTlist);
		if(IDLETIMELIMITlist!=null){
			result.add((List<List<String>>) query(IDLETIMELIMITlist, "IDLETIMELIMIT"));
		}
		if(IDLEDEFAULTlist!=null){
			result.add((List<List<String>>) query(IDLEDEFAULTlist, "IDLEDEFAULT"));
		}
		if(TRANSTIMELIMITlist!=null){
			result.add((List<List<String>>) query(TRANSTIMELIMITlist, "TRANSTIMELIMIT"));
		}
		if(TRANSDEFAULTlist!=null){
			result.add((List<List<String>>) query(TRANSDEFAULTlist, "TRANSDEFAULT"));
		}
		P_Logger.info("最终的拼接List是"+result);
		for(List<List<String>>r:result){
		for (  List list6 :r) {
			if(list6.get(2).equals("IDLETIMELIMIT")){
				json2 = new JSONObject();
				json2.put("id", list6.get(0));
				json2.put("path", list6.get(1));
				list1.add(json2);
			}
			else if(list6.get(2).equals("IDLEDEFAULT")){
				json2 = new JSONObject();
				json2.put("id", list6.get(0));
				json2.put("path", list6.get(1));
				list2.add(json2);
			}
			else if(list6.get(2).equals("TRANSTIMELIMIT")){
				json2 = new JSONObject();
				json2.put("id", list6.get(0));
				json2.put("path", list6.get(1));
				list3.add(json2);
			}
			else if(list6.get(2).equals("TRANSDEFAULT")){
				json2 = new JSONObject();
				json2.put("id", list6.get(0));
				json2.put("path", list6.get(1));
				list4.add(json2);
			}
		}
		}
		P_Logger.info(json.toString());
		return json.toString();
	}
	/**
	 * @category 查询广告信息
	 * @param tempId
	 *            入参|模板名称|{@link Object}
	 * @param ResultSet
	 *            出参|返回结果|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "tempId", comment = "模板名称", type = Object.class) })
	@OutParams(param = { @Param(name = "ResultSet", comment = "返回结果", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "查询广告信息", style = "判断型", type = "同步组件", comment = "查询广告信息", author = "hanbin", date = "2018-07-05 05:58:39")
	public static TCResult A_AdQuery(Object tempId) {
		String view = View(tempId);
		return TCResult.newSuccessResult(view);
	}
}
