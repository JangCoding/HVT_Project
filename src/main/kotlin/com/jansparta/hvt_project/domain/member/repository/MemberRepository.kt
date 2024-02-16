package com.jansparta.hvt_project.domain.member.repository

import com.jansparta.hvt_project.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository : JpaRepository<Member, UUID> {

    fun findByEmail(email: String): Member?
    fun existsByEmail(email: String): Boolean

    fun existsByNickName(nickName: String): Boolean
}