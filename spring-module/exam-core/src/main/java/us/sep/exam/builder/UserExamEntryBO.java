package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kana-cr
 * @version  2020/10/20 15:13
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamEntryBO  {


    /**
     * 考试报名信息id
     */
    private String examEntryId;


    /**
     * 用户报名考试id
     */
    private String userExamEntryId;

    /**
     * 报名用户id
     */
    private String userId;



}
