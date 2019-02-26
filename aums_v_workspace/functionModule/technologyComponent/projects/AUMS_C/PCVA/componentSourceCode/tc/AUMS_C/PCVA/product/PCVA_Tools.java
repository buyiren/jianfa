package tc.AUMS_C.PCVA.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import tc.bank.utils.MathUtil;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.svc.javaengine.context.JavaList;


/**
 * 工具类
 *
 * @date 2017-10-19 21:27:56
 */
@ComponentGroup(level = "应用", groupName = "P端工具类", projectName = "ARB", appName = "PCVA")
public class PCVA_Tools {



	/**
	 * @category 获取验证码
	 * @param YZMLength
	 *            入参|验证码长度|int
	 * @param YZMString
	 *            出参|验证码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "YZMLength", comment = "验证码长度", type = int.class)})
	@OutParams(param = { @Param(name = "YZMString", comment = "验证码", type =  java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "生成验证码", style = "判断型", type = "同步组件", comment = "生成验证码", author = "Anonymous", date = "2017-05-03 03:44:33")
	public static TCResult YZMCreater(int YZMLength) {
		/*char[] YZMArray={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I'
				,'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};*/
		char[] YZMArray={'0','1','2','3','4','5','6','7','8','9'};
		String YZMString="";
		Random random=new Random();
		for(int i=0;i<YZMLength;i++)
		{
			YZMString+=YZMArray[random.nextInt(10)];
		}

		return TCResult.newSuccessResult(YZMString);
	}

		/**
	 * @category webservice通讯
	 * @param YZMLength
	 *            入参|验证码长度|int
	 * @param YZMString
	 *            出参|验证码|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "SoapUrl", comment = "通讯url", type = java.lang.String.class),
            @Param(name = "SoapContext", comment = "Soap报文体", type = java.lang.String.class)
           })

	@OutParams(param = { @Param(name = "ReturnSoap", comment = "返回Soap报文", type =  java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "Http请求WebService", style = "判断型", type = "同步组件", comment = "Http请求WebService", author = "Anonymous", date = "2017-05-03 03:44:33")
	public static TCResult PostWebServiceClient(String SoapUrl,String SoapContext) {
		try {
			URL wsUrl;
			wsUrl = new URL(SoapUrl);
            String ReturnSoap="";
			HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			OutputStream os = conn.getOutputStream();
			// 请求体
			String soap = SoapContext;
			os.write(soap.getBytes());
			int responsecode = conn.getResponseCode();
			if (responsecode == 200) {
				InputStream is = conn.getInputStream();
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = is.read(b)) != -1) {
					String ss = new String(b, 0, len, "UTF-8");
					ReturnSoap += ss;
				}
				is.close();

			}
			else
			{
                return TCResult.newFailureResult(ErrorCode.AGR,"通讯异常，返回错误码：" + Integer.toString(responsecode));

			}
			os.close();
			conn.disconnect();
			AppLogger.info("返回结果==============" + ReturnSoap);
			return TCResult.newSuccessResult(ReturnSoap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return TCResult.newFailureResult(ErrorCode.AGR,"请求webservice服务异常");
	}

	/**
	 * @category 数值相加
	 * @param Number1
	 *            入参|数据1|{@link java.lang.String}
	 * @param Number2
	 *            入参|数据2|{@link java.lang.String}
	 * @param rsNumber
	 *            出参|相加后数据|{@link java.lang.String}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "Number1", comment = "数据1", type = java.lang.String.class),
			@Param(name = "Number2", comment = "数据2", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "rsNumber", comment = "相加后数据", type = java.lang.String.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "数值相加", style = "判断型", type = "同步组件", comment = "二个数字相加", author = "Anonymous", date = "2017-09-20 03:37:56")
	public static TCResult A_addBigDecimal(String Number1, String Number2) {

		String rsNumer = null;
		try {
			rsNumer = MathUtil.addBigDecimal(Number1, Number2);
		} catch (Exception e) {
			AppLogger.error(e);
			return TCResult.newFailureResult(ErrorCode.AGR,
					"数值相加出错" + e.getMessage());
		}
		return TCResult.newSuccessResult(rsNumer);

	}
	
	/**
	 * @category 凭证号整理
	 * @param DBVchrNums
	 *            入参|未整理的凭证号集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @param VchrNumsList
	 *            出参|整理后的凭证号集|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaList}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = { @Param(name = "DBVchrNums", comment = "未整理的凭证号集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@OutParams(param = { @Param(name = "VchrNumsList", comment = "整理后的凭证号集", type = cn.com.agree.afa.svc.javaengine.context.JavaList.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "凭证号整理", style = "判断型", type = "同步组件", comment = "把上送的所有凭证号按连续段整理到各个List中", author = "Anonymous", date = "2017-05-03 05:50:41")
	public static TCResult A_SettleVchrNums(JavaList DBVchrNums) {
		AppLogger.trace("调用虚拟组件  'SettleVchrNums'");
		JavaList vchrNumsList = new JavaList();
		JavaList vchrNums = new JavaList();
		vchrNums.add(((JavaDict)DBVchrNums.get(0)).getStringItem("vchrnum"));
		for(int i = 0;i < DBVchrNums.size()-1;i++){
			String vchrnumstr = ((JavaDict)DBVchrNums.get(i)).getStringItem("vchrnum");
			long vchrnumint = Long.parseLong(vchrnumstr);
			String vchrnumstr1 = ((JavaDict)DBVchrNums.get(i+1)).getStringItem("vchrnum");
			long vchrnumint1 = Long.parseLong(vchrnumstr1);
			if(vchrnumint1 - vchrnumint == 1){
				vchrNums.add(((JavaDict)DBVchrNums.get(i+1)).getStringItem("vchrnum"));
			}else{
				vchrNumsList.add(vchrNums);
				vchrNums = new JavaList();
				vchrNums.add(((JavaDict)DBVchrNums.get(i+1)).getStringItem("vchrnum"));
			}
		}
		vchrNumsList.add(vchrNums);
		return TCResult.newSuccessResult(vchrNumsList);
		
	}
	
	public static void main(String[] args) {



	}

}
