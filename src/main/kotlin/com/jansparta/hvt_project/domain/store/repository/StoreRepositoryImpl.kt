package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.QStore
import com.jansparta.hvt_project.domain.store.model.SimpleStore
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

    override fun getStoreBy(id: Long?, company: String?, shopName: String?, tel: String?): Store {

        var whereClause = BooleanBuilder()
        id?.let { whereClause.and(store.id.eq(id)) }
        company?.let { whereClause.and(store.company.eq(company)) }
        shopName?.let { whereClause.and(store.shopName.eq(shopName)) }
        tel?.let { whereClause.and(store.tel.eq(tel)) }

        return queryFactory
            .selectFrom(store)
            .where(whereClause)
            .fetchOne() ?: throw NotFoundException()
    }

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
}

// 제네릭 메서드로 통합
//    fun getPagedStores(pageable: Pageable): Page<Store> {
//        val totalCounts = queryFactory
//            .select(store.count())
//            .from(store)
//            .fetchOne()
//            ?:0L
//
//        val contents = queryFactory
//            .selectFrom(store)
//            .offset(pageable.offset)
//            .limit(pageable.pageSize.toLong())
//            .orderBy(store.id.desc())
//            .fetch()
//
//        return PageImpl(contents, pageable, totalCounts )
//    }
//
//    override fun getPagedSimpleStores(pageable: Pageable): Page<SimpleStore> {
//        val totalCounts = queryFactory
//            .select(store.count())
//            .from(store)
//            .fetchOne()
//            ?:0L
//
//        val contents = queryFactory
//            .select(
//                Projections.constructor(
//                    SimpleStore::class.java,
//                    store.id,
//                    store.company,
//                    store.shopName,
//                    store.domainName,
//                    store.tel,
//                    store.email,
//                    store.ypForm,
//                    store.comAddr ,
//                    store.statNm
//                )
//            )
//            .from(store)
//            .offset(pageable.offset)
//            .limit(pageable.pageSize.toLong())
//            .orderBy(store.id.desc())
//            .fetch()
//
//        return PageImpl(contents, pageable, totalCounts )
//    }
