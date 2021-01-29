package ru.mbannikov.flightservice.camunda

import ru.mbannikov.flightservice.camunda.client.dto.BooleanValue
import ru.mbannikov.flightservice.camunda.client.dto.StringValue


object CamundaUtils {
    fun String.toValue() = StringValue(this)

    fun Boolean.toValue() = BooleanValue(this)
}
