package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.ExamEntryRequest;
import us.sep.biz.exam.service.ExamEntryRecordService;
import us.sep.biz.exam.service.ExamEntryService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.exam.builder.ExamEntryBO;
import us.sep.exam.builder.ExamEntryRecordBO;
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
@RequestMapping("/examEntry")
public class ExamEntryController {

    @Resource
    ExamEntryService examEntryService;

    @Resource
    ExamEntryRecordService examEntryRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryBO> getExamEntry(String examEntryId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examEntryId,"考试报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.getExamEntry(examEntryId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamEntryBO>> getExamEntries(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize ,HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.getExamEntries(pageNum,pageSize));
    }

    @GetMapping("/record")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamEntryRecordBO>> getExamEntryRecord(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryRecordService.getAllExamEntryRecord(pageNum,pageSize));
    }

    @GetMapping("/recordByEntry")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryRecordBO> getExamEntryById(String examEntryId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examEntryId,"考试报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryRecordService.getExamEntryRecordByExamEntry(examEntryId));
    }

    @GetMapping("/term")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamEntryBO>> getExamEntryByTerm(String term , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(term,"学期信息不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.getExamEntryByTerm(term));
    }

    @GetMapping("/examDetail")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryBO> getExamEntryByExamDetail(String examDetailId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examDetailId,"考试信息不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.getExamEntryByExamDetail(examDetailId));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryBO> createExamEntry(@Valid ExamEntryRequest request , HttpServletRequest httpServletRequest){
        AssertUtil.assertNotNull(request.getNumber(),"报名人数限额不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.createExamEntry(request));
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryBO> modifyExamEntry(@Valid ExamEntryRequest request , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getExamEntryId(),"考试报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.modifyExamEntry(request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryBO> deleteExamEntry(String examEntryId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examEntryId,"考试报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.deleteExamEntry(examEntryId));
    }

    @DeleteMapping("/examDetail")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamEntryBO> deleteExamEntryByExamDetail(String examDetailId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examDetailId,"考试信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examEntryService.deleteExamEntryByExamDetail(examDetailId));
    }

}
