package ru.mbannikov.flightservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlightServiceApplication

fun main(args: Array<String>) {
    runApplication<FlightServiceApplication>(*args)
}
