/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author dango.yxm
 * @version : ToString.java 2018/10/05 下午11:03 dango.yxm
 */
public class ToString implements Serializable {

    private static final long serialVersionUID = 3587673707533082608L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
