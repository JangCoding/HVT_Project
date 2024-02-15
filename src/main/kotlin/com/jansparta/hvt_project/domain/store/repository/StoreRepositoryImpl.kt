package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.QStore
import com.jansparta.hvt_project.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class StoreRepositoryImpl : CustomStoreRepository, QueryDslSupport() {
    //QueryDSL 구현부

    private val store = QStore.store

}