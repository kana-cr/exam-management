package us.sep.message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.message.builder.MessageBO;

import javax.persistence.*;

/**
  * @Author kana-cr
  * @Date  2020/10/10 9:10
  **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message", indexes = {
        @Index(name = "publisher_index", columnList = "publisher")})
public class MessageDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //推送人
    @Column(nullable = false)
    private String publisher;

    //消息内容
    @Column(nullable = false , columnDefinition = "text" ,length = 32, updatable = false)
    private String content;

    //订阅频道
    @Column(nullable = false,length = 32)
    private String channel;

    //考试类型id
    @Column(nullable = false,length = 32)
    private String examTypeId;

    //该种考试描述
    @Column(nullable = false,length = 32)
    private String examDescription;
    //消息是否发布
    @Column(nullable = false)
    private boolean ifPublish;
    public MessageBO ToMessageBO(){
       return MessageBO.builder().content(content).channel(channel)
                .publisher(publisher).examType(examTypeId)
                .examDescription(examDescription).ifPublish(ifPublish).build();
    }
}
