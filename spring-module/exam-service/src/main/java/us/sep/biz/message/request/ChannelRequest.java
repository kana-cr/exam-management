package us.sep.biz.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelRequest {


    private String channelId;

    @NotBlank(message = "频道名不能为空")
    private String channel;

    @NotBlank(message = "考试类型id不能为空")
    private String examTypeId;
}
