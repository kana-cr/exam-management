package us.sep.biz.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRequest {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "权限名不能为空")
    private String roleName;
}
