package us.sep.biz.user.request;

import lombok.Data;
import us.sep.util.common.ToString;

@Data
public class EmailRequest extends ToString {

    /**
     * 收件人姓名
     **/
    private String receiverName;

    /**
     * 收件人邮箱地址
     **/
    private String receiverEmailAddress;

    /**
     * 邮件主题
     **/
    private String emailTheme;

    /**
     * 邮件内容
     **/
    private String emailContent;
}
