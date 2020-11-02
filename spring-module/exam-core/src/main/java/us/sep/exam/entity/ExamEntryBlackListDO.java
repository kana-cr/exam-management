package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamEntryBlackListBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_entry_blacklist", indexes = {
        @Index(name = "exam_blacklist_id_index", columnList = "examBlackListId")})
public class ExamEntryBlackListDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //单类考试具体信息id
    @Column(nullable = false , length = 32)
    private String examDetailId;

    //黑名单id
    @Column(nullable = false ,length = 32, updatable = false)
    private String examBlacklistId;

    /**
     * 关联报名信息id
     */
    @Column(nullable = false ,length = 32, updatable = false)
    private String examEntryId;

    /**
     * 用户id
     */
    @Column(length = 32, nullable = false, updatable = false)
    private String userId;

    /**
     * 原因
     */
    @Column(length = 32, nullable = false)
    private String reason;

    /**
     * 加入黑名单学期
     */
    @Column(length = 32, nullable = false)
    private String term;

    public ExamEntryBlackListBO ToExamEntryBlackListBO(){
      return ExamEntryBlackListBO.builder().examBlacklistId(examBlacklistId).examEntryId(examEntryId)
                .examDetailId(examDetailId).reason(reason).term(term).userId(userId).build();
    }
}
