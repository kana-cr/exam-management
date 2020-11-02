/**
 * sep.us
 * CopyRight (c) 2012 - 2019
 */
package us.sep.util.utils;

/**
 * @author MessiahJK
 * @version : NumberUtils.java 2019/01/27 21:31 MessiahJK
 */
public class NumberUtils {
    /**
     * 精度
     */
    private static final Double EPS =0.00000001;


    /**
     * 判空
     *
     * @param number
     * @return
     */
    public static boolean isBlank(Integer number){
        return number == null || number == 0;
    }

    /**
     * 判空
     *
     * @param number
     * @return
     */
    public static boolean isBlank(Double number){
        return number==null||(number>-EPS &&number<EPS );
    }

    /**
     * 判空
     *
     * @param number
     * @return
     */
    public static boolean isBlank(Long number){
        return number == null || number == 0;
    }

    public static boolean isNotBlank(Integer number){
        return !isBlank(number);
    }

    public static boolean isNotBlank(Double number){
        return !isBlank(number);
    }
    public static boolean isNotBlank(Long number){
        return !isBlank(number);
    }
}
