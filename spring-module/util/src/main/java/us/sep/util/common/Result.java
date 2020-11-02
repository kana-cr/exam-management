/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.common;

/**
 * 返回结构体
 *
 * @author dango.yxm
 * @version : Result.java 2018/10/06 下午2:13 dango.yxm
 */
public class Result<T> extends ToString {

    private static final long serialVersionUID = 8597086345883261973L;

    /**
     * 执行结果
     */
    private boolean success = false;

    /**
     * 是否需要重试
     */
    private boolean isRetry = false;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 数据
     */
    private T data;

    /**
     * 构造器
     *
     * @param success
     * @param errorCode
     * @param errorMsg
     * @param data
     */
    public Result(boolean success, String errorCode, String errorMsg, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public Result(boolean success, String errorCode, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isRetry() {
        return isRetry;
    }

    public void setRetry(boolean retry) {
        isRetry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}