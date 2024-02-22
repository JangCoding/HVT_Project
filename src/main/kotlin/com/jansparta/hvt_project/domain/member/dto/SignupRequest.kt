package com.jansparta.hvt_project.domain.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    @field: NotBlank
    @field: Pattern(
        regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$",
        message = "이메일의 형식에 맞게 입력해주세요"
    )
    @JsonProperty("email")
    private val _email: String?,

    @field: NotBlank
    @field: Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,15}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~15자리로 입력해주세요"
    )
    @JsonProperty("password")
    private val _password: String?,

    val nickName: String,


    @field: NotBlank
    @field: Pattern(
        regexp = "^\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])-\\*{7}\$",
        message = "주민등록번호(YYMMDD-*******)로 입력해주세요"
    )
    @JsonProperty("residentid")
    private val _residentId: String,

){
val email: String
    get() = _email!!
val password: String
    get() = _password!!


val residentId: String
    get() = _residentId
}


