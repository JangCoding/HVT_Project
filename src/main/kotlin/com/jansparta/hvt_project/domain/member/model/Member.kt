package com.jansparta.hvt_project.domain.member.model

import com.jansparta.hvt_project.domain.member.dto.MemberResponse
import com.jansparta.hvt_project.domain.member.dto.SignupResponse
import com.jansparta.hvt_project.domain.member.repository.MemberRole
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
@Table(name = "member")
class Member(
    @Column(name = "EMAIL")
    val email: String,

    @Column(name = "PASSWORD")
    var password: String,

    @Column(name = "NICKNAME")
    var nickName: String,

    @Column(name = "RESIDENT_ID")
    val residentId: String,


    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: MemberRole,

    ) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID? = null
}

fun Member.toSignupResponse(): SignupResponse {
    return SignupResponse(
        id = id!!,
        email = email,
        message = "회원가입에 성공하였습니다."
    )
}

fun Member.toResponse(): MemberResponse {

    return MemberResponse(
        id = id!!,
        email = email,
        nickname = nickName,
        residentId = residentId,
        role = role.toString(),
    )
}