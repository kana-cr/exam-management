/*
  sep.us
  CopyRight (c) 2012 - 2018
 */
package us.sep.util.validator;

import java.util.List;

/**
 * 复数校验器
 *
 * @author dango.yxm
 * @version : MultiValidator.java 2018/10/05 下午11:53 dango.yxm
 */
public class MultiValidator<R> {

    /**
     * 校验器组件集合
     */
    private List<Validator> validatorList;

    /**
     * 校验
     *
     * @param request
     */
    public void validate(R request) {
        if (validatorList == null) {
            return;
        }
        for (Validator<R> validator : validatorList) {
            if (validator.support(request)) {
                validator.validate(request);
            }
        }
    }

    public List<Validator> getValidatorList() {
        return validatorList;
    }

    public void setValidatorList(List<Validator> validatorList) {
        this.validatorList = validatorList;
    }
}
