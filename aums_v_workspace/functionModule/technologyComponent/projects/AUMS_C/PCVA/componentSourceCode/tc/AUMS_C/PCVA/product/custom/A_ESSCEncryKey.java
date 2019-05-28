package tc.AUMS_C.PCVA.product.custom;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import JavaEsscAPI.UnionAPI;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;

import com.wellcom.dev.JZTDevJni;

/**
 * ESSC密钥服务
 * 
 * @date 2017-09-18 10:25:0
 */
@ComponentGroup(level = "应用", groupName = "密钥服务")
public class A_ESSCEncryKey {

	/**
	 * @category 生成工作密钥
	 * @param EsscSeverPara
	 *            入参|加密机服务器参数|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param fullKeyName
	 *            入参|密钥全名|{@link java.lang.String}
	 * @param keyValue
	 *            出参|密钥密文（ZMK加密）|{@link java.lang.String}
	 * @param checkValue
	 *            出参|密钥校验值（密钥加密16个‘0’的结果）|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "EsscSeverPara", comment = "加密机服务器参数", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "fullKeyName", comment = "密钥全名", type = java.lang.String.class) })
	@OutParams(param = {
			@Param(name = "keyValue", comment = "密钥密文（ZMK加密）", type = java.lang.String.class),
			@Param(name = "checkValue", comment = "密钥校验值（密钥加密16个‘0’的结果）", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "生成工作密钥", style = "判断型", type = "同步组件", comment = "生成指定的密钥，替换密钥库中的密钥值；并输出ZMK加密的密钥密文。要生成的密钥必须已在密钥库中定义。ZMK和要生成的密钥属于同一节点。", author = "Anonymous", date = "2017-09-28 01:52:54")
	public static TCResult A_UnionGenerateKey(JavaDict EsscSeverPara,
			String fullKeyName) {

		String ip = EsscSeverPara.getStringItem("__EsscIp__");
		String port = EsscSeverPara.getStringItem("__EsscPort__");
		String timeOut = EsscSeverPara.getStringItem("__EsscTimeout__");
		String gunionIDOfEsscAPI = EsscSeverPara.getStringItem("__EsscIdof__");

		String[] resarr = new String[] {};
		try {
			UnionAPI unapi = new UnionAPI(ip, Integer.parseInt(port),
					Integer.parseInt(timeOut), gunionIDOfEsscAPI);
			resarr = unapi.UnionGenerateKey(fullKeyName);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return TCResult.newFailureResult(e.toString(), e.getMessage());
		}

		if (resarr.length != 2) {
			return TCResult.newFailureResult("ESSC999", "密钥生成处理错误");
		}
		return TCResult.newSuccessResult(resarr[0], resarr[1]);
	}

	/**
	 * @category 用户密码转加密
	 * @param EsscSeverPara
	 *            入参|加密机服务器参数|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param fullKeyName1
	 *            入参|源ZPK密钥名称|{@link java.lang.String}
	 * @param fullKeyName2
	 *            入参|目的ZPK密钥名称|{@link java.lang.String}
	 * @param pinBlock1
	 *            入参|源ZPK密钥PIN密文|{@link java.lang.String}
	 * @param accNo
	 *            入参|账号/卡号|{@link java.lang.String}
	 * @param pinBlock2
	 *            出参|目的ZPK密钥加密PIN密文|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "EsscSeverPara", comment = "加密机服务器参数", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "fullKeyName1", comment = "源ZPK密钥名称", type = java.lang.String.class),
			@Param(name = "fullKeyName2", comment = "目的ZPK密钥名称", type = java.lang.String.class),
			@Param(name = "pinBlock1", comment = "源ZPK密钥PIN密文", type = java.lang.String.class),
			@Param(name = "accNo", comment = "账号/卡号", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "pinBlock2", comment = "目的ZPK密钥加密PIN密文", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "用户密码转加密", style = "判断型", type = "同步组件", comment = "将一个ZPK加密的PIN密文转换为另一个ZPK加密的PIN密文，转换前后使用相同的帐号，加密算法为ANSI X9.8/X9.18", date = "2017-09-28 02:32:58")
	public static TCResult A_UnionTranslatePin(JavaDict EsscSeverPara,
			String fullKeyName1, String fullKeyName2, String pinBlock1,
			String accNo) {
		String ip = EsscSeverPara.getStringItem("__EsscIp__");
		String port = EsscSeverPara.getStringItem("__EsscPort__");
		String timeOut = EsscSeverPara.getStringItem("__EsscTimeout__");
		String gunionIDOfEsscAPI = EsscSeverPara.getStringItem("__EsscIdof__");

		String resarr;
		try {
			UnionAPI unapi = new UnionAPI(ip, Integer.parseInt(port),
					Integer.parseInt(timeOut), gunionIDOfEsscAPI);
			resarr = unapi.UnionTranslatePin(fullKeyName1, fullKeyName2,
					pinBlock1, accNo);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return TCResult.newFailureResult(e.toString(), e.getMessage());
		}

		return TCResult.newSuccessResult(resarr);
	}
	
	

	/**
	 * @category 指纹比对
	 * @param FingerSeverPara
	 *            入参|指纹服务器参数|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param SysCode
	 *            入参|系统号|{@link java.lang.String}
	 * @param OrgCode
	 *            入参|机构号|{@link java.lang.String}
	 * @param UserCode
	 *            入参|柜员号|{@link java.lang.String}
	 * @param UserZw
	 *            入参|用户指纹|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "FingerSeverPara", comment = "指纹服务器参数", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "SysCode", comment = "系统号", type = java.lang.String.class),
			@Param(name = "OrgCode", comment = "机构号", type = java.lang.String.class),
			@Param(name = "UserCode", comment = "柜员号", type = java.lang.String.class),
			@Param(name = "UserZw", comment = "用户指纹", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "指纹比对", style = "判断型", type = "同步组件", comment = "对指纹信息进行比对，并返回最终的结果", date = "2017-10-05 05:24:18")
	public static TCResult A_FingerCheck(JavaDict FingerSeverPara,
			String SysCode, String OrgCode, String UserCode, String UserZw) {
		
		JZTDevJni JztDev = new JZTDevJni();
		JztDev.setServerIp(FingerSeverPara.getStringItem("__FingerIp__"));
		JztDev.setServerProt(Integer.parseInt(FingerSeverPara.getStringItem("__FingerPort__")));
		// 系统号 机构号 柜员号 指纹信息   0比对成功
		int iRet = JztDev.wlFingerMatch(SysCode, OrgCode, UserCode,
				UserZw);
		if(iRet==0){
		return TCResult.newSuccessResult();
		}else
		{
			return TCResult.newFailureResult(String.valueOf(iRet),JztDev.getMsg());
		}
		
		
	}	

	public static void main(String[] args) {

		// JavaDict jd=new JavaDict();
		// jd.setItem("__EsscIp__", "32.114.64.40");
		// jd.setItem("__EsscPort__", "26102");
		// jd.setItem("__EsscTimeout__", "30");
		// jd.setItem("__EsscIdof__", "TE");
		//
		// TCResult rt=A_ESSCEncryKey.A_UnionGenerateKey(jd,
		// "zzzd.20000001.zpk");
		// TCResult rt2=A_UnionTranslatePin(jd, "zzzd.20000001.zpk",
		// "host.001.zpk", "9998772E224A34ED", "6222233");
		//
		// System.out.println(rt.getOutputParams());

		
		
		JavaDict fd=new JavaDict();
		fd.setItem("__FingerIp__", "32.114.63.52");
		fd.setItem("__FingerPort__", "8000");
		
		TCResult rt3=A_FingerCheck(fd, "1001", "999999", "56211",
				"67=8;::<>80463=43779<8;4::;7;:66?19;7:2?6?77100=5:9>2;==<3=<=10?98?01146069<?;<<9;15;4021<434>=0476?8>9;=;83>4:=?2760?>3<13>7?:?4<77126=<;6183;0433?4?:3704:9=69<3=1>8:22?443=1;;:<=65;<2?3>:46188092?2175:?5;1=9770953832<1=65322>3=3=90<15;>;?;<>:=;01?1<:=136?5:<=0><<;625=4?5:?<1;:>94<;<658<?>70613530;6<3166>257>1??:0:=33:48<6=783860075:0=893<8:94<;<658<?>70613530;6<3166>257>1??:0:=33:48<6=783860075:0=893<8:94<;<658<?>70613530;6<3166>257>1??:0:=33:48<6=783860075:0=893<8:94<;<658<?>70613530;6<3166>257>1??:0:=??");
		System.out.println(rt3.getErrorCode()+":"+rt3.getErrorMsg()+":"+rt3.getStatus());
		
		
		

	}


}
