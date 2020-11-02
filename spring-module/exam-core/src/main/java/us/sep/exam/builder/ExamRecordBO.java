package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRecordBO  {

    //考试类别id
    private String examTypeId;

    //考试内容
    private String examDescription;;

    //单类考试具体信息id
    private String examRecordId;

    //考试开始时间
    private String examStartTime;

    //考试结束时间
    private String examEndTime;

    //考试场地
    private String examLocation;


}
