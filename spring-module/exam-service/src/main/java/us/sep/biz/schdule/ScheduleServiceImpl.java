package us.sep.biz.schdule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.common.util.RedisUtil;
import us.sep.exam.entity.*;
import us.sep.exam.repo.*;
import us.sep.util.utils.CollectionUtils;
import us.sep.util.utils.DateUtil;
import us.sep.util.utils.LoggerUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static us.sep.biz.common.config.RedisConfig.*;

/**
  * 定时任务实现
  * @Author kana-cr
  * @Date  2020/10/10 20:54
  **/
@Component
public class ScheduleServiceImpl implements  ScheduleService {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Resource
    private BizIdFactory bizIdFactory;

    @Resource
    private ExamDetailRepo examDetailRepo;

    @Resource
    private ExamRecordRepo examRecordRepo;

    @Resource
    private ExamEntryRepo examEntryRepo;

    @Resource
    private ExamEntryRecordRepo examEntryRecordRepo;

    @Resource
    private UserExamEntryRecordRepo userExamEntryRecordRepo;

    @Resource
    private UserExamEntryRepo userExamEntryRepo;

    @Resource
    private RedisUtil redisUtil;


    @Override
    @Scheduled(cron = ScheduleConstant.AM_TWO_OF_THE_CLOCK)
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void finishExamDetail() {
        Date now = new Date();
        //过滤没结束的单类考试数据
        List<ExamDetailDO> examDetailDOList = examDetailRepo.findAll().stream().filter(examDetailDO -> DateUtil.parse(examDetailDO.getExamEndTime(), "yyyy年MM月dd日 HH:mm")
                .before(now)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(examDetailDOList)) {
            for (ExamDetailDO examDetailDO : examDetailDOList) {
                //todo del redis cache
                examDetailRepo.delete(examDetailDO);
                redisUtil.hDelete(EXAM_DETAIL, EXAM_DETAIL_ID + examDetailDO.getExamDetailId());
                redisUtil.zRemove(EXAM_DETAIL_PAGE, EXAM_DETAIL_ID + examDetailDO.getExamDetailId());
                ExamRecordDO examRecordDO = new ExamRecordDO();
                BeanUtils.copyProperties(examDetailDO, examRecordDO);
                examRecordDO.setExamRecordId(bizIdFactory.getExamRecordId());
                examRecordRepo.save(examRecordDO);
            }
            LoggerUtil.info(LOGGER, "每日考试归档 examDetails={0}", examDetailDOList);
            //help GC
            examDetailDOList = null;
        }
    }

    @Override
    @Scheduled(cron = ScheduleConstant.AM_TWO_OF_THE_CLOCK)
    @Transactional(rollbackFor = Exception.class)
    public void finishExamEntries() {


        //将所有完成或者取消的活动归档
        List<ExamEntryDO> examEntryFinish = examEntryRepo.findByState("FINISH");
        List<ExamEntryDO> examEntryCancel = examEntryRepo.findByState("CANCEL");

        List<ExamEntryDO> dealExamEntryList = new ArrayList<>(examEntryFinish);

        dealExamEntryList.addAll(examEntryCancel);

        //删除完成的报名
        examEntryRepo.deleteInBatch(dealExamEntryList);

        for (ExamEntryDO examEntryDO:dealExamEntryList) {
            ExamEntryRecordDO examEntryRecord = new ExamEntryRecordDO();
            BeanUtils.copyProperties(examEntryDO,examEntryRecord);
            examEntryRecord.setExamEntryRecordId(bizIdFactory.getExamEntryRecordId());
            examEntryRecordRepo.save(examEntryRecord);
            //help GC
            examEntryRecord = null;
        }

        List<String> examEntryIds = dealExamEntryList.stream().map(ExamEntryDO::getExamEntryId).collect(Collectors.toList());
        //help GC
        dealExamEntryList = null;

        List<UserExamEntryDO> userExamEntrys = new ArrayList<>();
        for (String examEntryId:examEntryIds) {
            userExamEntrys.addAll(userExamEntryRepo.findByExamEntryId(examEntryId));
        }

        //help GC
        examEntryIds = null;

        for (UserExamEntryDO userExamEntryDO:userExamEntrys) {
            UserExamEntryRecordDO userExamEntryRecord = new UserExamEntryRecordDO();
            BeanUtils.copyProperties(userExamEntryDO,userExamEntryRecord);
            userExamEntryRecord.setUserExamEntryRecordId(bizIdFactory.getUserExamEntryRecord());
            userExamEntryRecordRepo.save(userExamEntryRecord);

            //help GC
            userExamEntryDO = null;
        }
        userExamEntryRepo.deleteInBatch(userExamEntrys);

        //help GC
        userExamEntrys = null;


    }

}
