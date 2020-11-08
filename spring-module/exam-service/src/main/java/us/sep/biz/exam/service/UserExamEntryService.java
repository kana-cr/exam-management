package us.sep.biz.exam.service;

import us.sep.biz.exam.request.UserExamEntryRequest;
import us.sep.exam.builder.UserExamEntryBO;

import java.util.List;

public interface UserExamEntryService {

    List<UserExamEntryBO> getUserEntryByExamEntryId(String examEntryId);

    List<UserExamEntryBO> getUserEntryByUserId(String userId);

    List<UserExamEntryBO> getUserEntryByCache(String examEntryId);

    UserExamEntryBO getUserEntryById(String userEntryId);

    UserExamEntryBO createUserEntry(UserExamEntryRequest request);

    UserExamEntryBO createUserEntryCas(UserExamEntryRequest request);

    Long getCacheRemain(String examEntryId);

    int getDbRemain(String examEntryId);

    List<UserExamEntryBO> deleteUserEntryByExamEntry(String examEntryId);

    List<UserExamEntryBO> deleteUserEntryByUserId(String userId);

    UserExamEntryBO deleteUserEntryById(String userEntryId);




}
