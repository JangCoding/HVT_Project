package com.jansparta.hvt_project.domain.member.controller

import com.jansparta.hvt_project.domain.member.dto.LoginRequest
import com.jansparta.hvt_project.domain.member.dto.SignupRequest
import com.jansparta.hvt_project.domain.member.dto.SignupResponse
import com.jansparta.hvt_project.domain.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("members")
class MemberController(
    private val memberService: MemberService
) {
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signup(signupRequest: SignupRequest) : ResponseEntity<SignupResponse> {
        memberService.signup(signupRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(loginRequest : LoginRequest) : ResponseEntity<Any>{
        val memberDetails = memberService.login(loginRequest)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, memberDetails.accessToken)
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(memberDetails.accessToken)
    }
}