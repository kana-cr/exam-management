package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelRequest {


    private String channelId;

    @NotBlank
    private String channel;

    @NotBlank
    private String examTypeId;
}
