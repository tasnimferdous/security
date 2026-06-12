package com.project.security.event;

import com.project.security.request.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserPublisher {
    @Autowired
    private KafkaTemplate<String, UserRequestDto> kafkaTemplate;

    @Value("${spring.kafka.topic:user-info}")
    private String TOPIC;

    public void publishUserInfo(UserRequestDto userRequestDto) {
        Message<UserRequestDto> message =
                MessageBuilder.withPayload(userRequestDto)
                        .setHeader(KafkaHeaders.TOPIC, TOPIC)
                        .build();
        kafkaTemplate.send(message);
    }

}
