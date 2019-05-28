package tc.bank.product;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;

import java.io.UnsupportedEncodingException;

import tc.bank.constant.BusException;
import tc.bank.constant.CommonConstant;
import tc.bank.constant.ErrorCodeModule;
import tc.bank.utils.RetResultUtil;
import tc.bank.utils.StringUtil;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;

/**
 * 字符串转换
 * 
 * @author AlanMa
 * @date 2016-01-04 17:6:59
 */
@ComponentGroup(level = "银行", groupName = "报文解析器")
public class B_StringParser {

    /**
     * @param origByteArray
     *            入参|字节数组|{@link byte}
     * @param encoding
     *            入参|字符编码|{@link java.lang.String}
     * @param str
     *            出参|字符串|{@link java.lang.String}
     * @return 0 失败<br/>
     *         1 成功<br/>
     */
    @InParams(param = { @Param(name = "origByteArray", comment = "字节数组", type = byte[].class), @Param(name = "encoding", comment = "字符编码", type = java.lang.String.class) })
    @OutParams(param = { @Param(name = "str", comment = "字符串", type = java.lang.String[].class) })
    @Returns(returns = { @Return(id = "0", desp = "失败"), @Return(id = "1", desp = "成功") })
    @Component(label = "字节数组转换为字符串", style = "判断型", type = "同步组件", comment = "字节数组转换为字符串", date = "2016-01-04 05:36:08")
    public static TCResult B_parseToString(byte[] origByteArray, String encoding) {
        TCResult tcResult = null;
        String encode = StringUtil.isEmpty(encoding) ? CommonConstant.DEFAULT_ENCODE : encoding;
        try {
            AppLogger.info("[origByteArray]:" + new String(origByteArray, encode));

            tcResult = TCResult.newSuccessResult();
            tcResult.setOutputParams(new String(origByteArray, encode));
        }
        catch (UnsupportedEncodingException e) {
            AppLogger.error(e);
            return RetResultUtil.getTCResToExternal(new BusException(ErrorCodeModule.IME002, encode));
        }
        RetResultUtil.printTCResult(tcResult);
        return tcResult;
    }

    /**
     * @param origString
     *            入参|字符串|{@link java.lang.String}
     * @param encoding
     *            入参|字符编码|{@link java.lang.String}
     * @param byteArray
     *            出参|字节数组|{@link byte}
     * @return 0 失败<br/>
     *         1 成功<br/>
     *         2 异常<br/>
     */
    @InParams(param = { @Param(name = "origString", comment = "字符串", type = java.lang.String.class), @Param(name = "encoding", comment = "字符编码", type = java.lang.String.class) })
    @OutParams(param = { @Param(name = "byteArray", comment = "字节数组", type = byte.class) })
    @Returns(returns = { @Return(id = "0", desp = "失败"), @Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
    @Component(label = "字符串转换成字节数组", style = "选择型", type = "同步组件", comment = "字符串转换成字节数组", date = "2016-01-05 03:38:56")
    public static TCResult B_parseToByteArray(String origString, String encoding) {
        TCResult tcResult = null;
        String encode = StringUtil.isEmpty(encoding) ? CommonConstant.DEFAULT_ENCODE : encoding;
        try {
            if (StringUtil.isNotEmpty(origString)) {
                tcResult = TCResult.newSuccessResult();
                tcResult.setOutputParams(origString.getBytes(encode));
            }
            else {
                return RetResultUtil.getTCResToExternal(TCResult.newFailureResult(ErrorCodeModule.IMD001, "origString"));
            }
        }
        catch (UnsupportedEncodingException e) {
            AppLogger.error(e);
            return RetResultUtil.getTCResToExternal(new BusException(ErrorCodeModule.IME002, encoding));
        }
        RetResultUtil.printTCResult(tcResult);
        return tcResult;
    }
	/**
	 * @category 字符串转换成字节数组plus
	 * @param origString
	 *            入参|字符串|{@link java.lang.String}
	 * @param encoding
	 *            入参|字符编码|{@link java.lang.String}
	 * @param isCount
	 *            入参|是否自动添加长度头|boolean
	 * @param headLen
	 *            入参|头长度|int
	 * @param byteArray
	 *            出参|字节数组|{@link byte}
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 *         2 异常<br/>
	 */
	@InParams(param = {
			@Param(name = "origString", comment = "字符串", type = java.lang.String.class),
			@Param(name = "encoding", comment = "字符编码", type = java.lang.String.class),
			@Param(name = "isCount", comment = "是否自动添加长度头", type = boolean.class),
			@Param(name = "headLen", comment = "头长度", type = int.class) })
	@OutParams(param = { @Param(name = "byteArray", comment = "字节数组", type = byte.class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功"), @Return(id = "2", desp = "异常") })
	@Component(label = "字符串转换成字节数组plus", style = "选择型", type = "同步组件", comment = "字符串转换成字节数组plus", date = "2016-04-27 04:07:38")
	public static TCResult B_parseToByteArrayPlus(String origString,
			String encoding, boolean isCount, int headLen) {
		TCResult tcResult = null;
		String encode = StringUtil.isEmpty(encoding) ? CommonConstant.DEFAULT_ENCODE : encoding;
		try {
			if (StringUtil.isNotEmpty(origString)) {
				tcResult = TCResult.newSuccessResult();
				byte[] strBytes=origString.getBytes(encode);
				if (isCount){
					StringBuffer buffer = new StringBuffer();
					int len = String.valueOf(strBytes.length).length();
					if (len < headLen) {
					      for (int i = 0; i < headLen - len; i++) {
					        buffer.append("0");
					      }
					    }
					buffer.append(strBytes.length);
					AppLogger.debug("headString:"+buffer.toString());
					byte[] headBytes=buffer.toString().getBytes(encode);
					byte[] newBytes = new byte[headBytes.length+strBytes.length];
					System.arraycopy(headBytes, 0, newBytes, 0, headBytes.length);
					System.arraycopy(strBytes, 0, newBytes, headBytes.length, strBytes.length);
					tcResult.setOutputParams(newBytes);	
				}
				else{
					tcResult.setOutputParams(strBytes);					
				}
			} else {
				return RetResultUtil
						.getTCResToExternal(TCResult.newFailureResult(
								ErrorCodeModule.IMD001, "origString"));
			}
		} catch (UnsupportedEncodingException e) {
			AppLogger.error(e);
			return RetResultUtil.getTCResToExternal(new BusException(
					ErrorCodeModule.IME002, encoding));
		}
		RetResultUtil.printTCResult(tcResult);
		return tcResult;
	}
}
