package com.jansparta.hvt_project.infra.exception

import com.jansparta.hvt_project.infra.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {



    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(LikeException::class)
    fun handleLikeException(e: LikeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(EmailAlreadyExistException::class)
    fun handleEmailAlreadyExistException(e: EmailAlreadyExistException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(NicknameAlreadyExistException::class)
    fun handleNicknameAlreadyExistException(e: NicknameAlreadyExistException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(NotHavePermissionException::class)
    fun handleNotHavePermissionException(e: NotHavePermissionException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPasswordException(e: InvalidPasswordException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }

    @ExceptionHandler(InvalidRoleException::class)
    fun handleInvalidRoleException(e: InvalidRoleException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(e.message))
    }


    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(InvalidArgumentException::class)
    fun handleInvalidArgumentException(e: InvalidArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(e.message))
    }
}