package com.jansparta.hvt_project.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "stores")
class Store(
    @Column(name = "COMPANY")
    var company: String,
    @Column(name = "SHOP_NAME")
    var shopName: String,
    @Column(name = "DOMAIN_NAME")
    var domainName: String,
    @Column(name = "TEL")
    var tel: String,
    @Column(name = "EMAIL")
    var email: String,
    @Column(name = "UPJONG_NBR")
    var upjongNbr: String?,
    @Column(name = "YPFORM")
    var ypform: String?,
    @Column(name = "FIRST_HEO_DATE")
    var firstHeoDate: String,
    @Column(name = "COM_ADDR")
    var comAddr: String?,
    @Column(name = "STAT_NM")
    var statNm: String,
    @Column(name = "TOT_RATINGPOINT")
    var totRatingpoint: Int,
    @Column(name = "CHOGI_RATINGPOINT")
    var chogiRatingpoint: Int,
    @Column(name = "CHUNG_RATINGPOINT")
    var chungRatingpoint: Int,
    @Column(name = "DEAL_RATINGPOINT")
    var dealRatingpoint: Int,
    @Column(name = "PYOJUN_RATINGPOINT")
    var pyojunRatingpoint: Int,
    @Column(name = "SECURITY_RATINGPOINT")
    var securityRatingpoint: Int,
    @Column(name = "SERVICE")
    var service: String?,
    @Column(name = "CHUNG")
    var chung: String,
    @Column(name = "CHOGI")
    var chogi: String?,
    @Column(name = "GYULJE")
    var gyulje: String?,
    @Column(name = "PYOJUN")
    var pyojun: String,
    @Column(name = "P_INFO_CARE")
    var pInfoCare: String,
    @Column(name = "PER_INFO")
    var perInfo: String?,
    @Column(name = "DEAL_CARE")
    var dealCare: String,
    @Column(name = "SSL_YN")
    var sslYn: String?,
    @Column(name = "INJEUNG")
    var injung: String?,
    @Column(name = "BAESONG_YEJEONG")
    var baesongYejeong: String,
    @Column(name = "BAESONG")
    var baesong: String,
    @Column(name = "CLIENT_BBS")
    var clientBbs: String,
    @Column(name = "LEAVE")
    var leave: String,
    @Column(name = "KAESOL_YEAR")
    var kaesolYear: Int,
    @Column(name = "REG_DATE")
    var regDate: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}