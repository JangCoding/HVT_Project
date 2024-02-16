package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.service.StoreService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    fun getFilteredStoreList(
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<List<StoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredStoreList(rating, status))
    }

    @GetMapping("/pagenated")
    fun getFilteredStorePage(
        @PageableDefault(size = 10) pageable: Pageable,
        @RequestParam(value = "cursorId", required = false) cursorId: Long?,
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<Page<StoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredStorePage(pageable, cursorId, rating, status))
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