package us.sep.biz.common.util;

import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;
import us.sep.util.utils.DateUtil;


public class CheckDateUtil {

    public static void checkDateFormat(String start , String end){
        DateUtil.checkDateFormat(start,"yyyy-MM-dd HH:mm");
        DateUtil.checkDateFormat(end,"yyyy-MM-dd HH:mm");
        if ( DateUtil.parse(start,"yyyy-MM-dd HH:mm").after(DateUtil.parse(end,"yyyy-MM-dd HH:mm"))){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"结束时间不能在开始时间之后");
        }
    }
}
