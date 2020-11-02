/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.common;

/**
 * 结果码 接口
 *
 * @author dango.yxm
 * @version : ResultCode.java 2018/11/18 下午3:05 dango.yxm
 */
public interface ResultCode {

    /**
     * 获取码
     *
     * @return
     */
    String getCode();

    /**
     * 获取描述
     *
     * @return
     */
    String getMessage();
}
