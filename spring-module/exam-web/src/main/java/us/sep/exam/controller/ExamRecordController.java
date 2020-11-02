package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.ExamRecordRequest;
import us.sep.biz.exam.service.ExamRecordService;
import us.sep.common.annotion.LoggerName;
import us.sep.exam.builder.ExamRecordBO;
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
@RequestMapping("/examRecord")
public class ExamRecordController {

    @Resource
    ExamRecordService examRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamRecordBO>> getExamRecord(ExamRecordRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examRecordService.findExamRecordData(request,pageNum,pageSize));
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamRecordBO> modifyExamRecord(@Valid ExamRecordRequest request, HttpServletRequest httpServletRequest ){
        AssertUtil.assertStringNotBlank(request.getExamRecordId(),"考试归档id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examRecordService.modifyExamRecord(request));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamRecordBO> deleteExamRecord(String examRecordId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examRecordId,"考试归档id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examRecordService.deleteExamRecord(examRecordId));
    }


    @DeleteMapping("/examType")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamRecordBO>> deleteExamRecordByExamType(String examTypeId , HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(examTypeId,"考试类型id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examRecordService.deleteExamRecordByType(examTypeId));
    }
}
