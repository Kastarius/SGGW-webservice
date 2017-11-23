package pl.sggw.support.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by Kamil on 2017-11-23.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void sendEmail(String to, String title, String content, boolean isHtml){
        MimeMessage mail = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo("noreply@sggw.support.pl");
            helper.setFrom("sggw.support@o2.pl");
            helper.setSubject(title);
            helper.setText(content, isHtml);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        this.mailSender.send(mail);
    }

    public void sendTestEmail(String to, String title){
        sendEmail(to,title,processTemplate(),true);
    }


    private String processTemplate(){ //TODO create method to process any template and any context
        String body = "";
        Context context = new Context();
        context.setVariable("header", "Test Email");
        context.setVariable("title", "Hello");
        context.setVariable("description", "This is a test email");

        body = templateEngine.process("basicTemplate", context); //TODO find way to store templates on db

        return body;
    }
}
