package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamLocationRequest {


    //单类考试具体信息id
    @NotBlank(message = "考试发布id不能为空")
    @Size(min = 30 , max = 30 ,message = "考试发布id长度为32位")
    private String examDetailId;

    //考试座位id
    private String examLocationId;

    //用户报名id
    private String userExamEntryId;

    //考试座位号
    @NotBlank(message = "考试座位号不能为空")
    private String location;

    //考生(用户)id
    @NotBlank(message = "考生id不能为空")
    private String userId;

    //监考老师名称
    @NotBlank(message = "监考教师不能为空")
    private String teacher;




}
