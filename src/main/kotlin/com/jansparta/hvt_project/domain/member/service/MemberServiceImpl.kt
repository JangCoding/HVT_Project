package com.jansparta.hvt_project.domain.member.service

import com.jansparta.hvt_project.domain.member.dto.LoginRequest
import com.jansparta.hvt_project.domain.member.dto.LoginResponse
import com.jansparta.hvt_project.domain.member.dto.SignupRequest
import com.jansparta.hvt_project.domain.member.dto.SignupResponse
import com.jansparta.hvt_project.domain.member.model.Member
import com.jansparta.hvt_project.domain.member.model.toSignupResponse
import com.jansparta.hvt_project.domain.member.repository.MemberRepository
import com.jansparta.hvt_project.domain.member.repository.MemberRole
import com.jansparta.hvt_project.infra.JwtPlugin
import com.jansparta.hvt_project.infra.exception.EmailAlreadyExistException
import com.jansparta.hvt_project.infra.exception.InvalidPasswordException
import com.jansparta.hvt_project.infra.exception.ModelNotFoundException
import com.jansparta.hvt_project.infra.exception.NicknameAlreadyExistException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime


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