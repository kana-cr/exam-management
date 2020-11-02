package us.sep.biz.exam.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamScoreRequest {

    //单类考试具体信息id
    @NotBlank
    private String examDetailId;

    //考试分数
    @NotBlank
    private String examScore;

    //考试分数id
    private String examScoreId;

    //用户id
    @NotBlank
    private String userId;

    //学号
    @NotBlank
    private String stuNo;

}
