package com.jansparta.hvt_project.domain.store.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.StatNmStatus

data class SimpleStoreResponse constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("company") var company: String,
    @JsonProperty("shopName") var shopName: String?,
    @JsonProperty("domainName") var domainName: String?,
    @JsonProperty("tel") var tel: String?,
    @JsonProperty("email") var email: String?,
    @JsonProperty("ypForm") var ypForm: String?,
    @JsonProperty("comAddr") var comAddr: String?,
    @JsonProperty("statNm") var statNm: String
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
        statNm = statNm.name,
    )
}