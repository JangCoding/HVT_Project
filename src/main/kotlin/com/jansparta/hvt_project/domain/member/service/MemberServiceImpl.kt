package com.jansparta.hvt_project.domain.member.service

import com.jansparta.hvt_project.domain.member.dto.*
import com.jansparta.hvt_project.domain.member.model.Member
import com.jansparta.hvt_project.domain.member.model.toResponse
import com.jansparta.hvt_project.domain.member.model.toSignupResponse
import com.jansparta.hvt_project.domain.member.repository.MemberRepository
import com.jansparta.hvt_project.domain.member.repository.MemberRole
import com.jansparta.hvt_project.infra.Security.jwt.JwtPlugin
import com.jansparta.hvt_project.infra.exception.EmailAlreadyExistException
import com.jansparta.hvt_project.infra.exception.InvalidPasswordException
import com.jansparta.hvt_project.infra.exception.ModelNotFoundException
import com.jansparta.hvt_project.infra.exception.NicknameAlreadyExistException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder : PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) : MemberService {
    override fun signup(request: SignupRequest): SignupResponse {
        checkedEmailOrNicknameExists(request.email, request.nickName, memberRepository)

        return memberRepository.save(
            Member(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                nickName = request.nickName,
                residentId = request.residentId,
                createdAt = LocalDateTime.now(),
                role = MemberRole.MEMBER
            )
        ).toSignupResponse()
    }

    override fun login(request: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(request.email) ?: throw ModelNotFoundException("User", null)

        checkedLoginPassword(member.password, request.password, passwordEncoder)

        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = member.id.toString(),
                email = member.email,
                role = member.role.toString()
            )
        )
    }

    override fun getAllMembers(): List<MemberResponse> {
        return memberRepository.findAll().map{ member ->
            member.toResponse()
        }
    }

    override fun getMemberByUserId(userId: UUID): MemberResponse {
        val member = memberRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        return member.toResponse()
    }

    override fun updateMember(userId: UUID, request: UpdateMemberRequest): MemberResponse {
        val updateProfile = memberRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        memberRepository.save(updateProfile)
        return updateProfile.toResponse()
    }
}

private fun checkedEmailOrNicknameExists(email: String, nickname: String,memberRepository: MemberRepository) {
    if (memberRepository.existsByEmail(email)) {
        throw EmailAlreadyExistException(email)
    }

    if (memberRepository.existsByNickName(nickname)) {
        throw NicknameAlreadyExistException(nickname)
    }
}
private fun checkedLoginPassword(password: String, inputPassword: String, passwordEncoder: PasswordEncoder) {
    if(!passwordEncoder.matches(inputPassword, password)) {
        throw InvalidPasswordException(inputPassword)
    }
}