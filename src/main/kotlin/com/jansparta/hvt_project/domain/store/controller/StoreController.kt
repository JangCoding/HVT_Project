package com.jansparta.hvt_project.domain.store.controller

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.service.StoreService
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