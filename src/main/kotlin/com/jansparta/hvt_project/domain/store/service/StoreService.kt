package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface StoreService {

    fun getStoresFromCSV()

    fun createStore( request : CreateStoreRequest ) : StoreResponse

    fun updateStore( request : UpdateStoreRequest , id : Long) : StoreResponse

    fun <T> getStoreList( pageable: Pageable, toSimple:Boolean) : Page<T>
    fun getFilteredStores()
    fun getFilteredSimpleStore()

    fun getStoreBy( id : Long? , company : String? , shopName : String? , tel : String? ) : StoreResponse
}


// 제네릭 메서드 getStoreList() 로 통합
//fun getAllStores( pageable : Pageable) : Page<StoreResponse>
//fun getAllSimpleStores( pageable : Pageable ): Page<SimpleStoreResponse>