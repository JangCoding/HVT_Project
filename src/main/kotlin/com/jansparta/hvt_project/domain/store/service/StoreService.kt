package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.io.File

interface StoreService {
    fun readCsvFile()
    fun getStoresFromCSV(file: File)
    fun createStore( request : CreateStoreRequest ) : StoreResponse
    fun getFilteredStoreList(rating: Int?, status: String?) : List<StoreResponse>
    fun getFilteredSimpleStoreList(rating: Int?, status: String?) : List<SimpleStoreResponse>
    fun getFilteredStorePage(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<StoreResponse>
    fun getFilteredSimpleStorePage(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<SimpleStoreResponse>
    fun <T> getStoreList( pageable: Pageable, toSimple:Boolean) : Page<T>

    fun getStoreById( id : Long) : StoreResponse
    fun searchStoresBy( id : Long? , company : String? , shopName : String? , tel : String? ) : List<StoreResponse>
    fun getNewStores( size : Long ) : List<StoreResponse>
    fun updateStore( request : UpdateStoreRequest , id : Long) : StoreResponse
    fun deleteStore( id:Long )
}