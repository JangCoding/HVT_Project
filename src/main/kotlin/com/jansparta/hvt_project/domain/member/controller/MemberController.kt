package com.jansparta.hvt_project.domain.member.controller

import com.jansparta.hvt_project.domain.member.dto.LoginRequest
import com.jansparta.hvt_project.domain.member.dto.MemberResponse
import com.jansparta.hvt_project.domain.member.dto.SignupRequest
import com.jansparta.hvt_project.domain.member.dto.SignupResponse
import com.jansparta.hvt_project.domain.member.service.MemberService
import com.jansparta.hvt_project.infra.Security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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

    @Operation(summary = "회원정보 전체 조회")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getAllMembers() : ResponseEntity<List<MemberResponse>>{
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAllMembers())
    }

    @Operation(summary = "회원정보 단건 조회")
    @PreAuthorize("hasRole('ADMIN') or #userPrincipal.id == #userId")
    @GetMapping("/{userId}")
    fun getMember(
        @PathVariable userId : UUID,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MemberResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberByUserId(userId))
    }

}