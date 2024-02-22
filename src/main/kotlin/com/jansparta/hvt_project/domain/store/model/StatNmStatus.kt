package com.jansparta.hvt_project.domain.store.model

import com.jansparta.hvt_project.domain.exception.InvalidArgumentException

enum class StatNmStatus  {
    사이트운영중단 ,
    휴업중 ,
    `광고용(홍보용)`,
    등록정보불일치,
    사이트폐쇄,
    영업중,
    확인안됨;


    companion object {
        fun fromString(value: String): StatNmStatus { // String 값을 Enum 값으로 변환
            return when (value) {
                "사이트운영중단" -> 사이트운영중단
                "휴업중" -> 휴업중
                "광고용(홍보용)" -> `광고용(홍보용)`
                "등록정보불일치" -> 등록정보불일치
                "사이트폐쇄" -> 사이트폐쇄
                "영업중" -> 영업중
                "확인안됨" -> 확인안됨
                else -> throw InvalidArgumentException("StatNmStatus Error")
            }
        }
    }
}
