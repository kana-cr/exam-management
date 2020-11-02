package us.sep.user.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.user.entity.Role;
import us.sep.user.entity.User;
import us.sep.user.entity.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRoleAndUser(Role role , User user);

    List<UserRole> findByUser (User user);
}
