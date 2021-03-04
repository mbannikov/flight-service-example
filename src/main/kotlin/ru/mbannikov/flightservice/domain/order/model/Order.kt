package ru.mbannikov.flightservice.domain.order.model

import ru.mbannikov.flightservice.domain.order.api.CreateOrderCommand
import ru.mbannikov.flightservice.domain.order.api.OrderAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderBookedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderConfirmedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderNotAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.OrderPayedEvent
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import ru.mbannikov.flightservice.domain.order.model.OrderState
import ru.mbannikov.flightservice.domain.order.model.OrderState.AUTHORIZED
import ru.mbannikov.flightservice.domain.order.model.OrderState.BOOKED
import ru.mbannikov.flightservice.domain.order.model.OrderState.CONFIRMED
import ru.mbannikov.flightservice.domain.order.model.OrderState.INITIAL
import ru.mbannikov.flightservice.domain.order.model.OrderState.NOT_AUTHORIZED
import ru.mbannikov.flightservice.domain.order.model.OrderState.PAYED
import java.util.UUID
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity(name = "flight_order")
data class Order(
    @Id
    val orderId: String = UUID.randomUUID().toString(),
    var tripId: String,
    var travelerId: UUID,
    @Embedded
    val flightInfo: FlightInfo
) {
    @Enumerated(value = EnumType.STRING)
    var state: OrderState = INITIAL

    constructor(cmd: CreateOrderCommand) : this(
        tripId = cmd.tripId,
        travelerId = cmd.travelerId,
        flightInfo = cmd.flightInfo
    )

    fun confirm(): OrderConfirmedEvent {
        state = CONFIRMED
        return OrderConfirmedEvent(orderId)
    }

    fun authorize(authorizerId: UUID): OrderAuthorizedEvent {
        state = AUTHORIZED
        return OrderAuthorizedEvent(orderId, authorizerId)
    }

    fun doNotAuthorize(authorizerId: UUID): OrderNotAuthorizedEvent {
        state = NOT_AUTHORIZED
        return OrderNotAuthorizedEvent(orderId, authorizerId)
    }

    fun book(bookingInfo: BookingInfo): OrderBookedEvent {
        state = BOOKED
        return OrderBookedEvent(orderId, bookingInfo)
    }

    fun pay(): OrderPayedEvent {
        state = PAYED
        return OrderPayedEvent(orderId)
    }
}

