package us.sep.biz.schdule;

public interface ScheduleService {


    /**
     * 将完成的考试的信息归档
     */
    void finishExamDetail();

    /**
     * 结束报名
     */
    void finishExamEntries();
}
