/*
  betahouse.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.common.annotion;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import us.sep.biz.common.request.LogRequest;
import us.sep.biz.common.service.LogService;
import us.sep.common.RestAop;
import us.sep.common.ipUtil;
import us.sep.security.utils.CurrentUserUtils;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.log.LogLevel;
import us.sep.util.log.LogMark;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;


/**
 * 用户日志摘要
 */
@Order(-1)
@Aspect
@Component
public class LogDigest extends RestAop {

    /**
     * 结果摘要日志模板
     */
    private final static String DIGEST_TEMPLATE = "[{0}] ip=[{1}], action=[{2}], result=[{3}], time=[{4}], errorMessage=[{5}] , user=[{6}]";

    @Resource
    private CurrentUserUtils currentUserUtils;

    @Resource
    private LogService logService;

    @Pointcut("execution(* us.sep..*(..)) && @annotation(us.sep.util.log.Log)")
    public void targetLog() {
    }


    @SuppressWarnings("unchecked")
    @Around("targetLog() && @annotation(log)")
    public Object loggerParse(ProceedingJoinPoint proceedingJoinPoint, Log log) throws Throwable {
        // 获取logger
        final Logger logger = LoggerFactory.getLogger(log.loggerName());

        //RestRequest request = parseRestRequest(proceedingJoinPoint);
        HttpServletRequest httpServletRequest = parseHttpServletRequest(proceedingJoinPoint);

        AssertUtil.assertNotNull(httpServletRequest, CommonResultCode.SYSTEM_ERROR.getCode(), "请求解析失败");


        // 获取ip+-
        String ip = parseIP(httpServletRequest);

        // 获取方法名称
        String methodName = proceedingJoinPoint.getSignature().getName();

        String resultMessage = null;
        long start = System.currentTimeMillis();
        Result result = (Result) proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        String time = (end - start) + "ms";
        String managerName = currentUserUtils.getCurrentUser().getUserName();
        resultMessage = MessageFormat.format(DIGEST_TEMPLATE, log.identity(), ip, methodName, parseResult(result), time , result.getErrorMsg() ,
                ("当前访问该接口的用户为：" + managerName));

        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setOperationName(methodName);
        logRequest.setIfSuccess(parseResult(result));
        logRequest.setTime(time);
        logRequest.setMessage(result.getErrorMsg());
        logRequest.setUserName(managerName);
        logService.Save(logRequest);

        log(logger, log.logLevel(), resultMessage);
        return result;
    }

    /**
     * 打印日志
     *
     * @param logger
     * @param logLevel
     * @param message
     */
    private void log(Logger logger, LogLevel logLevel, String message) {
        if (logLevel == null) {
            throw new IllegalArgumentException("日志级别不能为空");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger 不能为空");
        }
        switch (logLevel) {
            case INFO:
                logger.info(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case TRACE:
                logger.trace(message);
                break;
            default:
                throw new IllegalArgumentException("不存在日志级别");
        }
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    private String parseResult(Result result) {
        return result.isSuccess() ? LogMark.SUCCESS : LogMark.FAIL;
    }

    /**
     * 解析ip
     *
     * @param request
     * @return
     */
    private String parseIP(HttpServletRequest request) {
        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
            return LogMark.DEFAULT;
        }
        return ip;
    }


}
