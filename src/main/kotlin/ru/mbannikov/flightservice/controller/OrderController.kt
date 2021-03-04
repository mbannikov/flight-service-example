package ru.mbannikov.flightservice.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.mbannikov.flightservice.domain.order.api.AuthorizeOrderCommand
import ru.mbannikov.flightservice.domain.order.api.ConfirmOrderCommand
import ru.mbannikov.flightservice.domain.order.api.CreateOrderCommand
import ru.mbannikov.flightservice.domain.order.api.DoNotAuthorizeOrderCommand
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import ru.mbannikov.mescofe.cqrs.CommandGateway
import java.util.UUID
import kotlin.random.Random

@RestController
@RequestMapping("/api/flight/order")
class OrderController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(): Map<String, String> {
        val command = CreateOrderCommand(
            tripId = "T-00${Random.nextInt(1, 999999)}",
            travelerId = UUID.randomUUID(),
            flightInfo = FlightInfo(
                id = "fffffffffffff",
                sourceRegion = "Moscow",
                destinationRegion = "Saint Petersburg",
                priceWhenChosen = 100500.toBigDecimal()
            )
        )
//        val orderId = commandGateway.sendAndWait<String>(command)
        val orderId = commandGateway.send(command).let { "TODO: fix returning command result in mescofe" }

        return mapOf(
            "orderId" to orderId
        )
    }

    @PostMapping("{id}/confirm")
    fun confirmOrder(@PathVariable id: String) {
        val command = ConfirmOrderCommand(orderId = id)
        commandGateway.send(command)
    }

    @PostMapping("{id}/authorize")
    fun authorizeOrder(@PathVariable id: String) {
        val command = AuthorizeOrderCommand(orderId = id, authorizerId = UUID.randomUUID())
        commandGateway.send(command)
    }

    @PostMapping("{id}/doNotAuthorize")
    fun doNotAuthorizeOrder(@PathVariable id: String) {
        val command = DoNotAuthorizeOrderCommand(orderId = id, authorizerId = UUID.randomUUID())
        commandGateway.send(command)
    }
}