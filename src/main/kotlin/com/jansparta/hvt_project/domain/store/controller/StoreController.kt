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
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/stores")
class StoreController (
    private val storeService : StoreService
){
    // 매일 정오에 데이터를 가져와 저장하는 스케줄링 함수
    @Scheduled(cron = "0 0 12 * * ?")// cron 표현식으로 매일 정오에 작업 수행 설정
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/collection-openapi")
    fun fetchDataAndStore(): ResponseEntity<String> {
        // 데이터를 가져와 저장하는 작업 수행
        storeService.fetchDataAndStore()
        // 작업이 성공적으로 완료되면 HTTP 상태 코드 200과 메시지를 반환
        return ResponseEntity.ok("데이터를 성공적으로 가져왔습니다.")
    }


    // CSV 파일을 업로드해서 쇼핑몰 정보를 가져오는 함수
    @PostMapping("/collection") // 업체 리스트 csv 불러오기
    @PreAuthorize("hasRole('ADMIN')")
    fun getStoresFromCSV(@RequestParam("file")multipartFile: MultipartFile)
    {
        // 임시 파일 생성
        val tempFile = File.createTempFile("temp", null)
        try {
            // 업로드된 파일을 임시 파일로 복사
            multipartFile.transferTo(tempFile)
            // 임시 파일로부터 쇼핑몰 정보를 가져오는 작업 수행
            storeService.getStoresFromCSV(tempFile)
        } finally {
            // 파일 처리가 끝난 후에 임시 파일 삭제
            tempFile.delete() 
        }
    }

    @PostMapping("/create") // 업체 등록 . 상호명으로 중복 판단
    @PreAuthorize("hasRole('ADMIN')")
    fun createStore(
        @RequestBody request : CreateStoreRequest
    ) : ResponseEntity<StoreResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.createStore(request))
    }

    @GetMapping("/filtered") // 필터 적용 리스트 조회

    fun getFilteredStoreList(
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<List<StoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredStoreList(rating, status))
    }

    @GetMapping("/filtered/simple") // 필터 적용 리스트 Projection 조회
    fun getFilteredSimpleStoreList(
        @RequestParam(value = "rating", required = false) rating:Int?,
        @RequestParam(value = "status", required = false) status:String?
    ) : ResponseEntity<List<SimpleStoreResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getFilteredSimpleStoreList(rating, status))
    }

    @GetMapping("/pagenated") // 필터 적용 페이지 조회
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

    @GetMapping("/pagenated/simple") // 필터 적용 페이지 Projection 조회
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

    @GetMapping() // 업체 리스트 전체 조회
    fun <T> getStores(
        @PageableDefault( size = 10, sort = ["id"]) pageable: Pageable,
        toSimple : Boolean // Projection 적용 여부
    ) : ResponseEntity<Page<T>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreList(pageable, toSimple))
    }

    @CacheTimer
    @GetMapping("/{id}") // 업체 단건 조회
    fun getStoreById(
        @PathVariable id : Long,
    ) : ResponseEntity<StoreResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreById(id))
    }
    @CacheTimer
    @GetMapping("/search") // 업체 검색. 전문 검색 엔진 필요 (ElasticSearch 등)
    fun searchStoresBy(
        @RequestParam(value = "Id(아이디)", required = false) id : Long?,
        @RequestParam(value = "Company(상호명)", required = false) company : String?,
        @RequestParam(value = "shopName(쇼핑몰명)", required = false) shopName : String?,
        @RequestParam(value = "tel(전화번호)", required = false) tel : String?,
    ) : ResponseEntity<List<StoreResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.searchStoresBy(id,company,shopName,tel))
    }

    @CacheTimer
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/news")
    fun getNewStores(
        @RequestParam(value = "Size", required = false) size : Long,
    ) : ResponseEntity<List<StoreResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getNewStores(size))
    }
    @PutMapping("/update/{id}") // 업체 수정
    @PreAuthorize("hasRole('ADMIN')")
    fun updateStore(
        @RequestBody request : UpdateStoreRequest,
        @PathVariable id : Long,
    ) : ResponseEntity<StoreResponse>
    {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.updateStore(request, id))
    }

    @DeleteMapping("/delete/{id}") // 업체 삭제
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteStore(
        @PathVariable id:Long
    ) : ResponseEntity<Unit>
    {
        storeService.deleteStore(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}
