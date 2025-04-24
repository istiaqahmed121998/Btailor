package com.backend.inventoryservice.config;

import com.backend.common.events.ReserveInventoryRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;


@Configuration
public class KafkaConfig {


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReserveInventoryRequest> reserveInventoryRequestListenerFactory(ConsumerFactory<String, ReserveInventoryRequest> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, ReserveInventoryRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setRecordMessageConverter(new StringJsonMessageConverter());
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
    // Configuration for AnotherEvent
//    @Bean
//    public ConsumerFactory<String, AnotherEvent> anotherEventConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "your-bootstrap-servers");
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.backend.common.events");
//        return new DefaultKafkaConsumerFactory<>(
//                props,
//                new StringDeserializer(),
//                new JsonDeserializer<>(AnotherEvent.class) // Deserializer for AnotherEvent
//        );
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, AnotherEvent> anotherEventListenerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, AnotherEvent> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(anotherEventConsumerFactory());
//        return factory;
//    }
}
