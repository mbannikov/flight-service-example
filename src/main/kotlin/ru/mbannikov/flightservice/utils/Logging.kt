package ru.mbannikov.flightservice.utils

import mu.KLogger
import mu.toKLogger
import org.slf4j.LoggerFactory

interface Logging {
    val log: KLogger
        get() = LoggerFactory.getLogger(this.javaClass).toKLogger()
}
