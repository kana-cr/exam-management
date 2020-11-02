package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamDetailDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamDetailRepo extends JpaRepository<ExamDetailDO,Long> {

    List<ExamDetailDO> findByExamTypeId(String id);

    Optional<ExamDetailDO> findByExamDetailId(String id);

    void deleteAllByExamTypeId(String id);

    void deleteByExamDetailId(String id);

    boolean existsByExamTypeId(String id);

    boolean existsByExamDetailId(String id);
}
