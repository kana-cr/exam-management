package us.sep.biz.user.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 专业管理请求dto
 */
@Data
public class MajorRequest {

    /**
     * 专业
     */
    @Valid
    private String major;

    /**
     * 专业小类(细分)
     */
    @NotBlank
    private String discipline;


    /**
     * 学生班级
     */
    @NotBlank
    private String className;

    /**
     * 班级人数
     */
    @NotBlank
    private String classNumber;
}
