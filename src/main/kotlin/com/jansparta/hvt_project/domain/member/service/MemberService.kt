package com.jansparta.hvt_project.domain.member.service

import com.jansparta.hvt_project.domain.member.dto.*
import java.util.UUID

interface MemberService {
    fun signup(request : SignupRequest) : SignupResponse

    fun login(request: LoginRequest) : LoginResponse

    fun getAllMembers(): List<MemberResponse>

    fun getMemberByUserId(userId : UUID) : MemberResponse

    fun updateMember(userId: UUID, request: UpdateMemberRequest) : MemberResponse
}