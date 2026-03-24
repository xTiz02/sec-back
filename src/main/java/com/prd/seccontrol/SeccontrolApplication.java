package com.prd.seccontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeccontrolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeccontrolApplication.class, args);
    }

}
