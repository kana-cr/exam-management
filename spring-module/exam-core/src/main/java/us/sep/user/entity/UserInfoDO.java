package us.sep.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.user.builder.UserInfoBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info", indexes = {
        @Index(name = "user_id_index", columnList = "userId")})
public class UserInfoDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //真实姓名
    @Column(nullable = false,length = 32)
    private String realName;

    //班级
    @Column(nullable = false,length = 32)
    private String className;

    //学号
    @Column(nullable = false,length = 32)
    private String stuNo;


    //专业
    @Column(nullable = false,length = 32)
    private String major;

    //身份证号码
    @Column(nullable = false,length = 32)
    private String identificationNumber;

    //用户id
    @Column(nullable = false,length = 32, updatable = false)
    private String userId;


    public UserInfoBO ToUserInfoBO(){
        return UserInfoBO.builder().userId(userId).stuNo(stuNo).realName(realName).major(major)
                .identificationNumber(identificationNumber).className(className).build();
    }


}
