package com.dalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableSpringDataWebSupport
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class DalkApplication {

//    public static final String APPLICATION_LOCATIONS = "spring.config.location="
//            + "classpath:application.yml,";
////            + "classpath:aws.yml";
//
//    public static void main(String[] args) {
//
//        new SpringApplicationBuilder(DalkApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .run(args);
//    }
public static void main(String[] args) {
    SpringApplication.run(DalkApplication.class, args);
}
}
