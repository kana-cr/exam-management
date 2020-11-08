/*
  betahouse.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.base.idfactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import us.sep.util.utils.DateUtil;

import java.util.Date;

/**
 * Id 工厂实现
 */
@Service
public class BizIdFactoryImpl implements BizIdFactory {

    /**
     * 随机数范围
     */
    private static int RANDOM_RANGE = 100000000;

    @Override
    public String getUserId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.User_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamTypeId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Exam_Type_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamTypeDetailId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Exam_Type_Detail_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamRecordId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Exam_Record_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamScoreId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Exam_Score_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamEntryId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Entry_Exam_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamEntryRecordId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Entry_Exam_Record_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamEntryBlackListId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.User_Entry_Exam_BlackList_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getChannelId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Channel_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getUserChannelId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.User_Channel_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getExamLocationId() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Exam_Location_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getUserExamEntry() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.User_Entry_Exam_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getUserExamEntryRecord() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.User_Entry_Exam_Record_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getCarousel() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.Carousel_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }

    @Override
    public String getImage() {
        StringBuilder builder = new StringBuilder(32);
        Date now = new Date();
        // 1-16 系统时间 16位
        builder.append(DateUtil.getShortDatesStr(now));
        // 16-24 随机数 8位随机数
        builder.append(getRandNum(8));
        // 24-28 业务码 4位业务码
        builder.append(IdTypeEnum.IMAGE_ID.getBizNum());
        // 28-32 业务自定义码
        builder.append(DateUtil.getYear(now));
        return builder.toString();
    }


    /**
     * 获取指定长度的随机数，不足向左补0
     *
     * @param length
     * @return
     */
    private String getRandNum(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("截取长度非法");
        }
        String numStr = String.valueOf((int) (Math.random() * 100000000));
        return getLengthString(numStr, length);
    }

    /**
     * 获取指定长度字符串，不足向左补0
     *
     * @param str
     * @param length
     * @return
     */
    private String getLengthString(String str, int length) {
        String lengthString = StringUtils.right(str, length);
        if (StringUtils.isBlank(str)) {
            return getZeroString(length);
        }
        if (length > str.length()) {
            return getZeroString(length - str.length()) + str;
        }
        return lengthString;
    }

    /**
     * 获取长度lenth的0字符串
     *
     * @param lenth
     * @return
     */
    private String getZeroString(int lenth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lenth; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}
