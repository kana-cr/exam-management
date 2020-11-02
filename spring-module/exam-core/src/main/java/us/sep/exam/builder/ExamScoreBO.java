package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamScoreBO {

    //单类考试具体信息id
    private String examDetailId;

    //考试分数
    private String examScore;

    //考试分数id
    private String examScoreId;

    //用户id
    private String userId;

    //学号
    private String stuNo;


}
