package tc.bank.constant;


/**
 * 业务异常类
 * @author AlanMa
 *
 */
public class BusException extends Exception {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 错误编码
	 */
	private String errorCode;
	/**
	 * 错误信息关键字
	 */
	private String errMsgKey;
	
	/**
	 * errorCode
	 * get方法
	 * @return
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * errorCode
	 * set方法
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * errMsgKey
	 * get方法
	 * @return
	 */
	public String getErrMsgKey() {
		return errMsgKey;
	}
	/**
	 * errMsgKey
	 * set方法
	 * @param errMsgKey
	 */
	public void setErrMsgKey(String errMsgKey) {
		this.errMsgKey = errMsgKey;
	}
	/**
	 * 构造方法
	 * @param errorCode
	 * @param errMsgKey
	 */
	public BusException(String errorCode, String errMsgKey) {
		this.errorCode = errorCode;
		this.errMsgKey=errMsgKey;
	}
	/**
	 * 构造 方法
	 * @param errorCode
	 */
	public BusException(String errorCode) {
		this.errorCode = errorCode;
	}
}
