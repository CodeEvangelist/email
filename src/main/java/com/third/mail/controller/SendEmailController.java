package com.third.mail.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @author bin.yin
 * @version 1.0
 * @createTime 2019/7/14 12:43
 * @since 1.0
 */


@RestController
@Slf4j
@RequestMapping(value = "/api/email",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SendEmailController {

    @Value("${proxy_email_count}")
    private  String EMAIL_ACCOUNT;

    @Value("${proxy_email_password}")
    private  String EMAIL_PASSWORD;

    @Value("${proxy_eamil_host}")
    private   String EMAIL_SERVICE_HOST;

    @Value("${recevi_account}")
    private  String EMAIL_RECIVER_ACCOUNT;

    @Value("${email_protocol_key}")
    private String EMAIL_PROTOCOL_KEY;

    @Value("${email_protocol}")
    private String EMAIL_PROTOCOL;

    @Value("${email_host_key}")
    private String EMAIL_HOST_KEY;

    @Value("${email_auth_key}")
    private String EMAIL_AUTH_KEY;

    @Value("${email_auth}")
    private String EAMIL_AUTH;



    @PostMapping(value = "send")
    public void sendEmail() {
        Transport transport = null;
        try {
            // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
            Properties props = new Properties();
            props.setProperty(EMAIL_PROTOCOL_KEY, EMAIL_PROTOCOL);
            props.setProperty(EMAIL_HOST_KEY, EMAIL_SERVICE_HOST);
            props.setProperty(EMAIL_AUTH_KEY, EAMIL_AUTH);
            //
            Session session = Session.getInstance(props);        // 根据参数配置，创建会话对象（为了发送邮件准备的）
            //开启后可以查看详细的发送log
            session.setDebug(true);
            MimeMessage message = createMessage(session, EMAIL_ACCOUNT, EMAIL_RECIVER_ACCOUNT);     // 创建邮件对象
            // 4. 根据 Session 获取邮件传输对象
            transport = session.getTransport();
            transport.connect(EMAIL_ACCOUNT, EMAIL_PASSWORD);

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

            // 7. 关闭连接
            transport.close();
        }catch (Exception e){
            log.error("SendEmailController sendEmail error msg:{}",e.getMessage(),e);
        }finally {
            if(transport != null){
                try {
                    transport.close();
                } catch (MessagingException e) {
                    log.error("SendEmailController sendMail transport close error:{}",e.getMessage(),e);
                }
            }
        }
    }

    private MimeMessage createMessage(Session session,String account,String recerviAccount) throws UnsupportedEncodingException, MessagingException {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(account, "昵称", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recerviAccount, "XX用户", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("测试邮件发送服务器", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent("测试邮件发送服务器", "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

}
