package us.sep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.sep.biz.user.request.EmailRequest;
import us.sep.common.MailSendUtils;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTest {
    @Resource
    MailSendUtils mailSendUtils;

    @Test
    public void sendMail(){
        EmailRequest request = new EmailRequest();
        request.setEmailTheme("给傻子的邮件");
        request.setEmailContent("你是不是傻的呀");
        request.setReceiverName("傻子方");
        request.setReceiverEmailAddress("190839936@qq.com");
        for (int i = 0; i < 15 ; i++) {
            mailSendUtils.sendEmailAsSysExceptionHtml(request);
        }

    }

    @Test
    public void sendMail2(){
        EmailRequest request = new EmailRequest();
        request.setEmailTheme("this is a test theme");
        request.setEmailContent("别玩手机了");
        request.setReceiverName("黄旭");
        request.setReceiverEmailAddress("602646397@qq.com");
        mailSendUtils.sendEmailAsText(request);
    }

}
