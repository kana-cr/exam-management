package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamRecordBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_record", indexes = {
        @Index(name = "exam_record_id_index", columnList = "examRecordId")})
public class ExamRecordDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //考试类别id
    @Column(nullable = false , length = 32, updatable = false)
    private String examTypeId;

    //考试内容
    @Column(nullable = false , length = 32)
    private String examDescription;;

    //单类考试具体信息id
    @Column(nullable = false , length = 32)
    private String examRecordId;

    //考试开始时间
    @Column(nullable = false , length = 32)
    private String examStartTime;

    //考试结束时间
    @Column(nullable = false , length = 32)
    private String examEndTime;

    //考试场地
    @Column(nullable = false , length = 32)
    private String examLocation;

    public ExamRecordBO ToExamRecordBO(){
        return ExamRecordBO.builder().examTypeId(examTypeId).examStartTime(examStartTime).examEndTime(examEndTime).examLocation(examLocation)
                .examDescription(examDescription).examRecordId(examRecordId).build();
    }


}
