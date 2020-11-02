/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.log;

import org.apache.commons.lang.StringUtils;

/**
 * 日志级别
 *
 * @author dango.yxm
 * @version : LogLevel.java 2018/11/19 下午4:21 dango.yxm
 */
public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR;

    public static LogLevel getByCode(String level) {
        if (StringUtils.isBlank(level)) {
            return null;
        }
        for (LogLevel logLevel : values()) {
            if (StringUtils.equals(level, logLevel.name())) {
                return logLevel;
            }
        }
        return null;
    }
}
