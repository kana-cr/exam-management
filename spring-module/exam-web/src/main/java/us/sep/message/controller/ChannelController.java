package us.sep.message.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.message.request.ChannelRequest;
import us.sep.biz.message.service.ChannelService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.message.builder.ChannelBO;
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
  * @version  2020/10/16 10:32
  */
@CrossOrigin
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Resource
    ChannelService channelService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ChannelBO>> getChannel(ChannelRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),channelService.getChannel(request,pageNum,pageSize));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ChannelBO> createChannel(@Valid ChannelRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),channelService.createChannel(request));
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ChannelBO> updateChannel(@Valid ChannelRequest request , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getChannelId(), "channelId不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), channelService.updateChannel(request));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteChannel(String channelId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(channelId, "channelId不能为空");
        channelService.deleteChannel(channelId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),channelId);
    }

}
