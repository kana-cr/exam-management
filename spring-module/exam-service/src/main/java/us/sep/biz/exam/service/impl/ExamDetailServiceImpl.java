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
import us.sep.biz.common.util.CheckDateUtil;
import us.sep.biz.common.util.ObjectUtil;
import us.sep.biz.common.util.RedisUtil;
import us.sep.biz.exam.request.ExamDetailRequest;
import us.sep.biz.exam.service.ExamDetailService;
import us.sep.exam.builder.ExamDetailBO;
import us.sep.exam.entity.ExamDetailDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static us.sep.biz.common.config.RedisConfig.*;
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

    @Resource
    private RedisUtil redisUtil;

    private static final long SLEEP_TIME = 200;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExamDetailBO save(ExamDetailRequest request) {
        //校验日期格式，不合法抛出异常
        CheckDateUtil.checkDateFormat(request.getExamStartTime(),request.getExamEndTime());

        ExamDetailDO examDetailDO = new ExamDetailDO();
        BeanUtils.copyProperties(request,examDetailDO);
        examDetailDO.setExamDetailId(bizIdFactory.getExamTypeDetailId());
        examDetailDO= examDetailRepo.save(examDetailDO);
        //写入cache
        redisUtil.hPut(EXAM_DETAIL, EXAM_DETAIL_ID + examDetailDO.getExamDetailId() , JSON.toJSONString(examDetailDO));
        redisUtil.zAdd(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetailDO.getExamDetailId(),examDetailDO.getId());

        return examDetailDO.ToExamDetailBO();
    }

    @Override
    public List<ExamDetailBO> find(ExamDetailRequest request, int pageNum, int pageSize) {

        //只需分页查询
        if (ObjectUtil.checkObjAllFieldsIsNull(request)){
            if (redisUtil.hasKey(EXAM_DETAIL) && redisUtil.hasKey(EXAM_DETAIL_PAGE)){
                //取自增主键id分页区间
                Set<String> examDetailIds = redisUtil.zRange(EXAM_DETAIL_PAGE, (pageNum * pageSize) , (pageNum + 1) * pageSize);
                List<Object> serializeList = redisUtil.hMultiGet(EXAM_DETAIL, new ArrayList<>(examDetailIds));
                List<ExamDetailDO> examDetails = new ArrayList<>();
                for (Object obj : serializeList) {
                   examDetails.add(JSON.parseObject((String) obj, ExamDetailDO.class));
                }
                //help gc
                examDetailIds = null;
                serializeList = null;
                return examDetails.stream().map(ExamDetailDO::ToExamDetailBO).collect(Collectors.toList());
            }else {
                List<ExamDetailDO> examDetails =  examDetailRepo.findAll();
                for (ExamDetailDO examDetailDO:examDetails) {
                    //写入cache
                    redisUtil.hPut(EXAM_DETAIL, EXAM_DETAIL_ID + examDetailDO.getExamDetailId() , JSON.toJSONString(examDetailDO));
                    redisUtil.zAdd(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetailDO.getExamDetailId(),examDetailDO.getId());
                }
                return examDetails.subList( pageNum * pageSize, ( (pageNum + 1) * pageSize) - 1 ).
                        stream().map(ExamDetailDO::ToExamDetailBO).collect(Collectors.toList());
            }
        }

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
    public void update(ExamDetailRequest request) throws InterruptedException {

        if (!examDetailRepo.existsByExamDetailId(request.getExamDetailId()))
            throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS , "该考试信息不存在");

          ExamDetailDO examDetailDO =  examDetailRepo.findByExamDetailId(request.getExamDetailId()).get();

          if (!StringUtils.isEmpty(request.getExamTypeId()))
              examDetailDO.setExamTypeId(request.getExamTypeId());

          if (!StringUtils.isEmpty(request.getExamStartTime()))
              examDetailDO.setExamStartTime(request.getExamStartTime());


          if (!StringUtils.isEmpty(request.getExamEndTime()))
              examDetailDO.setExamEndTime(request.getExamEndTime());

          //校验日期
           if (!StringUtils.isEmpty(request.getExamStartTime()) && !StringUtils.isEmpty(request.getExamEndTime()))
               CheckDateUtil.checkDateFormat(request.getExamStartTime() , request.getExamEndTime());

          if (!StringUtils.isEmpty(request.getExamDescription()))
              examDetailDO.setExamDescription(request.getExamDescription());

          if (!StringUtils.isEmpty(request.getExamLocation()))
              examDetailDO.setExamLocation(request.getExamLocation());

          if (!StringUtils.isEmpty(request.getExamAnnounce()))
              examDetailDO.setExamAnnounce(request.getExamAnnounce());


          redisUtil.hDelete(EXAM_DETAIL, EXAM_DETAIL_ID + request.getExamDetailId());
          redisUtil.zRemove(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + request.getExamDetailId());
          examDetailRepo.save(examDetailDO);

        //写入cache
        redisUtil.hPut(EXAM_DETAIL, EXAM_DETAIL_ID + examDetailDO.getExamDetailId() , JSON.toJSONString(examDetailDO));
        redisUtil.zAdd(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetailDO.getExamDetailId(),examDetailDO.getId());

          /*Thread.sleep(SLEEP_TIME);
          redisUtil.hDelete(EXAM_DETAIL,EXAM_DETAIL_ID + request.getExamDetailId());
          redisUtil.zRemove(EXAM_DETAIL_PAGE,EXAM_DETAIL_ID + request.getExamDetailId());*/


    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByExamTypeId(String examTypeId) {

        if (examDetailRepo.existsByExamTypeId(examTypeId)) {
            examDetailRepo.deleteAllByExamTypeId(examTypeId);
            List<ExamDetailDO> list = examDetailRepo.findByExamTypeId(examTypeId);
            for (ExamDetailDO examDetail:list) {
                redisUtil.hDelete(EXAM_DETAIL, EXAM_DETAIL_ID + examDetail.getExamDetailId());
                redisUtil.zRemove(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetail.getExamDetailId());
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByExamDetailId(String examDetailId) {
        if (examDetailRepo.existsByExamDetailId(examDetailId)) {
            examDetailRepo.deleteByExamDetailId(examDetailId);
            redisUtil.hDelete(EXAM_DETAIL, EXAM_DETAIL_ID + examDetailId);
            redisUtil.zRemove(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetailId);
        }
    }




}
