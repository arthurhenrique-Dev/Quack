package com.quack.quack_app.Infra.Adapters.Output.Persistence.Cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quack.quack_app.Domain.ValueObjects.Reviews;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;


public class ReviewRedisSerializer implements RedisSerializer<Object> {


    private final ObjectMapper objectMapper;

    public ReviewRedisSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(Object value) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new SerializationException("Error serializing", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) return null;
        try {
            return objectMapper.readValue(bytes, Reviews.class); // tipo explícito!
        } catch (Exception e) {
            throw new SerializationException("Error deserializing", e);
        }
    }
}
