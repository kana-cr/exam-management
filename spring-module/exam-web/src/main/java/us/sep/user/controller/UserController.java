package us.sep.user.controller;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.common.request.LogRequest;
import us.sep.biz.common.service.LogService;
import us.sep.biz.user.request.EmailRequest;
import us.sep.biz.user.request.UserRegisterRequest;
import us.sep.biz.user.request.UserUpdateRequest;
import us.sep.biz.user.service.UserService;
import us.sep.common.MailSendUtils;
import us.sep.biz.user.validator.EmailPatternValidator;
import us.sep.biz.common.util.RedisUtil;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.common.ipUtil;
import us.sep.user.builder.UserBO;
import us.sep.user.builder.UserRoleBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.log.LogMark;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @Author kana-cr
 * @Date  2020/10/8
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private  UserService userService;

    @Resource
    private LogService logService;

    @Resource
    private MailSendUtils mailSendUtils;

    @Resource
    private EmailPatternValidator emailPatternValidator;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/email")
    public Result<String> getVerifyCode(String email, HttpServletRequest httpServletRequest) {
       if (emailPatternValidator.isValid(email)) {
           String verifyCode = redisUtil.get(String.valueOf(email.hashCode()));
           if (StringUtils.isEmpty(verifyCode))
               return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), "该邮箱验证码不存在或已过期");

           return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), verifyCode);
       }
        return new Result<>(true, CommonResultCode.ILLEGAL_PARAMETERS.getCode(), CommonResultCode.ILLEGAL_PARAMETERS.getMessage(), "邮箱格式错误");
    }


    //60秒内不可重复调用邮箱服务
    @AvoidRepeatableCommit(timeout = 60000)
    @PostMapping("/email")
    public Result<String> sendEmail(String email, HttpServletRequest request) {

        if (!emailPatternValidator.isValid(email))
        return new Result<>(true, CommonResultCode.ILLEGAL_PARAMETERS.getCode(), CommonResultCode.ILLEGAL_PARAMETERS.getMessage(), "邮箱格式错误");

        long start = System.currentTimeMillis();
        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
            ip = LogMark.DEFAULT;
        }
        long end = System.currentTimeMillis();
        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setIp(ip);
        logRequest.setOperationName("sendEmail");
        logRequest.setIfSuccess("Y");
        logRequest.setTime(end - start + "ms");
        logRequest.setMessage("调用邮箱服务" );
        logRequest.setUserName("any");
        logService.Save(logRequest);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setReceiverName(email.split("@")[0]);
        emailRequest.setEmailTheme("邮箱服务");
        emailRequest.setReceiverEmailAddress(email);
        String verifyCode = mailSendUtils.VerifyCode(6);
        emailRequest.setEmailContent("邮箱验证码：" + verifyCode);

        mailSendUtils.sendEmailAsText(emailRequest);

        redisUtil.setEx(String.valueOf(email.hashCode()),verifyCode,60, TimeUnit.SECONDS);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),"发送成功");
    }

    @PutMapping("/email")
    public Result<String> modifyPasswordByEmail(String password , String email , String verifyCode , HttpServletRequest request){

        AssertUtil.assertStringNotBlank(password,"新密码不能为空");
        AssertUtil.assertStringNotBlank(email,"绑定邮箱不能为空");
        AssertUtil.assertStringNotBlank(verifyCode,"验证码不能为空");

        if (!emailPatternValidator.isValid(email))
            return new Result<>(true, CommonResultCode.ILLEGAL_PARAMETERS.getCode(), CommonResultCode.ILLEGAL_PARAMETERS.getMessage(), "邮箱格式错误");

        long start = System.currentTimeMillis();
        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
            ip = LogMark.DEFAULT;
        }
        long end = System.currentTimeMillis();
        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setIp(ip);
        logRequest.setOperationName("sendEmail");
        logRequest.setIfSuccess("Y");
        logRequest.setTime(end - start + "ms");
        logRequest.setMessage("调用邮箱服务" );
        logRequest.setUserName("any");
        logService.Save(logRequest);
        String redisVerifyCode = redisUtil.get(String.valueOf(email.hashCode()));
        if (StringUtils.isEmpty(redisVerifyCode))
            return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), "该邮箱验证码不存在或已过期");
        if (!redisVerifyCode.equals(verifyCode))
            return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), "验证码错误");

        userService.modifyPasswordByEmail(email,password);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),"修改成功");

    }

    @AvoidRepeatableCommit
    @PostMapping("/sign-up")
    public Result<UserRegisterRequest> signUp(@RequestBody @Valid UserRegisterRequest userRegisterRequest , HttpServletRequest request) {
        long start = System.currentTimeMillis();
        userService.save(userRegisterRequest);
        logger.info("注册用户：" + userRegisterRequest.getUserName());
        userRegisterRequest.setPassword(null);
        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
           ip = LogMark.DEFAULT;
        }
        long end = System.currentTimeMillis();
        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setIp(ip);
        logRequest.setOperationName("signUp");
        logRequest.setIfSuccess("Y");
        logRequest.setTime(end - start + "ms");
        logRequest.setMessage("注册成功 用户名：" + userRegisterRequest.getUserName());
        logRequest.setUserName(userRegisterRequest.getUserName());
        logService.Save(logRequest);

        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userRegisterRequest);
    }

    @AvoidRepeatableCommit
    @PostMapping("/manager")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public Result<UserRegisterRequest> createManager(@RequestBody @Valid UserRegisterRequest userRegisterRequest , HttpServletRequest request) {
        long start = System.currentTimeMillis();
        userService.saveManager(userRegisterRequest);
        logger.info("注册管理者：" + userRegisterRequest.getUserName());
        userRegisterRequest.setPassword(null);

        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
            ip = LogMark.DEFAULT;
        }
        long end = System.currentTimeMillis();
        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setIp(ip);
        logRequest.setOperationName("signUp");
        logRequest.setIfSuccess("Y");
        logRequest.setTime(end - start + "ms");
        logRequest.setMessage("注册成功 用户名：" + userRegisterRequest.getUserName());
        logRequest.setUserName(userRegisterRequest.getUserName());
        logService.Save(logRequest);

        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userRegisterRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Long> getUserNumber(HttpServletRequest request) {
        Long number = userService.getCount();
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),number);
    }

    @GetMapping("/userEmail")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserBO> getUserNameByEmail(String email , HttpServletRequest request) {
        AssertUtil.assertStringNotBlank(email,"邮箱不能为空");
        if (emailPatternValidator.isValid(email)) {
            UserBO userBo = userService.getUserByEmail(email);
            return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), userBo);
        }
        return new Result<>(true, CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "邮箱格式错误" );
    }

    @GetMapping("/role")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserRoleBO>> getUserRole(@RequestParam String userName ,HttpServletRequest request) {
        List<UserRoleBO> userRole = userService.getUserRole(userName);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userRole);
    }


    @GetMapping("/single")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserBO > getUser(String username ,HttpServletRequest request) {
        AssertUtil.assertStringNotBlank(username,"用户名不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userService.getUser(username));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserBO> > getAllUser(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize ,HttpServletRequest request) {
        Page<UserBO> allUser = userService.getAll(pageNum, pageSize);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),allUser.getContent());
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserUpdateRequest> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest ,HttpServletRequest request) {
        userService.update(userUpdateRequest);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userUpdateRequest);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteUserByUserName(@RequestParam("username") String username ,HttpServletRequest request) {
        userService.delete(username);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),username);
    }


    @DeleteMapping("/deleteInBatch")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<String>> deleteUserByUserNameInBatch(@RequestParam("username") List<String> username ,HttpServletRequest request) {
        userService.deleteInBatch(username);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),username);
    }

    @GetMapping("/check")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Boolean> ifManager(@RequestParam("username") String username ,HttpServletRequest request){
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userService.ifManager(username));
    }

    @GetMapping("/check/admin")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Boolean> ifAdmin(@RequestParam("username") String username ,HttpServletRequest request){
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userService.ifAdmin(username));
    }



}
