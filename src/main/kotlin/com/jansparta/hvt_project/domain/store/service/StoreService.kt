package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface StoreService {

    fun getStoresFromCSV()

    fun createStore( request : CreateStoreRequest ) : StoreResponse

    fun updateStore( request : UpdateStoreRequest , id : Long) : StoreResponse

    fun getAllStores( pageable : Pageable) : Page<StoreResponse>

    fun getAllSimpleStores()

    fun getFilteredStores()

    fun getFilteredSimpleStore()

    fun getStoreById( id : Long ) : StoreResponse
}