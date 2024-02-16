package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.QStore
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
            .limit(10)
            .fetch()
    }

    override fun findByPageableAndFilter(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<Store> {
        val whereClause = BooleanBuilder()

        rating?.let { whereClause.and(store.totRatingPoint.eq(it)) }
        status?.let { whereClause.and(store.statNm.eq(it)) }
        cursorId?.let { whereClause.and(store.id.lt(it)) } // desc

        val totalCount = queryFactory.select(store.count()).from(store).where(whereClause).fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(store)
            .where(whereClause)
            .limit(pageable.pageSize.toLong())
            .orderBy(store.id.desc())
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}