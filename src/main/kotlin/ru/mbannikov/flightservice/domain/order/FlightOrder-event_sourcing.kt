package ru.mbannikov.flightservice.domain.order

import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate
import ru.mbannikov.flightservice.domain.order.api.CreateFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.FlightOrderAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderBookedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderConfirmedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderCreatedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderNotAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderPayedEvent
import ru.mbannikov.flightservice.domain.order.model.AuthorizationDecisionEnum
import ru.mbannikov.flightservice.domain.order.model.AuthorizationInfo
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import ru.mbannikov.flightservice.domain.order.model.FlightOrderState
import ru.mbannikov.flightservice.domain.order.model.FlightOrderState.*
import ru.mbannikov.flightservice.utils.AxonUtils.applyEvent
import java.util.UUID

@Aggregate
final class FlightOrderEs() {

    @AggregateIdentifier
    lateinit var orderId: String
        private set
    lateinit var tripId: String
        private set
    lateinit var travelerId: UUID
        private set
    lateinit var flightInfo: FlightInfo
        private set
    lateinit var state: FlightOrderState
        private set

    private val hiddenAttributes = OrderHiddenAttributes()
    val authorizationInfo: AuthorizationInfo
        get() = hiddenAttributes.authorizationInfo ?: throw IllegalAccessException()
    val bookingInfo: BookingInfo
        get() = hiddenAttributes.bookingInfo ?: throw IllegalAccessException()

    private var isConfirmed: Boolean = false
    private val hasAuthorizationDecision: Boolean
        get() = hiddenAttributes.authorizationInfo != null
    private val isBooked: Boolean
        get() = hiddenAttributes.bookingInfo != null
    private var isPayed: Boolean = false

    constructor(cmd: CreateFlightOrderCommand) : this() {
        FlightOrderCreatedEvent(cmd.orderId, cmd.tripId, cmd.travelerId, cmd.flightInfo).applyEvent()
    }

    fun confirm() {
        isConfirmed.throwIfTrue { IllegalAccessException("The order already confirmed") }

        FlightOrderConfirmedEvent(orderId).applyEvent()
    }

    fun authorize(authorizerId: UUID) {
        hasAuthorizationDecision.throwIfTrue { IllegalAccessException("The order already has authorization decision") }

        FlightOrderAuthorizedEvent(orderId, authorizerId).applyEvent()
    }

    fun doNotAuthorize(authorizerId: UUID) {
        hasAuthorizationDecision.throwIfTrue { IllegalAccessException("The order already has authorization decision") }

        FlightOrderNotAuthorizedEvent(orderId, authorizerId).applyEvent()
    }

    fun book(bookingInfo: BookingInfo) {
        isBooked.throwIfTrue { IllegalAccessException("The order already booked") }

        FlightOrderBookedEvent(orderId, bookingInfo).applyEvent()
    }

    fun pay() {
        isPayed.throwIfTrue { IllegalAccessException("The order already payed") }

        FlightOrderPayedEvent(orderId).applyEvent()
    }

    @EventSourcingHandler
    fun on(e: FlightOrderCreatedEvent) {
        state = INITIAL
        orderId = e.orderId
        tripId = e.tripId
        travelerId = e.travelerId
        flightInfo = e.flightInfo
    }

    @EventSourcingHandler
    fun on(e: FlightOrderConfirmedEvent) {
        state = CONFIRMED
        isConfirmed = true
    }

    @EventSourcingHandler
    fun on(e: FlightOrderAuthorizedEvent) {
        state = AUTHORIZED
        hiddenAttributes.authorizationInfo = AuthorizationInfo(
            authorizerId = e.authorizerId,
            decision = AuthorizationDecisionEnum.AUTHORIZED
        )
    }

    @EventSourcingHandler
    fun on(e: FlightOrderNotAuthorizedEvent) {
        state = NOT_AUTHORIZED
        hiddenAttributes.authorizationInfo = AuthorizationInfo(
            authorizerId = e.authorizerId,
            decision = AuthorizationDecisionEnum.NOT_AUTHORIZED
        )
    }

    @EventSourcingHandler
    fun on(e: FlightOrderBookedEvent) {
        state = BOOKED
        hiddenAttributes.bookingInfo = e.bookingInfo
    }

    @EventSourcingHandler
    fun on(e: FlightOrderPayedEvent) {
        state = PAYED
        isPayed = true
    }
}

private class OrderHiddenAttributes {
    var authorizationInfo: AuthorizationInfo? = null
    var bookingInfo: BookingInfo? = null
}

private fun Boolean.throwIfTrue(throwable: () -> Throwable) {
    if (this) throw throwable()
}
