package com.jansparta.hvt_project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class HvtProjectApplication

fun main(args: Array<String>) {
    runApplication<HvtProjectApplication>(*args)
}
