package com.jansparta.hvt_project.domain.store.repository

import com.jansparta.hvt_project.domain.store.model.Store
import com.jansparta.hvt_project.infra.querydsl.QueryDslSupport
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(value = [QueryDslSupport::class])
@ActiveProfiles("test")
class StoreRepositoryTest @Autowired constructor(
    private val storeRepository: StoreRepository
){

    /* findByRatingAndStatus, findSimpleByRatingAndStatus (rating:Int?, status: String?) */
    @Test
    fun `필터 조건이 없을 경우 결과 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByRatingAndStatus(null, null)
        val result2 = storeRepository.findSimpleByRatingAndStatus(null, null)

        // THEN
        result1.size shouldBe 10
        result2.size shouldBe 10
    }

    @Test
    fun `필터 조건이 하나 있을 경우 결과 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByRatingAndStatus(1, null)
        val result2 = storeRepository.findSimpleByRatingAndStatus(1, null)
        val result3 = storeRepository.findByRatingAndStatus(null, "휴업중")
        val result4 = storeRepository.findSimpleByRatingAndStatus(null, "휴업중")

        // THEN
        result1.size shouldBe 7
        result2.size shouldBe 7
        result3.size shouldBe 1
        result4.size shouldBe 1
    }

    @Test
    fun `필터 조건이 모두 있을 경우 결과 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByRatingAndStatus(3, "사이트운영중단")
        val result2 = storeRepository.findSimpleByRatingAndStatus(3, "사이트운영중단")

        // THEN
        result1.size shouldBe 2
        result2.size shouldBe 2
    }

    @Test
    fun `필터 조건에 잘못된 값이 들어온 경우 결과 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByRatingAndStatus(4, null)
        val result2 = storeRepository.findSimpleByRatingAndStatus(4, null)
        val result3 = storeRepository.findByRatingAndStatus(null, "사이트운영중")
        val result4 = storeRepository.findSimpleByRatingAndStatus(null, "사이트운영중")

        // THEN
        result1.size shouldBe 0
        result2.size shouldBe 0
        result3.size shouldBe 0
        result4.size shouldBe 0
    }


    /* findByPageableAndFilter, findSimpleByPageableAndFilter (pageable: Pageable, cursorId: Long?, rating:Int?, status: String?) */
    @Test
    fun `필터 조건이 없을 경우 0Page 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),12L, null, null)
        val result2 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),12L, null, null)

        // THEN
        result1.totalElements shouldBe 11
        result1.content.size shouldBe 10
        result1.totalPages shouldBe 2
        result1.number shouldBe 0

        result2.totalElements shouldBe 11
        result2.content.size shouldBe 10
        result2.totalPages shouldBe 2
        result2.number shouldBe 0
    }

    @Test
    fun `필터 조건이 없을 경우 1Page 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),2L, null, null)
        val result2 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),2L, null, null)

        // THEN
        result1.totalElements shouldBe 1
        result1.content.size shouldBe 1
        result1.totalPages shouldBe 1
        result1.number shouldBe 0

        result2.totalElements shouldBe 1
        result2.content.size shouldBe 1
        result2.totalPages shouldBe 1
        result2.number shouldBe 0
    }

    @Test
    fun `필터 조건이 하나 있을 경우 0Page 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),12L, 1, null)
        val result2 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),12L, 1, null)
        val result3 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),12L, null, "휴업중")
        val result4 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),12L, null, "휴업중")

        // THEN
        result1.totalElements shouldBe 7
        result1.content.size shouldBe 7
        result1.totalPages shouldBe 1
        result1.number shouldBe 0

        result2.totalElements shouldBe 7
        result2.content.size shouldBe 7
        result2.totalPages shouldBe 1
        result2.number shouldBe 0

        result3.totalElements shouldBe 1
        result3.content.size shouldBe 1
        result3.totalPages shouldBe 1
        result3.number shouldBe 0

        result4.totalElements shouldBe 1
        result4.content.size shouldBe 1
        result4.totalPages shouldBe 1
        result4.number shouldBe 0
    }

    @Test
    fun `필터 조건이 모두 있을 경우 0Page 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),12L, 3, "사이트운영중단")
        val result2 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),12L, 3, "사이트운영중단")

        // THEN
        result1.totalElements shouldBe 2
        result1.content.size shouldBe 2
        result1.totalPages shouldBe 1
        result1.number shouldBe 0

        result2.totalElements shouldBe 2
        result2.content.size shouldBe 2
        result2.totalPages shouldBe 1
        result2.number shouldBe 0
    }

    @Test
    fun `필터 조건에 잘못된 값이 들어온 경우 Page 확인`() {
        // GIVEN
        storeRepository.saveAllAndFlush(DEFAULT_STORE_LIST)

        // WHEN
        val result1 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),12L, 4, null)
        val result2 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),12L, 4, null)
        val result3 = storeRepository.findByPageableAndFilter(Pageable.ofSize(10),12L, null, "사이트운영중")
        val result4 = storeRepository.findSimpleByPageableAndFilter(Pageable.ofSize(10),12L, null, "사이트운영중")

        // THEN
        result1.totalElements shouldBe 0
        result1.content.size shouldBe 0
        result1.totalPages shouldBe 0
        result1.number shouldBe 0

        result2.totalElements shouldBe 0
        result2.content.size shouldBe 0
        result2.totalPages shouldBe 0
        result2.number shouldBe 0

        result3.totalElements shouldBe 0
        result3.content.size shouldBe 0
        result3.totalPages shouldBe 0
        result3.number shouldBe 0

        result4.totalElements shouldBe 0
        result4.content.size shouldBe 0
        result4.totalPages shouldBe 0
        result4.number shouldBe 0
    }


    companion object {
        private val DEFAULT_STORE_LIST = listOf(
            Store(company = "베이지베이지", shopName = "확인안됨", domainName = "www.beigebeige.co.kr", tel = "070-7796-2787", email = "weloveith@naver.com",
                ypForm = "일반쇼핑몰", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 2, regDate = "2024-01-24",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "(주)오해린", shopName = "오해린/OHELIN", domainName = "www.ohelin.co.kr", tel = "02-2236-3830", email = "ohelin@naver.com",
                ypForm = "", comAddr = "서울특별시 중구 청구로4길 19 (신당동)", statNm = "휴업중", totRatingPoint = 1, regDate = "2024-01-26",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "케이투코리아(주)", shopName = "k2korea/케이투코리아", domainName = "k2group.co.kr", tel = "1644-7781", email = "swhong77@k2korea.co.kr",
                ypForm = "", comAddr = "서울특별시 강남구 자곡로 174-14", statNm = "광고용(홍보용)", totRatingPoint = 3, regDate = "2024-01-22",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "아이링골드", shopName = "확인안됨", domainName = "http://iringold.com", tel = "02-312-1112", email = "iringg@naver.com",
                ypForm = "일반쇼핑몰", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 3, regDate = "2024-01-28",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "스테디 에브리웨어", shopName = "확인안됨", domainName = "www.steadyeverywear.com", tel = "확인안됨", email = "steadyeverywear@naver.com",
                ypForm = "", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 1, regDate = "2024-01-24",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "주식회사 제이웍스이엑스티", shopName = "확인안됨", domainName = "www.eye-candy.co.kr", tel = "070-7514-3706", email = "jay@jworksext.co.kr",
                ypForm = "일반쇼핑몰", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 3, regDate = "2024-01-21",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "주식회사 현투어", shopName = "확인안됨", domainName = "www.hyuntour.com", tel = "031-656-7882/031-632-7882", email = "kh07777@naver.com",
                ypForm = "", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 1, regDate = "2024-01-22",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "레이지룸", shopName = "확인안됨", domainName = "http://lazyroom.kr", tel = "043-843-0157", email = "besnag@naver.com",
                ypForm = "", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 1, regDate = "2024-01-23",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "(주)더맑음컴퍼니(해브지점)", shopName = "확인안됨", domainName = "https://mlhtex.cafe24.com/", tel = "확인안됨", email = "move2134@gmail.com",
                ypForm = "", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 1, regDate = "2024-01-24",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "주식회사 더바다쓰/The Badass", shopName = "확인안됨", domainName = "www.badass.co.kr", tel = "확인안됨", email = "desicnerbiko@naver.com",
                ypForm = "", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 1, regDate = "2024-01-26",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = ""),

            Store(company = "모어에프엔", shopName = "확인안됨", domainName = "www.morette.co.kr", tel = "확인안됨", email = "morette_official@naver.com",
                ypForm = "", comAddr = "확인안됨", statNm = "사이트운영중단", totRatingPoint = 1, regDate = "2024-01-29",
                upjongNbr = "", firstHeoDate = "", chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
                securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
                sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = "")
        ) //11건
    }
}

//Store(company = "", shopName = "", domainName = "", tel = "", email = "", upjongNbr = "", ypForm = "", firstHeoDate = "",
//comAddr = "", statNm = "", totRatingPoint = 1, chogiRatingPoint = 1, chungRatingPoint = 1, dealRatingPoint = 1, pyojunRatingPoint = 1,
//securityRatingPoint = 1, service = "", chung = "", chogi = "", gyulje = "", pyojun = "", pInfoCare = "", perInfo = "", dealCare = "",
//sslYn = "", injeung ="", baesongYejeong = "", baesong = "", clientBbs = "", leave = "", kaesolYear = "", regDate = "")