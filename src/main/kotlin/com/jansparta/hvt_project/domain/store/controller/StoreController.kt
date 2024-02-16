package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.service.StoreService
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/stores")
class StoreController (
    private val storeService : StoreService
){
    @PostMapping("/collection") // 업체 리스트 csv 불러오기
    fun getStoresFromCSV(@RequestParam("file")multipartFile: MultipartFile)
    {
        val tempFile = File.createTempFile("temp", null)
        try {
            multipartFile.transferTo(tempFile)
            storeService.getStoresFromCSV(tempFile)
        } finally {
            tempFile.delete() // 파일 처리가 끝난 후에 임시 파일 삭제
        }
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

    @GetMapping("/search") // 업체 단건 조회
    fun getStoreBy(
        @RequestParam(value = "Id(아이디)", required = false) id : Long?,
        @RequestParam(value = "Company(상호명)", required = false) company : String?,
        @RequestParam(value = "shopName(쇼핑몰명)", required = false) shopName : String?,
        @RequestParam(value = "tel(전화번호)", required = false) tel : String?,
    ) : ResponseEntity<StoreResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreBy(id,company,shopName,tel))
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



//    // 제네릭 메서드 getStores() 로 통합
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
