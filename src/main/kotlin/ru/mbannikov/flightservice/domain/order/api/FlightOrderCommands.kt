package ru.mbannikov.flightservice.domain.order.api

//import org.axonframework.modelling.command.TargetAggregateIdentifier
import ru.mbannikov.flightservice.domain.order.model.FlightInfo
import java.util.UUID

annotation class TargetAggregateIdentifier

abstract class FlightOrderCommand(
    open val orderId: String
) : Command

data class CreateFlightOrderCommand(
    @TargetAggregateIdentifier
    override val orderId: String,
    val tripId: String,
    val travelerId: UUID,
    val flightInfo: FlightInfo
) : FlightOrderCommand(orderId)

data class ConfirmFlightOrderCommand(
    @TargetAggregateIdentifier
    override val orderId: String
) : FlightOrderCommand(orderId)

data class AuthorizeFlightOrderCommand(
    @TargetAggregateIdentifier
    override val orderId: String,
    val authorizerId: UUID
) : FlightOrderCommand(orderId)

data class DoNotAuthorizeFlightOrderCommand(
    @TargetAggregateIdentifier
    override val orderId: String,
    val authorizerId: UUID
) : FlightOrderCommand(orderId)

data class BookFlightOrderCommand(
    @TargetAggregateIdentifier
    override val orderId: String
) : FlightOrderCommand(orderId)

data class PayFlightOrderCommand(
    @TargetAggregateIdentifier
    override val orderId: String
) : FlightOrderCommand(orderId)
