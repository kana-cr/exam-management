package us.sep.exam.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDetailBO  {

    private String examTypeId;

    private String examDescription;

    private String examDetailId;

    private String examStartTime;

    private String examEndTime;

    private String examLocation;

    private String examAnnounce;

}
