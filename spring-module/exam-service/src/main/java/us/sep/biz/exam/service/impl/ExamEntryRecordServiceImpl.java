package us.sep.biz.exam.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import us.sep.biz.exam.service.ExamEntryRecordService;
import us.sep.exam.builder.ExamEntryRecordBO;
import us.sep.exam.entity.ExamEntryRecordDO;
import us.sep.exam.repo.ExamEntryRecordRepo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExamEntryRecordServiceImpl implements ExamEntryRecordService {

    @Resource
    ExamEntryRecordRepo examEntryRecordRepo;

    @Override
    public List<ExamEntryRecordBO> getAllExamEntryRecord(int pageNum, int paeSize) {
        return examEntryRecordRepo.findAll(PageRequest.of(pageNum,paeSize)).stream().map(ExamEntryRecordDO::ToExamEntryRecordBO).collect(Collectors.toList());
    }

    @Override
    public ExamEntryRecordBO getExamEntryRecordByExamEntry(String examEntryId) {
        return Objects.requireNonNull(examEntryRecordRepo.findByExamEntryId(examEntryId).orElse(null)).ToExamEntryRecordBO();
    }

    @Override
    public ExamEntryRecordBO getExamEntryRecordByExamEntryRecord(String examEntryRecordId) {
        return Objects.requireNonNull(examEntryRecordRepo.findByExamEntryRecordId(examEntryRecordId).orElse(null)).ToExamEntryRecordBO();
    }
}
