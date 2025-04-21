package com.fgh.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class KnowledgeSharingForumServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeSharingForumServerApplication.class, args);
    }

}
