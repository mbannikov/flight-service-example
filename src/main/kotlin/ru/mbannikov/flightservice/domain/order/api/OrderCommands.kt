package ru.mbannikov.flightservice.domain.order.api

import ru.mbannikov.flightservice.domain.Command
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import java.util.UUID

interface OrderCommand : Command

data class CreateOrderCommand(
    val tripId: String,
    val travelerId: UUID,
    val flightInfo: FlightInfo
) : OrderCommand

data class ConfirmOrderCommand(
    val orderId: String
) : OrderCommand

data class AuthorizeOrderCommand(
    val orderId: String,
    val authorizerId: UUID
) : OrderCommand

data class DoNotAuthorizeOrderCommand(
    val orderId: String,
    val authorizerId: UUID
) : OrderCommand

data class BookOrderCommand(
    val orderId: String
) : OrderCommand

data class PayOrderCommand(
    val orderId: String
) : OrderCommand
