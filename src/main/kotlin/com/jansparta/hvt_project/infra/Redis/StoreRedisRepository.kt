package com.jansparta.hvt_project.infra.Redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class StoreRedisRepository (
    private val redisTemplate: RedisTemplate<String, String> // key - value 타입 별로 나눔
){

}