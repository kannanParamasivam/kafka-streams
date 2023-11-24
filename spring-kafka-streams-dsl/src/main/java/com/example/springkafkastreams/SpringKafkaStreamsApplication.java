package com.example.springkafkastreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class SpringKafkaStreamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaStreamsApplication.class, args);
    }

}
