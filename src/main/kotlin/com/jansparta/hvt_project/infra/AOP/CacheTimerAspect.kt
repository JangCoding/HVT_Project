package com.jansparta.hvt_project.infra.AOP

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Component // Bean에 등록
@Aspect // Aspect 를 정의하는 클래스 라는 뜻
class CacheTimerAspect {

    private val logger = LoggerFactory.getLogger("Excution Time Logger")

    @Around("@annotation(com.jansparta.hvt_project.infra.AOP.CacheTimer)") // 어떤 어노테이션에 적용될지?
    fun run(joinPoint : ProceedingJoinPoint) : Any{ // ProceedingJoinPoint : aop가 적용되는 메소드.
        val stopWatch = StopWatch()                 // java.time 의 메서드. 본 메소드명과 관계 없음

        stopWatch.start()
        var result = joinPoint.proceed() // 이 @StopWatch 메소드를 실행하는 메소드의 전 후에 실행 -> Around
        stopWatch.stop()

        val methodName = joinPoint.signature.name
        val methodArguments = joinPoint.args

        val timeElapsedMs = stopWatch.totalTimeMillis
        logger.info("Method Name : ${methodName}" +
                "| Arguments : ${methodArguments.joinToString( ", " )}" +
                "| Excution Time : ${timeElapsedMs}" )

        return result


    }
}