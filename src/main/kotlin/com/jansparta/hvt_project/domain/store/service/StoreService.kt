package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import org.springframework.http.ResponseEntity
import java.io.File

interface StoreService {

    fun readCsvFile()

    fun getStoresFromCSV(file: File)

    fun createStore( request : CreateStoreRequest ) : StoreResponse

    fun updateStore( request : UpdateStoreRequest ) : ResponseEntity<StoreResponse>

    fun getAllStores()

    fun getAllSimpleStores()

    fun getFilteredStores()

    fun getFilteredSimpleStore()

    fun getStoreById( id : Long ) : StoreResponse
}