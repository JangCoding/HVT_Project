package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.repository.StoreRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,

) : StoreService {

    override fun getStoresFromCSV() {
        TODO("Not yet implemented")
    }

    override fun createStore(request: CreateStoreRequest): StoreResponse {
        TODO("Not yet implemented")
    }

    override fun updateStore(request: UpdateStoreRequest): ResponseEntity<StoreResponse> {
        TODO("Not yet implemented")
    }

    override fun getAllDetailedStores() {
        TODO("Not yet implemented")
    }

    override fun getAllSimpleStores() {
        TODO("Not yet implemented")
    }

    override fun getFilteredDetailedStores() {
        TODO("Not yet implemented")
    }

    override fun getFilteredSimpleStore() {
        TODO("Not yet implemented")
    }

    override fun getStoreById(id: Long): StoreResponse {
        TODO("Not yet implemented")
    }
}