package us.sep.user.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  * @author kana-cr
  * @version  2020/10/22 13:57
  */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorBO  {



    /**
     * 专业
     */
    private String major;

    /**
     * 专业小类(细分)
     */
    private String discipline;


    /**
     * 学生班级
     */
    private String className;

    /**
     * 班级人数
     */
    private String classNumber;



}
