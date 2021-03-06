package com.wsiz.gameshub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GameshubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameshubApplication.class, args);
    }

}
