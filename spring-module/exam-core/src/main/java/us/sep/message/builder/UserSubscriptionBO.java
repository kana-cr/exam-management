package us.sep.message.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionBO extends AbstractAuditBase {


    //用户id
    private String userId;

    //订阅频道
    private String channelId;

    //单条订阅记录id
    private String userChannelId;


}
