package com.jansparta.hvt_project.domain.store.model

enum class statNmStatus {
    SITE_SUSPENDED,
    CLOSED,
    ADVERTISEMENT,
    MISMATCHED_INFO,
    SITE_CLOSED,
    BUSINESS_USUAL,
    NOT_CONFIRMED;

    companion object {
        fun fromString(value: String): statNmStatus? {
            return when (value) {
                "사이트운영중단" -> SITE_SUSPENDED
                "휴업중" -> CLOSED
                "광고용" -> ADVERTISEMENT
                "등록정보불일치" -> MISMATCHED_INFO
                "사이트폐쇄" -> SITE_CLOSED
                "영업중" -> BUSINESS_USUAL
                "확인안됨" -> NOT_CONFIRMED
                else -> throw IllegalArgumentException("STAT_NM INPUT ERROR")
            }
        }
    }
}