package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamLocationDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamLocationRepo extends JpaRepository<ExamLocationDO,Long> {

    List<ExamLocationDO> findByUserId(String userId);

    List<ExamLocationDO> findByExamDetailId(String examDetailId);

    Optional<ExamLocationDO> findByUserExamEntryId(String userExamEntryId);

    Optional<ExamLocationDO> findByExamLocationId(String examLocationId);

    void deleteByUserId(String userId);

    void deleteByExamDetailId(String examDetailId);

    void deleteByExamLocationId(String examLocationId);

    void deleteByUserExamEntryId(String userExamEntryId);

    boolean existsByExamDetailId(String examDetailId);

    boolean existsByUserId(String userId);

    boolean existsByExamLocationId(String examLocationId);

}
