package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.service.StoreService
import com.jansparta.hvt_project.infra.AOP.CacheTimer
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

    @GetMapping("/filtered")
    fun getFilteredStoreList(
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<List<StoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredStoreList(rating, status))
    }

    @GetMapping("/filtered/simple")
    fun getFilteredSimpleStoreList(
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<List<SimpleStoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredSimpleStoreList(rating, status))
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


    @GetMapping("/pagenated/simple")
    fun getFilteredSimpleStorePage(
        @PageableDefault(size = 10) pageable: Pageable,
        @RequestParam(value = "cursorId", required = false) cursorId: Long?,
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<Page<SimpleStoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredSimpleStorePage(pageable, cursorId, rating, status))
    }

    @CacheTimer
    @GetMapping() // 업체 리스트 전체 조회
    // @Cacheable("storeListCache")
    fun <T> getStores(
        @PageableDefault( size = 10, sort = ["id"]) pageable: Pageable,
        toSimple : Boolean // Projection 적용 여부
    ) : ResponseEntity<Page<T>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreList(pageable, toSimple))
    }

    @CacheTimer
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

    @CacheTimer
    @GetMapping("/news")
    fun getNewStores(
        @RequestParam(value = "Size", required = false) size : Long,
    ) : ResponseEntity<List<StoreResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getNewStores(size))
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
        storeService.deleteStore(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}
