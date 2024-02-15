package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.QStore
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.infra.querydsl.QueryDslSupport
import com.querydsl.core.types.Projections
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
}


//    override fun getPagedStores(pageable: Pageable): Page<Store> {
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


