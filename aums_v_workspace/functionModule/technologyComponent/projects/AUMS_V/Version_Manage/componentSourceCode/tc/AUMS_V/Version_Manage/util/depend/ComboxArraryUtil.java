package tc.AUMS_V.Version_Manage.util.depend;

import java.util.List;

import tc.platform.P_Jdbc;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ComboxArraryUtil {
	// 设备品牌
	public static JSONArray devPpList(){
		String sql = "select DEVBRANDINFO,DEVBRANDID from T_PCVA_DEVBRAND";
		JSONArray data = new JSONArray();
		JSONObject obj0 = new JSONObject();
		obj0.put("desp", "--请选择--");
		obj0.put("value", "");
		data.add(obj0);
		try {
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objArr =  (Object[]) list.get(i);	
					JSONObject obj = new JSONObject();
					obj.put("desp",objArr[0]==null?"":objArr[0]);//品牌名称
					obj.put("value",objArr[1]==null?"":objArr[1]);//品牌编号
					data.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
		return data;
	}
	//设备厂商
	public static JSONArray devCsList(){
		String sql = "select devcsid,devcsname from T_PCVA_DEVCSINFO";
		JSONArray data = new JSONArray();
		try {
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			JSONObject obj0 = new JSONObject();
			obj0.put("desp", "--请选择--");
			obj0.put("value", "");
			data.add(obj0);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objArr = (Object[]) list.get(i);	
					JSONObject obj = new JSONObject();
					obj.put("desp",objArr[1]==null?"":objArr[1]);//厂商名称
					obj.put("value",objArr[0]==null?"":objArr[0]);//厂商编号
					data.add(obj);
				}
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return data;
		}
		return data;
	}
	//设备服务商
	public static JSONArray devFwsList(){
		String sql = "select fwsid,fwsname from T_PCVA_FWSINFO";
		JSONArray data = new JSONArray();
		try {
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			JSONObject obj0 = new JSONObject();
			obj0.put("desp", "--请选择--");
			obj0.put("value", "");
			data.add(obj0);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objArr = (Object[]) list.get(i);	
					JSONObject obj = new JSONObject();
					obj.put("desp",objArr[1]==null?"":objArr[1]);//服务商名称
					obj.put("value",objArr[0]==null?"":objArr[0]);//服务商名编号
					data.add(obj);
				}
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return data;
		}
		return data;
	}
	//设备类型
	public static JSONArray devTypeList(){
		String sql = "select  distinct(t2.devtype),devname from t_pcva_devbrand t1,t_pcva_devlxinfo t2 where t1.devtype=t2.devtypeid";
		JSONArray data = new JSONArray();
		try {
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			JSONObject obj0 = new JSONObject();
			obj0.put("desp", "--请选择--");
			obj0.put("value", "");
			data.add(obj0);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objArr = (Object[]) list.get(i);	
					JSONObject obj = new JSONObject();
					obj.put("desp",objArr[0]+"："+objArr[1]);//设备类型：设备名称
					obj.put("value",objArr[0]==null?"":objArr[0]);//品牌编号
					data.add(obj);
				}
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return data;
		}
		return data;
	}
	//设备类型
	public static JSONArray devLxList(String devSort){
		StringBuffer sql = new StringBuffer("select devtypeid,devtype,devname from T_PCVA_DEVLXINFO where 1=1");
		if(StringUtil.isNotEmpty(devSort)&&devSort!=null){
			sql.append(" and devsort = '"+devSort+"'");
		}
		JSONArray data = new JSONArray();
		try {
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql.toString(), -1).getOutputParams().get(1);
			JSONObject obj0 = new JSONObject();
			obj0.put("desp", "--请选择--");
			obj0.put("value", "");
			data.add(obj0);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objArr = (Object[]) list.get(i);	
					JSONObject obj = new JSONObject();
					obj.put("desp",objArr[1]+"："+objArr[2]);//设备类型
					obj.put("value",objArr[0]==null?"":objArr[0]);//设备类型
					data.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
		return data;
	}
	//设备型号
	public static JSONArray devTypeIdList(){
		String sql = "select distinct devtypeid,devtype,devname from T_PCVA_DEVLXINFO";
		JSONArray data = new JSONArray();

		try {
			JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			JSONObject obj1 = new JSONObject();
			obj1.put("desp", "--请选择--");// 设备类型编号
			obj1.put("value", "");// 设备类型编号
			data.add(obj1);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Object []objArr = (Object[]) list.get(i);
					JSONObject obj = new JSONObject();
					obj.put("desp", objArr[1] == null ? "" : objArr[1]);// 设备类型名称
					obj.put("value", objArr[0] == null ? "" : objArr[0]);// 设备类型编号
					obj.put("devtype", objArr[1] == null ? "" : objArr[1]+" "+objArr[2]);// 设备类型
					data.add(obj);
				}
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return data;
		}

		return data;
	}
	//设备设立形式下拉列表
	public static JSONArray devslxsList(){
		JavaList list=null;
		JSONArray data = new JSONArray();
		String sql = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='DEVWFCOMBOX' and paramkeyname ='DEVSLXS'";
		try {
			list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if(list!=null){
				JSONObject obj0 = new JSONObject();
				obj0.put("desp", "--请选择--");
				obj0.put("value", "0");
				data.add(obj0);
				String devslxsList = (String) list.get(0);
				String [] devslxs = devslxsList.split("#");
				for(int i=0;i<devslxs.length;i++){
					JSONObject obj = new JSONObject();
					String[] devsl = devslxs[i].split("\\|");
					obj.put("desp", devsl[1]);
					obj.put("value", devsl[0]);
					data.add(obj);
				}
			}
		}
		catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return data;
		}
		return data;
	}
	//设备使用状态下拉列表
	public static JSONArray devinusList() {
		JavaList list=null;
		JSONArray data = new JSONArray();
		String sql = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='DEVWFCOMBOX' and paramkeyname ='DEVINUSE'";
		//JSONArray data = new JSONArray();
		try {
			list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if(list!=null){
				JSONObject obj0 = new JSONObject();
				obj0.put("desp", "--请选择--");
				obj0.put("value", "");
				data.add(obj0);
				String devslxsList = (String) list.get(0);
				String [] devslxs = devslxsList.split("#");
				for(int i=0;i<devslxs.length;i++){
					JSONObject obj = new JSONObject();
					String[] devsl = devslxs[i].split("\\|");
					obj.put("desp", devsl[1]);
					obj.put("value", devsl[0]);
					data.add(obj);
				}
			}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return data;
			}
		return data;
	}
	//设备安装方式下拉列表
	public static JSONArray devazfsList() {
		List<?> list=null;
		JSONArray data = new JSONArray();
		String sql = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='DEVWFCOMBOX' and paramkeyname ='DEVAZFS'";
		//JSONArray data = new JSONArray();
		try {
			list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if(list!=null){
				JSONObject obj0 = new JSONObject();
				obj0.put("desp", "--请选择--");
				obj0.put("value", "");
				data.add(obj0);
				String devslxsList = (String) list.get(0);
				String [] devslxs = devslxsList.split("#");
				for(int i=0;i<devslxs.length;i++){
					JSONObject obj = new JSONObject();
					String[] devsl = devslxs[i].split("\\|");
					obj.put("desp", devsl[1]);
					obj.put("value", devsl[0]);
					data.add(obj);
				}
			}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return data;
			}
		return data;
	}
	//安装区域
	public static JSONArray devazqyList() {
		List<?> list=null;
		JSONArray data = new JSONArray();
		String sql = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='DEVWFCOMBOX' and paramkeyname ='DEVAZQY'";
		try {
			list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if(list!=null){
				JSONObject obj0 = new JSONObject();
				obj0.put("desp", "--请选择--");
				obj0.put("value", "");
				data.add(obj0);
				String devslxsList = (String) list.get(0);
				String [] devslxs = devslxsList.split("#");
				for(int i=0;i<devslxs.length;i++){
					JSONObject obj = new JSONObject();
					String[] devsl = devslxs[i].split("\\|");
					obj.put("desp", devsl[1]);
					obj.put("value", devsl[0]);
					data.add(obj);
				}
			}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return data;
			}
		return data;
	}
	//设备功能区域下拉列表
	public static JSONArray devgnqyList() {
		List<?> list=null;
		JSONArray data = new JSONArray();
		String sql = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='DEVWFCOMBOX' and paramkeyname ='DEVGNQY'";
		//JSONArray data = new JSONArray();
		try {
			list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
			if(list!=null){
				JSONObject obj0 = new JSONObject();
				obj0.put("desp", "--请选择--");
				obj0.put("value", "");
				data.add(obj0);
				String devslxsList = (String) list.get(0);
				String [] devslxs = devslxsList.split("#");
				for(int i=0;i<devslxs.length;i++){
					JSONObject obj = new JSONObject();
					String[] devsl = devslxs[i].split("\\|");
					obj.put("desp", devsl[1]);
					obj.put("value", devsl[0]);
					data.add(obj);
				}
			}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return data;
			}
		return data;
	}
	//设备编号
	public static JSONArray devidList(){
		String sql = "select devid from t_pcva_devinfo";
		JSONArray data = new JSONArray();
		JSONObject obj2 = new JSONObject();
		obj2.put("desp", "--请选择--");
		obj2.put("value","");
		data.add(obj2);
			try {
				JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
				if(list!=null){
					for(int i=0;i<list.size();i++){
						Object objArr =  list.get(i);	
						JSONObject obj = new JSONObject();
						obj.put("desp",objArr==null?"":objArr);//设备模块名称
						obj.put("value",objArr==null?"":objArr);//设备模块ID
						data.add(obj);
					}
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return data;
			}	
		return data;
	}
	//设备模块
	public static JSONArray devModelList(){
		String sql = "select MODELID,MODELNAME from T_PCVA_DEVMODELINFO";
		JSONArray data = new JSONArray();
		JSONObject obj2 = new JSONObject();
		obj2.put("desp", "--请选择--");
		obj2.put("value","");
		data.add(obj2);
			try {
				JavaList list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
				if(list!=null){
					for(int i=0;i<list.size();i++){
						Object []objArr =  (Object[]) list.get(i);	
						JSONObject obj = new JSONObject();
						obj.put("desp",objArr[1]==null?"":objArr[1]);//设备模块名称
						obj.put("value",objArr[0]==null?"":objArr[0]);//设备模块ID
						data.add(obj);
					}
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return data;
			}	
		return data;	
	}
	//设备分类
	public static JSONArray devSortList(){
		JavaList list=null;
		JSONArray data = new JSONArray();
		JSONObject obj0 = new JSONObject();
		obj0.put("desp", "--请选择--");
		obj0.put("value", "");
		data.add(obj0);
		String sql = "select paramvalue from tp_cip_sysparameters where modulecode='PCVA' and transcode='DEVTYPECOMBOX' and paramkeyname ='DEVSORT'";
		try {
			list = (JavaList)P_Jdbc.dmlSelect(null, sql, -1).getOutputParams().get(1);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return data;
		}
		if(list!=null){
			String devslxsList = (String) list.get(0);
			String [] devslxs = devslxsList.split("#");
			for(int i=0;i<devslxs.length;i++){
				JSONObject obj = new JSONObject();
				String[] devsl = devslxs[i].split("\\|");
				obj.put("desp", devsl[1]);
				obj.put("value", devsl[0]);
				data.add(obj);
			}
		}
		return data;	
	}
	/**
	 * 加载属性下拉框
	 * @return
	 */
//	public static JSONArray comboTreeInfo(){
//		DefaultUser user = (DefaultUser) ASAPI.authenticator().getCurrentUser();
//		String user_brno = user.getUserVO().getUsr_brno();
//		AppAppLogger.info("用户所在机构号：【" + user_brno + "】");
//		// 查询所在机构对应的下属网点信息
//		Set<String> set = new HashSet<String>();
//		set.add(user_brno);
//		List<String> brnoList = new ArrayList<String>();
//		List<BranchInfo> resultBranchInfo = new ArrayList<BranchInfo>();
//		List<BranchInfo> totalList = new ArrayList<BranchInfo>();
//		// 加载所有网点信息
//		totalList = BankInfoUtil.queryBranchInfoAll();
//		AppLogger.info("加载所有网点信息：【" + totalList.size() + "】");
//		// 加载该机构及及子节点信息
//		BankInfoUtil.queryBranchInfoAllByBrnoList(set, brnoList,
//				resultBranchInfo, totalList);
//		AppLogger.info("加载该机构及及子节点信息完成");
//		// 将自己添加到List内
//		brnoList.add(user_brno);
//		// 将所有用户信息加载为BankInfoBean
//		ArrayList<BankInfoBean> usrList = new ArrayList<BankInfoBean>();
//		for (int i = 0; i < brnoList.size(); i++) {
//			String userBrno = brnoList.get(i);
//			for (int j = 0; j < totalList.size(); j++) {
//				BranchInfo userBrnoInfo = totalList.get(j);
//				// 机构号是否相同
//				if (userBrnoInfo.getSite_code().equals(userBrno)) {
//					BankInfoBean bankInfoBean = new BankInfoBean();
//					bankInfoBean.setBankNo(userBrnoInfo.getSite_code());
//					bankInfoBean.setParentBankNo(userBrnoInfo
//							.getSuper_site_code());
//					bankInfoBean.setBankName(userBrnoInfo.getSite_name());
//					bankInfoBean.setJgLevel(userBrnoInfo.getSite_level());
//					usrList.add(bankInfoBean);
//					break;
//				}
//			}
//		}
//		AppLogger.info("将所有用户信息加载为BankInfoBean完成");
//		JSONArray result = new JSONArray();
//		// 加载根节点信息
//		for (BankInfoBean bank : usrList) {
//			// 根节点,定住总行节点
//			if (StringUtils.isEmpty(bank.getParentBankNo())) {
//				JSONObject item = new JSONObject();
//				item.put("id", bank.getBankNo());
//				item.put("text", bank.getBankNo() + ":" + bank.getBankName());
//				item.put("state", "open");// 展开
//				result.add(item);
//				break;
//			} else {
//				// 根节点，如果非总行节点的时候，定住自身为根节点
//				if (bank.getBankNo().equals(user_brno)) {
//					JSONObject item = new JSONObject();
//					item.put("id", bank.getBankNo());
//					item.put("text",
//							bank.getBankNo() + ":" + bank.getBankName());
//					// 确认是否只为自身，没有叶子节点
//					if (brnoList.size() != 1) {
//						item.put("state", "open");// 展开
//					}
//					result.add(item);
//					break;
//				}
//			}
//		}
//		AppLogger.info("加载根节点信息完成");
//		// 确定最根部叶子节点的jglevel
//		String sql = "select distinct(JGLEVEL) from T_PCVA_BRNOINFO order by JGLEVEL desc";
//		IDbSupport dbSupport = (IDbSupport) WebApplicationContextContainer
//				.getBean(IDbSupport.class);
//		int num = 0;
//		try {
//			List<?> list = dbSupport.queryDataBySql(sql);
//			num = Integer.valueOf((String) list.get(0));
//		} catch (DBSupportException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		AppLogger.info("确定最根部叶子节点的jglevel完成");
//		// 递归增加子节点
//		for (int i = 0; i < result.size(); i++) {
//			JSONObject jsonObj = result.getJSONObject(i);
//			construct(usrList, jsonObj, num);
//		}
//		AppLogger.info("树形下拉框按机构权限返回数据：【" + result + "】");
//		return result;
//	}
	/**
	 * 递归加载所有子节点
	 * 
	 * @param list
	 * @param jsonObj
	 */
//	private static void construct(List<BankInfoBean> list, JSONObject jsonObj,
//			int num) {
//		for (BankInfoBean bank : list) {
//			if (jsonObj.getString("id").equals(bank.getParentBankNo())) {
//				JSONArray children = jsonObj.getJSONArray("children");
//				if (children == null) {
//					children = new JSONArray();
//					jsonObj.put("children", children);
//				}
//				JSONObject item = new JSONObject();
//
//				item.put("id", bank.getBankNo());
//				item.put("text", bank.getBankNo() + ":" + bank.getBankName());
//				if (Integer.valueOf(bank.getJgLevel()) != num) {
//					item.put("state", "closed");// 关闭
//				}
//				children.add(item);
//				construct(list, item, num);
//			}
//		}
//	}
//	//设备模块复选
//	public static JSONArray devModelCheckList(){
//		String sql = "select MODELID,MODELNAME from T_PCVA_DEVMODELINFO";
//		IDbSupport dbSupport = (IDbSupport) WebApplicationContextContainer.getBean(IDbSupport.class);
//		JSONArray data = new JSONArray();
//		try {
//			List<?> list = dbSupport.executeSQLQuery(sql, null);
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					Object[] objArr = (Object[]) list.get(i);
//					JSONObject obj = new JSONObject();
//					obj.put("value", objArr[0] == null ? "" : objArr[0]);
//					obj.put("label", objArr[1] == null ? "" : objArr[1]);
//					obj.put("checked", false);
//					data.add(obj);
//				}
//			}
//		} catch (DBSupportException e) {
//			throw AWebException.raise("设备模块加载失败!");
//		}
//		return data;
//	}
//	//文件名-文件路径
//	public static JSONArray adUploadList(){
//		String sql = "select filename,filepath from T_PCVA_ADUPLOADINFO";
//		IDbSupport dbSupport = (IDbSupport) WebApplicationContextContainer.getBean(IDbSupport.class);
//		JSONArray data = new JSONArray();
//		try {
//			List<?> list = dbSupport.executeSQLQuery(sql, null);
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					Object[] objArr = (Object[]) list.get(i);
//					JSONObject obj = new JSONObject();
//					obj.put("desp", objArr[0] == null ? "" : objArr[0]);
//					obj.put("value", objArr[0] == null ? "" : objArr[0]);
//					obj.put("filepath", objArr[1] == null ? "" : objArr[1]);
//					data.add(obj);
//				}
//			}
//		} catch (DBSupportException e) {
//			throw AWebException.raise("设备模块加载失败!");
//		}
//		return data;
//	}
	/**
	 * 获取机构号与机构名
	 * @return
	 */
//	public static JSONArray brnoList(){
//		String sql = "SELECT BRNO,BRNONAME FROM T_PCVA_BRNOINFO";
//		JSONArray data = new JSONArray();
//		try {
//			List<?> list = dbSupport.executeSQLQuery(sql, null);
//			if (list != null) {
//				for (int i = 0; i < list.size(); i++) {
//					Object[] objArr = (Object[]) list.get(i);
//					JSONObject obj = new JSONObject();
//					obj.put("desp", objArr[1] == null ? "" : objArr[1]);
//					obj.put("value", objArr[0] == null ? "" : objArr[0]);
//					data.add(obj);
//				}
//			}
//		} catch (DBSupportException e) {
//			throw AWebException.raise("数据库操作异常!");
//		}
//		return data;
//	}
}
