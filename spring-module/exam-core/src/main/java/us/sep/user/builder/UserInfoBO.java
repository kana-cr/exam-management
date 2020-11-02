package us.sep.user.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoBO {

    //真实姓名
    private String realName;

    //班级
    private String className;

    //学号
    private String stuNo;

    //专业
    private String major;

    //身份证号码
    private String identificationNumber;

    //用户id
    private String userId;
}
