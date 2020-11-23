package us.sep.biz.user.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.biz.user.validator.FullName;
import us.sep.user.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "用户密码不能为空")
    @Size(min = 6,max = 25,message = "密码必须为6 - 25位")
    private String password;

    @FullName
    @NotBlank(message = "用户昵称不能为空")
    private String fullName;

    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    public User toUser() {
        return User.builder().fullName(this.getFullName())
                .userName(this.getUserName())
                .enabled(true).email(email).build();
    }
}
