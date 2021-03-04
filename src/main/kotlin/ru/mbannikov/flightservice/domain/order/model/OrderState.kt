package ru.mbannikov.flightservice.domain.order.model

enum class OrderState {
    INITIAL,
    CONFIRMED,
    AUTHORIZED,
    NOT_AUTHORIZED,
    BOOKED,
    PAYED;
}