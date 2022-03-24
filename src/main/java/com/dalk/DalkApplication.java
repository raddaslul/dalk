package com.dalk;

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

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
//            + "/home/ubuntu/dalk/build/libs/application.yml";
            + "classpath:application.yml";  //

    public static void main(String[] args) {

        new SpringApplicationBuilder(DalkApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
