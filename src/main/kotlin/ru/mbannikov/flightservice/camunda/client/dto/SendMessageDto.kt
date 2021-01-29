package ru.mbannikov.flightservice.camunda.client.dto

/** Тело запроса в Camunda для отправки сообщения. */
data class SendMessageDto(

    /** Идентификатор сообщения. */
    val messageName: String,

    /** Идентификатор процесса. */
    val businessKey: String,

    val processVariables: Map<String, CamundaValue<Any>> = emptyMap()
)
