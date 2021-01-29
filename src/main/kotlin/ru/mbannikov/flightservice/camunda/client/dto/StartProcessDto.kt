package ru.mbannikov.flightservice.camunda.client.dto

/** Тело запроса в Camunda для старта процесса. */
data class StartProcessDto(

    /** Стартовые параметры процесса. */
    val variables: Map<String, CamundaValue<Any>> = emptyMap(),

    /** Идентификатор процесса. */
    val businessKey: String? = null
)
