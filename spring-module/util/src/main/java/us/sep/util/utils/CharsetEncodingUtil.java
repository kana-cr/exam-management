/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import org.apache.commons.lang.StringUtils;

import java.nio.charset.Charset;

/**
 * 字符编码工具类
 *
 * @author dango.yxm
 * @version : CharsetEncodingUtil.java 2018/10/21 下午3:41 dango.yxm
 */
public class CharsetEncodingUtil {

    private static final Charset GBK = Charset.forName("GBK");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * 是否可以转换成GBK编码
     *
     * @param str
     * @return
     */
    public static boolean canEncodeGBK(String str) {
        return StringUtils.isNotBlank(str) && GBK.newEncoder().canEncode(str);
    }

    /**
     * 是否可以转换成UTF-8编码
     *
     * @param str
     * @return
     */
    public static boolean canEncodeUTF8(String str) {
        return StringUtils.isNotBlank(str) && UTF_8.newEncoder().canEncode(str);
    }
}
