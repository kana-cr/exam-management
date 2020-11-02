package us.sep.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.user.request.RoleCreateRequest;
import us.sep.biz.user.service.RoleService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.user.entity.Role;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Resource
    RoleService roleService;

    @GetMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<List<Role>> getAllRole(HttpServletRequest httpServletRequest) {
        List<Role> roles = roleService.getAllRole();
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),roles);
    }

    @AvoidRepeatableCommit
    @PostMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<RoleCreateRequest> CreateRole(@Valid RoleCreateRequest request, HttpServletRequest httpServletRequest){
        roleService.save(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }

    @AvoidRepeatableCommit
    @PutMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<RoleCreateRequest> updateRole(RoleCreateRequest request, HttpServletRequest httpServletRequest){
        roleService.update(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }

    @AvoidRepeatableCommit
    @DeleteMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<RoleCreateRequest> deleteRole(RoleCreateRequest request, HttpServletRequest httpServletRequest){
        roleService.delete(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }
}
