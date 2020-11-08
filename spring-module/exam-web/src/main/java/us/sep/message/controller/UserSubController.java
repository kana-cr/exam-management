package us.sep.message.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.user.request.UserSubRequest;
import us.sep.biz.user.service.UserSubscriptionService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.message.builder.MessageBO;
import us.sep.message.builder.UserSubscriptionBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
  * @author kana-cr
  * @version  2020/10/16 10:57
  */

@CrossOrigin
@RestController
@RequestMapping("/userSub")
public class UserSubController {

    @Resource
    UserSubscriptionService userSubscriptionService;


    /**
     * 获取所有订阅关系信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserSubscriptionBO>> getUserSub(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userSubscriptionService.findAll(pageNum,pageSize));
    }

    @GetMapping("/channel")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserSubscriptionBO>> getUserSubByChannelId(String channelId,@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(channelId,"频道id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userSubscriptionService.findByChannelId(channelId,pageNum,pageSize));
    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserSubscriptionBO>> getUserSubByUserId(String userId,@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userSubscriptionService.findByUserId(userId,pageNum,pageSize));
    }


    @GetMapping("/message")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<MessageBO>> getUserSubMessage(String userId){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userSubscriptionService.getChannelMessage(userId));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserSubscriptionBO> createUserSub(@Valid UserSubRequest request , HttpServletRequest httpServletRequest){
        if (!userSubscriptionService.ifUserSubExist(request))
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userSubscriptionService.createUserSub(request));

        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), "用户已经订阅过该频道");
    }

   @DeleteMapping("/single")
   @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
   @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteUserSub(UserSubRequest request, HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(request,"请求体不能为空");
        AssertUtil.assertStringNotBlank(request.getUserChannelId(),"用户频道订阅id不能为空");
            userSubscriptionService.cancelUserSubByUserChannelId(request.getUserChannelId());
       return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request.getUserChannelId());
   }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserSubRequest> deleteUserSubInBatch(UserSubRequest request, HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(request,"请求体不能为空");
        if (!StringUtils.isEmpty(request.getChannelId()))
        userSubscriptionService.cancelUserSubByChannelId(request.getChannelId());

        if (!StringUtils.isEmpty(request.getUserId()))
            userSubscriptionService.cancelUserSubByUserId(request.getUserId());

        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }
}
