package com.jansparta.hvt_project.infra.exception

data class InvalidArgumentException(val massage: String) : RuntimeException(
    "유효하지 않은 입력값입니다"
)
