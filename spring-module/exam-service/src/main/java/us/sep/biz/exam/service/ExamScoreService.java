package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamScoreRequest;
import us.sep.exam.builder.ExamScoreBO;

import java.util.List;

/**
  * @author kana-cr
  * @version  2020/10/17 10:09
  */
public interface ExamScoreService {

    List<ExamScoreBO> findByCondition(ExamScoreRequest request , int pageNum ,int pageSize);

    List<ExamScoreBO> findByExamDetailId(String examDetailId);

    ExamScoreBO findByExamScoreId(String examScoreId);

    List<ExamScoreBO> findByUserId(String userId);

    void deleteByExamDetailId(String examDetailId);

    void deleteByExamScoreId(String examScoreId);

    void deleteByUserId(String userId);

    ExamScoreBO createExamScore(ExamScoreRequest request);

    ExamScoreBO updateExamScore(ExamScoreRequest request);
}
