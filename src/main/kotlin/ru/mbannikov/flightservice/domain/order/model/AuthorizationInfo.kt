package ru.mbannikov.flightservice.domain.order.model

import java.util.UUID

data class AuthorizationInfo(
    val authorizerId: UUID,
    val decision: AuthorizationDecisionEnum
)
