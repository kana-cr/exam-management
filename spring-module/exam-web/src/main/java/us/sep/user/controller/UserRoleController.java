package us.sep.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.user.request.UserRoleRequest;
import us.sep.biz.user.service.UserRoleManager;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author kana
 */
@CrossOrigin
@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Resource
    private UserRoleManager userRoleManager;

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserRoleRequest> createUserRole(@Valid UserRoleRequest request, HttpServletRequest httpServletRequest){
        userRoleManager.createUserRole(request);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserRoleRequest> deleteUserRole(@Valid UserRoleRequest request, HttpServletRequest httpServletRequest){
        userRoleManager.deleteUserRole(request);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }
}
