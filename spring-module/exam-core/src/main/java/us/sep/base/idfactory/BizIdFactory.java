/*
  betahouse.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.base.idfactory;

/**
 * 业务id生成工厂
 * 生成32位业务id
 *
 */

public interface BizIdFactory {

    /**
     * 生成用户id
     *
     * @return
     */
    String getUserId();

    /**
     * 生成考试大类id
     *
     * @return
     */
    String getExamTypeId();


    /**
     * 生成单次考试id
     *
     * @return
     */
    String getExamTypeDetailId();


    /**
     * 生成考试归档id
     *
     * @return
     */
    String getExamRecordId();

    /**
     * 生成考试成绩id
     *
     * @return
     */
    String getExamScoreId();


    /**
     * 生成考试报名id
     *
     * @return
     */
    String getExamEntryId();

    /**
     * 生成考试报名归档id
     *
     * @return
     */
    String getExamEntryRecordId();


    /**
     * 生成考试报名黑名单id
     *
     * @return
     */
    String getExamEntryBlackListId();


    /**
     * 生成频道id
     *
     * @return
     */
    String getChannelId();

    /**
     * 生成用户订阅频道id
     *
     * @return
     */
    String getUserChannelId();

    /**
     * 生成用户考试座位id
     *
     * @return
     */
    String getExamLocationId();


    /**
     * 生成用户报名考试id
     *
     * @return
     */
    String getUserExamEntry();

    /**
     * 生成用户报名考试归档id
     *
     * @return
     */
    String getUserExamEntryRecord();

    /**
     * 生成轮播图id
     *
     * @return
     */
    String getCarousel();

    /**
     * 生成图片id
     *
     */
    String getImage();
}
