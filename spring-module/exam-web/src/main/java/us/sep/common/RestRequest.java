/*
  betahouse.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.common;


import us.sep.util.common.ToString;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * rest请求
 *
 * @author dango.yxm
 * @version : RestRequest.java 2018/11/25 12:22 AM dango.yxm
 */
public abstract class RestRequest extends ToString {

    private static final long serialVersionUID = -7591804651945805773L;

    /**
     * 请求id
     */
    @NotBlank
    private String requestId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    /**
     * 放入拓展信息
     *
     * @param key
     * @param value
     */
    public void putExtInfo(String key, String value) {
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        extInfo.put(key, value);
    }

    /**
     * 取出拓展信息
     *
     * @param key
     * @return
     */
    public String fetchExtInfo(String key) {
        if (extInfo == null) {
            return null;
        }
        return extInfo.get(key);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
