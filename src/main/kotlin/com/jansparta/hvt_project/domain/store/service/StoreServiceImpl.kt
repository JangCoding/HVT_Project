package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.*
import com.jansparta.hvt_project.domain.store.repository.StoreRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.StatNmStatus
import com.jansparta.hvt_project.domain.store.model.Store
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,
    private val redisTemplate: RedisTemplate<String, StoreResponse>,

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
        val stores = mutableListOf<Store>()
        lines.forEachIndexed {index, line ->
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
                stores.add(store)

                if ((index + 1) % 100 == 0) {
                    storeRepository.saveAll(stores)
                    stores.clear()
                }
            } catch (e: Exception) {
                println("데이터 저장 중 에러가 발생했습니다: $line")
                e.printStackTrace()
            }
        }
        if (stores.isNotEmpty()) {
            storeRepository.saveAll(stores)
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

    override fun getFilteredStoreList(rating: Int?, status: String?): List<StoreResponse> {
        checkFilterArgument(rating, status)
        return storeRepository.findByRatingAndStatus(rating, status).map { it.toResponse() }
    }

    override fun getFilteredSimpleStoreList(rating: Int?, status: String?): List<SimpleStoreResponse> {
        checkFilterArgument(rating, status)
        return storeRepository.findSimpleByRatingAndStatus(rating, status).map { it.toResponse() }
    }

    override fun getFilteredStorePage(
        pageable: Pageable,
        cursorId: Long?,
        rating: Int?,
        status: String?
    ): Page<StoreResponse> {
        checkFilterArgument(rating, status)
        return storeRepository.findByPageableAndFilter(pageable, cursorId, rating, status).map { it.toResponse() }
    }

    override fun getFilteredSimpleStorePage(
        pageable: Pageable,
        cursorId: Long?,
        rating: Int?,
        status: String?
    ): Page<SimpleStoreResponse> {
        checkFilterArgument(rating, status)
        return storeRepository.findSimpleByPageableAndFilter(pageable, cursorId, rating, status).map { it.toResponse() }
    }

    fun checkFilterArgument(rating: Int?, status: String?) {
        rating?.let {
            if(it !in 0..3 ) throw IllegalArgumentException("전체평가는 0~3 사이의 값을 입력해야 합니다")
        }
        status?.let {
            if(StatNmStatus.fromString(it) !in StatNmStatus.values()) throw IllegalArgumentException("업소상태에 대해 유효하지 않은 입력값입니다")
        }
    }

    @Cacheable("PagedStoreCache", key = "{#pageable.pageNumber, #pageable.pageSize, #toSimple }", cacheManager = "defaultCacheManager")
    override fun <T> getStoreList( pageable: Pageable, toSimple:Boolean) : Page<T> {

        return if(toSimple){
            storeRepository.getStores(pageable, SimpleStore::class.java)?.map{it.toResponse()} as Page<T>
        }
        else {
            storeRepository.getStores(pageable, Store::class.java)?.map{it.toResponse()} as Page<T>
        }
    }
    //@Cacheable("storeCache", key = "{#id}")
    override fun getStoreBy(id: Long?, company: String?, shopName: String?, tel: String?): StoreResponse {
        val logger = LoggerFactory.getLogger(StoreServiceImpl::class.java)


        if(id == null && company == null && shopName == null && tel == null)
            throw NotFoundException()

        // 레디스 템플릿에서 겹치는 키가 있는지 확인
        val key = "storeCache::$id:$company:$shopName:$tel"

        val cachedStore = redisTemplate.opsForValue().get(key)

        if (cachedStore != null) {
            logger.info("-------------".repeat(10))
            logger.info("Cache hit for key: {}", key)
            logger.info("cachedStore : {}", cachedStore)
            logger.info("-------------".repeat(10))
            return cachedStore
        }

        logger.info("-------------".repeat(10))
        logger.info("Cache miss for key: {}", key)
        logger.info("-------------".repeat(10))
        return storeRepository.getStoreBy(id, company, shopName, tel).toResponse().also { it ->
            redisTemplate.opsForValue().set("storeCache::${it.id}:${it.company}:${it.shopName}:${it.tel}", it)
        }

    }

    override fun getNewStores(size: Long): List<StoreResponse> {
        val storeList = storeRepository.getNewStores(size).map{it.toResponse()}

        //개선필요
        storeList.forEach{
            redisTemplate.opsForValue().set("storeCache::${it.id}:${it.company}:${it.shopName}:${it.tel}", it)
        }
        return storeList
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
    override fun deleteStore(id: Long) {
        val store = storeRepository.findByIdOrNull(id)
            ?:throw NotFoundException()

        store.isDeleted = false

        storeRepository.delete(store)

    }
}
