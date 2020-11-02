package us.sep.message.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelBO {

    private String channelId;

    private String channel;

    private String ExamTypeId;
}
