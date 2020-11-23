package us.sep.biz.user.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 专业管理请求dto
 */
@Data
public class MajorRequest {

    /**
     * 专业
     */
    @NotBlank(message = "专业大类不能为空")
    private String major;

    /**
     * 专业小类(细分)
     */
    @NotBlank(message = "专业不能为空")
    private String discipline;


    /**
     * 学生班级
     */
    @NotBlank(message = "学生班级不能为空")
    private String className;

    /**
     * 班级人数
     */
    @NotBlank(message = "班级人数不能为空")
    private String classNumber;
}
