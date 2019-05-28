package tc.AUMS_V.Reso_Manang.Parts;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import tc.platform.P_Jdbc;
import tc.platform.P_Logger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 广告模板删除判断
 * 
 * @date 2018-07-05 14:57:34
 */
@ComponentGroup(level = "应用", groupName = "Parts", projectName = "AUMS_V", appName = "Reso_Manang")
public class TempDel {
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
			P_Logger.info("result"+result);
		}
		return result;
	}

	/**
	 * @category tempDel
	 * @param tempId
	 *            入参|模板Id|{@link Object}
	 */
	@InParams(param = { @Param(name = "tempId", comment = "模板Id", type = Object.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "tempDel", style = "判断型", type = "同步组件", comment = "判断是否可以删除广告模板", date = "2018-08-21 10:45:17")
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
			StringBuffer sb = new StringBuffer();

			P_Logger.info("开始判筛选不可删除的");
			if(set==null||set.isEmpty()){
				P_Logger.info("所有请求都可以删除");
				String[] qingqiu = ((String) (tempId)).split(",");
				for(String qingq:qingqiu){
					sb.append(qingq);
					sb.append("','");	
				}
				String r = "'" + sb.substring(0, sb.length() - 2);
				String del="delete from AUMS_AD_TEMP where ADTEMPID in ("+ r + ")";
				P_Logger.info("执行的sql是"+del);
				
				P_Jdbc.executeSQL(null, del, false);
				P_Jdbc.commit(null);
			}else{
				for (String tempid : set) {
					sb.append(tempid);
					sb.append(",");		
				}
				String[] qingqiu = ((String) (tempId)).split(",");
				String sb1 = sb.substring(0, sb.length()-1 );
				P_Logger.info("sb1"+sb1);
				String[] keshanchu = sb1.split(",");
				P_Logger.info("请求的是"+tempId);
				P_Logger.info("可以删除的是"+sb1);	
				StringBuffer bukeshanchu = new StringBuffer();	

				for(int i=0;i<qingqiu.length;i++){
					P_Logger.info("进入循环了");
					boolean flag=false;
					for(int j=0;j<keshanchu.length;j++){
						P_Logger.info("进入2层循环了"+"qingqiu"+qingqiu[i]+"keshanchu"+keshanchu[j]);
						if(qingqiu[i].equals(keshanchu[j])){
							flag=true;
							P_Logger.info("相同");
						}		
					}
					if(flag==false){
						bukeshanchu.append(qingqiu[i]);
						bukeshanchu.append("','");
						P_Logger.info("找到了");
						P_Logger.info("可以删除的AAAAA"+bukeshanchu);
					}

				}
				P_Logger.info("可以删除的AAAAA"+bukeshanchu);
				if(bukeshanchu!=null){
					String r = "'" + bukeshanchu.substring(0, bukeshanchu.length() - 2);
					String del="delete from AUMS_AD_TEMP where ADTEMPID in ("+ r + ")";
					P_Logger.info("执行的sql是"+del);
					P_Jdbc.executeSQL(null, del, false);
					P_Jdbc.commit(null);	
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}finally{
			P_Jdbc.rollBack(null);
		}
		return  TCResult.newSuccessResult();
	}
}
