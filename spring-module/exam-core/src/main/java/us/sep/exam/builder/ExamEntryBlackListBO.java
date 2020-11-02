package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamEntryBlackListBO {


    //单类考试具体信息id
    private String examDetailId;

    //黑名单id
    private String examBlacklistId;

    /**
     * 关联报名信息id
     */
    private String examEntryId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 原因
     */
    private String reason;

    /**
     * 加入黑名单学期
     */
    private String term;
}
