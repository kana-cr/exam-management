package us.sep.biz.exam.service.impl;

import org.springframework.stereotype.Service;
import us.sep.biz.exam.service.UserExamEntryRecordService;
import us.sep.exam.builder.UserExamEntryRecordBO;
import us.sep.exam.entity.UserExamEntryRecordDO;
import us.sep.exam.repo.UserExamEntryRecordRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserExamEntryRecordServiceImpl implements UserExamEntryRecordService {

    @Resource
    UserExamEntryRecordRepo userExamEntryRecordRepo;

    @Override
    public List<UserExamEntryRecordBO> getUserExamEntryRecordByExamEntry(String examEntryId) {
       if (!userExamEntryRecordRepo.existsByExamEntryId(examEntryId))
           throw new CustomizeException(CommonResultCode.UNFOUNDED,"没有找到该报名信息");

       return userExamEntryRecordRepo.findByExamEntryId(examEntryId).stream().map(UserExamEntryRecordDO::ToUserExamEntryBO).collect(Collectors.toList());
    }

    @Override
    public List<UserExamEntryRecordBO> getUserExamEntryRecordByUserId(String userId) {
        if (!userExamEntryRecordRepo.existsByUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"没有找到该用户的报名信息");

        return userExamEntryRecordRepo.findByUserId(userId).stream().map(UserExamEntryRecordDO::ToUserExamEntryBO).collect(Collectors.toList());
    }

    //todo 黑名单模块 待完成
    @Override
    public List<UserExamEntryRecordBO> getUserExamEntryRecordByIfAttend(boolean ifAttend) {
        return null;
    }
}
