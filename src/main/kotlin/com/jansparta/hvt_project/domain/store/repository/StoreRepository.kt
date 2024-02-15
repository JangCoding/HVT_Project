package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.Store
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<Store, Long> {

}