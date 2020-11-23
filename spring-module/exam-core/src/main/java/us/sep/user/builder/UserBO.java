package us.sep.user.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBO {

    private String userName;

    private String fullName;

    private String userId;

    private String email;
    private boolean enable;
}
