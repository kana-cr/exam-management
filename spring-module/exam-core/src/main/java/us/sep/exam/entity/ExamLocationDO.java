package us.sep.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.exam.builder.ExamLocationBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_location", indexes = {
        @Index(name = "exam_location_id_index", columnList = "examLocationId")})
public class ExamLocationDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //单类考试具体信息id
    @Column(nullable = false , length = 32, updatable = false)
    private String examDetailId;

    //用户报名id
    @Column(nullable = false , length = 32)
    private String userExamEntryId;

    //考试座位id
    @Column(nullable = false , length = 32, updatable = false)
    private String examLocationId;

    //考试座位号 1 - 60
    @Column(nullable = false , length = 32)
    private String location;

    //考生(用户)id
    @Column(nullable = false , length = 32, updatable = false)
    private String userId;

    //监考老师名称
    @Column(nullable = false , length = 32)
    private String teacher;


    public ExamLocationBO ToExamLocationBO(){
        return ExamLocationBO.builder().examLocationId(examLocationId).examDetailId(examDetailId).userExamEntryId(userExamEntryId)
                .location(location).userId(userId).teacher(teacher).build();
    }

}
