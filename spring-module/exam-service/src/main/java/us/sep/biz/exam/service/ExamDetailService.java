package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamDetailRequest;
import us.sep.exam.builder.ExamDetailBO;

import java.util.List;

/**
  * @author  kana-cr
  * @version   2020/10/15 9:32
  **/
public interface ExamDetailService {

    ExamDetailBO save(ExamDetailRequest request);

    List<ExamDetailBO> find(ExamDetailRequest request , int pageNum , int pageSize);

    void update(ExamDetailRequest request) throws InterruptedException;

    void deleteByExamTypeId(String examTypeId);

    void deleteByExamDetailId(String examDetailId);

}
