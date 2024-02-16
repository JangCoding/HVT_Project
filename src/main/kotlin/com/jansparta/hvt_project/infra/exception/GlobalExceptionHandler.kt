package com.jansparta.hvt_project.infra.exception

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

class GlobalExceptionHandler {

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(e: JwtException): ResponseEntity<String> {
        // 예외에 따른 HTTP 상태 코드와 에러 메시지를 설정
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: ${e.message}")
    }

    // 여기에 다른 예외 처리 메소드를 추가할 수 있습니다.
}