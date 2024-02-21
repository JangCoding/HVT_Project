package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomStoreRepository {
    //QueryDSL 작성
    fun <T> getStores(pageable: Pageable, type: Class<T>): Page<T>?

    fun getStoreBy(id: Long?, company: String?, shopName: String?, tel: String?) : Store

    fun findByRatingAndStatus(rating:Int?, status: String?) : List<Store>

    fun findSimpleByRatingAndStatus(rating:Int?, status: String?) : List<SimpleStore>
  
    fun findByPageableAndFilter(pageable: Pageable, cursorId: Long?, rating:Int?, status: String?): Page<Store>

    fun findSimpleByPageableAndFilter(pageable: Pageable, cursorId: Long?, rating:Int?, status: String?): Page<SimpleStore>
}