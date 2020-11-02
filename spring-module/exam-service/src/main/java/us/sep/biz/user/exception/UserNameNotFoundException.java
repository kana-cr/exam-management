package us.sep.biz.user.exception;

import java.util.Map;


public class UserNameNotFoundException extends BaseException {
    public UserNameNotFoundException(Map<String, Object> data) {
        super(ErrorCode.USER_NAME_NOT_FOUND, data);
    }
}
