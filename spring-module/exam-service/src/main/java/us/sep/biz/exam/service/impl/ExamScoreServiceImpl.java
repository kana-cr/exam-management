package us.sep.biz.exam.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.request.ExamScoreRequest;
import us.sep.biz.exam.service.ExamScoreService;
import us.sep.exam.builder.ExamScoreBO;
import us.sep.exam.entity.ExamScoreDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamScoreRepo;
import us.sep.user.entity.UserInfoDO;
import us.sep.user.repo.UserInfoRepo;
import us.sep.user.repo.UserRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamScoreServiceImpl implements ExamScoreService {

    @Resource
    private ExamScoreRepo examScoreRepo;

    @Resource
    private  ExamDetailRepo examDetailRepo;

    @Resource
    private UserRepo userRepo;

    @Resource
    private  UserInfoRepo userInfoRepo;

    @Resource
    private BizIdFactory bizIdFactory;

    @Override
    public List<ExamScoreBO> findByCondition(ExamScoreRequest request, int pageNum, int pageSize) {

        if (!StringUtils.isEmpty(request.getExamScoreId())){
            if (examScoreRepo.existsByExamScoreId(request.getExamScoreId())){
                List<ExamScoreBO> examScoreBOS = new ArrayList<>();
                examScoreBOS.add(examScoreRepo.findByExamScoreId(request.getExamScoreId()).get().ToExamScoreBO());
                return examScoreBOS;
            }
        }


        ExamScoreDO examScoreDO = new ExamScoreDO();

        if (!StringUtils.isEmpty(request.getExamDetailId()))
            examScoreDO.setExamDetailId(request.getExamDetailId());

        if (!StringUtils.isEmpty(request.getExamScore()))
            examScoreDO.setExamScore(request.getExamScore());

        if (!StringUtils.isEmpty(request.getUserId()))
            examScoreDO.setUserId(request.getUserId());

        if (!StringUtils.isEmpty(request.getStuNo()))
            examScoreDO.setStuNo(request.getStuNo());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("examScoreId")
                .withMatcher("examDetailId" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("userId" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("stuNo" ,ExampleMatcher.GenericPropertyMatchers.startsWith());

        Example<ExamScoreDO> example = Example.of(examScoreDO,matcher);

        return examScoreRepo.findAll(example, PageRequest.of(pageNum,pageSize)).stream().map(ExamScoreDO::ToExamScoreBO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamScoreBO> findByExamDetailId(String examDetailId) {
        if (examScoreRepo.existsByExamDetailId(examDetailId)) {
            List<ExamScoreDO> examScoreList = examScoreRepo.findByExamDetailId(examDetailId);
            return examScoreList.stream().map(ExamScoreDO::ToExamScoreBO).collect(Collectors.toList());
    }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamScoreBO findByExamScoreId(String examScoreId) {
        Optional<ExamScoreDO> optional = examScoreRepo.findByExamScoreId(examScoreId);
        return optional.map(ExamScoreDO::ToExamScoreBO).orElse(null);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamScoreBO> findByUserId(String userId) {
        if (examScoreRepo.existsByUserId(userId)) {
            List<ExamScoreDO> examScoreList = examScoreRepo.findByUserId(userId);
            return examScoreList.stream().map(ExamScoreDO::ToExamScoreBO).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExamDetailId(String examDetailId) {
       if (!examScoreRepo.existsByExamDetailId(examDetailId))
        throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到删除信息");

        examScoreRepo.deleteByExamDetailId(examDetailId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExamScoreId(String examScoreId) {
        if (!examScoreRepo.existsByExamScoreId(examScoreId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到删除信息");

            examScoreRepo.deleteByExamScoreId(examScoreId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserId(String userId) {
        if (!examScoreRepo.existsByUserId(userId))
        throw new CustomizeException(CommonResultCode.UNFOUNDED,"未找到删除信息");
        examScoreRepo.deleteByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamScoreBO createExamScore(ExamScoreRequest request) {

        checkExamScoreInfo(request);

        if (examScoreRepo.existsByUserIdAndExamDetailId(request.getUserId(),request.getExamDetailId())){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"该用户该次考试成绩已被录入");
        }

        ExamScoreDO examScoreDO = new ExamScoreDO();
        BeanUtils.copyProperties(request,examScoreDO);
        examScoreDO.setExamScoreId(bizIdFactory.getExamScoreId());
        examScoreRepo.save(examScoreDO);
        return examScoreDO.ToExamScoreBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamScoreBO updateExamScore(ExamScoreRequest request) {

        checkExamScoreInfo(request);

        if (!examScoreRepo.existsByExamScoreId(request.getExamScoreId()))
            return null;
        ExamScoreDO examScoreDO = examScoreRepo.findByExamScoreId(request.getExamScoreId()).get();

        if (!StringUtils.isEmpty(request.getExamDetailId()))
            examScoreDO.setExamDetailId(request.getExamDetailId());

        if (!StringUtils.isEmpty(request.getExamScore()))
            examScoreDO.setExamScore(request.getExamScore());

        if (!StringUtils.isEmpty(request.getUserId()))
            examScoreDO.setUserId(request.getUserId());

        if (!StringUtils.isEmpty(request.getStuNo()))
            examScoreDO.setStuNo(request.getStuNo());

        examScoreRepo.save(examScoreDO);

        return examScoreDO.ToExamScoreBO();

    }

    private void checkExamScoreInfo(ExamScoreRequest request){

        if (!userRepo.existsByUserId(request.getUserId())) {
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"找不到该用户");
        }
        if (!examDetailRepo.existsByExamDetailId(request.getExamDetailId())) {
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"找不到该次考试");
        }

        Optional<UserInfoDO> optional = userInfoRepo.findByUserId(request.getUserId());
        if (!optional.isPresent() ||  !optional.get().getStuNo().equals(request.getStuNo())){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"该账户学号信息未录入或者信息错误");
        }

    }
}
