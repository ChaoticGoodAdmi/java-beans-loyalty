package ru.ushakov.beansloyalty.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import ru.ushakov.beansloyalty.domain.LoyaltySnapshot


@Configuration
class RedisConfig {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, LoyaltySnapshot> {
        val template = RedisTemplate<String, LoyaltySnapshot>()
        template.connectionFactory = redisConnectionFactory

        template.keySerializer = StringRedisSerializer()

        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        val serializer = Jackson2JsonRedisSerializer(objectMapper, LoyaltySnapshot::class.java)

        template.valueSerializer = serializer
        template.afterPropertiesSet()
        return template
    }
}