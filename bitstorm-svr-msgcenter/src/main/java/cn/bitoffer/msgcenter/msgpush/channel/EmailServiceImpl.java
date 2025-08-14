package cn.bitoffer.msgcenter.msgpush.channel;

import cn.bitoffer.msgcenter.common.conf.SendMsgConf;
import cn.bitoffer.msgcenter.exception.BusinessException;
import cn.bitoffer.msgcenter.exception.ErrorCode;
import cn.bitoffer.msgcenter.msgpush.MsgPushService;
import cn.bitoffer.msgcenter.msgpush.base.ChannelMsgBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
public class EmailServiceImpl implements MsgPushService {

    @Autowired
    SendMsgConf sendMsgConf;

    @Override
    public void pushMsg(ChannelMsgBase msgBase) {
        log.info("发送 Email !!!!! content:"+msgBase.getContent());

        // SMTP服务器配置
        String host = sendMsgConf.getEmailHost(); // SMTP服务器地址
        String port = sendMsgConf.getEmailPort(); // SMTP端口（通常为587或465）
        String username = sendMsgConf.getEmailAccount(); // 发件人邮箱
        String password = sendMsgConf.getEmailAuthCode(); // 发件人邮箱密码或授权码

        // 收件人信息
        String recipient = msgBase.getTo(); // 收件人邮箱

        // 邮件内容
        String subject = msgBase.getSubject();
        String body = msgBase.getContent();

        // 设置邮件服务器的配置
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // 使用TLS加密

        // 创建会话对象
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 创建邮件对象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // 设置发件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient)); // 设置收件人
            message.setSubject(subject); // 设置邮件主题
            message.setText(body); // 设置邮件正文

            // 发送邮件
            Transport.send(message);
            log.info("邮件发送成功！");
        } catch (MessagingException e) {
            e.printStackTrace();
            log.info("邮件发送失败！");
            throw new BusinessException(ErrorCode.PUSH_MSG_ERROR," email push msg error");
        }
    }
}
