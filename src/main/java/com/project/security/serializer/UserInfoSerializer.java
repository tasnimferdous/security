package com.project.security.serializer;

import com.project.security.request.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
public class UserInfoSerializer implements Serializer<UserRequestDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, UserRequestDto userRequestDto) {
        try {
            return new ObjectMapper().writeValueAsString(userRequestDto).getBytes();
        } catch (Exception e) {
            log.error("Exception: ",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, UserRequestDto data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
