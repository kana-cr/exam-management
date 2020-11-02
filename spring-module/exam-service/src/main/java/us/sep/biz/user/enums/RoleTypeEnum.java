package us.sep.biz.user.enums;

import lombok.Getter;

/**
 * @author shuang.kou
 */

@Getter
public enum RoleTypeEnum {
    USER("USER", "用户"),
    TEMP_USER("TEMP_USER", "临时用户"),
    MANAGER("MANAGER", "管理者"),
    ADMIN("ADMIN", "Admin");
    String name;
    String description;

    RoleTypeEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
