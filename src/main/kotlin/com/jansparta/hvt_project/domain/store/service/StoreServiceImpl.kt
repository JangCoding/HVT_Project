package com.jansparta.hvt_project.domain.store.service

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
import org.w3c.dom.Element
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,

) : StoreService {
    private val KEY = "6c66575174726c6136306f73446167"
    private val baseUrl = "http://openapi.seoul.go.kr:8088/$KEY/xml/ServiceInternetShopInfo"
    private val recordPerPage = 1000

    //XML 문서의 특정 Element 에서 특정 태그의 값을 가져오는 역할
    private fun getTagValue(element: Element, tagName: String): String? {
        val nodeList = element.getElementsByTagName(tagName) // Element에서 주어진 태그 이름에 해당하는 모든 노드를 NodeList 형태로 가져옴
        // 태그가 해당 Element 내에 존재하는지를 확인
        if (nodeList.length > 0) {
            val node = nodeList.item(0)
            return node?.textContent
        }
        return null
    }

    override fun fetchDataAndStore() {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()

        val initialDoc = builder.parse("$baseUrl/1/$recordPerPage")
        val totalItemCount = initialDoc.getElementsByTagName("list_total_count").item(0).textContent.toInt()
        val totalPage = (totalItemCount + recordPerPage - 1) / recordPerPage // 올림 연산

        val stores = mutableListOf<Store>()

        for (page in 1..totalPage) {
            val startIndex = (page - 1) * recordPerPage + 1
            val endIndex = page * recordPerPage
            val url = "$baseUrl/$startIndex/$endIndex"
            val doc = builder.parse(url)
            val items = doc.getElementsByTagName("row")

            for (i in 0 until items.length) {
                val item = items.item(i) as Element

                val store = Store(
                    company = getTagValue(item, "COMPANY"),
                    shopName = getTagValue(item, "SHOP_NAME"),
                    domainName = getTagValue(item, "DOMAIN_NAME"),
                    tel = getTagValue(item, "TEL"),
                    email = getTagValue(item, "EMAIL"),
                    upjongNbr = getTagValue(item, "UPJONG_NBR"),
                    ypForm = getTagValue(item, "YPFORM"),
                    firstHeoDate = getTagValue(item, "FIRST_HEO_DATE")?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() },
                    comAddr = getTagValue(item, "COM_ADDR"),
                    statNm = getTagValue(item, "STAT_NM"),
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

                stores.add(store)

                if (stores.size == 100) {
                    storeRepository.saveAll(stores)
                    stores.clear()
                }
            }
        }
        if (stores.isNotEmpty()) {
            storeRepository.saveAll(stores)
        }
    }


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
                    statNm = data[9].ifEmpty { null },
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
            statNm = request.statNm
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

    override fun getFilteredSimpleStoreList(rating: Int?, status: String?): List<SimpleStoreResponse> {
        return storeRepository.findSimpleByRatingAndStatus(rating, status).map { it.toResponse() }
    }

    override fun getFilteredStorePage(
        pageable: Pageable,
        cursorId: Long?,
        rating: Int?,
        status: String?
    ): Page<StoreResponse> {
        return storeRepository.findByPageableAndFilter(pageable, cursorId, rating, status).map { it.toResponse() }
    }

    override fun getFilteredSimpleStorePage(
        pageable: Pageable,
        cursorId: Long?,
        rating: Int?,
        status: String?
    ): Page<SimpleStoreResponse> {
        return storeRepository.findSimpleByPageableAndFilter(pageable, cursorId, rating, status).map { it.toResponse() }
    }

    override fun getStoreBy(id: Long?, company: String?, shopName: String?, tel: String?): StoreResponse {
        if(id == null && company == null && shopName == null && tel == null)
            throw NotFoundException()

        return storeRepository.getStoreBy(id, company, shopName, tel).toResponse()
    }
}
