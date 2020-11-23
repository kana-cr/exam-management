package us.sep.biz.exam.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamScoreRequest {

    //单类考试具体信息id
    @NotBlank(message = "考试发布id不能为空")
    @Size(min = 30 , max = 30 ,message = "考试发布id长度为32位")
    private String examDetailId;

    //考试分数
    @NotBlank(message = "考试分数不能为空")
    private String examScore;

    //考试分数id
    private String examScoreId;

    //用户id
    @NotBlank(message = "考生id不能为空")
    private String userId;

    //学号
    @NotBlank(message = "学号不能为空")
    private String stuNo;

}
