package com.tonthepduybao.api.app.helper;

import com.tonthepduybao.api.app.constant.Constant;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * MailHelperImpl
 *
 * @author khal
 * @since 2022/06/09
 */
@Component
@RequiredArgsConstructor
public class MailHelperImpl implements MailHelper {

    private final Constant constant;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void send(final String from, final String to, final String subject, final Map<String, Object> properties, final String template) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(constant.getMailSender())});

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setFrom(new InternetAddress(from));
        helper.setTo(to);
        helper.setSubject(subject);

        Context context = new Context();
        context.setVariables(properties);
        helper.setText(templateEngine.process(template, context), true);

        mailSender.send(message);
    }

}
