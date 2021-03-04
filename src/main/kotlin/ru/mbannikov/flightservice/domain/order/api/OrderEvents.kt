package ru.mbannikov.flightservice.domain.order.api

import ru.mbannikov.flightservice.domain.Event
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import java.util.UUID

interface OrderEvent : Event {
    val orderId: String
}

data class OrderCreatedEvent(
    override val orderId: String,
    val tripId: String,
    val travelerId: UUID,
    val flightInfo: FlightInfo
) : OrderEvent

data class OrderConfirmedEvent(
    override val orderId: String
) : OrderEvent

data class OrderAuthorizedEvent(
    override val orderId: String,
    val authorizerId: UUID
) : OrderEvent

data class OrderNotAuthorizedEvent(
    override val orderId: String,
    val authorizerId: UUID
) : OrderEvent

data class OrderBookedEvent(
    override val orderId: String,
    val bookingInfo: BookingInfo
) : OrderEvent

data class OrderPayedEvent(
    override val orderId: String
) : OrderEvent
