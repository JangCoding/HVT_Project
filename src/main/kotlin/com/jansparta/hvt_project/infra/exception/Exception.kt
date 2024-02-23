package com.jansparta.hvt_project.infra.exception

import java.util.UUID

data class EmailAlreadyExistException(val email: String) : RuntimeException(
    "이미 존재하는 이메일 입니다."
)
data class NicknameAlreadyExistException(val nickName: String) : RuntimeException(
    "이미 존재하는 닉네임입니다."
)

data class ModelNotFoundException(val modelName: String, val id: UUID?) :
    RuntimeException("Model $modelName not found with given id: $id")

data class InvalidPasswordException(val password: String) : RuntimeException(
    "맞지 않은 비밀번호 입니다. 다시 시도해주세요."
)
data class LikeException(val id: Any, val userId: Long) : RuntimeException(
    "이 아이디($id)로는 찜 할 수 없습니다."
)
data class InvalidRoleException(val role: String) : RuntimeException(
    "invalid ROLE = $role"
)
data class NotHavePermissionException(val aId: Long, val bId: Long) : RuntimeException(
    "$aId 는 $bId 에 권한이 없습니다"
)
