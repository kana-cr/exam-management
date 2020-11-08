package us.sep.message.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.message.request.MessageRequest;
import us.sep.biz.message.service.MessageService;
import us.sep.biz.exam.CheckExamTypeUtil;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.message.builder.MessageBO;
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
  * @version   2020/10/12 10:51
  **/
@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    MessageService messageService;

    @Resource
    CheckExamTypeUtil checkExamTypeUtil;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<MessageBO>> getMessageByCondition(MessageRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize , HttpServletRequest httpServletRequest) {
        AssertUtil.assertNotNull(request);
       return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), messageService.find(request,pageNum,pageSize));

    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<MessageRequest> PushMessage(@Valid MessageRequest request , HttpServletRequest httpServletRequest) {
       if (checkExamTypeUtil.checkExamType(request.getExamType() ) ){
            messageService.save(request);
           return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
        }else {
           return new Result<>(false, CommonResultCode.ILLEGAL_PARAMETERS.getCode(), CommonResultCode.ILLEGAL_PARAMETERS.getMessage());
       }
    }

    /**
     * 删除满足条件的消息
     * @param request
     * @return
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<MessageRequest> DeleteMessage( MessageRequest request , HttpServletRequest httpServletRequest) {
        AssertUtil.assertNotNull(request,"请求不能为空");
            messageService.delete(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }
}
