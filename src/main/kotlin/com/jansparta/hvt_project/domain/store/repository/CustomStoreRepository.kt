package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomStoreRepository {
    //QueryDSL 작성

    fun findByRatingAndStatus(rating:Int?, status: String?) : List<Store>

    fun findByPageableAndFilter(pageable: Pageable, cursorId: Long?, rating:Int?, status: String?): Page<Store>
}