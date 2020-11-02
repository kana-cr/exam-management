package us.sep.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.user.builder.MajorBO;

import javax.persistence.*;

/**
  * @author kana-cr
  * @version  2020/10/22 13:57
  */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "major", indexes = {
        @Index(name = "major_name_index", columnList = "major")})
public class MajorDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 专业
     */
    @Column(nullable = false,length = 32,updatable = false)
    private String major;

    /**
     * 专业小类(细分)
     */
    @Column(nullable = false,length = 32)
    private String discipline;


    /**
     * 学生班级
     */
    @Column(nullable = false,length = 32)
    private String className;

    /**
     * 班级人数
     */
    @Column(nullable = false,length = 32)
    private String classNumber;


    public MajorBO ToMajorBO(){
        return MajorBO.builder().className(className).classNumber(classNumber
        ).discipline(discipline).major(major).build();
    }

}
