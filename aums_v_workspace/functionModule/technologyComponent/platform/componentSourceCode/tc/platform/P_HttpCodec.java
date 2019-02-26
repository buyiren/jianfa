package tc.platform;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import galaxy.ide.tech.cpt.Component;
import galaxy.ide.tech.cpt.ComponentGroup;
import galaxy.ide.tech.cpt.InParams;
import galaxy.ide.tech.cpt.OutParams;
import galaxy.ide.tech.cpt.Param;
import galaxy.ide.tech.cpt.Return;
import galaxy.ide.tech.cpt.Returns;
import cn.com.agree.afa.jcomponent.ErrorCode;
import cn.com.agree.afa.svc.javaengine.AppLogger;
import cn.com.agree.afa.svc.javaengine.TCResult;
import cn.com.agree.afa.svc.javaengine.context.JavaDict;
import cn.com.agree.afa.util.HttpStatus;

/**
 * HTTP协议拼包组件
 * 
 * @date 2015-07-27 11:5:0
 */
@ComponentGroup(level = "平台", groupName = "渠道通讯类组件")
public class P_HttpCodec {
	
	/**
	 * specify Charset
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
     * Horizontal space
     */
    private static final byte SP = 32;

    /**
     * Carriage return
     */
    private static final byte CR = 13;

    /**
     * Line feed character
     */
    private static final byte LF = 10;

    /**
     * Colon ':'
     */
    private static final byte COLON = 58;

    private static String PCK_LENGTH = "Content-Length";

    private static final byte[] CRLF = { CR, LF };
    private static final byte[] HEADER_SEPARATOR = { COLON, SP };

    /** Reason phrases lookup table. */
    private static final String[][] REASON_PHRASES = new String[][] { null, new String[3], // 1xx
            new String[8], // 2xx
            new String[8], // 3xx
            new String[25], // 4xx
            new String[8] // 5xx
    };

    private static void setReason(final int status, final String reason) {
        final int category = status / 100;
        final int subcode = status - 100 * category;
        REASON_PHRASES[category][subcode] = reason;
    }
    
    /**
     * Obtains the reason phrase for a status code.
     * 
     * @param status the status code, in the range 100-599
     * 
     * 
     * @return the reason phrase, or <code>null</code>
     */
    private static String getReason(final int status) {
        if (status < 100 || status > 600) {
            throw new IllegalArgumentException("Unknown category for status code: " + status);
        }
        final int category = status / 100;
        final int subcode = status - 100 * category;

        String reason = "";
        if (REASON_PHRASES[category].length > subcode) {
            reason = REASON_PHRASES[category][subcode];
        }
        return reason;
    }

	/**
	 * @param status
	 *            入参|响应状态代码 ，如'200','502'|{@link java.lang.String}
	 * @param headers
	 *            入参|头部信息字典|
	 *            {@link cn.com.agree.afa.svc.javaengine.context.JavaDict}
	 * @param content
	 *            入参|报文内容|{@link java.lang.String}
	 * @param encoding
	 *            入参|编码,默认使用UTF-8|{@link java.lang.String}
	 * @param response
	 *            出参|HTTP响应报文|byte
	 * @return 0 失败<br/>
	 *         1 成功<br/>
	 */
	@InParams(param = {
			@Param(name = "status", comment = "响应状态代码 ，如\"200\",\"502\"", type = java.lang.String.class),
			@Param(name = "headers", comment = "头部信息字典", type = cn.com.agree.afa.svc.javaengine.context.JavaDict.class),
			@Param(name = "content", comment = "报文内容", type = java.lang.String.class),
			@Param(name = "encoding", comment = "编码,默认使用UTF-8", type = java.lang.String.class) })
	@OutParams(param = { @Param(name = "response", comment = "HTTP响应报文", type = byte[].class) })
	@Returns(returns = { @Return(id = "0", desp = "失败"),
			@Return(id = "1", desp = "成功") })
	@Component(label = "HTTP响应报文拼包", style = "判断型", type = "同步组件", comment = "字典headers可以为空，如果为空，则自动添加内容长度信息", date = "Mon Jul 27 11:21:13 CST 2015")
	public static TCResult packResponse(String status, JavaDict headers,
			String content, String encoding) {
		if(encoding == null) {
			encoding = DEFAULT_CHARSET;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(baos);
		try {
			dataOutputStream.writeBytes("HTTP/1.1");
			dataOutputStream.writeByte(SP);
			dataOutputStream.writeBytes(status);
			dataOutputStream.writeByte(SP);
			dataOutputStream.writeBytes(getReason(Integer.parseInt(status)));
			dataOutputStream.write(CRLF);
			if(headers != null) {
				Iterator<Entry<Object, Object>> headerIterator = headers.entrySet().iterator();
				while(headerIterator.hasNext()) {
					Map.Entry<Object, Object> entry = headerIterator.next();
					dataOutputStream.write(((String) entry.getKey()).getBytes(encoding));
					dataOutputStream.write(HEADER_SEPARATOR);
					dataOutputStream.write(((String) entry.getValue()).getBytes(encoding));
					dataOutputStream.write(CRLF);
				}
			}
			
			byte[] contentBytes = content.getBytes(encoding);
			
			if(headers == null || !headers.hasKey(new String(PCK_LENGTH))) {
				dataOutputStream.writeBytes(PCK_LENGTH);
				dataOutputStream.write(HEADER_SEPARATOR);
				dataOutputStream.write(String.valueOf(contentBytes.length).getBytes());
				dataOutputStream.write(CRLF);
			}
			
			dataOutputStream.write(CRLF);
			dataOutputStream.write(contentBytes);
			AppLogger.info(baos.toString());
			return TCResult.newSuccessResult(baos.toByteArray());
		} catch (IOException e) {
			return TCResult.newFailureResult(ErrorCode.HANDLING, e);
		} finally {
			try {
				if(dataOutputStream != null) {
					dataOutputStream.close();
				}
			} catch (IOException e) {
				return TCResult.newFailureResult(ErrorCode.HANDLING, e);
			}
		}
	}
	
	// ----------------------------------------------------- Static Initializer

    /** Set up status code to "reason phrase" map. */
    static {
        // HTTP 1.0 Server status codes -- see RFC 1945
        setReason(HttpStatus.SC_OK, "OK");
        setReason(HttpStatus.SC_CREATED, "Created");
        setReason(HttpStatus.SC_ACCEPTED, "Accepted");
        setReason(HttpStatus.SC_NO_CONTENT, "No Content");
        setReason(HttpStatus.SC_MOVED_PERMANENTLY, "Moved Permanently");
        setReason(HttpStatus.SC_MOVED_TEMPORARILY, "Moved Temporarily");
        setReason(HttpStatus.SC_NOT_MODIFIED, "Not Modified");
        setReason(HttpStatus.SC_BAD_REQUEST, "Bad Request");
        setReason(HttpStatus.SC_UNAUTHORIZED, "Unauthorized");
        setReason(HttpStatus.SC_FORBIDDEN, "Forbidden");
        setReason(HttpStatus.SC_NOT_FOUND, "Not Found");
        setReason(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        setReason(HttpStatus.SC_NOT_IMPLEMENTED, "Not Implemented");
        setReason(HttpStatus.SC_BAD_GATEWAY, "Bad Gateway");
        setReason(HttpStatus.SC_SERVICE_UNAVAILABLE, "Service Unavailable");

        // HTTP 1.1 Server status codes -- see RFC 2048
        setReason(HttpStatus.SC_CONTINUE, "Continue");
        setReason(HttpStatus.SC_TEMPORARY_REDIRECT, "Temporary Redirect");
        setReason(HttpStatus.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        setReason(HttpStatus.SC_CONFLICT, "Conflict");
        setReason(HttpStatus.SC_PRECONDITION_FAILED, "Precondition Failed");
        setReason(HttpStatus.SC_REQUEST_TOO_LONG, "Request Too Long");
        setReason(HttpStatus.SC_REQUEST_URI_TOO_LONG, "Request-URI Too Long");
        setReason(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        setReason(HttpStatus.SC_MULTIPLE_CHOICES, "Multiple Choices");
        setReason(HttpStatus.SC_SEE_OTHER, "See Other");
        setReason(HttpStatus.SC_USE_PROXY, "Use Proxy");
        setReason(HttpStatus.SC_PAYMENT_REQUIRED, "Payment Required");
        setReason(HttpStatus.SC_NOT_ACCEPTABLE, "Not Acceptable");
        setReason(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
        setReason(HttpStatus.SC_REQUEST_TIMEOUT, "Request Timeout");

        setReason(HttpStatus.SC_SWITCHING_PROTOCOLS, "Switching Protocols");
        setReason(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, "Non Authoritative Information");
        setReason(HttpStatus.SC_RESET_CONTENT, "Reset Content");
        setReason(HttpStatus.SC_PARTIAL_CONTENT, "Partial Content");
        setReason(HttpStatus.SC_GATEWAY_TIMEOUT, "Gateway Timeout");
        setReason(HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED, "Http Version Not Supported");
        setReason(HttpStatus.SC_GONE, "Gone");
        setReason(HttpStatus.SC_LENGTH_REQUIRED, "Length Required");
        setReason(HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable");
        setReason(HttpStatus.SC_EXPECTATION_FAILED, "Expectation Failed");

        // WebDAV Server-specific status codes
        setReason(HttpStatus.SC_PROCESSING, "Processing");
        setReason(HttpStatus.SC_MULTI_STATUS, "Multi-Status");
        setReason(HttpStatus.SC_UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        setReason(HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE, "Insufficient Space On Resource");
        setReason(HttpStatus.SC_METHOD_FAILURE, "Method Failure");
        setReason(HttpStatus.SC_LOCKED, "Locked");
        setReason(HttpStatus.SC_INSUFFICIENT_STORAGE, "Insufficient Storage");
        setReason(HttpStatus.SC_FAILED_DEPENDENCY, "Failed Dependency");
    }

}
