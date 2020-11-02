/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import us.sep.util.exceptions.CustomizeException;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author dango.yxm
 * @version : LoggerUtil.java 2018/10/04 下午11:38 dango.yxm
 */
public class LoggerUtil {

    /**
     * 生成info 日志
     *
     * @param logger
     * @param template
     * @param params
     */
    public static void info(Logger logger, String template, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(getLogString(template, params));
        }
    }

    /**
     * 生成error 日志
     *
     * @param logger
     * @param template
     * @param params
     */
    public static void error(Logger logger, String template, Object... params) {
        logger.error(getLogString(template, params));
    }

    /**
     * 生成error 日志 并记录异常
     *
     * @param throwable
     * @param logger
     * @param template
     * @param params
     */
    public static void error(Throwable throwable, Logger logger, String template, Object... params) {
        String errorCode = null;
        String errorMsg;
        if (throwable instanceof Exception) {
            Exception exception = (Exception) throwable;
            errorMsg = exception.getMessage();
        } else {
            errorCode = getErrorCode(throwable);
            errorMsg = errorCode;
        }
        logger.error(getLogString(template, params) + ", ErrorCode=" + errorCode + ", errorMsg=" + errorMsg, throwable);
    }

    /**
     * 生成warn 日志
     *
     * @param logger
     * @param template
     * @param params
     */
    public static void warn(Logger logger, String template, Object... params) {
        logger.warn(getLogString(template, params));
    }

    /**
     * 生成warn 日志 并记录异常
     *
     * @param throwable
     * @param logger
     * @param template
     * @param params
     */
    public static void warn(Throwable throwable, Logger logger, String template, Object... params) {
        String errorCode;
        String errorMsg;
        if (throwable instanceof CustomizeException) {
            CustomizeException customizeException = (CustomizeException) throwable;
            errorCode = customizeException.getErrorCode();
            errorMsg = customizeException.getMessage();
        } else {
            errorCode = getErrorCode(throwable);
            errorMsg = errorCode;
        }
        logger.warn(getLogString(template, params) + ", ErrorCode=" + errorCode + ", errorMsg=" + errorMsg, throwable);
    }

    /**
     * 生成日志
     *
     * @param template
     * @param params
     * @return
     */
    private static String getLogString(String template, Object... params) {
        return MessageFormat.format(template, params);
    }

    /**
     * 获取异常code
     *
     * @param throwable
     * @return
     */
    private static String getErrorCode(Throwable throwable) {
        if (throwable == null) {
            return StringUtils.EMPTY;
        }
        try {
            Method mt = throwable.getClass().getDeclaredMethod("getCode");
            if (mt != null) {
                Object obj = mt.invoke(throwable);
                if (obj != null) {
                    obj.toString();
                }
            }
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }
}
