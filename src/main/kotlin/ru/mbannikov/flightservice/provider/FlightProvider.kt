package ru.mbannikov.flightservice.provider

import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import java.math.BigDecimal

interface FlightProvider {
    fun book(flightId: String): BookingInfo

    fun pay(bookingId: String, price: BigDecimal)
}

