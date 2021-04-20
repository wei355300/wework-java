package com.qc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//@EnableWebSecurity
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class WxCpChatDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxCpChatDataApplication.class, args);
    }

}
