package com.jansparta.hvt_project.domain.member.controller

import com.jansparta.hvt_project.domain.member.dto.*
import com.jansparta.hvt_project.domain.member.repository.MemberRole
import com.jansparta.hvt_project.domain.member.service.MemberService
import com.jansparta.hvt_project.infra.Security.jwt.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
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
    fun getAllMembers(@AuthenticationPrincipal userPrincipal: UserPrincipal) : ResponseEntity<List<MemberResponse>>{
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

    @Operation(summary = "회원정보 변경")
    @PreAuthorize("hasRole('ADMIN') or #userPrincipal.id == #userId")
    @PutMapping("/{userId}")
    fun updateMemberProfile(
        @PathVariable userId : UUID,
        @RequestBody updateMemberRequest: UpdateMemberRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MemberResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMember(userId,updateMemberRequest))
    }

    @Operation(summary = "회원 등급 변경")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/role")
    fun updateMemberRole(
        @PathVariable userId : UUID,
        @RequestBody roleDto: RoleDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MemberResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateRole(userId,roleDto))
    }

    @Operation(summary = "user 삭제")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}/")
    fun deleteUser(
        @PathVariable userId: UUID,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<Unit> {
        memberService.deleteMember(userId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @Operation(summary = "회원탈퇴")
    @PreAuthorize("#userPrincipal.id == #userId")
    @DeleteMapping("/{userId}/deleteId")
    fun deleteMyEmail(
        @PathVariable userId: UUID,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        loginRequest: LoginRequest
    ): ResponseEntity<Unit> {
        memberService.deleteAccount(userId, loginRequest)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}