package us.sep.biz.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarouselRequest {

    private String carouselId;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String text;

    @NotBlank(message = "标签不能为空")
    private String label;

    @NotBlank(message = "发布日期不能为空")
    private String subDate;

    private String note;
}
