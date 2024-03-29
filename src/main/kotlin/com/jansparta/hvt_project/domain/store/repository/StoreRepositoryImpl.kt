package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.dto.SimpleStoreResponse
import com.jansparta.hvt_project.domain.store.model.QSimpleStore
import com.jansparta.hvt_project.domain.store.model.QStore
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.StatNmStatus
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class StoreRepositoryImpl : CustomStoreRepository, QueryDslSupport() {
    //QueryDSL 구현부

    private val store = QStore.store

    override fun getNewStores(size : Long): List<Store> {
        return queryFactory
            .selectFrom(store)
            .orderBy(store.id.desc())
            .limit(size)
            .fetch()
    }

    override fun <T> getStores(pageable: Pageable, type: Class<T>): Page<T>? {
        val totalCounts = queryFactory
            .select(store.count())
            .from(store)
            .fetchOne()
            ?:0L

        val query = if (type == SimpleStore::class.java) {
            queryFactory.select(
                Projections.constructor(
                    SimpleStore::class.java,
                    store.id,
                    store.company,
                    store.shopName,
                    store.domainName,
                    store.tel,
                    store.email,
                    store.ypForm,
                    store.comAddr,
                    store.statNm
                )
            )
        } else {
            queryFactory.selectFrom(store)
        }

        val contents = query
            .from(store)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(store.id.desc())
            .fetch() as List<T>

        return PageImpl(contents, pageable, totalCounts)
    }

    override fun getStoreBy(id: Long): Store {

        var whereClause = BooleanBuilder()
        whereClause.and(store.id.eq(id))

        return queryFactory
            .selectFrom(store)
            .where(whereClause)
            .limit(1)
            .fetchOne()?: throw NotFoundException()
    }
    override fun searchStoresBy(company: String?, shopName: String?, tel: String?): List<Store> {

        val whereClause = BooleanBuilder()
        company?.let { whereClause.and(store.company.eq(company)) }
        shopName?.let { whereClause.and(store.shopName.like("%$shopName%")) } // 응답시간 초과. 전문검색엔진 필요
        tel?.let { whereClause.and(store.tel.like("%$tel%")) }

        return queryFactory
            .selectFrom(store)
            .where(whereClause)
            .fetch() ?: emptyList()
    }

    override fun findByRatingAndStatus(rating: Int?, status: String?): List<Store> {
        val whereClause = BooleanBuilder()

        rating?.let { whereClause.and(store.totRatingPoint.eq(it)) }
        status?.let { whereClause.and(store.statNm.eq(StatNmStatus.fromString(it))) }

        return queryFactory.selectFrom(store)
            .where(whereClause)
            .orderBy(store.regDate.desc())
            .limit(10)
            .fetch()
    }

    override fun findSimpleByRatingAndStatus(rating: Int?, status: String?): List<SimpleStore> {
        val whereClause = BooleanBuilder()

        rating?.let { whereClause.and(store.totRatingPoint.eq(it)) }
        status?.let { whereClause.and(store.statNm.eq(StatNmStatus.fromString(it))) }

        return queryFactory
            .select(
                Projections.constructor(
                    SimpleStore::class.java,
                    store.id,
                    store.company,
                    store.shopName,
                    store.domainName,
                    store.tel,
                    store.email,
                    store.ypForm,
                    store.comAddr,
                    store.statNm
                )
            )
            .from(store)
            .where(whereClause)
            .orderBy(store.regDate.desc())
            .limit(10)
            .fetch()
    }

    override fun findByPageableAndFilter(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<Store> {
        val whereClause = BooleanBuilder()

        rating?.let { whereClause.and(store.totRatingPoint.eq(it)) }
        status?.let { whereClause.and(store.statNm.eq(StatNmStatus.fromString(it))) }
        cursorId?.let { whereClause.and(store.id.lt(it)) } // desc

        val totalCount = queryFactory.select(store.count()).from(store).where(whereClause).fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(store)
            .where(whereClause)
            .limit(pageable.pageSize.toLong())
            .orderBy(store.id.desc())
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    override fun findSimpleByPageableAndFilter(pageable: Pageable, cursorId: Long?, rating: Int?, status: String?): Page<SimpleStore> {
        val whereClause = BooleanBuilder()

        rating?.let { whereClause.and(store.totRatingPoint.eq(it)) }
        status?.let { whereClause.and(store.statNm.eq(StatNmStatus.fromString(it))) }
        cursorId?.let { whereClause.and(store.id.lt(it)) } // desc

        val totalCount = queryFactory.select(store.count()).from(store).where(whereClause).fetchOne() ?: 0L

        val contents = queryFactory
            .select(
                QSimpleStore(
                    store.id,
                    store.company,
                    store.shopName,
                    store.domainName,
                    store.tel,
                    store.email,
                    store.ypForm,
                    store.comAddr,
                    store.statNm
                )
            )
            .from(store)
            .where(whereClause)
            .limit(pageable.pageSize.toLong())
            .orderBy(store.id.desc())
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}
