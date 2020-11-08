package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import us.sep.exam.entity.ExamScoreDO;

import java.util.List;
import java.util.Optional;

public interface ExamScoreRepo extends JpaRepository<ExamScoreDO,Long> {

    Optional<ExamScoreDO> findByExamScoreId(String examScoreId);

    /**
     * 查找单次考试的所有用户分数
     * @param examDetailId
     * @return
     */
    List<ExamScoreDO> findByExamDetailId(String examDetailId);

    /**
     * 查找单个用户的所有分数
     * @param userId
     * @return
     */
    List<ExamScoreDO> findByUserId(String userId);

    List<ExamScoreDO> findByStuNo(String stuNo);

    /**
     * 删除单次考试分数记录
     * @param examDetailId
     */
    void deleteByExamDetailId(String examDetailId);

    /**
     * 删除单个用户所有记录
     * @param userId
     */
    void deleteByUserId(String userId);

    /**
     * 删除单条记录
     * @param scoreId
     */
    void deleteByExamScoreId(String scoreId);

    boolean existsByExamDetailId(String examDetailId);

    boolean existsByUserId(String userId);

    boolean existsByExamScoreId(String examScoreId);

    boolean existsByUserIdAndExamDetailId(String userId , String examDetailId);

}
