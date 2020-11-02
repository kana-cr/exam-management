package us.sep.message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.message.builder.ChannelBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "channel", indexes = {
        @Index(name = "channel_index", columnList = "channel")})
public class ChannelDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //订阅频道id
    @Column(nullable = false,length = 32)
    private String channelId;

    //订阅频道名
    @Column(nullable = false)
    private String channel;

    //频道相关考试大类
    @Column(nullable = false)
    private String examTypeId;

    public ChannelBO ToChannelBO(){
       return ChannelBO.builder().ExamTypeId(examTypeId).channel(channel).channelId(channelId).build();
    }
}
