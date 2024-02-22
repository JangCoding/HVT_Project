package com.jansparta.hvt_project.infra.Redis

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class PageImplWrapper<T> {

    @JsonProperty("content")
    lateinit var content: List<T>

    @JsonProperty("number")
    var number: Int = 0

    @JsonProperty("size")
    var size: Int = 0

    @JsonProperty("totalElements")
    var totalElements: Long = 0

    @JsonProperty("pageable")
    var pageable: Any? = null

    @JsonProperty("last")
    var last: Boolean = false

    @JsonProperty("totalPages")
    var totalPages: Int = 0

    @JsonProperty("sort")
    var sort: Any? = null

    @JsonProperty("first")
    var first: Boolean = false

    @JsonProperty("numberOfElements")
    var numberOfElements: Int = 0

    @JsonCreator
    constructor(
        @JsonProperty("content") content: List<T>,
        @JsonProperty("number") number: Int,
        @JsonProperty("size") size: Int,
        @JsonProperty("totalElements") totalElements: Long,
        @JsonProperty("pageable") pageable: Any?,
        @JsonProperty("last") last: Boolean,
        @JsonProperty("totalPages") totalPages: Int,
        @JsonProperty("sort") sort: Any?,
        @JsonProperty("first") first: Boolean,
        @JsonProperty("numberOfElements") numberOfElements: Int
    ) {
        this.content = content
        this.number = number
        this.size = size
        this.totalElements = totalElements
        this.pageable = pageable
        this.last = last
        this.totalPages = totalPages
        this.sort = sort
        this.first = first
        this.numberOfElements = numberOfElements
    }

    fun toPageImpl(): PageImpl<T> {
        return PageImpl(content, PageRequest.of(number, size), totalElements)
    }
}