package com.jansparta.hvt_project.infra.Security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

data class UserPrincipal(
    val id: UUID,
    val email: String,
    val authorities: Collection<GrantedAuthority>
) {
    constructor(id: UUID, email: String, roles: Set<String>) : this(
        id,
        email,
        roles.map { SimpleGrantedAuthority("ROLE_$it") }
    )
}