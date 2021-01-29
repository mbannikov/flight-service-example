package ru.mbannikov.flightservice.camunda.client.dto

interface CamundaValue<out ValueType> {
    val value: ValueType
    val type: String
}

data class StringValue(
    override val value: String
) : CamundaValue<String> {
    override val type: String = "String"
}

data class BooleanValue(
    override val value: Boolean
) : CamundaValue<Boolean> {
    override val type: String = "Boolean"
}
