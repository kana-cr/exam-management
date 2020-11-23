package us.sep.common.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.AuthBaiduScope;
import me.zhyd.oauth.enums.scope.AuthGithubScope;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.common.annotion.LoggerName;
import us.sep.common.auth.cache.AuthStateRedisCache;
import us.sep.security.service.AuthUserCacheService;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;
import us.sep.util.log.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/oauth")
@Slf4j
public class AuthController {

    @Resource
    private AuthStateRedisCache stateRedisCache;

    @Resource
    private AuthUserCacheService authUserCacheService;

    @Value("${auth.github.clientid}")
    private String github_client_id;

    @Value("${auth.github.secret}")
    private String github_secret;

    @Value("${auth.baidu.clientid}")
    private String baidu_client_id;

    @Value("${auth.baidu.secret}")
    private String baidu_secret;

    @Value("${auth.url}")
    private String url;

    @RequestMapping("/render/{source}")
    @ResponseBody
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        log.info("进入render：" + source);
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info(authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }

    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8000/oauth/callback/github
     */
    @RequestMapping("/callback/{source}")
    public Result<AuthUser> login(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request) {
        log.info("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        log.info(JSONObject.toJSONString(response));

        if (!response.ok()) {
           throw new CustomizeException(CommonResultCode.SYSTEM_ERROR,response.getMsg());
        }
        authUserCacheService.save(response.getData());
        return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(), authUserCacheService.getByUuid(response.getData().getUuid()));



    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<AuthUser>> getAllUser(HttpServletRequest request) {

        return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(), authUserCacheService.listAll());

    }

    @RequestMapping("/revoke/{source}/{uuid}")
    public Result<String> revokeAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) throws IOException {
        AuthRequest authRequest = getAuthRequest(source.toLowerCase());

        AuthUser user = authUserCacheService.getByUuid(uuid);
        if (null == user) {
            return new Result<>(true,CommonResultCode.UNFOUNDED.getCode(),CommonResultCode.UNFOUNDED.getMessage());
        }
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.revoke(user.getToken());
            if (response.ok()) {
                authUserCacheService.remove(user.getUuid());
                return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(),"用户 [" + user.getUsername() + "] 的 授权状态 已收回！");
            }

            return new Result<>(true,CommonResultCode.SYSTEM_ERROR.getCode(),CommonResultCode.SYSTEM_ERROR.getMessage(),"用户 [" + user.getUsername() + "] 的 授权状态 收回失败！" + response.getMsg());

        } catch (AuthException e) {
            return new Result<>(true,CommonResultCode.SYSTEM_ERROR.getCode(),CommonResultCode.SYSTEM_ERROR.getMessage(),e.getErrorMsg());
        }
    }

    @RequestMapping("/refresh/{source}/{uuid}")
    public Object refreshAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) {
        AuthRequest authRequest = getAuthRequest(source.toLowerCase());

        AuthUser user = authUserCacheService.getByUuid(uuid);
        if (null == user) {
               return new Result<>(true,CommonResultCode.UNFOUNDED.getCode(),CommonResultCode.UNFOUNDED.getMessage());
        }
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.refresh(user.getToken());
            if (response.ok()) {
                user.setToken(response.getData());
                authUserCacheService.save(user);
                return new Result<>(true,CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(),"用户 [" + user.getUsername() + "] 的 access token 已刷新！新的 accessToken: "
                        + response.getData().getAccessToken());
            }
            return new Result<>(true,CommonResultCode.SYSTEM_ERROR.getCode(),CommonResultCode.SYSTEM_ERROR.getMessage(),"用户 [" + user.getUsername() + "] 的 access token 刷新失败！" + response.getMsg());

        } catch (AuthException e) {
            return new Result<>(true,CommonResultCode.SYSTEM_ERROR.getCode(),CommonResultCode.SYSTEM_ERROR.getMessage(),e.getErrorMsg());
        }
    }

    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source.toLowerCase()) {
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder()
                        .clientId(baidu_client_id)
                        .clientSecret(baidu_secret)
                        .redirectUri(url + "baidu")
                        .scopes(Arrays.asList(
                                AuthBaiduScope.BASIC.getScope(),
                                AuthBaiduScope.SUPER_MSG.getScope(),
                                AuthBaiduScope.NETDISK.getScope()
                        ))
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(github_client_id)
                        .clientSecret(github_secret)
                        .redirectUri(url + "github")
                        .scopes(AuthScopeUtils.getScopes(AuthGithubScope.values()))
                        // 针对国外平台配置代理
                        /* .httpConfig(HttpConfig.builder()
                                 .timeout(15000)
                                 .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                 .build())*/
                        .build(),stateRedisCache);
                break;

            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }

}
