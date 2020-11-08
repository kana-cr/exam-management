package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;

/**
 *  活动报名记录实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamEntryRecordBO extends AbstractAuditBase {



    /**
     * 考试报名记录id
     */
    private String examEntryRecordId;

    /**
     * 考试关联报名信息id
     */
    private String examEntryId;

    /**
     * 单类考试具体信息id
     */
    private String examDetailId;

    /**
     * 记录状态
     */
    private String state;

    /**
     * 备注(预留)
     */
    private String note;





}
