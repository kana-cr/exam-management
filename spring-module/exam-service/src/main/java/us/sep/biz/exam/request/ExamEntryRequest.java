package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamEntryRequest {


    /**
     * 考试报名信息id
     */
    private String examEntryId;


    /**
     * 单类考试具体信息id
     */
    @NotBlank
    private String examDetailId;

    /**
     * 报名学期
     */
    @NotBlank
    private String term;

    /**
     * 报名状态
     */
    private String state;

    /**
     * 报名人数限额
     */
    private Integer number;

    /**
     * 联系方式
     */
    @NotBlank
    private String contact;

    /**
     * 报名要求(预留)
     */
    private String note;



}
