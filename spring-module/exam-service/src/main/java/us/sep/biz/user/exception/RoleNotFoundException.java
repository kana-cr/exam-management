package us.sep.biz.user.exception;

import java.util.Map;


public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(Map<String, Object> data) {
        super(ErrorCode.Role_NOT_FOUND, data);
    }
}
