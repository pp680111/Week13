package com.zst.week13;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {
    @Bean
    public NewTopic t1() {
        return TopicBuilder.name("t1")
                .partitions(3)
                .replicas(2)
                .build();
    }
}
