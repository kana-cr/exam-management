package us.sep.biz.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubRequest {

    @NotBlank(message = "用户id不能为空")
    private String userId;

    @NotBlank(message = "频道id不能为空")
    private String channelId;

    private String userChannelId;
}
