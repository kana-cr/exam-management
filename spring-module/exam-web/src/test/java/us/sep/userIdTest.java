package us.sep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.user.service.UserInfoService;
import us.sep.user.entity.User;
import us.sep.user.repo.UserRepo;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
    public class userIdTest {

    @Resource
    BizIdFactory bizIdFactory;

    @Resource
    UserInfoService userInfoService;

    @Resource
    UserRepo userRepo;
    @Test
    public void test(){
        for (int i = 0; i < 4; i++) {
            System.out.println(bizIdFactory.getUserId());
        }
    }

    @Test
    public void testUserInfo(){
        System.out.println( userInfoService.findByName("hujiayue"));
    }

    @Test
    public void  testUpdateFail(){
        User user = userRepo.findByUserName("huang").get();
        user.setFullName("huangxv");
        System.out.println(user.toString());
        userRepo.save(user);
    }
}
