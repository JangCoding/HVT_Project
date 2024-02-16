package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.model.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface StoreService {

    fun getStoresFromCSV()

    fun createStore( request : CreateStoreRequest ) : StoreResponse

    fun updateStore( request : UpdateStoreRequest ) : ResponseEntity<StoreResponse>

    fun getAllStores()

    fun getAllSimpleStores()

    fun getFilteredStoreList(rating: Int?, status: String?) : List<StoreResponse>

    fun getFilteredStorePage(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<StoreResponse>

    fun getFilteredSimpleStore()

    fun getStoreById( id : Long ) : StoreResponse
}