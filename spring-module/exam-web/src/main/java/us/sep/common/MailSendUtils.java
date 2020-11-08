package us.sep.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import us.sep.biz.common.util.RedisUtil;
import us.sep.biz.user.request.EmailRequest;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
@Slf4j
public class MailSendUtils {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private JavaMailSenderImpl javaMailSenderImpl;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 发送者地址
     **/
    private static final String posterAddress = "862336406@qq.com";

    /**
     * 发送者姓名
     **/
    private static final String posterName = "kana";


    /**
     * 文本发送
     **/
    @Async
    public void sendEmailAsText(final EmailRequest request) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            mimeMessage.setFrom(posterAddress);
            mimeMessage.setRecipients(Message.RecipientType.TO, request.getReceiverEmailAddress());
            mimeMessage.setText("<html><body>"
                    + "hello：" + request.getReceiverName()
                    + "<br>" + "this your verifyCode：" + request.getEmailContent()
                    + "<br>" + "from :" + posterName
                    + "</body></html>");
        };
        try {
            this.javaMailSender.send(mimeMessagePreparator);
            log.info("邮箱已返送至[{}]邮箱！", request.getReceiverName());
        } catch (MailException e) {
            log.error("邮箱异常：{}", e);
            redisUtil.delete(String.valueOf(request.getReceiverEmailAddress().hashCode()));
        }
    }

    /**
     * html 网页发送
     **/
    @Async
    public void sendEmailAsSysExceptionHtml(final EmailRequest request) {
        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(posterAddress);
            mimeMessageHelper.setTo(request.getReceiverEmailAddress());
            mimeMessageHelper.setText("<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "\t<head>\n" +
                            "\t\t<meta charset=\"UTF-8\">\n" +
                            "\t\t<title></title>\n" +
                            "\t</head>\n" +
                            "\t<style>\n" +
                            "\t\tbody,\n" +
                            "\t\ttable,\n" +
                            "\t\ttbody,\n" +
                            "\t\ttr {\n" +
                            "\t\t\tbackground-color: aquamarine;\n" +
                            "\t\t\tbackground-size: 100%;\n" +
                            "\t\t}\n" +
                            "\t</style>\n" +
                            "\n" +
                            "\t<body>\n" +
                            "\t\t<table border=\"solid 2 px\" align=\"center\" style=\"text-align: center;\">\n" +
                            "\t\t\t<tbody>\n" +
                            "\t\t\t\t<tr>\n" +
                            "\t\t\t\t\t<td width=\"200px\" bgcolor=\"coral\">时间</td>\n" +
                            "\t\t\t\t\t<td width=\"80%\" bgcolor=\"azure\">" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</td>\n" +
                            "\t\t\t\t</tr>\n" +
                            "\t\t\t\t<tr>\n" +
                            "\t\t\t\t\t<td width=\"200px\" bgcolor=\"coral\">信息</td>\n" +
                            "\t\t\t\t\t<td width=\"80%\" bgcolor=\"azure\">" + "测试" + "</td>\n" +
                            "\t\t\t\t</tr>\n" +
                            "\t\t\t\t<tr>\n" +
                            "\t\t\t\t\t<td width=\"200px\" bgcolor=\"coral\">堆栈</td>\n" +
                            "\t\t\t\t\t<td width=\"80%\" bgcolor=\"azure\" style=\"text-align: left;\">" + request.getEmailContent() + "</td>\n" +
                            "\t\t\t\t</tr>\n" +
                            "\t\t\t</tbody>\n" +
                            "\t\t</table>\n" +
                            "\t</body>\n" +
                            "\n" +
                            "</html>"
                    , true);

            this.javaMailSender.send(mimeMessage);
            log.info("邮箱已返送至[{}]邮箱！", request.getReceiverName());

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (MailException e) {
            log.error("邮箱异常：{}", e);
        }
    }
    public  String VerifyCode(int n){
        Random r = new Random();
        StringBuffer sb =new StringBuffer();
        for(int i = 0;i < n;i ++){
            int ran1 = r.nextInt(10);
            sb.append(ran1);
        }
        r = null;
        return sb.toString();
    }
}
