package com.jansparta.hvt_project.infra.Redis

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class CustomPageImpl<T> @JsonCreator constructor(
    @JsonProperty("content") content: List<T>,
    @JsonProperty("pageable") pageable: Pageable,
    @JsonProperty("totalElements") totalElements: Long
) : PageImpl<T>(content, pageable, totalElements) {
    constructor(content: List<T>, pageable: Pageable) : this(content, pageable, content.size.toLong())
}