package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.domain.store.service.StoreService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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


    @PostMapping("/create") // 업체 등록 . 상호명으로 중복 판단
    fun createStore(
        @RequestBody request : CreateStoreRequest
    ) : ResponseEntity<StoreResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.createStore(request))
    }

    @GetMapping() // 업체 리스트 전체 조회
    fun <T> getStores(
        @PageableDefault( size = 10, sort = ["id"]) pageable: Pageable,
        toSimple : Boolean // Projection 적용 여부
    ) : ResponseEntity<Page<T>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreList(pageable, toSimple))
    }

    @GetMapping("/filtered") // 업체 리스트 필터 조회
    fun getFilteredStores()
    {
        TODO()
    }

    @GetMapping("/filtered/simple") // 업체 리스트 필터 조회(단순조회)
    fun getFilteredSimpleStore()
    {
        TODO()
    }

    @GetMapping("{id}") // 업체 단건 조회
    fun getStoreById(
        @PathVariable id : Long
    ) : ResponseEntity<StoreResponse>
    {
        TODO()
    }


    @PutMapping("/update/{id}") // 업체 수정
    fun updateStore(
        @RequestBody request : UpdateStoreRequest,
        @PathVariable id : Long,
    ) : ResponseEntity<StoreResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.updateStore(request, id))
    }

    @DeleteMapping("/delete/{id}") // 업체 삭제
    fun deleteStore(
        @PathVariable id:Long
    ) : ResponseEntity<Unit>
    {
        TODO()
    }

}



//    // 제네릭 활용하여 getStores() 로 통합
//    @GetMapping("/all") // 업체 리스트 전체 조회
//    fun getAllStores(
//        @PageableDefault( size = 10, sort = ["id"]) pageable: Pageable
//
//    ) : ResponseEntity<Page<StoreResponse>>
//    {
//        return ResponseEntity.status(HttpStatus.OK).body(storeService.getAllStores(pageable))
//    }
//
//    @GetMapping("/all/simple") // 업체 리스트 전체 조회(단순조회)
//    fun getAllSimpleStores(
//    @PageableDefault( size = 10, sort = ["id"]) pageable: Pageable
//    ) : ResponseEntity<Page<SimpleStoreResponse>>
//    {
//        return ResponseEntity.status(HttpStatus.OK).body(storeService.getAllSimpleStores(pageable))
//    }
