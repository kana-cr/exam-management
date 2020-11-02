/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.enums;

import org.apache.commons.lang.StringUtils;
import us.sep.util.common.ResultCode;

/**
 * restful 结果码
 * <br/> http的结果码
 *
 * @author dango.yxm
 * @version : RestResultCode.java 2018/11/21 10:18 PM dango.yxm
 */
public enum RestResultCode implements ResultCode {

    SUCCESS("200", "执行成功"),

    PARTIAL_CONTENT("206", "部分执行"),

    ILLEGAL_PARAMETERS("400", "参数异常"),

    UNAUTHORIZED("401", "未登录"),

    FORBIDDEN("403", "无权访问"),

    NOT_FOUND("404", "请求内容不存在"),

    SYSTEM_ERROR("500", "系统异常"),;

    private String code;

    private String message;

    public static RestResultCode getByCode(String code) {
        for (RestResultCode restResultCode : values()) {
            if (StringUtils.equals(restResultCode.getCode(), code)) {
                return restResultCode;
            }
        }
        return null;
    }

    RestResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
