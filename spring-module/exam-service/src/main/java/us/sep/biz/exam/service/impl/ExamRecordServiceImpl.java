package us.sep.biz.exam.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.biz.common.util.CheckDateUtil;
import us.sep.biz.exam.request.ExamRecordRequest;
import us.sep.biz.exam.service.ExamRecordService;
import us.sep.exam.builder.ExamRecordBO;
import us.sep.exam.entity.ExamRecordDO;
import us.sep.exam.repo.ExamRecordRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamRecordServiceImpl implements ExamRecordService {

    @Resource
    private  ExamRecordRepo examRecordRepo;

    @Override
    public List<ExamRecordBO> findExamRecordData(ExamRecordRequest request, int pageNum, int pageSize) {
        ExamRecordDO examRecordDO = new ExamRecordDO();

        if (!StringUtils.isEmpty(request.getExamRecordId())) {
            Optional<ExamRecordDO> optional =  examRecordRepo.findByExamRecordId(request.getExamRecordId());
            if (optional.isPresent()){
                List<ExamRecordBO> list = new ArrayList<>();
                list.add(optional.get().ToExamRecordBO());
                return list;
            }
        }
        if (!StringUtils.isEmpty(request.getExamTypeId()))
            examRecordDO.setExamTypeId(request.getExamTypeId());

        if (!StringUtils.isEmpty(request.getExamStartTime()))
            examRecordDO.setExamStartTime(request.getExamStartTime());


        if (!StringUtils.isEmpty(request.getExamEndTime()))
            examRecordDO.setExamEndTime(request.getExamEndTime());

        //校验日期
        CheckDateUtil.checkDateFormat(request.getExamStartTime() , request.getExamEndTime());

        if (!StringUtils.isEmpty(request.getExamDescription()))
            examRecordDO.setExamDescription(request.getExamDescription());

        if (!StringUtils.isEmpty(request.getExamLocation()))
            examRecordDO.setExamLocation(request.getExamLocation());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("examRecordId")
                .withMatcher("examTypeId" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examStartTime" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("examEndTime" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("examDescription" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examLocation" ,ExampleMatcher.GenericPropertyMatchers.contains());


        Example<ExamRecordDO> example = Example.of(examRecordDO , matcher);

        return examRecordRepo.findAll(example , PageRequest.of(pageNum,pageSize)).getContent().stream()
                .map(ExamRecordDO::ToExamRecordBO).collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamRecordBO> deleteExamRecordByType(String examTypeId) {
        if (!examRecordRepo.existsByExamTypeId(examTypeId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该类归档信息");
        List<ExamRecordDO> examRecordDOList = examRecordRepo.findByExamTypeId(examTypeId);
        examRecordRepo.deleteInBatch(examRecordDOList);
        return examRecordDOList.stream().map(ExamRecordDO::ToExamRecordBO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamRecordBO deleteExamRecord(String examRecordId) {
        if (!examRecordRepo.existsByExamRecordId(examRecordId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该归档信息id");

        ExamRecordBO examRecordBO =  examRecordRepo.findByExamRecordId(examRecordId).get().ToExamRecordBO();
        examRecordRepo.deleteByExamRecordId(examRecordId);
        return examRecordBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamRecordBO modifyExamRecord(ExamRecordRequest request) {
        if (!examRecordRepo.existsByExamRecordId(request.getExamRecordId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该归档信息id");

        ExamRecordDO examRecordDO = examRecordRepo.findByExamRecordId(request.getExamRecordId()).get();

        if (!StringUtils.isEmpty(request.getExamTypeId()))
            examRecordDO.setExamTypeId(request.getExamTypeId());

        if (!StringUtils.isEmpty(request.getExamStartTime()))
            examRecordDO.setExamStartTime(request.getExamStartTime());


        if (!StringUtils.isEmpty(request.getExamEndTime()))
            examRecordDO.setExamEndTime(request.getExamEndTime());

        //校验日期
        CheckDateUtil.checkDateFormat(request.getExamStartTime() , request.getExamEndTime());

        if (!StringUtils.isEmpty(request.getExamDescription()))
            examRecordDO.setExamDescription(request.getExamDescription());

        if (!StringUtils.isEmpty(request.getExamLocation()))
            examRecordDO.setExamLocation(request.getExamLocation());

        examRecordRepo.save(examRecordDO);

        return examRecordDO.ToExamRecordBO();
    }
}
