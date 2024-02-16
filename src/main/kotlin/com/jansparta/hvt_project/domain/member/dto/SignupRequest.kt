package com.jansparta.hvt_project.domain.member.dto

data class SignupRequest(
    val email : String,
    val password : String,
    val nickName : String,
    val residentId : String,
)
