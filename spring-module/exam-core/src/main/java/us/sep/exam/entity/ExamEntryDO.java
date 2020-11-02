package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamEntryBO;

import javax.persistence.*;

/**
 *
 * 活动报名信息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "exam_entry",
        indexes = {
                @Index(name = "exam_entry_id", columnList = "examEntryId", unique = true)
        })
public class ExamEntryDO extends AbstractAuditBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 考试报名信息id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String examEntryId;


    /**
     * 单类考试具体信息id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String examDetailId;

    /**
     * 报名学期
     */
    @Column(nullable = false , length = 32)
    private String term;

    /**
     * 报名状态
     */
    @Column(nullable = false , length = 32)
    private String state;

    /**
     * 报名人数限额
     */
    @Column(nullable = false , length = 32)
    private Integer number;

    /**
     * 考方联系方式
     */
    @Column(nullable = false , length = 32)
    private String contact;

    /**
     * 报名要求(预留)
     */
    @Column(nullable = false , length = 32)
    private String note;

    /**
     * 版本号
     */
    @Column
    private Long version;

    public ExamEntryBO ToExamEntryBO(){
       return ExamEntryBO.builder().examEntryId(examEntryId).examDetailId(examDetailId).term(term)
                .state(state).number(number).contact(contact).note(note).version(version).build();
    }

}
