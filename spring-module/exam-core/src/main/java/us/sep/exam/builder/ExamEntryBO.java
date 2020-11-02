package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamEntryBO{


    /**
     * 考试报名信息id
     */
    private String examEntryId;


    /**
     * 单类考试具体信息id
     */
    private String examDetailId;

    /**
     * 报名学期
     */
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
    private String contact;

    /**
     * 报名要求(预留)
     */
    private String note;

    /**
     * 版本号
     */
    private Long version;


}
