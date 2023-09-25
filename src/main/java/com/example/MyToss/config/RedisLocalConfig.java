package com.example.MyToss.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;
import org.slf4j.Logger;

import java.io.IOException;

@Configuration
// Configuration이란게 따로 있는게 아니라,
// 그냥 해당 bean의 이름을 Configuration으로 지정함.
public class RedisLocalConfig {

    @Value("${spring.redis.port}")
    private int redisPort;
    private RedisServer redisServer;
    // 주의!! redis.embedded 패키지에 있는 RadisServer객체를 만들어야함.

    @PostConstruct  // 생성자를 호출하자마자 수행하는 메서드
    public void startRedis() throws IOException {  // 예외 설정 해줘야함.
        redisServer = new RedisServer(redisPort);
        try{
            redisServer.start();
        } catch(Exception e){
            System.out.println("");
        }

    }
    @PreDestroy // 객체 삭제 직전에 수행하는 메서드
    public void stopRedis() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}