package us.sep.user.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn
    private User user;

    @ManyToOne()
    @JoinColumn
    private Role role;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
