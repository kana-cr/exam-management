/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author kiminomiku.ywj
 * @version : FileUtil.java 2018/12/12 PM 1:35 kiminomiku.ywj
 */

public enum FileEnums {
    /**
     * 上传成功
     */
    UPLOAD_SUCCESSFUL("上传成功"),
    /**
     * 上传失败
     */
    UPLOAD_FALSE("上传失败"),
    /**
     * 上传文件为空
     */
    UPLOAD_NULL("上传为空");

    /**
     * 描述
     */
    String desc;

    public static FileEnums getByCode(String name) {
        for (FileEnums fileEnum : values()) {
            if (!StringUtils.equals(name, fileEnum.name())) {
                continue;
            }
            return fileEnum;
        }
        return null;
    }

    FileEnums(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return this.name();
    }
}