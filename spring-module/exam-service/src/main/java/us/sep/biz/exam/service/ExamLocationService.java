package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamLocationRequest;
import us.sep.exam.builder.ExamLocationBO;

import java.util.List;


public interface ExamLocationService {




    List<ExamLocationBO> findExamLocation(ExamLocationRequest request, int pageNum, int pageSize);

    List<ExamLocationBO> findByExamDetailId(ExamLocationRequest request);

    ExamLocationBO findByUserExamEntryId(ExamLocationRequest request);

    ExamLocationBO findByExamLocationId(ExamLocationRequest request);

    List<ExamLocationBO> findByUserId(ExamLocationRequest request);

    ExamLocationBO createExamLocation(ExamLocationRequest request);

    ExamLocationBO modifyExamLocation(ExamLocationRequest request);

    ExamLocationBO deleteByUserExamEntryId(String userExamEntryId);

    ExamLocationBO deleteByExamLocationId(String examLocationId);

    List<ExamLocationBO> deleteByExamDetailId(String examDetailId);

    List<ExamLocationBO> deleteByUserId(String userId);
}
