package ru.mbannikov.flightservice.controller

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.mbannikov.flightservice.domain.order.api.AuthorizeFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.ConfirmFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.CreateFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.api.DoNotAuthorizeFlightOrderCommand
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import java.util.UUID
import kotlin.random.Random

@RestController
@RequestMapping("/api/flight/order")
class FlightOrderController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(): Map<String, String> {
        val command = CreateFlightOrderCommand(
            orderId = "F-00${Random.nextInt(1, 999999)}",
            tripId = "T-00${Random.nextInt(1, 999999)}",
            travelerId = UUID.randomUUID(),
            flightInfo = FlightInfo(
                id = "fffffffffffff",
                sourceRegion = "Moscow",
                destinationRegion = "Saint Petersburg",
                priceWhenChosen = 100500.toBigDecimal()
            )
        )
        val orderId = commandGateway.sendAndWait<String>(command)

        return mapOf<String, String>(
            "orderId" to orderId
        )
    }

    @PostMapping("{id}/confirm")
    fun confirmOrder(@PathVariable id: String) {
        val command = ConfirmFlightOrderCommand(orderId = id)
        commandGateway.sendAndWait<Any>(command)
    }

    @PostMapping("{id}/authorize")
    fun authorizeOrder(@PathVariable id: String) {
        val command = AuthorizeFlightOrderCommand(orderId = id, authorizerId = UUID.randomUUID())
        commandGateway.sendAndWait<Any>(command)
    }

    @PostMapping("{id}/doNotAuthorize")
    fun doNotAuthorizeOrder(@PathVariable id: String) {
        val command = DoNotAuthorizeFlightOrderCommand(orderId = id, authorizerId = UUID.randomUUID())
        commandGateway.sendAndWait<Any>(command)
    }
}