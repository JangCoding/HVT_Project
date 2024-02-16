package com.jansparta.hvt_project.domain.member.service

import com.jansparta.hvt_project.domain.member.dto.LoginRequest
import com.jansparta.hvt_project.domain.member.dto.LoginResponse
import com.jansparta.hvt_project.domain.member.dto.SignupRequest
import com.jansparta.hvt_project.domain.member.dto.SignupResponse

interface MemberService {
    fun signup(request : SignupRequest) : SignupResponse

    fun login(request: LoginRequest) : LoginResponse
}