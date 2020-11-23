package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTypeRequest {

    @NotBlank(message = "考试类型名不能为空")
    private String examTypeName;

    @NotBlank(message = "考试报名限制不能为空")
    private String examLimit;

    @NotBlank(message = "考试类型描述信息不能为空")
    private String examTypeDescription;

    private String examTypeId;
}
