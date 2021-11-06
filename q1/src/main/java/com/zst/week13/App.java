package com.zst.week13;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
public class App implements CommandLineRunner {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        IntStream.range(0, 100).forEach(i -> {
            kafkaTemplate.send("t1", String.valueOf(System.currentTimeMillis()));
        });
        kafkaTemplate.flush();
    }
}
