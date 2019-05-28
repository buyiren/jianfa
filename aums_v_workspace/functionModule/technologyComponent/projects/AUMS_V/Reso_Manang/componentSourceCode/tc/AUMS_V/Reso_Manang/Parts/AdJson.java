package tc.AUMS_V.Reso_Manang.Parts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

/**
 * 将广告组装成Json格式，C端需要解析
 * 
 * @date 2018-06-22 16:32:2
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class AdJson {
	public static List j(String devId,List<Object> idleTimeLimit,List<Object> idleDefault,List<Object> transTimeLimit,List<Object> transDefault) throws IOException {
		Date date = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateNowStr = sdf.format(date);  
		OutputStream os=null;
		List list = new ArrayList<>();
		List list1 = new ArrayList<>();
		List list2 = new ArrayList<>();
		List list3 = new ArrayList<>();
		List result = new ArrayList<>();
		JSONObject j = new JSONObject();
		JSONObject j12 = new JSONObject();
		JSONObject j13= new JSONObject();
		if(idleTimeLimit==null){
			list.add("");
		}else{
			for(Object jo:idleTimeLimit) {
				list.add(jo);  
			}
		}
		if(idleDefault==null){
			list1.add("");
		}else{
			for(Object jo:idleDefault) {	
				list1.add(jo);
			}}
		if (transTimeLimit==null){
			list2.add("");
		}else{
			for(Object jo:transTimeLimit) {	
				list2.add(jo);
			}
		}
		if(transDefault==null){
			list3.add("");
		}else{
			for(Object jo:transDefault) {
				list3.add(jo);
			}
		}
		j.put("idle",j12 );
		j.put("transaction", j13);
		j12.put("timeLimitted", list);
		j12.put("default", list1);
		j13.put("timeLimitted", list2);
		j13.put("default", list3);
		System.out.println(j.toJSONString());
		//file.separator+"home"+file.separator+"afa4sj"+file.separator+"AFA4J_2.7.1_1207"+file.separator+"share"+file.separator+"excel"+file.separator+fileName+".xlsx"
		File file  = null;
		File fileParent=null;
		File filePath = null;
		List<List<String>> rootPathList=null;
		List<List<String>> jsonPathList=null;
		String root=null;
		String json=null;
		String [] devIds = devId.split(",");
		String rootPath="select  PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE='Reso_Manang' AND tpi.TRANSCODE='V105_AdTempDevInsert' AND tpi.PARAMKEYNAME='rootPath'";
		String jsonPath="select  PARAMVALUE from tp_cip_sysparameters tpi where tpi.MODULECODE='Reso_Manang' AND tpi.TRANSCODE='V105_AdTempDevInsert' AND tpi.PARAMKEYNAME='jsonPath'";
		List<?> rootOutPutParams = P_Jdbc.dmlSelect(null, rootPath, -1).getOutputParams();
		List<?> jsonOutputParams = P_Jdbc.dmlSelect(null, jsonPath, -1).getOutputParams();
		if(rootOutPutParams!=null&&!(rootOutPutParams.isEmpty())){
			rootPathList=(List<List<String>>) (rootOutPutParams.get(1));
			if (null!=rootPathList.get(0).get(0)&&!(rootPathList.get(0).get(0).isEmpty())) {
				root=rootPathList.get(0).get(0);
			}
		}else{
			root="http://192.9.200.225:8023/Ad/josn/";
		}
		if(jsonOutputParams!=null&&!(jsonOutputParams.isEmpty())){
			rootPathList=(List<List<String>>) (jsonOutputParams.get(1));
			if (null!=rootPathList.get(0).get(0)&&!(rootPathList.get(0).get(0).isEmpty())) {
				json=rootPathList.get(0).get(0);
			}
		}else{
			json="/home/afa4sj/AFA4J_2.7.1_1207/share/Ad/json";
		}
		for(int i=0;i<devIds.length;i++){
			fileParent  = new File(json+file.separator+"ad_"+devIds[i].substring(1,devIds[i].length()-1)+file.separator);  
			filePath = new File(fileParent+file.separator+"ads.json");
			String s = j.toString();
			if(!fileParent.exists()){//不存在则创建路径
				fileParent.mkdirs();
			}
			FileWriter fw = new FileWriter(filePath);  
			BufferedWriter out = new BufferedWriter(fw);

			out.write(s);
			out.flush();
			P_Logger.info("文件写入中"+filePath);
			out.close();
			StringBuffer sb = new StringBuffer();

			sb.append("ad_"+devIds[i].substring(1,devIds[i].length()-1)+file.separator+"ads.json");
			result.add(sb);
			String sqlstr2="select devId from AUMS_AD_JSON where devid ="+devIds[i];

			try {
				if(P_Jdbc.executeSQL(null, sqlstr2, true).getOutputParams().get(0).equals(0)){
					String sqlstr = "insert into AUMS_AD_JSON (ADJSONID,ADJSONNAME,UPDATETIME,DEVID) "
							+ "values "
							+ "('ad_'||(select nvl(max(to_number(substr(ADJSONID,4,length(ADJSONID)))),0)+1 from AUMS_AD_JSON),"
							+ result.get(i)+"',"
							+ dateNowStr+","
							+ devIds[i]+")";					
					P_Jdbc.executeSQL("qdzh_oracle", sqlstr, true);
				}else{

					String sqlstr = "update AUMS_AD_JSON set ADJSONNAME='"+result.get(i)+"',UPDATETIME='"+dateNowStr+"' where devid="+devIds[i];
					P_Jdbc.executeSQL("qdzh_oracle", sqlstr, true);

					P_Logger.info("1111执行的查询语句为"+sqlstr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				P_Jdbc.commit("qdzh_oracle");
			}

		}

		return result;	
	}






	/**
	 * @category 广告信息转Json
	 * @param idleDefault
	 *            入参|闲时默认广告|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param idleTimeLimit
	 *            入参|闲时限定时间广告|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param transDefault
	 *            入参|交易时默认广告|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param transTimieLimit
	 *            入参|交易时限制时间广告|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param ResultSet
	 *            出参|Json|{@link com.alibaba.fastjson.JSONObject}
	 * @return 1 成功<br/>
	 * @throws IOException 
	 */
	@InParams(param = {
			@Param(name = "devId", comment = "设备ID", type = java.lang.String.class),
			@Param(name = "idleTimeLimit", comment = "闲时限定时间广告", type = java.util.List.class),
			@Param(name = "idleDefault", comment = "闲时默认广告", type = java.util.List.class),
			@Param(name = "transTimieLimit", comment = "交易时限制时间广告", type = java.util.List.class),
			@Param(name = "transDefault", comment = "交易时默认广告", type = java.util.List.class),})
	@OutParams(param = { @Param(name = "JsonResult", comment = "Json", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "1", desp = "成功") })
	@Component(label = "广告信息转Json", style = "处理型", type = "同步组件", author = "hanbin", date = "2018-06-22 04:44:49")
	public static TCResult A_AdJson(String devId,List<Object> idleTimeLimit,List<Object> idleDefault,List<Object> transTimieLimit,List<Object> transDefault) throws IOException {
		List filePath= j(devId,idleTimeLimit, idleDefault, transTimieLimit, transDefault);
		return TCResult.newSuccessResult(filePath);
	}
}
