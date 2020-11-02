package us.sep.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.sep.user.entity.User;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String username);

    Optional<User> findByUserId(String userid);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserName(String userName);

    boolean existsByUserName(String username);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);



}
