package ru.mbannikov.flightservice.domain.order.model

enum class FlightOrderState {
    INITIAL,
    CONFIRMED,
    AUTHORIZED,
    NOT_AUTHORIZED,
    BOOKED,
    PAYED;
}