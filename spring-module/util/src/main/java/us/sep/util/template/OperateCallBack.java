/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.template;

import us.sep.util.common.Result;

/**
 * 操作模板
 *
 * @author dango.yxm
 * @version : OperateCallBack.java 2018/10/06 下午2:19 dango.yxm
 */
public interface OperateCallBack<T> {

    default void before() {
    }

    ;

    Result<T> execute();

    default void after() {
    }

    ;
}
