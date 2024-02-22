package com.jansparta.hvt_project.infra.Redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig (
    @Value("\${spring.data.redis.host}") val host: String,
    @Value("\${spring.data.redis.port}") val port: Int,

) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val connectionFactory = LettuceConnectionFactory(host, port)
        return connectionFactory
    }

    @Configuration
    class RedisConfig {

        @Bean
        fun redisTemplate(redisConnectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper): RedisTemplate<String, StoreResponse> {
            val newObjectMapper = ObjectMapper().registerModules(kotlinModule()).activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfBaseType(Any::class.java).build(),
                ObjectMapper.DefaultTyping.EVERYTHING

            ).registerModules(JavaTimeModule(), Jdk8Module())

            val template = RedisTemplate<String, StoreResponse>()
            template.connectionFactory = redisConnectionFactory

            // JSON 직렬화를 위한 Serializer 설정
            val valueSerializer = GenericJackson2JsonRedisSerializer(newObjectMapper)
            //valueSerializer.setObjectMapper(objectMapper)

            // String 자료구조를 위한 Serializer
            template.keySerializer = StringRedisSerializer()

            // StoreResponse 객체를 JSON으로 직렬화하는 Serializer 설정
            template.valueSerializer = valueSerializer

            // Hash 자료구조를 위한 Serializer
            template.hashKeySerializer = StringRedisSerializer()
            template.hashValueSerializer = valueSerializer

            return template
        }
    }

}