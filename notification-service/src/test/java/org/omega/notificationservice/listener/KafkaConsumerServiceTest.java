package org.omega.notificationservice.listener;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class KafkaConsumerServiceTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    static KafkaContainer kafkaContainer = new KafkaContainer("7.7.2");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        kafkaContainer.start();
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Test
    void listen() throws InterruptedException {
        String message = "Test message";
        kafkaTemplate.send("notification.content", message);
        Thread.sleep(2000);
        verify(messagingTemplate, times(1)).convertAndSend("/topic/notifications", message);
    }
}