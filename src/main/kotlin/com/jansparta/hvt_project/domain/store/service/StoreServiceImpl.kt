package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.toResponse
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.domain.store.repository.StoreRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,

) : StoreService {

    override fun getStoresFromCSV() {
        TODO("Not yet implemented")
    }

    override fun createStore(request: CreateStoreRequest): StoreResponse {

        if(storeRepository.existsByCompany(request.company)){
            throw DataIntegrityViolationException("Company name is already in use")
        }

        return storeRepository.save(
            Store(
                company = request.company,
                shopName = request.shopName,
                domainName = request.domainName,
                tel = request.tel,
                email = request.email,
                upjongNbr = request.upjongNbr,
                ypForm = request.ypForm,
                firstHeoDate = request.firstHeoDate,
                comAddr = request.comAddr,
                statNm = request.statNm,
                totRatingPoint = request.totRatingPoint,
                chogiRatingPoint = request.chogiRatingPoint,
                chungRatingPoint = request.chungRatingPoint,
                dealRatingPoint = request.dealRatingPoint,
                pyojunRatingPoint = request.pyojunRatingPoint,
                securityRatingPoint = request.securityRatingPoint,
                service = request.service,
                chung = request.chung,
                chogi = request.chogi,
                gyulje = request.gyulje,
                pyojun = request.pyojun,
                pInfoCare = request.pInfoCare,
                perInfo = request.perInfo,
                dealCare = request.dealCare,
                sslYn = request.sslYn,
                injeung = request.injeung,
                baesongYejeong = request.baesongYejeong,
                baesong = request.baesong,
                clientBbs = request.clientBbs,
                leave = request.leave,
                kaesolYear = request.kaesolYear,
                regDate = request.regDate
            )).toResponse()
    }

    override fun updateStore(request: UpdateStoreRequest, id:Long): StoreResponse {
        var store = storeRepository.findByIdOrNull(id)
            ?: throw NotFoundException()

        store = Store(
            company = request.company,
            shopName = request.shopName,
            domainName = request.domainName,
            tel = request.tel,
            email = request.email,
            upjongNbr = request.upjongNbr,
            ypForm = request.ypForm,
            firstHeoDate = request.firstHeoDate,
            comAddr = request.comAddr,
            statNm = request.statNm,
            totRatingPoint = request.totRatingPoint,
            chogiRatingPoint = request.chogiRatingPoint,
            chungRatingPoint = request.chungRatingPoint,
            dealRatingPoint = request.dealRatingPoint,
            pyojunRatingPoint = request.pyojunRatingPoint,
            securityRatingPoint = request.securityRatingPoint,
            service = request.service,
            chung = request.chung,
            chogi = request.chogi,
            gyulje = request.gyulje,
            pyojun = request.pyojun,
            pInfoCare = request.pInfoCare,
            perInfo = request.perInfo,
            dealCare = request.dealCare,
            sslYn = request.sslYn,
            injeung = request.injeung,
            baesongYejeong = request.baesongYejeong,
            baesong = request.baesong,
            clientBbs = request.clientBbs,
            leave = request.leave,
            kaesolYear = request.kaesolYear,
            regDate = request.regDate
        )

        return storeRepository.save(store).toResponse()
    }

    override fun getAllStores(
        pageable: Pageable
    ) : Page<StoreResponse> {



        TODO("Not yet implemented")
    }

    override fun getAllSimpleStores() {
        TODO("Not yet implemented")
    }

    override fun getFilteredStores() {
        TODO("Not yet implemented")
    }

    override fun getFilteredSimpleStore() {
        TODO("Not yet implemented")
    }

    override fun getStoreById(id: Long): StoreResponse {
        TODO("Not yet implemented")
    }
}