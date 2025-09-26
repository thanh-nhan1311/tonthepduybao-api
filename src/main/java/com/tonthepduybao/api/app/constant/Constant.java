package com.tonthepduybao.api.app.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Constant
 *
 * @author KhaL
 * @since 2022/10/04
 */
@Getter
@Component
@PropertySource(value = "classpath:constant.properties", encoding = "utf-8")
public class Constant {

    @Value("${app.locale}")
    private String locale;

    @Value("${mail.sender}")
    private String mailSender;

    @Value("${cf.r2.bucket.name}")
    private String r2BucketName;

    @Value("${cf.r2.bucket.url}")
    private String r2BucketUrl;

    @Value("${cf.r2.api_token}")
    private String r2ApiToken;

    @Value("${cf.r2.access_key}")
    private String r2AccessKey;

    @Value("${cf.r2.secret_key}")
    private String r2SecretKey;

    @Value("${cf.r2.endpoint}")
    private String r2Endpoint;

}
