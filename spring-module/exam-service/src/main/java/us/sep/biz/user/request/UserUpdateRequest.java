package us.sep.biz.user.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    private String password;

    private String fullName;

    private Boolean enabled;

}
