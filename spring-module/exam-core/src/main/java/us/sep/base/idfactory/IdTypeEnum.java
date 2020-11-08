package us.sep.base.idfactory;

/**
 * Id类型枚举
 */
public enum IdTypeEnum {

    /**
     * 用户id
     */
    User_ID("1001", "用户id"),

    /**
     * 考试大类id
     */
    Exam_Type_ID("1002", "考试类型id"),

    /**
     * 单次考试细分id
     */
    Exam_Type_Detail_ID("1003", "单次考试id"),

    /**
     * 考试归档id
     */
    Exam_Record_ID("1004", "考试归档id"),

    /**
     * 频道订阅id
     */
    Channel_ID("1005", "频道订阅id"),

    /**
     * 生成用户订阅频道id
     */
    User_Channel_ID("1006","用户订阅频道id"),

    /**
     * 生成考试成绩id
     */
    Exam_Score_ID("1007","考试成绩id"),

    /**
     * 生成考试报名id
     */
    Entry_Exam_ID("1008","考试报名id"),

    /**
     * 生成考试报名归档id
     */
    Entry_Exam_Record_ID("1009","考试报名归档id"),

    /**
     * 生成考试报名黑名单id
     */
    User_Entry_Exam_BlackList_ID("1010","考试报名黑名单id"),

    /**
     * 生成用户考试座位id
     */
    Exam_Location_ID("1011","用户考试座位id"),

    /**
     * 生成学生报名id
     */
    User_Entry_Exam_ID("1012","用户考试报名id"),

    /**
     * 生成学生报名归档id
     */
    User_Entry_Exam_Record_ID("1013","用户报名归档id"),

    /**
     * 轮播图id
     */
    Carousel_ID("1014","轮播图id"),

    /**
     * 轮播图
     */
    IMAGE_ID("1015","轮播图id"),
    ;


    /**
     * 业务id
     */
    private final String bizNum;

    /**
     * 描述
     */
    private final String desc;

    IdTypeEnum(String bizNum, String desc) {
        this.bizNum = bizNum;
        this.desc = desc;
    }

    public String getBizNum() {
        return bizNum;
    }

    public String getDesc() {
        return desc;
    }
}
