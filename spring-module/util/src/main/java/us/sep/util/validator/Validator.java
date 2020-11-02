/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.validator;

/**
 * 校验器
 *
 * @author dango.yxm
 * @version : Validator.java 2018/10/05 下午10:56 dango.yxm
 */
public interface Validator<R> {

    /**
     * 是否支持校验
     *
     * @param request
     * @return
     */
    boolean support(R request);

    /**
     * 校验
     *
     * @param request
     */
    void validate(R request);
}
