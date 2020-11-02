package us.sep.biz.user.exception;

import java.util.Map;


public class RoleAlreadyExistException extends BaseException {
    public RoleAlreadyExistException(Map<String, Object> data) {
        super(ErrorCode.ROLE_ALREADY_EXIST, data);
    }
}
