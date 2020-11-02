package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamRecordDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRecordRepo extends JpaRepository<ExamRecordDO,Long> {

    List<ExamRecordDO> findByExamTypeId(String id);

    Optional<ExamRecordDO> findByExamRecordId(String recordId);

    boolean existsByExamRecordId(String recordId);

    boolean existsByExamTypeId(String id);

    void deleteByExamTypeId(String id);

    void deleteByExamRecordId(String id);
}
