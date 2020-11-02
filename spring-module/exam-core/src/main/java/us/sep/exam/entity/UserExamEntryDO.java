package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.UserExamEntryBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_exam_entry", indexes = {
        @Index(name = "user_exam_entry_id_index", columnList = "userExamEntryId")})
public class UserExamEntryDO extends AbstractAuditBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 考试报名信息id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String examEntryId;


    /**
     * 用户报名考试id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String userExamEntryId;

    /**
     * 报名用户id
     */
    @Column(nullable = false , length = 32, updatable = false)
    private String userId;



    public UserExamEntryBO ToUserExamEntryBO(){
        return UserExamEntryBO.builder().examEntryId(examEntryId).userExamEntryId(userExamEntryId).userId(userId).build();
    }


}
