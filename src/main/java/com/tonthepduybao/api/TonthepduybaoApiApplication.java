package com.tonthepduybao.api;

import com.tonthepduybao.api.security.model.RsaKeyProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@MapperScan("com.tonthepduybao.api.mapper")
public class TonthepduybaoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TonthepduybaoApiApplication.class, args);
    }

}
