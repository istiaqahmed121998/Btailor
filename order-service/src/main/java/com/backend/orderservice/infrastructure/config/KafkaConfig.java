package com.backend.orderservice.infrastructure.config;

import com.backend.common.events.CartCheckedOutEvent;
import com.backend.common.events.ReserveInventoryResponseEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CartCheckedOutEvent> cartCheckedOutListenerFactory(ConsumerFactory<String, CartCheckedOutEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, CartCheckedOutEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setRecordMessageConverter(new StringJsonMessageConverter());
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReserveInventoryResponseEvent> reserveInventoryResponseEventConsumerFactory(ConsumerFactory<String, ReserveInventoryResponseEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, ReserveInventoryResponseEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setRecordMessageConverter(new StringJsonMessageConverter());
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
