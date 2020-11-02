package us.sep.biz.exam.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.request.ExamLocationRequest;
import us.sep.biz.exam.service.ExamLocationService;
import us.sep.exam.builder.ExamLocationBO;
import us.sep.exam.entity.ExamLocationDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamLocationRepo;
import us.sep.exam.repo.UserExamEntryRepo;
import us.sep.user.repo.UserInfoRepo;
import us.sep.user.repo.UserRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamLocationServiceImpl implements ExamLocationService {

    @Resource
    private ExamLocationRepo examLocationRepo;

    @Resource
    private ExamDetailRepo examDetailRepo;

    @Resource
    private UserExamEntryRepo userExamEntryRepo;

    @Resource
    private UserRepo userRepo;

    @Resource
    private UserInfoRepo userInfoRepo;

    @Resource
    private BizIdFactory bizIdFactory;

    @Override
    public List<ExamLocationBO> findExamLocation(ExamLocationRequest request, int pageNum, int pageSize) {

        if (!StringUtils.isEmpty(request.getExamLocationId()) && examLocationRepo.existsByExamLocationId(request.getExamLocationId())){
            List<ExamLocationBO> locationBOS = new ArrayList<>();
            locationBOS.add(examLocationRepo.findByExamLocationId(request.getExamLocationId()).get().ToExamLocationBO());
            return locationBOS;
        }

        ExamLocationDO examLocationDO = new ExamLocationDO();

        if (!StringUtils.isEmpty(request.getExamDetailId()))
            examLocationDO.setExamDetailId(request.getExamDetailId());

        if (!StringUtils.isEmpty(request.getUserExamEntryId()))
            examLocationDO.setUserExamEntryId(request.getUserExamEntryId());

        if (!StringUtils.isEmpty(request.getUserId()))
            examLocationDO.setUserId(request.getUserId());

        if (!StringUtils.isEmpty(request.getLocation()))
            examLocationDO.setLocation(request.getLocation());

        if (!StringUtils.isEmpty(request.getTeacher()))
            examLocationDO.setTeacher(request.getTeacher());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("examLocationId")
                .withMatcher("examDetailId" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("userExamEntryId" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("userId" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("location" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("teacher",ExampleMatcher.GenericPropertyMatchers.startsWith());
        Example<ExamLocationDO> example = Example.of(examLocationDO,matcher);

        return examLocationRepo.findAll(example, PageRequest.of(pageNum,pageSize)).stream()
                .map(ExamLocationDO::ToExamLocationBO).collect(Collectors.toList());
    }

    @Override
    public List<ExamLocationBO> findByExamDetailId(ExamLocationRequest request) {
        if (!examDetailRepo.existsByExamDetailId(request.getExamDetailId()) )
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该次考试");
        if (!examLocationRepo.existsByExamDetailId(request.getExamDetailId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"该次考试尚未录入座位");

        return examLocationRepo.findByExamDetailId(request.getExamDetailId()).stream()
                .map(ExamLocationDO::ToExamLocationBO).collect(Collectors.toList());
    }

    @Override
    public ExamLocationBO findByUserExamEntryId(ExamLocationRequest request) {
        if (!userExamEntryRepo.existsByUserExamEntryId(request.getUserExamEntryId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户报名id");
        return examLocationRepo.findByUserExamEntryId(request.getUserExamEntryId()).get().ToExamLocationBO();
    }

    @Override
    public ExamLocationBO findByExamLocationId(ExamLocationRequest request) {
        if (!examLocationRepo.existsByExamLocationId(request.getExamLocationId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该考试座位号id");
        return examLocationRepo.findByExamLocationId(request.getExamLocationId()).get().ToExamLocationBO();
    }

    @Override
    public List<ExamLocationBO> findByUserId(ExamLocationRequest request) {
        if (!userRepo.existsByUserId(request.getUserId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户");
        if (!examLocationRepo.existsByUserId(request.getUserId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"尚未录入该用户座位");

        return examLocationRepo.findByUserId(request.getUserId()).stream()
                .map(ExamLocationDO::ToExamLocationBO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamLocationBO createExamLocation(ExamLocationRequest request) {

        if (!userExamEntryRepo.existsByUserExamEntryId(request.getUserExamEntryId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户报名id");

        if (!examDetailRepo.existsByExamDetailId(request.getExamDetailId()) )
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该次考试");

        if (!userRepo.existsByUserId(request.getUserId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户");

        if (!userInfoRepo.existsByUserId(request.getUserId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"该用户未录入学生信息");

        ExamLocationDO examLocationDO = new ExamLocationDO();
        BeanUtils.copyProperties(request,examLocationDO);
        examLocationDO.setExamLocationId(bizIdFactory.getExamLocationId());

        examLocationRepo.save(examLocationDO);
        return examLocationDO.ToExamLocationBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamLocationBO modifyExamLocation(ExamLocationRequest request) {
        if (!examLocationRepo.existsByExamLocationId(request.getExamLocationId()))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该考试座位号id");

        ExamLocationDO examLocationDO = examLocationRepo.findByExamLocationId(request.getExamLocationId()).get();

        if (!StringUtils.isEmpty(request.getLocation()))
            examLocationDO.setLocation(request.getLocation());

        if (!StringUtils.isEmpty(request.getTeacher()))
            examLocationDO.setTeacher(request.getTeacher());

        examLocationRepo.save(examLocationDO);
        return examLocationDO.ToExamLocationBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamLocationBO deleteByUserExamEntryId(String userExamEntryId) {
        if (!userExamEntryRepo.existsByUserExamEntryId(userExamEntryId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户报名id");

        ExamLocationBO examLocationBO = examLocationRepo.findByUserExamEntryId(userExamEntryId).get().ToExamLocationBO();
        examLocationRepo.deleteByUserExamEntryId(userExamEntryId);
        return examLocationBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamLocationBO deleteByExamLocationId(String examLocationId) {
        if (!examLocationRepo.existsByExamLocationId(examLocationId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该考试座位号id");

        ExamLocationBO examLocationBO = examLocationRepo.findByExamLocationId(examLocationId).get().ToExamLocationBO();
        examLocationRepo.deleteByExamLocationId(examLocationId);
        return examLocationBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamLocationBO> deleteByExamDetailId(String examDetailId) {

        if (!examDetailRepo.existsByExamDetailId(examDetailId) )
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该次考试");
        if (!examLocationRepo.existsByExamDetailId(examDetailId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"该次考试尚未录入座位");
        List<ExamLocationBO> examLocationList = examLocationRepo.findByExamDetailId(examDetailId)
                .stream().map(ExamLocationDO::ToExamLocationBO).collect(Collectors.toList());;
        examLocationRepo.deleteByExamDetailId(examDetailId);
        return examLocationList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamLocationBO> deleteByUserId(String userId) {
        if (!userRepo.existsByUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户");
        if (!examLocationRepo.existsByUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"尚未录入该用户座位");
        List<ExamLocationBO> examLocationList = examLocationRepo.findByUserId(userId)
                .stream().map(ExamLocationDO::ToExamLocationBO).collect(Collectors.toList());;
        examLocationRepo.deleteByUserId(userId);
        return examLocationList;
    }
}
