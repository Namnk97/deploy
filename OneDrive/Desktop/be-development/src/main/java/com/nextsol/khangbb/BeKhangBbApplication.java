package com.nextsol.khangbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class BeKhangBbApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeKhangBbApplication.class, args);
    }

}
