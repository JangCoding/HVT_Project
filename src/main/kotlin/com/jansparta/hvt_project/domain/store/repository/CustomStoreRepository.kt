package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.domain.store.model.StatNmStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomStoreRepository {
    //QueryDSL 작성
    fun <T> getStores(pageable: Pageable, type: Class<T>): Page<T>?

    fun getStoreBy(id: Long?, company: String?, shopName: String?, tel: String?) : Store
  
  
    fun findByRatingAndStatus(rating:Int?, statNmStatus: StatNmStatus) : List<Store>
}




// 제네릭 메서드로 통합
//    fun getPagedStores(pageable: Pageable) : Page<Store>
//    fun getPagedSimpleStores(pageable: Pageable) : Page<SimpleStore>
