package us.sep.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.user.entity.Role;

import java.util.Optional;



@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleName);

    boolean existsByName(String name);

    void deleteByName(String name);

}
