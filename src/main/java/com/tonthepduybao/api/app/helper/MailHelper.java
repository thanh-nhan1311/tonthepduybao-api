package com.tonthepduybao.api.app.helper;

import jakarta.mail.MessagingException;

import java.util.Map;

/**
 * MailHelper
 *
 * @author khal
 * @since 2022/06/09
 */
public interface MailHelper {

    void send(String from, String to, String subject, Map<String, Object> properties, String template) throws MessagingException;

}
