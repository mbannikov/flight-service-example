package ru.mbannikov.flightservice.domain.order

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.eventhandling.EventBus
import org.axonframework.messaging.annotation.ParameterResolverFactory
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.GenericJpaRepository
import org.axonframework.modelling.command.Repository
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import ru.mbannikov.flightservice.domain.order.api.CreateFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.FlightOrderAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderBookedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderConfirmedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderCreatedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderNotAuthorizedEvent
import ru.mbannikov.flightservice.domain.order.api.FlightOrderPayedEvent
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import ru.mbannikov.flightservice.domain.order.model.FlightOrderState
import ru.mbannikov.flightservice.domain.order.model.FlightOrderState.CONFIRMED
import ru.mbannikov.flightservice.domain.order.model.FlightOrderState.INITIAL
import ru.mbannikov.flightservice.utils.AxonUtils.applyEvent
import java.lang.IllegalStateException
import java.util.UUID
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

//@Aggregate
@Entity
data class FlightOrder(
//    @Id @AggregateIdentifier var orderId: String,
    @Id var orderId: String,
    var tripId: String,
    var travelerId: UUID,
    @Embedded val flightInfo: FlightInfo
) {
    @Enumerated(value = EnumType.STRING)
    var state: FlightOrderState = INITIAL

    constructor(cmd: CreateFlightOrderCommand) : this(cmd.orderId, cmd.tripId, cmd.travelerId, cmd.flightInfo) {
        FlightOrderCreatedEvent(cmd.orderId, cmd.tripId, cmd.travelerId, cmd.flightInfo).applyEvent()
    }

    fun confirm() {
        state = CONFIRMED
        FlightOrderConfirmedEvent(orderId).applyEvent()
    }

    fun authorize(authorizerId: UUID) {
        FlightOrderAuthorizedEvent(orderId, authorizerId).applyEvent()
    }

    fun doNotAuthorize(authorizerId: UUID) {
        FlightOrderNotAuthorizedEvent(orderId, authorizerId).applyEvent()
    }

    fun book(bookingInfo: BookingInfo) {
        FlightOrderBookedEvent(orderId, bookingInfo).applyEvent()
    }

    fun pay() {
        FlightOrderPayedEvent(orderId).applyEvent()
    }
}

//@Configuration
//class AxonConfig {
//
//    @Bean
//    @Primary
//    fun flightOrderAxonRepository(emp: EntityManagerProvider, prf: ParameterResolverFactory, eventBus: EventBus): Repository<FlightOrder> =
//        GenericJpaRepository.builder(FlightOrder::class.java)
//            .parameterResolverFactory(prf)
//            .entityManagerProvider(emp)
//            .eventBus(eventBus)
//            .build()
//}

interface FlightOrderJpaRepository : JpaRepository<FlightOrder, String> {
    @JvmDefault
    fun findByIdOrThrow(orderId: String): FlightOrder = findByIdOrNull(orderId) ?: throw IllegalStateException("Order with id=$orderId not found!")

    fun findAllByState(state: FlightOrderState): List<FlightOrder>
}