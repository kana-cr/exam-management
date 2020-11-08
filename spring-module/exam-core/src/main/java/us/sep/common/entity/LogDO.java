package us.sep.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.common.builder.LogBO;
import us.sep.base.AbstractAuditBase;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log", indexes = {
        @Index(name = "transfer_user", columnList = "transferUserName")})
public class LogDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //调用接口的操作者
    @Column(nullable = false)
    private String transferUserName;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String message;

    //ip可能获取不到
    @Column
    private String ip;

    @Column(nullable = false)
    private String time;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean result;

    public LogBO ToLogBO(){
        return LogBO.builder().action(action).ip(ip)
                .message(message).name(transferUserName)
                .time(time).build();
    }
}
