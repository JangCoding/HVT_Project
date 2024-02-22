package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.Store
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<Store, Long>, CustomStoreRepository {
    //쿼리메서드 작성
    fun findTopByCompanyAndShopNameAndDomainName(company: String, shopName: String, domainName: String): Store?
    fun existsByCompany(company : String) : Boolean
}
