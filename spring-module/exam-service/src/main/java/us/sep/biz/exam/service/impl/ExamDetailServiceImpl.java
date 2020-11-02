package us.sep.biz.exam.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.request.ExamDetailRequest;
import us.sep.biz.exam.common.CheckDateUtil;
import us.sep.biz.exam.service.ExamDetailService;
import us.sep.exam.builder.ExamDetailBO;
import us.sep.exam.entity.ExamDetailDO;
import us.sep.exam.repo.ExamDetailRepo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
  * @author kana-cr
  * @version  2020/10/15 9:38
  */
@Service
@Slf4j
public class ExamDetailServiceImpl implements ExamDetailService {

    @Resource
    private  ExamDetailRepo examDetailRepo;

    @Resource
    private BizIdFactory bizIdFactory;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExamDetailBO save(ExamDetailRequest request) {
        //校验日期格式，不合法抛出异常
        CheckDateUtil.checkDateFormat(request.getExamStartTime(),request.getExamEndTime());

        ExamDetailDO examDetailDO = new ExamDetailDO();
        BeanUtils.copyProperties(request,examDetailDO);
        examDetailDO.setExamDetailId(bizIdFactory.getExamTypeDetailId());
        examDetailRepo.save(examDetailDO);
        return examDetailDO.ToExamDetailBO();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ExamDetailBO> find(ExamDetailRequest request, int pageNum, int pageSize) {

        ExamDetailDO examDetailDO = new ExamDetailDO();

        if (!StringUtils.isEmpty(request.getExamDetailId())) {
            Optional<ExamDetailDO> optional =  examDetailRepo.findByExamDetailId(request.getExamDetailId());
            if (optional.isPresent()){
                List<ExamDetailBO> list = new ArrayList<>();
                list.add(optional.get().ToExamDetailBO());
                return list;
            }
        }

        if (!StringUtils.isEmpty(request.getExamTypeId()))
            examDetailDO.setExamTypeId(request.getExamTypeId());

        if (!StringUtils.isEmpty(request.getExamStartTime()))
            examDetailDO.setExamStartTime(request.getExamStartTime());

        if (!StringUtils.isEmpty(request.getExamEndTime()))
            examDetailDO.setExamEndTime(request.getExamEndTime());

        if (!StringUtils.isEmpty(request.getExamDescription()))
            examDetailDO.setExamDescription(request.getExamDescription());

        if (!StringUtils.isEmpty(request.getExamLocation()))
            examDetailDO.setExamLocation(request.getExamLocation());

        if (!StringUtils.isEmpty(request.getExamAnnounce()))
            examDetailDO.setExamAnnounce(request.getExamAnnounce());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("examDetailId")
                .withMatcher("examTypeId" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examStartTime" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("examEndTime" ,ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("examDescription" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examLocation" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examAnnounce" ,ExampleMatcher.GenericPropertyMatchers.contains());


        Example<ExamDetailDO> example = Example.of(examDetailDO , matcher);

        return examDetailRepo.findAll(example , PageRequest.of(pageNum,pageSize)).getContent().stream()
                .map(ExamDetailDO::ToExamDetailBO).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ExamDetailRequest request) {
        if (examDetailRepo.existsByExamDetailId(request.getExamDetailId())){
          ExamDetailDO examDetailDO =  examDetailRepo.findByExamDetailId(request.getExamDetailId()).get();

          if (!StringUtils.isEmpty(request.getExamTypeId()))
              examDetailDO.setExamTypeId(request.getExamTypeId());

          if (!StringUtils.isEmpty(request.getExamStartTime()))
              examDetailDO.setExamStartTime(request.getExamStartTime());


          if (!StringUtils.isEmpty(request.getExamEndTime()))
              examDetailDO.setExamEndTime(request.getExamEndTime());

          //校验日期
            CheckDateUtil.checkDateFormat(request.getExamStartTime() , request.getExamEndTime());

          if (!StringUtils.isEmpty(request.getExamDescription()))
              examDetailDO.setExamDescription(request.getExamDescription());

          if (!StringUtils.isEmpty(request.getExamLocation()))
              examDetailDO.setExamLocation(request.getExamLocation());

          if (!StringUtils.isEmpty(request.getExamAnnounce()))
              examDetailDO.setExamAnnounce(request.getExamAnnounce());

          examDetailRepo.save(examDetailDO);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByExamTypeId(String examTypeId) {
        if (examDetailRepo.existsByExamTypeId(examTypeId))
            examDetailRepo.deleteAllByExamTypeId(examTypeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByExamDetailId(String examDetailId) {
        if (examDetailRepo.existsByExamDetailId(examDetailId))
            examDetailRepo.deleteByExamDetailId(examDetailId);
    }




}
