package us.sep.biz.user.validator;


import org.springframework.stereotype.Component;
@Component
public class EmailPatternValidator  {
    /**
     * 2 - 15 位字母 / 数字 / 简繁体字
     */
    private static final String EMAIL_REG_EXP = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";


    public boolean isValid(String fullNameField) {
        if (fullNameField == null) {
            // can not be null
            return false;
        }
        return fullNameField.matches(EMAIL_REG_EXP);
    }
}
