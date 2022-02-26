package com.dalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DalkApplication.class, args);
    }

}
