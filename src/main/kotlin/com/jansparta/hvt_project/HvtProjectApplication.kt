package com.jansparta.hvt_project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing // Auditing 기능 활성화
class HvtProjectApplication

fun main(args: Array<String>) {
    runApplication<HvtProjectApplication>(*args)
}
