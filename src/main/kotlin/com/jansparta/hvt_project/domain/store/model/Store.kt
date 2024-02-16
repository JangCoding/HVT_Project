package com.jansparta.hvt_project.domain.store.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "stores")
class Store(
    @Column(name = "COMPANY") // 상호
    var company: String?,
    @Column(name = "SHOP_NAME") // 쇼핑몰명
    var shopName: String?,
    @Column(name = "DOMAIN_NAME") // 도메인명
    var domainName: String?,
    @Column(name = "TEL") // 전화번호
    var tel: String?,
    @Column(name = "EMAIL") // 운영자이메일
    var email: String?,
    @Column(name = "UPJONG_NBR") // 통신판매번호
    var upjongNbr: String?,
    @Column(name = "YPFORM") // 영업형태
    var ypForm: String?,
    @Column(name = "FIRST_HEO_DATE") // 최초신고일자
    var firstHeoDate: String?,
    @Column(name = "COM_ADDR") // 회사주소
    var comAddr: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "STAT_NM") // 업소상태
<<<<<<< HEAD
    var statNm: StatNmStatus,

=======
    var statNm: String?,
>>>>>>> origin/feat/collection
    @Column(name = "TOT_RATINGPOINT") // 전체평가
    var totRatingPoint: Int?,
    @Column(name = "CHOGI_RATINGPOINT") // 사업자정보표시평가
    var chogiRatingPoint: Int?,
    @Column(name = "CHUNG_RATINGPOINT") // 청약철회평가
    var chungRatingPoint: Int?,
    @Column(name = "DEAL_RATINGPOINT") // 결재방법평가
    var dealRatingPoint: Int?,
    @Column(name = "PYOJUN_RATINGPOINT") // 이용약관평가
    var pyojunRatingPoint: Int?,
    @Column(name = "SECURITY_RATINGPOINT") // 개인정보보안평가
    var securityRatingPoint: Int?,
    @Column(name = "SERVICE") // 주요취급품목
    var service: String?,
    @Column(name = "CHUNG") // 청약철회가능여부
    var chung: String?,
    @Column(name = "CHOGI") // 초기화면필수항목중표시사항
    var chogi: String?,
    @Column(name = "GYULJE") // 결제방법
    var gyulje: String?,
    @Column(name = "PYOJUN") // 이용약관준수정도
    var pyojun: String?,
    @Column(name = "P_INFO_CARE") // 개인정보취급방침
    var pInfoCare: String?,
    @Column(name = "PER_INFO") // 표준약관이상개인정보항목요구
    var perInfo: String?,
    @Column(name = "DEAL_CARE") // 구매안전서비스
    var dealCare: String?,
    @Column(name = "SSL_YN") // 보안서버설치
    var sslYn: String?,
    @Column(name = "INJEUNG") // 인증마크
    var injeung: String?,
    @Column(name = "BAESONG_YEJEONG") // 배송예정일표시
    var baesongYejeong: String?,
    @Column(name = "BAESONG") // 철회시배송비부담여부
    var baesong: String?,
    @Column(name = "CLIENT_BBS") // 고객불만게시판운영
    var clientBbs: String?,
    @Column(name = "LEAVE") // 회원탈퇴방법
    var leave: String?,
    @Column(name = "KAESOL_YEAR") // 사이트개설년도
    var kaesolYear: String?,
    @Column(name = "REG_DATE") // 모니터링날짜
    var regDate: String?
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}