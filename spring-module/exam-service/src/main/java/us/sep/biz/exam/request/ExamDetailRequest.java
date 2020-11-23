package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailRequest {

    private String examDetailId;

    @NotBlank(message = "考试类型id不能为空")
    @Size(min = 30 , max = 30 ,message = "考试类型id长度为32位")
    private String examTypeId;

    @NotBlank(message = "考试说明不能为空")
    private String examDescription;

    @NotBlank(message = "考试开始时间不能为空")
    private String examStartTime;

    @NotBlank(message = "考试结束时间不能为空")
    private String examEndTime;

    @NotBlank(message = "考场位置不能为空")
    private String examLocation;

    @NotBlank(message = "成绩发布时间公告")
    private String examAnnounce;
}
