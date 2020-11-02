package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamLocationRequest {


    //单类考试具体信息id
    @NotBlank
    private String examDetailId;

    //考试座位id
    private String examLocationId;

    //用户报名id
    private String userExamEntryId;

    //考试座位号 1 - 60
    @NotBlank
    private String location;

    //考生(用户)id
    @NotBlank
    private String userId;

    //监考老师名称
    @NotBlank
    private String teacher;




}
