package us.sep.biz.exam.service;

import us.sep.exam.builder.ExamEntryRecordBO;

import java.util.List;

public interface ExamEntryRecordService {

    List<ExamEntryRecordBO> getAllExamEntryRecord(int pageNum,int paeSize);

    ExamEntryRecordBO getExamEntryRecordByExamEntry(String examEntryId);

    ExamEntryRecordBO getExamEntryRecordByExamEntryRecord(String examEntryRecordId);
}
