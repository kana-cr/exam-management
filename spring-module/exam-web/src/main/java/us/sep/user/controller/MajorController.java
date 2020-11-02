package us.sep.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.user.request.MajorRequest;
import us.sep.biz.user.service.MajorService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.user.builder.MajorBO;
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
  * @version  2020/10/22 14:34
  */
@CrossOrigin
@RestController
@RequestMapping("/major")
public class MajorController {

    @Resource
    MajorService majorService;


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<MajorBO>> getMajor( @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize ,HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), majorService.findMajors(pageNum,pageSize));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<MajorBO>> getMajor(String major, HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(major,"专业大类不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), majorService.findMajorByName(major));
    }

    @GetMapping("/class")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<MajorBO> getMajorByClass(String className, HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(className,"班级不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), majorService.findMajorByClass(className));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<MajorBO> createMajor(@Valid MajorRequest request, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), majorService.createMajor(request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<MajorBO>> modifyMajor(String major, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), majorService.deleteMajor(major));
    }

    @DeleteMapping("/class")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<MajorBO> modifyMajorByClass(String className, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), majorService.deleteByClassName(className));
    }
}
