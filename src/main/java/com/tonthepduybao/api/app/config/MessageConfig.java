package com.tonthepduybao.api.app.config;

import com.tonthepduybao.api.app.constant.Constant;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * MessageConfig
 *
 * @author khal
 * @since 2023/02/11
 */
@Configuration
public class MessageConfig {

    private final Constant constant;

    public MessageConfig(final Constant constant) {
        this.constant = constant;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale(constant.getLocale()));
        return messageSource;
    }

}
