package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamLocationBO{


    //单类考试具体信息id
    private String examDetailId;

    //考试座位id
    private String examLocationId;

    //用户报名id
    private String userExamEntryId;

    //考试座位号 1 - 60
    private String location;

    //考生(用户)id
    private String userId;

    //监考老师名称
    private String teacher;



}
