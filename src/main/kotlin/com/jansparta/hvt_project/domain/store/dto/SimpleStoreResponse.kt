package com.jansparta.hvt_project.domain.store.dto

import com.jansparta.hvt_project.domain.store.model.SimpleStore

data class SimpleStoreResponse(
    val id:Long,
    var company: String, // 상호
    var shopName: String?, // 쇼핑몰명
    var domainName: String?,  // 도메인명
    var tel: String?,    // 전화번호
    var email: String?, // 운영자이메일
    var ypForm: String?,  // 영업형태
    var comAddr: String?,     // 회사주소
    var statNm: String,   // 업소상태
)

fun SimpleStore.toResponse(): SimpleStoreResponse {
    return SimpleStoreResponse(
        id = id,
        company = company,
        shopName = shopName,
        domainName = domainName,
        tel = tel,
        email = email,
        ypForm = ypForm,
        comAddr = comAddr,
        statNm = statNm,
    )
}