/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.enums;

import org.apache.commons.lang.StringUtils;
import us.sep.util.common.ResultCode;

/**
 * 通用结果码
 *
 * @author dango.yxm
 * @version : CommonResultCode.java 2018/10/05 下午11:36 dango.yxm
 */
public enum CommonResultCode implements ResultCode {

    /**
     * 系统异常
     */
    SYSTEM_ERROR("系统异常"),

    /**
     * 参数异常
     */
    ILLEGAL_PARAMETERS("参数异常"),

    /**
     * 无权访问(未登录)
     */
    UNAUTHORIZED("未登录无权访问"),

    /**
     * 无权访问(无权限)
     */
    FORBIDDEN("无权访问"),

    /**
     * 调用成功
     */
    SUCCESS("调用成功"),

    /**
     * 未找到信息
     */
    UNFOUNDED("未找到信息");
    private String message;

    public static CommonResultCode getByCode(String code) {
        for (CommonResultCode commonResultCode : values()) {
            if (StringUtils.equals(commonResultCode.getCode(), code)) {
                return commonResultCode;
            }
        }
        return null;
    }

    CommonResultCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
