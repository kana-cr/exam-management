package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTypeRequest {

    @NotBlank
    private String examTypeName;

    @NotBlank
    private String examLimit;

    @NotBlank
    private String examTypeDescription;
}
