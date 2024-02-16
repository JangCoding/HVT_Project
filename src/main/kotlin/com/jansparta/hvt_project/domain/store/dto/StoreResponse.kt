package com.jansparta.hvt_project.domain.store.dto

import com.jansparta.hvt_project.domain.store.model.Store

data class StoreResponse(
    val id:Long,
    var company: String?, // 상호
    var shopName: String?, // 쇼핑몰명
    var domainName: String?, // 도메인명
    var tel: String?, // 전화번호
    var email: String?, // 운영자이메일
    var upjongNbr: String?, // 통신판매번호
    var ypForm: String?, // 영업형태
    var firstHeoDate: String?, // 최초신고일자
    var comAddr: String?, // 회사주소
    var statNm: String?, // 업소상태
    var totRatingPoint: Int?, // 전체평가
    var chogiRatingPoint: Int?, // 사업자정보표시평가
    var chungRatingPoint: Int?, // 청약철회평가
    var dealRatingPoint: Int?, // 결재방법평가
    var pyojunRatingPoint: Int?, // 이용약관평가
    var securityRatingPoint: Int?, // 개인정보보안평가
    var service: String?, // 주요취급품목
    var chung: String?, // 청약철회가능여부
    var chogi: String?, // 초기화면필수항목중표시사항
    var gyulje: String?, // 결제방법
    var pyojun: String?, // 이용약관준수정도
    var pInfoCare: String?, // 개인정보취급방침
    var perInfo: String?, // 표준약관이상개인정보항목요구
    var dealCare: String?, // 구매안전서비스
    var sslYn: String?, // 보안서버설치
    var injeung: String?, // 인증마크
    var baesongYejeong: String?, // 배송예정일표시
    var baesong: String?, // 철회시배송비부담여부
    var clientBbs: String?, // 고객불만게시판운영
    var leave: String?, // 회원탈퇴방법
    var kaesolYear: String?, // 사이트개설년도
    var regDate: String? // 모니터링날짜
)

fun Store.toResponse(): StoreResponse {
    return StoreResponse(
        id = id!!,
        company = company,
        shopName = shopName,
        domainName = domainName,
        tel = tel,
        email = email,
        upjongNbr = upjongNbr,
        ypForm = ypForm,
        firstHeoDate = firstHeoDate,
        comAddr = comAddr,
        statNm = statNm.name,
        totRatingPoint = totRatingPoint,
        chogiRatingPoint = chogiRatingPoint,
        chungRatingPoint = chungRatingPoint,
        dealRatingPoint = dealRatingPoint,
        pyojunRatingPoint = pyojunRatingPoint,
        securityRatingPoint = securityRatingPoint,
        service = service,
        chung = chung,
        chogi = chogi,
        gyulje = gyulje,
        pyojun = pyojun,
        pInfoCare = pInfoCare,
        perInfo = perInfo,
        dealCare = dealCare,
        sslYn = sslYn,
        injeung = injeung,
        baesongYejeong = baesongYejeong,
        baesong = baesong,
        clientBbs = clientBbs,
        leave = leave,
        kaesolYear = kaesolYear,
        regDate = regDate
    )
}