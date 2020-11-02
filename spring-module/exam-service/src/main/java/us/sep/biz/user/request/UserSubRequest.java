package us.sep.biz.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String channelId;

    private String userChannelId;
}
