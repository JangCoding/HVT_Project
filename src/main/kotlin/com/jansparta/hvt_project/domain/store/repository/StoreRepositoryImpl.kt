package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.QStore
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.stereotype.Repository

@Repository
class StoreRepositoryImpl : CustomStoreRepository, QueryDslSupport() {
    //QueryDSL 구현부

    private val store = QStore.store

    override fun findByRatingAndStatus(rating: Int?, status: String?): List<Store> {
        val whereClause = BooleanBuilder()

        rating?.let { whereClause.and(store.totRatingPoint.eq(it)) }
        status?.let { whereClause.and(store.statNm.eq(it)) }

        return queryFactory.selectFrom(store)
            .where(whereClause)
            .orderBy(store.regDate.desc())
            .fetch()
    }
}