package com.jansparta.hvt_project.domain.store.service

import com.jansparta.hvt_project.domain.store.dto.*
import com.jansparta.hvt_project.domain.store.model.SimpleStore
import com.jansparta.hvt_project.domain.store.model.StatNmStatus
import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.domain.store.repository.StoreRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.w3c.dom.Element
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,
    private val redisTemplate: RedisTemplate<String, StoreResponse>,

) : StoreService {
    private val KEY = "6c66575174726c6136306f73446167" // OpenAPI 인증키
    private val baseUrl = "http://openapi.seoul.go.kr:8088/$KEY/xml/ServiceInternetShopInfo"
    private val recordPerPage = 1000 // 한 페이지 당 나타내는 데이터 개수

    //XML 문서의 특정 Element 에서 특정 태그의 값을 가져오는 역할
    private fun getTagValue(element: Element, tagName: String): String? {
        val nodeList = element.getElementsByTagName(tagName) // Element에서 주어진 태그 이름에 해당하는 모든 노드를 NodeList 형태로 가져옴
        // 태그가 해당 Element 내에 존재하는지를 확인
        if (nodeList.length > 0) {
            val node = nodeList.item(0)
            return node?.textContent //첫 번째 노드의 내용을 반환
        }
        return null
    }
    // DocumentBuilder를 이용하여 XML 형태의 데이터를 파싱
    override fun fetchDataAndStore() {
        // DocumentBuilderFactory 인스턴스 생성
        val factory = DocumentBuilderFactory.newInstance()
        // DocumentBuilder 인스턴스 생성
        val builder = factory.newDocumentBuilder()
        // API의 첫 페이지에서 데이터를 가져와 파싱
        val initialDoc = builder.parse("$baseUrl/1/$recordPerPage")
        // 전체 아이템 수를 가져옴
        val totalItemCount = initialDoc.getElementsByTagName("list_total_count").item(0).textContent.toInt()
        // 전체 페이지 수 계산 (올림 처리)
        val totalPage = (totalItemCount + recordPerPage - 1) / recordPerPage
        // Store 객체를 담을 리스트 생성
        val stores = mutableListOf<Store>()
        // 각 페이지에 대해 수행
        for (page in 1..totalPage) {
            // 시작 인덱스와 끝 인덱스 계산
            val startIndex = (page - 1) * recordPerPage + 1
            val endIndex = page * recordPerPage
            // 해당 페이지의 데이터를 가져오는 URL 생성
            val url = "$baseUrl/$startIndex/$endIndex"
            // URL에서 데이터를 가져와 파싱
            val doc = builder.parse(url)
            // "row" 태그를 가진 모든 Element를 가져옴
            val items = doc.getElementsByTagName("row")

            // 각 Element에 대해 수행
            for (i in 0 until items.length) {
                // Element를 item 변수에 저장
                val item = items.item(i) as Element
                // 각 태그의 값을 가져옴
                val company = getTagValue(item, "COMPANY")
                val shopName = getTagValue(item, "SHOP_NAME")
                val domainName = getTagValue(item, "DOMAIN_NAME")
                // 회사명, 쇼핑몰 이름, 도메인 이름을 기준으로 가장 최근에 추가된 쇼핑몰을 찾음
                val existingStore = company?.let { comp ->
                    shopName?.let { shop ->
                        domainName?.let { domain ->
                            storeRepository.findTopByCompanyAndShopNameAndDomainName(comp, shop, domain)
                        }
                    }
                }
                // 기존에 존재하는 쇼핑몰이 있으면 정보를 업데이트
                val store = existingStore?.apply {
                        tel = getTagValue(item, "TEL")
                        email = getTagValue(item, "EMAIL")
                        upjongNbr = getTagValue(item, "UPJONG_NBR")
                        ypForm = getTagValue(item, "YPFORM")
                        firstHeoDate = getTagValue(item, "FIRST_HEO_DATE")?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() }
                        comAddr = getTagValue(item, "COM_ADDR")
                        statNm = StatNmStatus.fromString( getTagValue(item, "STAT_NM")!! )
                        totRatingPoint = getTagValue(item, "TOT_RATINGPOINT")?.toIntOrNull()
                        chogiRatingPoint = getTagValue(item, "CHOGI_RATINGPOINT")?.toIntOrNull()
                        chungRatingPoint = getTagValue(item, "CHUNG_RATINGPOINT")?.toIntOrNull()
                        dealRatingPoint = getTagValue(item, "DEAL_RATINGPOINT")?.toIntOrNull()
                        pyojunRatingPoint = getTagValue(item, "PYOJUN_RATINGPOINT")?.toIntOrNull()
                        securityRatingPoint = getTagValue(item, "SECURITY_RATINGPOINT")?.toIntOrNull()
                        service = getTagValue(item, "SERVICE")
                        chung = getTagValue(item, "CHUNG")
                        chogi = getTagValue(item, "CHOGI")
                        gyulje = getTagValue(item, "GYULJE")
                        pyojun = getTagValue(item, "PYOJUN")
                        pInfoCare = getTagValue(item, "P_INFO_CARE")
                        perInfo = getTagValue(item, "PER_INFO")
                        dealCare = getTagValue(item, "DEAL_CARE")
                        sslYn = getTagValue(item, "SSL_YN")
                        injeung = getTagValue(item, "INJEUNG")
                        baesongYejeong = getTagValue(item, "BAESONG_YEJEONG")
                        baesong = getTagValue(item, "BAESONG")
                        clientBbs = getTagValue(item, "CLIENT_BBS")
                        leave = getTagValue(item, "LEAVE")
                        kaesolYear = getTagValue(item, "KAESOL_YEAR")
                        regDate = getTagValue(item, "REG_DATE")?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() }
                    }
                    //없으면 새로운 Store 객체를 생성
                    ?: Store(
                        company = company,
                        shopName = shopName,
                        domainName = domainName,
                        tel = getTagValue(item, "TEL"),
                        email = getTagValue(item, "EMAIL"),
                        upjongNbr = getTagValue(item, "UPJONG_NBR"),
                        ypForm = getTagValue(item, "YPFORM"),
                        firstHeoDate = getTagValue(item, "FIRST_HEO_DATE")?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() },
                        comAddr = getTagValue(item, "COM_ADDR"),
                        statNm = StatNmStatus.fromString( getTagValue(item, "STAT_NM")!! ),
                        totRatingPoint = getTagValue(item, "TOT_RATINGPOINT")?.toIntOrNull(),
                        chogiRatingPoint = getTagValue(item, "CHOGI_RATINGPOINT")?.toIntOrNull(),
                        chungRatingPoint = getTagValue(item, "CHUNG_RATINGPOINT")?.toIntOrNull(),
                        dealRatingPoint = getTagValue(item, "DEAL_RATINGPOINT")?.toIntOrNull(),
                        pyojunRatingPoint = getTagValue(item, "PYOJUN_RATINGPOINT")?.toIntOrNull(),
                        securityRatingPoint = getTagValue(item, "SECURITY_RATINGPOINT")?.toIntOrNull(),
                        service = getTagValue(item, "SERVICE"),
                        chung = getTagValue(item, "CHUNG"),
                        chogi = getTagValue(item, "CHOGI"),
                        gyulje = getTagValue(item, "GYULJE"),
                        pyojun = getTagValue(item, "PYOJUN"),
                        pInfoCare = getTagValue(item, "P_INFO_CARE"),
                        perInfo = getTagValue(item, "PER_INFO"),
                        dealCare = getTagValue(item, "DEAL_CARE"),
                        sslYn = getTagValue(item, "SSL_YN"),
                        injeung = getTagValue(item, "INJEUNG"),
                        baesongYejeong = getTagValue(item, "BAESONG_YEJEONG"),
                        baesong = getTagValue(item, "BAESONG"),
                        clientBbs = getTagValue(item, "CLIENT_BBS"),
                        leave = getTagValue(item, "LEAVE"),
                        kaesolYear = getTagValue(item, "KAESOL_YEAR"),
                        regDate = getTagValue(item, "REG_DATE")?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() }
                    )
                // Store 객체를 리스트에 추가
                stores.add(store)
                // 리스트의 크기가 100이면 데이터베이스에 저장하고 리스트를 비움
                if (stores.size == 100) {
                    storeRepository.saveAll(stores)
                    stores.clear()
                }
            }
        }
        // 모든 페이지를 돌고 난 후, 리스트에 남아있는 Store 객체가 있으면 데이터베이스에 저장
        if (stores.isNotEmpty()) {
            storeRepository.saveAll(stores)
        }
    }

    // CSV 파일을 읽어서 Store 객체를 생성하고 데이터베이스에 저장하는 클래스
    companion object {
        const val EXPECTED_FIELD_COUNT = 32 // CSV 파일의 각 줄이 가지는 필드 개수
        val logger = LoggerFactory.getLogger(StoreServiceImpl::class.java)
    }
    // CSV 파일을 읽는 함수
    override fun readCsvFile() {
        // CSV 파일 경로
        val file = File("C:\\csv\\file.csv")
        // CSV 파일로부터 Store 객체를 생성하는 함수 호출
        this.getStoresFromCSV(file)
    }
    // CSV 파일로부터 Store 객체를 생성하는 함수
    override fun getStoresFromCSV(file: File) {
        // 파일이 존재하지 않으면 FileNotFoundException 발생
        if (!file.exists()) {
            throw FileNotFoundException("파일을 찾을 수 없습니다.")
        }
        // 파일의 모든 줄을 읽음
        val lines = file.readLines()
        // Store 객체를 저장할 리스트 생성
        val stores = mutableListOf<Store>()
        // 각 줄에 대해 수행
        lines.forEachIndexed {index, line ->
            try {
                // 따옴표로 묶인 필드를 올바르게 처리하는 정규식
                val regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()
                // 정규식을 이용해 줄을 필드별로 나눔
                val data = regex.split(line).map { it.trim('\"') }
                // 파일 포맷 검증: 필드 개수 확인
                if (data.size != EXPECTED_FIELD_COUNT) {
                    throw IllegalArgumentException("잘못된 데이터 포맷입니다: $line")
                }
                // 각 필드의 값을 이용해 Store 객체 생성
                val store = Store(
                    // 여기서의 코드는 각 필드의 값을 가져와서 Store 객체의 속성 값 설정
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
                // Store 객체를 리스트에 추가
                stores.add(store)
                // 리스트의 크기가 100이면 데이터베이스에 저장하고 리스트를 비움
                if ((index + 1) % 100 == 0) {
                    storeRepository.saveAll(stores)
                    stores.clear()
                }
            } catch (e: Exception) {
                println("데이터 저장 중 에러가 발생했습니다: $line")
                e.printStackTrace()
            }
        }
        // 모든 줄을 돌고 난 후, 리스트에 남아있는 Store 객체가 있으면 데이터베이스에 저장
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

    //@Cacheable("PagedStoreCache", key = "{#pageable.pageNumber, #pageable.pageSize, #toSimple }", cacheManager = "defaultCacheManager")
    override fun <T> getStoreList( pageable: Pageable, toSimple:Boolean) : Page<T> {

        return if(toSimple){
            storeRepository.getStores(pageable, SimpleStore::class.java)?.map{it.toResponse()} as Page<T>
        }
        else {
            storeRepository.getStores(pageable, Store::class.java)?.map{it.toResponse()} as Page<T>
        }
    }


    override fun getStoreById( id : Long) : StoreResponse
    {
       return getFromCache(id)
    }


    //@Cacheable("storeCache", key = "{#id}")
    override fun searchStoresBy(id: Long?, company: String?, shopName: String?, tel: String?): List<StoreResponse> {

        // id 값이 있으면 캐시 통한 단건조회
        if(id != null)
            return listOf(getFromCache(id))

        // 없으면 일반 검색 조회
        else if(company == null && shopName == null && tel == null)
            throw IllegalArgumentException("must input at least one")

        return storeRepository.searchStoresBy(company, shopName, tel).map{it.toResponse()}

    }

    override fun getNewStores(size: Long): List<StoreResponse> {
        val storeList = storeRepository.getNewStores(size).map{it.toResponse()}

        //개선필요
        storeList.forEach{
            redisTemplate.opsForValue().set("storeCache::${it.id}", it)
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


    fun getFromCache(id : Long) : StoreResponse{
        var key = "storeCache::$id"

        val cachedStore = redisTemplate.opsForValue().get(key)

        // 레디스 템플릿에서 겹치는 키가 있는지 확인
        if (cachedStore != null) {
            logger.info("-------------".repeat(10))
            logger.info("Cache hit for key: {}", key)
            logger.info("cachedStore : {}", cachedStore)
            logger.info("-------------".repeat(10))
            return cachedStore
        }
        // 레디스 템플릿에서 겹치지 않을 시 새로 등록
        logger.info("-------------".repeat(10))
        logger.info("Cache miss for key: {}", key)
        logger.info("-------------".repeat(10))
        return storeRepository.getStoreBy(id).toResponse().also { it ->
            redisTemplate.opsForValue().set(key, it, 1L)
        }
    }
}
