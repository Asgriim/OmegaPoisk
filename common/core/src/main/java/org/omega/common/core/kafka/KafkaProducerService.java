package org.omega.common.core.kafka;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate ;

    @Value("${spring.kafka.topic}")
    private String topicName;

    public void sendMessage(String message) {
        kafkaTemplate.send(new ProducerRecord<>(topicName, message));
    }
}
