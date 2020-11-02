package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.ExamScoreRequest;
import us.sep.biz.exam.service.ExamScoreService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.exam.builder.ExamScoreBO;
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
@RequestMapping("/examScore")
public class ExamScoreController {

    @Resource
    ExamScoreService examScoreService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamScoreBO>> getExamScoreByCondition(ExamScoreRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreService.findByCondition(request,pageNum,pageSize));
    }

    @GetMapping("/single")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamScoreBO> getExamScoreByExamScoreId(String examScoreId, HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examScoreId,"考试分数id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreService.findByExamScoreId(examScoreId));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamScoreBO>> getExamScoreByUserId(String userId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreService.findByUserId(userId));
    }

    @GetMapping("/examDetail")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamScoreBO>> getExamScoreByExamDetailId(String examDetailId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examDetailId,"考试id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreService.findByExamDetailId(examDetailId));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamScoreBO> createExamScore(@Valid ExamScoreRequest request , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreService.createExamScore(request));
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamScoreBO> updateExamScore(@Valid ExamScoreRequest request , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getExamScoreId(),"用户考试分数id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreService.updateExamScore(request));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteExamScoreByExamScoreId(String examScoreId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examScoreId,"用户考试分数id不能为空");
        examScoreService.deleteByExamScoreId(examScoreId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examScoreId);
    }


    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteExamScoreByUserId(String userId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        examScoreService.deleteByUserId(userId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userId);
    }


    @DeleteMapping("/examDetail")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteExamScoreByExamDetailId(String examDetailId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examDetailId,"考试id不能为空");
        examScoreService.deleteByExamDetailId(examDetailId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examDetailId);
    }






}
