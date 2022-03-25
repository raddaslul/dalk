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
//            무중단 배포일때 이것만 써야함.
            + "/home/ubuntu/dalk/build/libs/application.yml";

            //이게 로컬이고
//            + "classpath:application.yml";

    public static void main(String[] args) {

        new SpringApplicationBuilder(DalkApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
