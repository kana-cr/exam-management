package us.sep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.request.ExamLocationRequest;
import us.sep.biz.exam.service.ExamLocationService;
import us.sep.user.entity.User;
import us.sep.user.repo.UserInfoRepo;
import us.sep.user.repo.UserRepo;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
    public class ExamLocationTest {

    @Resource
    BizIdFactory bizIdFactory;

    @Resource
    ExamLocationService examLocationService;

    @Resource
    UserRepo userRepo;

    @Resource
    UserInfoRepo userInfoRepo;

    @Test
    public void test(){
        String examDetailId = "202010181505285583262110032020";
        String teacher = "楼永坚";
        int location = 1;
        List<User> userIds = userRepo.findAll();
        for (User user:userIds) {

            if (!userInfoRepo.existsByUserId(user.getUserId()))
                break;

            ExamLocationRequest request = new ExamLocationRequest();
            request.setExamDetailId(examDetailId);
            request.setLocation(String.valueOf(location));
            request.setTeacher(teacher);
            request.setUserId(user.getUserId());
            examLocationService.createExamLocation(request);
            location++;
        }


    }


}
