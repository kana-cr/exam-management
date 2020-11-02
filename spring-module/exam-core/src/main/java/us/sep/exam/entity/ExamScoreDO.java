package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamScoreBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_score", indexes = {
        @Index(name = "exam_score_id_index", columnList = "examScoreId")})
public class ExamScoreDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //单类考试具体信息id
    @Column(nullable = false , length = 32, updatable = false)
    private String examDetailId;

    //考试分数
    @Column(nullable = false , length = 32)
    private String examScore;

    //考试分数id
    @Column(nullable = false , length = 32, updatable = false)
    private String examScoreId;

    //用户id
    @Column(nullable = false , length = 32)
    private String userId;

    //学号
    @Column(nullable = false , length = 32)
    private String stuNo;

    public ExamScoreBO ToExamScoreBO(){
       return ExamScoreBO.builder().examDetailId(examDetailId).examScore(examScore).examScoreId(examScoreId)
                .userId(userId).stuNo(stuNo).build();
    }

}
