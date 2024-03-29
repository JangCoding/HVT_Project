package com.jansparta.hvt_project.infra

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
class BaseEntity{

    @CreatedDate
    @Column(updatable = false)
    var createdDate : LocalDateTime? = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDate : LocalDateTime? = LocalDateTime.now()

    var isDeleted : Boolean? = false;

}
