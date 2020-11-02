/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.utils;

import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具
 *
 * @author dango.yxm
 * @version : DateUtil.java 2018/10/06 下午1:28 dango.yxm
 */
public class DateUtil {

    private static final String MEDIUM_TIME = "MM月dd日 ";

    private static final String SHORT_TIME = "yyyyMMddHHmmss";

    private static final String YEAR_TIME = "yyyy";

    private static final String HOUR_TIME = "HH";

    private static final String MINUTE_TIME = "mm";

    private static final String MONTH_TIME= "MM";

    private static final String DAY = "dd";

    private static final String MONTH_DAY = "MMdd";

    private static final String YEAR_MONTH_DAY="yyyy-MM-dd";

    private static final String TIME_DESCRIPTION = "yyyy年MM月dd日 HH:mm";

    private static final String TIME_DATABASE = "yyyy-MM-dd HH:mm:ss";

    private static final String TIME_MONTH_DAY = "MM月dd日";

    /**
     * 获取短时间字符串
     *
     * @param date
     * @return
     */
    public static String getShortDatesStr(Date date) {
        return format(date, SHORT_TIME);
    }

    public static Date getDateByShortDatesSt(String str) {
        return parse(str, SHORT_TIME);
    }


   //该方法用于数据库中的日期转换为MEDIUM_TIME格式
    public static String getMediumDatesStr(String str) { return getMediumDatesStr(parse(str,TIME_DATABASE)); }

    private static String getMediumDatesStr(Date date) { return format(date, MEDIUM_TIME); }

    public static Date parseTime_Database(String str){return parse(str,TIME_DATABASE);}
    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        return format(date, YEAR_TIME);
    }

    /**
     * 获取月日
     *
     * @param date
     * @return
     */
    public static String getMonthDay(Date date) { return format(date, MONTH_DAY); }

    /**
     * 获取月份
     * @param date
     * @return
     */

    public static String getMonth(Date date) { return format(date, MONTH_TIME); }


    public static String getYearMonthDay(Date date){ return format(date, YEAR_MONTH_DAY); }
    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        return format(date, DAY);
    }

    /**
     * 获取小时
     */
    public static String getHour(Date date) { return format(date, HOUR_TIME); }

    /**
     * 获取分钟
     */
    public static String getMinute(Date date) { return format(date, MINUTE_TIME); }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 字符串转换成日期
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date parse(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期转换失败");
        }
    }

    /**
     * 校验日期格式 不符合则抛出业务异常
     * @param dateStr
     * @param format
     */
    public static void checkDateFormat(String dateStr, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
           dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"日期格式错误");
        }
    }
    /**
     * 判断当前是否在时间段内
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean nowIsBetween(Date start, Date end) {
        return isBetween(new Date(), start, end);
    }


    /**
     * 判断日期是不是在时间段内
     *
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        if (date.before(start) || date.after(end)) {
            return false;
        }
        return true;
    }


    /**
     * 转换成 yyyy年MM月dd日 形式
     */
   public static String parse_CH(String dateStr,String format){
       DateFormat dateFormat = new SimpleDateFormat(format);
       try {
           Date date = dateFormat.parse(dateStr);
           DateFormat CHFormat=new SimpleDateFormat(TIME_DESCRIPTION);
        return  CHFormat.format(date);
       } catch (ParseException e) {
           throw new IllegalArgumentException("日期转换失败");
       }
   }

   public static Date subMinute(Date date,int minute){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(Calendar.MINUTE,-minute);
       return calendar.getTime();
   }




}
