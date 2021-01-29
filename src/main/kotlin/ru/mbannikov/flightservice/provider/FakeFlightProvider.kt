package ru.mbannikov.flightservice.provider

import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class FakeFlightProvider : FlightProvider {
    override fun book(flightId: String): BookingInfo {
        return BookingInfo(
            id = "sdfsdfads",
            token = "fsdfasdfadf",
            expiresIn = LocalDateTime.now().plusDays(15)
        )
    }

    override fun pay(bookingId: String, price: BigDecimal) {

    }
}