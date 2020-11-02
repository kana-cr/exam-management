package us.sep.biz.user.request;

import lombok.Data;


/**
 * @description 用户登录请求DTO
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
    private Boolean rememberMe;
}
