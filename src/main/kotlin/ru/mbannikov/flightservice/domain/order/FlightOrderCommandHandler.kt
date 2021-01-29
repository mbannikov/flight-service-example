package ru.mbannikov.flightservice.domain.order

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.Repository
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.domain.order.api.AuthorizeFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.BookFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.ConfirmFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.CreateFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.DoNotAuthorizeFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.PayFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.FlightOrderState
import ru.mbannikov.flightservice.provider.FlightProvider
import ru.mbannikov.flightservice.utils.Logging

@Component
class FlightOrderCommandHandler(
//    private val axonRepository: Repository<FlightOrder>,
    private val jpaRepository: FlightOrderJpaRepository,
    private val flightProvider: FlightProvider
) : Logging {
    @CommandHandler
    fun handle(command: CreateFlightOrderCommand): String {
        log.info { "Processing command=$command" }

        val order = FlightOrder(command)
        jpaRepository.save(order)
        return order.orderId

//        return axonRepository.newInstance { FlightOrder(command) }.identifierAsString()
    }

    @CommandHandler
    fun handle(command: ConfirmFlightOrderCommand) {
        log.info { "Processing command=$command" }

        val order = jpaRepository.findByIdOrThrow(command.orderId)
        order.confirm()
        jpaRepository.save(order)
    }

    @CommandHandler
    fun handle(command: AuthorizeFlightOrderCommand) {
        log.info { "Processing command=$command" }

        val order = jpaRepository.findByIdOrThrow(command.orderId)
        order.authorize(command.authorizerId)
    }

    @CommandHandler
    fun handle(command: DoNotAuthorizeFlightOrderCommand) {
        log.info { "Processing command=$command" }

        val order = jpaRepository.findByIdOrThrow(command.orderId)
        order.doNotAuthorize(command.authorizerId)
    }

    @CommandHandler
    fun handle(command: BookFlightOrderCommand) {
        log.info { "Processing command=$command" }

        val order = jpaRepository.findByIdOrThrow(command.orderId)
        val bookingInfo: BookingInfo = flightProvider.book(order.flightInfo.id)
        order.book(bookingInfo)
    }

    @CommandHandler
    fun handle(command: PayFlightOrderCommand) {
        log.info { "Processing command=$command" }

        val order = jpaRepository.findByIdOrThrow(command.orderId)
//        flightProvider.pay(order.bookingInfo.id, order.flightInfo.priceWhenChosen)
        order.pay()
    }
}