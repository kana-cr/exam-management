package us.sep.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.user.request.UserInfoRequest;
import us.sep.biz.user.service.RoleService;
import us.sep.biz.user.service.UserInfoService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.security.utils.CurrentUserUtils;
import us.sep.user.builder.UserInfoBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @Author kana-cr
 * @Date  2020/10/11
 **/

@CrossOrigin
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Resource
    private CurrentUserUtils currentUserUtils;

    @Resource
    UserInfoService userInfoService;

    @Resource
    RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserInfoBO> getUserInfo(String username, HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(username,"用户名不能为空");
        String name = currentUserUtils.getCurrentUser().getUserName();
        if (!name.equals(username) && !roleService.IsManagerOrAdmin(name)){
            return new Result<>(false, CommonResultCode.FORBIDDEN.getCode(), CommonResultCode.FORBIDDEN.getMessage());
        }
        UserInfoBO userInfoBO = userInfoService.findByName(username);
        if (!StringUtils.isEmpty(userInfoBO))
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userInfoBO);

        return new Result<>(false, CommonResultCode.UNFOUNDED.getCode(), CommonResultCode.UNFOUNDED.getMessage());
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserInfoBO> SaveUserInfo(@Valid UserInfoRequest request, HttpServletRequest httpServletRequest){
       String name = currentUserUtils.getCurrentUser().getUserName();
        if (!name.equals(request.getUsername()) && !roleService.IsManagerOrAdmin(name)){
            return new Result<>(false, CommonResultCode.FORBIDDEN.getCode(), CommonResultCode.FORBIDDEN.getMessage());
        }
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), userInfoService.Save(request));

    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserInfoBO> UpdateUserInfo(UserInfoRequest request, HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(request,"请求体不能为空");
        AssertUtil.assertStringNotBlank(request.getUsername(),"用户名不能为空");
        String name = currentUserUtils.getCurrentUser().getUserName();
        if (!name.equals(request.getUsername()) && !roleService.IsManagerOrAdmin(name)){
            return new Result<>(false, CommonResultCode.FORBIDDEN.getCode(), CommonResultCode.FORBIDDEN.getMessage());
        }

        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), userInfoService.Update(request));

    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserInfoRequest> DeleteUserInfo(UserInfoRequest request, HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(request,"请求体不能为空");
        AssertUtil.assertStringNotBlank(request.getUsername(),"用户名不能为空");
        String name = currentUserUtils.getCurrentUser().getUserName();
        if (!name.equals(request.getUsername()) && !roleService.IsManagerOrAdmin(name)){
            return new Result<>(false, CommonResultCode.FORBIDDEN.getCode(), CommonResultCode.FORBIDDEN.getMessage());
        }
        userInfoService.delete(request.getUsername());
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);

    }


}
