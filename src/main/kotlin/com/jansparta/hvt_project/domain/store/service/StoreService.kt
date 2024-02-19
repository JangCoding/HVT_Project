package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.model.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import java.io.File

interface StoreService {

    fun readCsvFile()

    fun getStoresFromCSV(file: File)

    fun createStore( request : CreateStoreRequest ) : StoreResponse

    fun updateStore( request : UpdateStoreRequest , id : Long) : StoreResponse

    fun getFilteredStoreList(rating: Int?, status: String?) : List<StoreResponse>

    fun getFilteredStorePage(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<StoreResponse>

    fun <T> getStoreList( pageable: Pageable, toSimple:Boolean) : Page<T>

    fun getFilteredSimpleStore()

    fun getStoreBy( id : Long? , company : String? , shopName : String? , tel : String? ) : StoreResponse

    fun deleteStore( id:Long )
}