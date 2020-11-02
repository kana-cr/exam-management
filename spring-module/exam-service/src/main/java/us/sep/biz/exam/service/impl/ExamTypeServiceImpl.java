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
import us.sep.biz.exam.CheckExamTypeUtil;
import us.sep.biz.exam.request.ExamTypeRequest;
import us.sep.biz.exam.service.ExamTypeService;
import us.sep.exam.builder.ExamTypeBO;
import us.sep.exam.entity.ExamTypeDO;
import us.sep.exam.repo.ExamTypeRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamTypeServiceImpl implements ExamTypeService {

    @Resource
    private  ExamTypeRepo examTypeRepo;

    @Resource
    private CheckExamTypeUtil checkExamTypeUtil;

    @Resource
    private BizIdFactory bizIdFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamTypeBO save(ExamTypeRequest request) {
        //不存在该类型 才能增加
       if (!checkExamTypeUtil.checkExamType(request.getExamTypeName())){
           ExamTypeDO examTypeDO = new ExamTypeDO();
           BeanUtils.copyProperties(request,examTypeDO);
           examTypeDO.setExamTypeId(bizIdFactory.getExamTypeId());
           examTypeRepo.save(examTypeDO);
          return examTypeDO.ToExamTypeBO();
       }
        throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "已经存在该类型");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ExamTypeRequest request) {
        //存在该类型 才能删除

        //不能是初始属性
        if (checkExamTypeUtil.ifInitType(request.getExamTypeName())){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "初始类型不能删除");
    }
        if(checkExamTypeUtil.checkOtherExamType(request.getExamTypeName())){
            examTypeRepo.deleteByExamTypeName(request.getExamTypeName());
           return;
        }
        throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "类型不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String examTypeId) {
        Optional<ExamTypeDO> optional = examTypeRepo.findByExamTypeId(examTypeId);
        if (!optional.isPresent())
            return;
        if (checkExamTypeUtil.ifInitType(optional.get().getExamTypeName())){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "初始类型不能删除");
        }
        if (!examTypeRepo.existsByExamTypeId(examTypeId)){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "该考试类型id不存在");
        }
        examTypeRepo.deleteByExamTypeId(examTypeId);
    }

    @Override
    public List<ExamTypeBO> find(ExamTypeRequest request, int pageNum, int pageSize) {

        ExamTypeDO examTypeDO = new ExamTypeDO();

        if (!StringUtils.isEmpty(request.getExamTypeName())){
            examTypeDO.setExamTypeName(request.getExamTypeName());
        }

        if (!StringUtils.isEmpty(request.getExamLimit())){
            examTypeDO.setExamLimit(request.getExamLimit());
        }

        if (!StringUtils.isEmpty(request.getExamTypeDescription())){
            examTypeDO.setExamTypeDescription(request.getExamTypeDescription());
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("examTypeName" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examLimit" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examTypeDescription" ,ExampleMatcher.GenericPropertyMatchers.contains());

        Example<ExamTypeDO> example = Example.of(examTypeDO , matcher);

        //todo 如有时间分类需要再调用过滤器
        return examTypeRepo.findAll(example, PageRequest.of(pageNum,pageSize))
                .stream().map(ExamTypeDO::ToExamTypeBO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamTypeBO update(ExamTypeRequest request) {

        if (checkExamTypeUtil.ifInitType(request.getExamTypeName())){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "初始类型不能更改");
        }

        Optional<ExamTypeDO> examTypeDO = examTypeRepo.findByExamTypeName(request.getExamTypeName());
        if (!examTypeDO.isPresent()) {
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "该考试类型不存在");
        }
        ExamTypeDO examType = examTypeDO.get();

        if (Objects.nonNull(request.getExamTypeDescription()))
            examType.setExamTypeDescription(request.getExamTypeDescription());

        if (Objects.nonNull(request.getExamLimit()))
            examType.setExamLimit(request.getExamLimit());

        if (Objects.nonNull(request.getExamTypeName()))
            examType.setExamTypeName(request.getExamTypeName());

        examTypeRepo.save(examType);

        return examType.ToExamTypeBO();
    }
}
