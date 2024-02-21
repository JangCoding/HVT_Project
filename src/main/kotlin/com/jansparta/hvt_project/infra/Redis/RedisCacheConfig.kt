package com.jansparta.hvt_project.infra.Redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@Configuration
@EnableCaching
class RedisCacheConfig {

    // jackson LocalDateTime mapper
//    @Bean
//    fun objectMapper(): ObjectMapper {
//        val mapper = ObjectMapper()
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // timestamp 형식 안따르도록 설정
////        mapper.registerModules(JavaTimeModule(), Jdk8Module()) // LocalDateTime 매핑을 위해 모듈 활성화
//        mapper.registerModule(ParameterNamesModule());
//        return mapper
//    }

    @Bean
    fun defaultCacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val objectMapper = ObjectMapper().registerModules(kotlinModule()).activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder().allowIfBaseType(Any::class.java).build(),
            ObjectMapper.DefaultTyping.EVERYTHING

        ).registerModules(JavaTimeModule(), Jdk8Module())


        val redisCacheConfiguration: RedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(300))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer(objectMapper)
                )
            )

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
            .cacheDefaults(redisCacheConfiguration).build();
    }
}

//@Configuration
//@EnableCaching  // 캐싱 활성화
//class RedisCacheConfig {
//
//
//    @Bean
//    fun cacheManager(cf: RedisConnectionFactory): CacheManager {
//        // Redis 캐시 메니저를 생성하는 메서드. 레디스 연결 팩토리 를 인자로 받아 캐시 매니저를 생성함
//
//          // dateTime 없는 값엔 또 안됨
//          // local date time 역직렬화 위해 추가 코드
////        val objectMapper = ObjectMapper() // ObjectMapper 객체 생성. Jackson 라이브러리에서 JSON <-> Java 변환하는 데 사용됨.
////        objectMapper.registerModule(JavaTimeModule()) //ObjectMapper에 JavaTimeModule 등록. java.time 패키지(Java 8에서 추가된 날짜와 시간 API)의 클래스들을 Jackson이 처리할 수 있도록.
////        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL) // 역직렬화 시 클래스의 유형 정보를 기본적으로 포함하도록 ObjectMapper를 구성. Jackson에게 역직렬화할 때 필요한 클래스의 유형 정보를 전달하여 오류 방지. NON_FINAL은 객체의 최종 클래스가 아니더라도 유형 정보를 포함하도록 지정.
////        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//
//        // Redis 캐싱 구성을 생성하는 부분
//        val redisCacheConfiguration =
//            RedisCacheConfiguration.defaultCacheConfig() // 기본 캐시 구성 사용.
//            .serializeKeysWith(  // 키를 직렬화 - StringRedisSerializer 사용
//                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
//            .serializeValuesWith( // 값을 직렬화 - GenericJackson2JsonRedisSerializer 사용
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                    GenericJackson2JsonRedisSerializer()  // ObjectMapper 사용하여 GenericJackson2JsonRedisSerializer 객체 생성. Jackson 기반 JSON 직렬화 및 역직렬화 처리. Redis에 저장된 데이터를 Java 객체로 변환, Java 객체를 Redis에 저장할 때 사용.
//                ))
//            .entryTtl(Duration.ofMinutes(3L)) // 캐시 만료시간  3분으로 설정
//
//
//        return RedisCacheManager // RedisCacheManager를 생성하기 위한 빌더 패턴을 사용
//            .builder(cf) // RedisConnectionFactory 를 받아 CacheManager를 빌드
//            .cacheDefaults(redisCacheConfiguration) // 캐시 매니저의 기본 캐시 구성 설정. 위에서 만든 RedisCache 구성 사용
//            .build() // 최종적으로 빌드하여 반환. 캐싱을 관리하고 사용할 수 있도록 함.
//    }
//}