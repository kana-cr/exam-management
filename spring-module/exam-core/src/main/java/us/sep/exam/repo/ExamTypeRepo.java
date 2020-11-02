package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamTypeDO;

import java.util.Optional;

@Repository
public interface ExamTypeRepo extends JpaRepository<ExamTypeDO,Long> {

    Optional<ExamTypeDO> findByExamTypeId(String examTypeId);

    Optional<ExamTypeDO> findByExamTypeName(String name);

    boolean existsByExamTypeId(String examTypeId);

    boolean existsByExamTypeName(String examTypeName);

    void deleteByExamTypeId(String examTypeId);

    void deleteByExamTypeName(String examTypeName);
}
