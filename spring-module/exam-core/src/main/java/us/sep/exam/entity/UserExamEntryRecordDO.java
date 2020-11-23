package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.UserExamEntryRecordBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_exam_entry_record", indexes = {
        @Index(name = "exam_record_id_index", columnList = "userExamEntryRecordId")})
public class UserExamEntryRecordDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 考试报名信息id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String examEntryId;

    /**
     * 用户报名考试归档id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String userExamEntryRecordId;

    /**
     * 报名用户id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String userId;

    /**
     * 是否出席
     */
    @Column(nullable = false )
    private boolean ifAttend;

    public UserExamEntryRecordBO ToUserExamEntryBO(){
        return UserExamEntryRecordBO.builder().ifAttend(ifAttend).examEntryId(examEntryId).userExamEntryRecordId(userExamEntryRecordId).userId(userId).build();
    }

}
