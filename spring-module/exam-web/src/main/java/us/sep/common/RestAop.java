/*
  betahouse.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.common;

import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * rest 切面
 */
public class RestAop {

    /**
     * 解析rest请求
     *
     * @param joinPoint
     */
    protected HttpServletRequest parseHttpServletRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof HttpServletRequest) {
                return (HttpServletRequest) o;
            }
        }
        return null;
    }


    /**
     * 解析rest请求
     *
     * @param joinPoint
     */
    protected RestRequest parseRestRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof RestRequest) {
                return (RestRequest) o;
            }
        }
        return null;
    }
}
