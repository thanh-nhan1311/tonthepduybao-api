package com.tonthepduybao.api.app.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.tonthepduybao.api.app.constant.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AWSConfig
 *
 * @author khale
 * @since 2022/11/01
 */
@Configuration
@RequiredArgsConstructor
public class AWSConfig {

    private final Constant constant;

    /**
     * AWS Credentials Configuration
     *
     * @return AWSStaticCredentialsProvider
     */
    private AWSStaticCredentialsProvider getR2Credentials() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(constant.getR2AccessKey(), constant.getR2SecretKey());
        return new AWSStaticCredentialsProvider(credentials);
    }

    /**
     * R2 Compatibility with AWS S3 Client Configuration
     *
     * @return AmazonS3
     */
    @Bean
    public AmazonS3 s3CloudFlare() {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(constant.getR2Endpoint(), "auto");
        return AmazonS3ClientBuilder.standard()
                .withCredentials(getR2Credentials())
                .withEndpointConfiguration(endpoint)
                .build();
    }

}
