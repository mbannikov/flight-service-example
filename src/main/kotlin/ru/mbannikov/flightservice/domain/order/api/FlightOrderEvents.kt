package ru.mbannikov.flightservice.domain.order.api

import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import java.util.UUID

abstract class FlightOrderEvent(
    open val orderId: String
) : Event

data class FlightOrderCreatedEvent(
    override val orderId: String,
    val tripId: String,
    val travelerId: UUID,
    val flightInfo: FlightInfo
) : FlightOrderEvent(orderId)

data class FlightOrderConfirmedEvent(
    override val orderId: String
) : FlightOrderEvent(orderId)

data class FlightOrderAuthorizedEvent(
    override val orderId: String,
    val authorizerId: UUID
) : FlightOrderEvent(orderId)

data class FlightOrderNotAuthorizedEvent(
    override val orderId: String,
    val authorizerId: UUID
) : FlightOrderEvent(orderId)

data class FlightOrderBookedEvent(
    override val orderId: String,
    val bookingInfo: BookingInfo
) : FlightOrderEvent(orderId)

data class FlightOrderPayedEvent(
    override val orderId: String
) : FlightOrderEvent(orderId)
