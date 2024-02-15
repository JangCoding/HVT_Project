package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.service.StoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stores")
class StoreController (
    private val storeService : StoreService
){
    @PostMapping("/collection") // 업체 리스트 csv 불러오기
    fun getStoresFromCSV()
    {
        TODO()
    }


    @PostMapping("/create")
    fun createStore(
        @RequestBody request : CreateStoreRequest
    ) : ResponseEntity<StoreResponse>
    {
        TODO()
    }

    @PutMapping("/update")
    fun updateStore(
        @RequestBody request : UpdateStoreRequest
    ) : ResponseEntity<StoreResponse>
    {
        TODO()
    }

    @GetMapping("/all")
    fun getAllStores()
    {
        TODO()
    }

    @GetMapping("/all/simple")
    fun getAllSimpleStores()
    {
        TODO()
    }

    @GetMapping("/filtered")
    fun getFilteredStores()
    {
        TODO()
    }

    @GetMapping("/filtered/simple")
    fun getFilteredSimpleStore()
    {
        TODO()
    }

    @GetMapping("{id}")
    fun getStoreById(
        @PathVariable id : Long
    ) : ResponseEntity<StoreResponse>
    {
        TODO()
    }

}