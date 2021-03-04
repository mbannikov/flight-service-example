package ru.mbannikov.flightservice.domain.order

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.mbannikov.flightservice.domain.Event
import ru.mbannikov.flightservice.domain.order.api.AuthorizeOrderCommand
import ru.mbannikov.flightservice.domain.order.api.BookOrderCommand
import ru.mbannikov.flightservice.domain.order.api.ConfirmOrderCommand
import ru.mbannikov.flightservice.domain.order.api.CreateOrderCommand
import ru.mbannikov.flightservice.domain.order.api.DoNotAuthorizeOrderCommand
import ru.mbannikov.flightservice.domain.order.api.OrderCreatedEvent
import ru.mbannikov.flightservice.domain.order.api.PayOrderCommand
import ru.mbannikov.flightservice.domain.order.model.BookingInfo
import ru.mbannikov.flightservice.domain.order.model.Order
import ru.mbannikov.flightservice.provider.FlightProvider
import ru.mbannikov.flightservice.utils.Logging
import ru.mbannikov.mescofe.cqrs.annotation.CommandHandler
import ru.mbannikov.mescofe.eventhandling.EventGateway
import ru.mbannikov.mescofe.eventhandling.annotation.EventHandler
import java.time.LocalTime

@Component
class OrderCommandHandler(
    private val orderRepository: OrderRepository,
    private val flightProvider: FlightProvider,
    private val eventGateway: EventGateway
) : Logging {

    @CommandHandler
    fun handle(command: CreateOrderCommand): String {
        log.info { "Processing command=$command" }

        val order = Order(command)
        orderRepository.save(order)
        OrderCreatedEvent(order.orderId, command.tripId, command.travelerId, command.flightInfo).publishEvent() // TODO

        return order.orderId
    }

    @CommandHandler
    fun handle(command: ConfirmOrderCommand) {
        log.info { "Processing command=$command" }

        val order = orderRepository.findByIdOrThrow(command.orderId)
        saveOrderAndPublishEvent(order = order, event = order.confirm())
    }

    @CommandHandler
    fun handle(command: AuthorizeOrderCommand) {
        log.info { "Processing command=$command" }

        val order = orderRepository.findByIdOrThrow(command.orderId)
        saveOrderAndPublishEvent(order = order, event = order.authorize(command.authorizerId))
    }

    @CommandHandler
    fun handle(command: DoNotAuthorizeOrderCommand) {
        log.info { "Processing command=$command" }

        val order = orderRepository.findByIdOrThrow(command.orderId)
        saveOrderAndPublishEvent(order = order, event = order.doNotAuthorize(command.authorizerId))
    }

    @CommandHandler
    fun handle(command: BookOrderCommand) {
        log.info { "Processing command=$command" }

        val order = orderRepository.findByIdOrThrow(command.orderId)
        val bookingInfo: BookingInfo = flightProvider.book(order.flightInfo.id)
        saveOrderAndPublishEvent(order = order, event = order.book(bookingInfo))
    }

    @CommandHandler
    fun handle(command: PayOrderCommand) {
        log.info { "Processing command=$command" }

        val order = orderRepository.findByIdOrThrow(command.orderId)
//        flightProvider.pay(order.bookingInfo.id, order.flightInfo.priceWhenChosen)
        saveOrderAndPublishEvent(order = order, event = order.pay())
    }

    private fun saveOrderAndPublishEvent(order: Order, event: Event) {
        orderRepository.save(order)
        eventGateway.publish(event)
    }

    private fun Event.publishEvent() {
        eventGateway.publish(this)
    }
}

//@Component
class TestEventHandling(
    private val eventGateway: EventGateway
) : Logging {

    @EventListener
    fun run(e: ApplicationReadyEvent) {
        eventGateway.publish(event = AppReadyEvent(time = LocalTime.now()))

        log.info { ">>-----------------------------------" }
        log.info { "Event was sent in ${LocalTime.now()}" }
        log.info { "-----------------------------------<<" }
    }

    @EventHandler
    fun handleEvent(event: AppReadyEvent) {
        log.info { ">>-----------------------------------" }
        log.info { "Got event: $event" }
        log.info { LocalTime.now() }
        log.info { "-----------------------------------<<" }
    }

    data class AppReadyEvent(
        val time: LocalTime
    )
}

