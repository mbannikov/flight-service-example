package ru.mbannikov.flightservice.domain.order.model

import java.time.LocalDateTime

data class BookingInfo(
    val id: String,
    val token: String,
    val expiresIn: LocalDateTime
)