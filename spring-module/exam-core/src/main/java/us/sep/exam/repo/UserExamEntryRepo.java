package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import us.sep.exam.entity.UserExamEntryDO;

import java.util.List;
import java.util.Optional;

public interface UserExamEntryRepo extends JpaRepository<UserExamEntryDO,Long> {

    List<UserExamEntryDO> findByExamEntryId(String examEntryId);

    List<UserExamEntryDO> findByUserId(String userId);

    Optional<UserExamEntryDO> findByUserExamEntryId(String id);

    boolean existsByUserExamEntryId(String id);

    boolean existsByExamEntryId(String id);

    boolean existsByUserId(String id);

    boolean existsByUserIdAndExamEntryId(String userId , String examEntryId);

    void deleteByExamEntryId(String id);

    void deleteByUserId(String userId);

    Long countByExamEntryId(String examEntryId);

}
