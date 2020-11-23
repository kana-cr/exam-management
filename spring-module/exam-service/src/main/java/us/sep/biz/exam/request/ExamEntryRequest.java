package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


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
    @NotBlank(message = "考试发布id不能为空")
    @Size(min = 30 , max = 30 ,message = "考试发布id长度为32位")
    private String examDetailId;

    /**
     * 报名学期
     */
    @NotBlank(message = "考试学期不能为空")
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
    @NotBlank(message = "联系方式不能为空")
    private String contact;

    /**
     * 报名要求(预留)
     */
    private String note;



}
