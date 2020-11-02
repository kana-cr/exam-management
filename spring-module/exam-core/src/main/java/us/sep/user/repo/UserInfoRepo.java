package us.sep.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.user.entity.UserInfoDO;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfoDO, Long> {

    Optional<UserInfoDO> findByUserId(String userId);

    void deleteByUserId(String userId);

    boolean existsByUserId(String userId);

}
