package us.sep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.exam.entity.ExamDetailDO;
import us.sep.exam.entity.ExamRecordDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamRecordRepo;
import us.sep.util.utils.CollectionUtils;
import us.sep.util.utils.DateUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamRecordTest {

    @Resource
    ExamDetailRepo examDetailRepo;
    @Resource
    ExamRecordRepo examRecordRepo;
    @Resource
    private BizIdFactory bizIdFactory;

    //@Transactional(rollbackFor = Exception.class)
    @Test
    public void finishExamDetail() {
        Date now = new Date();
        //过滤没结束的单类考试数据
        List<ExamDetailDO> examDetailDOList = examDetailRepo.findAll().stream().filter(examDetailDO -> DateUtil.parse(examDetailDO.getExamEndTime(), "yyyy-MM-dd HH:mm")
                .before(now)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(examDetailDOList)) {
            for (ExamDetailDO examDetailDO : examDetailDOList) {
                examDetailRepo.delete(examDetailDO);
                ExamRecordDO examRecordDO = new ExamRecordDO();
                BeanUtils.copyProperties(examDetailDO, examRecordDO);
                examRecordDO.setExamRecordId(bizIdFactory.getExamRecordId());
                examRecordRepo.save(examRecordDO);
            }
            System.out.println(examDetailDOList);
            //GC
            examDetailDOList = null;
        }
    }
}
