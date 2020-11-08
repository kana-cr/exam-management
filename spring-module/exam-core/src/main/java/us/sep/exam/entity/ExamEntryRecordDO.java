package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamEntryRecordBO;

import javax.persistence.*;

/**
 *  活动报名记录实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "exam_entry_record",
        indexes = {
                @Index(name = "exam_entry_record_id", columnList = "examEntryRecordId", unique = true)
        })
public class ExamEntryRecordDO extends AbstractAuditBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 考试报名记录id
     */
    @Column(length = 32, nullable = false,updatable = false)
    private String examEntryRecordId;


    /**
     * 单类考试具体信息id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String examDetailId;


    /**
     * 考试关联报名信息id
     */
    @Column(length = 32, nullable = false,updatable = false)
    private String examEntryId;


    /**
     * 记录状态
     */
    @Column(length = 32, nullable = false)
    private String state;

    /**
     * 备注(预留)
     */
    @Column(length = 32, nullable = false)
    private String note;


    public ExamEntryRecordBO ToExamEntryRecordBO(){
        return ExamEntryRecordBO.builder().examEntryRecordId(examEntryRecordId).examEntryId(examEntryId)
                .state(state).note(note).examDetailId(examDetailId).build();
    }



}
