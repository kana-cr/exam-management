/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import org.apache.commons.lang.StringUtils;
import us.sep.util.common.ResultCode;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import java.math.BigDecimal;

/**
 * 断言工具
 *
 * @author dango.yxm
 * @version : AssertUtil.java 2018/10/05 下午11:30 dango.yxm
 */
public class AssertUtil {

    /**
     * 断言true
     *
     * @param expression
     * @param errorMsg
     */
    public static void assertTrue(Boolean expression, String errorMsg) {
        if (expression == null || !expression) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), errorMsg);
        }
    }

    public static void assertTrue(Boolean expression) {
        if (expression == null || !expression) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertTrue(Boolean expression, ResultCode resultCode) {
        if (expression == null || !expression) {
            throw new CustomizeException(resultCode);
        }
    }

    public static void assertTrue(Boolean expression, ResultCode resultCode, String errorMsg) {
        if (expression == null || !expression) {
            throw new CustomizeException(resultCode.getCode(), errorMsg);
        }
    }

    /**
     * 断言对象等于null
     *
     * @param obj
     * @param errorMsg
     */
    public static void assertNull(Object obj, String errorMsg) {
        if (obj != null) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), errorMsg);
        }
    }

    /**
     * 断言对象不等于null
     *
     * @param obj
     */
    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    /**
     * 断言对象不等于null
     *
     * @param obj
     * @param errorMsg
     */
    public static void assertNotNull(Object obj, String errorMsg) {
        assertNotNull(obj, CommonResultCode.ILLEGAL_PARAMETERS.getCode(), errorMsg);
    }

    /**
     * 断言对象不等于null
     *
     * @param obj
     * @param errorCode
     * @param errorMsg
     */
    public static void assertNotNull(Object obj, String errorCode, String errorMsg) {
        if (obj == null) {
            throw new CustomizeException(errorCode, errorMsg);
        }
    }

    /**
     * 断言对象不等于null
     *
     * @param obj
     * @param resultCode
     */
    public static void assertNotNull(Object obj, ResultCode resultCode) {
        if (obj == null) {
            throw new CustomizeException(resultCode);
        }
    }

    /**
     * 断言字符串不为空串
     *
     * @param str
     * @param errorMsg
     */
    public static void assertStringNotBlank(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), errorMsg);
        }
    }

    /**
     * 断言字符串不为空串
     *
     * @param str
     * @param errorCode
     * @param errorMsg
     */
    public static void assertStringNotBlank(String str, String errorCode, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new CustomizeException(errorCode, errorMsg);
        }
    }

    /**
     * 断言字符串不为空串
     *
     * @param str
     */
    public static void assertStringNotBlank(String str) {
        if (StringUtils.isBlank(str)) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    /**
     * 断言字符串为空串
     *
     * @param str
     * @param errorMsg
     */
    public static void assertStringBlank(String str, String errorMsg) {
        if (StringUtils.isNotBlank(str)) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), errorMsg);
        }
    }

    public static void assertBigDecimalNotPositive(BigDecimal bigDecimal, String errorCode, String errorMsg){
        assertNotNull(bigDecimal, errorCode, errorMsg);
        if(bigDecimal.compareTo(BigDecimal.ZERO)>0){
            throw new CustomizeException(errorCode, errorMsg);
        }
    }

    public static void assertBigDecimalPositive(BigDecimal bigDecimal, String errorCode, String errorMsg){
        assertNotNull(bigDecimal, errorCode, errorMsg);
        if(bigDecimal.compareTo(BigDecimal.ZERO)<=0){
            throw new CustomizeException(errorCode, errorMsg);
        }
    }

    /**
     * 断言相等
     *
     * @param o1
     * @param o2
     * @param errorMsg
     */
    public static void assertEquals(Object o1, Object o2, String errorMsg) {
        if (!o1.equals(o2)) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), errorMsg);
        }
    }

    /**
     * 断言相等
     *
     * @param o1
     * @param o2
     */
    public static void assertEquals(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

}
