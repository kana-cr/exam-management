package us.sep.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.user.builder.UserBO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user", indexes = {
        @Index(name = "exam_user_name_id", columnList = "userName,userId", unique = true)})
public class User extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 32, updatable = false)
    private String userName;

    @Column(nullable = false,length = 32)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;

    @Column(nullable = false,length = 32, updatable = false)
    private String userId;

    @Column(nullable = false, updatable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserRole> userRoles = new ArrayList<>();

    public UserBO toUserBO() {
        return UserBO.builder().fullName(this.fullName)
                .userName(this.userName).userId(userId).email(email).enable(enabled).build();
    }

}
