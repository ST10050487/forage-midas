package com.jpmc.midascore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

public class KafkaConsumerConfig {
    //Converts String JSON payloads to target method parameter types (Transaction)
    @Bean
    public RecordMessageConverter recordMessageConverter()
    {
        return new StringJsonMessageConverter();
    }
}
