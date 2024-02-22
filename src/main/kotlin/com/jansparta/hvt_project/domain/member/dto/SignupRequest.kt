package com.jansparta.hvt_project.domain.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    val email : String?,
    val password: String?,
    val nickName: String,
    var residentId: String?,
    )


