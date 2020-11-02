package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamRecordRequest {

    private String examRecordId;

    @NotBlank
    private String examTypeId;

    @NotBlank
    private String examDescription;

    @NotBlank
    private String examStartTime;

    @NotBlank
    private String examEndTime;

    @NotBlank
    private String examLocation;

}
