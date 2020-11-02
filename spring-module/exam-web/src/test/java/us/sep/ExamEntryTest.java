package us.sep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.sep.exam.repo.ExamEntryRepo;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamEntryTest {

    @Resource
    ExamEntryRepo examEntryRepo;

    @Test
    public void deleteExamEntry(){
         examEntryRepo.deleteByExamDetailId("202010311019337592289710032020");
        //System.out.println(examEntryDO.toString());
    }
}
