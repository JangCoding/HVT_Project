package com.jansparta.hvt_project.domain.member.dto

import java.time.LocalDateTime
import java.util.UUID

data class SignupResponse(
    val id: UUID? = null,
    val email : String,
    val createdAt: LocalDateTime,
    val message : String,
)