package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.sep.exam.entity.ExamEntryDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamEntryRepo extends JpaRepository<ExamEntryDO,Long> {

    List<ExamEntryDO> findByTerm(String term);

    Optional<ExamEntryDO> findByExamDetailId(String examDetailId);

    List<ExamEntryDO> findByState(String state);

    Optional<ExamEntryDO> findByExamEntryId(String examEntryId);

    void deleteByExamDetailId(String examDetailId);

    void deleteByExamEntryId(String examEntryId);

    boolean existsByExamEntryId(String examEntryId);

    boolean existsByExamDetailId(String examDetailId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE exam_entry SET version = version + 1,number = number - 1 WHERE version = :version and exam_entry_id =:examEntryId",nativeQuery = true)
    int updateVersionAndNumber(@Param("version") Long version , @Param("examEntryId")String examEntryId);

}
