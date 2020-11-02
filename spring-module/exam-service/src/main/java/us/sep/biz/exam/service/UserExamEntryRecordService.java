package us.sep.biz.exam.service;

import us.sep.exam.builder.UserExamEntryRecordBO;

import java.util.List;

public interface UserExamEntryRecordService {


    List<UserExamEntryRecordBO> getUserExamEntryRecordByExamEntry(String examEntryId);

    List<UserExamEntryRecordBO> getUserExamEntryRecordByUserId(String userId);

    List<UserExamEntryRecordBO> getUserExamEntryRecordByIfAttend(boolean ifAttend);


}
