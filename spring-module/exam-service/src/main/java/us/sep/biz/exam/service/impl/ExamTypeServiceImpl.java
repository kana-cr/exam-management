package us.sep.biz.exam.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.common.util.ObjectUtil;
import us.sep.biz.common.util.RedisUtil;
import us.sep.biz.exam.CheckExamTypeUtil;
import us.sep.biz.exam.request.ExamTypeRequest;
import us.sep.biz.exam.service.ExamTypeService;
import us.sep.exam.builder.ExamTypeBO;
import us.sep.exam.entity.ExamTypeDO;
import us.sep.exam.repo.ExamTypeRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static us.sep.biz.common.config.RedisConfig.*;
@Service
@Slf4j
public class ExamTypeServiceImpl implements ExamTypeService {

    @Resource
    private  ExamTypeRepo examTypeRepo;

    @Resource
    private CheckExamTypeUtil checkExamTypeUtil;

    @Resource
    private BizIdFactory bizIdFactory;

    @Resource
    private RedisUtil redisUtil;


    private static final long SLEEP_TIME = 200;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamTypeBO save(ExamTypeRequest request) {

        //不存在该类型 才能增加
       if (!checkExamTypeUtil.checkExamType(request.getExamTypeName())){
           ExamTypeDO examTypeDO = new ExamTypeDO();
           BeanUtils.copyProperties(request,examTypeDO);
           examTypeDO.setExamTypeId(bizIdFactory.getExamTypeId());
            examTypeDO = examTypeRepo.save(examTypeDO);
           //放入cache
           redisUtil.hPut(EXAM_TYPE,EXAM_TYPE_ID + examTypeDO.getExamTypeId() , JSON.toJSONString(examTypeDO));
           redisUtil.zAdd(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examTypeDO.getExamTypeId(),examTypeDO.getId());
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
            //先删除db 这样不存在就不会去查询
            examTypeRepo.deleteByExamTypeName(request.getExamTypeName());
            //删除缓存
            redisUtil.hDelete(EXAM_TYPE,  EXAM_TYPE_ID + examTypeRepo.findByExamTypeName(request.getExamTypeName()).get().getExamTypeId());
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

        //删除缓存
        redisUtil.hDelete(EXAM_TYPE,  EXAM_TYPE_ID + examTypeId);
        redisUtil.zRemove(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examTypeId);

    }

    @Override
    public List<ExamTypeBO> find(ExamTypeRequest request, int pageNum, int pageSize) {

        //只需分页查询
        if (ObjectUtil.checkObjAllFieldsIsNull(request)) {
            if (redisUtil.hasKey(EXAM_TYPE) && redisUtil.hasKey(EXAM_TYPE_PAGE)) {
                //取自增主键id分页区间
                Set<String> examTypeIds = redisUtil.zRange(EXAM_TYPE_PAGE, (pageNum * pageSize) , (pageNum + 1) * pageSize);
                List<Object> serializeList = redisUtil.hMultiGet(EXAM_TYPE, new ArrayList<>(examTypeIds));
                List<ExamTypeBO> examTypes = new ArrayList<>();
                for (Object obj : serializeList) {
                    examTypes.add(JSON.parseObject((String) obj, ExamTypeBO.class));
                }
                //help gc
                examTypeIds = null;
                serializeList = null;
                return examTypes;
            }else {
                List<ExamTypeDO> examTypes =  examTypeRepo.findAll();

                for (ExamTypeDO examType:examTypes) {
                    redisUtil.zAdd(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examType.getExamTypeId() ,examType.getId());
                    redisUtil.hPut(EXAM_TYPE,EXAM_TYPE_ID + examType.getExamTypeId(), JSON.toJSONString(examType));
                }
                return examTypes.subList( pageNum * pageSize, ( (pageNum + 1) * pageSize) - 1 ).
                        stream().map(ExamTypeDO::ToExamTypeBO).collect(Collectors.toList());
            }
        }

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
    public ExamTypeBO update(ExamTypeRequest request) throws InterruptedException {

     /*   if (!StringUtils.isEmpty(request.getExamTypeName())){
            Optional<ExamTypeDO> examTypeDO = examTypeRepo.findByExamTypeName(request.getExamTypeName());
            if (!examTypeDO.isPresent()) {
                throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "该考试类型不存在");
            }
        }*/

        if (checkExamTypeUtil.ifInitType(request.getExamTypeName())){
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "初始类型信息不能更改");
        }

        if (!examTypeRepo.existsByExamTypeId(request.getExamTypeId()))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "该考试类型id不存在");

        ExamTypeDO examType = examTypeRepo.findByExamTypeId(request.getExamTypeId()).get();

        if (!StringUtils.isEmpty(request.getExamTypeDescription()))
            examType.setExamTypeDescription(request.getExamTypeDescription());

        if (!StringUtils.isEmpty(request.getExamLimit()))
            examType.setExamLimit(request.getExamLimit());

        if (!StringUtils.isEmpty(request.getExamTypeName()))
            examType.setExamTypeName(request.getExamTypeName());


        redisUtil.hDelete(EXAM_TYPE,  EXAM_TYPE_ID + examType.getExamTypeId());
        redisUtil.zRemove(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examType.getExamTypeId());
        examTypeRepo.save(examType);

       /* //休眠200ms
        Thread.sleep(SLEEP_TIME);
        //延时双删
        redisUtil.hDelete(EXAM_TYPE,  EXAM_TYPE_ID + examType.getExamTypeId());
        redisUtil.zRemove(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examType.getExamTypeId());*/

        redisUtil.hPut(EXAM_TYPE,EXAM_TYPE_ID + examType.getExamTypeId() , JSON.toJSONString(examType));
        redisUtil.zAdd(EXAM_TYPE_PAGE,EXAM_TYPE_ID + examType.getExamTypeId(),examType.getId());

        return examType.ToExamTypeBO();
    }

}
