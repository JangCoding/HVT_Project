package com.jansparta.hvt_project.domain.member.dto

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class MemberResponse (
    val id : UUID,
    val email: String,
    val nickname: String,
    val role : String,
    val birthdate : LocalDate,
    val createdAt: LocalDateTime,
)