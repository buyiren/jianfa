package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import tc.bank.constant.BusException;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.constant.IErrorCode;
import tc.bank.utils.StringUtil;
import tc.platform.P_Jdbc;
import tc.platform.P_Json;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author ha9cu1
 * 
 * @date 2015-12-11 19:8:22
 */
@ComponentGroup(level = "银行", groupName = "接口处理类")
public class B_AppInterfaceMng {
	/**
	 * @param __SRC__
	 *            入参|请求容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param msgtype
	 *            入参|报文的类型(CNAPS_Pack-现代化支付报文, PXML-内部支付XML)|
	 *            {@link java.lang.String}
	 * @param msgstructs
	 *            入参|报文结构列表|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param direction
	 *            入参|映射方向(1t 渠道请求平台, 2 平台请求渠道)| {@link java.lang.String}
	 * @param __TAR__
	 *            入参|目标容器|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param ischk
	 *            入参|是否进行接口校验|boolean
	 * @param ismap
	 *            入参|是否进行接口映射|boolean
	 * @param istrans
	 *            入参|是否进行接口转换|boolean
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "__SRC__", comment = "请求容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "msgtype", comment = "报文的类型(comment 通用报文，natp 报文, PXML-内部支付XML)", type = java.lang.String.class),
			@Param(name = "msgstructs", comment = "报文结构列表", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class),
			@Param(name = "direction", comment = "映射方向(1 渠道请求平台,2 平台请求渠道)", type = java.lang.String.class),
			@Param(name = "__TAR__", comment = "目标容器", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "ischk", comment = "是否进行接口校验", type = boolean.class),
			@Param(name = "ismap", comment = "是否进行接口映射", type = boolean.class),
			@Param(name = "istrans", comment = "是否进行接口转换", type = boolean.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "处理接口请求信息", style = "判断型", type = "同步组件", comment = "根据数据库配置映射容器中的变量为平台可用变量", author = "Anonymous", date = "2015-12-11 07:13:02")
	public static TCResult B_ProcInMsg(JavaDict __SRC__, String msgtype,
			JavaList msgstructs, String direction, JavaDict __TAR__,
			boolean ischk, boolean ismap, boolean istrans) {
			AppLogger.info("版本2.0");
		JavaList interfaceKeys = new JavaList("colname", "coldefault",
				"coltype", "coldesc", "colmust", "colmapname", "colflag",
				"collength");
		int colnameno = interfaceKeys.indexOf("colname");
		int colmapnameno = interfaceKeys.indexOf("colmapname");
		int colflagno = interfaceKeys.indexOf("colflag");
		int coldescno = interfaceKeys.indexOf("coldesc");
		int collength = interfaceKeys.indexOf("collength");
		int colmustno = interfaceKeys.indexOf("colmust");
		int coldefaultno = interfaceKeys.indexOf("coldefault");
		int coltypeno = interfaceKeys.indexOf("coltype");

		JavaList result = new JavaList();

		String sqlstr = "select ";

		for (Object key : interfaceKeys) {
			key = key.toString();
			sqlstr = sqlstr + key + "  ,";
		}

		sqlstr = sqlstr.substring(0, sqlstr.length() - 1);
		sqlstr = sqlstr
				+ "from AUMS_PUB_INTERFACECOLVIEW where MSGTYPEID = ?  order by msgtypeid , colnum";

		// JavaList values = new JavaList();
		for (Object msgtypeid : msgstructs) {
			if (msgtypeid == null) {
				continue;
			}
			JavaList values = new JavaList();
			values.add(msgtypeid);

			//AppLogger.info(sqlstr + " --- " + values);

			TCResult res = P_Jdbc.preparedSelect(null, sqlstr, values, 0);
			if (2 == res.getStatus()) {
				//AppLogger.info("无此接口配置: " + values + " 跳过...");
				continue;
			} else if (1 != res.getStatus()) {
				return TCResult.newFailureResult(
						IErrorCode.getCode("RecorDoesNotMeet"),
						(String) msgtypeid);
			}
			result = (JavaList) res.getOutputParams().get(1);

			/* 创建目标容器和属性容器 */

			JavaDict tempdict = new JavaDict();
			JavaList exceptList = new JavaList(); // 记录NATP报文的结构字段
			try {
				for (int i = 0; i < result.size(); i++) {

					JavaList resl = result.getItem(i);

					String colname = resl.getItem(colnameno);
					String colflag = resl.getItem(colflagno);
					String colmapname = resl.getItem(colmapnameno);
					String coldesc = resl.getItem(coldescno);
					String coltype = resl.getItem(coltypeno);
					String colmust = resl.getItem(colmustno);
					String coldefault = resl.getItem(coldefaultno);
					int colleng = Integer.valueOf(resl.getItem(collength)
							.toString());

					if ("/".equals(colmapname) || "/".equals(colname)) {
						continue;
					}

					String[] mapnamearr = colmapname.substring(1,
							colmapname.length()).split("/");
					String[] colnamearr = colname
							.substring(1, colname.length()).split("/");
					boolean continueflag=false; 

					if ("1".equals(direction)) { // 渠道向平台映射
						if ("NATP".equals(msgtype)) {
							String srckey = colnamearr[colnamearr.length - 1];
							String tarkey ="";
							if(ismap){
								tarkey = mapnamearr[mapnamearr.length - 1];
							}else{
								tarkey = colnamearr[colnamearr.length - 1];
							}
							if ("O".equals(colflag)) {
								// json转JavaDict对象
								if( __SRC__.get(srckey)!=null){
									JSONObject jsonObj = (JSONObject) JSON.parseObject(__SRC__.get(srckey).toString());
									JavaDict jsonDict = getJavaDict(jsonObj);
									tempdict.put(tarkey, jsonDict);
								}else{
									tempdict.put(tarkey, new JavaDict());
								}
								
								exceptList.add(colname);
//								AppLogger.info("需要跳过的结构列表:"+exceptList);
							} else if ("OA".equals(colflag)) {
								// json转JavaList对象
								if( __SRC__.get(srckey)!=null){
									JSONArray jsonarray = (JSONArray) JSON.parseArray(__SRC__.get(srckey).toString());
									JavaList jsonList = getJavaList(jsonarray);
									tempdict.put(tarkey, jsonList);
								}else{
									tempdict.put(tarkey, new JavaList());
								}

								exceptList.add(colname);
//								AppLogger.info("需要跳过的结构列表:"+exceptList);
							} else { // 非结构
								for (Object exceptitem : exceptList) {
									if (colname.indexOf(String
											.valueOf(exceptitem)) != -1) {
										// 如果已处理结构，则结构下的字段跳过
										AppLogger.info("已处理父结构,跳过---"+colname);
										continueflag=true;
										break;
									}
								}
								if(continueflag==true){
									continueflag=false;
									continue;
								}
								Object srcvalue = __SRC__.get(srckey);
								Object value = dealwith(srckey, srcvalue, resl,
										ischk, istrans);
								if (value != null) {
									tempdict.put(tarkey, value);
								}
							}
							continue;
						}
						if (ismap) {
							putTarItemFromSrc(__SRC__, colnamearr, tempdict,
									mapnamearr, resl, ischk, istrans);
						} else {
							putTarItemFromSrc(__SRC__, colnamearr, tempdict,
									colnamearr, resl, ischk, istrans);
						}
					} else { // 平台向渠道映射
						if ("NATP".equals(msgtype)) {
							String srckey = mapnamearr[mapnamearr.length - 1];
							String tarkey ="";
							if(ismap){
								tarkey = colnamearr[colnamearr.length - 1];
							}else{
								tarkey = mapnamearr[mapnamearr.length - 1];
							}
							if ("O".equals(colflag)) {
								// JavaDict对象转json
								if( __SRC__.get(srckey)!=null){
									tempdict.put(tarkey, __SRC__.get(tarkey).toString());
								}else{
									tempdict.put(tarkey, "{}");
								}

								exceptList.add(colmapname);
								AppLogger.info("需要跳过的结构列表:"+exceptList);
							} else if ("OA".equals(colflag)) {
								// JavaList对象转json
								if( __SRC__.get(srckey)!=null){
									tempdict.put(srckey, __SRC__.get(srckey).toString());
								}else{
									tempdict.put(tarkey, "[]");
								}

								exceptList.add(colmapname);
								AppLogger.info("需要跳过的结构列表:"+exceptList);
							} else { // 非结构
								for (Object exceptitem : exceptList) {
									if (colmapname.indexOf(String
											.valueOf(exceptitem)) != -1) {
										// 如果已处理结构，则结构下的字段跳过
										AppLogger.info("已处理父结构,跳过---"+colmapname);
										continueflag=true;
										break;
									}
								}
								if(continueflag==true){
									continueflag=false;
									continue;
								}
								Object srcvalue = __SRC__.get(srckey);
								Object value = dealwith(srckey, srcvalue, resl,
										ischk, istrans);
								if (value != null) {
									tempdict.put(tarkey, value.toString());
								}
							}
							continue;
						}
						if (ismap) {
							putTarItemFromSrc(__SRC__, mapnamearr, tempdict,
									colnamearr, resl, ischk, istrans);
						} else {
							putTarItemFromSrc(__SRC__, mapnamearr, tempdict,
									mapnamearr, resl, ischk, istrans);
						}
					}

				}
			} catch (BusException e) {

				__SRC__.setItem("_CheckKey_", e.getErrMsgKey());
				__TAR__.setItem("_CheckKey_", e.getErrMsgKey());

				if (e.getErrorCode().equals("IMC001")) {
					return TCResult.newFailureResult(
							IErrorCode.getCode("FieldMustBeEntered"),
							e.getErrMsgKey() + "字段必输");
				}
				if (e.getErrorCode().equals("IMC002")) {
					return TCResult.newFailureResult(
							IErrorCode.getCode("FieldFormatConversError"),
							e.getErrMsgKey());
				}
				if (e.getErrorCode().equals("IMC003")) {
					return TCResult.newFailureResult(
							IErrorCode.getCode("FieldExceedsMaximum"),
							e.getErrMsgKey());
				}

			}

			AppLogger.info(tempdict+"");
			//业务报文头和报文体的机构都为Body/request/导致putAll时覆盖，故增加以下判断处理
			if (__TAR__.hasKey("Body") && tempdict.hasKey("Body")) {
				if (((JavaDict) __TAR__.getItem("Body")).hasKey("request") && 
						((JavaDict) tempdict.getItem("Body")).hasKey("request")) {
					((JavaDict) ((JavaDict) __TAR__.getItem("Body")).getItem("request")).putAll(((JavaDict) ((JavaDict) tempdict.getItem("Body")).getItem("request")));
				}
			} else if(__TAR__.hasKey("commandParameters") && tempdict.hasKey("commandParameters")){
				((JavaDict) __TAR__.getItem("commandParameters")).putAll((JavaDict) tempdict.getItem("commandParameters"));
			}else if(__TAR__.hasKey("XM") && tempdict.hasKey("XM")){
				((JavaDict) __TAR__.getItem("XM")).putAll((JavaDict) tempdict.getItem("XM"));
			}else{
				__TAR__.putAll(tempdict);
			}
//			__TAR__.putAll(tempdict);
		}

		return TCResult.newSuccessResult();
	}

	private static void putTarItemFromSrc(JavaDict srcparentdict,
			String[] srcarr, JavaDict tarparentdict, String[] tararr,
			JavaList attrlist, boolean ischk, boolean istrans)
			throws BusException {
		// AppLogger.info("attrlist---"+attrlist);
		String colname = attrlist.getItem(0);
		String coldefault = attrlist.getItem(1);
		String coltype = attrlist.getItem(2);
		String coldesc = attrlist.getItem(3);
		String colmust = attrlist.getItem(4);
		String colmapname = attrlist.getItem(5);
		String colflag = attrlist.getItem(6);
		int colleng = Integer.valueOf(attrlist.getItem(7).toString());
		
		// 先对齐结构
		if (srcarr.length < tararr.length) { // 目标结构比源结构长，先建立目标多余层级，应该都是JavaDict
			int loopcount=(tararr.length - srcarr.length);
			for (int i = 0; i < loopcount; i++) {
//				AppLogger.info("目标结构长对齐--当前目标结构:"+srcparentdict.toString()+",当前字段:"+srcarr[0]);
				if (!tarparentdict.hasKey(tararr[0])) {
					tarparentdict.put(tararr[0], new JavaDict());
				}
				tarparentdict = (JavaDict) tarparentdict.get(tararr[0]);
				String[] tmparr = new String[tararr.length - 1];
				System.arraycopy(tararr, 1, tmparr, 0, tmparr.length);
				tararr = tmparr;
			}
		} else if (srcarr.length > tararr.length) { // 源结构比目标结构长，先去掉多余层级，应该都是JavaDict
			int loopcount=(srcarr.length - tararr.length);
			for (int i = 0; i < loopcount; i++) {
//				AppLogger.info("源结构长对齐--当前源结构:"+srcparentdict.toString()+",当前字段:"+srcarr[0]);
				if (!srcparentdict.hasKey(srcarr[0])) {
//					AppLogger.info("对齐时源字典不存在对应字段，建立该字典-----"+srcarr[0]);
					srcparentdict.put(srcarr[0], new JavaDict());
				}
				Object temp_obj = srcparentdict.get(srcarr[0]);
				if (temp_obj instanceof JavaDict) {
					srcparentdict = (JavaDict) srcparentdict.get(srcarr[0]);
					String[] tmparr = new String[srcarr.length - 1];
					System.arraycopy(srcarr, 1, tmparr, 0, tmparr.length);
					srcarr = tmparr;
				}
			}
		}
		if (srcarr.length > 1) { // 未到叶子节点
			// 处理下一层级
			Object nowsrcobject = srcparentdict.get(srcarr[0]);
			if (nowsrcobject == null) {
//				AppLogger.info("父节点为空跳过---当前结构:"+srcparentdict.toString()+",当前字段:"+srcarr[0]);
				return;
			}
			String[] nowsrcarr = new String[srcarr.length - 1];
			System.arraycopy(srcarr, 1, nowsrcarr, 0, nowsrcarr.length);

			if (!tarparentdict.hasKey(tararr[0])) { // 目标字典中不存在对应字段，则建立结构
//				AppLogger.info("目标字典中不存在对应字段，则建立结构:"+tararr[0]);
				if (nowsrcobject instanceof JavaDict) { // 源结构为字典
//					AppLogger.info("源结构为字典，建立字典结构:"+nowsrcobject);
					if (srcarr.length == 2 && "A".equals(colflag)) { // 叶子节点的父节应该是循环结构，但却为字典结构
						tarparentdict.put(tararr[0], new JavaList());
						((JavaList) tarparentdict.get(tararr[0]))
								.add(new JavaDict());
						JavaList tmpList=new JavaList();
						tmpList.add(nowsrcobject);
						nowsrcobject = tmpList;
					}
					else{	//父节点为字典
						tarparentdict.put(tararr[0], new JavaDict());
					}
				}else if (nowsrcobject instanceof JavaList) { // 源结构为List
//					AppLogger.info("源结构为List，建立List结构");
					tarparentdict.put(tararr[0], new JavaList());
					for (Object listitem : (JavaList) nowsrcobject) {
						((JavaList) tarparentdict.get(tararr[0]))
								.add(new JavaDict());
					}
//					AppLogger.info("建立后的List结构:"+tarparentdict);
				}
			}

			Object nowtarobject = tarparentdict.get(tararr[0]);
			//
			String[] nowtararr = new String[tararr.length - 1];
			System.arraycopy(tararr, 1, nowtararr, 0, nowtararr.length);

			if (nowsrcobject instanceof JavaDict) {
				if (nowtarobject instanceof JavaList) {
					JavaList templist = (JavaList) nowtarobject;
					nowtarobject = templist.get(0);
				}
//				AppLogger.info("递归调用下层字典结构:"+nowsrcarr[0]);
				putTarItemFromSrc((JavaDict) nowsrcobject, nowsrcarr,
						(JavaDict) nowtarobject, nowtararr, attrlist, ischk,
						istrans);
			} else if (nowsrcobject instanceof JavaList) {
//				AppLogger.info("调用循环结构:"+nowsrcobject+"---处理字段:"+nowsrcarr[0]);
				putTarItemFromSrc((JavaList) nowsrcobject, nowsrcarr,
						(JavaList) nowtarobject, nowtararr, attrlist, ischk,
						istrans);
			}
//			AppLogger.info("递归调用结束，返回上层...");
			return;
		} else if (srcarr.length == 1) {
			Object value = srcparentdict.get(srcarr[0]);
//			AppLogger.info("源结构:" + srcparentdict.toString() + "---源字段:" + srcarr[0] +"---目标结构:"+tarparentdict.toString()+"---目标字段:"+tararr[0]+"---值:"+value);
			if (("O".equals(colflag) || "OA".equals(colflag)) && value != null) { // 如果是O或者是OA时，若有值则等待下层处理
//				AppLogger.info("结构有值，等待下层处理"+"结构:"+srcparentdict.toString()+"  值为:"+value.toString());
				return;
			}
			Object retvalue = dealwith(srcarr[0], value, attrlist, ischk,
					istrans);
			if (retvalue != null) {
				tarparentdict.put(tararr[0], retvalue);
			}
//			AppLogger.info("字段条目处理完毕 ---" + attrlist.toString() + "将处理下一条目");
			return;
		}
	}
	
	
	private static void putTarItemFromSrc(JavaList srcparentlist,
			String[] srcarr, JavaList tarparentlist, String[] tararr,
			JavaList attrlist, boolean ischk, boolean istrans)
			throws BusException {
		for (int i = 0; i < srcparentlist.size(); i++) { // 取List的每一个元素，应该是JavaDict
			JavaDict loopsrcDict = srcparentlist.getItem(i);
			JavaDict looptarcDict = tarparentlist.getItem(i);
			if(looptarcDict==null){
				
			}
			putTarItemFromSrc(loopsrcDict, srcarr, looptarcDict, tararr,
					attrlist, ischk, istrans);
		}
		return;

	}

	private static Object dealwith(String key, Object value, JavaList attrlist,
			boolean ischk, boolean istrans) throws BusException {
		//AppLogger.info("当前处理配置条目:"+attrlist.toString());
		//AppLogger.info("处理前的叶子节点---字段为:" + key + "---值为:" + value);

		String colname = attrlist.getItem(0);
		String coldefault = attrlist.getItem(1);
		String coltype = attrlist.getItem(2);
		String coldesc = attrlist.getItem(3);
		String colmust = attrlist.getItem(4);
		String colmapname = attrlist.getItem(5);
		String colflag = attrlist.getItem(6);
		int colleng = Integer.valueOf(attrlist.getItem(7).toString());

		String checkkey = key + "[ " + coldesc + " ]";
		Object ret = value;
		if (value == null || "".equals(String.valueOf(value))) { // 字段不存在或为空
			if ("O".equals(colflag)) { // 字典结构
				ret = new JavaDict();
			} else if ("OA".equals(colflag)) {
				ret = new JavaList();
			} else if (coldefault != null && !"".equals(coldefault)) { // 默认值处理
				if(coldefault.equals("\"\"")){
					coldefault="";
				}
				ret = coldefault;
			} else {
				if (ischk && ("M".equals(colmust) || "Y".equals(colmust))) { // 必输校验
					throw new BusException("IMC001", checkkey);
				}
			}
		} else { // 字段有值
			if (!"TextString".equals(coltype) && value instanceof String) {
				if (String.valueOf(value).length() > colleng && colleng != 9999) { // 长度校验
					throw new BusException("IMC003", checkkey);
				}
				if (!StringUtil.formatChk(String.valueOf(value), coltype)) {
					// 如果 type 不正确 ,进行转换
					TCResult tc = StringUtil.formatConvert(
							String.valueOf(value), coltype);
					if (tc.getStatus() == 0) {
						throw new BusException("IMC002", coltype);
					}
					if (tc.getStatus() == 1) {
						ret = (String) tc.getOutputParams().get(0);
					}
				}
			} else {
				ret = value;
			}

		}
		//AppLogger.info("处理后的叶子节点---字段为:" + key + "---值为:" + ret);

		return ret;
	}
	
	private static JavaDict getJavaDict(JSONObject jsonObj) {
		JavaDict dict = new JavaDict();

		for (@SuppressWarnings("rawtypes")
		Map.Entry entry : jsonObj.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				dict.setItem(key, null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				dict.setItem(key, getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				dict.setItem(key, getJavaDict((JSONObject) value));
			} else {
				dict.setItem(key, value);
			}
		}
		return dict;
	}
	
	private static JavaList getJavaList(JSONArray seq) {
		JavaList list = new JavaList();
		for (int i = 0; i < seq.size(); i++) {
			Object value = seq.get(i);
			if (value == null) {
				list.add(null);
				continue;
			}
			if (value.getClass() == JSONArray.class) {
				list.add(getJavaList((JSONArray) value));
			} else if (value.getClass() == JSONObject.class) {
				list.add(getJavaDict((JSONObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

}
