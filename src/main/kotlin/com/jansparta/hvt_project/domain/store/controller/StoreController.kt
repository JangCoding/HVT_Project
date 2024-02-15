package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.service.StoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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

    @GetMapping("/all/detailed")
    fun getAllDetailedStores()
    {
        TODO()
    }

    @GetMapping("/all/simple")
    fun getAllSimpleStores()
    {
        TODO()
    }

    @GetMapping("/filtered/detailed")
    fun getFilteredDetailedStores()
    {
        TODO()
    }

    @GetMapping("/filtered/simple")
    fun getFilteredSimpleStore()
    {
        TODO()
    }

}