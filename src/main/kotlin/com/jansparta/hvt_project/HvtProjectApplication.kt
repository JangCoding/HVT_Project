package com.jansparta.hvt_project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
@SpringBootApplication
@EnableJpaAuditing // Auditing 기능 활성화
@EnableCaching      // 캐싱 사용
@EnableAspectJAutoProxy // AOP 기능 활성화
@EnableScheduling // 스케줄러 기능 활성화
class HvtProjectApplication

fun main(args: Array<String>) {
    runApplication<HvtProjectApplication>(*args)
}
