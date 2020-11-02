package us.sep.biz.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateRequest {

    @NotBlank(message = "权限名不能为空")
    private String name;
    @NotBlank(message = "权限描述不能为空")
    private String description;

}
