package us.sep.common.annotion;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.sep.biz.common.util.RedisUtil;
import us.sep.common.RestAop;
import us.sep.common.ipUtil;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 重复提交aop
 *
 */
@Aspect
@Component
@Slf4j
public class AvoidRepeatableCommitAspect extends RestAop {

    @Autowired
    private RedisUtil redisUtil;

    private static final String AVOID_REPEATABLE_COMMIT = "AVOID_REPEATABLE_COMMIT";
    /**
     * @param point
     */
    @Around("@annotation(us.sep.common.annotion.AvoidRepeatableCommit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = parseHttpServletRequest(point);

        String ip = ipUtil.getIpAddr(request);

        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        //目标类、方法
        String className = method.getDeclaringClass().getName();

        String name = method.getName();

        // 得到类名和方法
        String ipKey = String.format("%s#%s", className, name);

        // 转换成HashCode
        int hashCode = Math.abs(ipKey.hashCode());

        String key = String.format("%s:%s_%d", AVOID_REPEATABLE_COMMIT, ip, hashCode);

        log.info("ipKey={},hashCode={},key={}", ipKey, hashCode, key);

        AvoidRepeatableCommit avoidRepeatableCommit = method.getAnnotation(AvoidRepeatableCommit.class);

        long timeout = avoidRepeatableCommit.timeout();

        String value = redisUtil.get(key);

        if (StringUtils.isNotBlank(value)) {
            log.info("请勿重复提交表单");
            return new Result<>(false, CommonResultCode.SYSTEM_ERROR.getCode(),CommonResultCode.SYSTEM_ERROR.getMessage(),"请勿重复提交表单");
        }

        // 设置过期时间
        redisUtil.setEx(key, UUID.randomUUID().toString().replaceAll("-", ""), timeout, TimeUnit.MILLISECONDS);

        //执行方法
        Object object = point.proceed();
        return object;
    }

}
