package com.jansparta.hvt_project.infra.exception.dto

import com.jansparta.hvt_project.infra.status.ResultCode

data class BaseResponse<T>(
    val resultCode: String = ResultCode.SUCCESS.name,
    val data: T? = null,
    val message: String = ResultCode.SUCCESS.msg,
)