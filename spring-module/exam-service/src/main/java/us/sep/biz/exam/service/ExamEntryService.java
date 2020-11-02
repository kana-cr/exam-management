package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamEntryRequest;
import us.sep.exam.builder.ExamEntryBO;

import java.util.List;

public interface ExamEntryService {

    ExamEntryBO createExamEntry(ExamEntryRequest request);

    ExamEntryBO getExamEntry(String examEntryId);

    ExamEntryBO getExamEntryByExamDetail(String examDetailId);

    List<ExamEntryBO> getExamEntries(int pageNum , int pageSize);

    List<ExamEntryBO> getExamEntryByTerm(String term);

    ExamEntryBO modifyExamEntry(ExamEntryRequest request);

    ExamEntryBO deleteExamEntry(String examEntryId);

    ExamEntryBO deleteExamEntryByExamDetail(String examDetailId);


}
