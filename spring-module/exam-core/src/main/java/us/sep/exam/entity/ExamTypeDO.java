package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamTypeBO;

import javax.persistence.*;

/**
  * @Author kana-cr
  * @Date  2020/10/10 9:58
  **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_type", indexes = {
        @Index(name = "exam_type_id_index", columnList = "examTypeId")})
public class ExamTypeDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 32, updatable = false)
    private String examTypeId;

    @Column(nullable = false , length = 32)
    private String examTypeName;

    @Column(nullable = false , length = 32)
    private String examTypeDescription;

    @Column(nullable = false , length = 32)
    private String examLimit;

    public ExamTypeBO ToExamTypeBO(){
        return ExamTypeBO.builder().examLimit(examLimit).examTypeDescription(examTypeDescription)
                .examTypeName(examTypeName).examTypeId(examTypeId).build();
    }

}
