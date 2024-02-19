package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.CreateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.StoreResponse
import com.jansparta.hvt_project.domain.store.dto.UpdateStoreRequest
import com.jansparta.hvt_project.domain.store.dto.toResponse
import com.jansparta.hvt_project.domain.store.repository.StoreRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import com.jansparta.hvt_project.domain.store.dto.*
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.domain.store.repository.StoreRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,

) : StoreService {

    companion object {
        const val EXPECTED_FIELD_COUNT = 32 // CSV 파일의 각 줄이 가지는 필드 개수
    }
    override fun readCsvFile() {
        val file = File("C:\\csv\\file.csv")

        this.getStoresFromCSV(file)
    }

    override fun getStoresFromCSV(file: File) {
        if (!file.exists()) {
            throw FileNotFoundException("파일을 찾을 수 없습니다.")
        }

        val lines = file.readLines()

        lines.forEach { line ->
            try {
                // 따옴표로 묶인 필드를 올바르게 처리하는 정규식
                val regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()  // 따옴표로 묶인 필드를 올바르게 처리하는 정규식
                val data = regex.split(line).map { it.trim('\"') }
                // 파일 포맷 검증: 필드 개수 확인
                if (data.size != EXPECTED_FIELD_COUNT) {
                    throw IllegalArgumentException("잘못된 데이터 포맷입니다: $line")
                }

                val store = Store(
                    company = data[0].ifEmpty { null },
                    shopName = data[1].ifEmpty { null },
                    domainName = data[2].ifEmpty { null },
                    tel = data[3].ifEmpty { null },
                    email = data[4].ifEmpty { null },
                    upjongNbr = data[5].ifEmpty { null },
                    ypForm = data[6].ifEmpty { null },
                    firstHeoDate = LocalDate.parse(data[7], DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString(),
                    comAddr = data[8].ifEmpty { null },
                    statNm = StatNmStatus.fromString(data[9]),
                    totRatingPoint = data[10].toIntOrNull(),
                    chogiRatingPoint = data[11].toIntOrNull(),
                    chungRatingPoint = data[12].toIntOrNull(),
                    dealRatingPoint = data[13].toIntOrNull(),
                    pyojunRatingPoint = data[14].toIntOrNull(),
                    securityRatingPoint = data[15].toIntOrNull(),
                    service = data[16].ifEmpty { null },
                    chung = data[17].ifEmpty { null },
                    chogi = data[18].ifEmpty { null },
                    gyulje = data[19].ifEmpty { null },
                    pyojun = data[20].ifEmpty { null },
                    pInfoCare = data[21].ifEmpty { null },
                    perInfo = data[22].ifEmpty { null },
                    dealCare = data[23].ifEmpty { null },
                    sslYn = data[24].ifEmpty { null },
                    injeung = data[25].ifEmpty { null },
                    baesongYejeong = data[26].ifEmpty { null },
                    baesong = data[27].ifEmpty { null },
                    clientBbs = data[28].ifEmpty { null },
                    leave = data[29].ifEmpty { null },
                    kaesolYear = data[30].ifEmpty { null },
                    regDate = LocalDate.parse(data[31], DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
                )

                storeRepository.save(store)
            } catch (e: Exception) {
                println("데이터 저장 중 에러가 발생했습니다: $line")
                e.printStackTrace()
            }
        }
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
                statNm = StatNmStatus.fromString(request.statNm),
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

        store.apply {
            company = request.company
            shopName = request.shopName
            domainName = request.domainName
            tel = request.tel
            email = request.email
            upjongNbr = request.upjongNbr
            ypForm = request.ypForm
            firstHeoDate = request.firstHeoDate
            comAddr = request.comAddr
            statNm = StatNmStatus.fromString(request.statNm)
            totRatingPoint = request.totRatingPoint
            chogiRatingPoint = request.chogiRatingPoint
            chungRatingPoint = request.chungRatingPoint
            dealRatingPoint = request.dealRatingPoint
            pyojunRatingPoint = request.pyojunRatingPoint
            securityRatingPoint = request.securityRatingPoint
            service = request.service
            chung = request.chung
            chogi = request.chogi
            gyulje = request.gyulje
            pyojun = request.pyojun
            pInfoCare = request.pInfoCare
            perInfo = request.perInfo
            dealCare = request.dealCare
            sslYn = request.sslYn
            injeung = request.injeung
            baesongYejeong = request.baesongYejeong
            baesong = request.baesong
            clientBbs = request.clientBbs
            leave = request.leave
            kaesolYear = request.kaesolYear
            regDate = request.regDate
        }

        return storeRepository.save(store).toResponse()
    }

    override fun <T> getStoreList( pageable: Pageable, toSimple:Boolean) : Page<T> {

         return if(toSimple){
             storeRepository.getStores(pageable, SimpleStore::class.java)?.map{it.toResponse()} as Page<T>
         }
        else {
             storeRepository.getStores(pageable, Store::class.java)?.map{it.toResponse()} as Page<T>
         }
    }

    override fun getFilteredStoreList(rating: Int?, status: String?): List<StoreResponse> {
        return storeRepository.findByRatingAndStatus(rating, status).map { it.toResponse() }
    }

    override fun getFilteredStorePage(
        pageable: Pageable,
        cursorId: Long?,
        rating: Int?,
        status: String?
    ): Page<StoreResponse> {
        return storeRepository.findByPageableAndFilter(pageable, cursorId, rating, status).map { it.toResponse() }
    }

    override fun getFilteredSimpleStore() {
        TODO("Not yet implemented")
    }

    override fun getStoreBy(id: Long?, company: String?, shopName: String?, tel: String?): StoreResponse {
        if(id == null && company == null && shopName == null && tel == null)
            throw NotFoundException()

        return storeRepository.getStoreBy(id, company, shopName, tel).toResponse()
    }
}
