package us.sep.biz.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    @NotBlank(message = "发布人不能为空")
    private String publisher;

    @NotBlank(message = "消息主体不能为空")
    private String content;

    @NotBlank(message = "频道名不能为空")
    private String channel;

    @NotBlank(message = "考试类型不能为空")
    private String examType;

    @NotBlank(message = "考试说明不能为空")
    private String examDescription;

    @NotBlank(message = "是否发布信息描述不能为空")
    //0是暂不发布 1是发布
    private boolean ifPublish;
}
