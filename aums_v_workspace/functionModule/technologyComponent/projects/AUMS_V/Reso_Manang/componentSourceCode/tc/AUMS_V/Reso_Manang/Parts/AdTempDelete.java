package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

import com.alibaba.fastjson.JSONObject;

/**
 * 广告模板删除判断
 * 
 * @date 2018-07-05 14:57:34
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class AdTempDelete {
	@SuppressWarnings("unchecked")
	public static List query(List<List<String>> typeResult,String type){
		List<List<String>> result = new ArrayList();
		List<List<String>> list = null;
		String[] split = typeResult.get(0).get(0).split(",");
		for(int i=0;i<split.length;i++){
			String sqlstr1="select ENDDATE||ENDTIME,ADMANAGEID from AUMS_AD_MANAGE where ADMANAGEID='"+split[i]+"'";
			P_Logger.info("执行的sql是>>>>>>>>"+sqlstr1);
			list = (List<List<String>>) 
					(P_Jdbc.dmlSelect(null, sqlstr1, -1).getOutputParams().get(1));
			list.get(0).add(type);
			result.add(list.get(0));
		}
		P_Logger.info("查询admanageid"+result);
		return result;
	}
	/**
	 * @category chooseDel
	 * @param tempId
	 *            入参|模板Id|{@link Object}
	 * @param result
	 *            出参|不可刪除的模板集合|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "tempId", comment = "模板Id", type = Object.class) })
	@OutParams(param = { @Param(name = "result", comment = "不可刪除的模板集合", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "chooseDel", style = "判断型", type = "同步组件", comment = "判断是否可以删除广告模板", date = "2018-08-20 09:20:24")
	@SuppressWarnings("unchecked")
	public static TCResult A_choose(Object tempId) {
		try {
			String[] tempIds = ((String) (tempId)).split(",");
			Set<String> set = new HashSet();
			List<List<String>> devIdList=null;
			for (int i = 0; i < tempIds.length; i++) {
				String deleteTempid="select DEVID from AUMS_AD_TEMP_TO_DEV where tempid = '"+ tempIds[i] + "'";
				P_Logger.info("執行的sql是"+deleteTempid);
				List<?> outputParams = P_Jdbc.dmlSelect(null, deleteTempid, -1).getOutputParams();
				P_Logger.info(outputParams);
				if(outputParams!=null&&
						!(outputParams.isEmpty())){
					devIdList=(List<List<String>>) (outputParams.get(1));
					if (null!=devIdList.get(0).get(0)&&!(devIdList.get(0).get(0).isEmpty())) {
						P_Logger.info("发布过,需要判断是否过期");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						Date date = new Date();
						String now = sdf.format(date);
						List<List<List<String>>> result = new ArrayList<>();

						String IDLETIMELIMIT = "select IDLETIMELIMIT from AUMS_AD_TEMP where ADTEMPID='"
								+ tempIds[i] + "'";
						String IDLEDEFAULT = "select IDLEDEFAULT from AUMS_AD_TEMP where ADTEMPID='"
								+ tempIds[i] + "'";
						String TRANSTIMELIMIT = "select TRANSTIMELIMIT from AUMS_AD_TEMP where ADTEMPID='"
								+ tempIds[i] + "'";
						String TRANSDEFAULT = "select TRANSDEFAULT from AUMS_AD_TEMP where ADTEMPID='"
								+ tempIds[i] + "'";
						List<List<String>> IDLETIMELIMITlist = (List<List<String>>) (P_Jdbc
								.dmlSelect(null, IDLETIMELIMIT, -1).getOutputParams()
								.get(1));
						List<List<String>> IDLEDEFAULTlist = (List<List<String>>) (P_Jdbc
								.dmlSelect(null, IDLEDEFAULT, -1).getOutputParams()
								.get(1));
						List<List<String>> TRANSTIMELIMITlist = (List<List<String>>) (P_Jdbc
								.dmlSelect(null, TRANSTIMELIMIT, -1).getOutputParams()
								.get(1));
						List<List<String>> TRANSDEFAULTlist = (List<List<String>>) (P_Jdbc
								.dmlSelect(null, TRANSDEFAULT, -1).getOutputParams()
								.get(1));
						P_Logger.info("TRANSDEFAULTlist"+TRANSDEFAULTlist);

						if (null!=IDLETIMELIMITlist.get(0).get(0)&&!(IDLETIMELIMITlist.get(0).get(0).isEmpty())) {

							P_Logger.info("AAAAAAAA");
							result.add((List<List<String>>) query(IDLETIMELIMITlist,
									"IDLETIMELIMIT"));
						}
						P_Logger.info("resultgogogogogogogg"+result);
						if (null!=IDLEDEFAULTlist.get(0).get(0)&&!(IDLEDEFAULTlist.get(0).get(0).isEmpty())) {
							P_Logger.info("22222222222222222");
							result.add((List<List<String>>) query(IDLEDEFAULTlist,
									"IDLEDEFAULT"));
						}
						if (null!=TRANSTIMELIMITlist.get(0).get(0)&&!(TRANSTIMELIMITlist.get(0).get(0).isEmpty())) {
							P_Logger.info("333333333333333");
							result.add((List<List<String>>) query(TRANSTIMELIMITlist,
									"TRANSTIMELIMIT"));
						}
						if (null!=TRANSDEFAULTlist.get(0).get(0)&&!(TRANSDEFAULTlist.get(0).get(0).isEmpty())){
							P_Logger.info("4444444444444444444");
							result.add((List<List<String>>) query(TRANSDEFAULTlist,
									"TRANSDEFAULT"));
						}
						P_Logger.info("result&&&&&&&&&&&&&&&&&"+result);
						for (List<List<String>> r : result) {
							P_Logger.info("result"+result);	
							for (int j = 0; j < r.size(); j++) {
								String end = r.get(j).get(0);
								String nowtime = sdf.format(sdf.parse(now));
								int endtime = Integer.parseInt(end.substring(0, 8));
								int time = Integer.parseInt(nowtime.substring(0, 8));
								if (endtime > time) {
									P_Logger.info("endtime"+endtime+"time"+time);
									P_Logger.info("不能删除" + endtime + ">>>>>" + time);
									set.add(tempIds[i]);
									break;
								};
							}
						}
					}
				}
			}
			P_Logger.info("不能删除的集合是" + set);
			StringBuffer sb = new StringBuffer();
			for (String tempid : set) {
				sb.append(tempid);
				sb.append("','");
			}
			P_Logger.info("查詢的字符串是"+sb);
			JSONObject json = null;;
			List res = new ArrayList();
			if(sb.length()>3){
				String r = "'" + sb.substring(0, sb.length() - 2);
				String sqlstr = "select ADTEMPID,TEMPNAME from AUMS_AD_TEMP where ADTEMPID in ("
						+ r + ")";
				List<List<String>> list = (List<List<String>>) (P_Jdbc.dmlSelect(null,
						sqlstr, -1).getOutputParams().get(1));

				for (List<String> list1 : list) {
					json = new JSONObject();
					json.put("adtempId",list1.get(0));
					json.put("adtempName",list1.get(1));
					res.add(json);
				}
			}
			P_Logger.info("最後的json" + res);
			return TCResult.newSuccessResult(res);
		}



		catch (Exception e) {

			e.printStackTrace();
		}finally{
			P_Jdbc.rollBack(null);
		}
		return  TCResult.newSuccessResult(new ArrayList<>());
	}
}