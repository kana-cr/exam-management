package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamEntryBlackListDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamEntryBlackListRepo extends JpaRepository<ExamEntryBlackListDO,Long> {

    List<ExamEntryBlackListDO> findByExamEntryId(String examEntryId);

    List<ExamEntryBlackListDO> findByExamDetailId(String examDetailId);

    Optional<ExamEntryBlackListDO> findByExamBlacklistId(String blacklistId);

    List<ExamEntryBlackListDO> findByUserId(String userId);

    List<ExamEntryBlackListDO> findByTerm(String term);

    void deleteByExamEntryId(String examEntryId);

    void deleteByExamDetailId(String examDetailId);

    void deleteByExamBlacklistId(String blacklistId);

    void deleteByUserId(String userId);

}
