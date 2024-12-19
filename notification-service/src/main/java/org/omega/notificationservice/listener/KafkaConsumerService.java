package org.omega.notificationservice.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @KafkaListener(topics = "notification.content", groupId = "org-omega")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        messagingTemplate.convertAndSend("/topic/notifications", message);

        // Add your business logic here
    }
}

