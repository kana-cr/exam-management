package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamDetailBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_type_detail", indexes = {
        @Index(name = "exam_type_detail_id_index", columnList = "examDetailId")})
public class ExamDetailDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //考试类别id
    @Column(nullable = false , length = 32)
    private String examTypeId;

    //考试内容
    @Column(nullable = false , length = 32)
    private String examDescription;;

    //单类考试具体信息id
    @Column(nullable = false , length = 32, updatable = false)
    private String examDetailId;

    //考试开始时间
    @Column(nullable = false , length = 32)
    private String examStartTime;

    //考试结束时间
    @Column(nullable = false , length = 32)
    private String examEndTime;

    //考试场地
    @Column(nullable = false , length = 32)
    private String examLocation;

    //成绩发布时间公告
    @Column(nullable = false , length = 32)
    private String examAnnounce;

    public ExamDetailBO ToExamDetailBO(){
      return ExamDetailBO.builder().examTypeId(examTypeId).examDetailId(examDetailId).examStartTime(examStartTime)
                .examEndTime(examEndTime).examDescription(examDescription).examLocation(examLocation)
                .examAnnounce(examAnnounce).build();
    }

}
