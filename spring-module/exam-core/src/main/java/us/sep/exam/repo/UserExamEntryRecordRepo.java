package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.UserExamEntryRecordDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserExamEntryRecordRepo extends JpaRepository<UserExamEntryRecordDO,Long> {

    List<UserExamEntryRecordDO> findByExamEntryId(String examEntryId);

    List<UserExamEntryRecordDO> findByUserId(String userId);

    Optional<UserExamEntryRecordDO> findByUserExamEntryRecordId(String id);

    boolean existsByExamEntryId(String id);

    boolean existsByUserId(String userId);
}
