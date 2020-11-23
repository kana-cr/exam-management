package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author kana-cr
 * @version  2020/10/20 15:13
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamEntryRequest {


    /**
     * 考试报名信息id
     */
    @NotBlank(message = "考试报名id不能为空")
    @Size(min = 30 , max = 30 ,message = "考试报名id长度为32位")
    private String examEntryId;


    /**
     * 用户报名考试id
     */
    private String userExamEntryId;

    /**
     * 报名用户id
     */
    @NotBlank(message = "报考用户id不能为空")
    @Size(min = 30 , max = 30 ,message = "用户id长度为32位")
    private String userId;




}
