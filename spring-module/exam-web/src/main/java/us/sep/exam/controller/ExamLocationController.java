package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.ExamLocationRequest;
import us.sep.biz.exam.service.ExamLocationService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.exam.builder.ExamLocationBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/examLocation")
public class ExamLocationController {

    @Resource
    ExamLocationService examLocationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamLocationBO>> getExamLocation(ExamLocationRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.findExamLocation(request,pageNum,pageSize));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamLocationBO>> getExamLocationByUser(ExamLocationRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.findByUserId(request));
    }

    @GetMapping("/examDetail")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamLocationBO>> getExamLocationByExamDetail(ExamLocationRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.findByExamDetailId(request));
    }

    @GetMapping("/userEntry")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamLocationBO> getExamLocationByUserEntry(ExamLocationRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.findByUserExamEntryId(request));
    }

    @GetMapping("/single")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamLocationBO> getSingleExamLocation(ExamLocationRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.findByExamLocationId(request));
    }
    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamLocationBO> createExamLocation(@Valid ExamLocationRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.createExamLocation(request));
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamLocationBO> updateExamLocation(@Valid ExamLocationRequest request, HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getExamLocationId(),"考场座位号id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.modifyExamLocation(request));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamLocationBO> deleteExamLocation(String examLocationId, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.deleteByExamLocationId(examLocationId));
    }

    @DeleteMapping("/examDetail")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamLocationBO>> deleteExamLocationByExamDetailId(String examDetailId, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.deleteByExamDetailId(examDetailId));
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamLocationBO>> deleteExamLocationByUserId(String userId, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.deleteByUserId(userId));
    }

    @DeleteMapping("/userEntry")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamLocationBO> deleteExamLocationByUserEntry(String userEntryId, HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examLocationService.deleteByUserExamEntryId(userEntryId));
    }

}
