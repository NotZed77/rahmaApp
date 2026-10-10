package com.notzed.rahmaApp.prayer_service.config;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("prayer-redis-")
                .entryTtl(Duration.ofMinutes(15))
                .enableTimeToIdle()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                redisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    private RedisSerializer<Object> redisSerializer(){
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build();

        ObjectMapper mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .activateDefaultTyping(ptv, DefaultTyping.NON_FINAL)
                .build();

        return new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(@Nullable Object value) throws SerializationException {
                if(value == null) return new byte[0];
                try {
                    return mapper.writeValueAsBytes(value);
                } catch (JacksonException e){
                    throw new SerializationException("Could not serialize object", e);
                }
            }

            @Override
            public @Nullable Object deserialize(byte @Nullable [] bytes) throws SerializationException {
                if(bytes == null || bytes.length == 0) return null;
                try {
                    return mapper.readValue(bytes, Object.class);
                }catch (JacksonException e){
                    throw new SerializationException("Could not serialize object", e);
                }
            }
        };
    }
}
