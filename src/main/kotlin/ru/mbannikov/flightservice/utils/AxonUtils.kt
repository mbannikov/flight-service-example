package ru.mbannikov.flightservice.utils

import org.axonframework.modelling.command.AggregateLifecycle

object AxonUtils {
    fun Any.applyEvent() {
        AggregateLifecycle.apply(this)
    }
}