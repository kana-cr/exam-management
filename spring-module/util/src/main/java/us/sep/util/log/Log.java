/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.log;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户日志
 *
 * @author dango.yxm
 * @version : Log.java 2018/11/19 下午4:11 dango.yxm
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 日志名称
     *
     * @return
     */
    String loggerName();

    /**
     * 日志标识
     *
     * @return
     */
    String identity() default LogMark.DEFAULT;

    /**
     * 日志级别
     *
     * @return
     */
    LogLevel logLevel() default LogLevel.INFO;
}
