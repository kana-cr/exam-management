/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.exceptions;

import us.sep.util.common.ResultCode;
import us.sep.util.enums.CommonResultCode;

/**
 *  通用业务异常
 *
 * @author dango.yxm
 * @version : CustomizeException.java 2018/10/05 下午11:32 dango.yxm
 */
public class CustomizeException extends RuntimeException {

    private static final long serialVersionUID = -2372085495406352289L;

    /**
     * 异常码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 构造器
     *
     * @param cause
     */
    public CustomizeException(final Throwable cause) {
        super(cause);
        this.errorCode = CommonResultCode.SYSTEM_ERROR.getCode();
        this.message = CommonResultCode.SYSTEM_ERROR.getMessage();
    }

    public CustomizeException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public CustomizeException(ResultCode resultCode) {
        this.errorCode = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public CustomizeException(ResultCode resultCode, String message) {
        this.errorCode = resultCode.getCode();
        this.message = message;
    }

    public CustomizeException(Throwable cause, String errorCode, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public CustomizeException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 异常获取信息
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }
}
