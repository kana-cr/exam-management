package us.sep.message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.message.builder.UserSubscriptionBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_sub", indexes = {
        @Index(name = "channel_index", columnList = "channelId")})
public class UserSubscriptionDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 32, updatable = false)
    private String userId;

    //订阅频道id
    @Column(nullable = false,length = 32)
    private String channelId;

    //单条订阅记录id
    @Column(nullable = false,length = 32)
    private String userChannelId;

    public UserSubscriptionBO ToUserSubBO(){
       return UserSubscriptionBO.builder().userId(userId).channelId(channelId).userChannelId(userChannelId).build();
    }
}
